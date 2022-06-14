package wayoftime.bloodmagic.client.hud.element;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionHand;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;

public abstract class ElementDivinedInformation<T extends BlockEntity> extends ElementTileInformation<T>
{

	private final boolean simple;

	public ElementDivinedInformation(int lines, boolean simple, Class<T> tileClass)
	{
		super(100, lines, tileClass);
		this.simple = simple;
	}

	@Override
	public boolean shouldRender(Minecraft minecraft)
	{
		Player player = Minecraft.getInstance().player;
		ItemStack sigilStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		boolean flag = false;
		if (simple)
		{
			if (sigilStack.getItem() == BloodMagicItems.DIVINATION_SIGIL.get() || sigilStack.getItem() == BloodMagicItems.SEER_SIGIL.get())
				flag = true;
			else
				flag = isFlagSigilHolding(sigilStack, true);

			if (!flag)
			{
				sigilStack = player.getItemInHand(InteractionHand.OFF_HAND);
				if (sigilStack.getItem() == BloodMagicItems.DIVINATION_SIGIL.get() || sigilStack.getItem() == BloodMagicItems.SEER_SIGIL.get())
					flag = true;
				else
					flag = isFlagSigilHolding(sigilStack, true);
			}

		} else
		{
			if (sigilStack.getItem() == BloodMagicItems.SEER_SIGIL.get())
				flag = true;
			else
				flag = isFlagSigilHolding(sigilStack, false);

			if (!flag)
			{
				sigilStack = player.getItemInHand(InteractionHand.OFF_HAND);
				if (sigilStack.getItem() == BloodMagicItems.SEER_SIGIL.get())
					flag = true;
				else
					flag = isFlagSigilHolding(sigilStack, false);
			}
		}

		return super.shouldRender(minecraft) && flag;
	}

	private boolean isFlagSigilHolding(ItemStack sigilStack, boolean simple)
	{
		if (sigilStack.getItem() instanceof ItemSigilHolding)
		{
			List<ItemStack> internalInv = ItemSigilHolding.getInternalInventory(sigilStack);
			int currentSlot = ItemSigilHolding.getCurrentItemOrdinal(sigilStack);
			if (internalInv != null && !internalInv.get(currentSlot).isEmpty())
			{
				return (internalInv.get(currentSlot).getItem() == BloodMagicItems.SEER_SIGIL.get() && !simple) || (internalInv.get(currentSlot).getItem() == BloodMagicItems.DIVINATION_SIGIL.get() && simple);
			}
		}
		return false;
	}
}
