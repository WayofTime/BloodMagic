package WayofTime.bloodmagic.client.render.alchemyArray;

import WayofTime.bloodmagic.alchemyArray.AlchemyCircleRenderer;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class DualAlchemyCircleRenderer extends AlchemyCircleRenderer {
    public final ResourceLocation secondaryArrayResource;
    public float offsetFromFace = -0.9f;

    public DualAlchemyCircleRenderer() {
        this(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret1.png"), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret2.png"));
    }

    public DualAlchemyCircleRenderer(ResourceLocation arrayResource, ResourceLocation secondaryArrayResource) {
        super(arrayResource);
        this.secondaryArrayResource = secondaryArrayResource;
    }

    @Override
    public float getSizeModifier(float craftTime) {
        return 1;
    }

    @Override
    public float getRotation(float craftTime) {
        float offset = 2;
        if (craftTime >= offset) {
            float modifier = (craftTime - offset) * 2f;
            return modifier * 1f;
        }
        return 0;
    }

    @Override
    public void renderAt(TileEntity tile, double x, double y, double z, float craftTime) {
        if (!(tile instanceof TileAlchemyArray)) {
            return;
        }

        TileAlchemyArray tileArray = (TileAlchemyArray) tile;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        // float rot = (float)(this.worldObj.provider.getWorldTime() % (360 /
        // this.rotationspeed) * this.rotationspeed) + this.rotationspeed * f;
        float rot = getRotation(craftTime);

        float size = 1.0F * getSizeModifier(craftTime);

        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 1);

        GlStateManager.translate(x, y, z);

        // Specify which face this "circle" is located on
        EnumFacing sideHit = EnumFacing.UP;
        EnumFacing rotation = tileArray.getRotation();

        GlStateManager.translate(sideHit.getXOffset() * offsetFromFace, sideHit.getYOffset() * offsetFromFace, sideHit.getZOffset() * offsetFromFace);

        switch (sideHit) {
            case DOWN:
                GlStateManager.translate(0, 0, 1);
                GlStateManager.rotate(-90.0f, 1, 0, 0);
                break;
            case EAST:
                GlStateManager.rotate(-90.0f, 0, 1, 0);
                GlStateManager.translate(0, 0, -1);
                break;
            case NORTH:
                break;
            case SOUTH:
                GlStateManager.rotate(180.0f, 0, 1, 0);
                GlStateManager.translate(-1, 0, -1);
                break;
            case UP:
                GlStateManager.translate(0, 1, 0);
                GlStateManager.rotate(90.0f, 1, 0, 0);
                break;
            case WEST:
                GlStateManager.translate(0, 0, 1);
                GlStateManager.rotate(90.0f, 0, 1, 0);
                break;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5f, 0.5f, getVerticalOffset(craftTime));
        GlStateManager.rotate(rotation.getHorizontalAngle() + 180, 0, 0, 1);

        // Bind the texture to the circle
        Minecraft.getMinecraft().renderEngine.bindTexture(arrayResource);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(rot, 0, 0, 1);
        double var31 = 0.0D;
        double var33 = 1.0D;
        double var35 = 0;
        double var37 = 1;

        GlStateManager.color(1f, 1f, 1f, 1f);
        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        // wr.setBrightness(200);
        wr.pos(size / 2f, size / 2f, 0.0D).tex(var33, var37).endVertex();
        wr.pos(size / 2f, -size / 2f, 0.0D).tex(var33, var35).endVertex();
        wr.pos(-size / 2f, -size / 2f, 0.0D).tex(var31, var35).endVertex();
        wr.pos(-size / 2f, size / 2f, 0.0D).tex(var31, var37).endVertex();
        tessellator.draw();

        GlStateManager.popMatrix();

        // Bind the second texture to the circle
        Minecraft.getMinecraft().renderEngine.bindTexture(secondaryArrayResource);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-rot, 0, 0, 1);

        GlStateManager.color(1f, 1f, 1f, 1f);
        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        // wr.setBrightness(200);
        wr.pos(size / 2f, size / 2f, 0.0D).tex(var33, var37).endVertex();
        wr.pos(size / 2f, -size / 2f, 0.0D).tex(var33, var35).endVertex();
        wr.pos(-size / 2f, -size / 2f, 0.0D).tex(var31, var35).endVertex();
        wr.pos(-size / 2f, size / 2f, 0.0D).tex(var31, var37).endVertex();
        tessellator.draw();

        GlStateManager.popMatrix();

        // GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
}
