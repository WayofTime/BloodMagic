package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import lombok.NoArgsConstructor;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

@NoArgsConstructor
public class TileImperfectRitualStone extends TileEntity implements IImperfectRitualStone
{
    // IImperfectRitualStone

    @Override
    public boolean performRitual(World world, BlockPos pos, ImperfectRitual imperfectRitual, EntityPlayer player)
    {
        if (!PlayerHelper.isFakePlayer(player) && imperfectRitual != null && ImperfectRitualRegistry.ritualEnabled(imperfectRitual))
        {
            NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, imperfectRitual.getActivationCost());
            if (imperfectRitual.onActivate(this, player))
                if (imperfectRitual.isLightshow())
                    getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), getPos().getX(), getPos().getY() + 2, getPos().getZ()));
        }

        return false;
    }

    @Override
    public World getWorld()
    {
        return super.getWorld();
    }

    @Override
    public BlockPos getPos()
    {
        return super.getPos();
    }
}
