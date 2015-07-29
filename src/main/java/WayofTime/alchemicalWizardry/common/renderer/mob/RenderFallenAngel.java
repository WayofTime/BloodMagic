package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFallenAngel;

@SideOnly(Side.CLIENT)
public class RenderFallenAngel extends RenderLiving
{
    private static final ResourceLocation field_110833_a = new ResourceLocation("alchemicalwizardry", "textures/models/WingedAngel.png");

    public RenderFallenAngel(ModelBase par1ModelBase, float par2)
    {
        super(Minecraft.getMinecraft().getRenderManager(), par1ModelBase, par2);
    }

    public ResourceLocation func_110832_a(EntityFallenAngel par1EntityFallenAngel)
    {
        return field_110833_a;
    }

    public ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110832_a((EntityFallenAngel) par1Entity);
    }
}
