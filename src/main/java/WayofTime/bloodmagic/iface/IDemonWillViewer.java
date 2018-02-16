package WayofTime.bloodmagic.iface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDemonWillViewer {
    boolean canSeeDemonWillAura(World world, ItemStack stack, EntityPlayer player);

    int getDemonWillAuraResolution(World world, ItemStack stack, EntityPlayer player);
}
