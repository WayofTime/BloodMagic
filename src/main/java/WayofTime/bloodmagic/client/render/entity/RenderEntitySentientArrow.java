package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEntitySentientArrow extends Render<EntitySentientArrow> {
    private static final ResourceLocation defaultTexture = new ResourceLocation("bloodmagic:textures/entities/soulArrow.png");
    private static final ResourceLocation corrosiveTexture = new ResourceLocation("bloodmagic:textures/entities/soulArrow_corrosive.png");
    private static final ResourceLocation vengefulTexture = new ResourceLocation("bloodmagic:textures/entities/soulArrow_vengeful.png");
    private static final ResourceLocation destructiveTexture = new ResourceLocation("bloodmagic:textures/entities/soulArrow_destructive.png");
    private static final ResourceLocation steadfastTexture = new ResourceLocation("bloodmagic:textures/entities/soulArrow_steadfast.png");

    public RenderEntitySentientArrow(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void doRender(EntitySentientArrow entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        int i = 0;
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = (float) (i * 10) / 32.0F;
        float f3 = (float) (5 + i * 10) / 32.0F;
        float f4 = 0.0F;
        float f5 = 0.15625F;
        float f6 = (float) (5 + i * 10) / 32.0F;
        float f7 = (float) (10 + i * 10) / 32.0F;
        float f8 = 0.05625F;
        GlStateManager.enableRescaleNormal();
        float f9 = (float) entity.arrowShake - partialTicks;

        if (f9 > 0.0F) {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(f8, f8, f8);
        GlStateManager.translate(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f8, 0.0F, 0.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex((double) f4, (double) f6).endVertex();
        worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex((double) f5, (double) f6).endVertex();
        worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex((double) f5, (double) f7).endVertex();
        worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex((double) f4, (double) f7).endVertex();
        tessellator.draw();
        GL11.glNormal3f(-f8, 0.0F, 0.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex((double) f4, (double) f6).endVertex();
        worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex((double) f5, (double) f6).endVertex();
        worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex((double) f5, (double) f7).endVertex();
        worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex((double) f4, (double) f7).endVertex();
        tessellator.draw();

        for (int j = 0; j < 4; ++j) {
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f8);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(-8.0D, -2.0D, 0.0D).tex((double) f, (double) f2).endVertex();
            worldrenderer.pos(8.0D, -2.0D, 0.0D).tex((double) f1, (double) f2).endVertex();
            worldrenderer.pos(8.0D, 2.0D, 0.0D).tex((double) f1, (double) f3).endVertex();
            worldrenderer.pos(-8.0D, 2.0D, 0.0D).tex((double) f, (double) f3).endVertex();
            tessellator.draw();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySentientArrow entity) {
        switch (entity.type) {
            case CORROSIVE:
                return corrosiveTexture;
            case DESTRUCTIVE:
                return destructiveTexture;
            case STEADFAST:
                return steadfastTexture;
            case VENGEFUL:
                return vengefulTexture;
            case DEFAULT:
            default:
                return defaultTexture;
        }
    }
}