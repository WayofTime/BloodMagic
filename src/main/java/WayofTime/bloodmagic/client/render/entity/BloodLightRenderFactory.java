package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class BloodLightRenderFactory implements IRenderFactory<EntityBloodLight> {
    @Override
    public Render<? super EntityBloodLight> createRenderFor(RenderManager manager) {
        return new RenderEntityBloodLight(manager);
    }
}
