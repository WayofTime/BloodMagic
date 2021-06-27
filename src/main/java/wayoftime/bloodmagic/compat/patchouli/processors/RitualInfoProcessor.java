package wayoftime.bloodmagic.compat.patchouli.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.ItemRitualDiviner;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.util.helper.RitualHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;
import wayoftime.bloodmagic.will.DemonWillHolder;

/*
 * Example Page: Info Page
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
 * The ritual_info template is a blank text page.  The page_type is "info" and filled in automatically.
 * 
 * Text Overrides: used to add Patchouli formatting codes to the auto-filled text.
 * Enter the Text to be formatted in the first entry.  The entire entry must be full word or words.
 * The formatting codes will have the Patchouli formatting code symbols, $(code), added automatically.
 * 
 * ****
 * 
 * Example Page: Data Page
 * 
 * {
 *   "type": "bloodmagic:ritual_data",    // Corresponding Template.
 *   "ritual": "ritual_id",    // Ritual ID set in-code by: @RitualRegister("ritual_id"),
 *   "page_type": "page_type_key"  // See note below
 *   "text_overrides": [    // Same as above
 *     ["text 1" , "formatting_code_1"],
 *     ["text 2" , "formatting_code_2"]
 *   ],
 *   "heading_override": "heading"    // (Optional) Overwrites page heading.
 *   "item_override": "item_id"    // (Optional) Overwrites the item selected for the Spotlight page.
 *   "text": "Extra text."    // (Optional) Adds extra text below rest of entry.
 * },
 * 
 * The ritual_data template recreates the Spotlight page layout.  page_type must be supplied, otherwise it will start filling in info page stuff.
 * 
 * Page Type: This determines what kind of information the page looks for to auto-fill.
 *   * "info" - This is the previous example's page type.  Formatting will not work correctly.  Do not use here!
 *   * "raw" - Raw Will augment.  Addresses the "default" name in language files, and gets the Raw Will Crystal item.
 *   * "corrosive", "destructive", "steadfast", "vengeful" - Will Augments.  Shows the relevant Will Crystal item.
 *   Any other input will attempt to pull description and range info using the input provided, and use the Ritual Tinkerer as the item.
 *   For example, using "heal" will find the "ritual.bloodmagic.regenerationRitual.heal.info" entry, which starts 
 *   with "(Healing)" so this will strip that out of the text and set "Healing" as the heading.
 */

public class RitualInfoProcessor implements IComponentProcessor
{
	private Ritual ritual; // Ritual ID.
	private String pageType; // page type selector key.
	private String extraText = ""; // (Optional) Text to insert at the end of the entry.
	private String heading; // heading (Spotlight Page).
	private ItemStack item = new ItemStack(BloodMagicItems.RITUAL_READER.get()); // Item (Spotlight page).
	private String infoBlurb = ""; // Information text from the language files.
	private final String LANGUAGE_BASE = "patchouli.bloodmagic.ritual_info."; // This Processor's language base.
	private final String DIVINER_BASE = ItemRitualDiviner.tooltipBase; // Ritual Diviner language base.

	@Override
	public void setup(IVariableProvider variables)
	{
		String id = variables.get("ritual").asString();
		ritual = BloodMagic.RITUAL_MANAGER.getRitual(id);
		if (ritual == null)
		{
			LogManager.getLogger().warn("Guidebook given invalid Ritual ID {}", id);
			return;
		}

		if (variables.has("page_type"))
		{
			pageType = variables.get("page_type").asString();
		} else
		{
			pageType = "info"; // if page_type is missing, assume it's an info page.
		}

		// Get, Format, and Set Info Blurb.
		// Also sets default heading and Item.
		Boolean infoAlreadySet = false;
		String rangeInfo = "";
		switch (pageType)
		{
		case "info":
			infoBlurb = TextHelper.localize(ritual.getTranslationKey() + ".info");
			infoAlreadySet = true;
			break;
		case "raw":
			infoBlurb = TextHelper.localize(ritual.getTranslationKey() + ".default.info");
			heading = GetAndRemoveLeadTitle();
			item = new ItemStack(BloodMagicItems.RAW_CRYSTAL.get());
			infoAlreadySet = true;
			break;
		case "corrosive":
			item = new ItemStack(BloodMagicItems.CORROSIVE_CRYSTAL.get());
			break;
		case "destructive":
			item = new ItemStack(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get());
			break;
		case "steadfast":
			item = new ItemStack(BloodMagicItems.STEADFAST_CRYSTAL.get());
			break;
		case "vengeful":
			item = new ItemStack(BloodMagicItems.VENGEFUL_CRYSTAL.get());
			break;
		default:
			final DemonWillHolder emptyHolder = new DemonWillHolder();
			int volume = ritual.getMaxVolumeForRange(pageType, Collections.emptyList(), emptyHolder);
			int horizontal = ritual.getMaxHorizontalRadiusForRange(pageType, Collections.emptyList(), emptyHolder);
			int vertical = ritual.getMaxVerticalRadiusForRange(pageType, Collections.emptyList(), emptyHolder);
			rangeInfo = TextHelper.localize("patchouli.bloodmagic.ritual_info.range_formatter", volume == 0
					? TextHelper.localize("patchouli.bloodmagic.ritual_info.full_range")
					: volume, horizontal, vertical);
		}
		if (!infoAlreadySet)
		{
			infoBlurb = TextHelper.localize(ritual.getTranslationKey() + "." + pageType + ".info");
			heading = GetAndRemoveLeadTitle();
		}
		infoBlurb += rangeInfo;
		if (variables.has("text_overrides"))
		{
			List<IVariable> varOverridePairs = variables.get("text_overrides").asList();
			List<List<String>> overrideTable = new ArrayList<List<String>>();
			for (IVariable varPair : varOverridePairs)
			{
				List<String> pair = new ArrayList<String>();
				varPair.asStream().forEach(p -> pair.add(p.asString()));
				overrideTable.add(pair);
			}
			for (List<String> pair : overrideTable)
			{
				String text = pair.get(0);
				String code = pair.get(1);
				infoBlurb = infoBlurb.replaceAll(String.format("\\b%s\\b", text), TextHelper.localize("patchouli.bloodmagic.ritual_info.text_override_formatter", code, text));
			}
		}

		if (variables.has("heading_override")) // Apply heading Override if applicable.
		{
			heading = variables.get("heading_override").asString();
		}

		if (variables.has("item_override"))// Apply Item Override if applicable.
		{
			item = variables.get("item_override").as(ItemStack.class);
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

		switch (key)
		{
		case "auto_text":
			StringBuilder outputText = new StringBuilder();
			if (pageType.equals("info"))
			{
				outputText.append(infoPageSetup());
			} else
			{
				outputText.append(infoBlurb);
			}
			outputText.append(TextHelper.localize("patchouli.bloodmagic.common.double_new_line", extraText));
			return IVariable.wrap(outputText.toString());

		case "heading":
			return IVariable.wrap(heading);

		case "item":
			return IVariable.from(item);
		}
		return null;
	}

	// For entries that start with "(title) ...", extract the title to use as the
	// default heading, and remove it from the body text.
	private String GetAndRemoveLeadTitle()
	{
		if (infoBlurb.charAt(0) == '(')
		{
			String leadTitle = infoBlurb.substring(1, infoBlurb.indexOf(")"));
			infoBlurb = infoBlurb.replaceFirst("^\\(" + leadTitle + "\\) ", "");
			return leadTitle;
		}
		return "";
	}

	// Sets up the info page information (description, rune counts, crystal,
	// activation cost, and upkeep cost).
	private String infoPageSetup()
	{
		StringBuilder runeCounts = new StringBuilder();
		Tuple<Integer, Map<EnumRuneType, Integer>> runeCounter = RitualHelper.countRunes(ritual);
		Map<EnumRuneType, Integer> runeMap = runeCounter.getB();
		for (EnumRuneType type : EnumRuneType.values())
		{
			int count = runeMap.getOrDefault(type, 0);
			if (count > 0)
			{
				runeCounts.append(TextHelper.localize(LANGUAGE_BASE + "counter_formatter", type.patchouliColor, TextHelper.localize(DIVINER_BASE + type.translationKey, count)));
			}
		}

		int totalRunes = runeCounter.getA();
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

		return TextHelper.localize(LANGUAGE_BASE + "info_formatter", infoBlurb, runeCounts.toString(), totalRuneCount, crystalLevel, activationCost, upkeepCost);
	}
}
