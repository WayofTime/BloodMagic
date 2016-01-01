package WayofTime.bloodmagic.ritual.imperfect;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

public class ImperfectRitualDay extends ImperfectRitual
{
    public ImperfectRitualDay()
    {
        super("day", new BlockStack(Blocks.gold_block), 5000, true);
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player)
    {

        if (!imperfectRitualStone.getWorld().isRemote)
            imperfectRitualStone.getWorld().setWorldTime((imperfectRitualStone.getWorld().getWorldTime() / 24000) * 24000);

        return true;
    }
}
