package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntityCorruptedSheep;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class CorruptedSheepRenderFactory implements IRenderFactory<EntityCorruptedSheep> {
    @Override
    public EntityRenderer<? super EntityCorruptedSheep> createRenderFor(EntityRendererManager manager) {
        return new RenderCorruptedSheep(manager);
    }
}
