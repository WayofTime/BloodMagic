package wayoftime.bloodmagic.common.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.util.helper.FilterHelper;

import static wayoftime.bloodmagic.util.helper.FilterHelper.*;

public class FilterMenu extends AbstractGhostMenu<FilterMenu> {

    public final boolean isTag;
    public final boolean isEnchant;
    public FilterMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        super(BMMenus.ITEM_FILTER.get(), containerId, playerInv, FilterHelper.CONTAINER_DATA_SIZE, 3, 3, 110, 15, 105, buf.readInt());
        isTag = buf.readBoolean();
        isEnchant = buf.readBoolean();
    }

    public FilterMenu(int containerId, Inventory playerInv, GhostItemHandler filterInv, FilterData filterData, int slot, boolean tag, boolean enchant) {
        super(BMMenus.ITEM_FILTER.get(), containerId, playerInv, filterData, filterInv, 3, 3, 110, 15, 105, slot);
        isTag = tag;
        isEnchant = enchant;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        return switch (id) {
            case BUTTON_BWLIST -> {
                int state = tracker.get(DATA_BWLIST);
                setData(DATA_BWLIST, state == 0 ? 1 : 0);
                yield true;
            }

            case BUTTON_TAG -> {
                int slot = tracker.get(DATA_SLOT);
                int state = tracker.get(DATA_TAG + slot);
                ItemStack contentStack = handler.getStackInSlot(slot);
                state = FilterHelper.cycleTag(contentStack, state);
                setData(DATA_TAG + slot, state);
                yield true;
            }

            case BUTTON_ENCHANT -> {
                int slot = tracker.get(DATA_SLOT);
                int state = tracker.get(DATA_ENCHANT + slot);
                ItemStack contentStack = handler.getStackInSlot(slot);
                state = FilterHelper.cycleEnchant(contentStack, state);
                setData(DATA_ENCHANT + slot, state);
                yield true;
            }

            case BUTTON_ENCHANT_LVL -> {
                int slot = tracker.get(DATA_SLOT);
                int state = tracker.get(DATA_ENCHANT_LVL + slot);
                setData(DATA_ENCHANT_LVL + slot, state == 0 ? 1 : 0);
                yield true;
            }

            default -> false;
        };
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
