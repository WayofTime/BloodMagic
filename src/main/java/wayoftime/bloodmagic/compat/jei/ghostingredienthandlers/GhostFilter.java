package wayoftime.bloodmagic.compat.jei.ghostingredienthandlers;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.screens.ScreenFilter;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.network.FilterGhostSlotPacket;
import wayoftime.bloodmagic.util.GhostItemHelper;

import java.util.ArrayList;
import java.util.List;

public class GhostFilter implements IGhostIngredientHandler<ScreenFilter>
{

	@Override
	public <I> List<Target<I>> getTargetsTyped(ScreenFilter gui, ITypedIngredient<I> typedIngredient, boolean doStart) {
		List<Target<I>> targets = new ArrayList<>();

		for (Slot slot : gui.getMenu().slots)
		{
			if (!slot.isActive())
			{
				continue;
			}

			Rect2i bounds = new Rect2i(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 16, 16);

			if (typedIngredient.getIngredient() instanceof ItemStack && (slot instanceof ContainerFilter.SlotGhostItem))
			{
				targets.add(new Target<I>()
				{
					@Override
					public Rect2i getArea()
					{
						return bounds;
					}

					@Override
					public void accept(I ingredient)
					{
						ItemStack stack = (ItemStack) ingredient;
						BloodMagic.packetHandler.sendToServer(new FilterGhostSlotPacket(slot.index, stack));

						GhostItemHelper.setItemGhostAmount(stack, 0);
						stack.setCount(1);
						slot.set(stack);

					}
				});
			}
		}

		return targets;
	}

	@Override
	public void onComplete()
	{

	}
}
