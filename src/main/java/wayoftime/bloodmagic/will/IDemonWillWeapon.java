package wayoftime.bloodmagic.will;

import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IDemonWillWeapon
{
	List<ItemStack> getRandomDemonWillDrop(LivingEntity killedEntity, LivingEntity attackingEntity, ItemStack stack, int looting);
}
