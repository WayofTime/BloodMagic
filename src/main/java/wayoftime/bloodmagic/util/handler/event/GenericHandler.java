package wayoftime.bloodmagic.util.handler.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.item.*;
import wayoftime.bloodmagic.core.AnointmentRegistrar;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.event.ItemBindEvent;
import wayoftime.bloodmagic.event.SacrificeKnifeUsedEvent;
import wayoftime.bloodmagic.network.BloodMagicPacketHandler;
import wayoftime.bloodmagic.network.CycleRitualDivinerPacket;
import wayoftime.bloodmagic.network.DemonAuraClientPacket;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.util.helper.BindableHelper;
import wayoftime.bloodmagic.util.helper.InventoryHelper;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;
import wayoftime.bloodmagic.will.DemonWillHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GenericHandler
{
	public static Map<UUID, Double> bounceMap = new HashMap<>();

//	@SubscribeEvent
//	public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
//	{
//
//		if (event.getSpawnReason() == MobSpawnType.SPAWNER)
//		{
//			ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, BloodMagic.rl("dungeon"));
//			if (event.getEntityLiving().getLevel().dimension().equals(key))
//			{
//				System.out.println("Original: " + event.getResult());
//				System.out.println("Enabling spawn from spawner");
//				event.setResult(Result.ALLOW);
//			}
//
//		}
////		event.getWorld().reso
//	}

	@SubscribeEvent
	public void onItemBurn(FurnaceFuelBurnTimeEvent event)
	{
		ItemStack burnStack = event.getItemStack();
		if (!burnStack.isEmpty() && burnStack.getItem() instanceof ItemLavaCrystal)
		{
			event.setBurnTime(((ItemLavaCrystal) burnStack.getItem()).getBurnTime(burnStack));
		}
	}

	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event)
	{
		LivingEntity eventEntityLiving = event.getEntity();

		if (eventEntityLiving.hasEffect(BloodMagicPotions.HEAVY_HEART.get()))
		{
			int i = eventEntityLiving.getEffect(BloodMagicPotions.HEAVY_HEART.get()).getAmplifier() + 1;
			event.setDamageMultiplier(event.getDamageMultiplier() + i);
			event.setDistance(event.getDistance() + i);
		}

		if (eventEntityLiving.hasEffect(BloodMagicPotions.BOUNCE.get()))
		{
			if (eventEntityLiving instanceof Player)
			{
				Player player = (Player) eventEntityLiving;
				event.setDamageMultiplier(0);
				if (!player.isShiftKeyDown() && event.getDistance() > 1.5)
				{
					if (player.level().isClientSide)
					{
						player.setDeltaMovement(player.getDeltaMovement().multiply(1, -1, 1));
						bounceMap.put(player.getUUID(), player.getDeltaMovement().y());
					} else
					{
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerTickPost(TickEvent.PlayerTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END && bounceMap.containsKey(event.player.getUUID()))
		{
			double motionY = bounceMap.remove(event.player.getUUID());
			event.player.setDeltaMovement(event.player.getDeltaMovement().multiply(1, 0, 1).add(0, motionY, 0));
		}
	}

	// Handles binding of IBindable's as well as setting a player's highest orb tier
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.RightClickItem event)
	{
		if (event.getLevel().isClientSide)
			return;

		Player player = event.getEntity();

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
		if (event.getItemStack().getItem() instanceof ItemRitualDiviner)
		{
			BloodMagicPacketHandler.INSTANCE.sendToServer(new CycleRitualDivinerPacket(event.getEntity().getInventory().selected));
		}
	}

	@SubscribeEvent
	// Called when an entity is set to be hurt. Called before vanilla armour
	// calculations.
	public void onLivingHurt(LivingHurtEvent event)
	{
		Entity sourceEntity = event.getSource().getEntity();
		LivingEntity living = event.getEntity();

		if (sourceEntity instanceof Player)
		{
			Player sourcePlayer = (Player) sourceEntity;
			if (LivingUtil.hasFullSet(sourcePlayer))
			{
				ItemStack mainWeapon = sourcePlayer.getUseItem();
				double additionalDamage = LivingUtil.getAdditionalDamage(sourcePlayer, mainWeapon, living, event.getAmount());
				event.setAmount((float) (event.getAmount() + additionalDamage));
			}

			ItemStack heldStack = sourcePlayer.getMainHandItem();
			AnointmentHolder holder = AnointmentHolder.fromItemStack(heldStack);

			if (holder != null)
			{
				double additionalDamage = holder.getAdditionalDamage(sourcePlayer, heldStack, event.getAmount(), living);

				event.setAmount((float) (event.getAmount() + additionalDamage));
			}
		}

		if (living instanceof Player)
		{
			Player player = (Player) living;
			if (LivingUtil.hasFullSet(player))
			{
				event.setAmount((float) LivingUtil.getDamageReceivedForArmour(player, event.getSource(), event.getAmount()));

				// The factor of 1.6 is due to taking into account iron armour's protection at
				// ~11 damage
				double factor = 1.6;
				if (event.getSource().is(DamageTypeTags.IS_PROJECTILE))
				{
//					LivingStats stats = LivingStats.fromPlayer(player);
//					stats.addExperience(LivingArmorRegistrar.TEST_UPGRADE.get().getKey(), 10);
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_ARROW_PROTECT.get(), event.getAmount() / factor);
				} else if (Utils.isMeleeDamage(event.getSource()))
				{
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_PHYSICAL_PROTECT.get(), event.getAmount() / factor);
				}

				if (event.getSource().is(DamageTypes.FALL))
				{
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_FALL_PROTECT.get(), event.getAmount() / factor);
				}
			}
		}

		if (!event.getSource().is(DamageTypes.MAGIC) && living.hasEffect(BloodMagicPotions.OBSIDIAN_CLOAK.get()))
		{
			float modifier = (float) (1 - 0.2 * (1 + living.getEffect(BloodMagicPotions.OBSIDIAN_CLOAK.get()).getAmplifier()));
			event.setAmount((float) (event.getAmount() * Math.max(0, modifier)));
		}
	}

	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent event)
	{
		if (!event.isCancelable())
		{
			return;
		}

		Player sourcePlayer = event.getEntity();
		if (LivingUtil.hasFullSet(sourcePlayer))
		{
			LivingStats stats = LivingStats.fromPlayer(sourcePlayer, true);
			if (event.getHand() == InteractionHand.OFF_HAND)
			{
				int level = stats.getLevel(LivingArmorRegistrar.DOWNGRADE_CRIPPLED_ARM.get().getKey());
				if (level > 0)
				{
					event.setCanceled(true);
					return;
				}
			}

			if (event.getItemStack().getUseAnimation() == UseAnim.DRINK)
			{
				ItemStack drinkStack = event.getItemStack();
				if (!(drinkStack.getItem() instanceof SplashPotionItem))
				{
					int level = stats.getLevel(LivingArmorRegistrar.DOWNGRADE_QUENCHED.get().getKey());
					if (level > 0)
					{
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	// Called after armour calculations (including LivingHurtEvent) are parsed.
	// Damage that the player should receive after armour/absorption hearts.
	public void onLivingDamage(LivingDamageEvent event)
	{
		Entity sourceEntity = event.getSource().getEntity();
		LivingEntity living = event.getEntity();

		if (sourceEntity instanceof Player)
		{
			Player sourcePlayer = (Player) sourceEntity;
			if (LivingUtil.hasFullSet(sourcePlayer))
			{
				LivingStats stats = LivingStats.fromPlayer(sourcePlayer, true);
				ItemStack chestStack = sourcePlayer.getItemBySlot(EquipmentSlot.CHEST);

				if (sourcePlayer.isSprinting())
				{
					LivingUtil.applyNewExperience(sourcePlayer, LivingArmorRegistrar.UPGRADE_SPRINT_ATTACK.get(), event.getAmount());
				}

				if (!event.getSource().is(DamageTypeTags.IS_PROJECTILE))
				{
					LivingUtil.applyNewExperience(sourcePlayer, LivingArmorRegistrar.UPGRADE_MELEE_DAMAGE.get(), event.getAmount());
				}

				int battleHungryLevel = stats.getLevel(LivingArmorRegistrar.DOWNGRADE_BATTLE_HUNGRY.get().getKey());
				if (battleHungryLevel > 0)
				{
					int delay = LivingArmorRegistrar.DOWNGRADE_BATTLE_HUNGRY.get().getBonusValue("delay", battleHungryLevel).intValue();

					chestStack.getTag().putInt("battle_cooldown", delay);
				}
			}

			ItemStack heldStack = sourcePlayer.getMainHandItem();
			AnointmentHolder holder = AnointmentHolder.fromItemStack(heldStack);
//			AnointmentHolder holder = AnointmentHolder.fromPlayer(sourcePlayer, Hand.MAIN_HAND);

			if (holder != null)
			{
				boolean hasAnointmentChanged = false;
				int repairLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_WEAPON_REPAIR.get());
				if (repairLevel > 0)
				{
					if (heldStack.isDamageableItem() && heldStack.isDamaged())
					{
						double expBonus = AnointmentRegistrar.ANOINTMENT_WEAPON_REPAIR.get().getBonusValue("exp", repairLevel).doubleValue();

						double repairRatio = heldStack.getXpRepairRatio();
						double durabilityBonus = Math.min(expBonus / repairRatio, heldStack.getDamageValue());
						if (heldStack.getDamageValue() >= durabilityBonus)
						{
							int durabilityAdded = (int) durabilityBonus + (durabilityBonus % 1 > sourcePlayer.level().getRandom().nextDouble()
									? 1
									: 0);
							if (durabilityAdded > 0)
								heldStack.setDamageValue(Math.max(0, heldStack.getDamageValue() - durabilityAdded));

							if (holder.consumeAnointmentDurability(heldStack, EquipmentSlot.MAINHAND, AnointmentRegistrar.ANOINTMENT_WEAPON_REPAIR.get(), sourcePlayer))
							{
								hasAnointmentChanged = true;
							}
						}
					}
				}

				if (holder.consumeAnointmentDurabilityOnHit(heldStack, EquipmentSlot.MAINHAND, sourcePlayer) || hasAnointmentChanged)
				{
					holder.toItemStack(heldStack);
				}
			}

//			System.out.println("Checking consumption. Holder is: " + holder);

		}

//		if (living instanceof PlayerEntity)
//		{
//			PlayerEntity player = (PlayerEntity) living;
//			if (LivingUtil.hasFullSet(player))
//			{
//				if (event.getSource().isProjectile())
//				{
////					LivingStats stats = LivingStats.fromPlayer(player);
////					stats.addExperience(LivingArmorRegistrar.TEST_UPGRADE.get().getKey(), 10);
//					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_ARROW_PROTECT.get(), event.getAmount());
//				} else
//				{
//					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_PHYSICAL_PROTECT.get(), event.getAmount());
//				}
//
//				if (event.getSource() == DamageSource.FALL)
//				{
//					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_FALL_PROTECT.get(), event.getAmount());
//				}
//			}
//		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onExperiencePickupHighest(PlayerXpEvent.PickupXp event)
	{
		LivingEntity living = event.getEntity();
		if (living instanceof Player)
		{
			Player player = (Player) living;
			if (LivingUtil.hasFullSet(player))
			{
				LivingStats stats = LivingStats.fromPlayer(player, true);
				double expModifier = 1 + LivingArmorRegistrar.UPGRADE_EXPERIENCE.get().getBonusValue("exp", stats.getLevel(LivingArmorRegistrar.UPGRADE_EXPERIENCE.get().getKey())).doubleValue();
//				System.out.println("Experience modifier: " + expModifier);

				int xp = event.getOrb().value;

				event.getOrb().value = ((int) Math.floor(xp * expModifier) + (player.level().random.nextDouble() < (xp * expModifier) % 1
						? 1
						: 0));

				LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_EXPERIENCE.get(), event.getOrb().getValue());
			}
		}
	}

//	@SubscribeEvent
//	public void onHoe(BlockToolModificationEvent event)
//	{
//		if(!event.isSimulated())
//		{
//			if (event.getToolType() == ToolType.HOE && Tags.Blocks.NETHERRACK.contains(event.getState().getBlock()))
//			{
//				event.setFinalState(BloodMagicBlocks.NETHER_SOIL.get().defaultBlockState());
//			}
//		}
//		
//	}

	// Experience Tome
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onExperiencePickup(PlayerXpEvent.PickupXp event)
	{
		Player player = event.getEntity();
		Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, player);

		if (entry != null)
		{
			ItemStack itemStack = entry.getValue();
			if (!itemStack.isEmpty() && itemStack.isDamaged())
			{
				int i = Math.min(xpToDurability(event.getOrb().value), itemStack.getDamageValue());
				event.getOrb().value -= durabilityToXp(i);
				itemStack.setDamageValue(itemStack.getDamageValue() - i);
			}
		}

		if (!player.getCommandSenderWorld().isClientSide)
		{
			for (ItemStack stack : InventoryHelper.getAllInventories(player))
			{
				if (stack.getItem() instanceof ItemExperienceBook)
				{
					ItemExperienceBook.addExperience(stack, event.getOrb().value);
					event.getOrb().value = 0;
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

	public static void sendPlayerDemonWillAura(Player player)
	{
		if (player instanceof ServerPlayer)
		{
			BlockPos pos = player.blockPosition();
			DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(WorldDemonWillHandler.getDimensionResourceLocation(player.level()), pos.getX() >> 4, pos.getZ() >> 4);
			if (holder != null)
			{
				BloodMagic.packetHandler.sendTo(new DemonAuraClientPacket(holder), (ServerPlayer) player);
			} else
			{
				BloodMagic.packetHandler.sendTo(new DemonAuraClientPacket(new DemonWillHolder()), (ServerPlayer) player);
			}
		}
	}

	@SubscribeEvent
	public void onHeal(LivingHealEvent event)
	{
		LivingEntity living = event.getEntity();
		if (living instanceof Player)
		{
			Player player = (Player) living;
			if (LivingUtil.hasFullSet(player))
			{
				LivingStats stats = LivingStats.fromPlayer(player, true);
				float healModifier = 1 + LivingArmorRegistrar.DOWNGRADE_SLOW_HEAL.get().getBonusValue("heal_modifier", stats.getLevel(LivingArmorRegistrar.DOWNGRADE_SLOW_HEAL.get().getKey())).floatValue();
				event.setAmount(event.getAmount() * healModifier);
				LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_HEALTH.get(), event.getAmount());
			}
		}
	}

	@SubscribeEvent
	public void onSelfSacrifice(SacrificeKnifeUsedEvent event)
	{
		if (LivingUtil.hasFullSet(event.player))
		{
			LivingStats stats = LivingStats.fromPlayer(event.player, true);
			double bonus = LivingArmorRegistrar.UPGRADE_SELF_SACRIFICE.get().getBonusValue("self_mod", stats.getLevel(LivingArmorRegistrar.UPGRADE_SELF_SACRIFICE.get().getKey())).doubleValue();
			event.lpAdded = (int) Math.round(event.lpAdded * (1 + bonus));
			LivingUtil.applyNewExperience(event.player, LivingArmorRegistrar.UPGRADE_SELF_SACRIFICE.get(), event.healthDrained);
		}
	}

	public static Map<UUID, Double> posXMap = new HashMap<>();
	public static Map<UUID, Double> posZMap = new HashMap<>();
	public static Map<UUID, Integer> foodMap = new HashMap<>();
	public static Map<UUID, Float> prevFlySpeedMap = new HashMap<>();

	Map<UUID, TargetGoal> goalMap = new HashMap<>();
	Map<UUID, MeleeAttackGoal> attackGoalMap = new HashMap<>();

	@SubscribeEvent
	public void onPotionAdded(MobEffectEvent.Added event)
	{
		if (event.getEffectInstance().getEffect() == BloodMagicPotions.FLIGHT.get() && event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			player.getAbilities().mayfly = true;
			if (!prevFlySpeedMap.containsKey(player.getUUID()))
			{
				prevFlySpeedMap.put(player.getUUID(), player.getAbilities().getFlyingSpeed());
			}

			if (event.getEntity().level().isClientSide)
				player.getAbilities().setFlyingSpeed(getFlySpeedForFlightLevel(event.getEffectInstance().getAmplifier()));
			player.onUpdateAbilities();
		}

	}

	@SubscribeEvent
	public void onPotionExpired(MobEffectEvent.Expired event)
	{
		if (event.getEffectInstance().getEffect() == BloodMagicPotions.FLIGHT.get() && event.getEntity() instanceof Player)
		{
			((Player) event.getEntity()).getAbilities().mayfly = ((Player) event.getEntity()).isCreative();
			((Player) event.getEntity()).getAbilities().flying = false;

			if (event.getEntity().level().isClientSide)
			{
				((Player) event.getEntity()).getAbilities().setFlyingSpeed(prevFlySpeedMap.getOrDefault((((Player) event.getEntity()).getUUID()), getFlySpeedForFlightLevel(-1)));
				prevFlySpeedMap.remove(((Player) event.getEntity()).getUUID());
			}

			((Player) event.getEntity()).onUpdateAbilities();
		}
	}

	private float getFlySpeedForFlightLevel(int level)
	{
		if (level >= 0)
		{
			return 0.05F * (level + 1);
		} else
		{
			// Default fly speed
			return 0.05F;
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityUpdate(LivingEvent.LivingTickEvent event)
	{
		if (event.getEntity().level().isClientSide)
		{
			if (event.getEntity() instanceof Player)
			{
				Player player = (Player) event.getEntity();
				if (LivingUtil.hasFullSet(player))
				{
					LivingStats stats = LivingStats.fromPlayer(player, true);
					if (!player.onGround() && player.getDeltaMovement().y() < 0)
					{
						int jumpLevel = stats.getLevel(LivingArmorRegistrar.UPGRADE_JUMP.get().getKey());
						double fallDistanceMultiplier = LivingArmorRegistrar.UPGRADE_JUMP.get().getBonusValue("fall", jumpLevel).doubleValue();
						player.fallDistance = (float) Math.max(0, player.fallDistance + fallDistanceMultiplier * player.getDeltaMovement().y());
//				System.out.println("Player's motion: " + player.getMotion().getY() + ", Player's fall reduction multiplier: " + fallDistanceMultiplier + ", Player's final fall distance: " + player.fallDistance);
					}
				}
			}
		}

		if (event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			if (player.hasEffect(BloodMagicPotions.FLIGHT.get()))
			{
				player.fallDistance = 0;
				if (!player.getAbilities().mayfly || !prevFlySpeedMap.containsKey(player.getUUID()))
				{
					prevFlySpeedMap.put(player.getUUID(), player.getAbilities().getFlyingSpeed());
					player.getAbilities().mayfly = true;
					if (player.level().isClientSide)
						player.getAbilities().setFlyingSpeed(getFlySpeedForFlightLevel(player.getEffect(BloodMagicPotions.FLIGHT.get()).getAmplifier()));
					player.onUpdateAbilities();
				}
			}

			float percentIncrease = 0;

//			System.out.println("Player's motion: " + player.getMotion().getY() + ", Player's final fall distance: " + player.fallDistance);

			if (LivingUtil.hasFullSet(player))
			{
				LivingStats stats = LivingStats.fromPlayer(player, true);
				ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);

				if (!event.getEntity().level().isClientSide)
				{
					int currentFood = player.getFoodData().getFoodLevel();

					if (foodMap.getOrDefault(player.getUUID(), 19) < currentFood)
					{
						LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_KNOCKBACK_RESIST.get(), currentFood - foodMap.getOrDefault(player.getUUID(), 19));
					}

					foodMap.put(player.getUUID(), currentFood);
				}

//				percentIncrease += LivingArmorRegistrar.UPGRADE_SPEED.get().getBonusValue("speed_modifier", stats.getLevel(LivingArmorRegistrar.UPGRADE_SPEED.get().getKey())).doubleValue();
				if (player.isSprinting())
				{
					int speedTime = LivingArmorRegistrar.UPGRADE_SPEED.get().getBonusValue("speed_time", stats.getLevel(LivingArmorRegistrar.UPGRADE_SPEED.get().getKey())).intValue();
					if (speedTime > 0)
					{
						int speedLevel = LivingArmorRegistrar.UPGRADE_SPEED.get().getBonusValue("speed_level", stats.getLevel(LivingArmorRegistrar.UPGRADE_SPEED.get().getKey())).intValue();
						player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, speedTime, speedLevel, true, false));
					}
				}

				double distance = 0;

				if (posXMap.containsKey(player.getUUID()))
				{
					distance = Math.sqrt((player.getX() - posXMap.get(player.getUUID())) * (player.getX() - posXMap.get(player.getUUID())) + (player.getZ() - posZMap.get(player.getUUID())) * (player.getZ() - posZMap.get(player.getUUID())));
				}

//				System.out.println("Distance travelled: " + distance);
				if (player.onGround() && distance > 0 && distance < 50)
				{
					distance *= (1 + percentIncrease);
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_SPEED.get(), distance);
//					System.out.println("Current exp for speed: " + LivingStats.fromPlayer(player).getUpgrades().getOrDefault(LivingArmorRegistrar.UPGRADE_SPEED.get(), 0d));
				}

				if (!player.onGround() && player.getDeltaMovement().y() < 0)
				{

					int jumpLevel = stats.getLevel(LivingArmorRegistrar.UPGRADE_JUMP.get().getKey());
					double fallDistanceMultiplier = LivingArmorRegistrar.UPGRADE_JUMP.get().getBonusValue("fall", jumpLevel).doubleValue();
					player.fallDistance = (float) Math.max(0, player.fallDistance + fallDistanceMultiplier * player.getDeltaMovement().y());
//					System.out.println("Player's motion: " + player.getMotion().getY() + ", Player's fall reduction multiplier: " + fallDistanceMultiplier + ", Player's final fall distance: " + player.fallDistance);
				}

				int fireLevel = stats.getLevel(LivingArmorRegistrar.UPGRADE_FIRE_RESIST.get().getKey());
				if (fireLevel > 0)
				{
					boolean hasChanged = false;
					int fireCooldown = chestStack.getTag().getInt("fire_cooldown");
					if (fireCooldown > 0)
					{
						fireCooldown--;
						hasChanged = true;
					}

					if (hasChanged)
					{
						chestStack.getTag().putInt("fire_cooldown", fireCooldown);
					}
				}

				if (player.getRemainingFireTicks() > 0)
				{
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_FIRE_RESIST.get(), 1);
					if (fireLevel > 0)
					{
						boolean hasChanged = false;
						int fireCooldown = chestStack.getTag().getInt("fire_cooldown");

						if (player.getRemainingFireTicks() > 0 && fireCooldown <= 0)
						{
							fireCooldown = LivingArmorRegistrar.UPGRADE_FIRE_RESIST.get().getBonusValue("cooldown_time", fireLevel).intValue();
							player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, LivingArmorRegistrar.UPGRADE_FIRE_RESIST.get().getBonusValue("resist_duration", fireLevel).intValue(), 0, true, false));
							hasChanged = true;
						}

						if (hasChanged)
						{
							chestStack.getTag().putInt("fire_cooldown", fireCooldown);
						}
					}
				}

				int poisonLevel = stats.getLevel(LivingArmorRegistrar.UPGRADE_POISON_RESIST.get().getKey());
				if (player.hasEffect(MobEffects.POISON))
				{
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_POISON_RESIST.get(), 1);
				}
				if (poisonLevel > 0)
				{
					boolean hasChanged = false;
					int poisonCooldown = chestStack.getTag().getInt("poison_cooldown");
					if (poisonCooldown > 0)
					{
						poisonCooldown--;
						hasChanged = true;
					}

					if (player.hasEffect(MobEffects.POISON) && poisonCooldown <= 0 && LivingArmorRegistrar.UPGRADE_POISON_RESIST.get().getBonusValue("max_cure", poisonLevel).intValue() >= player.getEffect(MobEffects.POISON).getAmplifier())
					{
						poisonCooldown = LivingArmorRegistrar.UPGRADE_POISON_RESIST.get().getBonusValue("cooldown", poisonLevel).intValue();
						player.removeEffect(MobEffects.POISON);
						hasChanged = true;
					}

					if (hasChanged)
					{
						chestStack.getTag().putInt("poison_cooldown", poisonCooldown);
					}
				}

				int battleHungryLevel = stats.getLevel(LivingArmorRegistrar.DOWNGRADE_BATTLE_HUNGRY.get().getKey());
				if (battleHungryLevel > 0)
				{
					boolean hasChanged = false;
					int battleCooldown = chestStack.getTag().getInt("battle_cooldown");
					if (battleCooldown > 0)
					{
						battleCooldown--;
						hasChanged = true;
					}

					if (battleCooldown <= 0)
					{
						battleCooldown = 20;
						float exhaustionAdded = LivingArmorRegistrar.DOWNGRADE_BATTLE_HUNGRY.get().getBonusValue("exhaustion", battleHungryLevel).floatValue();
						player.causeFoodExhaustion(exhaustionAdded);
						hasChanged = true;
					}

					if (hasChanged)
					{
						chestStack.getTag().putInt("battle_cooldown", battleCooldown);
					}
				}

				int pastArmourDamage = chestStack.getTag().getInt("past_damage");
				int currentArmourDamage = chestStack.getDamageValue();
				if (pastArmourDamage > currentArmourDamage)
				{
//					System.out.println("Past damage: " + pastArmourDamage + ", current damage: " + currentArmourDamage);
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_REPAIR.get(), pastArmourDamage - currentArmourDamage);
				}

				if (currentArmourDamage != pastArmourDamage)
				{
					chestStack.getTag().putInt("past_damage", currentArmourDamage);
				}

				if (!player.level().isClientSide)
				{
					int repairingLevel = stats.getLevel(LivingArmorRegistrar.UPGRADE_REPAIR.get().getKey());
					if (repairingLevel > 0)
					{
						boolean hasChanged = false;
						int repairCooldown = chestStack.getTag().getInt("repair_cooldown");
						if (repairCooldown > 0)
						{
							repairCooldown--;
							hasChanged = true;
						}

						if (repairCooldown <= 0)
						{

							repairCooldown = LivingArmorRegistrar.UPGRADE_REPAIR.get().getBonusValue("interval", repairingLevel).intValue();
							hasChanged = true;
							EquipmentSlot randomSlot = EquipmentSlot.values()[2 + player.level().random.nextInt(4)];
							ItemStack repairStack = player.getItemBySlot(randomSlot);
							if (!repairStack.isEmpty())
							{
								if (repairStack.isDamageableItem() && repairStack.isDamaged())
								{
									int maxDurabilityRepaired = LivingArmorRegistrar.UPGRADE_REPAIR.get().getBonusValue("max", repairingLevel).intValue();
									int toRepair = Math.min(maxDurabilityRepaired, repairStack.getDamageValue());
									if (toRepair > 0)
									{
										repairStack.setDamageValue(repairStack.getDamageValue() - toRepair);
									}
								}
							}
						}

						if (hasChanged)
						{
							chestStack.getTag().putInt("repair_cooldown", repairCooldown);
						}
					}
				}
			}

//			if (percentIncrease > 0 && (player.isOnGround()) && (Math.abs(player.moveForward) > 0 || Math.abs(player.moveStrafing) > 0))
//			{
//				player.travel(new Vector3d(player.moveStrafing * percentIncrease, 0, player.moveForward * percentIncrease));
//			}

			posXMap.put(player.getUUID(), player.getX());
			posZMap.put(player.getUUID(), player.getZ());
		}
	}

	@SubscribeEvent
	public void onMiningSpeedCheck(PlayerEvent.BreakSpeed event)
	{
		Player player = event.getEntity();
		float speedModifier = 1;

		if (LivingUtil.hasFullSet(player))
		{
			LivingStats stats = LivingStats.fromPlayer(player, true);
			speedModifier *= 1 + LivingArmorRegistrar.UPGRADE_DIGGING.get().getBonusValue("speed_modifier", stats.getLevel(LivingArmorRegistrar.UPGRADE_DIGGING.get().getKey())).doubleValue();
			speedModifier *= 1 + LivingArmorRegistrar.DOWNGRADE_DIG_SLOWDOWN.get().getBonusValue("speed_modifier", stats.getLevel(LivingArmorRegistrar.DOWNGRADE_DIG_SLOWDOWN.get().getKey())).doubleValue();
		}

		event.setNewSpeed((speedModifier) * event.getNewSpeed());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBreakBlock(BlockEvent.BreakEvent event)
	{
		Player player = event.getPlayer();
		if (player != null)
		{
			if (LivingUtil.hasFullSet(player))
			{
				LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_DIGGING.get(), 1);
				LivingStats stats = LivingStats.fromPlayer(player);
				int mineTime = LivingArmorRegistrar.UPGRADE_DIGGING.get().getBonusValue("speed_time", stats.getLevel(LivingArmorRegistrar.UPGRADE_DIGGING.get().getKey())).intValue();
				if (mineTime > 0)
				{
					player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, mineTime, LivingArmorRegistrar.UPGRADE_DIGGING.get().getBonusValue("speed_level", stats.getLevel(LivingArmorRegistrar.UPGRADE_DIGGING.get().getKey())).intValue(), true, false));
				}
			}

			ItemStack heldStack = player.getMainHandItem();
			AnointmentHolder holder = AnointmentHolder.fromItemStack(heldStack);

			if (holder != null)
			{
				boolean hasAnointmentChanged = false;

				if (holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_SILK_TOUCH.get()) >= 1)
				{
					int bonusLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getMainHandItem());
					int exp = event.getState().getExpDrop(event.getLevel(), event.getLevel().getRandom(), event.getPos(), bonusLevel, holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_SILK_TOUCH.get()));
					event.setExpToDrop(exp);
				}

				int hiddenLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_HIDDEN_KNOWLEDGE.get());
				if (hiddenLevel > 0)
				{
					double expBonus = AnointmentRegistrar.ANOINTMENT_HIDDEN_KNOWLEDGE.get().getBonusValue("exp", hiddenLevel).doubleValue();

					if (event.getExpToDrop() > 0 && expBonus > 0)
					{
						int expAdded = (int) expBonus + (expBonus % 1 > event.getLevel().getRandom().nextDouble() ? 1
								: 0);
						event.setExpToDrop(event.getExpToDrop() + expAdded);

						if (holder.consumeAnointmentDurability(heldStack, EquipmentSlot.MAINHAND, AnointmentRegistrar.ANOINTMENT_HIDDEN_KNOWLEDGE.get(), player))
						{
							hasAnointmentChanged = true;
						}
					}
				}

				int repairLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_WEAPON_REPAIR.get());
				if (repairLevel > 0)
				{
					if (heldStack.isDamageableItem() && heldStack.isDamaged())
					{
						double expBonus = AnointmentRegistrar.ANOINTMENT_WEAPON_REPAIR.get().getBonusValue("exp", repairLevel).doubleValue();

						double repairRatio = heldStack.getXpRepairRatio();
						double durabilityBonus = Math.min(expBonus / repairRatio, heldStack.getDamageValue());
						int durabilityAdded = (int) durabilityBonus + (durabilityBonus % 1 > event.getLevel().getRandom().nextDouble()
								? 1
								: 0);
						if (durabilityAdded > 0)
							heldStack.setDamageValue(Math.max(0, heldStack.getDamageValue() - durabilityAdded));

						if (holder.consumeAnointmentDurability(heldStack, EquipmentSlot.MAINHAND, AnointmentRegistrar.ANOINTMENT_WEAPON_REPAIR.get(), player))
						{
							hasAnointmentChanged = true;
						}
					}
				}

				if (holder.consumeAnointmentDurabilityOnHarvest(heldStack, EquipmentSlot.MAINHAND, player) || hasAnointmentChanged)
					holder.toItemStack(heldStack);
			}
		}
	}

	@SubscribeEvent
	public void onJump(LivingJumpEvent event)
	{
		if (event.getEntity().hasEffect(BloodMagicPotions.GROUNDED.get()))
		{
			Vec3 motion = event.getEntity().getDeltaMovement();
			motion = motion.multiply(1, 0, 1);
			event.getEntity().setDeltaMovement(motion);
			return;
		}

		if (event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();

			if (LivingUtil.hasFullSet(player))
			{
				LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_JUMP.get(), 1);
				if (!player.isShiftKeyDown())
				{
					LivingStats stats = LivingStats.fromPlayer(player);
					double jumpModifier = LivingArmorRegistrar.UPGRADE_JUMP.get().getBonusValue("jump", stats.getLevel(LivingArmorRegistrar.UPGRADE_JUMP.get().getKey())).doubleValue();
					player.setDeltaMovement(player.getDeltaMovement().add(0, jumpModifier, 0));
				}
			}
		}
	}

	private static final Map<ItemStack, Double> rollMap = new HashMap<ItemStack, Double>();

	@SubscribeEvent
	public void onEntityUseTick(LivingEntityUseItemEvent.Tick event)
	{
		ItemStack stack = event.getItem();
		if (stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem)
		{
			AnointmentHolder holder = AnointmentHolder.fromItemStack(stack);
			if (holder == null)
			{
				return;
			}
			int quickDrawLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_QUICK_DRAW.get());
			if (quickDrawLevel > 0)
			{
				double amount = rollMap.getOrDefault(stack, 0D) + AnointmentRegistrar.ANOINTMENT_QUICK_DRAW.get().getBonusValue("speed", quickDrawLevel).doubleValue();
				if (amount >= 1)
				{
					int drawReduction = (int) amount;
					event.setDuration(event.getDuration() - drawReduction);
				} else
				{
					rollMap.put(stack, amount);
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityFinishUse(LivingEntityUseItemEvent.Stop event)
	{
		ItemStack stack = event.getItem();
		if (stack.getItem() instanceof CrossbowItem)
		{
			int i = stack.getUseDuration() - event.getDuration();
			float f = getCharge(i, stack);
			if (f < 0)
			{
				return;
			}
		}
		AnointmentHolder holder = AnointmentHolder.fromItemStack(stack);
		if (holder != null)
		{
			if (holder.consumeAnointmentDurabilityOnUseFinish(stack, EquipmentSlot.MAINHAND, event.getEntity()))
			{

				holder.toItemStack(stack);
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinEvent(EntityJoinLevelEvent event)
	{
		Entity owner = null;
		Entity entity = event.getEntity();
		if (entity instanceof Arrow)
			owner = ((Arrow) event.getEntity()).getOwner();
		else if (entity instanceof ThrowableProjectile)
			owner = ((ThrowableProjectile) entity).getOwner();

		if (owner instanceof Player)
		{
			Entity projectile = event.getEntity();
			Player player = (Player) owner;

			if (LivingUtil.hasFullSet(player))
			{
				LivingStats stats = LivingStats.fromPlayer(player, true);

				double arrowJiggle = LivingArmorRegistrar.DOWNGRADE_STORM_TROOPER.get().getBonusValue("inaccuracy", stats.getLevel(LivingArmorRegistrar.DOWNGRADE_STORM_TROOPER.get().getKey())).doubleValue();

				if (arrowJiggle > 0)
				{
					Vec3 motion = projectile.getDeltaMovement();
					float velocityModifier = (float) (arrowJiggle * Math.sqrt(motion.x * motion.x + motion.y * motion.y + motion.z * motion.z));

					Vec3 newMotion = motion.add(2 * (event.getLevel().random.nextDouble() - 0.5) * velocityModifier, 2 * (event.getLevel().random.nextDouble() - 0.5) * velocityModifier, 2 * (event.getLevel().random.nextDouble() - 0.5) * velocityModifier);

					projectile.setDeltaMovement(newMotion);
				}
			}
		}

		if (entity instanceof Arrow)
		{
			if (entity.tickCount <= 0)
			{
//				System.out.println("An arrow joined the world! Looking for the shooter...");
				Arrow arrowEntity = (Arrow) entity;
				Entity shooter = arrowEntity.getOwner();
				if (shooter instanceof Player)
				{
					Player playerShooter = (Player) shooter;

					for (InteractionHand hand : InteractionHand.values())
					{
						ItemStack heldStack = playerShooter.getItemInHand(hand);
						AnointmentHolder holder = AnointmentHolder.fromItemStack(heldStack);
						if (holder == null)
						{
							continue;
						}

						int powerLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_BOW_POWER.get());
						if (powerLevel > 0)
						{
							arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() * AnointmentRegistrar.ANOINTMENT_BOW_POWER.get().getBonusValue("damage", powerLevel).doubleValue());

//							System.out.println("Arrow damage is now: " + arrowEntity.getDamage());
						}

						int velocityLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_BOW_VELOCITY.get());
						if (velocityLevel > 0)
						{
							Vec3 motion = arrowEntity.getDeltaMovement();

							double multiplier = (float) AnointmentRegistrar.ANOINTMENT_BOW_VELOCITY.get().getBonusValue("multiplier", velocityLevel).doubleValue();

							arrowEntity.setDeltaMovement(motion.scale(multiplier));
							arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() / multiplier);
//
//							arrowEntity.shoot(f, f1, f2, (float) velocity, 0);
						}

//						int accuracyLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_BOW_VELOCITY.get());
//						if (accuracyLevel > 0)
//						{
//							Vector2f arrowPitchYaw = arrowEntity.getPitchYaw();
//							Vector2f playerPitchYaw = playerShooter.getPitchYaw();
//
//							float velocity = (float) arrowEntity.getMotion().length();
//
//							float accuracy = (float) AnointmentRegistrar.ANOINTMENT_BOW_VELOCITY.get().getBonusValue("accuracy", accuracyLevel).doubleValue();
//
//							float pitch = playerPitchYaw.x;
//							float yaw = playerPitchYaw.y;
//							float perfectX = -MathHelper.sin(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
//							float perfectY = -MathHelper.sin((pitch) * ((float) Math.PI / 180F));
//							float perfectZ = MathHelper.cos(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
//
//							double difX = perfectX - arrowEntity.getMotion().getX() / velocity;
//							double difY = perfectY - arrowEntity.getMotion().getY() / velocity;
//							double difZ = perfectZ - arrowEntity.getMotion().getZ() / velocity;
//
//							Vector3d newMotion = new Vector3d(perfectX - (1 - accuracy) * difX, perfectY - (1 - accuracy) * difY, perfectZ - (1 - accuracy) * difZ).scale(velocity);
//
//							arrowEntity.setMotion(newMotion);
////
////							arrowEntity.shoot(f, f1, f2, (float) velocity, 0);
//						}

						break;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onCheckLooting(LootingLevelEvent event)
	{
		DamageSource source = event.getDamageSource();
		if (source == null)
		{
			return;
		}
		Entity entity = source.getEntity();
		if (entity instanceof Player)
		{
			ItemStack heldStack = ((Player) entity).getMainHandItem();
			AnointmentHolder holder = AnointmentHolder.fromItemStack(heldStack);
			if (holder == null)
			{
				return;
			}

			int plunderLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_LOOTING.get());
			if (plunderLevel > 0)
			{
				event.setLootingLevel(event.getLootingLevel() + plunderLevel);
			}
		}
	}

	public static Map<UUID, Integer> curiosLevelMap = new HashMap<>();

	@SubscribeEvent
	public void onLivingEquipmentChange(LivingEquipmentChangeEvent event)
	{
		if (BloodMagic.curiosLoaded)
		{ // Without Curios, there is nothing this cares about.
			if (event.getFrom().getItem() instanceof ILivingContainer || event.getTo().getItem() instanceof ILivingContainer)
			{ // Armor change involves Living Armor
				LivingEntity entity = event.getEntity();
				if (entity instanceof Player)
				{ // is a player
					Player player = (Player) entity;
					UUID uuid = player.getUUID();
					if (LivingUtil.hasFullSet(player))
					{ // Player has a full set
						LivingStats stats = LivingStats.fromPlayer(player);
						if (stats != null)
						{
							int curiosLevel = stats.getLevel(LivingArmorRegistrar.UPGRADE_CURIOS_SOCKET.get().getKey());
							if (curiosLevelMap.getOrDefault(uuid, 0) != curiosLevel)
							{ // Cache level does not match new level
								curiosLevelMap.put(uuid, BloodMagic.curiosCompat.recalculateCuriosSlots(player));
							}
						} else if (curiosLevelMap.getOrDefault(uuid, 0) != 0)
						{
							curiosLevelMap.put(uuid, 0);
						}

					} else if (curiosLevelMap.getOrDefault(uuid, 0) != 0)
					{ // cache has an upgrade that needs to be removed
						curiosLevelMap.put(uuid, BloodMagic.curiosCompat.recalculateCuriosSlots(player));
					}
				}
			}
		}
	}

	private static float getCharge(int useTime, ItemStack stack)
	{
		float f = (float) useTime / (float) getChargeTime(stack);
		if (f > 1.0F)
		{
			f = 1.0F;
		}

		return f;
	}

	public static int getChargeTime(ItemStack stack)
	{
		int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
		return i == 0 ? 25 : 25 - 5 * i;
	}

	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract event){
		if (event.getTarget() instanceof Piglin && event.getItemStack().is(Items.GOLD_INGOT))
		{
			Player player = event.getEntity();
			if (LivingUtil.hasFullSet(player))
			{
				LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_GILDED.get(), 1);
			}
		}
	}
}
