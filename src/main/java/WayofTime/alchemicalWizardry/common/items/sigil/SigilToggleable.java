package WayofTime.alchemicalWizardry.common.items.sigil;

import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.items.BindableItems;

public class SigilToggleable extends BindableItems
{
	public void setActivated(ItemStack stack, boolean newActivated)
    {
        stack.setItemDamage(newActivated ? 1 : 0);
    }

    public boolean getActivated(ItemStack stack)
    {
        return stack.getItemDamage() == 1;
    }
}
