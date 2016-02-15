package WayofTime.bloodmagic.item;

import com.google.common.base.Strings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.relauncher.Side;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;

public class ItemLavaCrystal extends ItemBindable implements IFuelHandler
{
    public ItemLavaCrystal()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".lavaCrystal");
        setRegistryName(Constants.BloodMagicItem.LAVA_CRYSTAL.getRegName());
        setLPUsed(25);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        syphonNetwork(itemStack, getLPUsed());
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public int getBurnTime(ItemStack fuel)
    {
        if (fuel == null)
        {
            return 0;
        }

        Item fuelItem = fuel.getItem();

        if (fuelItem instanceof ItemLavaCrystal)
        {
            System.out.println("Test get burn time: Side = " + FMLCommonHandler.instance().getSide());
//
//            if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
//            {
//                return 200;
//            }

            if (canSyphonFromNetwork(fuel, getLPUsed()))
            {
                return 200;
            } else
            {
                if (!Strings.isNullOrEmpty(getBindableOwner(fuel)))
                {
                    EntityPlayer player = PlayerHelper.getPlayerFromUUID(getBindableOwner(fuel));
                    if (player != null)
                    {
                        player.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 99));
                    }
                }

                return 0;
            }
        }

        return 0;
    }
}
