package wayoftime.bloodmagic.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.RoutingNodeBlock;
import wayoftime.bloodmagic.common.blockentity.RoutingNodeTile;
import wayoftime.bloodmagic.common.item.BMItems;

public class NodeRenderer implements BlockEntityRenderer<RoutingNodeTile> {

    public static final ResourceLocation DEFAULT = BloodMagic.rl("textures/block/crystal_default.png");
    public static final ResourceLocation CORROSIVE = BloodMagic.rl("textures/block/crystal_corrosive.png");
    public static final ResourceLocation DESTRUCTIVE = BloodMagic.rl("textures/block/crystal_destructive.png");
    public static final ResourceLocation STEADFAST = BloodMagic.rl("textures/block/crystal_steadfast.png");
    public static final ResourceLocation VENGEFUL = BloodMagic.rl("textures/block/crystal_vengeful.png");
    public NodeRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(RoutingNodeTile node, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockPos parentPos = node.getParentPos();
        if (parentPos == BlockPos.ZERO) {
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) { // what are we even doing lmao
            return;
        }
        boolean shouldRenderLine = BloodMagic.CLIENT_CONFIG.ALWAYS_RENDER_NODE_LINES.get() // config override
                || node.getBlockState().getValue(RoutingNodeBlock.RENDER_LINE) // node override
                || player.getMainHandItem().is(BMItems.NODE_ROUTER) // has router in mainhand -> maybe should be a tag and also check curio? routing glasses, anyone?
                || player.getOffhandItem().is(BMItems.NODE_ROUTER); // has router in offhand

        if (!shouldRenderLine) {
            return;
        }

        long gameTime = node.getLevel().getGameTime();
        float time = (float)Math.floorMod(gameTime, 40) + partialTick;

        BlockPos offsetPos = parentPos.subtract(node.getBlockPos());
        int xd = offsetPos.getX();
        int yd = offsetPos.getY();
        int zd = offsetPos.getZ();

        double distance = Math.sqrt(xd * xd + yd * yd + zd * zd);
        double subLength = Math.sqrt(xd * xd + zd * zd);
        float rotYaw = -((float) (Math.atan2(xd, zd) * 180.0D / Math.PI));
        float rotPitch = ((float) (Math.atan2(yd, subLength) * 180.0D / Math.PI));

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotYaw));
        poseStack.mulPose(Axis.XN.rotationDegrees(rotPitch - 90));

        ResourceLocation kind = switch (node.getBlockState().getValue(RoutingNodeBlock.WILL)) {
            case DEFAULT -> DEFAULT;
            case CORROSIVE -> CORROSIVE;
            case DESTRUCTIVE -> DESTRUCTIVE;
            case STEADFAST -> STEADFAST;
            case VENGEFUL -> VENGEFUL;
        };

        renderBeaconBeam(poseStack, bufferSource, kind, time, (float) distance, 0.06f, 0.1f, node.getBlockState().getValue(RoutingNodeBlock.ENABLED));
        poseStack.popPose();
    }

    public static void renderBeaconBeam(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            ResourceLocation beamLocation,
            float time,
            float height,
            float beamRadius,
            float glowRadius,
            boolean isConnected
    ) {
        poseStack.pushPose();
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(time * 2.25F - 45.0F));
        renderPart(
                poseStack,
                bufferSource.getBuffer(RenderType.beaconBeam(beamLocation, false)),
                0,
                height,
                0.0F,
                beamRadius,
                beamRadius,
                0.0F,
                -beamRadius,
                0.0F,
                0.0F,
                -beamRadius,
                isConnected
        );
        poseStack.popPose();
        renderPart(
                poseStack,
                bufferSource.getBuffer(RenderType.beaconBeam(beamLocation, true)),
                0,
                height,
                -glowRadius,
                -glowRadius,
                glowRadius,
                -glowRadius,
                -glowRadius,
                glowRadius,
                glowRadius,
                glowRadius,
                isConnected
        );
        poseStack.popPose();
    }
    private static void renderPart(
            PoseStack poseStack,
            VertexConsumer consumer,
            int minY,
            float maxY,
            float x1,
            float z1,
            float x2,
            float z2,
            float x3,
            float z3,
            float x4,
            float z4,
            boolean isConnected
    ) {
        PoseStack.Pose posestack$pose = poseStack.last();
        renderQuad(
                posestack$pose, consumer, minY, maxY, x1, z1, x2, z2, isConnected
        );
        renderQuad(
                posestack$pose, consumer, minY, maxY, x4, z4, x3, z3, isConnected
        );
        renderQuad(
                posestack$pose, consumer, minY, maxY, x2, z2, x4, z4, isConnected
        );
        renderQuad(
                posestack$pose, consumer, minY, maxY, x3, z3, x1, z1, isConnected
        );
    }

    private static void renderQuad(
            PoseStack.Pose pose,
            VertexConsumer consumer,
            int minY,
            float maxY,
            float minX,
            float minZ,
            float maxX,
            float maxZ,
            boolean isConnected
    ) {
        float minU = 1/16.0f;
        float maxU = 15/16.0f;
        float minV = 1/16.0f;
        float maxV = 15/16.0f;
        addVertex(pose, consumer, maxY, minX, minZ, maxU, minV, isConnected);
        addVertex(pose, consumer, minY, minX, minZ, maxU, maxV, isConnected);
        addVertex(pose, consumer, minY, maxX, maxZ, minU, maxV, isConnected);
        addVertex(pose, consumer, maxY, maxX, maxZ, minU, minV, isConnected);
    }

    private static void addVertex(
            PoseStack.Pose pose, VertexConsumer consumer, float y, float x, float z, float u, float v, boolean isConnected
    ) {
        consumer.addVertex(pose, x, y, z)
                .setColor(isConnected ? 0xFFFFFF : 0x666666)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public boolean shouldRenderOffScreen(RoutingNodeTile blockEntity) {
        return true;
    }
}
