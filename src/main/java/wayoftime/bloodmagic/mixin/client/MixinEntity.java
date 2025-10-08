package wayoftime.bloodmagic.mixin.client;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(Entity.class)
public class MixinEntity
{

    @Shadow
    public double distanceToSqr(Entity p_20281_)
    {
       throw new IllegalStateException("Failed to shadow distanceToSqr()");
    }
}
