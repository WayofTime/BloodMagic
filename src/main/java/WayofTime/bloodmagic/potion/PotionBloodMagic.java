package WayofTime.bloodmagic.potion;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionBloodMagic extends Potion
{
    public PotionBloodMagic(String name, ResourceLocation texture, boolean badEffect, int potionColor, int iconIndexX, int iconIndexY)
    {
        super(texture, badEffect, potionColor);
        this.setPotionName(name);
        this.setIconIndex(iconIndexX, iconIndexY);
    }
}
