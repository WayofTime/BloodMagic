package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.mob.MailOrderEntityItem;

public class ItemMailOrderCatalogue extends Item
{
	public ItemMailOrderCatalogue()
	{
		super();
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack)
	{
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		return new MailOrderEntityItem(world, location, itemstack);
	}
}
