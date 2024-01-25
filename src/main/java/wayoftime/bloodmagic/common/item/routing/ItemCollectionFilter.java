package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.button.FilterButtonTogglePress;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.common.routing.BasicItemFilter;
import wayoftime.bloodmagic.common.routing.IRoutingFilter;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;
import wayoftime.bloodmagic.util.Utils;

public class ItemCollectionFilter extends Item implements MenuProvider, IRoutingFilterProvider
{

    public ItemCollectionFilter()
    {
        super(new Item.Properties().stacksTo(16).tab(BloodMagic.TAB));
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack filterStack, Level world, List<Component> tooltip, TooltipFlag flag)
    {
        tooltip.add(new TranslatableComponent("tooltip.bloodmagic.collectionfilter.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

        if (filterStack.getTag() == null)
        {
            return;
        }

        boolean isSneaking = Screen.hasShiftDown();
        if (!isSneaking)
        {
            tooltip.add(new TranslatableComponent("tooltip.bloodmagic.extraInfo").withStyle(ChatFormatting.BLUE));
        } else
        {
            tooltip.add(new TranslatableComponent("tooltip.bloodmagic.contained_filters").withStyle(ChatFormatting.BLUE));
            ItemInventory inv = new InventoryFilter(filterStack);
            for (int i = 0; i < inv.getContainerSize(); i++)
            {
                ItemStack containedFilterStack = inv.getItem(i);
                if (containedFilterStack.isEmpty())
                {
                    continue;
                }
                tooltip.add(containedFilterStack.getHoverName());
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide)
        {
            Utils.setUUID(stack);
            if (player instanceof ServerPlayer)
            {
                NetworkHooks.openGui((ServerPlayer) player, this, buf -> buf.writeItemStack(stack, false));
            }
        }

        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public ItemStack getContainedStackForType(ItemStack filterStack, ItemStack keyStack)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public List<IRoutingFilter> getInputFilter(ItemStack mainFilterStack, BlockEntity tile, Direction side)
    {
        ItemInventory inv = new InventoryFilter(mainFilterStack);
        List<IRoutingFilter> filterList = Lists.newArrayList();

        for (int i = 0; i < inv.getContainerSize(); i++)
        {
            ItemStack filterStack = inv.getItem(i);
            if (filterStack.isEmpty())
            {
                continue;
            }

            if (filterStack.getItem() instanceof IRoutingFilterProvider)
            {
                List<IRoutingFilter> sublist = ((IRoutingFilterProvider) filterStack.getItem()).getInputFilter(filterStack, tile, side);
                sublist.forEach(filterList::add);
            }
        }

        return filterList;
    }

    @Override
    public List<IRoutingFilter> getOutputFilter(ItemStack mainFilterStack, BlockEntity tile, Direction side)
    {
        ItemInventory inv = new InventoryFilter(mainFilterStack);
        List<IRoutingFilter> filterList = Lists.newArrayList();

        for (int i = 0; i < inv.getContainerSize(); i++)
        {
            ItemStack filterStack = inv.getItem(i);
            if (filterStack.isEmpty())
            {
                continue;
            }

            if (filterStack.getItem() instanceof IRoutingFilterProvider)
            {
                List<IRoutingFilter> sublist = ((IRoutingFilterProvider) filterStack.getItem()).getOutputFilter(filterStack, tile, side);
                sublist.forEach(filterList::add);
            }
        }
        return filterList;
    }

    @Override
    public IRoutingFilter<FluidStack> getUninitializedItemFilter(ItemStack filterStack)
    {
        IRoutingFilter<FluidStack> testFilter = getFilterTypeFromConfig(filterStack);

        List<IFilterKey> filteredList = new ArrayList<>();
        ItemInventory inv = new InventoryFilter(filterStack);

        for (int i = 0; i < inv.getContainerSize(); i++)
        {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty())
            {
                continue;
            }

            int amount = GhostItemHelper.getItemGhostAmount(stack);
            ItemStack ghostStack = GhostItemHelper.getSingleStackFromGhost(stack);

            IFilterKey key = getFilterKey(filterStack, i, ghostStack, amount);

            filteredList.add(key);
        }

        testFilter.initializeFilter(filteredList);

        return testFilter;
    }

    @Override
    public void setGhostItemAmount(ItemStack filterStack, int ghostItemSlot, int amount)
    {
    }

    @Override
    public List<Component> getTextForHoverItem(ItemStack filterStack, String buttonKey, int ghostItemSlot)
    {
        List<Component> componentList = new ArrayList<Component>();

        return componentList;
    }

    @Override
    public int getCurrentButtonState(ItemStack filterStack, String buttonKey, int ghostItemSlot)
    {
        CompoundTag tag = filterStack.getTag();
        if (tag != null)
        {
            if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
            {
                int state = tag.getInt(Constants.NBT.BLACKWHITELIST);
                return state;
            }
        }

        return -1;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<Pair<String, OnPress>> getButtonAction(ContainerFilter container)
    {
        List<Pair<String, OnPress>> buttonList = new ArrayList<Pair<String, OnPress>>();

        buttonList.add(Pair.of(Constants.BUTTONID.BLACKWHITELIST, new FilterButtonTogglePress(Constants.BUTTONID.BLACKWHITELIST, container)));

        return buttonList;
    }

    @Override
    public Pair<Integer, Integer> getTexturePositionForState(ItemStack filterStack, String buttonKey, int currentButtonState)
    {
        if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
        {
            switch (currentButtonState)
            {
            case 1:
                return Pair.of(176, 20);

            default:
                return Pair.of(176, 0);
            }
        }

        return Pair.of(0, 0);
    }

    @Override
    public int receiveButtonPress(ItemStack filterStack, String buttonKey, int ghostItemSlot, int currentButtonState)
    {
        CompoundTag tag = filterStack.getTag();
        if (tag == null)
        {
            filterStack.setTag(new CompoundTag());
            tag = filterStack.getTag();
        }

        if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
        {
            int nextState = 0;
            switch (currentButtonState)
            {
            case 0:
                nextState = 1;
                break;

            default:
                nextState = 0;
            }

            tag.putInt(Constants.NBT.BLACKWHITELIST, nextState);
            return nextState;
        }

        return -1;
    }

    @Override
    public boolean isButtonGlobal(ItemStack filterStack, String buttonKey)
    {
        return buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST);
    }

    @Override
    public IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount)
    {
        return new BasicFilterKey(ItemStack.EMPTY, 42);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
    {
        assert player.getCommandSenderWorld() != null;
        return new ContainerFilter(windowId, player, inventory, player.getMainHandItem());
    }

    @Override
    public Component getDisplayName()
    {
        return new TextComponent("Collection Filter");
    }

    @Override
    public IRoutingFilter getFilterTypeFromConfig(ItemStack filterStack)
    {
        return new BasicItemFilter();
    }
}
