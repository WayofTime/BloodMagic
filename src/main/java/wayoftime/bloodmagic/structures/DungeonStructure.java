package wayoftime.bloodmagic.structures;

import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.util.BMLog;

public class DungeonStructure
{
	public ResourceLocation resource;

	public DungeonStructure(ResourceLocation resource)
	{
		this.resource = resource;
	}

	public boolean placeStructureAtPosition(Random rand, PlacementSettings settings, ServerWorld world, BlockPos pos)
	{
		if (pos == null)
			return false;

		MinecraftServer minecraftserver = world.getServer();
		TemplateManager templatemanager = world.getStructureManager();

		Template template = templatemanager.get(resource);

		if (template == null)
		{
			System.out.println("Invalid template for location: " + resource);
			BMLog.DEBUG.warn("Invalid template for location: " + resource);
			return false;
		}

//        settings.func_189946_a(MathHelper.clamp_float(schema.integrity, 0.0F, 1.0F));

		BlockPos offset = Template.calculateRelativePosition(settings, new BlockPos(0, 0, 0));
		BlockPos finalPos = pos.offset(offset);
//		template.addBlocksToWorldChunk(world, finalPos, settings);
		template.placeInWorldChunk(world, finalPos, settings, rand);
//		template.placeInWorld(world, finalPos, settings, rand);

		return true;
	}

	public DungeonStructure copy()
	{
		return new DungeonStructure(resource);
	}
}
