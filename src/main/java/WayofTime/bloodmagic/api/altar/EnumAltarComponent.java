package WayofTime.bloodmagic.api.altar;

import java.util.Locale;

/**
 * List of different components used to construct different tiers of altars.
 */
public enum EnumAltarComponent {
    GLOWSTONE,
    BLOODSTONE,
    BEACON,
    BLOODRUNE,
    CRYSTAL,
    NOTAIR;

    public static final EnumAltarComponent[] VALUES = values();
    private static final String BASE = "chat.bloodmagic.altar.comp.";
    private String key;

    EnumAltarComponent() {
        this.key = BASE + name().toLowerCase(Locale.ENGLISH);
    }

    public String getKey() {
        return key;
    }
}
