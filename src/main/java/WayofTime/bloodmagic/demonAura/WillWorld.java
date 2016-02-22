package WayofTime.bloodmagic.demonAura;

import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

public class WillWorld
{
    int dim;
    @Getter
    @Setter
    ConcurrentHashMap<PosXY, WillChunk> willChunks = new ConcurrentHashMap<PosXY, WillChunk>();

//    private static ConcurrentHashMap<PosXY, AspectList> nodeTickets = new ConcurrentHashMap();

    public WillWorld(int dim)
    {
        this.dim = dim;
    }

    public WillChunk getWillChunkAt(int x, int y)
    {
        return getWillChunkAt(new PosXY(x, y));
    }

    public WillChunk getWillChunkAt(PosXY loc)
    {
        return this.willChunks.get(loc);
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
