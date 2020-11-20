package wayoftime.bloodmagic.incense;

/**
 * Holds the tranquility type and value for valid tranquility modifiers
 */
public class TranquilityStack
{
	public final EnumTranquilityType type;
	public double value;

	public TranquilityStack(EnumTranquilityType type, double value)
	{
		this.type = type;
		this.value = value;
	}
}
