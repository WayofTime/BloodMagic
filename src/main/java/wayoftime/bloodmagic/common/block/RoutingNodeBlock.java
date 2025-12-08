package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.blockentity.BMTiles;
import wayoftime.bloodmagic.common.blockentity.RoutingNodeTile;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.tag.BMTags;
import wayoftime.bloodmagic.util.helper.BlockEntityHelper;

public class RoutingNodeBlock extends Block implements EntityBlock {
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final EnumProperty<EnumWillType> WILL = EnumProperty.create("will_type", EnumWillType.class);
    public static final BooleanProperty RENDER_LINE = BooleanProperty.create("render_line");

    public static final VoxelShape SHAPE = Block.box(6, 6, 6, 10, 10, 10);
    public RoutingNodeBlock() {
        super(
                Properties.of()
                        .strength(2, 5)
                        .requiresCorrectToolForDrops()
                        .forceSolidOn() // not washed away by water
                        .dynamicShape() // not sure needed, but it should update its connections
                        .noOcclusion() // dont cull blocks
                        .pushReaction(PushReaction.BLOCK) // dont move, even if moving BEs is allowed
        );

        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(ENABLED, true)
                        .setValue(DOWN, false)
                        .setValue(UP, false)
                        .setValue(NORTH, false)
                        .setValue(SOUTH, false)
                        .setValue(WEST, false)
                        .setValue(EAST, false)
                        .setValue(WILL, EnumWillType.DEFAULT)
                        .setValue(RENDER_LINE, false)
        );
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return BlockEntityHelper.getTicker(blockEntityType, BMTiles.ROUTING_NODE.get(), RoutingNodeTile::tick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isShiftKeyDown()) {
            level.setBlock(pos, state.setValue(RENDER_LINE, true), UPDATE_ALL);
            level.scheduleTick(pos, state.getBlock(), 20 * 5);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(ENABLED) && random.nextFloat() < 0.3) {
            level.addParticle(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.1f), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5f, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, state.setValue(RENDER_LINE, false), UPDATE_ALL);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(BMTags.Items.SOUL_GEM)) {
            EnumWillType gemType = stack.getOrDefault(BMDataComponents.DEMON_WILL_TYPE, EnumWillType.DEFAULT);
            EnumWillType setType = state.getValue(WILL) == gemType ? EnumWillType.DEFAULT : gemType;
            level.setBlock(pos, state.setValue(WILL, setType), UPDATE_ALL);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        // adapted from HopperBlock
        boolean enabled = !level.hasNeighborSignal(pos);
        if (enabled != state.getValue(ENABLED)) {
            level.setBlock(pos, state.setValue(ENABLED, enabled), 2);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState returnState = defaultBlockState();
        returnState = returnState.setValue(ENABLED, !level.hasNeighborSignal(blockpos));

        for (Direction dir : Direction.values())
        {
            BlockPos attachedPos = blockpos.relative(dir);
            BlockState attachedState = level.getBlockState(attachedPos);
            BooleanProperty prop = switch (dir) {
                case DOWN -> DOWN;
                case EAST -> EAST;
                case NORTH -> NORTH;
                case SOUTH -> SOUTH;
                case UP -> UP;
                case WEST -> WEST;
            };
            returnState = returnState.setValue(prop, attachedState.isFaceSturdy(level, attachedPos, dir.getOpposite()));
        }

        return returnState;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
            BlockPos attachedPos = pos.relative(dir);
            BlockState attachedState = level.getBlockState(attachedPos);
            BooleanProperty prop = switch (dir) {
                case DOWN -> DOWN;
                case EAST -> EAST;
                case NORTH -> NORTH;
                case SOUTH -> SOUTH;
                case UP -> UP;
                case WEST -> WEST;
            };
            state = state.setValue(prop, attachedState.isFaceSturdy(level, attachedPos, dir.getOpposite()));

        return state;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENABLED, DOWN, UP, NORTH, SOUTH, WEST, EAST, WILL, RENDER_LINE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RoutingNodeTile(BMTiles.ROUTING_NODE.get(), pos, state);
    }
}
