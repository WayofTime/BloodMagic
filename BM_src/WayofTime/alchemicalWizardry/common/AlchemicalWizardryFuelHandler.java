package WayofTime.alchemicalWizardry.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import WayofTime.alchemicalWizardry.common.items.LavaCrystal;
import cpw.mods.fml.common.IFuelHandler;

public class AlchemicalWizardryFuelHandler implements IFuelHandler
{
	@Override
	public int getBurnTime(ItemStack fuel)
	{
		ItemStack itemStack = fuel;
		int var1 = fuel.itemID;

		if (var1 == AlchemicalWizardry.lavaCrystal.itemID)
		{
			/*ItemStack newItem = new ItemStack(AlchemicalWizardry.lavaCrystal);
            newItem.getItem().setDamage(newItem, 50);
            fuel.getItem().setContainerItem(((LavaCrystal)newItem.getItem()).change());
			 */
			LavaCrystal item = (LavaCrystal)fuel.getItem();

			if (item.hasEnoughEssence(fuel))
			{
				return 200;
			}
			else
			{
				NBTTagCompound tag = itemStack.stackTagCompound;

				if (tag == null)
				{
					return 0;
				}

				if (MinecraftServer.getServer() == null)
				{
					return 0;
				}

				if (MinecraftServer.getServer().getConfigurationManager() == null)
				{
					return 0;
				}

				EntityPlayer owner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(tag.getString("ownerName"));

				if (owner == null)
				{
					return 0;
				}

				owner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
				return 0;
			}
		}

		return 0;
	}
}
