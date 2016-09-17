package WayofTime.bloodmagic.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedSheep;

public class CorruptedSheepRenderFactory implements IRenderFactory<EntityCorruptedSheep>
{
    @Override
    public Render<? super EntityCorruptedSheep> createRenderFor(RenderManager manager)
    {
        return new RenderCorruptedSheep(manager);
    }
}
