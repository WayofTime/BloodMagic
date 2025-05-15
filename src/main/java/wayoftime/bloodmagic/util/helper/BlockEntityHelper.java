package wayoftime.bloodmagic.util.helper;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityHelper {
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> getTicker(BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

    public static MutableComponent translatableHover(String key, Object... args) {
        return Component.translatable(key, args).withStyle(ChatFormatting.GRAY);
    }

    public static MutableComponent translatableHover(String key) {
        return Component.translatable(key).withStyle(ChatFormatting.GRAY);
    }
}
