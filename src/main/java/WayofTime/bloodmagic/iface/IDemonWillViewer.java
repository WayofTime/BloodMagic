package WayofTime.bloodmagic.iface;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDemonWillViewer {
    boolean canSeeDemonWillAura(World world, ItemStack stack, PlayerEntity player);

    int getDemonWillAuraResolution(World world, ItemStack stack, PlayerEntity player);
}
