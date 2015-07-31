package WayofTime.alchemicalWizardry.common;

import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.items.LavaCrystal;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;

public class AlchemicalWizardryFuelHandler implements IFuelHandler
{
    @Override
    public int getBurnTime(ItemStack fuel)
    {
        if (fuel == null)
        {
            return 0;
        }

        Item fuelItem = fuel.getItem();

        if (fuelItem.equals(ModItems.lavaCrystal))
        {
            LavaCrystal item = (LavaCrystal) fuel.getItem();

            if (item.hasEnoughEssence(fuel))
            {
                return 200;
            } else
            {
                NBTTagCompound tag = fuel.getTagCompound();

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

                EntityPlayer owner = SpellHelper.getPlayerForUsername(tag.getString("ownerName"));

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
