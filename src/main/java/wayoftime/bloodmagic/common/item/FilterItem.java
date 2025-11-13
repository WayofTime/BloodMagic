package wayoftime.bloodmagic.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;

public class FilterItem extends Item {

    private final String name;
    public FilterItem(Properties properties, String name) {
        super(properties);
        this.name = name;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!(stack.getItem() instanceof FilterItem)) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide) {
            /*
            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, player1) -> new FilterMenu(containerId, playerInventory, stack.get(BMDataComponents.FILTER_INVENTORY)),
                    Component.translatable("gui.bloodmagic.filter." + name))
            );
             */
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
