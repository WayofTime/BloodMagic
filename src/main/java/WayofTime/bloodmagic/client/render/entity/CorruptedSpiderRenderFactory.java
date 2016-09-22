package WayofTime.bloodmagic.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedSpider;

public class CorruptedSpiderRenderFactory implements IRenderFactory<EntityCorruptedSpider>
{
    @Override
    public Render<? super EntityCorruptedSpider> createRenderFor(RenderManager manager)
    {
        return new RenderCorruptedSpider(manager);
    }
}
