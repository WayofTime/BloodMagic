package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class SentientArrowRenderFactory implements IRenderFactory<EntitySentientArrow> {
    @Override
    public EntityRenderer<? super EntitySentientArrow> createRenderFor(EntityRendererManager manager) {
        return new RenderEntitySentientArrow(manager);
    }
}
