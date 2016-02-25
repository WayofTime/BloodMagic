package WayofTime.bloodmagic.ritual.imperfect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;

public class ImperfectRitualRain extends ImperfectRitual
{
    public ImperfectRitualRain()
    {
        super("rain", new BlockStack(Blocks.water), 5000, true, "ritual." + Constants.Mod.MODID + ".imperfect.rain");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player)
    {
        if (!imperfectRitualStone.getRitualWorld().isRemote)
        {
            imperfectRitualStone.getRitualWorld().getWorldInfo().setRaining(true);
        }

        if (imperfectRitualStone.getRitualWorld().isRemote)
        {
            imperfectRitualStone.getRitualWorld().setRainStrength(1.0F);
            imperfectRitualStone.getRitualWorld().setThunderStrength(1.0F);
        }

        return true;
    }
}
