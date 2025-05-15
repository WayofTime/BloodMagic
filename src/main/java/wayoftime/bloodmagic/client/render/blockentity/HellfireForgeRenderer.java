package wayoftime.bloodmagic.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
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

    @Override
    public void render(HellfireForgeTile forge, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = forge.getLevel();
        renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.SOUTH), 0, 1, level, poseStack, bufferSource, packedLight, packedOverlay);
        renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.WEST), 0, 0, level, poseStack, bufferSource, packedLight, packedOverlay);
        renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.NORTH), 1, 0, level, poseStack, bufferSource, packedLight, packedOverlay);
        renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.EAST), 1, 1, level, poseStack, bufferSource, packedLight, packedOverlay);

        if (forge.inv.getStackInSlot(HellfireForgeTile.OUTPUT_SLOT).isEmpty()) {
            renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.GEM_SLOT), 0.5F, 0.5F, level, poseStack, bufferSource, packedLight, packedOverlay);
        } else {
            renderSlot(forge.inv.getStackInSlot(HellfireForgeTile.OUTPUT_SLOT), 0.5F, 0.5F, level, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }

    private void renderSlot(ItemStack itemStack, float x, float z, Level level, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();
        poseStack.translate(x, 1, z);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        BakedModel bakedModel = renderer.getModel(itemStack, level, null, 1);
        renderer.render(itemStack, ItemDisplayContext.FIXED, true, poseStack, bufferSource, packedLight, packedOverlay, bakedModel);
        poseStack.popPose();
    }
}
