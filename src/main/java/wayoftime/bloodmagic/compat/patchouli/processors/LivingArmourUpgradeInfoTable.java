package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.Map.Entry;

import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.util.helper.TextHelper;

/*
 * Example Page:
 * 
 * {
 *   "type": "bloodmagic:living_armour_upgrade_table",    // Corresponding Template.
 *   "upgrade": "bloodmagic:upgrade_id",    // Upgrade ID set in ????
 *   "text": "Extra text."    // (Optional) Adds extra text below rest of entry.
 * },
 */

public class LivingArmourUpgradeInfoTable implements IComponentProcessor
{
	private ResourceLocation upgradeID;
	private String extraText = ""; // (Optional) Text to insert at the end of the entry.

	@Override
	public void setup(Level level, IVariableProvider variables)
	{
		ResourceLocation id = new ResourceLocation(variables.get("upgrade").asString());
		if (LivingArmorRegistrar.UPGRADE_MAP.containsKey(id))
		{
			this.upgradeID = id;
		} else
		{
			LogManager.getLogger().warn("Guidebook given invalid Living Armour Upgrade ID {}", id);
		}

		if (variables.has("text"))
		{
			extraText = variables.get("text").asString();
		}
	}

	@Override
	public IVariable process(Level level,String key)
	{
		if (this.upgradeID == null)
		{
			return null;
		}
		if (key.equals("table"))
		{
			StringBuilder output = new StringBuilder();
			String i18nLevel = TextHelper.localize("guide.patchouli.bloodmagic.living_armour_upgrade_table.level");
			String i18nUpgradePoints = TextHelper.localize("guide.patchouli.bloodmagic.living_armour_upgrade_table.upgrade_points");

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
						int upgradeLevel = upgrade.getLevel(exp);
						int upgradePoints = upgrade.getLevelCost(upgradeLevel);

						String formatStr = String.format("%s %%%dd: %%%dd %s$(br)", i18nLevel, maxLevelLength, maxUpgradePointsLength, i18nUpgradePoints);
						output.append(String.format(formatStr, upgradeLevel, upgradePoints));
					}
				}
			}

			output.append(String.format("%s%s", "$(br2)", extraText));

			return IVariable.wrap(output.toString());
		}
		return null;
	}
}
