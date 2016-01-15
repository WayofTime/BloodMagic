package WayofTime.bloodmagic.client.render;

import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import WayofTime.bloodmagic.client.helper.ShaderHelper;
import WayofTime.bloodmagic.tile.routing.TileRoutingNode;

public class RenderItemRoutingNode extends TileEntitySpecialRenderer<TileRoutingNode>
{
    private static final ResourceLocation field_110629_a = new ResourceLocation("textures/entity/beacon_beam.png");

//    private static final ResourceLocation test = new ResourceLocation("luminescence:textures/models/InputMirror.png");

    @Override
    public void renderTileEntityAt(TileRoutingNode tileNode, double x, double y, double z, float partialTicks, int destroyStage)
    {
        List<BlockPos> connectionList = tileNode.getConnected();
        for (BlockPos wantedPos : connectionList)
        {
            BlockPos offsetPos = wantedPos.subtract(tileNode.getPos());

            //The beam renders towards the east by default.

            int xd = offsetPos.getX();
            int yd = offsetPos.getY();
            int zd = offsetPos.getZ();
            double distance = Math.sqrt(xd * xd + yd * yd + zd * zd);
            double subLength = MathHelper.sqrt_double(xd * xd + zd * zd);
            float rotYaw = -((float) (Math.atan2(zd, xd) * 180.0D / Math.PI));
            float rotPitch = ((float) (Math.atan2(yd, subLength) * 180.0D / Math.PI));

            GL11.glPushMatrix();
            float f1 = 1.0f;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer wr = tessellator.getWorldRenderer();
            this.bindTexture(field_110629_a);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            float f2 = 0;
            float f3 = -f2 * 0.2F - (float) MathHelper.floor_float(-f2 * 0.1F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glDepthMask(false);

            double width = 0.1;

            float test = (tileNode.getWorld().getTotalWorldTime() + partialTicks) / 5f;

            double d18 = -width / 2;
            double d19 = -width / 2;
            double d20 = width / 2;
            double d21 = -width / 2;
            double d22 = -width / 2;
            double d23 = width / 2;
            double d24 = width / 2;
            double d25 = width / 2;
            double d26 = (double) (distance * f1);
            double d27 = 0.0D;
            double d28 = 1.0D;
            double d29 = (double) (f3) + test;
            double d30 = (double) (distance * f1) + d29;

            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

            GL11.glRotatef(rotYaw, 0.0F, 1, 0);
            GL11.glRotatef(rotPitch, 0, 0.0F, 1);

//        tessellator.setBrightness(240);
//        float s = 1F / 16F;
//        GL11.glTranslatef(0F, s, s);
//        GL11.glScalef(1F, s * 14F, s * 14F);
            wr.begin(7, DefaultVertexFormats.POSITION_TEX);
//        tessellator.setColorRGBA(255, 255, 255, 100);
            wr.pos(d26, d18, d19).tex(d28, d30).endVertex();
            wr.pos(0, d18, d19).tex(d28, d29).endVertex();
            wr.pos(0, d20, d21).tex(d27, d29).endVertex();
            wr.pos(d26, d20, d21).tex(d27, d30).endVertex();
            wr.pos(d26, d24, d25).tex(d28, d30).endVertex();
            wr.pos(0, d24, d25).tex(d28, d29).endVertex();
            wr.pos(0, d22, d23).tex(d27, d29).endVertex();
            wr.pos(d26, d22, d23).tex(d27, d30).endVertex();
            wr.pos(d26, d20, d21).tex(d28, d30).endVertex();
            wr.pos(0, d20, d21).tex(d28, d29).endVertex();
            wr.pos(0, d24, d25).tex(d27, d29).endVertex();
            wr.pos(d26, d24, d25).tex(d27, d30).endVertex();
            wr.pos(d26, d22, d23).tex(d28, d30).endVertex();
            wr.pos(0, d22, d23).tex(d28, d29).endVertex();
            wr.pos(0, d18, d19).tex(d27, d29).endVertex();
            wr.pos(d26, d18, d19).tex(d27, d30).endVertex();

            ShaderHelper.useShaderWithProps(ShaderHelper.beam, "time", (int) tileNode.getWorld().getTotalWorldTime());
            tessellator.draw();
            ShaderHelper.releaseShader();

            GL11.glDepthMask(true);

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}
