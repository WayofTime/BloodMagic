package WayofTime.bloodmagic.client.render.entity;

import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class SentientArrowRenderFactory implements IRenderFactory<EntitySentientArrow> {
    @Override
    public Render<? super EntitySentientArrow> createRenderFor(RenderManager manager) {
        return new RenderEntitySentientArrow(manager);
    }
}
