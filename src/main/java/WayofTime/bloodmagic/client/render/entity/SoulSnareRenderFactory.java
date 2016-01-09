package WayofTime.bloodmagic.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import WayofTime.bloodmagic.entity.projectile.EntitySoulSnare;

public class SoulSnareRenderFactory implements IRenderFactory<EntitySoulSnare>
{
    @Override
    public Render<? super EntitySoulSnare> createRenderFor(RenderManager manager)
    {
        return new RenderEntitySoulSnare(manager);
    }
}
