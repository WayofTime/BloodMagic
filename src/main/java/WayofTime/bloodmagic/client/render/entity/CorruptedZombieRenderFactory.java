package WayofTime.bloodmagic.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import WayofTime.bloodmagic.entity.mob.EntityCorruptedZombie;

public class CorruptedZombieRenderFactory implements IRenderFactory<EntityCorruptedZombie>
{
    @Override
    public Render<? super EntityCorruptedZombie> createRenderFor(RenderManager manager)
    {
        return new RenderCorruptedZombie(manager);
    }
}
