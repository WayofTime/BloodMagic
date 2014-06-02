package WayofTime.alchemicalWizardry.common.renderer.projectile;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import WayofTime.alchemicalWizardry.common.entity.projectile.ExplosionProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.FireProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.HolyProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.IceProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.LightningBoltProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.MudProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.WaterProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.WindGustProjectile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEnergyBlastProjectile extends Render
{
    public void doRenderEnergyBlastProjectile(Entity entityShot, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.1F, 0.1F, 0.1F);
        this.bindTexture(this.getEntityTexture(entityShot));
        Tessellator var12 = Tessellator.instance;
        GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        var12.startDrawingQuads();
        var12.setNormal(0.0F, 1.0F, 0.0F);
        var12.addVertexWithUV(-0.5F, -0.25F, 0.0D, 0, 1);
        var12.addVertexWithUV(0.5F, -0.25F, 0.0D, 1, 1);
        var12.addVertexWithUV(0.5F, 0.75F, 0.0D, 1, 0);
        var12.addVertexWithUV(-0.5F, 0.75F, 0.0D, 0, 0);
        var12.draw();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        if (par1Entity instanceof IProjectile)
        {
            this.doRenderEnergyBlastProjectile(par1Entity, par2, par4, par6, par8, par9);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        if (entity instanceof IceProjectile)
        {
            return new ResourceLocation("alchemicalwizardry", "textures/entities/iceProjectile.png");
        }

        if (entity instanceof FireProjectile)
        {
            return new ResourceLocation("alchemicalwizardry", "textures/entities/fireProjectile.png");
        }

        if (entity instanceof ExplosionProjectile)
        {
            return new ResourceLocation("alchemicalwizardry", "textures/entities/explosionProjectile.png");
        }

        if (entity instanceof HolyProjectile)
        {
            return new ResourceLocation("alchemicalwizardry", "textures/entities/holyProjectile.png");
        }

        if (entity instanceof WindGustProjectile)
        {
            return new ResourceLocation("alchemicalwizardry", "textures/entities/windGustProjectile.png");
        }

        if (entity instanceof LightningBoltProjectile)
        {
            return new ResourceLocation("alchemicalwizardry", "textures/entities/lightningProjectile.png");
        }

        if (entity instanceof WaterProjectile)
        {
            return new ResourceLocation("alchemicalwizardry", "textures/entities/waterProjectile.png");
        }

        if (entity instanceof MudProjectile)
        {
            return new ResourceLocation("alchemicalwizardry", "textures/entities/mudProjectile.png");
        }

        return new ResourceLocation("alchemicalwizardry", "textures/entities/energyBlastProjectile.png");
    }
}
