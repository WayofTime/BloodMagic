package wayoftime.bloodmagic.common.item.potion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.recipe.EffectHolder;

public class ItemAlchemyFlask extends Item
{
	public ItemAlchemyFlask()
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB).durability(8));
	}

	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.arctool.uses", getRemainingUses(stack)).withStyle(TextFormatting.GOLD));
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
	public UseAction getUseAnimation(ItemStack stack)
	{
		return UseAction.DRINK;
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
		List<EffectInstance> effectList = new ArrayList<EffectInstance>();

		for (EffectHolder holder : holderList)
		{
			effectList.add(holder.getEffectInstance(false, true));
		}

		setEffectsOfFlask(stack, effectList);
	}

	public ItemStack setEffectsOfFlask(ItemStack stack, Collection<EffectInstance> effects)
	{
		if (effects.isEmpty())
		{
			return stack;
		} else
		{
			CompoundNBT compoundnbt = stack.getOrCreateTag();
			ListNBT listnbt = new ListNBT();

			for (EffectInstance effectinstance : effects)
			{
				listnbt.add(effectinstance.save(new CompoundNBT()));
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
			CompoundNBT compoundnbt = stack.getOrCreateTag();
			ListNBT listnbt = new ListNBT();

			for (EffectHolder effectHolder : effects)
			{
				listnbt.add(effectHolder.write(new CompoundNBT()));
			}

			compoundnbt.put("effectholder", listnbt);
		}
	}

	public List<EffectHolder> getEffectHoldersOfFlask(ItemStack stack)
	{
		List<EffectHolder> holderList = new ArrayList<EffectHolder>();
		CompoundNBT tag = stack.getTag();
		ListNBT tags = tag.getList("effectholder", 10);
		if (tags.isEmpty())
		{
			return holderList;
		}

		for (int i = 0; i < tags.size(); i++)
		{
			CompoundNBT newTag = tags.getCompound(i);
			EffectHolder holder = EffectHolder.read(newTag);
			if (holder != null)
			{
				holderList.add(holder);
			}
		}

		return holderList;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack heldStack = playerIn.getItemInHand(handIn);
		if (getRemainingUses(heldStack) <= 0)
		{
			return ActionResult.pass(heldStack);
		}

		return DrinkHelper.useDrink(worldIn, playerIn, handIn);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving)
	{
		PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;
		if (playerentity instanceof ServerPlayerEntity)
		{
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) playerentity, stack);
		}

		if (!worldIn.isClientSide)
		{
			for (EffectInstance effectinstance : PotionUtils.getMobEffects(stack))
			{
				if (effectinstance.getEffect().isInstantenous())
				{
					effectinstance.getEffect().applyInstantenousEffect(playerentity, playerentity, entityLiving, effectinstance.getAmplifier(), 1.0D);
				} else
				{
					entityLiving.addEffect(new EffectInstance(effectinstance));
				}
			}
		}

		if (playerentity != null)
		{
			playerentity.awardStat(Stats.ITEM_USED.get(this));
			if (!playerentity.abilities.instabuild)
			{
				stack.setDamageValue(stack.getDamageValue() + 1);
			}
		}

		return stack;
	}
}
