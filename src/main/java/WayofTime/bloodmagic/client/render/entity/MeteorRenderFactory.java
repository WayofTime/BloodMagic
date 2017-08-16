package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntityMeteor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class MeteorRenderFactory implements IRenderFactory<EntityMeteor> {
    @Override
    public Render<? super EntityMeteor> createRenderFor(RenderManager manager) {
        return new RenderEntityMeteor(manager);
    }
}
