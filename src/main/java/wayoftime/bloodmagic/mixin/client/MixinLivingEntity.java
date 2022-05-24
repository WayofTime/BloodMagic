package wayoftime.bloodmagic.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class MixinLivingEntity
{
	@Inject(method = "tick()", at = @At("TAIL"))
	public void tick(CallbackInfo ci)
	{
		this.setFlag(6, true);
//		PlayerEntity player = Minecraft.getInstance().player;
//		if (player.isPotionActive(BloodMagicPotions.SPECTRAL_SIGHT))
//		{
//			double distance = (player.getActivePotionEffect(BloodMagicPotions.SPECTRAL_SIGHT).getAmplifier() * 32 + 24);
//			if (Minecraft.getInstance().player.getDistanceSq(entity) <= (distance * distance))
//				ci.setReturnValue(true);
//		}

	}

	@Shadow
	protected void setFlag(int flag, boolean set)
	{
		throw new IllegalStateException("Mixin failed to shadow setFlag()");
	}
}
