package wayoftime.bloodmagic.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends MixinEntity
{
    @ModifyReturnValue(
            method = "isCurrentlyGlowing",
            at = @At(value = "RETURN")
    )
    public boolean isCurrentlyGlowing(boolean original){
        Player player = Minecraft.getInstance().player;

        if(player != null && player.hasEffect(BloodMagicPotions.SPECTRAL_SIGHT))
        {
            double distance = (player.getEffect(BloodMagicPotions.SPECTRAL_SIGHT).getAmplifier() * 32 + 24);
            if (distanceToSqr(Minecraft.getInstance().player) <= (distance * distance))
            {
                return true;
            }
        }
        return original;
    }



}
