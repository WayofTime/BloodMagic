package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;

public class OmegaParadigm 
{
	public OmegaArmour helmet;
	public OmegaArmour chestPiece;
	public OmegaArmour leggings;
	public OmegaArmour boots;
	
	public ReagentRegenConfiguration config;
	
	public OmegaParadigm(Reagent reagent, OmegaArmour helmet, OmegaArmour chestPiece, OmegaArmour leggings, OmegaArmour boots, ReagentRegenConfiguration config)
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
		
		this.config = new ReagentRegenConfiguration(20, 10, 1);
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
	
	public ReagentRegenConfiguration getRegenConfig(EntityPlayer player)
	{
		return this.config;
	}
	
	public int getMaxAdditionalHealth()
	{
		return 50;
	}
	
	public boolean setOmegaStalling(EntityPlayer player, int duration)
	{
		ItemStack[] armours = player.inventory.armorInventory;
		
		ItemStack chestStack = armours[2];
		
		if(chestStack != null && chestStack.getItem() == this.chestPiece)
		{
			((OmegaArmour)chestStack.getItem()).setOmegaStallingDuration(chestStack, duration);
			return true;
		}
		
		return false;
	}
	
	public float getCostPerTickOfUse(EntityPlayer player)
	{
		return 1;
	}
	
	public boolean doDrainReagent(EntityPlayer player)
	{
		ItemStack[] armours = player.inventory.armorInventory;
		
		ItemStack chestStack = armours[2];
		
		if(chestStack != null && chestStack.getItem() == this.chestPiece)
		{
			return !((OmegaArmour)chestStack.getItem()).hasOmegaStalling(chestStack);
		}
		return true;
	}
	
	public boolean isPlayerWearingFullSet(EntityPlayer player)
	{
		ItemStack[] armours = player.inventory.armorInventory;
		
		ItemStack helmetStack = armours[3];
		ItemStack chestStack = armours[2];
		ItemStack leggingsStack = armours[1];
		ItemStack bootsStack = armours[0];
		
		return helmetStack != null && helmetStack.getItem() == helmet && chestStack != null && chestStack.getItem() == chestPiece && leggingsStack != null && leggingsStack.getItem() == leggings && bootsStack != null && bootsStack.getItem() == boots;
	}
	
	public void onUpdate(World world, EntityPlayer player, ItemStack stack)
	{
		
	}
	
	public void onOmegaKeyPressed(EntityPlayer player, ItemStack stack)
	{
		
	}
	
	public boolean getBlockEffectWhileInside(Entity entity, int x, int y, int z)
	{
		return false;
	}

	public boolean onHPBarDepleted(EntityPlayer player, ItemStack stack)
	{
		return false;
	}
}
