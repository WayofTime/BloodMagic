package WayofTime.bloodmagic.ritual.types.imperfect;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

//@RitualRegister.Imperfect("day")
public class ImperfectRitualDay extends ImperfectRitual {
    public ImperfectRitualDay() {
        super("day", s -> s.getBlock() == Blocks.GOLD_BLOCK, 5000, true, "ritual." + BloodMagic.MODID + ".imperfect.day");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player) {

        if (!imperfectRitualStone.getRitualWorld().isRemote)
            imperfectRitualStone.getRitualWorld().setWorldTime((imperfectRitualStone.getRitualWorld().getWorldTime() / 24000) * 24000);

        return true;
    }
}
