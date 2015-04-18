package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;

public class OmegaParadigmFire extends OmegaParadigm
{
	public OmegaParadigmFire(OmegaArmour helmet, OmegaArmour chestPiece, OmegaArmour leggings, OmegaArmour boots) 
	{
		super(ReagentRegistry.incendiumReagent,  helmet, chestPiece, leggings, boots, new ReagentRegenConfiguration(50, 1, 10));
	}
	
	@Override
	public float getCostPerTickOfUse(EntityPlayer player)
	{
		if(player.isBurning())
		{
			return 0.5f;
		}
		return 1;
	}
	
	@Override
	public void onUpdate(World world, EntityPlayer player, ItemStack stack)
	{
		if(world.getWorldTime() % 100 == 0 && !world.isRemote)
		{
			boolean isInLava = player.isInsideOfMaterial(Material.lava);
			if(player.isBurning() && player.getHealth() < player.getMaxHealth())
			{
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, isInLava ? 1 : 0, true));
			}
			
			if(player.isBurning())
			{
				player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, isInLava ? 400 : 200, isInLava ? 1 : 0, true));
			}
			
			if(player.isInWater())
			{
				player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 200, 2, true));
				player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 2, true));
				player.addPotionEffect(new PotionEffect(Potion.weakness.id, 200, 1, true));
			}
		}
		
		if(player.isBurning())
		{
			player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 200, 0, true));
		}
	}
	
	@Override
	public boolean getBlockEffectWhileInside(Entity entity, int x, int y, int z)
	{
		return true;
	}
	
	@Override
	public void onOmegaKeyPressed(EntityPlayer player, ItemStack stack)
	{

	}
}
