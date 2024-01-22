package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;

import java.util.List;

public class ItemBase extends Item
{
	private final String desc;

	public ItemBase()
	{
		this("");
	}

	public ItemBase(String desc)
	{
		this(64, desc);
	}

	public ItemBase(int stackSize)
	{
		this(stackSize, "");
	}

	public ItemBase(int stackSize, String desc)
	{
		super(new Item.Properties().stacksTo(stackSize));
		this.desc = desc;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		if (!desc.isEmpty())
			tooltip.add(Component.translatable("tooltip.bloodmagic." + desc).withStyle(ChatFormatting.GRAY));

	}
}