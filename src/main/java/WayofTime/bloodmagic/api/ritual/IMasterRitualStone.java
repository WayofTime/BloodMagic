package WayofTime.bloodmagic.api.ritual;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IMasterRitualStone {

    String getOwner();

    boolean activateRitual(ItemStack activationCrystal, EntityPlayer activator);

    void performRitual(World world, BlockPos pos, Ritual ritual);

    void stopRitual();

    int getCooldown();

    void setCooldown(int cooldown);

    void setActive(boolean active);

    EnumFacing getDirection();

    boolean areTanksEmpty();

    int getRunningTime();

    World getWorld();

    BlockPos getPos();
}
