package com.wayoftime.bloodmagic.core;

import com.wayoftime.bloodmagic.BloodMagic;
import net.minecraftforge.common.config.Config;

@Config(modid = BloodMagic.MODID, name = BloodMagic.MODID + "/" + BloodMagic.MODID, category = "")
public class BloodMagicConfiguration {

    public static LoggingConfig logging = new LoggingConfig();

    public static class LoggingConfig {
        @Config.Comment("Prints information like plugin detection and how long it takes plugins to load their various stages.")
        public boolean enableApiLogging = true;
        @Config.Comment("Extremely verbose logging for things like recipe addition.")
        public boolean enableVerboseApiLogging;
        @Config.Comment("Debug printing that may help with debugging certain issues.")
        public boolean enableDebugLogging;
    }
}
