package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;

public interface IRoutingFilterProvider
{
	/**
	 * Translates the inputed keyStack into the proper filtered key
	 *
	 * @param filterStack
	 * @param keyStack
	 * @return A new ItemStack which modifies the keyStack
	 */
	ItemStack getContainedStackForItem(ItemStack filterStack, ItemStack keyStack);

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
}
