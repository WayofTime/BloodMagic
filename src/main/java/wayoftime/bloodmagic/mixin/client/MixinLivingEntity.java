package wayoftime.bloodmagic.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

@Mixin(LivingEntity.class)
public class MixinLivingEntity extends MixinEntity
{
	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci)
	{
		PlayerEntity player = Minecraft.getInstance().player;
		if (player == null)
		{
			return;
		}

		if (player.hasEffect(BloodMagicPotions.SPECTRAL_SIGHT))
		{
			double distance = (player.getEffect(BloodMagicPotions.SPECTRAL_SIGHT).getAmplifier() * 32 + 24);
			if (getDistanceSq(Minecraft.getInstance().player) <= (distance * distance))
			{
				if (!this.glowing)
				{
					if (!this.getFlag(6))
					{
						this.setFlag(6, true);
					}
				}
			}
		}
	}
}
