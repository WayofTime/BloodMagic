package wayoftime.bloodmagic.compat;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.util.ISlotHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

import java.util.Map;
import java.util.UUID;

public class CuriosCompat
{
	public void registerInventory()
	{
		BloodMagicAPI.INSTANCE.registerInventoryProvider("curiosInventory", player -> getCuriosInventory(player));
		BloodMagicAPI.INSTANCE.registerActiveInventoryProvider("curiosInventory");
	}

	public NonNullList<ItemStack> getCuriosInventory(Player player)
	{
		IItemHandlerModifiable itemHandler = CuriosApi.getCuriosInventory(player).resolve().get().getEquippedCurios();
		NonNullList<ItemStack> inventory = NonNullList.create();
		for (int i = 0; i < itemHandler.getSlots(); i++)
		{
			inventory.add(itemHandler.getStackInSlot(i));
		}
		return inventory;
	}

	public int recalculateCuriosSlots(Player player)
	{
		Map<String, ICurioStacksHandler> curiosMap = CuriosApi.getCuriosInventory(player).resolve().get().getCurios();
		if (LivingUtil.hasFullSet(player))
		{
			Double amount = 0.0;
			LivingStats stats = LivingStats.fromPlayer(player);
			int curiosLevel = stats != null ? stats.getLevel(LivingArmorRegistrar.UPGRADE_CURIOS_SOCKET.get().getKey())
					: 0;

			if (curiosLevel != 0)
			{
				amount = Double.valueOf(LivingArmorRegistrar.UPGRADE_CURIOS_SOCKET.get().getBonusValue("slots", curiosLevel).intValue());
			}
			System.out.println("VT Test Code: amount = " + amount);
			curiosMap.get("living_armour_socket").addPermanentModifier(new AttributeModifier(player.getUUID(), "legacy", amount, AttributeModifier.Operation.ADDITION));
			return curiosLevel;
		} else
		{
			curiosMap.get("living_armour_socket").addPermanentModifier(new AttributeModifier(player.getUUID(), "legacy", 0.0, AttributeModifier.Operation.ADDITION));
			return 0;
		}
	}
}
