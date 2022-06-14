package wayoftime.bloodmagic.common.item;

import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;

/**
 * Interface for any items that are Blood Orbs
 * TODO: Should either merge this implementation with BloodOrb or clean it up idk
 */
public interface IBloodOrb
{
	@Nullable
	BloodOrb getOrb(ItemStack stack);
}