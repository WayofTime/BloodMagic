package wayoftime.bloodmagic.registry;

import net.minecraft.block.FireBlock;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tags.BlockTags;
import wayoftime.bloodmagic.api.impl.BloodMagicAPI;
import wayoftime.bloodmagic.incense.EnumTranquilityType;
import wayoftime.bloodmagic.incense.IncenseTranquilityRegistry;
import wayoftime.bloodmagic.incense.TranquilityStack;

public class ModTranquilityHandlers
{

	public static void init()
	{
		IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof LeavesBlock
				? new TranquilityStack(EnumTranquilityType.PLANT, 1.0D)
				: null);
		IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof FireBlock
				? new TranquilityStack(EnumTranquilityType.FIRE, 1.0D)
				: null);
		IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof GrassBlock
				? new TranquilityStack(EnumTranquilityType.EARTHEN, 0.5D)
				: null);
		IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> BlockTags.LOGS.contains(block)
				? new TranquilityStack(EnumTranquilityType.TREE, 1.0D)
				: null);
		IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> BloodMagicAPI.INSTANCE.getValueManager().getTranquility().get(state));
	}
}
