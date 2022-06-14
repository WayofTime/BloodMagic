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

public class ItemSigilToggleableBase extends ItemSigilToggleable// implements IMeshProvider
{
	protected final String tooltipBase;
	private final String name;

	public ItemSigilToggleableBase(String name, int lpUsed)
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB), lpUsed);

		this.name = name;
		this.tooltipBase = "tooltip.bloodmagic.sigil." + name + ".";

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		super.appendHoverText(stack, world, tooltip, flag);
		if (!stack.hasTag())
			return;

		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic." + (getActivated(stack) ? "activated"
				: "deactivated")).withStyle(TextFormatting.GRAY));
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	public ItemMeshDefinition getMeshDefinition()
//	{
//		return new CustomMeshDefinitionActivatable("sigil_" + name.toLowerCase(Locale.ROOT));
//	}
//
//	@Override
//	public void gatherVariants(Consumer<String> variants)
//	{
//		variants.accept("active=false");
//		variants.accept("active=true");
//	}
}