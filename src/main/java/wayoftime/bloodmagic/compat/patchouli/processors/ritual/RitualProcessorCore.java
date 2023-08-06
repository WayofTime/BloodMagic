package wayoftime.bloodmagic.compat.patchouli.processors.ritual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.util.helper.TextHelper;

public abstract class RitualProcessorCore implements IComponentProcessor
{
	protected Ritual ritual; // Ritual ID.
	private Map<String, String> textOverrides = new HashMap<String, String>();

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
		localSetup(variables);
	}

	public abstract void localSetup(IVariableProvider variables);

	protected String applyTextOverridesTo(String string)
	{
		for (String text : this.textOverrides.keySet())
		{
			String formatCode = this.textOverrides.get(text);
			string = string.replaceAll(String.format("\\b%s\\b", text), TextHelper.localize("guide.patchouli.bloodmagic.ritual_info.text_override_formatter", formatCode, text));
		}
		return string;
	}

	protected void setTextOverridesMap(IVariable variable)
	{
		List<IVariable> overridesList = variable.asList();
		for (IVariable override : overridesList)
		{
			List<String> string = new ArrayList<String>();
			override.asStream().forEach((a) -> string.add(a.asString()));
			this.textOverrides.put(string.get(0), string.get(1));
		}
	}

}
