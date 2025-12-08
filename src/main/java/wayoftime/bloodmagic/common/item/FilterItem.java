package wayoftime.bloodmagic.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.menu.FilterData;
import wayoftime.bloodmagic.common.menu.FilterMenu;
import wayoftime.bloodmagic.common.menu.GhostItemHandler;
import wayoftime.bloodmagic.common.tag.BMTags;
import wayoftime.bloodmagic.util.helper.FilterHelper;

import java.util.ArrayList;
import java.util.List;

import static wayoftime.bloodmagic.util.helper.FilterHelper.*;

public class FilterItem extends Item {

    public FilterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack filterStack = player.getItemInHand(usedHand);
        if (!(filterStack.getItem() instanceof FilterItem)) {
            return InteractionResultHolder.pass(filterStack);
        }

        if (!level.isClientSide) {
            int slot = usedHand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 0;
            player.openMenu(getFilterProvider(filterStack, slot, BlockPos.ZERO), buf -> writeBuf(filterStack, slot, BlockPos.ZERO, buf));
        }
        return InteractionResultHolder.sidedSuccess(filterStack, level.isClientSide);
    }

    public static void writeBuf(ItemStack filterStack, int slot, BlockPos nodePos, RegistryFriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeBoolean(filterStack.is(BMTags.Items.TAG_FILTER));
        buf.writeBoolean(filterStack.is(BMTags.Items.ENCHANT_FILTER));
        buf.writeBlockPos(nodePos);
    }

    public static MenuProvider getFilterProvider(ItemStack filterStack, int slotHeldIn, BlockPos nodePos) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);
        if (filterStack.has(BMDataComponents.FILTER_INVENTORY)) {
            ItemContainerContents contents = filterStack.get(BMDataComponents.FILTER_INVENTORY);
            contents.copyInto(stacks);
        }

        GhostItemHandler filterInv = new GhostItemHandler(stacks) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return !stack.is(BMTags.Items.FILTERS);
            }

            @Override
            public void onContentsChanged(int slot) {
                filterStack.set(BMDataComponents.FILTER_INVENTORY, this.toContents());
            }
        };
        List<Integer> rawData = new ArrayList<>(CONTAINER_DATA_SIZE);
        for (int i = 0; i < CONTAINER_DATA_SIZE; i++) {
            rawData.add(0);
        }
        rawData.set(DATA_SLOT, filterStack.getOrDefault(BMDataComponents.GHOST_SLOT, 0)); // dont wanna deal with -1 here really
        rawData.set(DATA_BWLIST, filterStack.getOrDefault(BMDataComponents.FILTER_BLACKWHITELIST, 0));

        for (int i = 0; i < 9; i++) {
            ItemStack slotStack = stacks.get(i);
            rawData.set(DATA_TAG + i, slotStack.getOrDefault(BMDataComponents.FILTER_TAG_INDEX, 0));
            rawData.set(DATA_ENCHANT + i, slotStack.getOrDefault(BMDataComponents.FILTER_ENCHANT_INDEX, 0));
            rawData.set(DATA_ENCHANT_LVL + i, slotStack.getOrDefault(BMDataComponents.FILTER_ENCHANT_LEVEL, 0));
        }

        FilterData data = new FilterData(rawData) {
            @Override
            public void set(int index, int value) {
                super.set(index, value);

                if (index == DATA_SLOT) {
                    filterStack.set(BMDataComponents.GHOST_SLOT, value);
                    return;
                }

                if (index == DATA_BWLIST) {
                    filterStack.set(BMDataComponents.FILTER_BLACKWHITELIST, value);
                    return;
                }

                int kind = (index - 2) / 9;
                int offset = (index - 2) % 9;
                ItemStack targetStack = filterInv.getStackInSlot(offset);
                DataComponentType<Integer> component = switch (kind) {
                    case 0 -> {
                        if (value == 0) {
                            targetStack.remove(BMDataComponents.FILTER_TAG);
                        } else {
                            targetStack.set(BMDataComponents.FILTER_TAG, FilterHelper.getTag(targetStack, value));
                        }
                        yield BMDataComponents.FILTER_TAG_INDEX.get();
                    }
                    case 1 -> BMDataComponents.FILTER_ENCHANT_INDEX.get();
                    case 2 -> BMDataComponents.FILTER_ENCHANT_LEVEL.get();

                    default -> throw new IllegalStateException("got {} for index -> {} for kind");
                };
                targetStack.set(component, value);
                filterInv.setChanged(offset);
            }
        };

        return new SimpleMenuProvider(
                (containerId, playerInv, player) -> new FilterMenu(
                        containerId,
                        playerInv,
                        filterInv,
                        data,
                        slotHeldIn,
                        filterStack.has(BMDataComponents.FILTER_TAG_INDEX),
                        filterStack.has(BMDataComponents.FILTER_ENCHANT_INDEX),
                        nodePos
                ),
                filterStack.getDisplayName()
        );
    }
}
