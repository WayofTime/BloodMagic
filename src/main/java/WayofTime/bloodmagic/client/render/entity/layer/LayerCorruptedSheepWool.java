package WayofTime.bloodmagic.client.render.entity.layer;

import WayofTime.bloodmagic.client.render.entity.RenderCorruptedSheep;
import WayofTime.bloodmagic.client.render.model.ModelCorruptedSheep;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedSheep;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerCorruptedSheepWool implements LayerRenderer<EntityCorruptedSheep> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final RenderCorruptedSheep sheepRenderer;
    private final ModelCorruptedSheep sheepModel = new ModelCorruptedSheep(1);

    public LayerCorruptedSheepWool(RenderCorruptedSheep renderCorruptedSheep) {
        this.sheepRenderer = renderCorruptedSheep;
    }

    public void doRenderLayer(EntityCorruptedSheep entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entitylivingbaseIn.getSheared() && !entitylivingbaseIn.isInvisible()) {
            this.sheepRenderer.bindTexture(TEXTURE);

            if (entitylivingbaseIn.hasCustomName() && "jeb_".equals(entitylivingbaseIn.getCustomNameTag())) {
                int i1 = 25;
                int i = entitylivingbaseIn.ticksExisted / 25 + entitylivingbaseIn.getEntityId();
                int j = EnumDyeColor.values().length;
                int k = i % j;
                int l = (i + 1) % j;
                float f = ((float) (entitylivingbaseIn.ticksExisted % 25) + partialTicks) / 25.0F;
                float[] afloat1 = EntityCorruptedSheep.getDyeRgb(EnumDyeColor.byMetadata(k));
                float[] afloat2 = EntityCorruptedSheep.getDyeRgb(EnumDyeColor.byMetadata(l));
                GlStateManager.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
            } else {
                float[] afloat = EntityCorruptedSheep.getDyeRgb(entitylivingbaseIn.getFleeceColor());
                GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            }

            this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
            this.sheepModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.sheepModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}