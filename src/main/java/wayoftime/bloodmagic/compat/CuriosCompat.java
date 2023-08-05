package wayoftime.bloodmagic.compat;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.items.IItemHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.util.ISlotHelper;
import wayoftime.bloodmagic.BloodMagic;
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
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("living_armour_socket").size(0).icon(BloodMagic.rl("item/curios_empty_living_armour_socket")).build());
	}

	public void registerInventory()
	{
		BloodMagicAPI.INSTANCE.registerInventoryProvider("curiosInventory", player -> getCuriosInventory(player));
		BloodMagicAPI.INSTANCE.registerActiveInventoryProvider("curiosInventory");
	}

	public NonNullList<ItemStack> getCuriosInventory(Player player)
	{
		IItemHandler itemHandler = CuriosApi.getCuriosHelper().getEquippedCurios(player).resolve().get();

		NonNullList<ItemStack> inventory = NonNullList.create();
		for (int i = 0; i < itemHandler.getSlots(); i++)
		{
			inventory.add(itemHandler.getStackInSlot(i));
		}
		return inventory;
	}

	public int recalculateCuriosSlots(Player player)
	{
		ISlotHelper slotHelper = CuriosApi.getSlotHelper();
		if (LivingUtil.hasFullSet(player))
		{
			LivingStats stats = LivingStats.fromPlayer(player);

			int curiosLevel = stats != null ? stats.getLevel(LivingArmorRegistrar.UPGRADE_CURIOS_SOCKET.get().getKey())
					: 0;

			if (curiosLevel == 0)
			{
				slotHelper.setSlotsForType("living_armour_socket", player, 0);
			} else
			{
				int slotCount = LivingArmorRegistrar.UPGRADE_CURIOS_SOCKET.get().getBonusValue("slots", curiosLevel).intValue();
				slotHelper.setSlotsForType("living_armour_socket", player, slotCount);
			}
			return curiosLevel;
		} else
		{
			slotHelper.setSlotsForType("living_armour_socket", player, 0);
			return 0;
		}
	}
}
