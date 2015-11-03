package WayofTime.bloodmagic.ritual.imperfect;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

public class ImperfectRitualNight extends ImperfectRitual {

    public ImperfectRitualNight() {
        super("night", new BlockStack(Blocks.lapis_block), 5000, true);
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player) {

        boolean retFlag = false;

        if (!imperfectRitualStone.getWorld().isRemote) {
            imperfectRitualStone.getWorld().addWeatherEffect(new EntityLightningBolt(imperfectRitualStone.getWorld(), imperfectRitualStone.getPos().getX(), imperfectRitualStone.getPos().getY() + 2, imperfectRitualStone.getPos().getZ()));
            imperfectRitualStone.getWorld().setWorldTime((imperfectRitualStone.getWorld().getWorldTime() / 24000) * 24000 + 13800);
            retFlag = true;
        }

        return retFlag;
    }
}
