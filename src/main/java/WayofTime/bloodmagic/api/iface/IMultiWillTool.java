package WayofTime.bloodmagic.api.iface;

import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import net.minecraft.item.ItemStack;

public interface IMultiWillTool {
    EnumDemonWillType getCurrentType(ItemStack stack);
}
