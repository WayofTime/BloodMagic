package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.render.model.ModelMeteor;
import WayofTime.bloodmagic.entity.projectile.EntityMeteor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityMeteor extends Render<EntityMeteor> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(BloodMagic.MODID, "textures/models/Meteor.png");
    private static final float SCALE = 1;

    public ModelBase model = new ModelMeteor();

    public RenderEntityMeteor(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(EntityMeteor entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(SCALE, SCALE, SCALE);
        this.bindTexture(TEXTURE);
        model.render(entity, 0, (float) x, (float) y, (float) z, entityYaw, partialTicks);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMeteor entity) {
        return TEXTURE;
    }
}
