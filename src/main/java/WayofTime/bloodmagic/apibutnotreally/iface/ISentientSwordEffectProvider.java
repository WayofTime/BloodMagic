package WayofTime.bloodmagic.apibutnotreally.iface;

import WayofTime.bloodmagic.apibutnotreally.soul.EnumDemonWillType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ISentientSwordEffectProvider {
    boolean applyOnHitEffect(EnumDemonWillType type, ItemStack swordStack, ItemStack providerStack, EntityLivingBase attacker, EntityLivingBase target);

    boolean providesEffectForWill(EnumDemonWillType type);
}
