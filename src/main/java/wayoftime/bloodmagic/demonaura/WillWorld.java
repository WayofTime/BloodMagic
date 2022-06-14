package wayoftime.bloodmagic.demonaura;

import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.resources.ResourceLocation;

public class WillWorld
{
	// TODO: It was noted I may need to use RegistryKey<World> instead.
	ResourceLocation dim;
	ConcurrentHashMap<PosXY, WillChunk> willChunks = new ConcurrentHashMap<>();

//    private static ConcurrentHashMap<PosXY, AspectList> nodeTickets = new ConcurrentHashMap();

	public WillWorld(ResourceLocation resourceLocation)
	{
		this.dim = resourceLocation;
	}

	public WillChunk getWillChunkAt(int x, int y)
	{
		return getWillChunkAt(new PosXY(x, y));
	}

	public WillChunk getWillChunkAt(PosXY loc)
	{
		return this.willChunks.get(loc);
	}

	public ConcurrentHashMap<PosXY, WillChunk> getWillChunks()
	{
		return willChunks;
	}

	public void setWillChunks(ConcurrentHashMap<PosXY, WillChunk> willChunks)
	{
		this.willChunks = willChunks;
	}

//    public static ConcurrentHashMap<PosXY, AspectList> getNodeTickets()
//    {
//        return nodeTickets;
//    }
//
//    public static void setNodeTickets(ConcurrentHashMap<PosXY, AspectList> nodeTickets)
//    {
//        nodeTickets = nodeTickets;
//    }
}