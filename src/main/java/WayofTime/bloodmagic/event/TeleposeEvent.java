package WayofTime.bloodmagic.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fired when a teleposer attempts to transpose two blocks. Use this to perform
 * special cleanup or compensation, or cancel it entirely to prevent the
 * transposition.
 */
@Cancelable
public class TeleposeEvent extends Event {
    public final World initalWorld;
    public final BlockPos initialBlockPos;
    public final IBlockState initialState;

    public final World finalWorld;
    public final BlockPos finalBlockPos;
    public final IBlockState finalState;

    public TeleposeEvent(World initialWorld, BlockPos initialBlockPos, World finalWorld, BlockPos finalBlockPos) {
        this.initalWorld = initialWorld;
        this.initialBlockPos = initialBlockPos;
        this.initialState = initialWorld.getBlockState(initialBlockPos);

        this.finalWorld = finalWorld;
        this.finalBlockPos = finalBlockPos;
        this.finalState = finalWorld.getBlockState(finalBlockPos);
    }

    public TileEntity getInitialTile() {
        return initalWorld.getTileEntity(initialBlockPos);
    }

    public TileEntity getFinalTile() {
        return finalWorld.getTileEntity(finalBlockPos);
    }

    /**
     * Fired when a Teleposer attempts to move an Entity between locations. Can
     * be cancelled to stop transposition.
     */
    @Cancelable
    public static class Ent extends TeleposeEvent {
        public final Entity entity;

        public Ent(Entity entity, World initialWorld, BlockPos initialBlockPos, World finalWorld, BlockPos finalBlockPos) {
            super(initialWorld, initialBlockPos, finalWorld, finalBlockPos);

            this.entity = entity;
        }

        @Override
        public TileEntity getInitialTile() throws IllegalArgumentException {
            throw new IllegalArgumentException("Attempted to get a TileEntity from an Entity Telepose Event.");
        }

        @Override
        public TileEntity getFinalTile() throws IllegalArgumentException {
            throw new IllegalArgumentException("Attempted to get a TileEntity from an Entity Telepose Event.");
        }

        /**
         * Called after the entity has been transposed.
         */
        public static class Post extends Ent {

            public Post(Entity entity, World initialWorld, BlockPos initialBlockPos, World finalWorld, BlockPos finalBlockPos) {
                super(entity, initialWorld, initialBlockPos, finalWorld, finalBlockPos);
            }
        }
    }
}
