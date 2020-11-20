package wayoftime.bloodmagic.altar;

import wayoftime.bloodmagic.incense.EnumTranquilityType;

import java.util.Locale;

/**
 * List of different components used to construct different tiers of altars.
 */
public enum ComponentType
{
	GLOWSTONE, BLOODSTONE, BEACON, BLOODRUNE, CRYSTAL, NOTAIR;

	public static final ComponentType[] VALUES = values();
	private static final String BASE = "chat.bloodmagic.altar.comp.";
	private String key;

	ComponentType()
	{
		this.key = BASE + name().toLowerCase(Locale.ENGLISH);
	}

	public String getKey()
	{
		return key;
	}

	public static ComponentType getType(String type)
	{
		for (ComponentType t : values())
		{
			if (t.name().equalsIgnoreCase(type))
			{
				return t;
			}
		}

		return null;
	}
}