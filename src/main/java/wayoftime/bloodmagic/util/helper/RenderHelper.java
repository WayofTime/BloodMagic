package wayoftime.bloodmagic.util.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderHelper {
    public static void addVertex (
            VertexConsumer buf, Matrix4f matrix, float x, float y, float z, float u, float v, int colour, int light, int overlay, Vector3f norm
    ){
        buf.addVertex(matrix, x, y, z)
                .setColor(red(colour), green(colour), blue(colour), alpha(colour))
                .setUv(u, v)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(norm.x, norm.y, norm.z);
    }

    public static void addCubeAll(
            VertexConsumer buf, PoseStack poseStack, TextureAtlasSprite texture, int tintColour, int light, int overlay, float x0, float y0, float z0, float x1, float y1, float z1
    ) {
        Vector3f norm = new Vector3f();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();

        float u0 = texture.getU0();
        float u1 = texture.getU1();

        float v0 = texture.getV0();
        float v1 = texture.getV1();

        // East -X
        pose.transformNormal(-1, 0, 0, norm);
        addVertex(buf, matrix, x0, y0, z1, u0, v0, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x0, y1, z1, u0, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x0, y1, z0, u1, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x0, y0, z0, u1, v0, tintColour, light, overlay, norm);

        // West +X
        pose.transformNormal(1, 0, 0, norm);
        addVertex(buf, matrix, x1, y0, z0, u0, v0, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x1, y1, z0, u0, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x1, y1, z1, u1, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x1, y0, z1, u1, v0, tintColour, light, overlay, norm);

        // Down -Y
        pose.transformNormal(0, -1, 0, norm);
        addVertex(buf, matrix, x1, y0, z0, u0, v0, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x0, y0, z0, u0, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x0, y0, z1, u1, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x1, y0, z1, u1, v0, tintColour, light, overlay, norm);

        // Up +Y
        pose.transformNormal(0, 1, 0, norm);
        addVertex(buf, matrix, x1, y1, z1, u0, v0, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x1, y1, z0, u0, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x0, y1, z0, u1, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x0, y1, z1, u1, v0, tintColour, light, overlay, norm);

        // North -Z
        pose.transformNormal(0, 0, -1, norm);
        addVertex(buf, matrix, x0, y0, z0, u0, v0, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x0, y1, z0, u0, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x1, y1, z0, u1, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x1, y0, z0, u1, v0, tintColour, light, overlay, norm);

        // South +Z
        pose.transformNormal(0, 0, 1, norm);
        addVertex(buf, matrix, x1, y0, z1, u0, v0, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x1, y1, z1, u0, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x0, y1, z1, u1, v1, tintColour, light, overlay, norm);
        addVertex(buf, matrix, x0, y0, z1, u1, v0, tintColour, light, overlay, norm);
    }

    public static int alpha(int colour) {
        return colour >> 24 & 0xFF;
    }

    public static int red(int colour) {
        return colour >> 16 & 0xFF;
    }

    public static int green(int colour) {
        return colour >> 8 & 0xFF;
    }

    public static int blue(int colour) {
        return colour & 0xFF;
    }

    public static int argb(int alpha, int red, int green, int blue) {
        return alpha << 24 + red << 16 + green << 8 + blue;
    }
}
