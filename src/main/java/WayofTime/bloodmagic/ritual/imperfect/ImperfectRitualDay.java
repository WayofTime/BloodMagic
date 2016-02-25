package WayofTime.bloodmagic.ritual.imperfect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;

public class ImperfectRitualDay extends ImperfectRitual
{
    public ImperfectRitualDay()
    {
        super("day", new BlockStack(Blocks.gold_block), 5000, true, "ritual." + Constants.Mod.MODID + ".imperfect.day");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player)
    {

        if (!imperfectRitualStone.getRitualWorld().isRemote)
            imperfectRitualStone.getRitualWorld().setWorldTime((imperfectRitualStone.getRitualWorld().getWorldTime() / 24000) * 24000);

        return true;
    }
}
