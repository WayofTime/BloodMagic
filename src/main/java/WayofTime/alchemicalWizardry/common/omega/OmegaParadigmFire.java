package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;

public class OmegaParadigmFire extends OmegaParadigm
{
	public OmegaParadigmFire(OmegaArmour helmet, OmegaArmour chestPiece, OmegaArmour leggings, OmegaArmour boots) 
	{
		super(ReagentRegistry.incendiumReagent,  helmet, chestPiece, leggings, boots, new ReagentRegenConfiguration(50, 10, 100));
	}
	
	@Override
	public float getCostPerTickOfUse(EntityPlayer player)
	{
		return 1;
	}
	
	@Override
	public void onUpdate(World world, EntityPlayer player, ItemStack stack)
	{

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
