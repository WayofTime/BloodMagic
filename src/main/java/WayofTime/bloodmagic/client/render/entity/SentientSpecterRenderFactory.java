package WayofTime.bloodmagic.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;

public class SentientSpecterRenderFactory implements IRenderFactory<EntitySentientSpecter>
{
    @Override
    public Render<? super EntitySentientSpecter> createRenderFor(RenderManager manager)
    {
        return new RenderSentientSpecter(manager);
    }
}
