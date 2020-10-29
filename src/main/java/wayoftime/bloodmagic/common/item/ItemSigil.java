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
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.iface.IBindable;
import wayoftime.bloodmagic.iface.ISigil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;

/**
 * Base class for all (static) sigils.
 */
public class ItemSigil extends Item implements IBindable, ISigil
{
	private int lpUsed;

	public ItemSigil(Properties prop, int lpUsed)
	{
		super(prop);

		this.lpUsed = lpUsed;
	}

	public boolean isUnusable(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		return stack.getTag().getBoolean(Constants.NBT.UNUSABLE);
	}

	public ItemStack setUnusable(ItemStack stack, boolean unusable)
	{
		NBTHelper.checkNBT(stack);

		stack.getTag().putBoolean(Constants.NBT.UNUSABLE, unusable);
		return stack;
	}

	public int getLpUsed()
	{
		return lpUsed;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (!stack.hasTag())
			return;

		Binding binding = getBinding(stack);
		if (binding != null)
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.currentOwner", binding.getOwnerName()));
	}
}
