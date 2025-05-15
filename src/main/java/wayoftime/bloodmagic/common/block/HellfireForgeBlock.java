package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.blockentity.BMTiles;
import wayoftime.bloodmagic.common.blockentity.HellfireForgeTile;
import wayoftime.bloodmagic.util.helper.BlockEntityHelper;

public class HellfireForgeBlock extends Block implements EntityBlock {
    public static final VoxelShape BOX = box(1, 0, 1, 15, 12, 15);

    public HellfireForgeBlock() {
        super(Properties.of()
                .strength(2.0F, 5.0F)
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof HellfireForgeTile forge)) {
            return ItemInteractionResult.FAIL;
        }

        ItemStack forgeStack = forge.inv.getStackInSlot(HellfireForgeTile.OUTPUT_SLOT);

        if (player.isShiftKeyDown() && !forgeStack.isEmpty() && stack.isEmpty()) {
            player.setItemInHand(hand, forgeStack.copy());
            forge.inv.setStackInSlot(HellfireForgeTile.OUTPUT_SLOT, ItemStack.EMPTY);
            forge.setChanged();
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        Direction side = hitResult.getDirection();
        int slot = switch (side) {
            case UP -> {
                Vec3 relative = hitResult.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
                double x = relative.x - 0.5D;
                double z = relative.z - 0.5D;
                if (Math.abs(x) < 3/16D && Math.abs(z) < 3/16D && (stack.isEmpty() || forge.inv.isItemValid(HellfireForgeTile.GEM_SLOT, stack))) {
                    yield HellfireForgeTile.GEM_SLOT;
                }
                double max = Math.max(Math.abs(x), Math.abs(z));
                if (max == Math.abs(x)) {
                    yield x < 0 ? HellfireForgeTile.WEST : HellfireForgeTile.EAST;
                } else {
                    yield z < 0 ? HellfireForgeTile.NORTH : HellfireForgeTile.SOUTH;
                }
            }

            case DOWN -> HellfireForgeTile.OUTPUT_SLOT;
            case EAST -> HellfireForgeTile.EAST;
            case WEST -> HellfireForgeTile.WEST;
            case SOUTH -> HellfireForgeTile.SOUTH;
            case NORTH -> HellfireForgeTile.NORTH;
        };

        BloodMagic.LOGGER.info("got: {} which is {}", slot, Direction.from2DDataValue(slot));

        forgeStack = forge.inv.getStackInSlot(slot);
        if (forgeStack.isEmpty() && !stack.isEmpty()) {
            forge.inv.setStackInSlot(slot, stack.copy());
            player.setItemInHand(hand, ItemStack.EMPTY);
            forge.setChanged();
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else if (!forgeStack.isEmpty() && stack.isEmpty()) {
            forge.inv.setStackInSlot(slot, ItemStack.EMPTY);
            player.setItemInHand(hand, forgeStack.copy());
            forge.setChanged();
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.FAIL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HellfireForgeTile(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return BlockEntityHelper.getTicker(blockEntityType, BMTiles.HELLFIRE_FORGE_TYPE.get(), HellfireForgeTile::tick);
    }
}
