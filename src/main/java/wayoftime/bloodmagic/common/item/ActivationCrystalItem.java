package wayoftime.bloodmagic.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.mutable.MutableBoolean;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.datacomponent.Binding;
import wayoftime.bloodmagic.api.helper.SoulNetworkHelper;
import wayoftime.bloodmagic.api.ritual.RitualStructure;
import wayoftime.bloodmagic.common.blockentity.MasterRitualTile;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;

import java.util.Map;
import java.util.Optional;

public class ActivationCrystalItem extends Item {
    public ActivationCrystalItem() {
        super(new Properties()
                .component(BMDataComponents.BINDING, Binding.EMPTY)
                .stacksTo(1)
        );
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity be = level.getBlockEntity(pos);
        if (be != null && be instanceof MasterRitualTile mrs) {
            mrs.tryActivate(context.getItemInHand().getOrDefault(BMDataComponents.BINDING, Binding.EMPTY));
        }

        return InteractionResult.PASS;
    }
}
