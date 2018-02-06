package WayofTime.bloodmagic.apibutnotreally.iface;

import WayofTime.bloodmagic.apibutnotreally.soul.EnumDemonWillType;
import net.minecraft.item.ItemStack;

public interface IMultiWillTool {
    EnumDemonWillType getCurrentType(ItemStack stack);
}
