package wayoftime.bloodmagic;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {

    public final ModConfigSpec.ConfigValue<Integer> SELF_SACRIFICE_CONVERSION;
    public final ModConfigSpec.ConfigValue<Integer> SACRIFICE_CONVERSION;
    public final ModConfigSpec.ConfigValue<Integer> DEFAULT_UPGRADE_POINTS;
    public final ModConfigSpec.ConfigValue<Integer> EVOLUTION_UPGRADE_POINTS;

    public final ModConfigSpec.ConfigValue<Integer> BASE_ITEM_TRANSFER;
    public final ModConfigSpec.ConfigValue<Integer> UPGRADE_ITEM_TRANSFER;

    public final ModConfigSpec.ConfigValue<Integer> BASE_FLUID_TRANSFER;
    public final ModConfigSpec.ConfigValue<Integer> UPGRADE_FLUID_TRANSFER;

    public final ModConfigSpec.ConfigValue<Integer> MAX_AMOUNT_UPGRADES;
    public final ModConfigSpec.ConfigValue<Integer> MAX_SPEED_UPGRADES;

    protected ServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("Range: > 0 means between " + Integer.MAX_VALUE + " and 0 inclusive");
        SELF_SACRIFICE_CONVERSION = builder.defineInRange("self_sacrifice_conversion", 100, 0, Integer.MAX_VALUE);
        SACRIFICE_CONVERSION = builder.defineInRange("sacrifice_conversion", 25, 0, Integer.MAX_VALUE);
        DEFAULT_UPGRADE_POINTS = builder.defineInRange("default_upgrade_points", 100, 0, Integer.MAX_VALUE);
        EVOLUTION_UPGRADE_POINTS = builder.defineInRange("evolution_upgrade_points", 300, 0, Integer.MAX_VALUE);

        BASE_ITEM_TRANSFER = builder.defineInRange("base_item_transfer", 16, 0, Integer.MAX_VALUE);
        UPGRADE_ITEM_TRANSFER = builder.defineInRange("upgrade_item_transfer", 16, 0, Integer.MAX_VALUE);

        BASE_FLUID_TRANSFER = builder.defineInRange("base_fluid_transfer", 4000, 0, Integer.MAX_VALUE);
        UPGRADE_FLUID_TRANSFER = builder.defineInRange("upgrade_fluid_transfer", 4000, 0, Integer.MAX_VALUE);

        MAX_AMOUNT_UPGRADES = builder.defineInRange("max_amount_upgrades", 9, 0, 64);
        MAX_SPEED_UPGRADES = builder.defineInRange("max_speed_upgrades", 9, 0, 19);
    }
}
