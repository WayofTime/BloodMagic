package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.components.Button;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.routing.IItemFilter;

public interface IItemFilterProvider extends IRoutingFilterProvider
{
	IItemFilter getInputItemFilter(ItemStack stack, BlockEntity tile, IItemHandler handler);

	IItemFilter getOutputItemFilter(ItemStack stack, BlockEntity tile, IItemHandler handler);

	// Only used for filters that check ItemStacks and do not transfer items to/from
	// a connected inventory.
	IItemFilter getUninitializedItemFilter(ItemStack stack);

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

	IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount);
}
