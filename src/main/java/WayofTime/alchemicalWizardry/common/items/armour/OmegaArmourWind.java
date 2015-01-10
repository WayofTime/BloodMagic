package WayofTime.alchemicalWizardry.common.items.armour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaWind;

public class OmegaArmourWind extends OmegaArmour
{
	public OmegaArmourWind(int armorType) 
	{
		super(armorType);
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
		return "alchemicalwizardry:models/armor/OmegaWind.png";
    }
	
	@Override
	public ModelBiped getChestModel()
	{
		return new ModelOmegaWind(1.0f, true, true, false, true);
	}
	
	@Override
	public ModelBiped getLegsModel()
	{
		return new ModelOmegaWind(0.5f, false, false, true, false);
	}
}
