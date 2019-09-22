package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntityCorruptedChicken;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class CorruptedChickenRenderFactory implements IRenderFactory<EntityCorruptedChicken> {
    @Override
    public EntityRenderer<? super EntityCorruptedChicken> createRenderFor(EntityRendererManager manager) {
        return new RenderCorruptedChicken(manager);
    }
}
