package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.incense.EnumTranquilityType;
import WayofTime.bloodmagic.incense.IncenseTranquilityRegistry;
import WayofTime.bloodmagic.incense.TranquilityStack;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;

public class ModTranquilityHandlers {

    public static void init() {
        IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof BlockLeaves ? new TranquilityStack(EnumTranquilityType.PLANT, 1.0D) : null);
        IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof BlockFire ? new TranquilityStack(EnumTranquilityType.FIRE, 1.0D) : null);
        IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof BlockGrass ? new TranquilityStack(EnumTranquilityType.EARTHEN, 0.5D) : null);
        IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> block instanceof BlockLog ? new TranquilityStack(EnumTranquilityType.TREE, 1.0D) : null);
        IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> BloodMagicAPI.INSTANCE.getValueManager().getTranquility().get(state));
    }
}
