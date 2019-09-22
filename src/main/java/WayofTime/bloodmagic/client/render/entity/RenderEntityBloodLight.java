package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

public class RenderEntityBloodLight extends EntityRenderer<EntityBloodLight> {
    private final ItemRenderer renderItem = Minecraft.getMinecraft().getRenderItem();

    public RenderEntityBloodLight(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void doRender(EntityBloodLight entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        this.renderItem.renderItem(ComponentTypes.REAGENT_BLOOD_LIGHT.getStack(), ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityBloodLight entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
