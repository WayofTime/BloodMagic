package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;

public abstract class RitualEffect
{
    public abstract void performEffect(TEMasterStone ritualStone);

    public abstract int getCostPerRefresh();

    public int getInitialCooldown()
    {
        return 0;
    }
}
