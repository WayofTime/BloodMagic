package wayoftime.bloodmagic.ritual;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.api.will.EnumDemonWillType;

/**
 * This interface is for internal implementation only.
 * <p>
 * It is provided via the API for easy obtaining of basic data.
 */
public interface IMasterRitualStone
{
	UUID getOwner();

	SoulNetwork getOwnerNetwork();

	boolean activateRitual(ItemStack activationCrystal, PlayerEntity activator, Ritual ritual);

	void performRitual(World world, BlockPos pos);

	void stopRitual(Ritual.BreakType breakType);

	int getCooldown();

	void setCooldown(int cooldown);

	boolean isActive();

	void setActive(boolean active);

	Direction getDirection();

	boolean areTanksEmpty();

	int getRunningTime();

	World getWorldObj();

	BlockPos getBlockPos();

	String getNextBlockRange(String range);

	void provideInformationOfRitualToPlayer(PlayerEntity player);

	void provideInformationOfRangeToPlayer(PlayerEntity player, String range);

	void provideInformationOfWillConfigToPlayer(PlayerEntity player, List<EnumDemonWillType> typeList);

	void setActiveWillConfig(PlayerEntity player, List<EnumDemonWillType> typeList);

	EnumReaderBoundaries setBlockRangeByBounds(PlayerEntity player, String range, BlockPos offset1, BlockPos offset2);

	List<EnumDemonWillType> getActiveWillConfig();

	default SoulTicket ticket(int amount)
	{
		return SoulTicket.block(getWorldObj(), getBlockPos(), amount);
	}

	AreaDescriptor getBlockRange(String range);

	void addBlockRanges(Map<String, AreaDescriptor> blockRanges);

	void addBlockRange(String range, AreaDescriptor defaultRange);

	void setBlockRanges(Map<String, AreaDescriptor> blockRanges);

	void setBlockRange(String range, AreaDescriptor defaultRange);

	Ritual getCurrentRitual();
}
