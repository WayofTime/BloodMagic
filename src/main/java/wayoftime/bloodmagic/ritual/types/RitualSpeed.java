package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.network.SetClientVelocityPacket;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;

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
		World world = masterRitualStone.getWorldObj();
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

		for (LivingEntity entity : world.getEntitiesWithinAABB(LivingEntity.class, speedRange.getAABB(masterRitualStone.getMasterBlockPos())))
		{
			if (entity.isSneaking())
				continue;

			boolean transportChildren = destructiveWill < destructiveWillDrain;
			boolean transportAdults = vengefulWill < vengefulWillDrain;

			if ((entity.isChild() && !transportChildren) || (!entity.isChild() && !transportAdults))
			{
				continue;
			}

			if (entity instanceof PlayerEntity && (transportChildren ^ transportAdults))
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

			Vector3d motion = entity.getMotion();

			double motionX = motion.getX();
			double motionZ = motion.getZ();
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
				entity.addPotionEffect(new EffectInstance(BloodMagicPotions.SOFT_FALL, 100, 0));
				steadfastWill -= steadfastWillDrain;
				steadfastDrain += steadfastWillDrain;
			}

			entity.setMotion(motionX, motionY, motionZ);
			if (entity instanceof ServerPlayerEntity)
			{
				BloodMagic.packetHandler.sendTo(new SetClientVelocityPacket(motionX, motionY, motionZ), (ServerPlayerEntity) entity);
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
	public ITextComponent[] provideInformationOfRitualToPlayer(PlayerEntity player)
	{
		return new ITextComponent[] { new TranslationTextComponent(this.getTranslationKey() + ".info"),
				new TranslationTextComponent(this.getTranslationKey() + ".default.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".corrosive.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".steadfast.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".destructive.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".vengeful.info") };
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