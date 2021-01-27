package wayoftime.bloodmagic.common.item;

import java.util.List;

import javax.annotation.Nonnull;

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
import wayoftime.bloodmagic.core.data.Binding;

public class ItemActivationCrystal extends Item implements IBindable
{
	final CrystalType type;

	public ItemActivationCrystal(CrystalType type)
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
		this.type = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.activationcrystal." + type.name().toLowerCase()).mergeStyle(TextFormatting.GRAY));

		if (!stack.hasTag())
			return;

		Binding binding = getBinding(stack);
		if (binding != null)
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.currentOwner", binding.getOwnerName()).mergeStyle(TextFormatting.GRAY));

		super.addInformation(stack, world, tooltip, flag);
	}

	public int getCrystalLevel(ItemStack stack)
	{
		return this.type.equals(CrystalType.CREATIVE) ? Integer.MAX_VALUE : type.ordinal() + 1;
	}

	public enum CrystalType
	{
		WEAK, AWAKENED, CREATIVE,;

		@Nonnull
		public static ItemStack getStack(int level)
		{
			if (level < 0)
			{
				level = 0;
			}
			switch (level)
			{
			case 0:
				return new ItemStack(BloodMagicItems.WEAK_ACTIVATION_CRYSTAL.get());
			case 1:
				return new ItemStack(BloodMagicItems.AWAKENED_ACTIVATION_CRYSTAL.get());
			default:
				return new ItemStack(BloodMagicItems.CREATIVE_ACTIVATION_CRYSTAL.get());
			}
		}
	}
}