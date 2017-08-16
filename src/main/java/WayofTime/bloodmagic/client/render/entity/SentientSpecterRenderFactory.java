package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class SentientSpecterRenderFactory implements IRenderFactory<EntitySentientSpecter> {
    @Override
    public Render<? super EntitySentientSpecter> createRenderFor(RenderManager manager) {
        return new RenderSentientSpecter(manager);
    }
}
