package wayoftime.bloodmagic.common.item.sigil;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB), lpUsed);
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
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent(tooltipBase + "desc").mergeStyle(TextFormatting.ITALIC));
//		if (TextHelper.canTranslate(tooltipBase + "desc"))
//			tooltip.addAll(Arrays.asList(WordUtils.wrap(TextHelper.localizeEffect(tooltipBase
//					+ "desc"), 30, "/cut", false).split("/cut")));

		super.addInformation(stack, world, tooltip, flag);
	}

//	public String getName()
//	{
//		return name;
//	}
}