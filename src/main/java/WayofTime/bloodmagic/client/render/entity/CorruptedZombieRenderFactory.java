package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntityCorruptedZombie;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class CorruptedZombieRenderFactory implements IRenderFactory<EntityCorruptedZombie> {
    @Override
    public EntityRenderer<? super EntityCorruptedZombie> createRenderFor(EntityRendererManager manager) {
        return new RenderCorruptedZombie(manager);
    }
}
