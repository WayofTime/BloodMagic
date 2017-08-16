package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.mob.EntityCorruptedSheep;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class CorruptedSheepRenderFactory implements IRenderFactory<EntityCorruptedSheep> {
    @Override
    public Render<? super EntityCorruptedSheep> createRenderFor(RenderManager manager) {
        return new RenderCorruptedSheep(manager);
    }
}
