package WayofTime.bloodmagic.ritual.types.imperfect;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.RitualRegister;
import WayofTime.bloodmagic.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;

@RitualRegister.Imperfect("resistance")
public class ImperfectRitualResistance extends ImperfectRitual {
    public ImperfectRitualResistance() {
        super("resistance", s -> s.getBlock() == Blocks.BEDROCK, 5000, "ritual." + BloodMagic.MODID + ".imperfect.resistance");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, PlayerEntity player) {

        player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 1200, 1));

        return true;
    }
}
