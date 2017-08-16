package WayofTime.bloodmagic.tile.base;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

/**
 * Base class for tiles that tick. Allows disabling the ticking programmatically.
 */
// TODO - Move implementations that depend on existed ticks to new methods from here.
public abstract class TileTicking extends TileBase implements ITickable {
    private int ticksExisted;
    private boolean shouldTick = true;

    @Override
    public final void update() {
        if (shouldTick()) {
            ticksExisted++;
            onUpdate();
        }
    }

    @Override
    void deserializeBase(NBTTagCompound tagCompound) {
        this.ticksExisted = tagCompound.getInteger("ticksExisted");
        this.shouldTick = tagCompound.getBoolean("shouldTick");
    }

    @Override
    NBTTagCompound serializeBase(NBTTagCompound tagCompound) {
        tagCompound.setInteger("ticksExisted", getTicksExisted());
        tagCompound.setBoolean("shouldTick", shouldTick());
        return tagCompound;
    }

    /**
     * Called every tick that {@link #shouldTick()} is true.
     */
    public abstract void onUpdate();

    public int getTicksExisted() {
        return ticksExisted;
    }

    public void resetLifetime() {
        ticksExisted = 0;
    }

    public boolean shouldTick() {
        return shouldTick;
    }

    public void setShouldTick(boolean shouldTick) {
        this.shouldTick = shouldTick;
    }
}
