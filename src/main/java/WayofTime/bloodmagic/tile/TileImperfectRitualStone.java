package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.tile.base.TileBase;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TileImperfectRitualStone extends TileBase implements IImperfectRitualStone {

    @Override
    public boolean performRitual(World world, BlockPos pos, @Nullable ImperfectRitual imperfectRitual, EntityPlayer player) {
        if (imperfectRitual != null && BloodMagic.RITUAL_MANAGER.enabled(BloodMagic.RITUAL_MANAGER.getId(imperfectRitual), true)) {
            if (!PlayerHelper.isFakePlayer(player) && !world.isRemote) {
                NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, SoulTicket.block(getWorld(), getPos(), imperfectRitual.getActivationCost()));
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
