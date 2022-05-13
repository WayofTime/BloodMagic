package wayoftime.bloodmagic.common.item.potion;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;
import wayoftime.bloodmagic.BloodMagic;

public class ItemAlchemyFlask extends PotionItem
{
	public ItemAlchemyFlask()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB).maxDamage(8));
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving)
	{
		PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;
		if (playerentity instanceof ServerPlayerEntity)
		{
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) playerentity, stack);
		}

		if (!worldIn.isRemote)
		{
			for (EffectInstance effectinstance : PotionUtils.getEffectsFromStack(stack))
			{
				if (effectinstance.getPotion().isInstant())
				{
					effectinstance.getPotion().affectEntity(playerentity, playerentity, entityLiving, effectinstance.getAmplifier(), 1.0D);
				} else
				{
					entityLiving.addPotionEffect(new EffectInstance(effectinstance));
				}
			}
		}

		if (playerentity != null)
		{
			playerentity.addStat(Stats.ITEM_USED.get(this));
			if (!playerentity.abilities.isCreativeMode)
			{
				stack.shrink(1);
			}
		}

		if (playerentity == null || !playerentity.abilities.isCreativeMode)
		{
			if (stack.isEmpty())
			{
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (playerentity != null)
			{
				playerentity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return stack;
	}
}
