package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntityCorruptedZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class CorruptedZombieRenderFactory implements IRenderFactory<EntityCorruptedZombie> {
    @Override
    public Render<? super EntityCorruptedZombie> createRenderFor(RenderManager manager) {
        return new RenderCorruptedZombie(manager);
    }
}
