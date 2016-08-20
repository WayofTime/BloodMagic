package WayofTime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;

public class DungeonTester
{
    public static void testDungeonElementWithOutput(WorldServer world, BlockPos pos)
    {
        ResourceLocation resource = new ResourceLocation(Constants.Mod.MODID, "Corridor1");
        Map<EnumFacing, List<BlockPos>> doorMap = new HashMap<EnumFacing, List<BlockPos>>();
        List<AreaDescriptor> descriptorList = new ArrayList<AreaDescriptor>();
        descriptorList.add(new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 5, 3, 7));

        DungeonUtil.addRoom(doorMap, EnumFacing.NORTH, new BlockPos(3, 0, 0));
        DungeonUtil.addRoom(doorMap, EnumFacing.SOUTH, new BlockPos(3, 0, 6));
        DungeonUtil.addRoom(doorMap, EnumFacing.WEST, new BlockPos(0, 0, 3));

        DungeonStructure structure = new DungeonStructure(resource, doorMap, descriptorList);

        int i = 0;

        for (Mirror mirror : Mirror.values())
        {
//            System.out.print("Mirror: " + mirror + '\n');
            int j = 0;
            for (Rotation rot : Rotation.values())
            {
//                System.out.print("Rotation: " + rot + '\n');
                PlacementSettings settings = new PlacementSettings();
                settings.setRotation(rot);
                settings.setMirror(mirror);

                BlockPos offsetPos = pos.add(i * 16, 0, j * 16);
                structure.placeStructureAtPosition(new Random(), mirror, rot, world, offsetPos);

//                List<AreaDescriptor> descriptors = structure.getAreaDescriptors(settings, offsetPos);
//                for (AreaDescriptor desc : descriptors)
//                {
//                    List<BlockPos> posList = desc.getContainedPositions(new BlockPos(0, 0, 0));
//                    for (BlockPos placePos : posList)
//                    {
//                        world.setBlockState(placePos, Blocks.REDSTONE_BLOCK.getDefaultState());
//                    }
//                }

//                for (EnumFacing facing : EnumFacing.HORIZONTALS)
//                {
//                    List<BlockPos> doorList = structure.getDoorOffsetsForFacing(settings, facing);
//                    for (BlockPos doorPos : doorList)
//                    {
//                        System.out.print("Door at " + doorPos + " facing " + facing + '\n');
//                    }
//                }
                j++;
            }
            i++;
        }
    }
}
