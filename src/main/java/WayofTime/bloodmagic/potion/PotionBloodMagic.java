package WayofTime.bloodmagic.potion;

import WayofTime.bloodmagic.BloodMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBloodMagic extends Effect {
    public static ResourceLocation texture = new ResourceLocation(BloodMagic.MODID, "textures/misc/potions.png");

    public PotionBloodMagic(String name, boolean badEffect, int potionColor, int iconIndexX, int iconIndexY) {
        super(badEffect, potionColor);
        this.setPotionName(name);
        this.setIconIndex(iconIndexX, iconIndexY);
    }

    @Override
    public boolean shouldRenderInvText(EffectInstance effect) {
        return true;
    }

    public EffectInstance apply(LivingEntity entity, int duration) {
        return apply(entity, duration, 0);
    }

    public EffectInstance apply(LivingEntity entity, int duration, int level) {
        EffectInstance effect = new EffectInstance(this, duration, level, false, false);
        entity.addPotionEffect(effect);
        return effect;
    }

    public int getLevel(LivingEntity entity) {
        EffectInstance effect = entity.getActivePotionEffect(this);
        if (effect != null) {
            return effect.getAmplifier();
        }
        return 0;
    }

    @Override
    public boolean shouldRender(EffectInstance effect) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        return super.getStatusIconIndex();
    }
}
