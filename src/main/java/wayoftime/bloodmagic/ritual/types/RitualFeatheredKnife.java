package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigHandler;
import wayoftime.bloodmagic.altar.IBloodAltar;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.network.SetClientHealthPacket;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.helper.PlayerSacrificeHelper;

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
		setMaximumVolumeAndDistanceOfRange(DAMAGE_RANGE, 0, 25, 15);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();
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

		TileEntity tile = world.getBlockEntity(altarPos);

		AreaDescriptor altarRange = masterRitualStone.getBlockRange(ALTAR_RANGE);

		if (!altarRange.isWithinArea(altarOffsetPos) || !(tile instanceof IBloodAltar))
		{
			for (BlockPos newPos : altarRange.getContainedPositions(pos))
			{
				TileEntity nextTile = world.getBlockEntity(newPos);
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
			AxisAlignedBB range = damageRange.getAABB(pos);

			double destructiveDrain = 0;

			List<PlayerEntity> entities = world.getEntitiesOfClass(PlayerEntity.class, range);

			for (PlayerEntity player : entities)
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

				if ((health / player.getMaxHealth() > healthThreshold) && (!useIncense || !player.hasEffect(BloodMagicPotions.SOUL_FRAY)))
				{
					if (useIncense)
					{
						double incenseAmount = PlayerSacrificeHelper.getPlayerIncense(player);

						sacrificedHealth = health - maxHealth * healthThreshold;
						lpModifier *= PlayerSacrificeHelper.getModifier(incenseAmount);

						PlayerSacrificeHelper.setPlayerIncense(player, 0);
						player.addEffect(new EffectInstance(BloodMagicPotions.SOUL_FRAY, PlayerSacrificeHelper.soulFrayDuration));
					}

					if (destructiveWill >= destructiveWillDrain * sacrificedHealth)
					{
						lpModifier *= getLPModifierForWill(destructiveWill);
						destructiveWill -= destructiveWillDrain * sacrificedHealth;
						destructiveDrain += destructiveWillDrain * sacrificedHealth;
					}

//					if (LivingArmour.hasFullSet(player))
//					{
//						ItemStack chestStack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
//						LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
//						if (armour != null)
//						{
//							LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.selfSacrifice", chestStack);
//
//							if (upgrade instanceof LivingArmourUpgradeSelfSacrifice)
//							{
//								double modifier = ((LivingArmourUpgradeSelfSacrifice) upgrade).getSacrificeModifier();
//
//								lpModifier *= (1 + modifier);
//							}
//						}
//					}

					player.setHealth(health - sacrificedHealth);
					BloodMagic.packetHandler.sendTo(new SetClientHealthPacket(health - sacrificedHealth), (ServerPlayerEntity) player);

					tileAltar.sacrificialDaggerCall((int) (ConfigHandler.values.sacrificialDaggerConversion * lpModifier * sacrificedHealth), false);

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
	public ITextComponent[] provideInformationOfRitualToPlayer(PlayerEntity player)
	{
		return new ITextComponent[] { new TranslationTextComponent(this.getTranslationKey() + ".info"),
				new TranslationTextComponent(this.getTranslationKey() + ".default.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".corrosive.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".steadfast.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".destructive.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".vengeful.info") };
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
