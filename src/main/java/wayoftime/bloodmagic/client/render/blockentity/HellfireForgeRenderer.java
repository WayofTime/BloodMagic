package wayoftime.bloodmagic.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.blockentity.HellfireForgeTile;

public class HellfireForgeRenderer implements BlockEntityRenderer<HellfireForgeTile> {

    public HellfireForgeRenderer(BlockEntityRendererProvider.Context context) {}

    private static final float CORNER_OFFSET = 1 + 3/16F;
    @Override
    public void render(HellfireForgeTile forge, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = forge.getLevel();
        renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.SOUTH), 1/16F, CORNER_OFFSET, 15/16F, level, poseStack, bufferSource, packedLight, packedOverlay);
        renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.WEST), 1/16F, CORNER_OFFSET, 1/16F, level, poseStack, bufferSource, packedLight, packedOverlay);
        renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.NORTH), 15/16F, CORNER_OFFSET, 1/16F, level, poseStack, bufferSource, packedLight, packedOverlay);
        renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.EAST), 15/16F, CORNER_OFFSET, 15/16F, level, poseStack, bufferSource, packedLight, packedOverlay);

        if (!forge.inv.getStackInSlot(HellfireForgeTile.GEM_SLOT).isEmpty()) {
            renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.GEM_SLOT), 0.5F, 1, 0.5F, level, poseStack, bufferSource, packedLight, packedOverlay);
        }
        if (!forge.inv.getStackInSlot(HellfireForgeTile.OUTPUT_SLOT).isEmpty()) {
            renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.OUTPUT_SLOT), 0.5F, 1.5F, 0.5F, level, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }

    private void renderSlot(ItemStack itemStack, float x, float y, float z, Level level, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        float rotation = (720.0F * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.scale(0.5F, 0.5F, 0.5F);
        BakedModel bakedModel = renderer.getModel(itemStack, level, null, 1);
        renderer.render(itemStack, ItemDisplayContext.FIXED, true, poseStack, bufferSource, packedLight, packedOverlay, bakedModel);
        poseStack.popPose();
    }
}
