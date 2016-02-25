package WayofTime.bloodmagic.ritual.imperfect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;

public class ImperfectRitualResistance extends ImperfectRitual
{
    public ImperfectRitualResistance()
    {
        super("resistance", new BlockStack(Blocks.bedrock), 5000, "ritual." + Constants.Mod.MODID + ".imperfect.resistance");
    }

    @Override
    public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player)
    {

        player.addPotionEffect(new PotionEffect(Potion.resistance.id, 1200, 1));

        return true;
    }
}
