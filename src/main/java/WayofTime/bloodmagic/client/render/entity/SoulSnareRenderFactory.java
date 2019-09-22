package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntitySoulSnare;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class SoulSnareRenderFactory implements IRenderFactory<EntitySoulSnare> {
    @Override
    public EntityRenderer<? super EntitySoulSnare> createRenderFor(EntityRendererManager manager) {
        return new RenderEntitySoulSnare(manager);
    }
}
