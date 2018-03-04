package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualRegistry;
import WayofTime.bloodmagic.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import WayofTime.bloodmagic.tile.base.TileBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TileImperfectRitualStone extends TileBase implements IImperfectRitualStone {

    @Override
    public boolean performRitual(World world, BlockPos pos, @Nullable ImperfectRitual imperfectRitual, EntityPlayer player) {
        if (imperfectRitual != null && ImperfectRitualRegistry.ritualEnabled(imperfectRitual)) {
            if (!PlayerHelper.isFakePlayer(player) && !world.isRemote) {
                NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, imperfectRitual.getActivationCost());
                if (imperfectRitual.onActivate(this, player)) {
                    if (imperfectRitual.isLightShow())
                        getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), getPos().getX(), getPos().getY() + 2, getPos().getZ(), true));
                    return true;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public World getRitualWorld() {
        return getWorld();
    }

    @Override
    public BlockPos getRitualPos() {
        return getPos();
    }
}
