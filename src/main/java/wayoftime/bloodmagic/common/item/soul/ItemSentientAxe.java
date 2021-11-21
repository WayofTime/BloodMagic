package wayoftime.bloodmagic.common.item.soul;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWill;
import wayoftime.bloodmagic.api.compat.IDemonWillWeapon;
import wayoftime.bloodmagic.api.compat.IMultiWillTool;
import wayoftime.bloodmagic.common.item.BMItemTier;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

public class ItemSentientAxe extends AxeItem implements IDemonWillWeapon, IMultiWillTool
{
	public static int[] soulBracket = new int[] { 16, 60, 200, 400, 1000 };
	public static double[] defaultDamageAdded = new double[] { 1, 2, 3, 3.5, 4 };
	public static double[] destructiveDamageAdded = new double[] { 2, 3, 4, 5, 6 };
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

	public final double baseAttackDamage = 8;
	public final double baseAttackSpeed = -3;

	public ItemSentientAxe()
	{
		super(BMItemTier.SENTIENT, 8, -3.1f, new Item.Properties().maxDamage(520).group(BloodMagic.TAB));
//		super(RegistrarBloodMagicItems.SOUL_TOOL_MATERIAL, 8.0F, 3.1F);
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

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return BloodMagicTags.CRYSTAL_DEMON.contains(repair.getItem()) || super.getIsRepairable(toRepair, repair);
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
				attacker.addPotionEffect(new EffectInstance(Effects.ABSORPTION, absorptionTime[willBracket], 127));
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
		if (super.hitEntity(stack, target, attacker))
		{
			if (attacker instanceof PlayerEntity)
			{
				PlayerEntity attackerPlayer = (PlayerEntity) attacker;
				this.recalculatePowers(stack, attackerPlayer.getEntityWorld(), attackerPlayer);
				EnumDemonWillType type = this.getCurrentType(stack);
				double will = PlayerDemonWillHandler.getTotalDemonWill(type, attackerPlayer);
				int willBracket = this.getLevel(stack, will);

				applyEffectToEntity(type, willBracket, target, attackerPlayer);

				ItemStack offStack = attackerPlayer.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
//				if (offStack.getItem() instanceof ISentientSwordEffectProvider)
//				{
//					ISentientSwordEffectProvider provider = (ISentientSwordEffectProvider) offStack.getItem();
//					if (provider.providesEffectForWill(type))
//					{
//						provider.applyOnHitEffect(type, stack, offStack, attacker, target);
//					}
//				}
			}

			return true;
		}

		return false;
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

		return EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ROOT));
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

		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sentientAxe.desc").mergeStyle(TextFormatting.GRAY));
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.currentType." + getCurrentType(stack).name().toLowerCase(Locale.ROOT)).mergeStyle(TextFormatting.GRAY));
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

//    @Override
//    public boolean spawnSentientEntityOnDrop(ItemStack droppedStack, PlayerEntity player) {
//        World world = player.getEntityWorld();
//        if (!world.isRemote) {
//            this.recalculatePowers(droppedStack, world, player);
//
//            EnumDemonWillType type = this.getCurrentType(droppedStack);
//            double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(type, player);
//            if (soulsRemaining < 1024) {
//                return false;
//            }
//
//            PlayerDemonWillHandler.consumeDemonWill(type, player, 100);
//
//            EntitySentientSpecter specterEntity = new EntitySentientSpecter(world);
//            specterEntity.setPosition(player.posX, player.posY, player.posZ);
//            world.spawnEntity(specterEntity);
//
//            specterEntity.setItemStackToSlot(EquipmentSlotType.MAINHAND, droppedStack.copy());
//
//            specterEntity.setType(this.getCurrentType(droppedStack));
//            specterEntity.setOwner(player);
//            specterEntity.setTamed(true);
//
//            return true;
//        }
//
//        return false;
//    }
}