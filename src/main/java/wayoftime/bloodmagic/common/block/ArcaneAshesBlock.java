package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.blockentity.ArcaneAshesTile;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.recipe.ash.AshCraftingRecipe;
import wayoftime.bloodmagic.common.recipe.ash.AshInput;
import wayoftime.bloodmagic.common.recipe.ash.AshRecipe;
import wayoftime.bloodmagic.util.BlockEntityHelper;

public class ArcaneAshesBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<AshMode> MODE = EnumProperty.create("state", AshMode.class);

    public ArcaneAshesBlock() {
        super(Properties.of()
                .instabreak() // feels right not to have to mine it
                .noCollission()
                .ignitedByLava()
        );
    }

    protected static final VoxelShape BOX = Block.box(0, 0, 0, 16, 1, 16);
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE);
    }

    private static RecipeManager.CachedCheck<AshInput, AshRecipe> lookup = null;
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ArcaneAshesTile ashes)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(BMBlocks.ARCANE_ASHES.asItem())) {
            return ItemInteractionResult.CONSUME; // do not want to place ashes
        }
        if (ashes.inv.getStackInSlot(0).isEmpty()) {
            ashes.inv.setStackInSlot(0, heldItem.split(1));
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else if (ashes.inv.getStackInSlot(1).isEmpty()) {
            ashes.inv.setStackInSlot(1, heldItem.split(1));

            if (ashes.inv.getStackInSlot(0).is(Items.POTION /*BMItems.FLASK.get()*/)) {
                // TODO implement beacon effect
            } else {
                AshInput input = new AshInput(ashes.inv);
                if (lookup == null) {
                    lookup = RecipeManager.createCheck(BMRecipes.ASH_TYPE.get());
                }
                RecipeHolder<AshRecipe> holder = lookup.getRecipeFor(input, level).orElse(null);
                if (holder != null) {
                    ashes.inv.setStackInSlot(2, holder.value().assemble(input, level.registryAccess()));
                    level.sendBlockUpdated(pos, state, state.setValue(MODE, AshMode.CRAFTING), UPDATE_ALL);
                    level.scheduleTick(pos, state.getBlock(), 1);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ArcaneAshesTile ashes)) {
            return;
        }

        AshMode mode = state.getValue(MODE);
        switch (mode) {
            case CRAFTING -> {
                if (++ashes.progress >= 200) {
                    ItemStack output = ashes.inv.getStackInSlot(2);
                    if (level.isClientSide) {
                        Vec3 vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5, 1 / 16f, 0.5).offsetRandom(level.random, 0.7f);
                        ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), output);
                        itementity.setDefaultPickUpDelay();
                        level.addFreshEntity(itementity);
                    }
                    ashes.progress = 0;
                    ashes.inv.setStackInSlot(0, ItemStack.EMPTY);
                    ashes.inv.setStackInSlot(1, ItemStack.EMPTY);
                    ashes.inv.setStackInSlot(2, ItemStack.EMPTY);
                    level.sendBlockUpdated(pos, state, state.setValue(MODE, AshMode.IDLE), UPDATE_ALL);
                    return;
                }
                level.scheduleTick(pos, state.getBlock(), 1);
            }

            case BEACON -> {
                // TODO implement
            }

            case IDLE -> {
                // why has tick when idle?
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextFloat() < 0.3f) {
            switch (state.getValue(MODE)) {
                case CRAFTING -> level.addParticle(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.1f), pos.getX() + 0.5, pos.getY() + 0.1f, pos.getZ() + 0.5, 0.2, 0.2, 0.2);
                case BEACON -> {
                }
                case IDLE -> {
                }
            }
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // TODO implement
        //  check if valid trap items -> do trap; else do nothing
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().
                setValue(MODE, AshMode.IDLE)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof ArcaneAshesTile ash) {
                BlockEntityHelper.dropContents(level, pos, ash.inv, 2);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ArcaneAshesTile(pos, state);
    }

    public enum AshMode implements StringRepresentable {
        IDLE,
        CRAFTING,
        BEACON;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
