package wayoftime.bloodmagic.ritual.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("felling")
public class RitualFelling extends Ritual
{
	public static final String FELLING_RANGE = "fellingRange";
	public static final String CHEST_RANGE = "chest";

	private ArrayList<BlockPos> treePartsCache;
	private Iterator<BlockPos> blockPosIterator;

	private boolean cached = false;
	private BlockPos currentPos;

	private static final ItemStack mockAxe = new ItemStack(Items.DIAMOND_AXE, 1);

	public RitualFelling()
	{
		super("ritualFelling", 0, 20000, "ritual." + BloodMagic.MODID + ".fellingRitual");
		addBlockRange(FELLING_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -3, -10), new BlockPos(11, 27, 11)));
		addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

		setMaximumVolumeAndDistanceOfRange(FELLING_RANGE, 14000, 15, 30);
		setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);

		treePartsCache = new ArrayList<>();
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		BlockPos masterPos = masterRitualStone.getMasterBlockPos();
		AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
		TileEntity tileInventory = world.getBlockEntity(chestRange.getContainedPositions(masterPos).get(0));

		if (world.isClientSide)
		{
			return;
		}

		if (tileInventory != null && Utils.getNumberOfFreeSlots(tileInventory, Direction.DOWN) < 1)
		{
			return;
		}

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		if (!cached || treePartsCache.isEmpty())
		{
			for (BlockPos blockPos : masterRitualStone.getBlockRange(FELLING_RANGE).getContainedPositions(masterRitualStone.getMasterBlockPos()))
			{
				if (!treePartsCache.contains(blockPos))
					if (!world.isEmptyBlock(blockPos) && (BlockTags.LOGS.contains(world.getBlockState(blockPos).getBlock()) || BlockTags.LEAVES.contains(world.getBlockState(blockPos).getBlock())))
					{
						treePartsCache.add(blockPos);
					}
			}

			cached = true;
			blockPosIterator = treePartsCache.iterator();
		}

		if (blockPosIterator.hasNext() && tileInventory != null)
		{
			masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
			currentPos = blockPosIterator.next();
			IItemHandler inventory = Utils.getInventory(tileInventory, Direction.DOWN);
			BlockState state = world.getBlockState(currentPos);
			placeInInventory(state, world, currentPos, inventory);

			BlockItemUseContext ctx = new BlockItemUseContext(world, null, Hand.MAIN_HAND, ItemStack.EMPTY, BlockRayTraceResult.miss(new Vector3d(0, 0, 0), Direction.UP, currentPos));
			spawnParticlesAndSound((ServerWorld) world, currentPos, state, ctx);

			world.setBlockAndUpdate(currentPos, Blocks.AIR.defaultBlockState());
			blockPosIterator.remove();
		}
	}

	public void spawnParticlesAndSound(ServerWorld world, BlockPos pos, BlockState state, BlockItemUseContext context)
	{
		SoundType soundtype = state.getSoundType(world, pos, context.getPlayer());
		world.playSound(context.getPlayer(), pos, state.getSoundType(world, pos, context.getPlayer()).getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

		BlockParticleData particleData = new BlockParticleData(ParticleTypes.BLOCK, state);

		world.sendParticles(particleData, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, 0.2, 0.2, 0.2, 0.03);
	}

	@Override
	public int getRefreshCost()
	{
		return 10;
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
		addCornerRunes(components, 1, 1, EnumRuneType.EARTH);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualFelling();
	}

	private void placeInInventory(BlockState choppedState, World world, BlockPos choppedPos, @Nullable IItemHandler inventory)
	{
		if (inventory == null)
			return;

		LootContext.Builder lootBuilder = new LootContext.Builder((ServerWorld) world);
		Vector3d blockCenter = new Vector3d(choppedPos.getX() + 0.5, choppedPos.getY() + 0.5, choppedPos.getZ() + 0.5);
		List<ItemStack> silkDrops = choppedState.getDrops(lootBuilder.withParameter(LootParameters.ORIGIN, blockCenter).withParameter(LootParameters.TOOL, mockAxe));

//		ItemHandlerHelper.insertItem(inventory, stack, simulate)
		for (ItemStack stack : silkDrops)
		{
			ItemStack remainder = ItemHandlerHelper.insertItem(inventory, stack, false);
			if (!remainder.isEmpty())
				world.addFreshEntity(new ItemEntity(world, choppedPos.getX() + 0.4, choppedPos.getY() + 2, choppedPos.getZ() + 0.4, remainder));
		}
	}
}
