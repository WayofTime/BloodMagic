package WayofTime.bloodmagic.api.ritual;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;

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

    String getNextBlockRange(String range);

    void provideInformationOfRitualToPlayer(EntityPlayer player);

    void provideInformationOfRangeToPlayer(EntityPlayer player, String range);

    void setActiveWillConfig(EntityPlayer player, List<EnumDemonWillType> typeList);

    boolean setBlockRangeByBounds(EntityPlayer player, String range, BlockPos offset1, BlockPos offset2);

    List<EnumDemonWillType> getCurrentActiveWillConfig();
}
