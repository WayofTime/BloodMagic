package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;

@RitualRegister("upgrade_remove")
public class RitualUpgradeRemove extends Ritual
{
	public static final String CHECK_RANGE = "fillRange";

	public RitualUpgradeRemove()
	{
		super("ritualUpgradeRemove", 0, 25000, "ritual." + BloodMagic.MODID + ".upgradeRemoveRitual");
		addBlockRange(CHECK_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1, 2, 1));
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();

		if (world.isClientSide)
		{
			return;
		}

		BlockPos pos = masterRitualStone.getMasterBlockPos();

		AreaDescriptor checkRange = masterRitualStone.getBlockRange(CHECK_RANGE);

		List<Player> playerList = world.getEntitiesOfClass(Player.class, checkRange.getAABB(pos));

		for (Player player : playerList)
		{
			if (LivingUtil.hasFullSet(player))
			{
				boolean removedUpgrade = false;

				ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
				LivingStats stats = LivingStats.fromPlayer(player);
				if (stats != null)
				{
					Map<LivingUpgrade, Double> upgrades = stats.getUpgrades();

					for (Entry<LivingUpgrade, Double> entry : upgrades.entrySet())
					{
						int exp = entry.getValue().intValue();
						LivingUpgrade upgrade = entry.getKey();
						int level = upgrade.getLevel(exp);
						if (level >= 1)
						{
							ItemStack upgradeStack = new ItemStack(BloodMagicItems.LIVING_TOME.get());
//							int expForLevel = upgrade.getNextRequirement(upgrade.getLevel(exp) - 1);
							((ILivingContainer) BloodMagicItems.LIVING_TOME.get()).updateLivingStats(upgradeStack, new LivingStats().setMaxPoints(upgrade.getLevelCost(exp)).addExperience(upgrade.getKey(), exp));
							ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), upgradeStack);
							world.addFreshEntity(item);
							removedUpgrade = true;
						}
						stats.resetExperience(upgrade.getKey());
					}

//					@SuppressWarnings("unchecked")
//					HashMap<String, LivingArmourUpgrade> upgradeMap = (HashMap<String, LivingArmourUpgrade>) armour.upgradeMap.clone();
//
//					for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet())
//					{
//						LivingArmourUpgrade upgrade = entry.getValue();
//						String upgradeKey = entry.getKey();
//
//						ItemStack upgradeStack = new ItemStack(RegistrarBloodMagicItems.UPGRADE_TOME);
//						LivingUpgrades.setKey(upgradeStack, upgradeKey);
//						LivingUpgrades.setLevel(upgradeStack, upgrade.getUpgradeLevel());
//
//						boolean successful = armour.removeUpgrade(player, upgrade);
//
//						if (successful)
//						{
//							removedUpgrade = true;
//							world.spawnEntity(new ItemEntity(world, player.posX, player.posY, player.posZ, upgradeStack));
//							for (Entry<String, StatTracker> trackerEntry : armour.trackerMap.entrySet())
//							{
//								StatTracker tracker = trackerEntry.getValue();
//								if (tracker != null)
//								{
//									if (tracker.providesUpgrade(upgradeKey))
//									{
//										tracker.resetTracker(); // Resets the tracker if the upgrade corresponding to it
//																// was removed.
//									}
//								}
//							}
//						}
//					}

					if (removedUpgrade)
					{
						LivingStats.toPlayer(player, stats);

						masterRitualStone.setActive(false);

						LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
//						LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
						lightningboltentity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
						lightningboltentity.setVisualOnly(true);
						world.addFreshEntity(lightningboltentity);
					}

				}
			}
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
		return 0;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addCornerRunes(components, 1, 0, EnumRuneType.DUSK);
		addCornerRunes(components, 2, 0, EnumRuneType.FIRE);
		addOffsetRunes(components, 1, 2, 0, EnumRuneType.FIRE);
		addCornerRunes(components, 1, 1, EnumRuneType.WATER);
		addParallelRunes(components, 4, 0, EnumRuneType.EARTH);
		addCornerRunes(components, 1, 3, EnumRuneType.WATER);
		addParallelRunes(components, 1, 4, EnumRuneType.AIR);

		for (int i = 0; i < 4; i++)
		{
			addCornerRunes(components, 3, i, EnumRuneType.EARTH);
		}
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualUpgradeRemove();
	}
}