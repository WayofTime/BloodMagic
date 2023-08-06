package wayoftime.bloodmagic.client.hud.element;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;
import wayoftime.bloodmagic.util.helper.InventoryHelper;

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

		NonNullList<ItemStack> inventory = InventoryHelper.getActiveInventories(player);

		boolean flag = false;
		for (ItemStack sigilStack : inventory)
		{
			if ((sigilStack.getItem() == BloodMagicItems.DIVINATION_SIGIL.get() && simple) || sigilStack.getItem() == BloodMagicItems.SEER_SIGIL.get())
				flag = true;
			else
				flag = isFlagSigilHolding(sigilStack, simple);

			if (flag)
				break;
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
				return (internalInv.get(currentSlot).getItem() == BloodMagicItems.SEER_SIGIL.get()) || (internalInv.get(currentSlot).getItem() == BloodMagicItems.DIVINATION_SIGIL.get() && simple);
			}
		}
		return false;
	}
}
