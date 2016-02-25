package WayofTime.bloodmagic.ritual.imperfect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;

public class ImperfectRitualNight extends ImperfectRitual
{
    public ImperfectRitualNight()
    {
        super("night", new BlockStack(Blocks.lapis_block), 100, true, "ritual." + Constants.Mod.MODID + ".imperfect.night");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player)
    {

        if (!imperfectRitualStone.getRitualWorld().isRemote)
            imperfectRitualStone.getRitualWorld().setWorldTime((imperfectRitualStone.getRitualWorld().getWorldTime() / 24000) * 24000 + 13800);

        return true;
    }
}
