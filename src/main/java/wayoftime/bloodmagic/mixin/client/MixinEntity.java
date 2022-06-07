package wayoftime.bloodmagic.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class MixinEntity
{
	@Shadow
	protected boolean glowing;

	@Shadow
	protected void setFlag(int flag, boolean set)
	{
		throw new IllegalStateException("Mixin failed to shadow setFlag()");
	}

	@Shadow
	protected boolean getFlag(int flag)
	{
		throw new IllegalStateException("Mixin failed to shadow getFlag()");
	}

	@Shadow
	public double getDistanceSq(Entity entityIn)
	{
		throw new IllegalStateException("Mixin failed to shadow getDistanceSq()");
	}
}
