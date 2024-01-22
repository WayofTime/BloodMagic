package wayoftime.bloodmagic.structures;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import wayoftime.bloodmagic.util.BMLog;

public class DungeonStructure
{
	public ResourceLocation resource;

	public DungeonStructure(ResourceLocation resource)
	{
		this.resource = resource;
	}

	public boolean placeStructureAtPosition(RandomSource rand, StructurePlaceSettings settings, ServerLevel world, BlockPos pos)
	{
		if (pos == null)
			return false;

		MinecraftServer minecraftserver = world.getServer();
		StructureTemplateManager templatemanager = world.getStructureManager();

		Optional<StructureTemplate> template = templatemanager.get(resource);

		if (template.isEmpty())
		{
			System.out.println("Invalid template for location: " + resource);
			BMLog.DEBUG.warn("Invalid template for location: " + resource);
			return false;
		}

//        settings.func_189946_a(MathHelper.clamp_float(schema.integrity, 0.0F, 1.0F));

		BlockPos offset = StructureTemplate.calculateRelativePosition(settings, new BlockPos(0, 0, 0));
		BlockPos finalPos = pos.offset(offset);
//		template.addBlocksToWorldChunk(world, finalPos, settings);

//		placeInWorld

//		template.get().placeInWorldChunk(world, finalPos, settings, rand);
		template.get().placeInWorld(world, finalPos, finalPos, settings, rand, 2);
//		template.placeInWorld(world, finalPos, settings, rand);

		return true;
	}

	public DungeonStructure copy()
	{
		return new DungeonStructure(resource);
	}
}
