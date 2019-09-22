package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class SentientSpecterRenderFactory implements IRenderFactory<EntitySentientSpecter> {
    @Override
    public EntityRenderer<? super EntitySentientSpecter> createRenderFor(EntityRendererManager manager) {
        return new RenderSentientSpecter(manager);
    }
}
