package WayofTime.bloodmagic.iface;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface ISentientSwordEffectProvider {
    boolean applyOnHitEffect(EnumDemonWillType type, ItemStack swordStack, ItemStack providerStack, LivingEntity attacker, LivingEntity target);

    boolean providesEffectForWill(EnumDemonWillType type);
}
