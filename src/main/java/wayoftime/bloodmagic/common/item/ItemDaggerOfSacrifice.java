package wayoftime.bloodmagic.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.FakePlayer;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.util.DamageSourceBloodMagic;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

public class ItemDaggerOfSacrifice extends Item
{
	public ItemDaggerOfSacrifice()
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB));
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		if (attacker instanceof FakePlayer)
			return false;

		if (target == null || attacker == null || attacker.getCommandSenderWorld().isClientSide || (attacker instanceof PlayerEntity && !(attacker instanceof ServerPlayerEntity)))
			return false;

		if (!target.canChangeDimensions())
			return false;

		if (target instanceof PlayerEntity)
			return false;

		if (target.isBaby() && !(target instanceof IMob))
			return false;

		if (!target.isAlive() || target.getHealth() < 0.5F)
			return false;

//		EntityEntry entityEntry = EntityRegistry.getEntry(target.getClass());
//		if (entityEntry == null)
//			return false;
		ResourceLocation id = target.getType().getRegistryName();
		int lifeEssenceRatio = BloodMagicAPI.INSTANCE.getValueManager().getSacrificial().getOrDefault(id, 25);
//		int lifeEssenceRatio = 25;

		if (lifeEssenceRatio <= 0)
			return false;

		int lifeEssence = (int) (lifeEssenceRatio * target.getHealth());
//		if (target instanceof AnimalEntity)
//		{
//			lifeEssence = (int) (lifeEssence * (1 + PurificationHelper.getCurrentPurity((AnimalEntity) target)));
//		}

		if (target.isBaby())
		{
			lifeEssence *= 0.5F;
		}

		if (PlayerSacrificeHelper.findAndFillAltar(attacker.getCommandSenderWorld(), target, lifeEssence, true))
		{
			target.getCommandSenderWorld().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (target.getCommandSenderWorld().random.nextFloat() - target.getCommandSenderWorld().random.nextFloat()) * 0.8F);
			target.setHealth(0.00001f);
			target.invulnerableTime = 0;
			target.hurt(DamageSourceBloodMagic.INSTANCE, 10);
//			target.onDeath(DamageSourceBloodMagic.INSTANCE);
		}

		return false;
	}
}
