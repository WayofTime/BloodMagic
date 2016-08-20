package WayofTime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;

public class DungeonStructure
{
    final ResourceLocation resource;
    Map<EnumFacing, List<BlockPos>> doorMap = new HashMap<EnumFacing, List<BlockPos>>(); //Map of doors. The EnumFacing indicates what way this door faces.
    List<AreaDescriptor> descriptorList = new ArrayList<AreaDescriptor>();

    public DungeonStructure(ResourceLocation resource, Map<EnumFacing, List<BlockPos>> doorMap, List<AreaDescriptor> descriptorList)
    {
        this.resource = resource;
        this.doorMap = doorMap;
        this.descriptorList = descriptorList;
    }

    public List<AreaDescriptor> getAreaDescriptors(PlacementSettings settings, BlockPos offset)
    {
        List<AreaDescriptor> newList = new ArrayList<AreaDescriptor>();

        for (AreaDescriptor desc : descriptorList)
        {
            newList.add(desc.rotateDescriptor(settings).offset(offset));
        }

        return newList;
    }

    public List<BlockPos> getDoorOffsetsForFacing(PlacementSettings settings, EnumFacing facing)
    {
        List<BlockPos> offsetList = new ArrayList<BlockPos>();

        EnumFacing originalFacing = DungeonUtil.reverseRotate(settings.getMirror(), settings.getRotation(), facing);
        if (doorMap.containsKey(originalFacing))
        {
            List<BlockPos> doorList = doorMap.get(originalFacing);
            for (BlockPos doorPos : doorList)
            {
                offsetList.add(Template.transformedBlockPos(settings, doorPos));
            }
        }

        return offsetList;
    }

    public boolean placeStructureAtPosition(Random rand, Mirror mirror, Rotation rotation, WorldServer world, BlockPos pos)
    {
        if (pos == null)
            return false;

        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = world.getStructureTemplateManager();

        ResourceLocation resource = new ResourceLocation(Constants.Mod.MODID, "Corridor1");
        Template template = templatemanager.func_189942_b(minecraftserver, resource);

        if (template == null)
        {
            System.out.println("Invalid template for location: " + resource);
            return false;
        }

        PlacementSettings settings = new PlacementSettings();
        Mirror mir = mirror;
        if (mir == null)
        {
            mir = Mirror.NONE;
        }

        settings.setMirror(mir);

        Rotation rot;

        rot = rotation;
        if (rot == null)
            rot = Rotation.NONE;

        settings.setRotation(rot);
        settings.setIgnoreEntities(true);
        settings.setChunk((ChunkPos) null);
        settings.setReplacedBlock((Block) null);
        settings.setIgnoreStructureBlock(false);

//        settings.func_189946_a(MathHelper.clamp_float(schema.integrity, 0.0F, 1.0F));

        BlockPos offset = Template.transformedBlockPos(settings, new BlockPos(0, 0, 0));
        BlockPos finalPos = pos.add(offset);
        template.addBlocksToWorldChunk(world, finalPos, settings);

        return true;
    }
}
