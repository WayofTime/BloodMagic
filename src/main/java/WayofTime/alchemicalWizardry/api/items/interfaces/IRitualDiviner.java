package WayofTime.alchemicalWizardry.api.items.interfaces;

import net.minecraft.item.ItemStack;

public interface IRitualDiviner {
	int cycleDirection(ItemStack stack);
	String getCurrentRitual(ItemStack stack);
	int getDirection(ItemStack stack);
	int getMaxRuneDisplacement(ItemStack stack);
	String getNameForDirection(int direction);
	void setCurrentRitual(ItemStack stack, String ritualID);
	void setDirection(ItemStack stack, int direction);
	void setMaxRuneDisplacement(ItemStack stack, int displacement);
}
