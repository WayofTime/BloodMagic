package wayoftime.bloodmagic.ritual.harvest;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class HarvestHandlerNetherWart implements IHarvestHandler
{
	private static final ItemStack mockHoe = new ItemStack(Items.DIAMOND_HOE, 1);

	@Override
	public boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops)
	{
		boolean foundSeed = false;
		LootParams.Builder lootBuilder = new LootParams.Builder((ServerLevel) world);
		Vec3 blockCenter = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		List<ItemStack> blockDrops = state.getDrops(lootBuilder.withParameter(LootContextParams.ORIGIN, blockCenter).withParameter(LootContextParams.TOOL, mockHoe));

		for (ItemStack stack : blockDrops)
		{
			if (stack.isEmpty())
				continue;

			// This hurts my soul.
			if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() == state.getBlock())
			{
				stack.shrink(1);
				foundSeed = true;
				break;
			}
		}

		if (foundSeed)
		{
			world.setBlockAndUpdate(pos, state.getBlock().defaultBlockState());
			world.levelEvent(2001, pos, Block.getId(state));
			for (ItemStack stack : blockDrops)
			{
				if (stack.isEmpty())
					continue;

				drops.add(stack);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean test(Level world, BlockPos pos, BlockState state)
	{
		if (state.getBlock() instanceof NetherWartBlock)
		{
			return state.getValue(NetherWartBlock.AGE) >= 3;
		}

		return false;
	}
}
