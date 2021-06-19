package wayoftime.bloodmagic.ritual;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public enum EnumRuneType implements IStringSerializable
{
	BLANK(TextFormatting.GRAY, "blankRune", "$(blank)"),
	WATER(TextFormatting.AQUA, "waterRune", "$(water)"),
	FIRE(TextFormatting.RED, "fireRune", "$(fire)"),
	EARTH(TextFormatting.GREEN, "earthRune", "$(earth)"),
	AIR(TextFormatting.WHITE, "airRune", "$(air)"),
	DUSK(TextFormatting.DARK_GRAY, "duskRune", "$(dusk)"),
	DAWN(TextFormatting.GOLD, "dawnRune", "$(dawn)");

	public final TextFormatting colorCode; // Ritual Diviner's tooltip Color
	public final String translationKey; // Suffix for translation.
	public final String patchouliColor; // Patchouli Guidebook's color code (set in book.json).

	EnumRuneType(TextFormatting colorCode, String translationKey, String patchouliColor)
	{
		this.colorCode = colorCode;
		this.translationKey = translationKey;
		this.patchouliColor = patchouliColor;
	}

	@Override
	public String toString()
	{
		return name().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public String getString()
	{
		return this.toString();
	}

//	@Nonnull
//	public ItemStack getStack(int count)
//	{
//		ItemStack ret = new ItemStack(RegistrarBloodMagicItems.INSCRIPTION_TOOL, count, ordinal());
//		CompoundNBT tag = new CompoundNBT();
//		tag.putInt(Constants.NBT.USES, 10);
//		ret.setTag(tag);
//		return ret;
//	}

	public static EnumRuneType byMetadata(int meta)
	{
		if (meta < 0 || meta >= values().length)
			meta = 0;

		return values()[meta];
	}
}