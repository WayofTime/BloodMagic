package wayoftime.bloodmagic.util.handler.event;

import java.util.Map.Entry;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.world.BlockEvent.BlockToolInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.ItemExperienceBook;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.event.ItemBindEvent;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.network.DemonAuraClientPacket;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.item.IBloodOrb;
import wayoftime.bloodmagic.util.helper.BindableHelper;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;
import wayoftime.bloodmagic.will.DemonWillHolder;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GenericHandler
{
	// Handles binding of IBindable's as well as setting a player's highest orb tier
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.RightClickItem event)
	{
		if (event.getWorld().isRemote)
			return;

		PlayerEntity player = event.getPlayer();

		if (PlayerHelper.isFakePlayer(player))
			return;

		ItemStack held = event.getItemStack();
		if (!held.isEmpty() && held.getItem() instanceof IBindable)
		{ // Make sure it's bindable
			IBindable bindable = (IBindable) held.getItem();
			Binding binding = bindable.getBinding(held);
			if (binding == null)
			{ // If the binding is null, let's create one
				if (bindable.onBind(player, held))
				{
					ItemBindEvent toPost = new ItemBindEvent(player, held);
					if (MinecraftForge.EVENT_BUS.post(toPost)) // Allow cancellation of binding
						return;

					BindableHelper.applyBinding(held, player); // Bind item to the player
				}
				// If the binding exists, we'll check if the player's name has changed since
				// they last used it and update that if so.
			} else if (binding.getOwnerId().equals(player.getGameProfile().getId()) && !binding.getOwnerName().equals(player.getGameProfile().getName()))
			{
				binding.setOwnerName(player.getGameProfile().getName());
				BindableHelper.applyBinding(held, binding);
			}
		}

		if (!held.isEmpty() && held.getItem() instanceof IBloodOrb)
		{
			IBloodOrb bloodOrb = (IBloodOrb) held.getItem();
			SoulNetwork network = NetworkHelper.getSoulNetwork(player);

			BloodOrb orb = bloodOrb.getOrb(held);
			if (orb == null)
				return;

			if (orb.getTier() > network.getOrbTier())
				network.setOrbTier(orb.getTier());
		}
	}

	@SubscribeEvent
	public void onPlayerLeftClickAir(PlayerInteractEvent.LeftClickEmpty event)
	{
//		if (event.getItemStack().getItem() instanceof ItemSentientScythe)
//		{
//			((ItemSentientScythe) event.getItemStack().getItem()).onLeftClickAir(event.getItemStack(), event.getEntityLiving());
//		}
	}

	@SubscribeEvent
	public void onHoe(BlockToolInteractEvent event)
	{
		if (event.getToolType() == ToolType.HOE && Tags.Blocks.NETHERRACK.contains(event.getState().getBlock()))
		{
			event.setFinalState(BloodMagicBlocks.NETHER_SOIL.get().getDefaultState());
		}
	}

	// Experience Tome
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onExperiencePickup(PlayerXpEvent.PickupXp event)
	{
		PlayerEntity player = event.getPlayer();
		Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWithEnchantment(Enchantments.MENDING, player);

		if (entry != null)
		{
			ItemStack itemStack = entry.getValue();
			if (!itemStack.isEmpty() && itemStack.isDamaged())
			{
				int i = Math.min(xpToDurability(event.getOrb().xpValue), itemStack.getDamage());
				event.getOrb().xpValue -= durabilityToXp(i);
				itemStack.setDamage(itemStack.getDamage() - i);
			}
		}

		if (!player.getEntityWorld().isRemote)
		{
			for (ItemStack stack : player.inventory.mainInventory)
			{
				if (stack.getItem() instanceof ItemExperienceBook)
				{
					ItemExperienceBook.addExperience(stack, event.getOrb().xpValue);
					event.getOrb().xpValue = 0;
					break;
				}
			}
		}
	}

	private static int xpToDurability(int xp)
	{
		return xp * 2;
	}

	private static int durabilityToXp(int durability)
	{
		return durability / 2;
	}

	public static void sendPlayerDemonWillAura(PlayerEntity player)
	{
		if (player instanceof ServerPlayerEntity)
		{
			BlockPos pos = player.getPosition();
			DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(WorldDemonWillHandler.getDimensionResourceLocation(player.world), pos.getX() >> 4, pos.getZ() >> 4);
			if (holder != null)
			{
				BloodMagic.packetHandler.sendTo(new DemonAuraClientPacket(holder), (ServerPlayerEntity) player);
			} else
			{
				BloodMagic.packetHandler.sendTo(new DemonAuraClientPacket(new DemonWillHolder()), (ServerPlayerEntity) player);
			}
		}
	}
}
