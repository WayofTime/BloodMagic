package wayoftime.bloodmagic.common.item.arc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.IARCTool;

public class ItemARCToolBase extends Item implements IARCTool
{
	private final double craftingMultiplier;

	public ItemARCToolBase(int maxDamage, double craftingMultiplier)
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB).maxDamage(maxDamage));
		this.craftingMultiplier = craftingMultiplier;
	}

	@Override
	public double getCraftingSpeedMultiplier(ItemStack stack)
	{
		return craftingMultiplier;
	}
}
