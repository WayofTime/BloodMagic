package wayoftime.bloodmagic.client.hud.element;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

public abstract class ElementDivinedInformation<T extends TileEntity> extends ElementTileInformation<T>
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
		PlayerEntity player = Minecraft.getInstance().player;
		ItemStack sigilStack = player.getHeldItem(Hand.MAIN_HAND);
		boolean flag = false;
		if (simple)
		{
			if (sigilStack.getItem() == BloodMagicItems.DIVINATION_SIGIL.get() || sigilStack.getItem() == BloodMagicItems.SEER_SIGIL.get())
				flag = true;
			else
				flag = isFlagSigilHolding(sigilStack, true);

//			if (!flag)
//			{
//				sigilStack = player.getHeldItem(Hand.OFF_HAND);
//				if (sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_DIVINATION || sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
//					flag = true;
//				else
//					flag = isFlagSigilHolding(sigilStack, true);
//			}

		} else
		{
			if (sigilStack.getItem() == BloodMagicItems.SEER_SIGIL.get())
				flag = true;
			else
				flag = isFlagSigilHolding(sigilStack, false);
//
//			if (!flag)
//			{
//				sigilStack = player.getHeldItem(Hand.OFF_HAND);
//				if (sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
//					flag = true;
//				else
//					flag = isFlagSigilHolding(sigilStack, false);
//			}
		}

		return super.shouldRender(minecraft) && flag;
	}

	private boolean isFlagSigilHolding(ItemStack sigilStack, boolean simple)
	{
//		if (sigilStack.getItem() instanceof ItemSigilHolding)
//		{
//			List<ItemStack> internalInv = ItemSigilHolding.getInternalInventory(sigilStack);
//			int currentSlot = ItemSigilHolding.getCurrentItemOrdinal(sigilStack);
//			if (internalInv != null && !internalInv.get(currentSlot).isEmpty())
//			{
//				return (internalInv.get(currentSlot).getItem() == RegistrarBloodMagicItems.SIGIL_SEER && !simple) || (internalInv.get(currentSlot).getItem() == RegistrarBloodMagicItems.SIGIL_DIVINATION && simple);
//			}
//		}
		return false;
	}
}
