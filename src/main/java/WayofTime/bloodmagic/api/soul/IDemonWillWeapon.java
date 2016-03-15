package WayofTime.bloodmagic.api.soul;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IDemonWillWeapon
{
    List<ItemStack> getRandomDemonWillDrop(EntityLivingBase killedEntity, EntityLivingBase attackingEntity, ItemStack stack, int looting);
}
