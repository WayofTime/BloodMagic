package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.ArrayList;
import java.util.List;
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

/*
 * Example Page:
 * 
 * {
 *   "type": "bloodmagic:ritual_info",    // Corresponding Template.
 *   "ritual": "ritual_id",    // Ritual ID set in-code by: @RitualRegister("ritual_id")
 *   "text_overrides": [    // <See note below.>
 *     ["text 1" , "formatting_code_1"],
 *     ["text 2" , "formatting_code_2"]
 *   ],
 *   "text": "Extra text."    // (Optional) Adds extra text below rest of entry.
 * },
 * 
 * Text overrides is used to add Patchouli formatting codes to the Ritual Diviner's text.
 * Enter the Text to be formatted in the first entry.  The entire entry must be full word or words.
 * The formatting codes will have the Patchouli formatting code symbols, $(code), are added automatically.
 */

public class RitualInfoProcessor implements IComponentProcessor
{
	private Ritual ritual; // Ritual ID.
	private String extraText = ""; // (Optional) Text to insert at the end of the entry.
	private List<List<String>> overrideTable = new ArrayList<List<String>>();
	// (Optional) list of text formatting overrides.

	@Override
	public void setup(IVariableProvider variables)
	{
		String id = variables.get("ritual").asString();
		ritual = BloodMagic.RITUAL_MANAGER.getRitual(id);
		if (ritual == null)
		{
			LogManager.getLogger().warn("Guidebook given invalid Ritual ID {}", id);
		}

		if (variables.has("text_overrides"))
		{
			List<IVariable> varOverridePairs = variables.get("text_overrides").asList();
			for (IVariable varPair : varOverridePairs)
			{
				List<String> pair = new ArrayList<String>();
				varPair.asStream().forEach(p -> pair.add(p.asString()));
				overrideTable.add(pair);
			}
		}

		if (variables.has("text"))
		{
			extraText = variables.get("text").asString();
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
			final String LANGUAGE_BASE = "patchouli.bloodmagic.ritual_info.";
			final String DIVINER_BASE = ItemRitualDiviner.tooltipBase; // Use the Ritual Diviner's text.

			String infoBlurb = TextHelper.localize(ritual.getTranslationKey() + ".info");
			if (!overrideTable.isEmpty())
			{
				for (List<String> pair : overrideTable)
				{
					String text = pair.get(0);
					String code = pair.get(1);
					infoBlurb = infoBlurb.replaceAll(String.format("\\b%s\\b", text), TextHelper.localize("patchouli.bloodmagic.ritual_info.text_override_formatter", code, text));
				}
			}

			StringBuilder runeCounts = new StringBuilder();
			Tuple<Integer, Map<EnumRuneType, Integer>> runeCounter = RitualHelper.countRunes(ritual);
			int totalRunes = runeCounter.getA();
			Map<EnumRuneType, Integer> runeMap = runeCounter.getB();
			for (EnumRuneType type : EnumRuneType.values())
			{
				int count = runeMap.getOrDefault(type, 0);
				if (count > 0)
				{
					runeCounts.append(TextHelper.localize(LANGUAGE_BASE + "counter_formatter", type.patchouliColor, TextHelper.localize(DIVINER_BASE + type.translationKey, count)));
				}
			}

			String totalRuneCount = TextHelper.localize(DIVINER_BASE + "totalRune", totalRunes);

			String crystalLevel;
			switch (ritual.getCrystalLevel())
			{
			case 0:
				crystalLevel = TextHelper.localize(LANGUAGE_BASE + "weak_activation_crystal_link", TextHelper.localize("item.bloodmagic.activationcrystalweak"));
				break;
			case 1:
				crystalLevel = TextHelper.localize(LANGUAGE_BASE + "awakened_activation_crystal_link", TextHelper.localize("item.bloodmagic.activationcrystalawakened"));
				break;
			default:
				crystalLevel = TextHelper.localize("item.bloodmagic.activationcrystalcreative");
			}

			String activationCost = TextHelper.localize(LANGUAGE_BASE + "activation_cost", ritual.getActivationCost());

			String upkeepCost = "";
			if (ritual.getRefreshCost() != 0)
			{
				upkeepCost = TextHelper.localize(LANGUAGE_BASE + "upkeep_cost", ritual.getRefreshCost(), ritual.getRefreshTime());
			}

			return IVariable.wrap(TextHelper.localize(LANGUAGE_BASE + "output_formatter", infoBlurb, runeCounts.toString(), totalRuneCount, crystalLevel, activationCost, upkeepCost, extraText));
		}
		return null;
	}
}
