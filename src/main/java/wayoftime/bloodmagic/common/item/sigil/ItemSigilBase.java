package wayoftime.bloodmagic.common.item.sigil;

import java.util.List;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.ItemSigil;

public class ItemSigilBase extends ItemSigil
{
	protected final String tooltipBase;
//	private final String name;

	public ItemSigilBase(String name, int lpUsed)
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB), lpUsed);
//		super(lpUsed);

//		this.name = name;
		this.tooltipBase = "tooltip.bloodmagic.sigil." + name + ".";
	}

	public ItemSigilBase(String name)
	{
		this(name, 0);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent(tooltipBase + "desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
//		if (TextHelper.canTranslate(tooltipBase + "desc"))
//			tooltip.addAll(Arrays.asList(WordUtils.wrap(TextHelper.localizeEffect(tooltipBase
//					+ "desc"), 30, "/cut", false).split("/cut")));

		super.appendHoverText(stack, world, tooltip, flag);
	}

//	public String getName()
//	{
//		return name;
//	}
}