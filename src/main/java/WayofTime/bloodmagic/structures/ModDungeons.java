package WayofTime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;

public class ModDungeons
{
    public static void init()
    {
        ResourceLocation resource = new ResourceLocation(Constants.Mod.MODID, "Corridor1");

        DungeonStructure structure = new DungeonStructure(resource);
        Map<DungeonStructure, BlockPos> structureMap = new HashMap<DungeonStructure, BlockPos>();
        structureMap.put(structure, new BlockPos(0, 0, 0));

        Map<EnumFacing, List<BlockPos>> doorMap = new HashMap<EnumFacing, List<BlockPos>>();
        List<AreaDescriptor> descriptorList = new ArrayList<AreaDescriptor>();
        descriptorList.add(new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 5, 3, 7));

        DungeonUtil.addRoom(doorMap, EnumFacing.NORTH, new BlockPos(3, 0, 0));
        DungeonUtil.addRoom(doorMap, EnumFacing.SOUTH, new BlockPos(3, 0, 6));
        DungeonUtil.addRoom(doorMap, EnumFacing.WEST, new BlockPos(0, 0, 3));

        DungeonRoom room = new DungeonRoom(structureMap, doorMap, descriptorList);

        DungeonRoomRegistry.registerDungeonRoom(room, 1);
    }
}
