package WayofTime.bloodmagic.api.util.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.api.Constants;

public class IncenseHelper
{
    public static double getCurrentIncense(EntityPlayer player)
    {
        NBTTagCompound data = player.getEntityData();
        if (data.hasKey(Constants.NBT.CURRENT_INCENSE))
        {
            return data.getDouble(Constants.NBT.CURRENT_INCENSE);
        }

        return 0;
    }

    public static void setCurrentIncense(EntityPlayer player, double amount)
    {
        NBTTagCompound data = player.getEntityData();
        data.setDouble(Constants.NBT.CURRENT_INCENSE, amount);
    }
}