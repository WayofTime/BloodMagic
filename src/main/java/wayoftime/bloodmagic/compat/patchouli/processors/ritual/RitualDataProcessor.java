package wayoftime.bloodmagic.compat.patchouli.processors.ritual;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;

import net.minecraft.world.item.ItemStack;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.util.helper.TextHelper;
import wayoftime.bloodmagic.will.DemonWillHolder;

/*
 * Example Page
 * 
 * {
 *   "type": "bloodmagic:ritual_data",    // Corresponding Template.
 *   "ritual": "ritual_id",    // Ritual ID set in-code by: @RitualRegister("ritual_id"),
 *   "page_type": "page_type_key"  // See note below
 *   "text_overrides": [    // See note below
 *     ["text 1" , "formatting_code_1"],
 *     ["text 2" , "formatting_code_2"]
 *   ],
 *   "heading_override": "heading"    // (Optional) Overwrites page heading.
 *   "item_override": "item_id"    // (Optional) Overwrites the item selected for the Spotlight page.
 *   "text": "Extra text."    // (Optional) Adds extra text below rest of entry.
 * },
 * 
 * 
 * Page Type: This determines what kind of information the page looks for to auto-fill.
 *   * "raw" - Raw Will augment.  Addresses the "default" name in language files, and gets the Raw Will Crystal item.
 *   * "corrosive", "destructive", "steadfast", "vengeful" - Will Augments.  Shows the relevant Will Crystal item.
 *   Any other input will attempt to pull description and range info using the input provided, and use the Ritual Tinkerer as the item.
 *   For example, using "heal" will find the "ritual.bloodmagic.regenerationRitual.heal.info" entry, which starts 
 *   with "(Healing)" so this will strip that out of the text and set "Healing" as the heading.
 *   
 * Text Overrides: used to add Patchouli formatting codes to the auto-filled text.
 *   Enter the Text to be formatted in the first entry.  The entire entry must be full word or words.
 *   The formatting codes will have the Patchouli formatting code symbols, $(code), added automatically.
 */

public class RitualDataProcessor extends RitualProcessorCore
{
	// protected Recipe recipe // set by RitualProcessorCore
	private String pageType;
	private String heading = "";
	private ItemStack item = new ItemStack(BloodMagicItems.RITUAL_READER.get());;
	private String text;
	private String extraText = "";

	public void localSetup(IVariableProvider variables)
	{
		// Set Page Type.
		if (variables.has("page_type"))
		{
			this.pageType = variables.get("page_type").asString();
		} else
		{
			LogManager.getLogger().warn("Guidebook missing \"page_type\" for one of {}'s Data Pages", ritual.getName());
			this.ritual = null; // blank out the page.
			return;
		}

		// Set Text Overrides Map.
		if (variables.has("text_overrides"))
		{
			setTextOverridesMap(variables.get("text_overrides")); // In RitualProcessorCore
		}

		// Set Heading Override.
		if (variables.has("heading_override"))
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

		// use pageType to set Item, Text, Heading, and Range.
		Boolean infoAlreadySet = false;
		String rangeInfo = "";
		switch (pageType)
		{
		case "raw":
			text = TextHelper.localize(ritual.getTranslationKey() + ".default.info");
			text = RemoveLeadTitle(text);
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
			rangeInfo = TextHelper.localize("guide.patchouli.bloodmagic.ritual_info.range_formatter", volume == 0
					? TextHelper.localize("guide.patchouli.bloodmagic.ritual_info.full_range")
					: volume, horizontal, vertical);
		}
		if (!infoAlreadySet)
		{
			text = TextHelper.localize(ritual.getTranslationKey() + "." + pageType + ".info");
			text = RemoveLeadTitle(text);

		}
		text += rangeInfo;
		text = applyTextOverridesTo(text);
		text += TextHelper.localize("guide.patchouli.bloodmagic.common.double_new_line", extraText);
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
		case "heading":
			return IVariable.wrap(this.heading);
		case "item":
			return IVariable.from(this.item);
		case "auto_text":
			return IVariable.wrap(this.text);
		}
		return null;
	}

	// For entries that start with "(title) ...", extract the title to use as the
	// default heading, and remove it from the body text.
	private String RemoveLeadTitle(String text)
	{
		if (text.charAt(0) == '(')
		{
			String leadTitle = text.substring(1, text.indexOf(")"));
			text = text.replaceFirst("^\\(" + leadTitle + "\\) ", "");
			if (this.heading.equals(""))
			{
				this.heading = leadTitle;
			}
		}
		return text;
	}

}
