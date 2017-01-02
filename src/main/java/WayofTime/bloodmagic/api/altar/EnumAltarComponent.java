package WayofTime.bloodmagic.api.altar;

import java.util.Locale;

import lombok.Getter;

/**
 * List of different components used to construct different tiers of altars.
 */
@Getter
public enum EnumAltarComponent
{
    GLOWSTONE,
    BLOODSTONE,
    BEACON,
    BLOODRUNE,
    CRYSTAL,
    NOTAIR;

    private static final String BASE = "chat.bloodmagic.altar.comp.";
    private String key;

    EnumAltarComponent()
    {
        this.key = BASE + name().toLowerCase(Locale.ENGLISH);
    }
}
