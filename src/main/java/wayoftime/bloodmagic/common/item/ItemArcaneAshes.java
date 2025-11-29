package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.blockentity.AlchemyArrayTile;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemArcaneAshes extends Item {
    public ItemArcaneAshes() {
        super(new Item.Properties().stacksTo(1).durability(20));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.bloodmagic.arcaneAshes").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos newPos = context.getClickedPos().relative(context.getClickedFace());
        Level world = context.getLevel();
        Player player = context.getPlayer();

        if (world.isEmptyBlock(newPos)) {
            if (!world.isClientSide) {
                Direction rotation = Direction.fromYRot(player.getYHeadRot());
                world.setBlockAndUpdate(newPos, BMBlocks.ALCHEMY_ARRAY.get().defaultBlockState());
                BlockEntity tile = world.getBlockEntity(newPos);
                if (tile instanceof AlchemyArrayTile arrayTile) {
                    arrayTile.setRotation(rotation);
                }

                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
