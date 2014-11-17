package WayofTime.alchemicalWizardry.api.renderer;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;

public abstract class MRSRenderer
{
    public abstract void renderAt(IMasterRitualStone tile, double x, double y, double z);

    protected void bindTexture(ResourceLocation p_147499_1_)
    {
        TextureManager texturemanager = TileEntityRendererDispatcher.instance.field_147553_e;

        if (texturemanager != null)
        {
            texturemanager.bindTexture(p_147499_1_);
        }
    }
}
