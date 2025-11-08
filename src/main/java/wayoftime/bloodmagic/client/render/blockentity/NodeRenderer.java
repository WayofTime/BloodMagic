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
import net.minecraft.util.Mth;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.RoutingNodeBlock;
import wayoftime.bloodmagic.common.blockentity.RoutingNodeTile;
import wayoftime.bloodmagic.common.tag.BMTags;

public class NodeRenderer implements BlockEntityRenderer<RoutingNodeTile> {

    public static final ResourceLocation BEAM = ResourceLocation.withDefaultNamespace("textures/entity/beacon_beam.png");
    public static final int DEFAULT = 0x91CCC6;
    public static final int CORROSIVE = 0xA7CE90;
    public static final int DESTRUCTIVE = 0xD9BFA2;
    public static final int STEADFAST = 0x9794CD;
    public static final int VENGEFUL = 0xCFB98F;
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
                || player.getMainHandItem().is(BMTags.Items.NODE_DEBUGGER)
                || player.getOffhandItem().is(BMTags.Items.NODE_DEBUGGER);

        if (!shouldRenderLine) {
            return;
        }

        long gameTime = node.getLevel().getGameTime();
        float time = (float)Math.floorMod(gameTime, 40) + partialTick;

        BlockPos offsetPos = node.getBlockPos().subtract(parentPos);
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
        poseStack.translate(0, -distance, 0);

        int colour = switch (node.getBlockState().getValue(RoutingNodeBlock.WILL)) {
            case DEFAULT -> DEFAULT;
            case CORROSIVE -> CORROSIVE;
            case DESTRUCTIVE -> DESTRUCTIVE;
            case STEADFAST -> STEADFAST;
            case VENGEFUL -> VENGEFUL;
        };

        renderBeaconBeam(poseStack, bufferSource, BEAM, colour, time, (float) distance, 0.06f, node.getBlockState().getValue(RoutingNodeBlock.ENABLED));
        poseStack.popPose();
    }

    public static void renderBeaconBeam(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            ResourceLocation beamLocation,
            int colour,
            float time,
            float height,
            float beamRadius,
            boolean isConnected
    ) {
        float f1 = height < 0 ? time : -time;
        float f2 = Mth.frac(f1 * 0.2F - (float)Mth.floor(f1 * 0.1F));
        float v1 = -1 + f2;
        float v0 = height * (0.5F / beamRadius) + v1;

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(time * 2.25F - 45.0F));
        renderPart(
                poseStack,
                bufferSource.getBuffer(RenderType.beaconBeam(beamLocation, false)),
                colour,
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
                v0,
                v1,
                isConnected
        );
        poseStack.popPose();
    }
    private static void renderPart(
            PoseStack poseStack,
            VertexConsumer consumer,
            int colour,
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
            float v0,
            float v1,
            boolean isConnected
    ) {
        PoseStack.Pose posestack$pose = poseStack.last();
        renderQuad(
                posestack$pose, consumer, colour, minY, maxY, x1, z1, x2, z2, v0, v1, isConnected
        );
        renderQuad(
                posestack$pose, consumer, colour, minY, maxY, x4, z4, x3, z3, v0, v1, isConnected
        );
        renderQuad(
                posestack$pose, consumer, colour, minY, maxY, x2, z2, x4, z4, v0, v1, isConnected
        );
        renderQuad(
                posestack$pose, consumer, colour, minY, maxY, x3, z3, x1, z1, v0, v1, isConnected
        );
    }

    private static void renderQuad(
            PoseStack.Pose pose,
            VertexConsumer consumer,
            int colour,
            int minY,
            float maxY,
            float minX,
            float minZ,
            float maxX,
            float maxZ,
            float v0,
            float v1,
            boolean isConnected
    ) {
        addVertex(pose, consumer, colour, maxY, minX, minZ, 1, v0, isConnected);
        addVertex(pose, consumer, colour, minY, minX, minZ, 1, v1, isConnected);
        addVertex(pose, consumer, colour, minY, maxX, maxZ, 0, v1, isConnected);
        addVertex(pose, consumer, colour, maxY, maxX, maxZ, 0, v0, isConnected);
    }

    private static void addVertex(
            PoseStack.Pose pose, VertexConsumer consumer, int colour, float y, float x, float z, float u, float v, boolean isConnected
    ) {
        consumer.addVertex(pose, x, y, z)
                .setColor((isConnected ? colour : 0x666666))
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
