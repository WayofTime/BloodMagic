package wayoftime.bloodmagic.util.handler.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BlockToolInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.common.item.IBloodOrb;
import wayoftime.bloodmagic.common.item.ItemExperienceBook;
import wayoftime.bloodmagic.core.AnointmentRegistrar;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.event.ItemBindEvent;
import wayoftime.bloodmagic.event.SacrificeKnifeUsedEvent;
import wayoftime.bloodmagic.network.DemonAuraClientPacket;
import wayoftime.bloodmagic.potion.BMPotionUtils;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
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
	// Called when an entity is set to be hurt. Called before vanilla armour
	// calculations.
	public void onLivingHurt(LivingHurtEvent event)
	{
		Entity sourceEntity = event.getSource().getTrueSource();
		LivingEntity living = event.getEntityLiving();

		if (sourceEntity instanceof PlayerEntity)
		{
			PlayerEntity sourcePlayer = (PlayerEntity) sourceEntity;
			if (LivingUtil.hasFullSet(sourcePlayer))
			{
				ItemStack mainWeapon = sourcePlayer.getActiveItemStack();
				double additionalDamage = LivingUtil.getAdditionalDamage(sourcePlayer, mainWeapon, living, event.getAmount());
				event.setAmount((float) (event.getAmount() + additionalDamage));
			}

			ItemStack heldStack = sourcePlayer.getHeldItemMainhand();
			AnointmentHolder holder = AnointmentHolder.fromItemStack(heldStack);

			if (holder != null)
			{
				double additionalDamage = holder.getAdditionalDamage(sourcePlayer, heldStack, event.getAmount(), living);

				event.setAmount((float) (event.getAmount() + additionalDamage));
			}
		}

		if (living instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) living;
			if (LivingUtil.hasFullSet(player))
			{
				event.setAmount((float) LivingUtil.getDamageReceivedForArmour(player, event.getSource(), event.getAmount()));
			}
		}
	}

	@SubscribeEvent
	// Called after armour calculations (including LivingHurtEvent) are parsed.
	// Damage that the player should receive after armour/absorption hearts.
	public void onLivingDamage(LivingDamageEvent event)
	{
		Entity sourceEntity = event.getSource().getTrueSource();
		LivingEntity living = event.getEntityLiving();

		if (sourceEntity instanceof PlayerEntity)
		{
			PlayerEntity sourcePlayer = (PlayerEntity) sourceEntity;
			if (LivingUtil.hasFullSet(sourcePlayer))
			{
				if (sourcePlayer.isSprinting())
				{
					LivingUtil.applyNewExperience(sourcePlayer, LivingArmorRegistrar.UPGRADE_SPRINT_ATTACK.get(), event.getAmount());
				}
			}

			ItemStack heldStack = sourcePlayer.getHeldItemMainhand();
			AnointmentHolder holder = AnointmentHolder.fromItemStack(heldStack);
//			AnointmentHolder holder = AnointmentHolder.fromPlayer(sourcePlayer, Hand.MAIN_HAND);

//			System.out.println("Checking consumption. Holder is: " + holder);
			if (holder != null && holder.consumeAnointmentDurabilityOnHit(heldStack, EquipmentSlotType.MAINHAND))
			{
				holder.toItemStack(heldStack);
			}
		}

		if (living instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) living;
			if (LivingUtil.hasFullSet(player))
			{
				if (event.getSource().isProjectile())
				{
//					LivingStats stats = LivingStats.fromPlayer(player);
//					stats.addExperience(LivingArmorRegistrar.TEST_UPGRADE.get().getKey(), 10);
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_ARROW_PROTECT.get(), event.getAmount());
				} else
				{
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_PHYSICAL_PROTECT.get(), event.getAmount());
				}

				if (event.getSource() == DamageSource.FALL)
				{
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_FALL_PROTECT.get(), event.getAmount());
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onExperiencePickupHighest(PlayerXpEvent.PickupXp event)
	{
		LivingEntity living = event.getEntityLiving();
		if (living instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) living;
			if (LivingUtil.hasFullSet(player))
			{
				LivingStats stats = LivingStats.fromPlayer(player, true);
				double expModifier = 1 + LivingArmorRegistrar.UPGRADE_EXPERIENCE.get().getBonusValue("exp", stats.getLevel(LivingArmorRegistrar.UPGRADE_EXPERIENCE.get().getKey())).doubleValue();
//				System.out.println("Experience modifier: " + expModifier);

				int xp = event.getOrb().xpValue;

				event.getOrb().xpValue = ((int) Math.floor(xp * expModifier) + (player.world.rand.nextDouble() < (xp * expModifier) % 1
						? 1
						: 0));

				LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_EXPERIENCE.get(), event.getOrb().getXpValue());
			}
		}
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

	// Handles sending the client the Demon Will Aura updates
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if (!event.getEntityLiving().getEntityWorld().isRemote)
		{
			LivingEntity entity = event.getEntityLiving();
			if (entity.isPotionActive(BloodMagicPotions.PLANT_LEECH))
			{
				int amplifier = entity.getActivePotionEffect(BloodMagicPotions.PLANT_LEECH).getAmplifier();
				int timeRemaining = entity.getActivePotionEffect(BloodMagicPotions.PLANT_LEECH).getDuration();
				if (timeRemaining % 10 == 0)
				{
					BMPotionUtils.damageMobAndGrowSurroundingPlants(entity, 2 + amplifier, 1, 0.5 * 3 / (amplifier + 3), 25 * (1 + amplifier));
				}
			}
		}
	}

	@SubscribeEvent
	public void onHeal(LivingHealEvent event)
	{
		LivingEntity living = event.getEntityLiving();
		if (living instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) living;
			if (LivingUtil.hasFullSet(player))
			{
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

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
	{
		if (event.getEntity().world.isRemote)
		{
			if (event.getEntityLiving() instanceof PlayerEntity)
			{
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();
				if (LivingUtil.hasFullSet(player))
				{
					LivingStats stats = LivingStats.fromPlayer(player, true);
					if (!player.isOnGround() && player.getMotion().getY() < 0)
					{

						int jumpLevel = stats.getLevel(LivingArmorRegistrar.UPGRADE_JUMP.get().getKey());
						double fallDistanceMultiplier = LivingArmorRegistrar.UPGRADE_JUMP.get().getBonusValue("fall", jumpLevel).doubleValue();
						player.fallDistance = (float) Math.max(0, player.fallDistance + fallDistanceMultiplier * player.getMotion().getY());
//				System.out.println("Player's motion: " + player.getMotion().getY() + ", Player's fall reduction multiplier: " + fallDistanceMultiplier + ", Player's final fall distance: " + player.fallDistance);
					}
					return;
				}
			}
		}
		if (event.getEntityLiving() instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			float percentIncrease = 0;

//			System.out.println("Player's motion: " + player.getMotion().getY() + ", Player's final fall distance: " + player.fallDistance);

			if (LivingUtil.hasFullSet(player))
			{
				LivingStats stats = LivingStats.fromPlayer(player, true);
				ItemStack chestStack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
//				percentIncrease += LivingArmorRegistrar.UPGRADE_SPEED.get().getBonusValue("speed_modifier", stats.getLevel(LivingArmorRegistrar.UPGRADE_SPEED.get().getKey())).doubleValue();
				if (player.isSprinting())
				{
					int speedTime = LivingArmorRegistrar.UPGRADE_SPEED.get().getBonusValue("speed_time", stats.getLevel(LivingArmorRegistrar.UPGRADE_SPEED.get().getKey())).intValue();
					if (speedTime > 0)
					{
						int speedLevel = LivingArmorRegistrar.UPGRADE_SPEED.get().getBonusValue("speed_level", stats.getLevel(LivingArmorRegistrar.UPGRADE_SPEED.get().getKey())).intValue();
						player.addPotionEffect(new EffectInstance(Effects.SPEED, speedTime, speedLevel, true, false));
					}
				}

				double distance = 0;

				if (posXMap.containsKey(player.getUniqueID()))
				{
					distance = Math.sqrt((player.getPosX() - posXMap.get(player.getUniqueID())) * (player.getPosX() - posXMap.get(player.getUniqueID())) + (player.getPosZ() - posZMap.get(player.getUniqueID())) * (player.getPosZ() - posZMap.get(player.getUniqueID())));
				}

				int currentFood = player.getFoodStats().getFoodLevel();

				if (foodMap.getOrDefault(player.getUniqueID(), 19) < currentFood)
				{
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_KNOCKBACK_RESIST.get(), currentFood - foodMap.getOrDefault(player.getUniqueID(), 19));
				}

				foodMap.put(player.getUniqueID(), currentFood);

//				System.out.println("Distance travelled: " + distance);
				if (player.isOnGround() && distance > 0 && distance < 50)
				{
					distance *= (1 + percentIncrease);
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_SPEED.get(), distance);
				}

				if (!player.isOnGround() && player.getMotion().getY() < 0)
				{

					int jumpLevel = stats.getLevel(LivingArmorRegistrar.UPGRADE_JUMP.get().getKey());
					double fallDistanceMultiplier = LivingArmorRegistrar.UPGRADE_JUMP.get().getBonusValue("fall", jumpLevel).doubleValue();
					player.fallDistance = (float) Math.max(0, player.fallDistance + fallDistanceMultiplier * player.getMotion().getY());
//					System.out.println("Player's motion: " + player.getMotion().getY() + ", Player's fall reduction multiplier: " + fallDistanceMultiplier + ", Player's final fall distance: " + player.fallDistance);
				}

				if (player.getFireTimer() > 0)
				{
					LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_FIRE_RESIST.get(), 1);
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

						if (player.getFireTimer() > 0 && fireCooldown <= 0)
						{
							fireCooldown = LivingArmorRegistrar.UPGRADE_FIRE_RESIST.get().getBonusValue("cooldown_time", fireLevel).intValue();
							player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, LivingArmorRegistrar.UPGRADE_FIRE_RESIST.get().getBonusValue("resist_duration", fireLevel).intValue(), 0, true, false));
							hasChanged = true;
						}

						if (hasChanged)
						{
							chestStack.getTag().putInt("fire_cooldown", fireCooldown);
						}
					}
				}

				int poisonLevel = stats.getLevel(LivingArmorRegistrar.UPGRADE_POISON_RESIST.get().getKey());
				if (player.isPotionActive(Effects.POISON))
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

					if (player.isPotionActive(Effects.POISON) && poisonCooldown <= 0 && LivingArmorRegistrar.UPGRADE_POISON_RESIST.get().getBonusValue("max_cure", poisonLevel).intValue() >= player.getActivePotionEffect(Effects.POISON).getAmplifier())
					{
						poisonCooldown = LivingArmorRegistrar.UPGRADE_POISON_RESIST.get().getBonusValue("cooldown", poisonLevel).intValue();
						player.removePotionEffect(Effects.POISON);
						hasChanged = true;
					}

					if (hasChanged)
					{
						chestStack.getTag().putInt("poison_cooldown", poisonCooldown);
					}
				}
			}

//			if (percentIncrease > 0 && (player.isOnGround()) && (Math.abs(player.moveForward) > 0 || Math.abs(player.moveStrafing) > 0))
//			{
//				player.travel(new Vector3d(player.moveStrafing * percentIncrease, 0, player.moveForward * percentIncrease));
//			}

			posXMap.put(player.getUniqueID(), player.getPosX());
			posZMap.put(player.getUniqueID(), player.getPosZ());
		}
	}

	@SubscribeEvent
	public void onMiningSpeedCheck(PlayerEvent.BreakSpeed event)
	{
		PlayerEntity player = event.getPlayer();
		float percentIncrease = 0;

		if (LivingUtil.hasFullSet(player))
		{
			LivingStats stats = LivingStats.fromPlayer(player, true);
			percentIncrease += LivingArmorRegistrar.UPGRADE_DIGGING.get().getBonusValue("speed_modifier", stats.getLevel(LivingArmorRegistrar.UPGRADE_DIGGING.get().getKey())).doubleValue();
		}

		event.setNewSpeed((1 + percentIncrease) * event.getNewSpeed());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onBreakBlock(BlockEvent.BreakEvent event)
	{
		PlayerEntity player = event.getPlayer();
		if (player != null)
		{
			if (LivingUtil.hasFullSet(player))
			{
				LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_DIGGING.get(), 1);
				LivingStats stats = LivingStats.fromPlayer(player);
				int mineTime = LivingArmorRegistrar.UPGRADE_DIGGING.get().getBonusValue("speed_time", stats.getLevel(LivingArmorRegistrar.UPGRADE_DIGGING.get().getKey())).intValue();
				if (mineTime > 0)
				{
					player.addPotionEffect(new EffectInstance(Effects.HASTE, mineTime, LivingArmorRegistrar.UPGRADE_DIGGING.get().getBonusValue("speed_level", stats.getLevel(LivingArmorRegistrar.UPGRADE_DIGGING.get().getKey())).intValue(), true, false));
				}
			}

			ItemStack heldStack = player.getHeldItemMainhand();
			AnointmentHolder holder = AnointmentHolder.fromItemStack(heldStack);

			if (holder != null)
			{
				if (holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_SILK_TOUCH.get()) >= 1)
				{
					int bonusLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
					int exp = event.getState().getExpDrop(event.getWorld(), event.getPos(), bonusLevel, holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_SILK_TOUCH.get()));
					event.setExpToDrop(exp);
				}

				int hiddenLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_HIDDEN_KNOWLEDGE.get());
				if (hiddenLevel > 0)
				{
					double expBonus = AnointmentRegistrar.ANOINTMENT_HIDDEN_KNOWLEDGE.get().getBonusValue("exp", hiddenLevel).doubleValue();
					int expAdded = (int) expBonus + (expBonus % 1 > event.getWorld().getRandom().nextDouble() ? 1 : 0);
					event.setExpToDrop(event.getExpToDrop() + expAdded);
				}

				if (holder.consumeAnointmentDurabilityOnHarvest(heldStack, EquipmentSlotType.MAINHAND))
					holder.toItemStack(heldStack);
			}
		}
	}

	@SubscribeEvent
	public void onJump(LivingJumpEvent event)
	{
		if (event.getEntityLiving() instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			if (LivingUtil.hasFullSet(player))
			{
				LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_JUMP.get(), 1);
				if (!player.isSneaking())
				{
					LivingStats stats = LivingStats.fromPlayer(player);
					double jumpModifier = LivingArmorRegistrar.UPGRADE_JUMP.get().getBonusValue("jump", stats.getLevel(LivingArmorRegistrar.UPGRADE_JUMP.get().getKey())).doubleValue();
					player.setMotion(player.getMotion().add(0, jumpModifier, 0));
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
			if (holder.consumeAnointmentDurabilityOnUseFinish(stack, EquipmentSlotType.MAINHAND))
			{

				holder.toItemStack(stack);
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinEvent(EntityJoinWorldEvent event)
	{
		Entity entity = event.getEntity();
		if (entity instanceof ArrowEntity)
		{
			if (entity.ticksExisted <= 0)
			{
//				System.out.println("An arrow joined the world! Looking for the shooter...");
				ArrowEntity arrowEntity = (ArrowEntity) entity;
				Entity shooter = arrowEntity.func_234616_v_();
				if (shooter instanceof PlayerEntity)
				{
					PlayerEntity playerShooter = (PlayerEntity) shooter;
					for (Hand hand : Hand.values())
					{
						ItemStack heldStack = playerShooter.getHeldItem(hand);
						AnointmentHolder holder = AnointmentHolder.fromItemStack(heldStack);
						if (holder == null)
						{
							continue;
						}

						int powerLevel = holder.getAnointmentLevel(AnointmentRegistrar.ANOINTMENT_BOW_POWER.get());
						if (powerLevel > 0)
						{
							arrowEntity.setDamage(arrowEntity.getDamage() * AnointmentRegistrar.ANOINTMENT_BOW_POWER.get().getBonusValue("damage", powerLevel).doubleValue());

//							System.out.println("Arrow damage is now: " + arrowEntity.getDamage());
						}

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
		Entity entity = source.getTrueSource();
		if (entity instanceof PlayerEntity)
		{
			ItemStack heldStack = ((PlayerEntity) entity).getHeldItemMainhand();
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
		int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
		return i == 0 ? 25 : 25 - 5 * i;
	}
}
