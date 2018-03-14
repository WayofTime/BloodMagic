package WayofTime.bloodmagic.ritual;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class RitualRenderer {
    public abstract void renderAt(IMasterRitualStone masterRitualStone, double x, double y, double z);

    protected void bindTexture(ResourceLocation resourceLocation) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    }
}
