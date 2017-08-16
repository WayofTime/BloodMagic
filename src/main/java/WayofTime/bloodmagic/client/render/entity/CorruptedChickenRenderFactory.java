package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntityCorruptedChicken;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class CorruptedChickenRenderFactory implements IRenderFactory<EntityCorruptedChicken> {
    @Override
    public Render<? super EntityCorruptedChicken> createRenderFor(RenderManager manager) {
        return new RenderCorruptedChicken(manager);
    }
}
