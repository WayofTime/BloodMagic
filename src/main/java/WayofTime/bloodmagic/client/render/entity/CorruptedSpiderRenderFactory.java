package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntityCorruptedSpider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class CorruptedSpiderRenderFactory implements IRenderFactory<EntityCorruptedSpider> {
    @Override
    public EntityRenderer<? super EntityCorruptedSpider> createRenderFor(EntityRendererManager manager) {
        return new RenderCorruptedSpider(manager);
    }
}
