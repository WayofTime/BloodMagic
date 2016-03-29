package WayofTime.bloodmagic.api.iface;

import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;

public interface IMultiWillTool
{
    public EnumDemonWillType getCurrentType(ItemStack stack);
}
