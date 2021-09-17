package wayoftime.bloodmagic.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class BlockAlternator extends Block {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockAlternator(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(ACTIVE, false));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(ACTIVE, false);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 20);
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
            worldIn.getPendingBlockTicks().scheduleTick(pos, this, 1);

    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {
            tick(state, (ServerWorld) worldIn, pos);
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    public void tick(BlockState state, ServerWorld world, BlockPos pos){
        if (world.isRemote){
            return;
        }

        if(state.get(ACTIVE)){
            world.setBlockState(pos, state.with(ACTIVE, false));
        }
        else {
            world.setBlockState(pos, state.with(ACTIVE, true));
        }
        world.getPendingBlockTicks().scheduleTick(pos, this, 20);
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    public int getStrongPower(BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return state.get(ACTIVE) ? 15 : 0;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }
}
