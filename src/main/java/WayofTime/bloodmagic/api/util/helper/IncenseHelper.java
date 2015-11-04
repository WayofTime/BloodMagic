package WayofTime.bloodmagic.api.util.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class IncenseHelper {
    public static float getCurrentIncense(EntityPlayer player) {
        NBTTagCompound data = player.getEntityData();
        if (data.hasKey("BM:CurrentIncense")) {
            return data.getFloat("BM:CurrentIncense");
        }

        return 0;
    }

    public static void setCurrentIncense(EntityPlayer player, float amount) {
        NBTTagCompound data = player.getEntityData();
        data.setFloat("BM:CurrentIncense", amount);
    }
}
