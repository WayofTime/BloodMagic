package wayoftime.bloodmagic.client.render.entity.layer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.living.LivingEffectComponents;
import wayoftime.bloodmagic.common.living.LivingHelper;

public class LivingElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {

    public static final ResourceLocation TEXTURE = BloodMagic.rl("textures/entity/living_elytra.png");
    public LivingElytraLayer(RenderLayerParent renderer, EntityModelSet modelSet) {
        super(renderer, modelSet);
    }

    @Override
    public boolean shouldRender(ItemStack stack, T entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        if (LivingHelper.isNeverValid(stack)) {
            return false;
        }

        return LivingHelper.hasFullSet(player) && LivingHelper.has(stack, LivingEffectComponents.ELYTRA.get());
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, T entity) {
        return TEXTURE;
    }
}
