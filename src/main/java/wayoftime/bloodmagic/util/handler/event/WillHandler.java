package wayoftime.bloodmagic.util.handler.event;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.will.EnumDemonWillType;
import wayoftime.bloodmagic.will.IDemonWill;
import wayoftime.bloodmagic.will.IDemonWillWeapon;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WillHandler
{
	private static final HashMap<Integer, Integer> SERVER_TICKS = new HashMap<>();

	// Adds Will to player
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		ItemStack stack = event.getItem().getItem();
		if (stack.getItem() instanceof IDemonWill)
		{
			PlayerEntity player = event.getPlayer();
			EnumDemonWillType pickupType = ((IDemonWill) stack.getItem()).getType(stack);
			ItemStack remainder = PlayerDemonWillHandler.addDemonWill(player, stack);

			if (remainder == null || ((IDemonWill) stack.getItem()).getWill(pickupType, stack) < 0.0001
					|| PlayerDemonWillHandler.isDemonWillFull(pickupType, player))
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
		LivingEntity attackedEntity = event.getEntityLiving();
		DamageSource source = event.getSource();
		Entity entity = source.getTrueSource();

		if (attackedEntity.isPotionActive(BloodMagicPotions.SOUL_SNARE) && (attackedEntity instanceof MobEntity
				|| attackedEntity.getEntityWorld().getDifficulty() == Difficulty.PEACEFUL))
		{
			EffectInstance eff = attackedEntity.getActivePotionEffect(BloodMagicPotions.SOUL_SNARE);
			int lvl = eff.getAmplifier();

			double amountOfSouls = attackedEntity.getEntityWorld().rand.nextDouble() * (lvl + 1) * (lvl + 1) * 4 + 1;
			ItemStack soulStack = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_RAW.get()).createWill(amountOfSouls);
			event.getDrops().add(new ItemEntity(attackedEntity.getEntityWorld(), attackedEntity.getPosX(), attackedEntity.getPosY(), attackedEntity.getPosZ(), soulStack));
		}

		if (entity != null && entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) entity;
			ItemStack heldStack = player.getHeldItemMainhand();
			if (heldStack.getItem() instanceof IDemonWillWeapon && !player.getEntityWorld().isRemote)
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
								event.getDrops().add(new ItemEntity(attackedEntity.getEntityWorld(), attackedEntity.getPosX(), attackedEntity.getPosY(), attackedEntity.getPosZ(), remainder));
							}
						}
					}
					player.container.detectAndSendChanges();
				}
			}
		}
	}
}
