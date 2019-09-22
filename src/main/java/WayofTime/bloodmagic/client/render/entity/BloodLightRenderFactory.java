package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class BloodLightRenderFactory implements IRenderFactory<EntityBloodLight> {
    @Override
    public EntityRenderer<? super EntityBloodLight> createRenderFor(EntityRendererManager manager) {
        return new RenderEntityBloodLight(manager);
    }
}
