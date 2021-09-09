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
import top.theillusivec4.curios.api.type.util.ISlotHelper;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

public class CuriosCompat
{
	public void setupSlots(InterModEnqueueEvent evt)
	{
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("living_armour_socket").lock().build());
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

	public void recalculateCuriosSlots(PlayerEntity player)
	{
		ISlotHelper slotHelper = CuriosApi.getSlotHelper();
		if (LivingUtil.hasFullSet(player))
		{
			int curiosLevel = LivingStats.fromPlayer(player).getLevel(LivingArmorRegistrar.UPGRADE_CURIOS_SOCKET.get().getKey());

			if (curiosLevel == 0)
			{
				slotHelper.lockSlotType("living_armour_socket", player);
			} else
			{
				int slotCount = LivingArmorRegistrar.UPGRADE_CURIOS_SOCKET.get().getBonusValue("slots", curiosLevel).intValue();
				slotHelper.unlockSlotType("living_armour_socket", player);
				slotHelper.setSlotsForType("living_armour_socket", player, slotCount); // why this no work?
			}
		} else
			slotHelper.lockSlotType("living_armour_socket", player);
	}
}
