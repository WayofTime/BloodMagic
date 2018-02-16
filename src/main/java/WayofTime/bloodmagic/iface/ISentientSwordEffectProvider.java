package WayofTime.bloodmagic.iface;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ISentientSwordEffectProvider {
    boolean applyOnHitEffect(EnumDemonWillType type, ItemStack swordStack, ItemStack providerStack, EntityLivingBase attacker, EntityLivingBase target);

    boolean providesEffectForWill(EnumDemonWillType type);
}
