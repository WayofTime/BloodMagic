package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntityMeteor;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class MeteorRenderFactory implements IRenderFactory<EntityMeteor> {
    @Override
    public EntityRenderer<? super EntityMeteor> createRenderFor(EntityRendererManager manager) {
        return new RenderEntityMeteor(manager);
    }
}
