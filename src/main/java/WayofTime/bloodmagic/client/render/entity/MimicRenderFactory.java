package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntityMimic;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class MimicRenderFactory implements IRenderFactory<EntityMimic> {
    @Override
    public EntityRenderer<? super EntityMimic> createRenderFor(EntityRendererManager manager) {
        return new RenderEntityMimic(manager);
    }
}
