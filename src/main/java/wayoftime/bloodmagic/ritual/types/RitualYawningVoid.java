package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.item.routing.IItemFilterProvider;
import wayoftime.bloodmagic.common.routing.IItemFilter;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("yawning_void")
public class RitualYawningVoid extends Ritual
{
	public static final String PLACEMENT_RANGE = "placementRange";
	public static final String QUARRY_RANGE = "quarryRange";
	public static final String CHEST_RANGE = "chest";

	public static double rawWillDrain = 0.05;
	public static double steadfastWillDrain = 0.05;
	public static double corrosiveWillDrain = 0.05;

	public static int defaultRefreshTime = 10;
	public int refreshTime = 10;

	public BlockPos lastPos; // An offset

	public RitualYawningVoid()
	{
		super("ritualYawningVoid", 0, 5000, "ritual." + BloodMagic.MODID + ".yawningVoidRitual");
		addBlockRange(PLACEMENT_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 1, -1), 3));
		addBlockRange(QUARRY_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, -3, -1), 3));
		addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

		setMaximumVolumeAndDistanceOfRange(PLACEMENT_RANGE, 50, 4, 4);
		setMaximumVolumeAndDistanceOfRange(QUARRY_RANGE, 0, 64, 32);
		setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();
//		Vector3d MRSpos = new Vector3d(masterRitualStone.getMasterBlockPos().getX(), masterRitualStone.getMasterBlockPos().getY(), masterRitualStone.getMasterBlockPos().getZ());
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getMasterBlockPos();

		List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

		double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
		double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
		double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);

		refreshTime = getRefreshTimeForRawWill(rawWill);

		boolean consumeRawWill = rawWill >= rawWillDrain && refreshTime != defaultRefreshTime;

		// If it passes the filter, we destroy it
		BlockPos replacement = pos;
		boolean replaceNonDestroyed = steadfastWill >= steadfastWillDrain;
		boolean destroy = !replaceNonDestroyed;
		boolean tryFilter = corrosiveWill >= corrosiveWillDrain;

		boolean consumeSteadfastWill = steadfastWill >= steadfastWillDrain;
		boolean consumeCorrosiveWill = corrosiveWill >= corrosiveWillDrain;

		// Set to use Steadfast Will, but not enough Will available, so stopping!
		if ((!consumeSteadfastWill && willConfig.contains(EnumDemonWillType.STEADFAST)) || (!consumeCorrosiveWill && willConfig.contains(EnumDemonWillType.CORROSIVE)))
		{
			return;
		}

		boolean replace = false;

		if (replaceNonDestroyed)
		{
			AreaDescriptor placementRange = masterRitualStone.getBlockRange(PLACEMENT_RANGE);

			for (BlockPos offset : placementRange.getContainedPositions(pos))
			{
				if (world.isAirBlock(offset))
				{
					replacement = offset;
					replace = true;
					break;
				}
			}
		}

		BlockState downState = world.getBlockState(pos.down());

		int maxBlockChecks = 100;
		int checks = 0;

		AreaDescriptor.Rectangle quarryRange = (AreaDescriptor.Rectangle) masterRitualStone.getBlockRange(QUARRY_RANGE);

		BlockPos minOffset = quarryRange.getMinimumOffset();
		BlockPos maxOffset = quarryRange.getMaximumOffset().add(-1, -1, -1);

		boolean isDone = false;

		IItemHandler inventory = null;
		boolean doFilter = false;

		if (tryFilter)
		{
			AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
			TileEntity tile = world.getTileEntity(chestRange.getContainedPositions(pos).get(0));

			if (tile != null)
			{
				inventory = Utils.getInventory(tile, null);
				if (inventory != null)
				{
					doFilter = true;
				}
			}
		}

		if (replace || destroy)
		{
			if (doFilter)
			{
				destroy = false;
			}

			int j = maxOffset.getY();
			int i = minOffset.getX();
			int k = minOffset.getZ();

			if (lastPos != null && !lastPos.equals(BlockPos.ZERO))
			{
				j = lastPos.getY();
				i = Math.min(maxOffset.getX(), Math.max(i, lastPos.getX()));
				k = Math.min(maxOffset.getZ(), Math.max(k, lastPos.getZ()));
			}

			while (j >= minOffset.getY())
			{
				while (i <= maxOffset.getX())
				{
					while (k <= maxOffset.getZ())
					{
						if (checks >= maxBlockChecks || isDone)
						{
							this.lastPos = new BlockPos(i, j, k);
							return;
						}
						checks++;
						BlockPos newPos = pos.add(i, j, k);
						BlockState state = world.getBlockState(newPos);

						if (!state.isAir(world, newPos))
						{
							ItemStack checkStack = new ItemStack(state.getBlock());

							if (doFilter)
							{
								for (int n = 0; n < inventory.getSlots(); n++)
								{
									ItemStack filterStack = inventory.getStackInSlot(n);
									if (filterStack.isEmpty() || !(filterStack.getItem() instanceof IItemFilterProvider))
									{
										continue;
									}

									IItemFilterProvider filterItem = (IItemFilterProvider) filterStack.getItem();
									IItemFilter filter = filterItem.getUninitializedItemFilter(filterStack);
									if (filter.doesStackPassFilter(checkStack))
									{
										destroy = true;
									}

									break;
								}
							}

							if (destroy)
							{
								world.setBlockState(newPos, Blocks.AIR.getDefaultState(), 3);
								masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
								k++;
								this.lastPos = new BlockPos(i, j, k);
								isDone = true;
								// Block wasn't protected
								consumeSteadfastWill = false;
							} else if (replace)
							{
								Utils.swapLocations(world, newPos, world, replacement);
								masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
								k++;
								this.lastPos = new BlockPos(i, j, k);
								isDone = true;
							} else
							{
								k++;
							}
						} else
						{
							k++;
						}
					}
					i++;
					k = minOffset.getZ();
				}
				j--;
				i = minOffset.getX();
			}

			j = maxOffset.getY();
			this.lastPos = new BlockPos(i, j, k);
		}

		if (isDone)
		{
			if (consumeRawWill)
			{
				WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawWillDrain, true);
			}

			if (consumeCorrosiveWill)
			{
				WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveWillDrain, true);
			}

			if (consumeSteadfastWill)
			{
				WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastWillDrain, true);
			}
		}
	}

	public void readFromNBT(CompoundNBT tag)
	{
		super.readFromNBT(tag);
		lastPos = new BlockPos(tag.getInt(Constants.NBT.X_COORD), tag.getInt(Constants.NBT.Y_COORD), tag.getInt(Constants.NBT.Z_COORD));
	}

	public void writeToNBT(CompoundNBT tag)
	{
		super.writeToNBT(tag);
		if (lastPos != null)
		{
			tag.putInt(Constants.NBT.X_COORD, lastPos.getX());
			tag.putInt(Constants.NBT.Y_COORD, lastPos.getY());
			tag.putInt(Constants.NBT.Z_COORD, lastPos.getZ());
		}
	}

	@Override
	public ITextComponent[] provideInformationOfRitualToPlayer(PlayerEntity player)
	{
		return new ITextComponent[] { new TranslationTextComponent(this.getTranslationKey() + ".info"),
				new TranslationTextComponent(this.getTranslationKey() + ".default.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".corrosive.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".steadfast.info") };
	}

	public int getRefreshTimeForRawWill(double rawWill)
	{
		if (rawWill >= rawWillDrain)
		{
			return Math.max(1, (int) (10 - rawWill / 10));
		}

		return defaultRefreshTime;
	}

	@Override
	public int getRefreshTime()
	{
		return refreshTime;
	}

	@Override
	public int getRefreshCost()
	{
		return 10;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addParallelRunes(components, 1, 0, EnumRuneType.BLANK);
		addParallelRunes(components, 4, 0, EnumRuneType.EARTH);
		addOffsetRunes(components, 1, 3, 0, EnumRuneType.WATER);
		addOffsetRunes(components, 3, 1, 0, EnumRuneType.WATER);
		addCornerRunes(components, 2, 0, EnumRuneType.DUSK);
		addParallelRunes(components, 3, 1, EnumRuneType.AIR);
		addParallelRunes(components, 2, 1, EnumRuneType.EARTH);
		addParallelRunes(components, 2, 2, EnumRuneType.EARTH);
		addParallelRunes(components, 3, 3, EnumRuneType.AIR);
		addCornerRunes(components, 2, 3, EnumRuneType.FIRE);
		addOffsetRunes(components, 2, 1, 3, EnumRuneType.FIRE);
		addOffsetRunes(components, 1, 2, 3, EnumRuneType.FIRE);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualYawningVoid();
	}
}
