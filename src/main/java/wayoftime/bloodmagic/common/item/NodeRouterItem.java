package wayoftime.bloodmagic.common.item;

import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;

import java.util.List;

public class NodeRouterItem extends Item {

    public NodeRouterItem() {
        super(
                new Properties().stacksTo(1)
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getHand() == InteractionHand.OFF_HAND) {
            return InteractionResult.PASS;
        }
        ItemStack routerStack = context.getItemInHand();
        if (context.isSecondaryUseActive()) {
            routerStack.remove(BMDataComponents.STORED_POSITION);
        } else {
            if (!routerStack.has(BMDataComponents.STORED_POSITION)) {
                // first node, this is the parent
            } else {
                // second node, this is the child
            }
        }

        return InteractionResult.PASS;
    }
}
