package WayofTime.alchemicalWizardry.common.renderer.mob;

import WayofTime.alchemicalWizardry.common.entity.mob.EntityBoulderFist;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderBoulderFist extends RenderLiving
{
    private static final ResourceLocation field_110833_a = new ResourceLocation("alchemicalwizardry", "textures/models/BoulderFist.png"); //refers to:YourMod/modelsTextureFile/optionalFile/yourTexture.png

    public RenderBoulderFist(ModelBase par1ModelBase, float par2)
    {
        super(par1ModelBase, par2);
    }

    public ResourceLocation func_110832_a(EntityBoulderFist par1EntityBoulderFist)
    {
        return field_110833_a;
    }

    public ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110832_a((EntityBoulderFist) par1Entity);
    }
}
