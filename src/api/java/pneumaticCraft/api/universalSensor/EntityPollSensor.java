package pneumaticCraft.api.universalSensor;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public abstract class EntityPollSensor implements IPollSensorSetting{

    @Override
    public String getSensorPath(){
        return "entityTracker";
    }

    @Override
    public int getPollFrequency(){
        return 1;
    }

    @Override
    public int getRedstoneValue(World world, int x, int y, int z, int sensorRange, String textBoxText){
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(x - sensorRange, y - sensorRange, z - sensorRange, x + 1 + sensorRange, y + 1 + sensorRange, z + 1 + sensorRange);
        return getRedstoneValue(world.getEntitiesWithinAABB(getEntityTracked(), aabb), textBoxText);
    }

    public abstract Class getEntityTracked();

    public abstract int getRedstoneValue(List<Entity> entities, String textBoxText);

}
