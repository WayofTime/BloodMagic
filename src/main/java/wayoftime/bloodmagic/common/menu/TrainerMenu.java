package wayoftime.bloodmagic.common.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.UpgradeTome;
import wayoftime.bloodmagic.common.living.LivingHelper;

public class TrainerMenu extends AbstractGhostMenu<TrainerMenu> {

    // CLIENT constructor
    public TrainerMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        // buf ->  int heldSlot
        super(BMMenus.TRAINER.get(), containerId, playerInv, 3 + 16, 4, 4, 89, 15, 105, buf.readInt());
    }

    // SERVER constructor
    public TrainerMenu(int containerId, Inventory playerInv, GhostItemHandler handler, ContainerData trainerData, int heldSlot) {
        super(BMMenus.TRAINER.get(), containerId, playerInv, trainerData, handler, 4, 4, 89, 15, 105, heldSlot);
    }

    @Override
    public void updateGhostSelection(int previousSlot, int currentSlot) {
        if (previousSlot < 0) {
            return;
        }
        ItemStack old = this.handler.getStackInSlot(previousSlot);
        if (old.isEmpty()) {
            return;
        }
        UpgradeTome tome = old.get(BMDataComponents.UPGRADE_TOME_DATA);
        if (tome == null) {
            return;
        }
        old.set(BMDataComponents.UPGRADE_TOME_DATA, new UpgradeTome(tome.upgrade(), LivingHelper.getExpForLevel(tome.upgrade(), getData(3 + previousSlot))));
        this.slots.get(previousSlot).set(old);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        int slot = 3 + getLastGhostSlotClicked();
        return switch (id) {
            case 1 -> {
                setData(slot, getData(slot) -1);
                yield true;
            }
            case 2 -> {
                setData(slot, getData(slot) +1);
                yield true;
            }
            case 3 -> {
                setData(1, isAllowOthers() ? DENY : ALLOW);
                yield true;
            }
            case 4 -> {
                setData(2, 1);
                yield true;
            }

            default -> false;
        };
    }

    public static final int ALLOW = 0;
    public static final int DENY = 1;
    public boolean isAllowOthers() {
        boolean res = getData(1) == ALLOW;
        BloodMagic.LOGGER.info("isAllowOthers -> {}", res);
        return res;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
