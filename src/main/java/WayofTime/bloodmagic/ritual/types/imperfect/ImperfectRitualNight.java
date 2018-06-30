package WayofTime.bloodmagic.ritual.types.imperfect;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.RitualRegister;
import WayofTime.bloodmagic.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

@RitualRegister.Imperfect("night")
public class ImperfectRitualNight extends ImperfectRitual {
    public ImperfectRitualNight() {
        super("night", s -> s.getBlock() == Blocks.LAPIS_BLOCK, 100, true, "ritual." + BloodMagic.MODID + ".imperfect.night");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player) {

        if (!imperfectRitualStone.getRitualWorld().isRemote)
            imperfectRitualStone.getRitualWorld().setWorldTime((imperfectRitualStone.getRitualWorld().getWorldTime() / 24000) * 24000 + 13800);

        return true;
    }
}
