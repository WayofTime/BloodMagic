package WayofTime.bloodmagic.teleport;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public abstract class Teleport implements ITeleport {
    protected int x;
    protected int y;
    protected int z;
    protected Entity entity;
    protected UUID networkOwner;

    public Teleport(int x, int y, int z, Entity entity, UUID networkOwner) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.entity = entity;
        this.networkOwner = networkOwner;
    }

    public Teleport(BlockPos blockPos, Entity entity, UUID networkOwner) {
        this(blockPos.getX(), blockPos.getY(), blockPos.getZ(), entity, networkOwner);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Entity getEntity() {
        return entity;
    }

    public UUID getNetworkOwner() {
        return networkOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teleport)) return false;

        Teleport teleport = (Teleport) o;

        if (x != teleport.x) return false;
        if (y != teleport.y) return false;
        if (z != teleport.z) return false;
        if (entity != null ? !entity.equals(teleport.entity) : teleport.entity != null) return false;
        return networkOwner != null ? networkOwner.equals(teleport.networkOwner) : teleport.networkOwner == null;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + (entity != null ? entity.hashCode() : 0);
        result = 31 * result + (networkOwner != null ? networkOwner.hashCode() : 0);
        return result;
    }
}
