package WayofTime.bloodmagic.ritual.imperfect;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.BlockStack;
import WayofTime.bloodmagic.ritual.data.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.data.imperfect.ImperfectRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class ImperfectRitualResistance extends ImperfectRitual {
    public ImperfectRitualResistance() {
        super("resistance", new BlockStack(Blocks.BEDROCK), 5000, "ritual." + BloodMagic.MODID + ".imperfect.resistance");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player) {

        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 1200, 1));

        return true;
    }
}
