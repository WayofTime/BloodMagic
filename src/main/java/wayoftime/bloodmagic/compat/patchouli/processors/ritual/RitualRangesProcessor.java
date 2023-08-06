package wayoftime.bloodmagic.compat.patchouli.processors.ritual;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;

import net.minecraft.core.BlockPos;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.util.helper.TextHelper;

/*
 * Example Page
 * 
 * {
 *   "type": "bloodmagic:ritual_ranges",    // Corresponding Template.
 *   "ritual": "ritual_id",    // Ritual ID set in-code by: @RitualRegister("ritual_id")
 *   "ranges": ["area_1", "area_2", "area_3"],    // Page Type keys for the ranges you want.
 *   "text": "Extra text.",    // (Optional) Adds extra text below rest of entry.
 * },
 * 
 * specify the single range "*" to list all of the ranges automatically.
 */

public class RitualRangesProcessor extends RitualProcessorCore
{
	// protected Recipe recipe // set by RitualProcessorCore
	String output = "";
	List<String> typeKeys = new ArrayList<String>();

	public void localSetup(IVariableProvider variables)
	{
		// Get the range key names.
		if (variables.has("ranges"))
		{
			variables.get("ranges").asStream().forEach(e -> typeKeys.add(e.asString()));
		} else
		{
			LogManager.getLogger().warn("Guidebook missing \"ranges\" for {}'s Ranges Page", ritual.getName());
			this.ritual = null; // blank out the page.
			return;
		}
		if (typeKeys.contains("*")) // our "get all ranges" flag.
		{
			typeKeys = ritual.getListOfRanges(); // replace typeKeys with a list of all ranges.
		}

		// gather each one's data and add it to the output.
		for (String key : typeKeys)
		{
			// Get Title
			String title;
			String text = TextHelper.localize(ritual.getTranslationKey() + "." + key + ".info");
			if (text.charAt(0) == '(')
			{
				title = TextHelper.localize("guide.patchouli.bloodmagic.common.header", text.substring(1, text.indexOf(")")));
			} else
			{
				title = String.format("$(bold)%s$()$(br)", key);
				LogManager.getLogger().warn("Guidebook missing localization for {}'s \"{}\" area title", ritual.getName(), key);
			}

			// Get Ranges and Center
			AreaDescriptor areaDescriptor = ritual.getBlockRange(key);
			if (areaDescriptor instanceof AreaDescriptor.Rectangle)
			{
				AreaDescriptor.Rectangle rectangle = (AreaDescriptor.Rectangle) areaDescriptor;
				output += title;

				BlockPos minOffset = rectangle.getMinimumOffset();
				BlockPos maxOffset = rectangle.getMaximumOffset();

				int deltaX = maxOffset.getX() - minOffset.getX(); // Horizontal
				int deltaY = maxOffset.getY() - minOffset.getY(); // Height
				int deltaZ = maxOffset.getZ() - minOffset.getZ(); // Horizontal

				// Block (0, 0, 0)'s center is actually at (0.5, 0.5, 0.5)
				int centerX = Math.round(((float) (maxOffset.getX() + minOffset.getX()) / 2) - (float) 0.5);
				int centerY = Math.round(((float) (maxOffset.getY() + minOffset.getY()) / 2) - (float) 0.5);
				int centerZ = Math.round(((float) (maxOffset.getZ() + minOffset.getZ()) / 2) - (float) 0.5);

				if (centerX == 0 && centerZ == 0)
				{
					if (centerY == 0) // at MRS
					{
						output += TextHelper.localize("guide.patchouli.bloodmagic.ritual_ranges.at_mrs", deltaX, deltaY, deltaZ);
					} else if (centerY > 0) // Above MRS
					{
						output += TextHelper.localize("guide.patchouli.bloodmagic.ritual_ranges.above_mrs", deltaX, deltaY, deltaZ, centerY);
					} else // Below MRS
					{
						output += TextHelper.localize("guide.patchouli.bloodmagic.ritual_ranges.below_mrs", deltaX, deltaY, deltaZ, -centerY);
					}

				} else // X or Z offset not 0
				{
					output += TextHelper.localize("guide.patchouli.bloodmagic.ritual_ranges.away_from_mrs", deltaX, deltaY, deltaZ, centerX, centerY, centerZ);
				}

			}
			// Skip any ritual area using a non-Rectangular shape.
			// can be added later if any are ever made.
		}

		if (variables.has("text"))
		{
			output += variables.get("text").asString();
		}
	}

	@Override
	public IVariable process(String key)
	{
		if (this.ritual == null)
		{
			return null;
		}
		if (key.equals("auto_text"))
		{
			return IVariable.wrap(this.output);
		}
		return null;
	}

}
