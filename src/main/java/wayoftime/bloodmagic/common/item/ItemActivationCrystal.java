package wayoftime.bloodmagic.common.item;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

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
import wayoftime.bloodmagic.core.data.Binding;

public class ItemActivationCrystal extends Item implements IBindable
{
	final CrystalType type;

	public ItemActivationCrystal(CrystalType type)
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB));
		this.type = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.activationcrystal." + type.name().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY));

		if (!stack.hasTag())
			return;

		Binding binding = getBinding(stack);
		if (binding != null)
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.currentOwner", binding.getOwnerName()).withStyle(ChatFormatting.GRAY));

		super.appendHoverText(stack, world, tooltip, flag);
	}

	public int getCrystalLevel(ItemStack stack)
	{
		return this.type.equals(CrystalType.CREATIVE) ? Integer.MAX_VALUE : type.ordinal() + 1;
	}

	public enum CrystalType
	{
		WEAK,
		AWAKENED,
		CREATIVE,;

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