package wayoftime.bloodmagic.api.compat;

import java.util.List;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Interface for weapons that drop will when a LivingEntity is killed
 */
public interface IDemonWillWeapon
{
	List<ItemStack> getRandomDemonWillDrop(LivingEntity killedEntity, LivingEntity attackingEntity, ItemStack stack, int looting);
}
