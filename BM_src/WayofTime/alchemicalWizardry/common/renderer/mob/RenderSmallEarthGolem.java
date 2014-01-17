package WayofTime.alchemicalWizardry.common.renderer.mob;

import WayofTime.alchemicalWizardry.common.entity.mob.EntitySmallEarthGolem;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderSmallEarthGolem extends RenderLiving
{
    private static final ResourceLocation field_110833_a = new ResourceLocation("alchemicalwizardry", "textures/models/SmallEarthGolem.png"); //refers to:YourMod/modelsTextureFile/optionalFile/yourTexture.png

    public RenderSmallEarthGolem(ModelBase par1ModelBase, float par2)
    {
        super(par1ModelBase, par2);
    }

    public ResourceLocation func_110832_a(EntitySmallEarthGolem par1EntitySmallEarthGolem)
    {
        return field_110833_a;
    }

    public ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110832_a((EntitySmallEarthGolem) par1Entity);
    }
}
