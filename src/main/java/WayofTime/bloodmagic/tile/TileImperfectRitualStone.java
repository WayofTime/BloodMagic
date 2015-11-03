package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.api.util.helper.SoulNetworkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileImperfectRitualStone extends TileEntity implements IImperfectRitualStone {

    public TileImperfectRitualStone() {

    }

    // IImperfectRitualStone

    @Override
    public boolean performRitual(World world, BlockPos pos, ImperfectRitual imperfectRitual, EntityPlayer player) {

        if (imperfectRitual != null && ImperfectRitualRegistry.ritualEnabled(imperfectRitual)) {
            SoulNetworkHelper.getSoulNetwork(player.getDisplayNameString(), world).syphonAndDamage(imperfectRitual.getActivationCost());
            return imperfectRitual.onActivate(this, player);
        }

        return false;
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    @Override
    public BlockPos getPos() {
        return super.getPos();
    }
}
