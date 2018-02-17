package WayofTime.bloodmagic.util;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum BMLog {

    DEFAULT(BloodMagic.MODID) {
        @Override
        boolean enabled() {
            return true;
        }
    },
    DEBUG() {
        @Override
        boolean enabled() {
            return ConfigHandler.general.enableDebugLogging;
        }
    },
    API() {
        @Override
        boolean enabled() {
            return ConfigHandler.general.enableAPILogging;
        }
    },
    API_VERBOSE() {
        @Override
        boolean enabled() {
            return ConfigHandler.general.enableVerboseAPILogging;
        }
    },
    ;

    private final Logger logger;

    BMLog(String logName) {
        logger = LogManager.getLogger(logName);
    }

    BMLog() {
        logger = LogManager.getLogger(BloodMagic.MODID + "|" + WordUtils.capitalizeFully(name().replace("_", " ")));
    }

    abstract boolean enabled();

    public void info(String input, Object... args) {
        if (enabled())
            logger.info(input, args);
    }

    public void error(String input, Object... args) {
        if (enabled())
            logger.error(input, args);
    }

    public void warn(String input, Object... args) {
        if (enabled())
            logger.warn(input, args);
    }
}
