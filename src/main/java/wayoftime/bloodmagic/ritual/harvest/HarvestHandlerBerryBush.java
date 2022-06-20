package wayoftime.bloodmagic.ritual.harvest;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HarvestHandlerBerryBush implements IHarvestHandler
{
	@Override
	public boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops)
	{
		if (test(world, pos, state))
		{
			int berries = 2 + world.random.nextInt(2);
			Block.popResource(world, pos, new ItemStack(Items.SWEET_BERRIES, berries));
			world.playSound((Player) null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
			world.setBlock(pos, state.setValue(SweetBerryBushBlock.AGE, Integer.valueOf(1)), 2);

			return true;
		}

		return false;
	}

	@Override
	public boolean test(Level world, BlockPos pos, BlockState state)
	{
		if (state.getBlock() instanceof SweetBerryBushBlock)
		{
			return state.getValue(SweetBerryBushBlock.AGE) >= 3;
		}

		return false;
	}
}
