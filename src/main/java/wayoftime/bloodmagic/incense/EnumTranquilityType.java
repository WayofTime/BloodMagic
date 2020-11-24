package wayoftime.bloodmagic.incense;

public enum EnumTranquilityType
{
	PLANT(),
	CROP(),
	TREE(),
	EARTHEN(),
	WATER(),
	FIRE(),
	LAVA(),;

	public static EnumTranquilityType getType(String type)
	{
		for (EnumTranquilityType t : values())
		{
			if (t.name().equalsIgnoreCase(type))
			{
				return t;
			}
		}

		return null;
	}
}