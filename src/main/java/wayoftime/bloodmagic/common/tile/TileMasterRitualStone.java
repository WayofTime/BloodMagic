package wayoftime.bloodmagic.common.tile;

import com.google.common.base.Strings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.common.item.ItemActivationCrystal;
import wayoftime.bloodmagic.common.tile.base.TileTicking;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.event.RitualEvent;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumReaderBoundaries;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.*;
import wayoftime.bloodmagic.will.DemonWillHolder;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

public class TileMasterRitualStone extends TileTicking implements IMasterRitualStone
{
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

	public TileMasterRitualStone(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public TileMasterRitualStone(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.MASTER_RITUAL_STONE_TYPE.get(), pos, state);
	}

	@Override
	public void onUpdate()
	{
		if (getLevel().isClientSide)
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
				performRitual(getLevel(), getBlockPos());

			activeTime++;
		}
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		owner = tag.hasUUID("owner") ? tag.getUUID("owner") : null;
		if (owner != null)
			cachedNetwork = NetworkHelper.getSoulNetwork(owner);
		currentRitual = BloodMagic.RITUAL_MANAGER.getRitual(tag.getString(Constants.NBT.CURRENT_RITUAL));
		if (currentRitual != null)
		{
			CompoundTag ritualTag = tag.getCompound(Constants.NBT.CURRENT_RITUAL_TAG);
			if (!ritualTag.isEmpty())
			{
				currentRitual.readFromNBT(ritualTag);
			}
			addBlockRanges(currentRitual.getModableRangeMap());
			for (Entry<String, AreaDescriptor> entry : modableRangeMap.entrySet())
			{
				CompoundTag descriptorTag = ritualTag.getCompound(entry.getKey());
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
	public CompoundTag serialize(CompoundTag tag)
	{
		String ritualId = BloodMagic.RITUAL_MANAGER.getId(getCurrentRitual());
		if (owner != null)
			tag.putUUID("owner", owner);
		tag.putString(Constants.NBT.CURRENT_RITUAL, Strings.isNullOrEmpty(ritualId) ? "" : ritualId);
		if (currentRitual != null)
		{
			CompoundTag ritualTag = new CompoundTag();
			currentRitual.writeToNBT(ritualTag);
			for (Entry<String, AreaDescriptor> entry : modableRangeMap.entrySet())
			{
				CompoundTag descriptorTag = new CompoundTag();
				entry.getValue().writeToNBT(descriptorTag);
				ritualTag.put(entry.getKey(), descriptorTag);
			}
			tag.put(Constants.NBT.CURRENT_RITUAL_TAG, ritualTag);
		}
		tag.putBoolean(Constants.NBT.IS_RUNNING, isActive());
		tag.putInt(Constants.NBT.RUNTIME, getActiveTime());
		tag.putInt(Constants.NBT.DIRECTION, direction.get3DDataValue());
		tag.putBoolean(Constants.NBT.IS_REDSTONED, redstoned);

		for (EnumDemonWillType type : currentActiveWillConfig)
		{
			tag.putBoolean("EnumWill" + type, true);
		}

		return tag;
	}

	@Override
	public boolean activateRitual(ItemStack activationCrystal, @Nullable Player activator, Ritual ritual)
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
					if (!getLevel().isClientSide)
					{
						SoulNetwork network = NetworkHelper.getSoulNetwork(binding);

						if (!isRedstoned() && network.getCurrentEssence() < ritual.getActivationCost() && (activator != null && !activator.isCreative()))
						{
							activator.displayClientMessage(Component.translatable("chat.bloodmagic.ritual.weak"), true);
							return false;
						}

						if (currentRitual != null)
							currentRitual.stopRitual(this, Ritual.BreakType.ACTIVATE);

						RitualEvent.RitualActivatedEvent event = new RitualEvent.RitualActivatedEvent(this, binding.getOwnerId(), ritual, activator, activationCrystal, crystalLevel);

						if (MinecraftForge.EVENT_BUS.post(event))
						{
							if (activator != null)
								activator.displayClientMessage(Component.translatable("chat.bloodmagic.ritual.prevent"), true);
							return false;
						}

						if (ritual.activateRitual(this, activator, binding.getOwnerId()))
						{
							if (!isRedstoned() && (activator != null && !activator.isCreative()))
								network.syphon(ticket(ritual.getActivationCost()));

							if (activator != null)
								activator.displayClientMessage(Component.translatable("chat.bloodmagic.ritual.activate"), true);

							this.active = true;
							this.owner = binding.getOwnerId();
							this.cachedNetwork = network;
							this.currentRitual = ritual;

							if (!checkBlockRanges(ritual.getModableRangeMap()))
								addBlockRanges(ritual.getModableRangeMap());

							notifyUpdate();
							this.setChanged();
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
				activator.displayClientMessage(Component.translatable("chat.bloodmagic.ritual.notValid"), true);
		}

		return false;
	}

	@Override
	public void performRitual(Level world, BlockPos pos)
	{
		if (!world.isClientSide && getCurrentRitual() != null && BloodMagic.RITUAL_MANAGER.enabled(BloodMagic.RITUAL_MANAGER.getId(currentRitual), false))
		{
			if (RitualHelper.checkValidRitual(getLevel(), getBlockPos(), currentRitual, getDirection()))
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
		if (!getLevel().isClientSide && getCurrentRitual() != null)
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
	public Level getLevel()
	{
		return super.getLevel();
	}

	@Override
	public BlockPos getBlockPos()
	{
		return super.getBlockPos();
	}

	@Override
	public Level getWorldObj()
	{
		return getLevel();
	}

	@Override
	public BlockPos getMasterBlockPos()
	{
		return getBlockPos();
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
	public void provideInformationOfRitualToPlayer(Player player)
	{
		if (this.currentRitual != null)
		{
			ChatUtil.sendNoSpam(player, this.currentRitual.provideInformationOfRitualToPlayer(player));
		}
	}

	@Override
	public void provideInformationOfRangeToPlayer(Player player, String range)
	{
		if (this.currentRitual != null && this.currentRitual.getListOfRanges().contains(range))
		{
			ChatUtil.sendNoSpam(player, this.currentRitual.provideInformationOfRangeToPlayer(player, range));
		}
	}

	@Override
	public void setActiveWillConfig(Player player, List<EnumDemonWillType> typeList)
	{
		this.currentActiveWillConfig = typeList;
	}

	@Override
	public EnumReaderBoundaries setBlockRangeByBounds(Player player, String range, BlockPos offset1, BlockPos offset2)
	{
		AreaDescriptor descriptor = this.getBlockRange(range);
		DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(level, getMasterBlockPos());

		EnumReaderBoundaries modificationType = currentRitual.canBlockRangeBeModified(range, descriptor, this, offset1, offset2, holder);
		if (modificationType == EnumReaderBoundaries.SUCCESS)
			descriptor.modifyAreaByBlockPositions(offset1, offset2);

		this.setChanged();
		level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);

		return modificationType;
	}

	@Override
	public List<EnumDemonWillType> getActiveWillConfig()
	{
		return new ArrayList<>(currentActiveWillConfig);
	}

	@Override
	public void provideInformationOfWillConfigToPlayer(Player player, List<EnumDemonWillType> typeList)
	{
		// There is probably an easier way to make expanded chat messages
		if (typeList.size() >= 1)
		{
			Object[] translations = new Component[typeList.size()];
			StringBuilder constructedString = new StringBuilder("%s");

			for (int i = 1; i < typeList.size(); i++)
			{
				constructedString.append(", %s");
			}

			for (int i = 0; i < typeList.size(); i++)
			{
				translations[i] = Component.translatable("tooltip.bloodmagic.currentBaseType." + typeList.get(i).name.toLowerCase(Locale.ROOT));
			}

			ChatUtil.sendNoSpam(player, Component.translatable("ritual.bloodmagic.willConfig.set", Component.translatable(constructedString.toString(), translations)));
		} else
		{
			ChatUtil.sendNoSpam(player, Component.translatable("ritual.bloodmagic.willConfig.void"));
		}
	}

	public boolean isPowered()
	{
		if (inverted)
			return !getLevel().hasNeighborSignal(getBlockPos());

		return getLevel().hasNeighborSignal(getBlockPos());
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
