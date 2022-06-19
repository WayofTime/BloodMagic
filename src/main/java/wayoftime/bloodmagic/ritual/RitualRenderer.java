package wayoftime.bloodmagic.ritual;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.resources.ResourceLocation;

public abstract class RitualRenderer
{
	public abstract void renderAt(IMasterRitualStone masterRitualStone, double x, double y, double z);

	protected void bindTexture(ResourceLocation resourceLocation)
	{
		RenderSystem.setShaderTexture(0, resourceLocation);
	}
}
