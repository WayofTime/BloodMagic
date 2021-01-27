package wayoftime.bloodmagic.common.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWill;
import wayoftime.bloodmagic.api.compat.IDemonWillWeapon;
import wayoftime.bloodmagic.api.compat.IMultiWillTool;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

public class ItemSentientScythe extends HoeItem implements IDemonWillWeapon, IMultiWillTool
{
	public static int[] soulBracket = new int[] { 16, 60, 200, 400, 1000 };
	public static double[] defaultDamageAdded = new double[] { 0.5, 1, 1.5, 2, 2.5 };
	public static double[] destructiveDamageAdded = new double[] { 1, 1.5, 2, 2.5, 3 };
	public static double[] vengefulDamageAdded = new double[] { 0, 0.5, 1, 1.5, 2 };
	public static double[] steadfastDamageAdded = new double[] { 0, 0.5, 1, 1.5, 2 };
	public static double[] defaultDigSpeedAdded = new double[] { 1, 1.5, 2, 3, 4 };
	public static double[] soulDrainPerSwing = new double[] { 0.05, 0.1, 0.2, 0.4, 0.75 };
	public static double[] soulDrop = new double[] { 2, 4, 7, 10, 13 };
	public static double[] staticDrop = new double[] { 1, 1, 2, 3, 3 };

	public static double[] healthBonus = new double[] { 0, 0, 0, 0, 0 }; // TODO: Think of implementing this later
	public static double[] vengefulAttackSpeed = new double[] { -3, -2.8, -2.7, -2.6, -2.5 };
	public static double[] destructiveAttackSpeed = new double[] { -3.1, -3.1, -3.2, -3.3, -3.3 };

	public static int[] absorptionTime = new int[] { 200, 300, 400, 500, 600 };

	public static double maxAbsorptionHearts = 10;

	public static int[] poisonTime = new int[] { 25, 50, 60, 80, 100 };
	public static int[] poisonLevel = new int[] { 0, 0, 0, 1, 1 };

	public static double[] movementSpeed = new double[] { 0.05, 0.1, 0.15, 0.2, 0.25 };

	public final static double baseAttackDamage = 4;
	public final static double baseAttackSpeed = -3;
	private static Map<UUID, Boolean> hitMap = new HashMap<UUID, Boolean>();

	public ItemSentientScythe()
	{
		super(BMItemTier.SENTIENT, (int) baseAttackDamage, (float) baseAttackSpeed, new Item.Properties().maxDamage(520).group(BloodMagic.TAB));
//		super(RegistrarBloodMagicItems.SOUL_TOOL_MATERIAL, 8.0F, 3.1F);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return BloodMagicTags.CRYSTAL_DEMON.contains(repair.getItem()) || super.getIsRepairable(toRepair, repair);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
		float value = super.getDestroySpeed(stack, state);
		if (value > 1)
		{
			return (float) (value + getDigSpeedOfSword(stack));
		} else
		{
			return value;
		}
	}

	public void recalculatePowers(ItemStack stack, World world, PlayerEntity player)
	{
		EnumDemonWillType type = PlayerDemonWillHandler.getLargestWillType(player);
		double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);
		this.setCurrentType(stack, soulsRemaining > 0 ? type : EnumDemonWillType.DEFAULT);
		int level = getLevel(stack, soulsRemaining);

		double drain = level >= 0 ? soulDrainPerSwing[level] : 0;
		double extraDamage = getExtraDamage(type, level);

		setDrainOfActivatedSword(stack, drain);
		setDamageOfActivatedSword(stack, baseAttackDamage + extraDamage);
		setStaticDropOfActivatedSword(stack, level >= 0 ? staticDrop[level] : 1);
		setDropOfActivatedSword(stack, level >= 0 ? soulDrop[level] : 0);
		setAttackSpeedOfSword(stack, level >= 0 ? getAttackSpeed(type, level) : baseAttackSpeed);
		setHealthBonusOfSword(stack, level >= 0 ? getHealthBonus(type, level) : 0);
		setSpeedOfSword(stack, level >= 0 ? getMovementSpeed(type, level) : 0);
		setDigSpeedOfSword(stack, level >= 0 ? getDigSpeed(type, level) : 0);
	}

	public double getExtraDamage(EnumDemonWillType type, int willBracket)
	{
		if (willBracket < 0)
		{
			return 0;
		}

		switch (type)
		{
		case CORROSIVE:
		case DEFAULT:
			return defaultDamageAdded[willBracket];
		case DESTRUCTIVE:
			return destructiveDamageAdded[willBracket];
		case VENGEFUL:
			return vengefulDamageAdded[willBracket];
		case STEADFAST:
			return steadfastDamageAdded[willBracket];
		}

		return 0;
	}

	public double getAttackSpeed(EnumDemonWillType type, int willBracket)
	{
		switch (type)
		{
		case VENGEFUL:
			return vengefulAttackSpeed[willBracket];
		case DESTRUCTIVE:
			return destructiveAttackSpeed[willBracket];
		default:
			return -2.9;
		}
	}

	public double getHealthBonus(EnumDemonWillType type, int willBracket)
	{
		switch (type)
		{
		case STEADFAST:
			return healthBonus[willBracket];
		default:
			return 0;
		}
	}

	public double getMovementSpeed(EnumDemonWillType type, int willBracket)
	{
		switch (type)
		{
		case VENGEFUL:
			return movementSpeed[willBracket];
		default:
			return 0;
		}
	}

	public double getDigSpeed(EnumDemonWillType type, int willBracket)
	{
		switch (type)
		{
		case VENGEFUL:
//            return movementSpeed[willBracket];
		default:
			return defaultDigSpeedAdded[willBracket];
		}
	}

//	public void onLeftClickAir(ItemStack stack, LivingEntity attacker)
//	{
//		if (attacker instanceof PlayerEntity)
//		{
//			PlayerEntity attackerPlayer = (PlayerEntity) attacker;
//			this.recalculatePowers(stack, attackerPlayer.getEntityWorld(), attackerPlayer);
//			EnumDemonWillType type = this.getCurrentType(stack);
//			double will = PlayerDemonWillHandler.getTotalDemonWill(type, attackerPlayer);
//			int willBracket = this.getLevel(stack, will);
//
//			this.recalculatePowers(stack, attacker.world, attackerPlayer);
//
////			applyEffectToEntity(type, willBracket, target, attackerPlayer);
//
//			attackEntitiesInAreaExcludingEntity(stack, attackerPlayer, type, willBracket);
//		}
//	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity attacker)
	{
		if (attacker instanceof PlayerEntity)
		{
			PlayerEntity attackerPlayer = (PlayerEntity) attacker;
			this.recalculatePowers(stack, attackerPlayer.getEntityWorld(), attackerPlayer);
			EnumDemonWillType type = this.getCurrentType(stack);
			double will = PlayerDemonWillHandler.getTotalDemonWill(type, attackerPlayer);
			int willBracket = this.getLevel(stack, will);

			this.recalculatePowers(stack, attacker.world, attackerPlayer);

//			applyEffectToEntity(type, willBracket, target, attackerPlayer);

			attackEntitiesInAreaExcludingEntity(stack, attackerPlayer, type, willBracket, null);
		}
		return false;
	}

	public void attackEntitiesInAreaExcludingEntity(ItemStack stack, PlayerEntity attacker, EnumDemonWillType type, int willBracket, LivingEntity attackedEntity)
	{
//		System.out.println("Is client: " + attacker.world.isRemote);
		double verticalRange = 2;
		double horizontalRange = 2;
		double range = 2;

		AxisAlignedBB aabb = null;
		List<Entity> list = null;

		if (attackedEntity != null)
		{
			aabb = attackedEntity.getBoundingBox().expand(horizontalRange, verticalRange, horizontalRange);
			list = attacker.world.getEntitiesInAABBexcluding(attackedEntity, aabb, EntityPredicates.IS_ALIVE);
		} else
		{
			Vector3d eyeVec = attacker.getEyePosition(1).add(attacker.getLookVec().scale(range));
			aabb = new AxisAlignedBB(eyeVec.getX() - horizontalRange, eyeVec.getY() - verticalRange, eyeVec.getZ() - horizontalRange, eyeVec.getX() + horizontalRange, eyeVec.getY() + verticalRange, eyeVec.getZ() + horizontalRange);
//			List<Entity> list = attacker.world.getEntitiesInAABBexcluding(attacker, aabb, EntityPredicates.IS_ALIVE);
			list = attacker.world.getEntitiesInAABBexcluding(attacker, aabb, EntityPredicates.IS_ALIVE);
		}

		list = attacker.world.getEntitiesWithinAABB(LivingEntity.class, aabb);

		// TODO: check if we actually hit something first, 'kay?
		float f = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);

		float f2 = attacker.getCooledAttackStrength(0.5F);
//		float f2 = 1;
		f = f * (0.2F + f2 * f2 * 0.8F);
		attacker.resetCooldown();
//		f *= 5;

		boolean flag1 = false;
		int i = 0;
		i = i + EnchantmentHelper.getKnockbackModifier(attacker);
		boolean flag = f2 > 0.9F;

		if (attacker.isSprinting() && flag)
		{
			attacker.world.playSound((PlayerEntity) null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, attacker.getSoundCategory(), 1.0F, 1.0F);
			++i;
			flag1 = true;
		}

		boolean didHit = false;
		boolean strongAttackHit = false;
		boolean weakAttackHit = false;
		boolean noDamageHit = false;

		for (Entity targetEntity : list)
		{
			if (targetEntity.equals(attacker))
			{
				continue;
			}

			if (targetEntity.canBeAttackedWithItem())
			{
				if (!targetEntity.hitByEntity(attacker))
				{
					float f1;
					if (targetEntity instanceof LivingEntity)
					{
						f1 = EnchantmentHelper.getModifierForCreature(stack, ((LivingEntity) targetEntity).getCreatureAttribute());
					} else
					{
						f1 = EnchantmentHelper.getModifierForCreature(stack, CreatureAttribute.UNDEFINED);
					}

					f1 = f1 * f2;

					if (f > 0.0F || f1 > 0.0F)
					{
						boolean flag2 = flag && attacker.fallDistance > 0.0F && !attacker.isOnGround() && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(Effects.BLINDNESS) && !attacker.isPassenger() && targetEntity instanceof LivingEntity;
						flag2 = flag2 && !attacker.isSprinting();
						net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(attacker, targetEntity, flag2, flag2
								? 1.5F
								: 1.0F);
						flag2 = hitResult != null;
						if (flag2)
						{
							f *= hitResult.getDamageModifier();
						}

						f = f + f1;
						boolean flag3 = false;
						double d0 = (double) (attacker.distanceWalkedModified - attacker.prevDistanceWalkedModified);

						float f4 = 0.0F;
						boolean flag4 = false;
						int j = EnchantmentHelper.getFireAspectModifier(attacker);
						if (targetEntity instanceof LivingEntity)
						{
							f4 = ((LivingEntity) targetEntity).getHealth();
							if (j > 0 && !targetEntity.isBurning())
							{
								flag4 = true;
								targetEntity.setFire(1);
							}
						}

						Vector3d vector3d = targetEntity.getMotion();
						boolean flag5 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(attacker), f);

						if (flag5)
						{
							didHit = true;
							if (i > 0)
							{
								if (targetEntity instanceof LivingEntity)
								{
									((LivingEntity) targetEntity).applyKnockback((float) i * 0.5F, (double) MathHelper.sin(attacker.rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(attacker.rotationYaw * ((float) Math.PI / 180F))));
								} else
								{
									targetEntity.addVelocity((double) (-MathHelper.sin(attacker.rotationYaw * ((float) Math.PI / 180F)) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(attacker.rotationYaw * ((float) Math.PI / 180F)) * (float) i * 0.5F));
								}

							}

//							if (flag3)
//							{
//								float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(attacker) * f;
//
//								for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, targetEntity.getBoundingBox().grow(1.0D, 0.25D, 1.0D)))
//								{
//									if (livingentity != this && livingentity != targetEntity && !this.isOnSameTeam(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity) livingentity).hasMarker()) && this.getDistanceSq(livingentity) < 9.0D)
//									{
//										livingentity.applyKnockback(0.4F, (double) MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F))));
//										livingentity.attackEntityFrom(DamageSource.causePlayerDamage(this), f3);
//									}
//								}
//
//								this.world.playSound((PlayerEntity) null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
//								this.spawnSweepParticles();
//							}

							attacker.spawnSweepParticles();

							if (targetEntity instanceof ServerPlayerEntity && targetEntity.velocityChanged)
							{
								((ServerPlayerEntity) targetEntity).connection.sendPacket(new SEntityVelocityPacket(targetEntity));
								targetEntity.velocityChanged = false;
								targetEntity.setMotion(vector3d);
							}

							if (flag2)
							{
								attacker.onCriticalHit(targetEntity);
							}

							if (!flag2 && !flag3)
							{
								if (flag)
								{
									strongAttackHit = true;
								} else
								{
									weakAttackHit = true;
								}
							}

							if (f1 > 0.0F)
							{
								attacker.onEnchantmentCritical(targetEntity);
							}

							attacker.setLastAttackedEntity(targetEntity);
							if (targetEntity instanceof LivingEntity)
							{
								EnchantmentHelper.applyThornEnchantments((LivingEntity) targetEntity, attacker);
							}

							EnchantmentHelper.applyArthropodEnchantments(attacker, targetEntity);
							ItemStack itemstack1 = stack;
							Entity entity = targetEntity;
//							if (targetEntity instanceof EnderDragonPartEntity)
//							{
//								entity = ((EnderDragonPartEntity) targetEntity).dragon;
//							}

							if (!attacker.world.isRemote && !itemstack1.isEmpty() && entity instanceof LivingEntity)
							{
								ItemStack copy = itemstack1.copy();
								itemstack1.hitEntity((LivingEntity) entity, attacker);
								if (itemstack1.isEmpty())
								{
									net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(attacker, copy, Hand.MAIN_HAND);
									attacker.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
								}
							}

							if (targetEntity instanceof LivingEntity)
							{
								float f5 = f4 - ((LivingEntity) targetEntity).getHealth();
								attacker.addStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
								if (j > 0)
								{
									targetEntity.setFire(j * 4);
								}

								if (attacker.world instanceof ServerWorld && f5 > 2.0F)
								{
									int k = (int) ((double) f5 * 0.5D);
									((ServerWorld) attacker.world).spawnParticle(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getPosX(), targetEntity.getPosYHeight(0.5D), targetEntity.getPosZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
								}

								applyEffectToEntity(type, willBracket, (LivingEntity) targetEntity, attacker);
							}

						} else
						{
							noDamageHit = true;
							if (flag4)
							{
								targetEntity.extinguish();
							}
						}
					}

				}
			}
		}

		if (didHit)
		{
			if (flag1)
			{
				attacker.setMotion(attacker.getMotion().mul(0.6D, 1.0D, 0.6D));
				attacker.setSprinting(false);
			}

			attacker.addExhaustion(0.1F);

			attacker.world.playSound((PlayerEntity) null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, attacker.getSoundCategory(), 1.0F, 1.0F);
		}

		if (noDamageHit)
		{
			attacker.world.playSound((PlayerEntity) null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, attacker.getSoundCategory(), 1.0F, 1.0F);
		}

		if (strongAttackHit)
		{
			attacker.world.playSound((PlayerEntity) null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, attacker.getSoundCategory(), 1.0F, 1.0F);
		}
		if (weakAttackHit)
		{
			attacker.world.playSound((PlayerEntity) null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, attacker.getSoundCategory(), 1.0F, 1.0F);
		}
	}

	public void applyEffectToEntity(EnumDemonWillType type, int willBracket, LivingEntity target, PlayerEntity attacker)
	{
		switch (type)
		{
		case CORROSIVE:
			target.addPotionEffect(new EffectInstance(Effects.WITHER, poisonTime[willBracket], poisonLevel[willBracket]));
			break;
		case DEFAULT:
			break;
		case DESTRUCTIVE:
			break;
		case STEADFAST:
			if (!target.isAlive())
			{
				float absorption = attacker.getAbsorptionAmount();
				attacker.addPotionEffect(new EffectInstance(Effects.ABSORPTION, absorptionTime[willBracket], 127, true, false));
				attacker.setAbsorptionAmount((float) Math.min(absorption + target.getMaxHealth() * 0.05f, maxAbsorptionHearts));
			}
			break;
		case VENGEFUL:
			break;
		}
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		stack.damageItem(1, attacker, (entity) -> {
			entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
		});

//		if (super.hitEntity(stack, target, attacker))
//		{
		if (attacker instanceof PlayerEntity)
		{
			UUID id = attacker.getUniqueID();

			PlayerEntity attackerPlayer = (PlayerEntity) attacker;

//			System.out.println("Hit map: " + hitMap.containsKey(id));

			if (hitMap.containsKey(id) && hitMap.get(id))
			{
				EnumDemonWillType type = this.getCurrentType(stack);
				double will = PlayerDemonWillHandler.getTotalDemonWill(type, attackerPlayer);
				int willBracket = this.getLevel(stack, will);
				applyEffectToEntity(type, willBracket, target, attackerPlayer);
			} else
			{
				this.recalculatePowers(stack, attackerPlayer.getEntityWorld(), attackerPlayer);
				EnumDemonWillType type = this.getCurrentType(stack);
				double will = PlayerDemonWillHandler.getTotalDemonWill(type, attackerPlayer);
				int willBracket = this.getLevel(stack, will);
				hitMap.put(id, true);
				this.attackEntitiesInAreaExcludingEntity(stack, attackerPlayer, type, willBracket, target);
				hitMap.remove(id);
			}
		}

		return true;
//		}

//		return false;
	}

	@Override
	public EnumDemonWillType getCurrentType(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		if (!tag.contains(Constants.NBT.WILL_TYPE))
		{
			return EnumDemonWillType.DEFAULT;
		}

		return EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ENGLISH));
	}

	public void setCurrentType(ItemStack stack, EnumDemonWillType type)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putString(Constants.NBT.WILL_TYPE, type.toString());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		recalculatePowers(player.getHeldItem(hand), world, player);

		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return oldStack.getItem() != newStack.getItem();
	}

	private int getLevel(ItemStack stack, double soulsRemaining)
	{
		int lvl = -1;
		for (int i = 0; i < soulBracket.length; i++)
		{
			if (soulsRemaining >= soulBracket[i])
			{
				lvl = i;
			}
		}

		return lvl;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (!stack.hasTag())
			return;

		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sentientAxe.desc"));
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.currentType." + getCurrentType(stack).name().toLowerCase()));
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity)
	{
		recalculatePowers(stack, player.getEntityWorld(), player);

		double drain = this.getDrainOfActivatedSword(stack);
		if (drain > 0)
		{
			EnumDemonWillType type = getCurrentType(stack);
			double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);

			if (drain > soulsRemaining)
			{
				return false;
			} else
			{
				PlayerDemonWillHandler.consumeDemonWill(type, player, drain);
			}
		}

		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public List<ItemStack> getRandomDemonWillDrop(LivingEntity killedEntity, LivingEntity attackingEntity, ItemStack stack, int looting)
	{
		List<ItemStack> soulList = new ArrayList<>();

		if (killedEntity.getEntityWorld().getDifficulty() != Difficulty.PEACEFUL && !(killedEntity instanceof IMob))
		{
			return soulList;
		}

		double willModifier = killedEntity instanceof SlimeEntity ? 0.67 : 1;

		IDemonWill soul;

		EnumDemonWillType type = this.getCurrentType(stack);
		switch (type)
		{
		case CORROSIVE:
			soul = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_CORROSIVE.get());
			break;
		case DESTRUCTIVE:
			soul = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_DESTRUCTIVE.get());
			break;
		case STEADFAST:
			soul = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_STEADFAST.get());
			break;
		case VENGEFUL:
			soul = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_VENGEFUL.get());
			break;
		default:
			soul = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_RAW.get());
			break;
		}

		for (int i = 0; i <= looting; i++)
		{
			if (i == 0 || attackingEntity.getEntityWorld().rand.nextDouble() < 0.4)
			{
				ItemStack soulStack = soul.createWill(willModifier * (this.getDropOfActivatedSword(stack) * attackingEntity.getEntityWorld().rand.nextDouble() + this.getStaticDropOfActivatedSword(stack)) * killedEntity.getMaxHealth() / 20d);
				soulList.add(soulStack);
			}
		}

		return soulList;
	}

	// TODO: Change attack speed.
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
		if (slot == EquipmentSlotType.MAINHAND)
		{
			multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getDamageOfActivatedSword(stack), AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", this.getAttackSpeedOfSword(stack), AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.MAX_HEALTH, new AttributeModifier(new UUID(0, 31818145), "Weapon modifier", this.getHealthBonusOfSword(stack), AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(new UUID(0, 4218052), "Weapon modifier", this.getSpeedOfSword(stack), AttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}

	public double getDamageOfActivatedSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_DAMAGE);
	}

	public void setDamageOfActivatedSword(ItemStack stack, double damage)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_DAMAGE, damage);
	}

	public double getDrainOfActivatedSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_ACTIVE_DRAIN);
	}

	public void setDrainOfActivatedSword(ItemStack stack, double drain)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_ACTIVE_DRAIN, drain);
	}

	public double getStaticDropOfActivatedSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_STATIC_DROP);
	}

	public void setStaticDropOfActivatedSword(ItemStack stack, double drop)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_STATIC_DROP, drop);
	}

	public double getDropOfActivatedSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_DROP);
	}

	public void setDropOfActivatedSword(ItemStack stack, double drop)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_DROP, drop);
	}

	public double getHealthBonusOfSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_HEALTH);
	}

	public void setHealthBonusOfSword(ItemStack stack, double hp)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_HEALTH, hp);
	}

	public double getAttackSpeedOfSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_ATTACK_SPEED);
	}

	public void setAttackSpeedOfSword(ItemStack stack, double speed)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_ATTACK_SPEED, speed);
	}

	public double getSpeedOfSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_SPEED);
	}

	public void setSpeedOfSword(ItemStack stack, double speed)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_SPEED, speed);
	}

	public double getDigSpeedOfSword(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();
		return tag.getDouble(Constants.NBT.SOUL_SWORD_DIG_SPEED);
	}

	public void setDigSpeedOfSword(ItemStack stack, double speed)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putDouble(Constants.NBT.SOUL_SWORD_DIG_SPEED, speed);
	}
}
