package WayofTime.bloodmagic.structures;

import WayofTime.bloodmagic.util.BMLog;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class DungeonStructure {
    public ResourceLocation resource;

    public DungeonStructure(ResourceLocation resource) {
        this.resource = resource;
    }

    public boolean placeStructureAtPosition(Random rand, PlacementSettings settings, WorldServer world, BlockPos pos) {
        if (pos == null)
            return false;

        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = world.getStructureTemplateManager();

        Template template = templatemanager.getTemplate(minecraftserver, resource);

        if (template == null) {
            BMLog.DEBUG.warn("Invalid template for location: " + resource);
            return false;
        }

//        settings.func_189946_a(MathHelper.clamp_float(schema.integrity, 0.0F, 1.0F));

        BlockPos offset = Template.transformedBlockPos(settings, new BlockPos(0, 0, 0));
        BlockPos finalPos = pos.add(offset);
        template.addBlocksToWorldChunk(world, finalPos, settings);

        return true;
    }

    public DungeonStructure copy() {
        return new DungeonStructure(resource);
    }
}
