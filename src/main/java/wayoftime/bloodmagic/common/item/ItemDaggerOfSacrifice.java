package wayoftime.bloodmagic.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.FakePlayer;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.DamageSourceBloodMagic;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

public class ItemDaggerOfSacrifice extends Item
{
	public ItemDaggerOfSacrifice()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		if (attacker instanceof FakePlayer)
			return false;

		if (target == null || attacker == null || attacker.getEntityWorld().isRemote
				|| (attacker instanceof PlayerEntity && !(attacker instanceof ServerPlayerEntity)))
			return false;

		if (!target.isNonBoss())
			return false;

		if (target instanceof PlayerEntity)
			return false;

		if (target.isChild() && !(target instanceof IMob))
			return false;

		if (!target.isAlive() || target.getHealth() < 0.5F)
			return false;

//		EntityEntry entityEntry = EntityRegistry.getEntry(target.getClass());
//		if (entityEntry == null)
//			return false;
//		int lifeEssenceRatio = BloodMagicAPI.INSTANCE.getValueManager().getSacrificial().getOrDefault(entityEntry.getRegistryName(), 25);
		int lifeEssenceRatio = 25;

		if (lifeEssenceRatio <= 0)
			return false;

		int lifeEssence = (int) (lifeEssenceRatio * target.getHealth());
//		if (target instanceof AnimalEntity)
//		{
//			lifeEssence = (int) (lifeEssence * (1 + PurificationHelper.getCurrentPurity((AnimalEntity) target)));
//		}

		if (target.isChild())
		{
			lifeEssence *= 0.5F;
		}

		if (PlayerSacrificeHelper.findAndFillAltar(attacker.getEntityWorld(), target, lifeEssence, true))
		{
			target.getEntityWorld().playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F
					+ (target.getEntityWorld().rand.nextFloat() - target.getEntityWorld().rand.nextFloat()) * 0.8F);
			target.setHealth(-1);
			target.onDeath(DamageSourceBloodMagic.INSTANCE);
		}

		return false;
	}
}
