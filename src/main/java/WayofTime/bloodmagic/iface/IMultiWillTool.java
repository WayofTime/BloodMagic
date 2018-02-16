package WayofTime.bloodmagic.iface;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.item.ItemStack;

public interface IMultiWillTool {
    EnumDemonWillType getCurrentType(ItemStack stack);
}
