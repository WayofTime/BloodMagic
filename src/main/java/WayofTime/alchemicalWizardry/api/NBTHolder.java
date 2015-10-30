package WayofTime.alchemicalWizardry.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHolder {

    public static final String NBT_OWNER = "ownerName";
    public static final String NBT_USES = "uses";
    public static final String NBT_UNUSABLE  = "unusable";
    public static final String NBT_SACRIFICE = "sacrifice";
    public static final String NBT_DIMID = "dimensionId";
    public static final String NBT_COORDX = "xCoord";
    public static final String NBT_COORDY = "yCoord";
    public static final String NBT_COORDZ = "zCoord";
    public static final String NBT_MAXORB = "maxOrb";
    public static final String NBT_CURRENTESSENCE = "currentEssence";

    public static ItemStack checkNBT(ItemStack stack) {
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());

        return stack;
    }
}
