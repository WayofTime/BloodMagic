package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;

public class ItemBase extends Item
{
	private final String desc;

	public ItemBase()
	{
		this("");
	}

	public ItemBase(String desc)
	{
		super(new Item.Properties().maxStackSize(64).group(BloodMagic.TAB));
		this.desc = desc;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (!desc.isEmpty())
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic." + desc));

	}
}