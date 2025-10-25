package wayoftime.bloodmagic.ritual.types;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigManager;
import wayoftime.bloodmagic.altar.IBloodAltar;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.network.SetClientHealthPacket;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.*;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("feathered_knife")
public class RitualFeatheredKnife extends Ritual
{
	public static final String ALTAR_RANGE = "altar";
	public static final String DAMAGE_RANGE = "damage";

	public static double rawWillDrain = 0.05;
	public static double destructiveWillDrain = 0.05;
	public static double corrosiveWillThreshold = 10;
	public static double steadfastWillThreshold = 10;
	public static double vengefulWillThreshold = 10;
	public static int defaultRefreshTime = 20;
	public int refreshTime = 20;
	public BlockPos altarOffsetPos = new BlockPos(0, 0, 0); // TODO: Save!

	public RitualFeatheredKnife()
	{
		super("ritualFeatheredKnife", 0, 25000, "ritual." + BloodMagic.MODID + ".featheredKnifeRitual");
		addBlockRange(ALTAR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -10, -5), 11, 21, 11));
		addBlockRange(DAMAGE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-15, -20, -15), 31, 41, 31));

		setMaximumVolumeAndDistanceOfRange(ALTAR_RANGE, 0, 10, 15);
		setMaximumVolumeAndDistanceOfRange(DAMAGE_RANGE, 0, 25, 25);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
//		if (world.isRemote)
//		{
//			return;
//		}
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

		refreshTime = getRefreshTimeForRawWill(rawWill);

		boolean consumeRawWill = rawWill >= rawWillDrain && refreshTime != defaultRefreshTime;

		int maxEffects = currentEssence / getRefreshCost();
		int totalEffects = 0;

		BlockPos altarPos = pos.offset(altarOffsetPos);

		BlockEntity tile = world.getBlockEntity(altarPos);

		AreaDescriptor altarRange = masterRitualStone.getBlockRange(ALTAR_RANGE);

		if (!altarRange.isWithinArea(altarOffsetPos) || !(tile instanceof IBloodAltar))
		{
			for (BlockPos newPos : altarRange.getContainedPositions(pos))
			{
				BlockEntity nextTile = world.getBlockEntity(newPos);
				if (nextTile instanceof IBloodAltar)
				{
					tile = nextTile;
					altarOffsetPos = newPos.subtract(pos);

					altarRange.resetCache();
					break;
				}
			}
		}

		boolean useIncense = corrosiveWill >= corrosiveWillThreshold;

		if (tile instanceof IBloodAltar)
		{
			IBloodAltar tileAltar = (IBloodAltar) tile;

			AreaDescriptor damageRange = masterRitualStone.getBlockRange(DAMAGE_RANGE);
			AABB range = damageRange.getAABB(pos);

			double destructiveDrain = 0;

			List<Player> entities = world.getEntitiesOfClass(Player.class, range);

			for (Player player : entities)
			{
				float healthThreshold = steadfastWill >= steadfastWillThreshold ? 0.7f : 0.3f;

				if (vengefulWill >= vengefulWillThreshold && !player.getGameProfile().getId().equals(masterRitualStone.getOwner()))
				{
					healthThreshold = 0.1f;
				}

				float health = player.getHealth();
				float maxHealth = player.getMaxHealth();

				float sacrificedHealth = 1;
				double lpModifier = 1;

				if ((health / player.getMaxHealth() > healthThreshold) && (!useIncense || !player.hasEffect(BloodMagicPotions.SOUL_FRAY.get())))
				{
					if (useIncense)
					{
						double incenseAmount = PlayerSacrificeHelper.getPlayerIncense(player);

						sacrificedHealth = health - maxHealth * healthThreshold;
						lpModifier *= PlayerSacrificeHelper.getModifier(incenseAmount);

						PlayerSacrificeHelper.setPlayerIncense(player, 0);
						player.addEffect(new MobEffectInstance(BloodMagicPotions.SOUL_FRAY.get(), PlayerSacrificeHelper.soulFrayDuration));
					}

					if (destructiveWill >= destructiveWillDrain * sacrificedHealth)
					{
						lpModifier *= getLPModifierForWill(destructiveWill);
						destructiveWill -= destructiveWillDrain * sacrificedHealth;
						destructiveDrain += destructiveWillDrain * sacrificedHealth;
					}

					if (LivingUtil.hasFullSet(player))
					{
						LivingStats stats = LivingStats.fromPlayer(player, true);
						double bonus = LivingArmorRegistrar.UPGRADE_SELF_SACRIFICE.get().getBonusValue("self_mod", stats.getLevel(LivingArmorRegistrar.UPGRADE_SELF_SACRIFICE.get().getKey())).doubleValue();
						lpModifier *= (1 + bonus);
						LivingUtil.applyNewExperience(player, LivingArmorRegistrar.UPGRADE_SELF_SACRIFICE.get(), sacrificedHealth);
					}

					player.setHealth(health - sacrificedHealth);
					BloodMagic.packetHandler.sendTo(new SetClientHealthPacket(health - sacrificedHealth), (ServerPlayer) player);

					tileAltar.sacrificialDaggerCall((int) (ConfigManager.COMMON.sacrificialDaggerConversion.get() * lpModifier * sacrificedHealth), false);

					totalEffects++;

					if (totalEffects >= maxEffects)
					{
						break;
					}

				}
			}

			if (destructiveDrain > 0)
			{
				WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, destructiveDrain, true);
			}
		}

		masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
		if (totalEffects > 0 && consumeRawWill)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawWillDrain, true);
		}
	}

	@Override
	public int getRefreshTime()
	{
		return refreshTime;
	}

	@Override
	public int getRefreshCost()
	{
		return 20;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addParallelRunes(components, 1, 0, EnumRuneType.DUSK);
		addParallelRunes(components, 2, -1, EnumRuneType.WATER);
		addCornerRunes(components, 1, -1, EnumRuneType.AIR);
		addOffsetRunes(components, 2, 4, -1, EnumRuneType.FIRE);
		addOffsetRunes(components, 2, 4, 0, EnumRuneType.EARTH);
		addOffsetRunes(components, 4, 3, 0, EnumRuneType.EARTH);
		addCornerRunes(components, 3, 0, EnumRuneType.AIR);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualFeatheredKnife();
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

	public double getLPModifierForWill(double destructiveWill)
	{
		return 1 + destructiveWill * 0.2 / 100;
	}

	public int getRefreshTimeForRawWill(double rawWill)
	{
		if (rawWill >= rawWillDrain)
		{
			return 10;
		}

		return defaultRefreshTime;
	}
}
