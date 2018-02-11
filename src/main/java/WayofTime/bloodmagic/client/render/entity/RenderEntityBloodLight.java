package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class RenderEntityBloodLight extends Render<EntityBloodLight> {
    private final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

    public RenderEntityBloodLight(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void doRender(EntityBloodLight entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.renderItem.renderItem(ComponentTypes.REAGENT_BLOOD_LIGHT.getStack(), ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityBloodLight entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
