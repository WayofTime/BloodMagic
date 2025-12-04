package wayoftime.bloodmagic.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IRitualStone;

import java.util.List;

public class BlockRitualStone extends Block implements IRitualStone {
    private final EnumRuneType type;

    public BlockRitualStone(EnumRuneType type) {
        super(BlockBehaviour.Properties.of()
                .strength(2.0F, 5.0F)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops());
        this.type = type;
    }

    public EnumRuneType getRuneType() {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.bloodmagic.decoration.safe").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public boolean isRuneType(Level world, BlockPos pos, EnumRuneType runeType) {
        return type.equals(runeType);
    }

    @Override
    public void setRuneType(Level world, BlockPos pos, EnumRuneType runeType) {
        // TODO: Implement ritual stone type switching when scribe tools are added
        Block runeBlock = this;
        switch (runeType) {
            case AIR:
                runeBlock = BMBlocks.AIR_RITUAL_STONE.block().get();
                break;
            case BLANK:
                runeBlock = BMBlocks.BLANK_RITUAL_STONE.block().get();
                break;
            case DAWN:
                runeBlock = BMBlocks.DAWN_RITUAL_STONE.block().get();
                break;
            case DUSK:
                runeBlock = BMBlocks.DUSK_RITUAL_STONE.block().get();
                break;
            case EARTH:
                runeBlock = BMBlocks.EARTH_RITUAL_STONE.block().get();
                break;
            case FIRE:
                runeBlock = BMBlocks.FIRE_RITUAL_STONE.block().get();
                break;
            case WATER:
                runeBlock = BMBlocks.WATER_RITUAL_STONE.block().get();
                break;
        }

        world.setBlockAndUpdate(pos, runeBlock.defaultBlockState());
    }
}
