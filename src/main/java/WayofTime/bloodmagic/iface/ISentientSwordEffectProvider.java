package WayofTime.bloodmagic.iface;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ISentientSwordEffectProvider {
    /**
     * Attempts to apply the effect of off-handing sigils
     * have fire breathing.
     * @param type
     * @param swordStack the sword swung
     * @param providerStack the stack providing the effect
     * @param attacker
     * @param target
     * @return true if the effect was successful; false otherwise. Failures include events 
     * such as attempting to drown a player, but they have water breathing, or burn when they
     * have fire protection
     */
    boolean applyOnHitEffect(EnumDemonWillType type, int willLevel, ItemStack swordStack, ItemStack providerStack, EntityLivingBase attacker, EntityLivingBase target);
}
