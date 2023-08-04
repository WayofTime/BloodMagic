package wayoftime.bloodmagic.common.item.potion;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.recipe.EffectHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemAlchemyFlask extends Item
{
	public ItemAlchemyFlask()
	{
		super(new Item.Properties().stacksTo(1).durability(8));
	}

	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.arctool.uses", getRemainingUses(stack)).withStyle(ChatFormatting.GOLD));
		PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
	}

	public int getRemainingUses(ItemStack stack)
	{
		return stack.getMaxDamage() - stack.getDamageValue();
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getUseDuration(ItemStack stack)
	{
		return 32;
	}

	/**
	 * returns the action that specifies what animation to play when the items is
	 * being used
	 */
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.DRINK;
	}

//	@Override
//	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
//	{
//		if (!isInGroup(group))
//			return;
//
//		ItemStack speedStack = new ItemStack(this);
//		items.add(speedStack);
////		for (Entry<ResourceLocation, LivingUpgrade> entry : LivingArmorRegistrar.UPGRADE_MAP.entrySet())
////		{
//
////		}
//	}

	public void resyncEffectInstances(ItemStack stack)
	{
		List<EffectHolder> holderList = getEffectHoldersOfFlask(stack);
		List<MobEffectInstance> effectList = new ArrayList<MobEffectInstance>();

		for (EffectHolder holder : holderList)
		{
			effectList.add(holder.getEffectInstance(false, true));
		}

		setEffectsOfFlask(stack, effectList);
	}

	public ItemStack setEffectsOfFlask(ItemStack stack, Collection<MobEffectInstance> effects)
	{
		if (effects.isEmpty())
		{
			return stack;
		} else
		{
			CompoundTag compoundnbt = stack.getOrCreateTag();
			ListTag listnbt = new ListTag();

			for (MobEffectInstance effectinstance : effects)
			{
				listnbt.add(effectinstance.save(new CompoundTag()));
			}

			compoundnbt.put("CustomPotionEffects", listnbt);
			return stack;
		}
	}

	public void setEffectHoldersOfFlask(ItemStack stack, List<EffectHolder> effects)
	{
		if (effects.isEmpty())
		{
			return;
		} else
		{
			CompoundTag compoundnbt = stack.getOrCreateTag();
			ListTag listnbt = new ListTag();

			for (EffectHolder effectHolder : effects)
			{
				listnbt.add(effectHolder.write(new CompoundTag()));
			}

			compoundnbt.put("effectholder", listnbt);
		}
	}

	public List<EffectHolder> getEffectHoldersOfFlask(ItemStack stack)
	{
		List<EffectHolder> holderList = new ArrayList<EffectHolder>();
		CompoundTag tag = stack.getTag();
		ListTag tags = tag.getList("effectholder", 10);
		if (tags.isEmpty())
		{
			return holderList;
		}

		for (int i = 0; i < tags.size(); i++)
		{
			CompoundTag newTag = tags.getCompound(i);
			EffectHolder holder = EffectHolder.read(newTag);
			if (holder != null)
			{
				holderList.add(holder);
			}
		}

		return holderList;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		ItemStack heldStack = playerIn.getItemInHand(handIn);
		if (getRemainingUses(heldStack) <= 0)
		{
			return InteractionResultHolder.pass(heldStack);
		}

		return ItemUtils.startUsingInstantly(worldIn, playerIn, handIn);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving)
	{
		Player playerentity = entityLiving instanceof Player ? (Player) entityLiving : null;
		if (playerentity instanceof ServerPlayer)
		{
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) playerentity, stack);
		}

		if (!worldIn.isClientSide)
		{
			for (MobEffectInstance effectinstance : PotionUtils.getMobEffects(stack))
			{
				if (effectinstance.getEffect().isInstantenous())
				{
					effectinstance.getEffect().applyInstantenousEffect(playerentity, playerentity, entityLiving, effectinstance.getAmplifier(), 1.0D);
				} else
				{
					entityLiving.addEffect(new MobEffectInstance(effectinstance));
				}
			}
		}

		if (playerentity != null)
		{
			playerentity.awardStat(Stats.ITEM_USED.get(this));
			if (!playerentity.getAbilities().instabuild)
			{
				stack.setDamageValue(stack.getDamageValue() + 1);
			}
		}

		return stack;
	}
}
