package wayoftime.bloodmagic.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.blockentity.AlchemyTableTile;
import wayoftime.bloodmagic.common.blockentity.BMTiles;

public class AlchemyTableBlock extends BaseEntityBlock {
    public static final MapCodec<AlchemyTableBlock> CODEC = simpleCodec(AlchemyTableBlock::new);
    public static final DirectionProperty DIRECTION = DirectionProperty.create("direction", Direction.Plane.HORIZONTAL);
    public static final BooleanProperty INVISIBLE = BooleanProperty.create("invisible");
    protected static final VoxelShape BODY = Block.box(1, 0, 1, 15, 15, 15);

    public AlchemyTableBlock() {
        super(BlockBehaviour.Properties.of().strength(2.0F, 5.0F).noOcclusion().isRedstoneConductor(AlchemyTableBlock::isntSolid).isViewBlocking(AlchemyTableBlock::isntSolid).requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(DIRECTION, Direction.NORTH).setValue(INVISIBLE, false));
    }

    public AlchemyTableBlock(BlockBehaviour.Properties properties) {
        super(properties.strength(2.0F, 5.0F).noOcclusion().isRedstoneConductor(AlchemyTableBlock::isntSolid).isViewBlocking(AlchemyTableBlock::isntSolid).requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(DIRECTION, Direction.NORTH).setValue(INVISIBLE, false));
    }

    private static boolean isntSolid(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BODY;
    }

    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyTableTile(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(INVISIBLE)) {
            return null;
        }
        return createTickerHelper(type, BMTiles.ALCHEMY_TABLE_TYPE.get(), AlchemyTableTile::tick);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (world.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof AlchemyTableTile tableTile) {
            if (tableTile.isSlave()) {
                BlockEntity masterTile = world.getBlockEntity(tableTile.getConnectedPos());
                if (masterTile instanceof MenuProvider menuProvider) {
                    player.openMenu(menuProvider);
                }
            } else {
                player.openMenu(tableTile);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(DIRECTION, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION, INVISIBLE);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        AlchemyTableTile tile = (AlchemyTableTile) world.getBlockEntity(pos);
        if (tile != null) {
            BlockPos connectedPos = tile.getConnectedPos();
            if (connectedPos.equals(BlockPos.ZERO)) {
                return;
            }
            BlockEntity connectedTile = world.getBlockEntity(connectedPos);
            if (!(connectedTile instanceof AlchemyTableTile && ((AlchemyTableTile) connectedTile).getConnectedPos().equals(pos))) {
                tile.getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState) {
        AlchemyTableTile tile = (AlchemyTableTile) world.getBlockEntity(blockPos);
        if (tile != null && !tile.isSlave()) {
            tile.dropItems();
        }
        super.destroy(world, blockPos, blockState);
    }

    @Override
    protected void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof AlchemyTableTile alchemyTable && !alchemyTable.isSlave()) {
                alchemyTable.dropItems();
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }
}
