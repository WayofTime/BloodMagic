package wayoftime.bloodmagic.ritual.harvest;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.state.BlockState;

public class HarvestHandlerGlowberry implements IHarvestHandler
{
	@Override
	public boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops)
	{
		InteractionResult result = CaveVines.use(null, state, world, pos);
		return result.consumesAction();
	}

	@Override
	public boolean test(Level world, BlockPos pos, BlockState state)
	{
		if (state.getBlock() instanceof CaveVines)
		{
			return CaveVines.hasGlowBerries(state);
		}

		return false;
	}
}
