package WayofTime.bloodmagic.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import WayofTime.bloodmagic.entity.projectile.EntitySoulSnare;
import WayofTime.bloodmagic.registry.ModItems;

public class SoulSnareRenderFactory implements IRenderFactory<EntitySoulSnare>
{
    @Override
    public Render<? super EntitySoulSnare> createRenderFor(RenderManager manager)
    {
        return new RenderEntitySoulSnare(manager, ModItems.soulSnare, Minecraft.getMinecraft().getRenderItem());
    }
}
