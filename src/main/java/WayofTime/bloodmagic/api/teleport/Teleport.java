package WayofTime.bloodmagic.api.teleport;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

@ToString
@EqualsAndHashCode
public abstract class Teleport implements ITeleport
{
    @Getter
    protected int x;
    @Getter
    protected int y;
    @Getter
    protected int z;
    @Getter
    protected Entity entity;
    @Getter
    protected String networkToDrain;

    public Teleport(int x, int y, int z, Entity entity, String networkToDrain)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.entity = entity;
        this.networkToDrain = networkToDrain;
    }

    public Teleport(BlockPos blockPos, Entity entity, String networkToDrain)
    {
        this(blockPos.getX(), blockPos.getY(), blockPos.getZ(), entity, networkToDrain);
    }
}
