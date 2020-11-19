package wayoftime.bloodmagic.ritual.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BlockMasterRitualStone;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.IRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.api.will.EnumDemonWillType;

@RitualRegister("crushing")
public class RitualCrushing extends Ritual
{
	public static final String CRUSHING_RANGE = "crushingRange";
	public static final String CHEST_RANGE = "chest";

	public static double rawWillDrain = 0.05;
	public static double steadfastWillDrain = 0.2;
	public static double destructiveWillDrain = 0.2;
	public static double vengefulWillDrain = 0.2;

	public static Map<ItemStack, Integer> cuttingFluidLPMap = new HashMap<>();
	public static Map<ItemStack, Double> cuttingFluidWillMap = new HashMap<>();
	public static int defaultRefreshTime = 40;
	private FakePlayer fakePlayer;
	public int refreshTime = 40;

	private static final ItemStack mockPick = new ItemStack(Items.DIAMOND_PICKAXE, 1);
	static
	{
		mockPick.addEnchantment(Enchantments.SILK_TOUCH, 1);
	}

	public RitualCrushing()
	{
		super("ritualCrushing", 0, 5000, "ritual." + BloodMagic.MODID + ".crushingRitual");
		addBlockRange(CRUSHING_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, -3, -1), 3));
		addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

		setMaximumVolumeAndDistanceOfRange(CRUSHING_RANGE, 50, 10, 10);
		setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();
		if (world.isRemote)
		{
			return;
		}
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getBlockPos();
		AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
		TileEntity tile = world.getTileEntity(chestRange.getContainedPositions(pos).get(0));

		if (tile != null && Utils.getNumberOfFreeSlots(tile, Direction.DOWN) < 1)
		{
			return;
		}

		List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

		double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
		double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
		double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
		double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
		double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

		refreshTime = getRefreshTimeForRawWill(rawWill);

		boolean consumeRawWill = rawWill >= rawWillDrain && refreshTime != defaultRefreshTime;

		boolean isSilkTouch = steadfastWill >= steadfastWillDrain;
		boolean useCuttingFluid = corrosiveWill > 0;

		int fortune = destructiveWill > 0 ? 3 : 0;

		AreaDescriptor crushingRange = masterRitualStone.getBlockRange(CRUSHING_RANGE);
		boolean hasOperated = false;

		double rawDrain = 0;

		for (BlockPos newPos : crushingRange.getContainedPositions(pos))
		{
			if (world.isAirBlock(newPos))
			{
				continue;
			}

			BlockState state = world.getBlockState(newPos);
			Block block = state.getBlock();

			if (block instanceof BlockMasterRitualStone || block instanceof IRitualStone
					|| state.getBlockHardness(world, newPos) == -1.0F || Utils.isBlockLiquid(state))
			{
				continue;
			}

			boolean isBlockClaimed = false;
//			if (useCuttingFluid)
//			{
//				ItemStack checkStack = block.getItem(world, newPos, state);
//				if (checkStack.isEmpty())
//				{
//					continue;
//				}
//
//				ItemStack copyStack = checkStack.copy();
//
//				for (ICrushingHandler handler : CrushingRegistry.getCrushingHandlerList())
//				{
//					int lpDrain = handler.getLpDrain();
//					double willDrain = handler.getWillDrain();
//
//					if (corrosiveWill < willDrain || currentEssence < lpDrain + getRefreshCost())
//					{
//						continue;
//					}
//
//					ItemStack result = handler.getRecipeOutput(copyStack, world, pos);
//
//					if (result.isEmpty())
//					{
//						continue;
//					}
//
//					if (tile != null)
//					{
//						result = Utils.insertStackIntoTile(result, tile, Direction.DOWN);
//						if (!result.isEmpty())
//						{
//							Utils.spawnStackAtBlock(world, pos, Direction.UP, result);
//						}
//					} else
//					{
//						Utils.spawnStackAtBlock(world, pos, Direction.UP, result);
//					}
//
//					WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, willDrain, true);
//					corrosiveWill -= willDrain;
//
//					masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(lpDrain));
//					currentEssence -= lpDrain;
//
//					isBlockClaimed = true;
//				}
//
//			}

			Blocks d;
			if (!isBlockClaimed && isSilkTouch)
			{
				LootContext.Builder lootBuilder = new LootContext.Builder((ServerWorld) world);
				Vector3d blockCenter = new Vector3d(newPos.getX() + 0.5, newPos.getY() + 0.5, newPos.getZ() + 0.5);
				List<ItemStack> silkDrops = state.getDrops(lootBuilder.withParameter(LootParameters.field_237457_g_, blockCenter).withParameter(LootParameters.TOOL, mockPick));

				for (ItemStack item : silkDrops)
				{
					ItemStack copyStack = item.copy();

					if (tile != null)
					{
						copyStack = Utils.insertStackIntoTile(copyStack, tile, Direction.DOWN);
					} else
					{
						Utils.spawnStackAtBlock(world, pos, Direction.UP, copyStack);
						continue;
					}
					if (!copyStack.isEmpty())
					{
						Utils.spawnStackAtBlock(world, pos, Direction.UP, copyStack);
					}
				}

				if (steadfastWill >= steadfastWillDrain)
				{
					WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastWillDrain, true);
					steadfastWill -= steadfastWillDrain;
				} else
				{
					continue;
				}

			} else if (!isBlockClaimed)
			{
				if (fortune > 0 && destructiveWill < destructiveWillDrain)
				{
					fortune = 0;
				}

				ItemStack mockFortunePick = new ItemStack(Items.DIAMOND_PICKAXE, 1);
				mockFortunePick.addEnchantment(Enchantments.FORTUNE, fortune);

				LootContext.Builder lootBuilder = new LootContext.Builder((ServerWorld) world);
				Vector3d blockCenter = new Vector3d(newPos.getX() + 0.5, newPos.getY() + 0.5, newPos.getZ() + 0.5);
				List<ItemStack> stackList = state.getDrops(lootBuilder.withParameter(LootParameters.field_237457_g_, blockCenter).withParameter(LootParameters.TOOL, mockFortunePick));
//				List<ItemStack> stackList = Block.getDrops(state, world, newPos, world.getTileEntity(newPos));

//				List<ItemStack> stackList = block.getDrops(world, newPos, state, fortune);

				for (ItemStack item : stackList)
				{
					ItemStack copyStack = item.copy();

					if (tile != null)
					{
						copyStack = Utils.insertStackIntoTile(copyStack, tile, Direction.DOWN);
					} else
					{
						Utils.spawnStackAtBlock(world, pos, Direction.UP, copyStack);
						continue;
					}
					if (!copyStack.isEmpty())
					{
						Utils.spawnStackAtBlock(world, pos, Direction.UP, copyStack);
					}
				}

				if (fortune > 0)
				{
					WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DESTRUCTIVE, destructiveWillDrain, true);
					destructiveWill -= destructiveWillDrain;
				}
			}

			world.destroyBlock(newPos, false);
			masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
			hasOperated = true;

			if (consumeRawWill)
			{
				rawDrain += rawWillDrain;
				rawWill -= rawWillDrain;
			}

			break;
		}

//		if (hasOperated && tile != null && vengefulWill >= vengefulWillDrain)
//		{
//			Pair<ItemStack, Boolean> pair = CompressionRegistry.compressInventory(tile, world);
//			if (pair.getRight())
//			{
//				ItemStack returned = pair.getLeft();
//				if (returned != null)
//				{
//					Utils.spawnStackAtBlock(world, pos, Direction.UP, returned);
//				}
//
//				WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulWillDrain, true);
//			}
//		}

		if (rawDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawDrain, true);
		}
	}

	public int getRefreshTimeForRawWill(double rawWill)
	{
		if (rawWill >= rawWillDrain)
		{
			return Math.max(1, (int) (40 - rawWill / 5));
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
		return 7;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addParallelRunes(components, 1, 0, EnumRuneType.EARTH);
		addParallelRunes(components, 2, 0, EnumRuneType.FIRE);
		addCornerRunes(components, 2, 0, EnumRuneType.DUSK);
		addParallelRunes(components, 2, 1, EnumRuneType.AIR);
	}

	@Override
	public ITextComponent[] provideInformationOfRitualToPlayer(PlayerEntity player)
	{
		return new ITextComponent[]
		{ new TranslationTextComponent(this.getTranslationKey() + ".info"),
				new TranslationTextComponent(this.getTranslationKey() + ".default.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".corrosive.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".steadfast.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".destructive.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".vengeful.info") };
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualCrushing();
	}

	public static void registerCuttingFluid(ItemStack stack, int lpDrain, double willDrain)
	{
		cuttingFluidLPMap.put(stack, lpDrain);
		cuttingFluidWillMap.put(stack, willDrain);
	}

	private FakePlayer getFakePlayer(ServerWorld world)
	{
		return fakePlayer == null
				? fakePlayer = FakePlayerFactory.get(world, new GameProfile(null, BloodMagic.MODID + "_ritual_crushing"))
				: fakePlayer;
	}
}
