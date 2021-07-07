package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;

import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class LivingArmourUpgradeInfoTable implements IComponentProcessor
{
	private ResourceLocation upgradeID;

	@Override
	public void setup(IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("upgrade").asString());
		if (LivingArmorRegistrar.UPGRADE_MAP.containsKey(id))
		{
			this.upgradeID = id;
		} else
		{
			LogManager.getLogger().warn("Guidebook given invalid Living Armour Upgrade ID {}", id);
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (this.upgradeID == null)
		{
			return null;
		}
		if (key.equals("table"))
		{
			StringBuilder output = new StringBuilder();
			String i18nLevel = TextHelper.localize("patchouli.bloodmagic.living_armour_upgrade_table.level");
			String i18nUpgradePoints = TextHelper.localize("patchouli.bloodmagic.living_armour_upgrade_table.upgrade_points");

			for (Entry<ResourceLocation, LivingUpgrade> entry : LivingArmorRegistrar.UPGRADE_MAP.entrySet())
			{
				if (entry.getKey().equals(this.upgradeID))
				{
					LivingUpgrade upgrade = entry.getValue();
					int maxLevel = upgrade.getLevel(Integer.MAX_VALUE);
					int maxLevelLength = Integer.toString(maxLevel).length();
					int maxUpgradePointsLength = Integer.toString(upgrade.getLevelCost(maxLevel)).length();
					int exp = 0;

					while ((exp = upgrade.getNextRequirement(exp)) != 0)
					{
						int level = upgrade.getLevel(exp);
						int upgradePoints = upgrade.getLevelCost(level);

						String formatStr = String.format("%s %%%dd: %%%dd %s$(br)", i18nLevel, maxLevelLength, maxUpgradePointsLength, i18nUpgradePoints);
						output.append(String.format(formatStr, level, upgradePoints));
					}
				}
			}
			return IVariable.wrap(output.toString());
		}
		return null;
	}
}
