package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGrunt;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntEarth;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntFire;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntIce;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntWind;

public class RenderMinorDemonGrunt extends RenderLiving
{
    private static final ResourceLocation normalTexture = new ResourceLocation("alchemicalwizardry", "textures/models/MinorDemonGrunt_normal.png");
    private static final ResourceLocation fireTexture = new ResourceLocation("alchemicalwizardry", "textures/models/MinorDemonGrunt_fire.png");
    private static final ResourceLocation iceTexture = new ResourceLocation("alchemicalwizardry", "textures/models/MinorDemonGrunt_ice.png");
    private static final ResourceLocation windTexture = new ResourceLocation("alchemicalwizardry", "textures/models/MinorDemonGrunt_wind.png");
    private static final ResourceLocation earthTexture = new ResourceLocation("alchemicalwizardry", "textures/models/MinorDemonGrunt_earth.png");

    public RenderMinorDemonGrunt(ModelBase par1ModelBase, float par2)
    {
        super(par1ModelBase, par2);
    }

    public ResourceLocation func_110832_a(EntityMinorDemonGrunt entity)
    {
    	if(entity instanceof EntityMinorDemonGruntFire)
    	{
            return fireTexture;
    	}else if(entity instanceof EntityMinorDemonGruntWind)
    	{
    		return windTexture;
    	}else if(entity instanceof EntityMinorDemonGruntIce)
    	{
    		return iceTexture;
    	}else if(entity instanceof EntityMinorDemonGruntEarth)
    	{
    		return earthTexture;
    	}
    	
        return normalTexture;
    }

    public ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_110832_a((EntityMinorDemonGrunt) entity);
    }
}