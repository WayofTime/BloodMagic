package WayofTime.bloodmagic.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import WayofTime.bloodmagic.entity.projectile.EntitySoulArrow;

public class SoulArrowRenderFactory implements IRenderFactory<EntitySoulArrow>
{
    @Override
    public Render<? super EntitySoulArrow> createRenderFor(RenderManager manager)
    {
        return new RenderEntitySoulArrow(manager);
    }
}
