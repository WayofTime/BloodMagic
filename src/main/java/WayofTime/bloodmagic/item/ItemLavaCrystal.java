package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import com.google.common.base.Strings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.IFuelHandler;

public class ItemLavaCrystal extends ItemBindable implements IFuelHandler
{
    public ItemLavaCrystal()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".lavaCrystal");
        setLPUsed(1);
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
            if (syphonNetwork(fuel, getLPUsed()))
            {
                return 1;
            }
            else
            {
                NBTTagCompound tag = fuel.getTagCompound();

                if (tag == null || MinecraftServer.getServer() == null || MinecraftServer.getServer().getConfigurationManager() == null)
                    return 0;

                if (Strings.isNullOrEmpty(((ItemLavaCrystal) fuelItem).getBindableOwner(fuel))) return 0;
                else hurtPlayer(PlayerHelper.getPlayerFromUUID(getBindableOwner(fuel)), getLPUsed());

                return 0;
            }
        }

        return 0;
    }
}
