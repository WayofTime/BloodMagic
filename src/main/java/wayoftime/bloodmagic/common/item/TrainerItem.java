package wayoftime.bloodmagic.common.item;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.UpgradeLimits;
import wayoftime.bloodmagic.common.datacomponent.UpgradeTome;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.menu.GhostItemHandler;
import wayoftime.bloodmagic.common.menu.TrainerMenu;

import java.util.List;

public class TrainerItem extends Item {
    public TrainerItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            ItemStack chest = LivingHelper.getChest(player);
            if (LivingHelper.isNeverValid(chest)) {
                // cannot operate
                return InteractionResultHolder.fail(stack);
            }

            GhostItemHandler handler = new GhostItemHandler(16) {
                @Override
                public boolean isItemValid(int slot, ItemStack stack) {
                    return stack.is(BMItems.UPGRADE_TOME) && stack.has(BMDataComponents.UPGRADE_TOME_DATA);
                }
            };

            UpgradeLimits limits = chest.getOrDefault(BMDataComponents.LIMITS, UpgradeLimits.EMPTY);
            SimpleContainerData data = new SimpleContainerData(19) {
                @Override
                public void set(int index, int value) {
                    if (index == 2 && value == 1) {
                        Object2FloatOpenHashMap<Holder<LivingUpgrade>> map = new Object2FloatOpenHashMap<>();
                        for (int i = 0; i < handler.getSlots(); i++) {
                            ItemStack ghostStack = handler.getStackInSlot(i);
                            if (ghostStack.isEmpty()) {
                                continue;
                            }
                            UpgradeTome tome = ghostStack.get(BMDataComponents.UPGRADE_TOME_DATA);
                            if (tome == null) {
                                continue;
                            }
                            map.put(tome.upgrade(), LivingHelper.getExpForLevel(tome.upgrade(), this.get(3 + i)));
                        }
                        chest.set(BMDataComponents.LIMITS, new UpgradeLimits(this.get(1) == TrainerMenu.ALLOW, map));
                        return;
                    }
                    super.set(index, value);
                }
            };
            List<Pair<Integer, Integer>> start = limits.fillData(handler);
            start.forEach(pair -> data.set(pair.first(), pair.second()));

            serverPlayer.openMenu(
                    new SimpleMenuProvider(
                            (id, inv, playerIn) -> new TrainerMenu(id, inv, handler, data, playerIn.getInventory().selected),
                            Component.translatable(getDescriptionId())
                    ),
                    buf -> buf.writeInt(serverPlayer.getInventory().selected)
            );
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
