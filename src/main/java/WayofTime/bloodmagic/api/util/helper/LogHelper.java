package WayofTime.bloodmagic.api.util.helper;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {

    private Logger logger;

    public LogHelper(String logger) {
        this.logger = LogManager.getLogger(logger);
    }

    public void info(Object info) {
        if (BloodMagicAPI.isLoggingEnabled())
            logger.info(info);
    }

    public void error(Object error) {
        if (BloodMagicAPI.isLoggingEnabled())
            logger.info(error);
    }

    public void debug(Object debug) {
        if (BloodMagicAPI.isLoggingEnabled())
            logger.info(debug);
    }

    public void fatal(Object fatal) {
        logger.fatal(fatal);
    }

    public Logger getLogger() {
        return logger;
    }
}
