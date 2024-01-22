package wayoftime.bloodmagic.ritual.types;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.network.SetClientVelocityPacket;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.*;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("speed")
public class RitualSpeed extends Ritual
{
	public static final String SPEED_RANGE = "sanicRange";

	public static final double vengefulWillDrain = 0.05;
	public static final double destructiveWillDrain = 0.05;
	public static final double rawWillDrain = 0.1;
	public static final double steadfastWillDrain = 0.05;
	public static final double corrosiveWillDrain = 0.05;

	public RitualSpeed()
	{
		super("ritualSpeed", 0, 1000, "ritual." + BloodMagic.MODID + ".speedRitual");
		addBlockRange(SPEED_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 1, -2), new BlockPos(3, 5, 3)));
		setMaximumVolumeAndDistanceOfRange(SPEED_RANGE, 0, 4, 5);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getMasterBlockPos();

		List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

		double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
		double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
		double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
		double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
		double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

		AreaDescriptor speedRange = masterRitualStone.getBlockRange(SPEED_RANGE);

		double vengefulDrain = 0;
		double destructiveDrain = 0;
		double rawDrain = 0;
		double steadfastDrain = 0;
		double corrosiveDrain = 0;

		if (rawWill < rawWillDrain)
		{
			rawWill = 0; // Simplifies later calculations
		}

		if (corrosiveWill < corrosiveWillDrain)
		{
			corrosiveWill = 0; // Simplifies later calculations
		}

		for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, speedRange.getAABB(masterRitualStone.getMasterBlockPos())))
		{
			if (entity.isShiftKeyDown())
				continue;

			boolean transportChildren = destructiveWill < destructiveWillDrain;
			boolean transportAdults = vengefulWill < vengefulWillDrain;

			if ((entity.isBaby() && !transportChildren) || (!entity.isBaby() && !transportAdults))
			{
				continue;
			}

			if (entity instanceof Player && (transportChildren ^ transportAdults))
			{
				continue;
			}

			if (!transportChildren)
			{
				destructiveWill -= destructiveWillDrain;
				destructiveDrain += destructiveWillDrain;
			}

			if (!transportAdults)
			{
				vengefulWill -= vengefulWillDrain;
				vengefulDrain += vengefulWillDrain;
			}

			double motionY = getVerticalSpeedForWill(rawWill);
			double speed = getHorizontalSpeedForWill(rawWill);
			Direction direction = masterRitualStone.getDirection();

			if (rawWill >= rawWillDrain)
			{
				rawWill -= rawWillDrain;
				rawDrain += rawWillDrain;
			}

			if (corrosiveWill >= corrosiveWillDrain)
			{
				corrosiveWill -= corrosiveWillDrain;
				corrosiveDrain += corrosiveWillDrain;
				speed += getAdditionalHorizontalSpeedForWill(corrosiveWill);
			}

			Vec3 motion = entity.getDeltaMovement();

			double motionX = motion.x();
			double motionZ = motion.z();
			entity.fallDistance = 0;

			switch (direction)
			{
			case NORTH:
				motionX = 0;
				motionZ = -speed;
				break;

			case SOUTH:
				motionX = 0;
				motionZ = speed;
				break;

			case WEST:
				motionX = -speed;
				motionZ = 0;
				break;

			case EAST:
				motionX = speed;
				motionZ = 0;
				break;
			default:
				break;
			}

			if (steadfastWill >= steadfastWillDrain)
			{
				entity.addEffect(new MobEffectInstance(BloodMagicPotions.SOFT_FALL.get(), 100, 0));
				steadfastWill -= steadfastWillDrain;
				steadfastDrain += steadfastWillDrain;
			}

			entity.setDeltaMovement(motionX, motionY, motionZ);
			if (entity instanceof ServerPlayer)
			{
				BloodMagic.packetHandler.sendTo(new SetClientVelocityPacket(motionX, motionY, motionZ), (ServerPlayer) entity);
			}
		}

		if (rawDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawDrain, true);
		}

		if (vengefulDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulDrain, true);
		}

		if (destructiveDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DESTRUCTIVE, destructiveDrain, true);
		}

		if (steadfastDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastDrain, true);
		}

		if (corrosiveDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveDrain, true);
		}
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
	}

	@Override
	public int getRefreshCost()
	{
		return 5;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addRune(components, 0, 0, -2, EnumRuneType.DUSK);
		addRune(components, 1, 0, -1, EnumRuneType.AIR);
		addRune(components, -1, 0, -1, EnumRuneType.AIR);
		for (int i = 0; i < 3; i++)
		{
			addRune(components, 2, 0, i, EnumRuneType.AIR);
			addRune(components, -2, 0, i, EnumRuneType.AIR);
		}
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualSpeed();
	}

	@Override
	public Component[] provideInformationOfRitualToPlayer(Player player)
	{
		return new Component[] { Component.translatable(this.getTranslationKey() + ".info"),
				Component.translatable(this.getTranslationKey() + ".default.info"),
				Component.translatable(this.getTranslationKey() + ".corrosive.info"),
				Component.translatable(this.getTranslationKey() + ".steadfast.info"),
				Component.translatable(this.getTranslationKey() + ".destructive.info"),
				Component.translatable(this.getTranslationKey() + ".vengeful.info") };
	}

	public double getVerticalSpeedForWill(double rawWill)
	{
		return 1.2 + rawWill / 200;
	}

	public double getHorizontalSpeedForWill(double rawWill)
	{
		return 3 + rawWill / 40;
	}

	public double getAdditionalHorizontalSpeedForWill(double corrosiveWill)
	{
		return corrosiveWill / 40;
	}
}