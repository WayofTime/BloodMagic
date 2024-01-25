package wayoftime.bloodmagic.compat.jei.ghostingredienthandlers;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.screens.ScreenFilter;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.network.FilterGhostSlotPacket;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class GhostFilter implements IGhostIngredientHandler<ScreenFilter>
{

	@Override
	public <I> List<Target<I>> getTargets(ScreenFilter gui, I ingredient, boolean doStart)
	{
		List<Target<I>> targets = new ArrayList<>();
		for (Slot slot : gui.getMenu().slots)
		{
			if (!slot.isActive())
			{
				continue;
			}

			Rect2i bounds = new Rect2i(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 16, 16);

			if (ingredient instanceof ItemStack && (slot instanceof ContainerFilter.SlotGhostItem) && gui.getFilterType() == 1)
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

			Optional<IFluidHandlerItem> optional = Optional.empty();
			if (ingredient instanceof ItemStack)
				optional = FluidUtil.getFluidHandler((ItemStack) ingredient).resolve();

			if ((ingredient instanceof FluidStack || (ingredient instanceof ItemStack && optional.isPresent())) && (slot instanceof ContainerFilter.SlotGhostItem) && gui.getFilterType() == 2)
			{
				if (ingredient instanceof ItemStack)
				{
					IFluidHandlerItem handler = optional.get();
					boolean hasFluid = false;
					for (int i = 0; i < handler.getTanks(); i++)
					{
						FluidStack testStack = handler.getFluidInTank(i);
						if (!testStack.isEmpty())
						{
							hasFluid = true;
							break;
						}
					}

					if (!hasFluid)
					{
						continue;
					}
				}

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
						ItemStack stack = ItemStack.EMPTY;
						if (ingredient instanceof FluidStack)
						{
							stack = ((FluidStack) ingredient).getFluid().getBucket().getDefaultInstance();
						} else if (ingredient instanceof ItemStack)
						{
							stack = (ItemStack) ingredient;
						}
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
