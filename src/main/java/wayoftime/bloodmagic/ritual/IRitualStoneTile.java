package wayoftime.bloodmagic.ritual;

public interface IRitualStoneTile
{
	boolean isRuneType(EnumRuneType runeType);

	EnumRuneType getRuneType();

	void setRuneType(EnumRuneType runeType);
}
