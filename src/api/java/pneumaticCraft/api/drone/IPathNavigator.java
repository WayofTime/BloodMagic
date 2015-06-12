package pneumaticCraft.api.drone;

import net.minecraft.entity.Entity;

public interface IPathNavigator{
    public boolean moveToXYZ(double x, double y, double z);

    public boolean moveToEntity(Entity entity);

    public boolean hasNoPath();
}
