package WayofTime.bloodmagic.tile;

import lombok.NoArgsConstructor;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;

@NoArgsConstructor
public class TileImperfectRitualStone extends TileEntity implements IImperfectRitualStone
{
    // IImperfectRitualStone

    @Override
    public boolean performRitual(World world, BlockPos pos, ImperfectRitual imperfectRitual, EntityPlayer player)
    {
        if (imperfectRitual != null && ImperfectRitualRegistry.ritualEnabled(imperfectRitual))
        {
            if (!PlayerHelper.isFakePlayer(player) && !world.isRemote)
            {
                NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, imperfectRitual.getActivationCost());
                if (imperfectRitual.onActivate(this, player))
                {
                    if (imperfectRitual.isLightshow())
                        getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), getPos().getX(), getPos().getY() + 2, getPos().getZ()));
                    return true;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public World getRitualWorld()
    {
        return getWorld();
    }

    @Override
    public BlockPos getRitualPos()
    {
        return getPos();
    }
}
