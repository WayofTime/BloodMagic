package wayoftime.bloodmagic.common.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class ArcaneAshesItem extends BlockItem {
    public ArcaneAshesItem(Block block) {
        super(block, new Properties()
                .stacksTo(1)
                .durability(42) // 1.20 has 20 but that seems low given that we want to give it more stuff to do
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        context.getItemInHand().hurtAndBreak(1, context.getPlayer(), context.getItemInHand().getEquipmentSlot());
        return super.useOn(context);
    }
}
