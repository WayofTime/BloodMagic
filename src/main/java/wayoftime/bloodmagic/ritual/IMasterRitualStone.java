package wayoftime.bloodmagic.ritual;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;

/**
 * This interface is for internal implementation only.
 * <p>
 * It is provided via the API for easy obtaining of basic data.
 */
public interface IMasterRitualStone
{
	UUID getOwner();

	SoulNetwork getOwnerNetwork();

	boolean activateRitual(ItemStack activationCrystal, Player activator, Ritual ritual);

	void performRitual(Level world, BlockPos pos);

	void stopRitual(Ritual.BreakType breakType);

	int getCooldown();

	void setCooldown(int cooldown);

	boolean isActive();

	void setActive(boolean active);

	Direction getDirection();

	boolean areTanksEmpty();

	int getRunningTime();

	Level getWorldObj();

	BlockPos getMasterBlockPos();

	String getNextBlockRange(String range);

	void provideInformationOfRitualToPlayer(Player player);

	void provideInformationOfRangeToPlayer(Player player, String range);

	void provideInformationOfWillConfigToPlayer(Player player, List<EnumDemonWillType> typeList);

	void setActiveWillConfig(Player player, List<EnumDemonWillType> typeList);

	EnumReaderBoundaries setBlockRangeByBounds(Player player, String range, BlockPos offset1, BlockPos offset2);

	List<EnumDemonWillType> getActiveWillConfig();

	default SoulTicket ticket(int amount)
	{
		return SoulTicket.block(getWorldObj(), getMasterBlockPos(), amount);
	}

	AreaDescriptor getBlockRange(String range);

	void addBlockRanges(Map<String, AreaDescriptor> blockRanges);

	void addBlockRange(String range, AreaDescriptor defaultRange);

	void setBlockRanges(Map<String, AreaDescriptor> blockRanges);

	void setBlockRange(String range, AreaDescriptor defaultRange);

	Ritual getCurrentRitual();
}
