package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardian;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianEarth;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianFire;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianIce;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianWind;

public class RenderMinorDemonGruntGuardian extends RenderLiving
{
    private static final ResourceLocation normalTexture = new ResourceLocation("alchemicalwizardry", "textures/models/MinorDemonGruntGuardian_normal.png");
    private static final ResourceLocation fireTexture = new ResourceLocation("alchemicalwizardry", "textures/models/MinorDemonGruntGuardian_fire.png");
    private static final ResourceLocation iceTexture = new ResourceLocation("alchemicalwizardry", "textures/models/MinorDemonGruntGuardian_ice.png");
    private static final ResourceLocation windTexture = new ResourceLocation("alchemicalwizardry", "textures/models/MinorDemonGruntGuardian_wind.png");
    private static final ResourceLocation earthTexture = new ResourceLocation("alchemicalwizardry", "textures/models/MinorDemonGruntGuardian_earth.png");

    public RenderMinorDemonGruntGuardian(ModelBase par1ModelBase, float par2)
    {
        super(par1ModelBase, par2);
    }

    public ResourceLocation func_110832_a(EntityMinorDemonGruntGuardian entity)
    {
    	if(entity instanceof EntityMinorDemonGruntGuardianFire)
    	{
            return fireTexture;
    	}else if(entity instanceof EntityMinorDemonGruntGuardianWind)
    	{
    		return windTexture;
    	}else if(entity instanceof EntityMinorDemonGruntGuardianIce)
    	{
    		return iceTexture;
    	}else if(entity instanceof EntityMinorDemonGruntGuardianEarth)
    	{
    		return earthTexture;
    	}
    	
        return normalTexture;
    }

    public ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_110832_a((EntityMinorDemonGruntGuardian) entity);
    }
}