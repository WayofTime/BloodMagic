package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This interface is for internal implementation only.
 * <p>
 * It is provided via the API for easy obtaining of basic data.
 */
public interface IMasterRitualStone {
    UUID getOwner();

    SoulNetwork getOwnerNetwork();

    boolean activateRitual(ItemStack activationCrystal, EntityPlayer activator, Ritual ritual);

    void performRitual(World world, BlockPos pos);

    void stopRitual(Ritual.BreakType breakType);

    int getCooldown();

    void setCooldown(int cooldown);

    boolean isActive();

    void setActive(boolean active);

    EnumFacing getDirection();

    boolean areTanksEmpty();

    int getRunningTime();

    World getWorldObj();

    BlockPos getBlockPos();

    String getNextBlockRange(String range);

    void provideInformationOfRitualToPlayer(EntityPlayer player);

    void provideInformationOfRangeToPlayer(EntityPlayer player, String range);

    void provideInformationOfWillConfigToPlayer(EntityPlayer player, List<EnumDemonWillType> typeList);

    void setActiveWillConfig(EntityPlayer player, List<EnumDemonWillType> typeList);

    EnumReaderBoundaries setBlockRangeByBounds(EntityPlayer player, String range, BlockPos offset1, BlockPos offset2);

    List<EnumDemonWillType> getActiveWillConfig();

    default SoulTicket ticket(int amount) {
        return SoulTicket.block(getWorldObj(), getBlockPos(), amount);
    }

    AreaDescriptor getBlockRange(String range);

    void addBlockRanges(Map<String, AreaDescriptor> blockRanges);

    void addBlockRange(String range, AreaDescriptor defaultRange);

    void setBlockRanges(Map<String, AreaDescriptor> blockRanges);

    void setBlockRange(String range, AreaDescriptor defaultRange);

    Ritual getCurrentRitual();
}
