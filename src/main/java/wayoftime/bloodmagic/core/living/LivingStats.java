package wayoftime.bloodmagic.core.living;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;

public class LivingStats
{

	public static final int DEFAULT_UPGRADE_POINTS = 100;

	private final Map<LivingUpgrade, Double> upgrades;
	private int maxPoints = DEFAULT_UPGRADE_POINTS;

	public LivingStats(Map<LivingUpgrade, Double> upgrades)
	{
		this.upgrades = upgrades;
	}

	public LivingStats()
	{
		this(Maps.newHashMap());
	}

	public Map<LivingUpgrade, Double> getUpgrades()
	{
		return ImmutableMap.copyOf(upgrades);
	}

	public LivingStats addExperience(ResourceLocation key, double experience)
	{
//		LivingUpgrade upgrade = LivingArmorRegistrar.UPGRADES.getOrDefault(key);
		LivingUpgrade upgrade = LivingArmorRegistrar.UPGRADE_MAP.getOrDefault(key, LivingUpgrade.DUMMY);
		double current = upgrades.getOrDefault(upgrade, 0d);

//		System.out.println("Upgrade: " + upgrade);

		if (upgrade.getNextRequirement((int) current) == 0)
			return this;

		upgrades.put(upgrade, current + experience);
		return this;
	}

	public int getLevel(ResourceLocation key)
	{
		LivingUpgrade upgrade = LivingArmorRegistrar.UPGRADE_MAP.getOrDefault(key, LivingUpgrade.DUMMY);
//		LivingUpgrade upgrade = LivingArmorRegistrar.UPGRADES.getOrDefault(key);
		return upgrade.getLevel(upgrades.getOrDefault(upgrade, 0d).intValue());
	}

	public int getUsedPoints()
	{
		int total = 0;
		for (Map.Entry<LivingUpgrade, Double> applied : upgrades.entrySet())
		{
			double experience = applied.getValue();
			int level = applied.getKey().getLevel((int) experience);
			int cost = applied.getKey().getLevelCost(level);
			total += cost;
		}

		return total;
	}

	public int getMaxPoints()
	{
		return maxPoints;
	}

	public LivingStats setMaxPoints(int maxPoints)
	{
		this.maxPoints = maxPoints;
		return this;
	}

	public CompoundNBT serialize()
	{
		CompoundNBT compound = new CompoundNBT();
		ListNBT statList = new ListNBT();
		upgrades.forEach((k, v) -> {
			CompoundNBT upgrade = new CompoundNBT();
			upgrade.putString("key", k.getKey().toString());
			upgrade.putDouble("exp", v);
			statList.add(upgrade);
		});
		compound.put("upgrades", statList);

		compound.putInt("maxPoints", maxPoints);

		return compound;
	}

	public void deserialize(CompoundNBT nbt)
	{
		ListNBT statList = nbt.getList("upgrades", 10);
		statList.forEach(tag -> {
			if (!(tag instanceof CompoundNBT))
				return;

			LivingUpgrade upgrade = LivingArmorRegistrar.UPGRADE_MAP.getOrDefault(new ResourceLocation(((CompoundNBT) tag).getString("key")), LivingUpgrade.DUMMY);
			if (upgrade == LivingUpgrade.DUMMY)
				return;
			double experience = ((CompoundNBT) tag).getDouble("exp");
			upgrades.put(upgrade, experience);
		});

		maxPoints = nbt.getInt("maxPoints");
	}

	public static LivingStats fromNBT(CompoundNBT statTag)
	{
		LivingStats stats = new LivingStats();
		stats.deserialize(statTag);
		return stats;
	}

	public static LivingStats fromPlayer(PlayerEntity player)
	{
		return fromPlayer(player, false);
	}

	public static LivingStats fromPlayer(PlayerEntity player, boolean createNew)
	{
		if (!LivingUtil.hasFullSet(player))
			return null;

		ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
		LivingStats stats = ((ILivingContainer) chest.getItem()).getLivingStats(chest);
		return stats == null && createNew ? new LivingStats() : stats;
	}

	public static void toPlayer(PlayerEntity player, LivingStats stats)
	{
		if (!LivingUtil.hasFullSet(player))
			return;

		ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
		((ILivingContainer) chest.getItem()).updateLivingStats(chest, stats);
	}
}
