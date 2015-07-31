package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;

public class OmegaParadigmWind extends OmegaParadigm
{
	public OmegaParadigmWind(OmegaArmour helmet, OmegaArmour chestPiece, OmegaArmour leggings, OmegaArmour boots) 
	{
		super(ReagentRegistry.aetherReagent,  helmet, chestPiece, leggings, boots, new ReagentRegenConfiguration(50, 1, 10));
	}
	
	@Override
	public float getCostPerTickOfUse(EntityPlayer player)
	{
		if(player.isAirBorne)
		{
			return 0.5f;
		}else
		{
			return 1;
		}
	}
	
	@Override
	public void onUpdate(World world, EntityPlayer player, ItemStack stack)
	{
		if(world.getWorldTime() % 100 == 0 && !world.isRemote && player.posY > 128 && player.getHealth() < player.getMaxHealth())
		{
			player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, player.posY > 128 + 64 ? 1 : 0, true));
		}
		
		player.fallDistance = 0;
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
