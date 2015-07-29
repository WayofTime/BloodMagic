package WayofTime.alchemicalWizardry.api.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;

public abstract class MRSRenderer
{
    public abstract void renderAt(IMasterRitualStone tile, double x, double y, double z);

    protected void bindTexture(ResourceLocation location)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
    }
}
