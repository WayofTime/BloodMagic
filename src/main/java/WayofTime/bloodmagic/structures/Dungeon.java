package WayofTime.bloodmagic.structures;

import WayofTime.bloodmagic.ritual.AreaDescriptor;
import WayofTime.bloodmagic.util.BMLog;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.Map.Entry;

public class Dungeon {
    public static boolean placeStructureAtPosition(Random rand, WorldServer world, BlockPos pos) {
        long startTime = System.nanoTime();

        Map<EnumFacing, List<BlockPos>> availableDoorMap = new HashMap<>(); //Map of doors. The EnumFacing indicates what way this door faces.
        List<AreaDescriptor> descriptorList = new ArrayList<>();
        Map<BlockPos, Pair<DungeonRoom, PlacementSettings>> roomMap = new HashMap<>(); // Placement positions in terms of actual positions

        PlacementSettings settings = new PlacementSettings();
        Mirror mir = Mirror.NONE;

        settings.setMirror(mir);

        Rotation rot = Rotation.NONE;

        settings.setRotation(rot);
        settings.setIgnoreEntities(true);
        settings.setChunk(null);
        settings.setReplacedBlock(null);
        settings.setIgnoreStructureBlock(false);

        DungeonRoom room = getRandomRoom(rand);
        roomMap.put(pos, Pair.of(room, settings.copy()));
        descriptorList.addAll(room.getAreaDescriptors(settings, pos));
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (availableDoorMap.containsKey(facing)) {
                List<BlockPos> doorList = availableDoorMap.get(facing);
                doorList.addAll(room.getDoorOffsetsForFacing(settings, facing, pos));
            } else {
                List<BlockPos> doorList = room.getDoorOffsetsForFacing(settings, facing, pos);
                availableDoorMap.put(facing, doorList);
            }
        }

        //Initial AreaDescriptors and door positions are initialized. Time for fun!
        for (int i = 0; i < 100; i++) {
            List<EnumFacing> facingList = new ArrayList<>();
            for (Entry<EnumFacing, List<BlockPos>> entry : availableDoorMap.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                    facingList.add(entry.getKey());
                }
            }

            Collections.shuffle(facingList); //Shuffle the list so that it is random what is chosen

            Pair<EnumFacing, BlockPos> removedDoor1 = null;
            Pair<EnumFacing, BlockPos> removedDoor2 = null;
            BlockPos roomLocation = null;

            for (EnumFacing doorFacing : facingList) {
                EnumFacing oppositeDoorFacing = doorFacing.getOpposite();
                List<BlockPos> availableDoorList = availableDoorMap.get(doorFacing); //May need to copy here
                Collections.shuffle(availableDoorList);

                settings.setRotation(Rotation.values()[rand.nextInt(Rotation.values().length)]); //Same for the Mirror
                DungeonRoom testingRoom = getRandomRoom(rand);

                List<BlockPos> otherDoorList = testingRoom.getDoorOffsetsForFacing(settings, oppositeDoorFacing, BlockPos.ORIGIN);
                if (otherDoorList != null && !otherDoorList.isEmpty()) {
                    //See if one of these doors works. 
                    Collections.shuffle(otherDoorList);
                    BlockPos testDoor = otherDoorList.get(0);
                    testDoor:
                    for (BlockPos availableDoor : availableDoorList) {
                        //TODO: Test if it fits, then add the doors to the list.
                        roomLocation = availableDoor.subtract(testDoor).add(doorFacing.getDirectionVec());

                        List<AreaDescriptor> descriptors = testingRoom.getAreaDescriptors(settings, roomLocation);
                        for (AreaDescriptor testDesc : descriptors) {
                            for (AreaDescriptor currentDesc : descriptorList) {
                                if (testDesc.intersects(currentDesc)) {
                                    break testDoor;
                                }
                            }
                        }

                        roomMap.put(roomLocation, Pair.of(testingRoom, settings.copy()));
                        descriptorList.addAll(descriptors);
                        removedDoor1 = Pair.of(doorFacing, availableDoor);
                        removedDoor2 = Pair.of(oppositeDoorFacing, testDoor.add(roomLocation));

                        room = testingRoom;

                    }

                    break;
                }

//                Collections.shuffle(otherDoorList);
            }

            if (removedDoor1 != null) {
                for (EnumFacing facing : EnumFacing.VALUES) {
                    if (availableDoorMap.containsKey(facing)) {
                        List<BlockPos> doorList = availableDoorMap.get(facing);
                        doorList.addAll(room.getDoorOffsetsForFacing(settings, facing, roomLocation));
                    } else {
                        List<BlockPos> doorList = room.getDoorOffsetsForFacing(settings, facing, roomLocation);
                        availableDoorMap.put(facing, doorList);
                    }
                }

                EnumFacing face = removedDoor1.getKey();
                if (availableDoorMap.containsKey(face)) {
                    availableDoorMap.get(face).remove(removedDoor1.getRight());
                }
            }

            if (removedDoor2 != null) {
                EnumFacing face = removedDoor2.getKey();
                if (availableDoorMap.containsKey(face)) {
                    availableDoorMap.get(face).remove(removedDoor2.getRight());
                }
            }
        }

        long endTime = System.nanoTime();

        long duration = (endTime - startTime); //divide by 1000000 to get milliseconds.
        BMLog.DEBUG.info("Duration: " + duration + "(ns), " + duration / 1000000 + "(ms)");

        //Building what I've got
        for (Entry<BlockPos, Pair<DungeonRoom, PlacementSettings>> entry : roomMap.entrySet()) {
            BlockPos placementPos = entry.getKey();
            DungeonRoom placedRoom = entry.getValue().getKey();
            PlacementSettings placementSettings = entry.getValue().getValue();

            placedRoom.placeStructureAtPosition(rand, placementSettings, world, placementPos);
        }

        return false;
    }

    public static DungeonRoom getRandomRoom(Random rand) {
        return DungeonRoomRegistry.getRandomDungeonRoom(rand);
    }
}
