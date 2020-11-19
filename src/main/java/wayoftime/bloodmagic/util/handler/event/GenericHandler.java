package wayoftime.bloodmagic.util.handler.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.event.ItemBindEvent;
import wayoftime.bloodmagic.api.item.IBindable;
import wayoftime.bloodmagic.network.DemonAuraClientPacket;
import wayoftime.bloodmagic.api.item.BloodOrb;
import wayoftime.bloodmagic.api.item.IBloodOrb;
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
