package wayoftime.bloodmagic.compat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.items.IItemHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

public class CuriosCompat
{
	public void setupSlots(InterModEnqueueEvent evt)
	{
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
	}

	public void registerInventory()
	{
		BloodMagicAPI.INSTANCE.registerInventoryProvider("curiosInventory", player -> getCuriosInventory(player));
	}

	public NonNullList<ItemStack> getCuriosInventory(PlayerEntity player)
	{
		IItemHandler itemHandler = CuriosApi.getCuriosHelper().getEquippedCurios(player).resolve().get();

		NonNullList<ItemStack> inventory = NonNullList.create();
		for (int i = 0; i < itemHandler.getSlots(); i++)
		{
			inventory.add(itemHandler.getStackInSlot(i));
		}
		return inventory;
	}

}
