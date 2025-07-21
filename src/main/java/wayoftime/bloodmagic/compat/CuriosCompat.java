package wayoftime.bloodmagic.compat;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import java.util.Map;
import java.util.Optional;

public class CuriosCompat
{
	public void registerInventory()
	{
		BloodMagicAPI.INSTANCE.registerInventoryProvider("curiosInventory", player -> getCuriosInventory(player));
		BloodMagicAPI.INSTANCE.registerActiveInventoryProvider("curiosInventory");
	}

	public NonNullList<ItemStack> getCuriosInventory(Player player)
	{
		Optional<ICuriosItemHandler> curioInv = CuriosApi.getCuriosInventory(player).resolve();
		if (curioInv.isEmpty()) {
			return NonNullList.create();
		}
		IItemHandlerModifiable itemHandler = curioInv.get().getEquippedCurios();
		NonNullList<ItemStack> inventory = NonNullList.create();
		for (int i = 0; i < itemHandler.getSlots(); i++)
		{
			inventory.add(itemHandler.getStackInSlot(i));
		}
		return inventory;
	}

	public int recalculateCuriosSlots(Player player)
	{
		ICurioStacksHandler livingArmourSockets = CuriosApi.getCuriosInventory(player).resolve().get().getCurios().get("living_armour_socket");
		if (LivingUtil.hasFullSet(player))
		{
			LivingStats stats = LivingStats.fromPlayer(player);
			int curiosLevel = (stats != null) ? stats.getLevel(LivingArmorRegistrar.UPGRADE_CURIOS_SOCKET.get().getKey()) : 0;
			livingArmourSockets.removeModifier(player.getUUID());
			if (curiosLevel != 0)
			{
				livingArmourSockets.addTransientModifier(new AttributeModifier(player.getUUID(), "legacy", Double.valueOf(LivingArmorRegistrar.UPGRADE_CURIOS_SOCKET.get().getBonusValue("slots", curiosLevel).intValue()), AttributeModifier.Operation.ADDITION));
			}
			return curiosLevel;
		} else
		{
			livingArmourSockets.removeModifier(player.getUUID());
			return 0;
		}
	}
}
