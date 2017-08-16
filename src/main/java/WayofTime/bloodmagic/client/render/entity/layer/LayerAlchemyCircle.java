package WayofTime.bloodmagic.client.render.entity.layer;

import WayofTime.bloodmagic.entity.mob.EntityCorruptedSheep;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerAlchemyCircle<T extends EntityCorruptedSheep> implements LayerRenderer<T> {
    private static final ResourceLocation ARRAY_TEXTURE = new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png");

    float rotationspeed = 10;

    public LayerAlchemyCircle() {

    }

    @Override
    public void doRenderLayer(EntityCorruptedSheep demon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (demon.getCastTimer() <= 0) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        float rot = this.rotationspeed * (partialTicks + demon.ticksExisted);
        float secondaryRot = 0;

        float size = 3.0F;

        // Bind the texture to the circle
        Minecraft.getMinecraft().renderEngine.bindTexture(ARRAY_TEXTURE);

        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 1);
        GlStateManager.translate(0, 1.5, 0);
        GlStateManager.rotate(90.0f, 1, 0, 0);

        GlStateManager.pushMatrix();
//        GlStateManager.translate(0, 0.5f, 0);

        GlStateManager.pushMatrix();
        GlStateManager.rotate(rot, 0, 0, 1);
        GlStateManager.rotate(secondaryRot, 1, 0, 0);
        GlStateManager.rotate(secondaryRot * 0.45812f, 0, 0, 1);
        double var31 = 0.0D;
        double var33 = 1.0D;
        double var35 = 0;
        double var37 = 1;

        GlStateManager.color(1f, 1f, 1f, 1f);
        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        // wr.setBrightness(200);
        wr.pos(size / 2f, size / 2f, 0.0D).tex(var33, var37).endVertex();
        wr.pos(size / 2f, -size / 2f, 0.0D).tex(var33, var35).endVertex();
        wr.pos(-size / 2f, -size / 2f, 0.0D).tex(var31, var35).endVertex();
        wr.pos(-size / 2f, size / 2f, 0.0D).tex(var31, var37).endVertex();
        tessellator.draw();

        GlStateManager.popMatrix();

        GlStateManager.disableBlend();
        GlStateManager.enableCull();

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}