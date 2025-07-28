package wayoftime.bloodmagic.common.block.decoration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

public class BlockWillType extends Block {

    public static final EnumProperty<EnumDemonWillType> WILL_TYPE = EnumProperty.create("will_type", EnumDemonWillType.class);

    public BlockWillType(Properties properties) {
        super(properties);
        registerDefaultState(this.getStateDefinition().any().setValue(WILL_TYPE, EnumDemonWillType.DEFAULT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WILL_TYPE);
    }
}
