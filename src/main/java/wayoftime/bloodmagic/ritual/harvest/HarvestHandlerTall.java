package wayoftime.bloodmagic.ritual.harvest;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Harvest handler for crops that grow vertically such as Sugar Cane and Cactus.
 * <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerTallCrop(BlockState)}
 */
public class HarvestHandlerTall implements IHarvestHandler
{
	private static final ItemStack mockHoe = new ItemStack(Items.DIAMOND_HOE, 1);

	public HarvestHandlerTall()
	{
		for (int i = 0; i < 15; i++)
		{
			HarvestRegistry.registerTallCrop(Blocks.SUGAR_CANE.getDefaultState().with(SugarCaneBlock.AGE, i));
			HarvestRegistry.registerTallCrop(Blocks.CACTUS.getDefaultState().with(CactusBlock.AGE, i));
		}
	}

	@Override
	public boolean harvest(World world, BlockPos pos, BlockState state, List<ItemStack> drops)
	{
		BlockState up = world.getBlockState(pos.up());
		if (up.getBlock() == state.getBlock())
		{
			LootContext.Builder lootBuilder = new LootContext.Builder((ServerWorld) world);
			Vector3d blockCenter = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			List<ItemStack> blockDrops = state.getDrops(lootBuilder.withParameter(LootParameters.field_237457_g_, blockCenter).withParameter(LootParameters.TOOL, mockHoe));
			drops.addAll(blockDrops);
			world.destroyBlock(pos.up(), false);
			return true;
		}

		return false;
	}

	@Override
	public boolean test(World world, BlockPos pos, BlockState state)
	{
		return HarvestRegistry.getTallCrops().contains(state);
	}
}