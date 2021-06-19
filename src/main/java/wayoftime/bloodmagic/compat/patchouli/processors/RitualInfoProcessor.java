package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.Map;

import org.apache.logging.log4j.LogManager;

import net.minecraft.util.Tuple;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.ItemRitualDiviner;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.util.helper.RitualHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class RitualInfoProcessor implements IComponentProcessor
{
	private Ritual ritual;

	@Override
	public void setup(IVariableProvider variables)
	{
		String id = variables.get("ritual").asString();
		ritual = BloodMagic.RITUAL_MANAGER.getRitual(id);
		if (ritual == null)
		{
			LogManager.getLogger().warn("Guidebook given invalid Ritual ID {}", id);
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (ritual == null)
		{
			return null;
		}
		if (key.equals("auto_text"))
		{
			final String TEXT_BASE = ItemRitualDiviner.tooltipBase; // Use the Ritual Diviner's text.

			String output = TextHelper.localize(ritual.getTranslationKey() + ".info") + "$(br2)";

			Tuple<Integer, Map<EnumRuneType, Integer>> runeCount = RitualHelper.countRunes(ritual);
			int totalRunes = runeCount.getA();
			Map<EnumRuneType, Integer> runeMap = runeCount.getB();
			for (EnumRuneType type : EnumRuneType.values())
			{
				int count = runeMap.get(type);
				if (count > 0)
				{
					output += type.patchouliColor + TextHelper.localize(TEXT_BASE + type.translationKey, count) + "$()$(br)";
				}
			}

			output += "$(br2)" + TextHelper.localize(TEXT_BASE + "totalRune", totalRunes) + "$(br)";
			switch (ritual.getCrystalLevel())
			{
			case 0:
				output += TextHelper.localize("patchouli.bloodmagic.ritual_info.weak_activation_crystal_link", TextHelper.localize("item.bloodmagic.activationcrystalweak"));
				break;
			case 1:
				output += TextHelper.localize("patchouli.bloodmagic.ritual_info.awakened_activation_crystal_link", TextHelper.localize("item.bloodmagic.activationcrystalawakened"));
				break;
			default:
				output += TextHelper.localize("item.bloodmagic.activationcrystalcreative") + "$(br)";
			}
			output += TextHelper.localize("patchouli.bloodmagic.ritual_info.activation_cost", ritual.getActivationCost());
			if (ritual.getRefreshCost() != 0)
			{
				output += TextHelper.localize("patchouli.bloodmagic.ritual_info.upkeep_cost", ritual.getRefreshCost(), ritual.getRefreshTime());
			}

			return IVariable.wrap(output);
		}
		return null;
	}
}
