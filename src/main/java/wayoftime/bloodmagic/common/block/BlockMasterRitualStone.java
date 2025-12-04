package wayoftime.bloodmagic.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

// TODO: Implement full ritual system - this is a placeholder block
// Full implementation needs: TileMasterRitualStone, Ritual system, RitualHelper, etc.
public class BlockMasterRitualStone extends Block {
    public final boolean isInverted;

    public BlockMasterRitualStone(boolean isInverted) {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.STONE)
                .strength(2.0F, 5.0F)
                .requiresCorrectToolForDrops());
        this.isInverted = isInverted;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.bloodmagic.decoration.safe").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Ritual system not yet implemented").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            // TODO: Implement ritual activation with activation crystal
            player.displayClientMessage(Component.literal("Ritual system not yet implemented").withStyle(ChatFormatting.RED), true);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
