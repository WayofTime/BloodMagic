package wayoftime.bloodmagic.ritual.types;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.*;
import wayoftime.bloodmagic.will.DemonWillHolder;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("grounding")
public class RitualGrounding extends Ritual
{

	public static final int willRefreshTime = 20;
	public static final String GROUNDING_RANGE = "groundingRange";
	public static final double willDrain = 0.1;

	public RitualGrounding()
	{
		super("ritualGrounding", 0, 5000, "ritual." + BloodMagic.MODID + ".groundingRitual");
		addBlockRange(GROUNDING_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, 0, -10), 21, 30, 21));
		setMaximumVolumeAndDistanceOfRange(GROUNDING_RANGE, 0, 200, 200);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		/* Default Ritual Stuff */
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
		BlockPos pos = masterRitualStone.getMasterBlockPos();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		int maxEffects = currentEssence / getRefreshCost();
		int totalEffects = 0;

		/* Default will augment stuff */
		List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();
		DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(world, pos);

		double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
		double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
		double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
		double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
		double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

		double rawDrained = 0;
		double corrosiveDrained = 0;
		double destructiveDrained = 0;
		double steadfastDrained = 0;
		double vengefulDrained = 0;

		/* Actual ritual stuff begins here */
		AreaDescriptor groundingRange = masterRitualStone.getBlockRange(GROUNDING_RANGE);
		List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, groundingRange.getAABB(pos));
		for (LivingEntity entity : entities)
		{
			if (totalEffects >= maxEffects)
			{
				break;
			}

			if (entity instanceof Player && ((Player) entity).isCreative())
				continue;

			totalEffects++;

			if (entity instanceof Player)
			{
				/* Raw will effect: Affects players */
				if (world.getGameTime() % 10 == 0)
				{
					if (rawWill >= willDrain)
					{

						rawDrained += willDrain;

						double[] drainagePlayer = sharedWillEffects(world, entity, corrosiveWill, destructiveWill, vengefulWill, corrosiveDrained, destructiveDrained, vengefulDrained);

						corrosiveDrained += drainagePlayer[0];
						destructiveDrained += drainagePlayer[1];
						vengefulDrained += drainagePlayer[2];
					}
				}
			} else if (entity.canChangeDimensions())
			{
				if (world.getGameTime() % 10 == 0)
				{
					double[] drainageEntity = sharedWillEffects(world, entity, corrosiveWill, destructiveWill, vengefulWill, corrosiveDrained, destructiveDrained, vengefulDrained);

					corrosiveDrained += drainageEntity[0];
					destructiveDrained += drainageEntity[1];
					vengefulDrained += drainageEntity[2];
				}
			} else if (!entity.canChangeDimensions())
			{
				/*
				 * Steadfast will effect: Affects bosses (some bosses, like the wither, have a
				 * restriction to motion modification, others, like the Ender Dragon, don't do
				 * potions)
				 */
				if (steadfastWill >= willDrain)
				{
					if (entity instanceof WitherBoss || entity instanceof EnderDragon)
						entity.move(MoverType.SELF, new Vec3(0, -0.05, 0)); // to work on Wither and EnderDragon
																				// without
					// interfering with other mod author's decisions
					// (looking at you, Vazkii)

					steadfastDrained += willDrain / 10f;

					double[] drainagePlayer = sharedWillEffects(world, entity, corrosiveWill, destructiveWill, vengefulWill, corrosiveDrained, destructiveDrained, vengefulDrained);

					corrosiveDrained += drainagePlayer[0];
					destructiveDrained += drainagePlayer[1];
					vengefulDrained += drainagePlayer[2];
				}
			}
		}

		if (rawDrained > 0)
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawDrained, true);
		if (corrosiveDrained > 0)
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveDrained, true);
		if (destructiveDrained > 0)
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DESTRUCTIVE, destructiveDrained, true);
		if (steadfastDrained > 0)
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastDrained, true);
		if (vengefulDrained > 0)
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulDrained, true);

		masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
	}

	@Override
	public int getRefreshCost()
	{
		return Math.max(1, getBlockRange(GROUNDING_RANGE).getVolume() / 10000);
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addParallelRunes(components, 1, 0, EnumRuneType.DUSK);
		addCornerRunes(components, 2, 2, EnumRuneType.EARTH);
		addCornerRunes(components, 3, 3, EnumRuneType.EARTH);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualGrounding();
	}

	public double[] sharedWillEffects(Level world, LivingEntity entity, double corrosiveWill, double destructiveWill, double vengefulWill, double corrosiveDrained, double destructiveDrained, double vengefulDrained)
	{
		/* Corrosive will effect: Suspension */
		if (corrosiveWill >= willDrain)
		{

			entity.addEffect(new MobEffectInstance(BloodMagicPotions.SUSPENDED.get(), 20, 0));
			corrosiveDrained += willDrain;

			/* Vengeful will effect: Levitation */
		} else if (vengefulWill >= willDrain)
		{

			vengefulDrained += willDrain;
			entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 10));

		} else
		{
			entity.addEffect(new MobEffectInstance(BloodMagicPotions.GROUNDED.get(), 20, 0));
			entity.addEffect(new MobEffectInstance(BloodMagicPotions.GRAVITY.get(), 20, 0));
		}

		/* Destructive will effect: Increased fall damage */
		if (destructiveWill >= willDrain)
		{
			destructiveDrained += willDrain;

			entity.addEffect(new MobEffectInstance(BloodMagicPotions.HEAVY_HEART.get(), 100, 1));
		}
		return new double[] { corrosiveDrained, destructiveDrained, vengefulDrained };
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
}