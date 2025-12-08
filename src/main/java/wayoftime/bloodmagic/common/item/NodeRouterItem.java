package wayoftime.bloodmagic.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.blockentity.MasterNodeTile;
import wayoftime.bloodmagic.common.blockentity.RoutingNodeTile;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.util.ChatUtil;

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
            if (context.getLevel().isClientSide) {
                ChatUtil.sendChat(context.getPlayer(), ChatUtil.translatableHover("tooltip.bloodmagic.router.pos_cleared"));
            }
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
        } else {
            BlockPos clickedPos = context.getClickedPos();
            Level level = context.getLevel();
            if (!(level.getBlockEntity(clickedPos) instanceof RoutingNodeTile clickedNode)) {
                if (level.isClientSide) {
                    ChatUtil.sendChat(context.getPlayer(), ChatUtil.translatableHover("tooltip.bloodmagic.router.no_node", ChatUtil.posString(clickedPos)));
                }
                return InteractionResult.FAIL;
            }
            if (!routerStack.has(BMDataComponents.STORED_POSITION)) {
                // first node, this is the parent
                routerStack.set(BMDataComponents.STORED_POSITION, new GlobalPos(context.getLevel().dimension(), clickedPos));
                if (level.isClientSide) {
                    ChatUtil.sendChat(context.getPlayer(), ChatUtil.translatableHover("tooltip.bloodmagic.router.pos_set", ChatUtil.posString(clickedPos)));
                }
            } else {
                // second node, this is the child
                if (clickedNode instanceof MasterNodeTile) {
                    if (level.isClientSide) {
                        ChatUtil.sendChat(context.getPlayer(), Component.translatable("tooltip.bloodmagic.router.master_child"));
                    }
                    return InteractionResult.FAIL;
                }

                GlobalPos storedPos = routerStack.get(BMDataComponents.STORED_POSITION);
                if (clickedPos.distSqr(storedPos.pos()) > 16 * 16) {
                    if (level.isClientSide) {
                        ChatUtil.sendChat(context.getPlayer(), Component.translatable("tooltip.bloodmagic.router.distance"));
                    }
                    return InteractionResult.FAIL;
                }

                if (clickedPos.equals(storedPos.pos())) {
                    if (level.isClientSide) {
                        ChatUtil.sendChat(context.getPlayer(), Component.translatable("tooltip.bloodmagic.router.same"));
                    }
                    return InteractionResult.FAIL;
                }

                if (!(level.getBlockEntity(storedPos.pos()) instanceof RoutingNodeTile)) {
                    if (level.isClientSide) {
                        ChatUtil.sendChat(context.getPlayer(), Component.translatable("tooltip.bloodmagic.router.no_node", ChatUtil.posString(storedPos.pos())));
                    }
                    return InteractionResult.FAIL;
                }

                if (!clickedNode.addToNetwork(storedPos.pos())) {
                    return InteractionResult.FAIL;
                }
                // if chaining nodes together for long distance, having this active is good
                // if connecting x nodes to the same parent having this active is annoying
                // perhaps client config? is client config available on server? probably gonna need to send a packet from client
                // TODO decide how to handle this
                routerStack.remove(BMDataComponents.STORED_POSITION);
                if (level.isClientSide) {
                    ChatUtil.sendChat(context.getPlayer(), Component.translatable("tooltip.bloodmagic.router.connected"));
                    ChatUtil.sendChat(context.getPlayer(), Component.translatable("tooltip.bloodmagic.router.pos_cleared"));
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}
