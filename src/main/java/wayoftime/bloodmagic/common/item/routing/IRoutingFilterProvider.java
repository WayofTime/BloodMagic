package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.routing.IRoutingFilter;

public interface IRoutingFilterProvider
{

	ItemStack getContainedStackForType(ItemStack filterStack, ItemStack keyStack);

	List<IRoutingFilter> getInputFilter(ItemStack filterStack, BlockEntity tile, Direction side);

	List<IRoutingFilter> getOutputFilter(ItemStack filterStack, BlockEntity tile, Direction side);

	// Only used for filters that check ItemStacks and do not transfer items to/from
	// a connected inventory.
	IRoutingFilter getUninitializedItemFilter(ItemStack filterStack);

	void setGhostItemAmount(ItemStack filterStack, int ghostItemSlot, int amount);

	List<Component> getTextForHoverItem(ItemStack filterStack, String buttonKey, int ghostItemSlot);

	// -1 equals an invalid button input;
	int getCurrentButtonState(ItemStack filterStack, String buttonKey, int ghostItemSlot);

	@OnlyIn(Dist.CLIENT)
	List<Pair<String, Button.OnPress>> getButtonAction(ContainerFilter container);

	Pair<Integer, Integer> getTexturePositionForState(ItemStack filterStack, String buttonKey, int currentButtonState);

	// -1 equals an invalid button input;
	int receiveButtonPress(ItemStack filterStack, String buttonKey, int ghostItemSlot, int currentButtonState);

	boolean isButtonGlobal(ItemStack filterStack, String buttonKey);

	IRoutingFilter getFilterTypeFromConfig(ItemStack filterStack);

	IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount);
}
