package WayofTime.bloodmagic.demonAura;

import java.util.concurrent.ConcurrentHashMap;

public class WillWorld {
    int dim;
    ConcurrentHashMap<PosXY, WillChunk> willChunks = new ConcurrentHashMap<>();

//    private static ConcurrentHashMap<PosXY, AspectList> nodeTickets = new ConcurrentHashMap();

    public WillWorld(int dim) {
        this.dim = dim;
    }

    public WillChunk getWillChunkAt(int x, int y) {
        return getWillChunkAt(new PosXY(x, y));
    }

    public WillChunk getWillChunkAt(PosXY loc) {
        return this.willChunks.get(loc);
    }

    public ConcurrentHashMap<PosXY, WillChunk> getWillChunks() {
        return willChunks;
    }

    public void setWillChunks(ConcurrentHashMap<PosXY, WillChunk> willChunks) {
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
