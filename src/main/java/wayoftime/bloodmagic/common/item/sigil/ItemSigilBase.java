package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.ItemSigil;

import java.util.List;

public class ItemSigilBase extends ItemSigil
{
	protected final String tooltipBase;
//	private final String name;

	public ItemSigilBase(String name, int lpUsed)
	{
		super(new Item.Properties().stacksTo(1), lpUsed);
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
		tooltip.add(Component.translatable(tooltipBase + "desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
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