package WayofTime.bloodmagic.client.render.entity.layer;

import WayofTime.bloodmagic.client.render.entity.RenderCorruptedSpider;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerCorruptedSpiderEyes implements LayerRenderer<EntityCorruptedSpider> {
    private static final ResourceLocation SPIDER_EYES = new ResourceLocation("textures/entity/spider_eyes.png");
    private final RenderCorruptedSpider spiderRenderer;

    public LayerCorruptedSpiderEyes(RenderCorruptedSpider spiderRendererIn) {
        this.spiderRenderer = spiderRendererIn;
    }

    public void doRenderLayer(EntityCorruptedSpider entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.spiderRenderer.bindTexture(SPIDER_EYES);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

        if (entitylivingbaseIn.isInvisible()) {
            GlStateManager.depthMask(false);
        } else {
            GlStateManager.depthMask(true);
        }

        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.spiderRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        i = entitylivingbaseIn.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
        this.spiderRenderer.setLightmap(entitylivingbaseIn);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}