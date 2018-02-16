package WayofTime.bloodmagic.client.render.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.iface.INodeRenderer;
import WayofTime.bloodmagic.client.helper.ShaderHelper;
import WayofTime.bloodmagic.tile.routing.TileRoutingNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderItemRoutingNode extends TileEntitySpecialRenderer<TileRoutingNode> {
    private static final ResourceLocation beamTexture = new ResourceLocation(BloodMagic.MODID, "textures/entities/nodeBeam.png");
    private static final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(TileRoutingNode tileNode, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (mc.player.getHeldItemMainhand().getItem() instanceof INodeRenderer || ConfigHandler.client.alwaysRenderRoutingLines) {
            List<BlockPos> connectionList = tileNode.getConnected();
            for (BlockPos wantedPos : connectionList) {
                BlockPos offsetPos = wantedPos.subtract(tileNode.getPos());

                //The beam renders towards the east by default.

                int xd = offsetPos.getX();
                int yd = offsetPos.getY();
                int zd = offsetPos.getZ();
                double distance = Math.sqrt(xd * xd + yd * yd + zd * zd);
                double subLength = MathHelper.sqrt(xd * xd + zd * zd);
                float rotYaw = -((float) (Math.atan2(zd, xd) * 180.0D / Math.PI));
                float rotPitch = ((float) (Math.atan2(yd, subLength) * 180.0D / Math.PI));

                GlStateManager.pushMatrix();
                float f1 = 1.0f;
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder wr = tessellator.getBuffer();
                this.bindTexture(beamTexture);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                float f2 = 0;
                float f3 = -f2 * 0.2F - (float) MathHelper.floor(-f2 * 0.1F);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                double width = 0.06;

                float test = (tileNode.getWorld().getTotalWorldTime() + partialTicks) / 5f;

                double d18 = -width / 2;
                double d19 = -width / 2;
                double d20 = width / 2;
                double d21 = -width / 2;
                double d22 = -width / 2;
                double d23 = width / 2;
                double d24 = width / 2;
                double d25 = width / 2;
                double d26 = distance * f1;
                double d27 = 0.0D;
                double d28 = 1.0D;
                double d29 = (double) (f3) + test;
                double d30 = distance * f1 + d29;

                GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

                GlStateManager.rotate(rotYaw, 0, 1, 0);
                GlStateManager.rotate(rotPitch, 0, 0, 1);

//        tessellator.setBrightness(240);
//        float s = 1F / 16F;
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

                ShaderHelper.useShader(ShaderHelper.psiBar, (int) tileNode.getWorld().getTotalWorldTime());
                tessellator.draw();
                ShaderHelper.releaseShader();

                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
    }
}
