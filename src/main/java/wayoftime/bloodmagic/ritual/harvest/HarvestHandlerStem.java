package wayoftime.bloodmagic.ritual.harvest;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Harvest handler for crops with stems such as Pumpkins and Melons. Rotation
 * based crop blocks are a good reason to use this (see pumpkins). <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerStemCrop(BlockState, BlockState)}
 */
public class HarvestHandlerStem implements IHarvestHandler
{
	private static final ItemStack mockHoe = new ItemStack(Items.DIAMOND_HOE, 1);

	public HarvestHandlerStem()
	{
		for (int i = 0; i < 4; i++)
		{
			Direction facing = Direction.from2DDataValue(i);
			HarvestRegistry.registerStemCrop(Blocks.PUMPKIN.defaultBlockState(), Blocks.ATTACHED_PUMPKIN_STEM.defaultBlockState().setValue(AttachedStemBlock.FACING, facing));
			HarvestRegistry.registerStemCrop(Blocks.MELON.defaultBlockState(), Blocks.ATTACHED_MELON_STEM.defaultBlockState().setValue(AttachedStemBlock.FACING, facing));
		}
	}

	@Override
	public boolean harvest(World world, BlockPos pos, BlockState state, List<ItemStack> drops)
	{
		Direction cropDir = state.getValue(AttachedStemBlock.FACING);

		if (cropDir != Direction.UP)
		{
			BlockPos cropPos = pos.relative(cropDir);
			BlockState probableCrop = world.getBlockState(cropPos);
			Collection<BlockState> registeredCrops = HarvestRegistry.getStemCrops().get(state);

			for (BlockState registeredCrop : registeredCrops)
			{
				if (registeredCrop == probableCrop)
				{
					LootContext.Builder lootBuilder = new LootContext.Builder((ServerWorld) world);
					Vector3d blockCenter = new Vector3d(cropPos.getX() + 0.5, cropPos.getY() + 0.5, cropPos.getZ() + 0.5);
					List<ItemStack> blockDrops = registeredCrop.getDrops(lootBuilder.withParameter(LootParameters.ORIGIN, blockCenter).withParameter(LootParameters.TOOL, mockHoe));
					drops.addAll(blockDrops);
					world.destroyBlock(cropPos, false);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean test(World world, BlockPos pos, BlockState state)
	{
		return HarvestRegistry.getStemCrops().containsKey(state);
	}
}