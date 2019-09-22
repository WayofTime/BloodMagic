package WayofTime.bloodmagic.soul;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IDemonWillWeapon {
    List<ItemStack> getRandomDemonWillDrop(LivingEntity killedEntity, LivingEntity attackingEntity, ItemStack stack, int looting);
}
