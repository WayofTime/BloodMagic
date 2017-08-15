package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.BloodMagic;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.client.render.model.ModelMeteor;
import WayofTime.bloodmagic.entity.projectile.EntityMeteor;

public class RenderEntityMeteor extends Render<EntityMeteor>
{
    public ModelBase model = new ModelMeteor();
    private float scale = 1;
    private static final ResourceLocation resource = new ResourceLocation(BloodMagic.MODID, "textures/models/Meteor.png");

    public RenderEntityMeteor(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public void doRender(EntityMeteor entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
//        GlStateManager.pushMatrix();
//        GlStateManager.translate((float) x, (float) y, (float) z);
//        GlStateManager.enableRescaleNormal();
//        GlStateManager.scale(0.5F, 0.5F, 0.5F);
//        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
//        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//        this.renderItem.renderItem(ItemComponent.getStack(ItemComponent.REAGENT_BLOODLIGHT), ItemCameraTransforms.TransformType.GROUND);
//        GlStateManager.disableRescaleNormal();
//        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(scale, scale, scale);
        this.bindTexture(this.getEntityTexture(entity));
//        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(180.0f - entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 1.0F, 0.0F, 0.0f);
        model.render(entity, 0, (float) x, (float) y, (float) z, entityYaw, partialTicks);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMeteor entity)
    {
        return resource;
    }
}
