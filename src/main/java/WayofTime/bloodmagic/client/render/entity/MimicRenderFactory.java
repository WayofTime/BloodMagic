package WayofTime.bloodmagic.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import WayofTime.bloodmagic.entity.mob.EntityMimic;

public class MimicRenderFactory implements IRenderFactory<EntityMimic>
{
    @Override
    public Render<? super EntityMimic> createRenderFor(RenderManager manager)
    {
        return new RenderEntityMimic(manager);
    }
}
