package wayoftime.bloodmagic.common.item.sigil;

import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.registries.BloodMagicDamageTypes;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilFastMiner extends ItemSigilToggleableBase
{
	public ItemSigilFastMiner()
	{
		super("fast_miner", 100);
	}

	@Override
	public void onSigilUpdate(ItemStack stack, Level world, Player player, int itemSlot, boolean isSelected)
	{
		if (PlayerHelper.isFakePlayer(player))
			return;
		player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 2, 0, true, false));
	}

	@Override
	public boolean performArrayEffect(Level world, BlockPos pos)
	{
		double radius = 10;
		int ticks = 600;
		int potionPotency = 2;

		AABB bb = new AABB(pos).inflate(radius);
		List<Player> playerList = world.getEntitiesOfClass(Player.class, bb);
		for (Player player : playerList)
		{
			if (!player.hasEffect(MobEffects.DIG_SPEED) || (player.hasEffect(MobEffects.DIG_SPEED)
					&& player.getEffect(MobEffects.DIG_SPEED).getAmplifier() < potionPotency))
			{
				player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, ticks, potionPotency));
				if (!player.isCreative())
				{
					player.invulnerableTime = 0;
					player.hurt(player.damageSources().source(BloodMagicDamageTypes.SACRIFICE), 1.0F);
				}
			}
		}

		return false;
	}

	@Override
	public boolean hasArrayEffect()
	{
		return true;
	}
}
