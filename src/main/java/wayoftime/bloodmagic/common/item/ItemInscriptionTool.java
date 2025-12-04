package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.block.BlockRitualStone;
import wayoftime.bloodmagic.ritual.EnumRuneType;

import java.util.List;

public class ItemInscriptionTool extends Item {
    private final EnumRuneType type;

    public ItemInscriptionTool(EnumRuneType type) {
        super(new Item.Properties().stacksTo(1).durability(40));
        this.type = type;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        Player player = context.getPlayer();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockRitualStone ritualStone
                && !ritualStone.isRuneType(world, pos, type)) {
            ritualStone.setRuneType(world, pos, type);
            if (player != null && !player.isCreative()) {
                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.bloodmagic.inscriber.desc").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    public EnumRuneType getRuneType() {
        return type;
    }
}
