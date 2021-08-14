package wayoftime.bloodmagic.common.item.routing;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.common.item.inventory.ContainerFilter;
import wayoftime.bloodmagic.common.routing.IItemFilter;

public interface IItemFilterProvider extends IRoutingFilterProvider
{
	IItemFilter getInputItemFilter(ItemStack stack, TileEntity tile, IItemHandler handler);

	IItemFilter getOutputItemFilter(ItemStack stack, TileEntity tile, IItemHandler handler);

	void setGhostItemAmount(ItemStack filterStack, int ghostItemSlot, int amount);

	ITextComponent getTextForHoverItem(ItemStack filterStack, String buttonKey, int ghostItemSlot);

	int getCurrentButtonState(ItemStack filterStack, String buttonKey, int ghostItemSlot);

	@OnlyIn(Dist.CLIENT)
	List<Pair<String, Button.IPressable>> getButtonAction(ContainerFilter container);

	Pair<Integer, Integer> getTexturePositionForState(String buttonKey, int currentButtonState);

	int receiveButtonPress(ItemStack filterStack, String buttonKey, int ghostItemSlot, int currentButtonState);

	boolean isButtonGlobal(ItemStack filterStack, String buttonKey);
}
