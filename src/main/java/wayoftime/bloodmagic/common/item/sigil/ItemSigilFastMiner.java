package wayoftime.bloodmagic.common.item.sigil;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.util.DamageSourceBloodMagic;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilFastMiner extends ItemSigilToggleableBase
{
	public ItemSigilFastMiner()
	{
		super("fast_miner", 100);
	}

	@Override
	public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected)
	{
		if (PlayerHelper.isFakePlayer(player))
			return;
		player.addEffect(new EffectInstance(Effects.DIG_SPEED, 2, 0, true, false));
	}

	@Override
	public boolean performArrayEffect(World world, BlockPos pos)
	{
		double radius = 10;
		int ticks = 600;
		int potionPotency = 2;

		AxisAlignedBB bb = new AxisAlignedBB(pos).inflate(radius);
		List<PlayerEntity> playerList = world.getEntitiesOfClass(PlayerEntity.class, bb);
		for (PlayerEntity player : playerList)
		{
			if (!player.hasEffect(Effects.DIG_SPEED) || (player.hasEffect(Effects.DIG_SPEED)
					&& player.getEffect(Effects.DIG_SPEED).getAmplifier() < potionPotency))
			{
				player.addEffect(new EffectInstance(Effects.DIG_SPEED, ticks, potionPotency));
				if (!player.isCreative())
				{
					player.invulnerableTime = 0;
					player.hurt(DamageSourceBloodMagic.INSTANCE, 1.0F);
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
