package WayofTime.bloodmagic.api.util.helper;

import WayofTime.bloodmagic.api.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class IncenseHelper
{
    public static float getCurrentIncense(EntityPlayer player)
    {
        NBTTagCompound data = player.getEntityData();
        if (data.hasKey(Constants.NBT.CURRENT_INCENSE))
        {
            return data.getFloat(Constants.NBT.CURRENT_INCENSE);
        }

        return 0;
    }

    public static void setCurrentIncense(EntityPlayer player, float amount)
    {
        NBTTagCompound data = player.getEntityData();
        data.setFloat(Constants.NBT.CURRENT_INCENSE, amount);
    }
}