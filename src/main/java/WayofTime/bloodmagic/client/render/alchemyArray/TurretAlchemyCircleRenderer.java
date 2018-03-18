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
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffect;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectArrowTurret;
import WayofTime.bloodmagic.alchemyArray.AlchemyCircleRenderer;
import WayofTime.bloodmagic.tile.TileAlchemyArray;

public class TurretAlchemyCircleRenderer extends AlchemyCircleRenderer
{
    private ResourceLocation bottomArrayResource = new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MovementArray.png");
    private ResourceLocation middleArrayResource = new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret2.png");

    public TurretAlchemyCircleRenderer(ResourceLocation location)
    {
        super(location);
    }

    public TurretAlchemyCircleRenderer()
    {
        this(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MovementArray.png"));
    }

    @Override
    public float getSizeModifier(float craftTime)
    {
        return 1;
    }

    @Override
    public float getRotation(float craftTime)
    {
        float offset = 50;
        if (craftTime >= offset)
        {
            float modifier = (craftTime - offset) * 5f;
            return modifier * 1f;
        }
        return 0;
    }

    @Override
    public float getSecondaryRotation(float craftTime)
    {
        return 0;
    }

    @Override
    public void renderAt(TileEntity tile, double x, double y, double z, float craftTime)
    {
        if (!(tile instanceof TileAlchemyArray))
        {
            return;
        }

        float f = 0; //Not working

        TileAlchemyArray tileArray = (TileAlchemyArray) tile;
        AlchemyArrayEffect effect = tileArray.arrayEffect;
        double pitch = 0;
        double yaw = 0;
        int arrowTimer = -1;
        if (effect instanceof AlchemyArrayEffectArrowTurret)
        {
            AlchemyArrayEffectArrowTurret turretEffect = (AlchemyArrayEffectArrowTurret) effect;
            pitch = (turretEffect.getPitch() - turretEffect.getLastPitch()) * f + turretEffect.getLastPitch();
            yaw = (turretEffect.getYaw() - turretEffect.getLastYaw()) * f + turretEffect.getLastYaw();
            arrowTimer = turretEffect.arrowTimer;
        }

        double arrowAnimation = arrowTimer + f;

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

        GlStateManager.translate(sideHit.getFrontOffsetX() * offsetFromFace, sideHit.getFrontOffsetY() * offsetFromFace, sideHit.getFrontOffsetZ() * offsetFromFace);

        switch (sideHit)
        {
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

        double topHeightOffset = getTopHeightOffset(craftTime, arrowAnimation);
        double middleHeightOffset = getMiddleHeightOffset(craftTime, arrowAnimation);
        double bottomHeightOffset = getBottomHeightOffset(craftTime, arrowAnimation);

        BlockPos pos = tileArray.getPos();
        World world = tileArray.getWorld();

        GlStateManager.rotate((float) (yaw + 360 * getStartupPitchYawRatio(craftTime)), 0, 0, 1);
        GlStateManager.rotate((float) ((pitch + 90) * getStartupPitchYawRatio(craftTime)), 1, 0, 0);

        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(bottomArrayResource);
        GlStateManager.rotate(-rot, 0, 0, 1);
        GlStateManager.translate(0, 0, -bottomHeightOffset);
        renderStandardCircle(tessellator, wr, size / 2);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(middleArrayResource);
        GlStateManager.rotate(0, 0, 0, 1);
        GlStateManager.translate(0, 0, -middleHeightOffset);
        renderStandardCircle(tessellator, wr, size);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(arrayResource);
        GlStateManager.rotate(rot, 0, 0, 1);
        GlStateManager.translate(0, 0, -topHeightOffset);
        renderStandardCircle(tessellator, wr, size);
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

        // GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    public float getStartupPitchYawRatio(float craftTime)
    {
        if (craftTime <= 80)
        {
            return 0;
        } else if (craftTime > 80 && craftTime < 140)
        {
            return (craftTime - 80) / 60f;
        }

        return 1;
    }

    public double getBottomHeightOffset(double craftTime, double arrowAnimation)
    {
        if (craftTime <= 40)
        {
            return 0;
        } else if (craftTime > 40 && craftTime < 100)
        {
            return -0.4 * (craftTime - 40) / 60d;
        } else if (craftTime >= 100 && craftTime < 140)
        {
            return -0.4 * (140 - craftTime) / 40d;
        }

        if (arrowAnimation > 0 && arrowAnimation < 45)
        {
            return -0.4 * (arrowAnimation) / 45;
        } else if (arrowAnimation >= 45 && arrowAnimation < 50)
        {
            return -0.4 * (50 - arrowAnimation) / 5;
        }

        return 0;
    }

    public double getMiddleHeightOffset(double craftTime, double arrowAnimation)
    {
        if (craftTime <= 40)
        {
            return 0;
        } else if (craftTime > 40 && craftTime < 100)
        {
            return 0.1 * (craftTime - 40) / 60d;
        } else if (craftTime >= 100 && craftTime < 140)
        {
            return 0.1 * (140 - craftTime) / 40d;
        }

        if (arrowAnimation > 0 && arrowAnimation < 45)
        {
            return 0.1 * (arrowAnimation) / 45;
        } else if (arrowAnimation >= 45 && arrowAnimation < 50)
        {
            return 0.1 * (50 - arrowAnimation) / 5;
        }

        return 0;
    }

    public double getTopHeightOffset(double craftTime, double arrowAnimation)
    {
        if (craftTime <= 40)
        {
            return 0;
        } else if (craftTime > 40 && craftTime < 100)
        {
            return 0.4 * (craftTime - 40) / 60d;
        } else if (craftTime >= 100 && craftTime < 140)
        {
            return 0.4 * (140 - craftTime) / 40d;
        }

        if (arrowAnimation > 0 && arrowAnimation < 45)
        {
            return 0.4 * (arrowAnimation) / 45;
        } else if (arrowAnimation >= 45 && arrowAnimation < 50)
        {
            return 0.4 * (50 - arrowAnimation) / 5;
        }

        return 0;
    }

    private void renderStandardCircle(Tessellator tessellator, BufferBuilder builder, double size)
    {
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
