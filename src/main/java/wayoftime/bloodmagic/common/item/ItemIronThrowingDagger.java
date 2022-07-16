package wayoftime.bloodmagic.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.entity.projectile.AbstractEntityThrowingDagger;
import wayoftime.bloodmagic.entity.projectile.EntityThrowingDagger;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

public class ItemIronThrowingDagger extends ItemThrowingDagger
{
	public static int[] soulBracket = new int[] { 1, 60, 200, 400, 1000, 2000, 4000 };

	public static double[] soulDrop = new double[] { 2, 4, 7, 10, 13, 15, 18 };
	public static double[] staticDrop = new double[] { 1, 1, 2, 3, 3, 4, 4 };

	public AbstractEntityThrowingDagger getDagger(ItemStack stack, Level world, Player player)
	{
		AbstractEntityThrowingDagger dagger = new EntityThrowingDagger(stack, world, player);
		EnumDemonWillType largestType = PlayerDemonWillHandler.getLargestWillType(player);
		double souls = PlayerDemonWillHandler.getTotalDemonWill(largestType, player);

		int level = getLevel(souls);
		if (level >= 0)
		{
			double willDrop = (soulDrop[level] * world.random.nextDouble() + staticDrop[level]);
			dagger.setWillDrop(willDrop);
			dagger.setWillType(largestType);
		}

		dagger.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0.5F);
		dagger.setDamage(10);
		dagger.addEffect(new MobEffectInstance(MobEffects.POISON, 800, 0));

		return dagger;
	}

	protected int getLevel(double soulsRemaining)
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
}
