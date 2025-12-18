package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;
import wayoftime.bloodmagic.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ItemStandardFilter extends ItemRouterFilter implements ICompositeItemFilterProvider
{
    // of course this inherits from composite... anyways, copy-paste from IRF
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND) {
            return InteractionResultHolder.pass(stack);
        }

		if (!world.isClientSide)
		{
			if (player instanceof ServerPlayer)
			{
				NetworkHooks.openScreen((ServerPlayer) player, this, buf -> {
                    buf.writeBoolean(hasTagButton());
                    buf.writeBoolean(hasEnchantButtons());
                });
			}
		}

		return new InteractionResultHolder<>(InteractionResult.sidedSuccess(world.isClientSide), stack);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.bloodmagic.filter.standard");
    }

    @OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack filterStack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.basicfilter.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
		}

		List<ItemStack> nestedFilters = getNestedFilters(filterStack);
		if (nestedFilters.size() > 0)
		{
			boolean sneaking = Screen.hasShiftDown();
			if (!sneaking)
			{
				tooltip.add(Component.translatable("tooltip.bloodmagic.extraInfo").withStyle(ChatFormatting.BLUE));
			} else
			{
				tooltip.add(Component.translatable("tooltip.bloodmagic.contained_filters").withStyle(ChatFormatting.BLUE));
				for (ItemStack nestedStack : nestedFilters)
				{
					tooltip.add(nestedStack.getHoverName());
				}
			}
		}

		int whitelistState = this.getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
		boolean isWhitelist = whitelistState == 0;

		if (isWhitelist)
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.filter.whitelist").withStyle(ChatFormatting.GRAY));
		} else
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.filter.blacklist").withStyle(ChatFormatting.GRAY));
		}

		InventoryFilter inv = getInv(filterStack);
		for (int i = 0; i < inv.getSlots(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (isWhitelist)
			{
				int amount = GhostItemHelper.getItemGhostAmount(stack);
				if (amount > 0)
				{
					tooltip.add(Component.translatable("tooltip.bloodmagic.filter.count", amount, stack.getHoverName()));
				} else
				{
					tooltip.add(Component.translatable("tooltip.bloodmagic.filter.all", stack.getHoverName()));
				}
			} else
			{
				tooltip.add(stack.getHoverName());
			}
		}
	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount)
	{
		return new BasicFilterKey(ghostStack, amount);
	}

    public List<ItemStack> getNestedFilters(ItemStack mainFilterStack)
    {
        List<ItemStack> nestedFilters = new ArrayList<>();
        InventoryFilter inv = getInv(mainFilterStack);
        for (int i = 0; i < inv.getSlots(); i++)
        {
            ItemStack testStack = inv.getStackInSlot(i);
            if (testStack.isEmpty())
            {
                continue;
            }

            if (testStack.getItem() instanceof INestableItemFilterProvider)
            {
                nestedFilters.add(testStack);
            }
        }

        return nestedFilters;
    }

    @Override
    public boolean canReceiveNestedFilter(ItemStack mainFilterStack, ItemStack nestedFilterStack)
    {
        if (nestedFilterStack.isEmpty())
        {
            return false;
        } else if (!(nestedFilterStack.getItem() instanceof INestableItemFilterProvider))
        {
            return false;
        }

        boolean hasEmpty = false;

        InventoryFilter inv = getInv(mainFilterStack);
        for (int i = 0; i < inv.getSlots(); i++)
        {
            ItemStack testStack = inv.getStackInSlot(i);
            if (testStack.isEmpty())
            {
                hasEmpty = true;
                continue;
            }

            if (testStack.getItem().equals(nestedFilterStack.getItem()))
            {
                return false;
            }
        }

        return hasEmpty;
    }

    @Override
    public ItemStack nestFilter(ItemStack mainFilterStack, ItemStack nestedFilterStack)
    {
        if (canReceiveNestedFilter(mainFilterStack, nestedFilterStack))
        {
            ItemStack copyStack = mainFilterStack.copy();

            InventoryFilter inv = getInv(copyStack);
            for (int i = 0; i < inv.getSlots(); i++)
            {
                ItemStack testStack = inv.getStackInSlot(i);
                if (testStack.isEmpty())
                {
                    inv.setStackInSlot(i, nestedFilterStack);
                    CompoundTag tag = copyStack.getOrCreateTag();
                    tag.put(Constants.NBT.ITEM_INVENTORY, inv.serializeNBT());
                    return copyStack;
                }
            }
        }

        return ItemStack.EMPTY;
    }
}
