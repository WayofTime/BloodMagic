package wayoftime.bloodmagic.util.handler.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWill;
import wayoftime.bloodmagic.api.compat.IDemonWillWeapon;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WillHandler
{
	private static final HashMap<ResourceLocation, Integer> SERVER_TICKS = new HashMap<>();

	// Adds Will to player
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		ItemStack stack = event.getItem().getItem();
		if (stack.getItem() instanceof IDemonWill)
		{
			Player player = event.getEntity();
			EnumDemonWillType pickupType = ((IDemonWill) stack.getItem()).getType(stack);
			ItemStack remainder = PlayerDemonWillHandler.addDemonWill(player, stack);

			if (remainder == null || ((IDemonWill) stack.getItem()).getWill(pickupType, stack) < 0.0001 || PlayerDemonWillHandler.isDemonWillFull(pickupType, player))
			{
				stack.setCount(0);
				event.setResult(Event.Result.ALLOW);
			}
		}
	}

//	@SubscribeEvent
//	public static void onEntityAttacked(LivingDeathEvent event)
//	{
//		if (event.getSource() instanceof EntityDamageSourceIndirect)
//		{
//			Entity sourceEntity = event.getSource().getImmediateSource();
//
//			if (sourceEntity instanceof EntitySentientArrow)
//			{
//				((EntitySentientArrow) sourceEntity).reimbursePlayer(event.getEntityLiving(), event.getEntityLiving().getMaxHealth());
//			}
//		}
//	}

	// Add/Drop Demon Will for Player
	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event)
	{
		LivingEntity attackedEntity = event.getEntity();
		DamageSource source = event.getSource();
		Entity entity = source.getEntity();

		if (attackedEntity.hasEffect(BloodMagicPotions.SOUL_SNARE.get()) && (attackedEntity instanceof Mob || attackedEntity.getCommandSenderWorld().getDifficulty() == Difficulty.PEACEFUL))
		{
			MobEffectInstance eff = attackedEntity.getEffect(BloodMagicPotions.SOUL_SNARE.get());
			int lvl = eff.getAmplifier();

			double amountOfSouls = attackedEntity.getCommandSenderWorld().random.nextDouble() * (lvl + 1) * (lvl + 1) * 4 + 1;
			ItemStack soulStack = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_RAW.get()).createWill(amountOfSouls);
			event.getDrops().add(new ItemEntity(attackedEntity.getCommandSenderWorld(), attackedEntity.getX(), attackedEntity.getY(), attackedEntity.getZ(), soulStack));
		}

		if (entity != null && entity instanceof Player)
		{
			Player player = (Player) entity;
			ItemStack heldStack = player.getMainHandItem();
			if (heldStack.getItem() instanceof IDemonWillWeapon && !player.getCommandSenderWorld().isClientSide)
			{
				IDemonWillWeapon demonWillWeapon = (IDemonWillWeapon) heldStack.getItem();
				List<ItemStack> droppedSouls = demonWillWeapon.getRandomDemonWillDrop(attackedEntity, player, heldStack, event.getLootingLevel());
				if (!droppedSouls.isEmpty())
				{
					ItemStack remainder;
					for (ItemStack willStack : droppedSouls)
					{
						remainder = PlayerDemonWillHandler.addDemonWill(player, willStack);

						if (!remainder.isEmpty())
						{
							EnumDemonWillType pickupType = ((IDemonWill) remainder.getItem()).getType(remainder);
							if (((IDemonWill) remainder.getItem()).getWill(pickupType, remainder) >= 0.0001)
							{
								event.getDrops().add(new ItemEntity(attackedEntity.getCommandSenderWorld(), attackedEntity.getX(), attackedEntity.getY(), attackedEntity.getZ(), remainder));
							}
						}
					}
					player.inventoryMenu.broadcastChanges();
				}
			}
		}
	}
}
