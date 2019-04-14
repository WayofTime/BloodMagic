package WayofTime.bloodmagic.client.render.alchemyArray;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.alchemyArray.AlchemyCircleRenderer;
import WayofTime.bloodmagic.tile.TileAlchemyArray;

public class MobSacrificeAlchemyCircleRenderer extends AlchemyCircleRenderer {
    private ResourceLocation bottomArrayResource = new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MovementArray.png");

    private ResourceLocation mobSacrificeSwirlResource = new ResourceLocation("bloodmagic", "textures/models/mobsacrificeswirl.png");

    public MobSacrificeAlchemyCircleRenderer(ResourceLocation location) {
        super(location);
    }

    public MobSacrificeAlchemyCircleRenderer() {
        this(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/mobsacrifice.png"));
    }

    @Override
    public float getSizeModifier(float craftTime) {
        if (craftTime < 40) {
            return 0;
        } else if (craftTime > 40 && craftTime < 100) {
            return (craftTime - 40) / 60f;
        }
        return 1;
    }

    @Override
    public float getRotation(float craftTime) {
        float offset = 50;
        if (craftTime >= offset) {
            float modifier = (craftTime - offset) * 5f;
            return modifier * 1f;
        }
        return 0;
    }

    @Override
    public float getSecondaryRotation(float craftTime) {
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

        float rot = getRotation(craftTime);

        float size = 1.0F * getSizeModifier(craftTime);

        // Bind the texture to the circle
        Minecraft.getMinecraft().renderEngine.bindTexture(arrayResource);

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
//        GlStateManager.rotate(rotation.getHorizontalAngle() + 180, 0, 0, 1);

        GlStateManager.pushMatrix();

        double topHeightOffset = 0;
        double middleHeightOffset = 0;
        double bottomHeightOffset = 0;

        BlockPos pos = tileArray.getPos();
        World world = tileArray.getWorld();

//        GlStateManager.rotate((float) (yaw + 360 * getStartupPitchYawRatio(craftTime)), 0, 0, 1);
//        GlStateManager.rotate((float) ((pitch + 90) * getStartupPitchYawRatio(craftTime)), 1, 0, 0);

        for (int i = 1; i <= 3; i++) {
            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(bottomArrayResource);
            translateAndRotateFloatingArray(tessellator, wr, size, rot, craftTime, i);
            GlStateManager.popMatrix();
        }

        //Render the main array.
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(arrayResource);
//        GlStateManager.rotate(rot, 0, 0, 1);
        renderStandardCircle(tessellator, wr, 3);
        GlStateManager.popMatrix();

        //Render the swirlz
        float swirlSize = 3;
        if (craftTime <= 40) {
            swirlSize = 0;
        } else if (craftTime <= 100) {
            swirlSize = 3 * (craftTime - 40) / 60;
        }
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(mobSacrificeSwirlResource);
        GlStateManager.translate(0, 0, 0.1);
        GlStateManager.rotate(rot / 3, 0, 0, 1);
        renderStandardCircle(tessellator, wr, swirlSize);
        GlStateManager.popMatrix();

//        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

        // GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    public float getStartupPitchYawRatio(float craftTime) {
        if (craftTime <= 80) {
            return 0;
        } else if (craftTime > 80 && craftTime < 140) {
            return (craftTime - 80) / 60f;
        }

        return 1;
    }

    private void translateAndRotateFloatingArray(Tessellator tessellator, BufferBuilder builder, double size, float rotation, float craftTime, int circle) {
        double verticalOffset = 2;

        float primaryRotation = 0;
        float secondaryRotation = 0;
        if (craftTime >= 40) {
            primaryRotation = (craftTime - 40) * 4f;
            secondaryRotation = (craftTime - 40) * 2f;
        }

        float translationOffset = 1;
        if (craftTime < 80) {
            translationOffset = 0;
        } else if (craftTime < 140) {
            translationOffset = (craftTime - 80) / 60;
        }

        switch (circle) {
            case 1:
                GlStateManager.translate(0, 0, -verticalOffset);
                GlStateManager.rotate(rotation / 200, 1, 0, 0);
                GlStateManager.rotate(rotation / 10, 0, 0, 1);
                GlStateManager.translate(1.7 * translationOffset, 0, 0);
                break;
            case 2:
                GlStateManager.translate(0, 0, -verticalOffset);
//            GlStateManager.rotate(254, 0, 0, 1);
                GlStateManager.rotate((float) (rotation / 150 + 120), 1, 0, 0);
                GlStateManager.rotate(120, 0, 1, 0);
                GlStateManager.rotate(-rotation / 10, 0, 0, 1);
                GlStateManager.translate(1.2 * translationOffset, 0, 0);
                break;
            case 3:
                GlStateManager.translate(0, 0, -verticalOffset);
//            GlStateManager.rotate(130, 0, 0, 1);
                GlStateManager.rotate((float) (rotation / 100 + 284), 1, 0, 0);
                GlStateManager.rotate(240, 0, 1, 0);
                GlStateManager.rotate(-rotation / 7 + 180, 0, 0, 1);
                GlStateManager.translate(2 * translationOffset, 0, 0);
                break;
            default:
                //What are you doing, Way???
        }

        GlStateManager.rotate(primaryRotation, 0, 1, 0);
        GlStateManager.rotate(secondaryRotation, 1, 0, 0);
        GlStateManager.rotate(secondaryRotation * 0.41831f, 0, 0, 1);

        renderStandardCircle(tessellator, builder, size);
    }

    private void renderStandardCircle(Tessellator tessellator, BufferBuilder builder, double size) {
        double var31 = 0.0D;
        double var33 = 1.0D;
        double var35 = 0;
        double var37 = 1;
        GlStateManager.color(1f, 1f, 1f, 1f);
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        // wr.setBrightness(200);
        builder.pos(size / 2f, size / 2f, 0).tex(var33, var37).endVertex();
        builder.pos(size / 2f, -size / 2f, 0).tex(var33, var35).endVertex();
        builder.pos(-size / 2f, -size / 2f, 0).tex(var31, var35).endVertex();
        builder.pos(-size / 2f, size / 2f, 0).tex(var31, var37).endVertex();
        tessellator.draw();
    }
}
