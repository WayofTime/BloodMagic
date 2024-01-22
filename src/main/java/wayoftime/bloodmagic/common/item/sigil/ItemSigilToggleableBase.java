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

import java.util.List;

public class ItemSigilToggleableBase extends ItemSigilToggleable// implements IMeshProvider
{
	protected final String tooltipBase;
	private final String name;

	public ItemSigilToggleableBase(String name, int lpUsed)
	{
		super(new Item.Properties().stacksTo(1), lpUsed);

		this.name = name;
		this.tooltipBase = "tooltip.bloodmagic.sigil." + name + ".";

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		super.appendHoverText(stack, world, tooltip, flag);
		if (!stack.hasTag())
			return;

		tooltip.add(Component.translatable("tooltip.bloodmagic." + (getActivated(stack) ? "activated"
				: "deactivated")).withStyle(ChatFormatting.GRAY));
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