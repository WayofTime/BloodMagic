package WayofTime.bloodmagic.api.ritual;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * This interface is for internal implementation only.
 * 
 * It is provided via the API for easy obtaining of basic data.
 */
public interface IMasterRitualStone
{
    String getOwner();

    boolean activateRitual(ItemStack activationCrystal, EntityPlayer activator, Ritual ritual);

    void performRitual(World world, BlockPos pos);

    void stopRitual(Ritual.BreakType breakType);

    int getCooldown();

    void setCooldown(int cooldown);

    void setActive(boolean active);

    EnumFacing getDirection();

    boolean areTanksEmpty();

    int getRunningTime();

    World getWorldObj();

    BlockPos getBlockPos();
}
