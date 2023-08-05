package wayoftime.bloodmagic.client.hud.element;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import top.theillusivec4.curios.api.CuriosApi;
import wayoftime.bloodmagic.BloodMagic;
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

		NonNullList<ItemStack> inventory = NonNullList.create();
		if (BloodMagic.curiosLoaded)
		{
			IItemHandler curioSlots = CuriosApi.getCuriosHelper().getEquippedCurios(player).resolve().get();
			for (int i = 0; i < curioSlots.getSlots(); i++)
			{
				inventory.add(curioSlots.getStackInSlot(i));
			}
		}

		inventory.add(player.getItemInHand(InteractionHand.MAIN_HAND));
		inventory.add(player.getItemInHand(InteractionHand.OFF_HAND));

		boolean flag = false;
		for (int i = 0; i < inventory.size(); i++)
		{
			ItemStack sigilStack = inventory.get(i);
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
				return (internalInv.get(currentSlot).getItem() == BloodMagicItems.SEER_SIGIL.get() && !simple) || (internalInv.get(currentSlot).getItem() == BloodMagicItems.DIVINATION_SIGIL.get() && simple);
			}
		}
		return false;
	}
}
