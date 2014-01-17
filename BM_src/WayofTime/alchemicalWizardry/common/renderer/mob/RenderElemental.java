package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import WayofTime.alchemicalWizardry.common.EntityAirElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityEarthElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFireElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityHolyElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityShadeElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityWaterElemental;

public class RenderElemental extends RenderLiving
{
	private static final ResourceLocation airBeacon = new ResourceLocation("alchemicalwizardry", "textures/models/AirFloatingBeacon.png"); //refers to:YourMod/modelsTextureFile/optionalFile/yourTexture.png
	private static final ResourceLocation waterBeacon = new ResourceLocation("alchemicalwizardry", "textures/models/WaterFloatingBeacon.png");
	private static final ResourceLocation earthBeacon = new ResourceLocation("alchemicalwizardry", "textures/models/EarthFloatingBeacon.png");
	private static final ResourceLocation fireBeacon = new ResourceLocation("alchemicalwizardry", "textures/models/FireFloatingBeacon.png");
	private static final ResourceLocation shadeBeacon = new ResourceLocation("alchemicalwizardry", "textures/models/DarkFloatingBeacon.png");
	private static final ResourceLocation holyBeacon = new ResourceLocation("alchemicalwizardry", "textures/models/HolyFloatingBeacon.png");

	public RenderElemental(ModelBase par1ModelBase, float par2)
	{
		super(par1ModelBase, par2);
	}

	public ResourceLocation func_110832_a(EntityElemental par1EntityElemental)
	{
		if (par1EntityElemental instanceof EntityAirElemental)
		{
			return airBeacon;
		}

		if (par1EntityElemental instanceof EntityWaterElemental)
		{
			return waterBeacon;
		}

		if (par1EntityElemental instanceof EntityEarthElemental)
		{
			return earthBeacon;
		}

		if (par1EntityElemental instanceof EntityFireElemental)
		{
			return fireBeacon;
		}

		if (par1EntityElemental instanceof EntityShadeElemental)
		{
			return shadeBeacon;
		}

		if (par1EntityElemental instanceof EntityHolyElemental)
		{
			return holyBeacon;
		}

		return airBeacon;
	}

	@Override
	public ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return func_110832_a((EntityElemental)par1Entity);
	}
}