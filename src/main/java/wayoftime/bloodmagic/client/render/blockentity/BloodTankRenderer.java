package wayoftime.bloodmagic.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.blockentity.BloodTankTile;
import wayoftime.bloodmagic.util.helper.RenderHelper;

public class BloodTankRenderer implements BlockEntityRenderer<BloodTankTile> {
    public BloodTankRenderer(BlockEntityRendererProvider.Context context) {}

    // Tank inside
    private static final float minHeight = 1F/16F + 0.01F;
    private static final float maxHeight = 11F/16F;
    private static final float start = 4F/16F; // inside corner
    private static final float end = 12F/16F; // other inside corner

    @Override
    public void render(BloodTankTile blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Minecraft minecraft = Minecraft.getInstance();
        FluidStack fluidStack = blockEntity.getFluidContained();
        if (fluidStack.isEmpty()) {
            return;
        }
        IClientFluidTypeExtensions fluidClientInfo = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        FluidState fluidState = fluidStack.getFluid().defaultFluidState();
        RenderType blockRenderType = ItemBlockRenderTypes.getRenderLayer(fluidState);
        TextureAtlasSprite sprite = minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidClientInfo.getStillTexture());
        int color = fluidClientInfo.getTintColor();

        poseStack.pushPose();

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderTypeHelper.getEntityRenderType(blockRenderType, false));
        float capacityHeight = maxHeight - minHeight;
        float height = ((float) fluidStack.getAmount() / (float) blockEntity.getCapacity()) * capacityHeight;

        RenderHelper.addCubeAll(vertexConsumer, poseStack, sprite, color, packedLight, packedOverlay, start, minHeight, start, end, height, end);

        poseStack.popPose();
    }
}
