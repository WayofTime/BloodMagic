package WayofTime.bloodmagic.api.soul;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ISoulWeapon
{
    public List<ItemStack> getRandomSoulDrop(EntityLivingBase killedEntity, EntityLivingBase attackingEntity, ItemStack stack, int looting);
}
