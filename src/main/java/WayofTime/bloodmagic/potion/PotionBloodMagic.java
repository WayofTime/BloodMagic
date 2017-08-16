package WayofTime.bloodmagic.potion;

import WayofTime.bloodmagic.BloodMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBloodMagic extends Potion {
    public static ResourceLocation texture = new ResourceLocation(BloodMagic.MODID, "textures/misc/potions.png");

    public PotionBloodMagic(String name, boolean badEffect, int potionColor, int iconIndexX, int iconIndexY) {
        super(badEffect, potionColor);
        this.setPotionName(name);
        this.setIconIndex(iconIndexX, iconIndexY);
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return true;
    }

    public PotionEffect apply(EntityLivingBase entity, int duration) {
        return apply(entity, duration, 0);
    }

    public PotionEffect apply(EntityLivingBase entity, int duration, int level) {
        PotionEffect effect = new PotionEffect(this, duration, level, false, false);
        entity.addPotionEffect(effect);
        return effect;
    }

    public int getLevel(EntityLivingBase entity) {
        PotionEffect effect = entity.getActivePotionEffect(this);
        if (effect != null) {
            return effect.getAmplifier();
        }
        return 0;
    }

    @Override
    public boolean shouldRender(PotionEffect effect) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        return super.getStatusIconIndex();
    }
}
