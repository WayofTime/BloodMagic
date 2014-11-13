package WayofTime.alchemicalWizardry.api.renderer;

import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public abstract class MRSRenderer
{
    public abstract void renderAt(TEMasterStone tile, double x, double y, double z);

    protected void bindTexture(ResourceLocation p_147499_1_)
    {
        TextureManager texturemanager = TileEntityRendererDispatcher.instance.field_147553_e;

        if (texturemanager != null)
        {
            texturemanager.bindTexture(p_147499_1_);
        }
    }
}
