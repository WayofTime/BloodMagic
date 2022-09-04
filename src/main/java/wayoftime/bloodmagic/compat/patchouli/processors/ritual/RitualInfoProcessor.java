package wayoftime.bloodmagic.compat.patchouli.processors.ritual;

import java.util.Map;

import net.minecraft.util.Tuple;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.util.helper.RitualHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;

/*
 * Example Page
 * 
 * {
 *   "type": "bloodmagic:ritual_info",    // Corresponding Template.
 *   "ritual": "ritual_id",    // Ritual ID set in-code by: @RitualRegister("ritual_id")
 *   "text_overrides": [    // See note below.
 *     ["text 1" , "formatting_code_1"],
 *     ["text 2" , "formatting_code_2"]
 *   ],
 *   "text": "Extra text."    // (Optional) Adds extra text below rest of entry.
 * },
 * 
 * Text Overrides: used to add Patchouli formatting codes to the auto-filled text.
 *   Enter the Text to be formatted in the first entry.  The entire entry must be full word or words.
 *   The formatting codes will have the Patchouli formatting code symbols, $(code), added automatically.
 */

public class RitualInfoProcessor extends RitualProcessorCore
{
	// protected Ritual ritual; - is set in RitualProcessorCore.setup()

	public void localSetup(IVariableProvider variables)
	{
		// Set Text Overrides Map.
		if (variables.has("text_overrides"))
		{
			setTextOverridesMap(variables.get("text_overrides")); // in RitualProcessorCore
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (ritual == null)
		{
			return null;
		} else if (key.equals("auto_text"))
		{
			return IVariable.wrap(generateAutoText());
		} else
			return null;
	}

	private String generateAutoText()
	{
		// Constants to save some repetitive typing.
		final String LANG_DIVINER = "tooltip.bloodmagic.diviner.";
		final String LANG_GUIDE = "guide.patchouli.bloodmagic.ritual_info.";

		// info.
		String info = TextHelper.localize(String.format("%s%s", this.ritual.getTranslationKey(), ".info"));
		info = applyTextOverridesTo(info); // in RitualProcessorCore

		// Rune counts.
		StringBuilder runeCounts = new StringBuilder();
		Tuple<Integer, Map<EnumRuneType, Integer>> runeCounter = RitualHelper.countRunes(ritual);
		Map<EnumRuneType, Integer> runeMap = runeCounter.getB();
		for (EnumRuneType type : EnumRuneType.values())
		{
			int count = runeMap.getOrDefault(type, 0);
			if (count > 0)
			{
				runeCounts.append(TextHelper.localize(LANG_GUIDE + "counter_formatter", type.patchouliColor, TextHelper.localize(LANG_DIVINER + type.translationKey, count)));
			}
		}

		// Total Rune count.
		int totalRunes = runeCounter.getA();
		String totalRuneCount = TextHelper.localize(LANG_DIVINER + "totalRune", totalRunes);

		// Activation Crystal level.
		String crystalLevel;
		switch (ritual.getCrystalLevel())
		{
		case 0:
			crystalLevel = TextHelper.localize(LANG_GUIDE + "weak_activation_crystal_link", TextHelper.localize("item.bloodmagic.activationcrystalweak"));
			break;
		case 1:
			crystalLevel = TextHelper.localize(LANG_GUIDE + "awakened_activation_crystal_link", TextHelper.localize("item.bloodmagic.activationcrystalawakened"));
			break;
		default:
			crystalLevel = TextHelper.localize("item.bloodmagic.activationcrystalcreative");
		}

		// Activation Cost
		String activationCost = TextHelper.localize(LANG_GUIDE + "activation_cost", ritual.getActivationCost());

		// Upkeep Cost and Interval.
		String upkeepCost = "";
		if (ritual.getRefreshCost() != 0)
		{
			upkeepCost = TextHelper.localize(LANG_GUIDE + "upkeep_cost", ritual.getRefreshCost(), ritual.getRefreshTime());
		}

		return TextHelper.localize(LANG_GUIDE + "info_formatter", info, runeCounts.toString(), totalRuneCount, crystalLevel, activationCost, upkeepCost);
	}

}
