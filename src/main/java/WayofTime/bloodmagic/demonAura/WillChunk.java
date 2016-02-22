package WayofTime.bloodmagic.demonAura;

import java.lang.ref.WeakReference;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.chunk.Chunk;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;

@Getter
@Setter
public class WillChunk
{
    PosXY loc;
    private short base;
    private DemonWillHolder currentWill = new DemonWillHolder();
    private WeakReference<Chunk> chunkRef;

    public WillChunk(PosXY loc)
    {
        this.loc = loc;
    }

    public WillChunk(Chunk chunk, short base, DemonWillHolder currentWill)
    {
        this.loc = new PosXY(chunk.xPosition, chunk.zPosition);
        this.chunkRef = new WeakReference(chunk);
        this.base = base;
        this.currentWill = currentWill;
    }

    public boolean isModified()
    {
        if ((this.chunkRef != null) && (this.chunkRef.get() != null))
        {
            return ((Chunk) this.chunkRef.get()).needsSaving(false);
        }

        return false;
    }
}
