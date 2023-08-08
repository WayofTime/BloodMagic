package wayoftime.bloodmagic.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.common.registries.BloodMagicDamageTypes;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

public class ItemDaggerOfSacrifice extends Item
{
	public ItemDaggerOfSacrifice()
	{
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		if (attacker instanceof FakePlayer)
			return false;

		if (target == null || attacker == null || attacker.getCommandSenderWorld().isClientSide || (attacker instanceof Player && !(attacker instanceof ServerPlayer)))
			return false;

		if (!target.canChangeDimensions())
			return false;

		if (target instanceof Player)
			return false;

		if (target.isBaby() && !(target instanceof Enemy))
			return false;

		if (!target.isAlive() || target.getHealth() < 0.5F)
			return false;

//		EntityEntry entityEntry = EntityRegistry.getEntry(target.getClass());
//		if (entityEntry == null)
//			return false;
		ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(target.getType());
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
			target.getCommandSenderWorld().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (target.getCommandSenderWorld().random.nextFloat() - target.getCommandSenderWorld().random.nextFloat()) * 0.8F);
			target.setHealth(0.00001f);
			target.invulnerableTime = 0;
			target.hurt(target.damageSources().source(BloodMagicDamageTypes.SACRIFICE), 10);
//			target.onDeath(DamageSourceBloodMagic.INSTANCE);
		}

		return false;
	}
}
