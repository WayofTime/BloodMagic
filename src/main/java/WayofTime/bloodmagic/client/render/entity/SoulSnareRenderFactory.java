package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntitySoulSnare;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class SoulSnareRenderFactory implements IRenderFactory<EntitySoulSnare> {
    @Override
    public Render<? super EntitySoulSnare> createRenderFor(RenderManager manager) {
        return new RenderEntitySoulSnare(manager);
    }
}
