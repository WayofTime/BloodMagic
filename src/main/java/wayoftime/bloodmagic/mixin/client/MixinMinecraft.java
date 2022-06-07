package wayoftime.bloodmagic.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
	@Inject(method = "isEntityGlowing(Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
	public void isEntityGlowing(Entity entity, CallbackInfoReturnable<Boolean> ci)
	{
		PlayerEntity player = Minecraft.getInstance().player;
		if (player.isPotionActive(BloodMagicPotions.SPECTRAL_SIGHT))
		{
			double distance = (player.getActivePotionEffect(BloodMagicPotions.SPECTRAL_SIGHT).getAmplifier() * 32 + 24);
			if (Minecraft.getInstance().player.getDistanceSq(entity) <= (distance * distance))
				ci.setReturnValue(true);
		}

	}
}
