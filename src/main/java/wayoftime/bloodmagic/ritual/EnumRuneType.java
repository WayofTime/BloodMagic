package wayoftime.bloodmagic.ritual;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public enum EnumRuneType implements IStringSerializable
{
	BLANK(TextFormatting.GRAY),
	WATER(TextFormatting.AQUA),
	FIRE(TextFormatting.RED),
	EARTH(TextFormatting.GREEN),
	AIR(TextFormatting.WHITE),
	DUSK(TextFormatting.DARK_GRAY),
	DAWN(TextFormatting.GOLD);

	public final TextFormatting colorCode; // Ritual Diviner's tooltip Color
	public final String translationKey = this.name().toLowerCase(Locale.ROOT) + "Rune"; // Suffix for translation.
	public final String patchouliColor = "$(" + this.name().toLowerCase(Locale.ROOT) + ")"; // Patchouli Guidebook's
																							// color code
	// (set in book.json).

	EnumRuneType(TextFormatting colorCode)
	{
		this.colorCode = colorCode;
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