package wayoftime.bloodmagic.ritual.harvest;

import java.util.Collection;
import java.util.List;

import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

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
	public boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops)
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
					LootParams.Builder lootBuilder = new LootParams.Builder((ServerLevel) world);
					Vec3 blockCenter = new Vec3(cropPos.getX() + 0.5, cropPos.getY() + 0.5, cropPos.getZ() + 0.5);
					List<ItemStack> blockDrops = registeredCrop.getDrops(lootBuilder.withParameter(LootContextParams.ORIGIN, blockCenter).withParameter(LootContextParams.TOOL, mockHoe));
					drops.addAll(blockDrops);
					world.destroyBlock(cropPos, false);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean test(Level world, BlockPos pos, BlockState state)
	{
		return HarvestRegistry.getStemCrops().containsKey(state);
	}
}