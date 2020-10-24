package wayoftime.bloodmagic.orb;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public interface IBloodOrb
{
	@Nullable
	BloodOrb getOrb(ItemStack stack);
}