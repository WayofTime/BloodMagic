package WayofTime.bloodmagic.structures;

import WayofTime.bloodmagic.ritual.AreaDescriptor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import java.util.*;
import java.util.Map.Entry;

public class DungeonRoom {
    public int dungeonWeight = 1;
    public Map<String, BlockPos> structureMap = new HashMap<>();

    public Map<EnumFacing, List<BlockPos>> doorMap = new HashMap<>(); //Map of doors. The EnumFacing indicates what way this door faces.
    public List<AreaDescriptor.Rectangle> descriptorList = new ArrayList<>();

    public DungeonRoom(Map<String, BlockPos> structureMap, Map<EnumFacing, List<BlockPos>> doorMap, List<AreaDescriptor.Rectangle> descriptorList) {
        this.structureMap = structureMap;
        this.doorMap = doorMap;
        this.descriptorList = descriptorList;
    }

    public List<AreaDescriptor> getAreaDescriptors(PlacementSettings settings, BlockPos offset) {
        List<AreaDescriptor> newList = new ArrayList<>();

        for (AreaDescriptor desc : descriptorList) {
            newList.add(desc.rotateDescriptor(settings).offset(offset));
        }

        return newList;
    }

    public List<BlockPos> getDoorOffsetsForFacing(PlacementSettings settings, EnumFacing facing, BlockPos offset) {
        List<BlockPos> offsetList = new ArrayList<>();

        EnumFacing originalFacing = DungeonUtil.reverseRotate(settings.getMirror(), settings.getRotation(), facing);
        if (doorMap.containsKey(originalFacing)) {
            List<BlockPos> doorList = doorMap.get(originalFacing);
            for (BlockPos doorPos : doorList) {
                offsetList.add(Template.transformedBlockPos(settings, doorPos).add(offset));
            }
        }

        return offsetList;
    }

    public boolean placeStructureAtPosition(Random rand, PlacementSettings settings, WorldServer world, BlockPos pos) {
        for (Entry<String, BlockPos> entry : structureMap.entrySet()) {
            ResourceLocation location = new ResourceLocation(entry.getKey());
            DungeonStructure structure = new DungeonStructure(location);
            BlockPos offsetPos = Template.transformedBlockPos(settings, entry.getValue());

            structure.placeStructureAtPosition(rand, settings, world, pos.add(offsetPos));
        }

        return true;
    }
}
