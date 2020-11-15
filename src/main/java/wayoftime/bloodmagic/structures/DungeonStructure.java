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
		TemplateManager templatemanager = world.getStructureTemplateManager();

		Template template = templatemanager.getTemplate(resource);

		if (template == null)
		{
			System.out.println("Invalid template for location: " + resource);
			BMLog.DEBUG.warn("Invalid template for location: " + resource);
			return false;
		}

//        settings.func_189946_a(MathHelper.clamp_float(schema.integrity, 0.0F, 1.0F));

		BlockPos offset = Template.transformedBlockPos(settings, new BlockPos(0, 0, 0));
		BlockPos finalPos = pos.add(offset);
//		template.addBlocksToWorldChunk(world, finalPos, settings);
		template.func_237144_a_(world, finalPos, settings, rand);
//		template.func_237152_b_(world, finalPos, settings, rand);

		return true;
	}

	public DungeonStructure copy()
	{
		return new DungeonStructure(resource);
	}
}
