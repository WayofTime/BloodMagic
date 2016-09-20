package WayofTime.bloodmagic.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedChicken;

public class CorruptedChickenRenderFactory implements IRenderFactory<EntityCorruptedChicken>
{
    @Override
    public Render<? super EntityCorruptedChicken> createRenderFor(RenderManager manager)
    {
        return new RenderCorruptedChicken(manager);
    }
}
