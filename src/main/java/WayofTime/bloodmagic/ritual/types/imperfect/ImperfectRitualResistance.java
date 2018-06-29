package WayofTime.bloodmagic.ritual.types.imperfect;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.RitualRegister;
import WayofTime.bloodmagic.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

@RitualRegister.Imperfect("resistance")
public class ImperfectRitualResistance extends ImperfectRitual {
    public ImperfectRitualResistance() {
        super("resistance", s -> s.getBlock() == Blocks.BEDROCK, 5000, "ritual." + BloodMagic.MODID + ".imperfect.resistance");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player) {

        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 1200, 1));

        return true;
    }
}
