package wayoftime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Strings;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.ItemActivationCrystal;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.event.RitualEvent;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumReaderBoundaries;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.tile.base.TileTicking;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.BindableHelper;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;
import wayoftime.bloodmagic.util.helper.RitualHelper;
import wayoftime.bloodmagic.will.DemonWillHolder;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

public class TileMasterRitualStone extends TileTicking implements IMasterRitualStone
{
	@ObjectHolder("bloodmagic:masterritualstone")
	public static TileEntityType<TileMasterRitualStone> TYPE;
	protected final Map<String, AreaDescriptor> modableRangeMap = new HashMap<>();
	private UUID owner;
	private SoulNetwork cachedNetwork;
	private boolean active;
	private boolean redstoned;
	private int activeTime;
	private int cooldown;
	private Ritual currentRitual;
	private Direction direction = Direction.NORTH;
	private boolean inverted;
	private List<EnumDemonWillType> currentActiveWillConfig = new ArrayList<>();

	public TileMasterRitualStone(TileEntityType<?> type)
	{
		super(type);
	}

	public TileMasterRitualStone()
	{
		this(TYPE);
	}

	@Override
	public void onUpdate()
	{
		if (getWorld().isRemote)
			return;

		if (isPowered() && isActive())
		{
			active = false;
			redstoned = true;
			stopRitual(Ritual.BreakType.REDSTONE);
			return;
		}

		if (!isActive() && !isPowered() && isRedstoned() && getCurrentRitual() != null)
		{
			active = true;
			ItemStack crystalStack = NBTHelper.checkNBT(ItemActivationCrystal.CrystalType.getStack(getCurrentRitual().getCrystalLevel()));
			BindableHelper.applyBinding(crystalStack, new Binding(owner, PlayerHelper.getUsernameFromUUID(owner)));
			activateRitual(crystalStack, null, getCurrentRitual());
			redstoned = false;
		}

		if (getCurrentRitual() != null && isActive())
		{
			if (activeTime % getCurrentRitual().getRefreshTime() == 0)
				performRitual(getWorld(), getPos());

			activeTime++;
		}
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		owner = tag.hasUniqueId("owner") ? tag.getUniqueId("owner") : null;
		if (owner != null)
			cachedNetwork = NetworkHelper.getSoulNetwork(owner);
		currentRitual = BloodMagic.RITUAL_MANAGER.getRitual(tag.getString(Constants.NBT.CURRENT_RITUAL));
		if (currentRitual != null)
		{
			CompoundNBT ritualTag = tag.getCompound(Constants.NBT.CURRENT_RITUAL_TAG);
			if (!ritualTag.isEmpty())
			{
				currentRitual.readFromNBT(ritualTag);
			}
			addBlockRanges(currentRitual.getModableRangeMap());
			for (Entry<String, AreaDescriptor> entry : modableRangeMap.entrySet())
			{
				CompoundNBT descriptorTag = ritualTag.getCompound(entry.getKey());
				entry.getValue().readFromNBT(descriptorTag);
//				ritualTag.put(entry.getKey(), descriptorTag);
			}
		}
		active = tag.getBoolean(Constants.NBT.IS_RUNNING);
		activeTime = tag.getInt(Constants.NBT.RUNTIME);
		direction = Direction.values()[tag.getInt(Constants.NBT.DIRECTION)];
		redstoned = tag.getBoolean(Constants.NBT.IS_REDSTONED);

		for (EnumDemonWillType type : EnumDemonWillType.values())
		{
			if (tag.getBoolean("EnumWill" + type))
			{
				currentActiveWillConfig.add(type);
			}
		}
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		String ritualId = BloodMagic.RITUAL_MANAGER.getId(getCurrentRitual());
		if (owner != null)
			tag.putUniqueId("owner", owner);
		tag.putString(Constants.NBT.CURRENT_RITUAL, Strings.isNullOrEmpty(ritualId) ? "" : ritualId);
		if (currentRitual != null)
		{
			CompoundNBT ritualTag = new CompoundNBT();
			currentRitual.writeToNBT(ritualTag);
			for (Entry<String, AreaDescriptor> entry : modableRangeMap.entrySet())
			{
				CompoundNBT descriptorTag = new CompoundNBT();
				entry.getValue().writeToNBT(descriptorTag);
				ritualTag.put(entry.getKey(), descriptorTag);
			}
			tag.put(Constants.NBT.CURRENT_RITUAL_TAG, ritualTag);
		}
		tag.putBoolean(Constants.NBT.IS_RUNNING, isActive());
		tag.putInt(Constants.NBT.RUNTIME, getActiveTime());
		tag.putInt(Constants.NBT.DIRECTION, direction.getIndex());
		tag.putBoolean(Constants.NBT.IS_REDSTONED, redstoned);

		for (EnumDemonWillType type : currentActiveWillConfig)
		{
			tag.putBoolean("EnumWill" + type, true);
		}

		return tag;
	}

	@Override
	public boolean activateRitual(ItemStack activationCrystal, @Nullable PlayerEntity activator, Ritual ritual)
	{
		if (PlayerHelper.isFakePlayer(activator))
			return false;

		Binding binding = ((IBindable) activationCrystal.getItem()).getBinding(activationCrystal);
		if (binding != null && ritual != null)
		{
			if (activationCrystal.getItem() instanceof ItemActivationCrystal)
			{
				int crystalLevel = ((ItemActivationCrystal) activationCrystal.getItem()).getCrystalLevel(activationCrystal);
				if (RitualHelper.canCrystalActivate(ritual, crystalLevel))
				{
					if (!getWorld().isRemote)
					{
						SoulNetwork network = NetworkHelper.getSoulNetwork(binding);

						if (!isRedstoned() && network.getCurrentEssence() < ritual.getActivationCost()
								&& (activator != null && !activator.isCreative()))
						{
							activator.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.ritual.weak"), true);
							return false;
						}

						if (currentRitual != null)
							currentRitual.stopRitual(this, Ritual.BreakType.ACTIVATE);

						RitualEvent.RitualActivatedEvent event = new RitualEvent.RitualActivatedEvent(this, binding.getOwnerId(), ritual, activator, activationCrystal, crystalLevel);

						if (MinecraftForge.EVENT_BUS.post(event))
						{
							if (activator != null)
								activator.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.ritual.prevent"), true);
							return false;
						}

						if (ritual.activateRitual(this, activator, binding.getOwnerId()))
						{
							if (!isRedstoned() && (activator != null && !activator.isCreative()))
								network.syphon(ticket(ritual.getActivationCost()));

							if (activator != null)
								activator.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.ritual.activate"), true);

							this.active = true;
							this.owner = binding.getOwnerId();
							this.cachedNetwork = network;
							this.currentRitual = ritual;

							if (!checkBlockRanges(ritual.getModableRangeMap()))
								addBlockRanges(ritual.getModableRangeMap());

							notifyUpdate();
							return true;
						}
					}

					notifyUpdate();
					return true;
				}
			}
		} else
		{
			if (activator != null)
				activator.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.ritual.notValid"), true);
		}

		return false;
	}

	@Override
	public void performRitual(World world, BlockPos pos)
	{
		if (!world.isRemote && getCurrentRitual() != null
				&& BloodMagic.RITUAL_MANAGER.enabled(BloodMagic.RITUAL_MANAGER.getId(currentRitual), false))
		{
			if (RitualHelper.checkValidRitual(getWorld(), getPos(), currentRitual, getDirection()))
			{
				Ritual ritual = getCurrentRitual();
				RitualEvent.RitualRunEvent event = new RitualEvent.RitualRunEvent(this, getOwner(), ritual);

				if (MinecraftForge.EVENT_BUS.post(event))
					return;

				if (!checkBlockRanges(getCurrentRitual().getModableRangeMap()))
					addBlockRanges(getCurrentRitual().getModableRangeMap());

				getCurrentRitual().performRitual(this);
			} else
			{
				stopRitual(Ritual.BreakType.BREAK_STONE);
			}
		}
	}

	@Override
	public void stopRitual(Ritual.BreakType breakType)
	{
		if (!getWorld().isRemote && getCurrentRitual() != null)
		{
			RitualEvent.RitualStopEvent event = new RitualEvent.RitualStopEvent(this, getOwner(), getCurrentRitual(), breakType);

			if (MinecraftForge.EVENT_BUS.post(event))
				return;

			getCurrentRitual().stopRitual(this, breakType);
			if (breakType != Ritual.BreakType.REDSTONE)
			{
				this.currentRitual = null;
				this.active = false;
				this.activeTime = 0;
			}
			notifyUpdate();
		}
	}

	@Override
	public int getCooldown()
	{
		return cooldown;
	}

	@Override
	public void setCooldown(int cooldown)
	{
		this.cooldown = cooldown;
	}

	@Override
	public Direction getDirection()
	{
		return direction;
	}

	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	@Override
	public boolean areTanksEmpty()
	{
		return false;
	}

	@Override
	public int getRunningTime()
	{
		return activeTime;
	}

	@Override
	public UUID getOwner()
	{
		return owner;
	}

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	@Override
	public SoulNetwork getOwnerNetwork()
	{
		return cachedNetwork;
	}

	@Override
	public World getWorld()
	{
		return super.getWorld();
	}

	@Override
	public BlockPos getPos()
	{
		return super.getPos();
	}

	@Override
	public World getWorldObj()
	{
		return getWorld();
	}

	@Override
	public BlockPos getBlockPos()
	{
		return getPos();
	}

	@Override
	public String getNextBlockRange(String range)
	{
		if (this.currentRitual != null)
		{
			return this.currentRitual.getNextBlockRange(range);
		}

		return "";
	}

	@Override
	public void provideInformationOfRitualToPlayer(PlayerEntity player)
	{
		if (this.currentRitual != null)
		{
			ChatUtil.sendNoSpam(player, this.currentRitual.provideInformationOfRitualToPlayer(player));
		}
	}

	@Override
	public void provideInformationOfRangeToPlayer(PlayerEntity player, String range)
	{
		if (this.currentRitual != null && this.currentRitual.getListOfRanges().contains(range))
		{
			ChatUtil.sendNoSpam(player, this.currentRitual.provideInformationOfRangeToPlayer(player, range));
		}
	}

	@Override
	public void setActiveWillConfig(PlayerEntity player, List<EnumDemonWillType> typeList)
	{
		this.currentActiveWillConfig = typeList;
	}

	@Override
	public EnumReaderBoundaries setBlockRangeByBounds(PlayerEntity player, String range, BlockPos offset1, BlockPos offset2)
	{
		AreaDescriptor descriptor = this.getBlockRange(range);
		DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(world, getBlockPos());

		EnumReaderBoundaries modificationType = currentRitual.canBlockRangeBeModified(range, descriptor, this, offset1, offset2, holder);
		if (modificationType == EnumReaderBoundaries.SUCCESS)
			descriptor.modifyAreaByBlockPositions(offset1, offset2);

		world.notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);

		return modificationType;
	}

	@Override
	public List<EnumDemonWillType> getActiveWillConfig()
	{
		return new ArrayList<>(currentActiveWillConfig);
	}

	@Override
	public void provideInformationOfWillConfigToPlayer(PlayerEntity player, List<EnumDemonWillType> typeList)
	{
		// There is probably an easier way to make expanded chat messages
		if (typeList.size() >= 1)
		{
			Object[] translations = new TranslationTextComponent[typeList.size()];
			StringBuilder constructedString = new StringBuilder("%s");

			for (int i = 1; i < typeList.size(); i++)
			{
				constructedString.append(", %s");
			}

			for (int i = 0; i < typeList.size(); i++)
			{
				translations[i] = new TranslationTextComponent("tooltip.bloodmagic.currentBaseType." + typeList.get(i).name.toLowerCase());
			}

			ChatUtil.sendNoSpam(player, new TranslationTextComponent("ritual.bloodmagic.willConfig.set", new TranslationTextComponent(constructedString.toString(), translations)));
		} else
		{
			ChatUtil.sendNoSpam(player, new TranslationTextComponent("ritual.bloodmagic.willConfig.void"));
		}
	}

	public boolean isPowered()
	{
		if (inverted)
			return !getWorld().isBlockPowered(getPos());

		return getWorld().isBlockPowered(getPos());
	}

	public SoulNetwork getCachedNetwork()
	{
		return cachedNetwork;
	}

	public void setCachedNetwork(SoulNetwork cachedNetwork)
	{
		this.cachedNetwork = cachedNetwork;
	}

	public boolean isActive()
	{
		return active;
	}

	@Override
	public void setActive(boolean active)
	{
		this.active = active;
	}

	public boolean isRedstoned()
	{
		return redstoned;
	}

	public void setRedstoned(boolean redstoned)
	{
		this.redstoned = redstoned;
	}

	public int getActiveTime()
	{
		return activeTime;
	}

	public void setActiveTime(int activeTime)
	{
		this.activeTime = activeTime;
	}

	public Ritual getCurrentRitual()
	{
		return currentRitual;
	}

	public void setCurrentRitual(Ritual currentRitual)
	{
		this.currentRitual = currentRitual;
	}

	public boolean isInverted()
	{
		return inverted;
	}

	public void setInverted(boolean inverted)
	{
		this.inverted = inverted;
	}

	public List<EnumDemonWillType> getCurrentActiveWillConfig()
	{
		return currentActiveWillConfig;
	}

	public void setCurrentActiveWillConfig(List<EnumDemonWillType> currentActiveWillConfig)
	{
		this.currentActiveWillConfig = currentActiveWillConfig;
	}

	/**
	 * Used to grab the range of a ritual for a given effect.
	 *
	 * @param range - Range that needs to be pulled.
	 * @return -
	 */
	public AreaDescriptor getBlockRange(String range)
	{
		if (modableRangeMap.containsKey(range))
		{
			return modableRangeMap.get(range);
		}

		return null;
	}

	@Override
	public void addBlockRange(String range, AreaDescriptor defaultRange)
	{
		modableRangeMap.putIfAbsent(range, defaultRange.copy());
	}

	@Override
	public void addBlockRanges(Map<String, AreaDescriptor> blockRanges)
	{
		for (Map.Entry<String, AreaDescriptor> entry : blockRanges.entrySet())
		{
			modableRangeMap.putIfAbsent(entry.getKey(), entry.getValue().copy());
		}
	}

	@Override
	public void setBlockRange(String range, AreaDescriptor defaultRange)
	{
		modableRangeMap.put(range, defaultRange.copy());
	}

	@Override
	public void setBlockRanges(Map<String, AreaDescriptor> blockRanges)
	{
		for (Map.Entry<String, AreaDescriptor> entry : blockRanges.entrySet())
		{
			modableRangeMap.put(entry.getKey(), entry.getValue().copy());
		}
	}

	public boolean checkBlockRanges(Map<String, AreaDescriptor> blockRanges)
	{
		for (Map.Entry<String, AreaDescriptor> entry : blockRanges.entrySet())
		{
			if (modableRangeMap.get(entry.getKey()) == null)
				return false;
		}
		return true;
	}

}
