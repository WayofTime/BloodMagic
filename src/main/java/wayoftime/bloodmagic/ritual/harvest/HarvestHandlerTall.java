package wayoftime.bloodmagic.ritual.harvest;

import java.util.List;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

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
			HarvestRegistry.registerTallCrop(Blocks.SUGAR_CANE.defaultBlockState().setValue(SugarCaneBlock.AGE, i));
			HarvestRegistry.registerTallCrop(Blocks.CACTUS.defaultBlockState().setValue(CactusBlock.AGE, i));
		}
	}

	@Override
	public boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops)
	{
		BlockState up = world.getBlockState(pos.above());
		if (up.getBlock() == state.getBlock())
		{
			LootParams.Builder lootBuilder = new LootParams.Builder((ServerLevel) world);
			Vec3 blockCenter = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			List<ItemStack> blockDrops = state.getDrops(lootBuilder.withParameter(LootContextParams.ORIGIN, blockCenter).withParameter(LootContextParams.TOOL, mockHoe));
			drops.addAll(blockDrops);
			world.destroyBlock(pos.above(), false);
			return true;
		}

		return false;
	}

	@Override
	public boolean test(Level world, BlockPos pos, BlockState state)
	{
		return HarvestRegistry.getTallCrops().contains(state);
	}
}