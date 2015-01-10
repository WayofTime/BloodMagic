package WayofTime.alchemicalWizardry.common.items.armour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaEarth;

public class OmegaArmourEarth extends OmegaArmour
{
	public OmegaArmourEarth(int armorType) 
	{
		super(armorType);
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
		return "alchemicalwizardry:models/armor/OmegaEarth.png";
    }
	
	@Override
	public ModelBiped getChestModel()
	{
		return new ModelOmegaEarth(1.0f, true, true, false, true);
	}
	
	@Override
	public ModelBiped getLegsModel()
	{
		return new ModelOmegaEarth(0.5f, false, false, true, false);
	}
}
