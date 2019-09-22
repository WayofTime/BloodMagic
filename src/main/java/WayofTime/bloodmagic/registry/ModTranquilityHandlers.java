package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.incense.EnumTranquilityType;
import WayofTime.bloodmagic.incense.IncenseTranquilityRegistry;
import WayofTime.bloodmagic.incense.TranquilityStack;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.GrassBlock;

public class ModTranquilityHandlers {

    public static void init() {
        IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof LeavesBlock ? new TranquilityStack(EnumTranquilityType.PLANT, 1.0D) : null);
        IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof FireBlock ? new TranquilityStack(EnumTranquilityType.FIRE, 1.0D) : null);
        IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof GrassBlock ? new TranquilityStack(EnumTranquilityType.EARTHEN, 0.5D) : null);
        IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof LogBlock ? new TranquilityStack(EnumTranquilityType.TREE, 1.0D) : null);
        IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> BloodMagicAPI.INSTANCE.getValueManager().getTranquility().get(state));
    }
}
