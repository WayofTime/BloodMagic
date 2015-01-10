package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;

public class OmegaParadigm 
{
	public OmegaArmour helmet;
	public OmegaArmour chestPiece;
	public OmegaArmour leggings;
	public OmegaArmour boots;
	
	public OmegaParadigm(Reagent reagent, OmegaArmour helmet, OmegaArmour chestPiece, OmegaArmour leggings, OmegaArmour boots)
	{
		this.helmet = helmet;
		this.chestPiece = chestPiece;
		this.leggings = leggings;
		this.boots = boots;
		
		this.helmet.setParadigm(this);
		this.chestPiece.setParadigm(this);
		this.leggings.setParadigm(this);
		this.boots.setParadigm(this);
		this.helmet.setReagent(reagent);
		this.chestPiece.setReagent(reagent);
		this.leggings.setReagent(reagent);
		this.boots.setReagent(reagent);
	}
	
	public void convertPlayerArmour(EntityPlayer player)
	{
		ItemStack[] armours = player.inventory.armorInventory;
		
		ItemStack helmetStack = armours[3];
		ItemStack chestStack = armours[2];
		ItemStack leggingsStack = armours[1];
		ItemStack bootsStack = armours[0];
		
		if(helmetStack != null && helmetStack.getItem() == ModItems.boundHelmet && chestStack != null && chestStack.getItem() == ModItems.boundPlate && leggingsStack != null && leggingsStack.getItem() == ModItems.boundLeggings && bootsStack != null && bootsStack.getItem() == ModItems.boundBoots)
		{
			ItemStack omegaHelmetStack = helmet.getSubstituteStack(helmetStack);
			ItemStack omegaChestStack = chestPiece.getSubstituteStack(chestStack);
			ItemStack omegaLeggingsStack = leggings.getSubstituteStack(leggingsStack);
			ItemStack omegaBootsStack = boots.getSubstituteStack(bootsStack);
			
			armours[3] = omegaHelmetStack;
			armours[2] = omegaChestStack;
			armours[1] = omegaLeggingsStack;
			armours[0] = omegaBootsStack;
		}
	}
}
