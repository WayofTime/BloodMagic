package WayofTime.bloodmagic.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public enum EnumServerDyeColor implements IStringSerializable {
    WHITE(0, 15, "white", "white", 16383998, TextFormatting.WHITE),
    ORANGE(1, 14, "orange", "orange", 16351261, TextFormatting.GOLD),
    MAGENTA(2, 13, "magenta", "magenta", 13061821, TextFormatting.AQUA),
    LIGHT_BLUE(3, 12, "light_blue", "lightBlue", 3847130, TextFormatting.BLUE),
    YELLOW(4, 11, "yellow", "yellow", 16701501, TextFormatting.YELLOW),
    LIME(5, 10, "lime", "lime", 8439583, TextFormatting.GREEN),
    PINK(6, 9, "pink", "pink", 15961002, TextFormatting.LIGHT_PURPLE),
    GRAY(7, 8, "gray", "gray", 4673362, TextFormatting.DARK_GRAY),
    SILVER(8, 7, "silver", "silver", 10329495, TextFormatting.GRAY),
    CYAN(9, 6, "cyan", "cyan", 1481884, TextFormatting.DARK_AQUA),
    PURPLE(10, 5, "purple", "purple", 8991416, TextFormatting.DARK_PURPLE),
    BLUE(11, 4, "blue", "blue", 3949738, TextFormatting.DARK_BLUE),
    BROWN(12, 3, "brown", "brown", 8606770, TextFormatting.GOLD),
    GREEN(13, 2, "green", "green", 6192150, TextFormatting.DARK_GREEN),
    RED(14, 1, "red", "red", 11546150, TextFormatting.DARK_RED),
    BLACK(15, 0, "black", "black", 1908001, TextFormatting.BLACK);

    private static final EnumServerDyeColor[] META_LOOKUP = new EnumServerDyeColor[values().length];
    private static final EnumServerDyeColor[] DYE_DMG_LOOKUP = new EnumServerDyeColor[values().length];
    private final int meta;
    private final int dyeDamage;
    private final String name;
    private final String unlocalizedName;
    private final int colorValue;
    private final float[] colorComponentValues;
    private final TextFormatting chatColor;

    private EnumServerDyeColor(int p_i47505_3_, int p_i47505_4_, String p_i47505_5_, String p_i47505_6_, int p_i47505_7_, TextFormatting p_i47505_8_) {
        this.meta = p_i47505_3_;
        this.dyeDamage = p_i47505_4_;
        this.name = p_i47505_5_;
        this.unlocalizedName = p_i47505_6_;
        this.colorValue = p_i47505_7_;
        this.chatColor = p_i47505_8_;
        int lvt_9_1_ = (p_i47505_7_ & 16711680) >> 16;
        int lvt_10_1_ = (p_i47505_7_ & '\uff00') >> 8;
        int lvt_11_1_ = (p_i47505_7_ & 255) >> 0;
        this.colorComponentValues = new float[]{(float)lvt_9_1_ / 255.0F, (float)lvt_10_1_ / 255.0F, (float)lvt_11_1_ / 255.0F};
    }

    public int getMetadata() {
        return this.meta;
    }

    public int getDyeDamage() {
        return this.dyeDamage;
    }

    public String getDyeColorName() {
        return this.name;
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public int getColorValue() {
        return this.colorValue;
    }

    public float[] getColorComponentValues() {
        return this.colorComponentValues;
    }

    public static EnumServerDyeColor byDyeDamage(int p_byDyeDamage_0_) {
        if (p_byDyeDamage_0_ < 0 || p_byDyeDamage_0_ >= DYE_DMG_LOOKUP.length) {
            p_byDyeDamage_0_ = 0;
        }

        return DYE_DMG_LOOKUP[p_byDyeDamage_0_];
    }

    public static EnumServerDyeColor byMetadata(int p_byMetadata_0_) {
        if (p_byMetadata_0_ < 0 || p_byMetadata_0_ >= META_LOOKUP.length) {
            p_byMetadata_0_ = 0;
        }

        return META_LOOKUP[p_byMetadata_0_];
    }

    public String toString() {
        return this.unlocalizedName;
    }

    public String getName() {
        return this.name;
    }

    public static EnumServerDyeColor byBaseColor(ItemStack stack){
        return EnumServerDyeColor.byDyeDamage(stack.getMetadata() & 15);
    }

    static {
        EnumServerDyeColor[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            EnumServerDyeColor lvt_3_1_ = var0[var2];
            META_LOOKUP[lvt_3_1_.getMetadata()] = lvt_3_1_;
            DYE_DMG_LOOKUP[lvt_3_1_.getDyeDamage()] = lvt_3_1_;
        }

    }
}
