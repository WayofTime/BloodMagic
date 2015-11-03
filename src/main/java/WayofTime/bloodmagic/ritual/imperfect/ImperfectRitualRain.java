package WayofTime.bloodmagic.ritual.imperfect;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

public class ImperfectRitualRain extends ImperfectRitual {

    public ImperfectRitualRain() {
        super("rain", new BlockStack(Blocks.water), 5000, true);
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player) {
        boolean retFlag = false;

        if (!imperfectRitualStone.getWorld().isRemote) {
            imperfectRitualStone.getWorld().addWeatherEffect(new EntityLightningBolt(imperfectRitualStone.getWorld(), imperfectRitualStone.getPos().getX(), imperfectRitualStone.getPos().getY() + 2, imperfectRitualStone.getPos().getZ()));
            imperfectRitualStone.getWorld().getWorldInfo().setRaining(true);
            retFlag = true;
        }

        if (imperfectRitualStone.getWorld().isRemote) {
            imperfectRitualStone.getWorld().setRainStrength(1.0F);
            imperfectRitualStone.getWorld().setThunderStrength(1.0F);
        }

        return retFlag;
    }
}
