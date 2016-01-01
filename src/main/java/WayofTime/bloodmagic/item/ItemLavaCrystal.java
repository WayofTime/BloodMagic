package WayofTime.bloodmagic.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;

public class ItemLavaCrystal extends ItemBindable implements IFuelHandler
{
    public ItemLavaCrystal()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".lavaCrystal");
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
            if (syphonNetwork(fuel, getLPUsed())) //TODO: change to canSyphonNetwork
            {
                return 200;
            } else
            {
                EntityPlayer player = PlayerHelper.getPlayerFromUUID(getBindableOwner(fuel));
                if (player != null)
                {
                    //TODO: Add nausea to the player.
                }

                return 0;
            }
        }

        return 0;
    }
}
