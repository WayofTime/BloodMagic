package wayoftime.bloodmagic.common.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;

public class NodeRouterItem extends Item {

    public NodeRouterItem() {
        super(
                new Properties().stacksTo(1)
                        .component(BMDataComponents.)
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

    }
}
