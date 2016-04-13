package WayofTime.bloodmagic.api.iface;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;

public interface ISentientSwordEffectProvider
{
    boolean applyOnHitEffect(EnumDemonWillType type, ItemStack swordStack, ItemStack providerStack, EntityLivingBase attacker, EntityLivingBase target);

    boolean providesEffectForWill(EnumDemonWillType type);
}
