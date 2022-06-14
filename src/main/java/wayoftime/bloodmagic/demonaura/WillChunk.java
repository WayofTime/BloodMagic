package wayoftime.bloodmagic.demonaura;

import java.lang.ref.WeakReference;

import net.minecraft.world.level.chunk.ChunkAccess;
import wayoftime.bloodmagic.will.DemonWillHolder;

public class WillChunk
{
	PosXY loc;
	private short base;
	private DemonWillHolder currentWill = new DemonWillHolder();
	private WeakReference<ChunkAccess> chunkRef;

	public WillChunk(PosXY loc)
	{
		this.loc = loc;
	}

	public WillChunk(ChunkAccess chunk, short base, DemonWillHolder currentWill)
	{
		this.loc = new PosXY(chunk.getPos().x, chunk.getPos().z);
		this.chunkRef = new WeakReference(chunk);
		this.base = base;
		this.currentWill = currentWill;
	}

	public boolean isModified()
	{
		return (this.chunkRef != null) && (this.chunkRef.get() != null) && this.chunkRef.get().isUnsaved();
	}

	public PosXY getLoc()
	{
		return loc;
	}

	public void setLoc(PosXY loc)
	{
		this.loc = loc;
	}

	public short getBase()
	{
		return base;
	}

	public void setBase(short base)
	{
		this.base = base;
	}

	public DemonWillHolder getCurrentWill()
	{
		return currentWill;
	}

	public void setCurrentWill(DemonWillHolder currentWill)
	{
		this.currentWill = currentWill;
	}

	public WeakReference<ChunkAccess> getChunkRef()
	{
		return chunkRef;
	}

	public void setChunkRef(WeakReference<ChunkAccess> chunkRef)
	{
		this.chunkRef = chunkRef;
	}
}