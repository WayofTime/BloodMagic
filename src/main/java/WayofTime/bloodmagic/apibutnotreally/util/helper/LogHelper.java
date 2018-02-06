package WayofTime.bloodmagic.apibutnotreally.util.helper;

import WayofTime.bloodmagic.apibutnotreally.BloodMagicAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
    private Logger logger;

    public LogHelper(String logger) {
        this.logger = LogManager.getLogger(logger);
    }

    public void info(String info, Object... format) {
        if (BloodMagicAPI.loggingEnabled)
            logger.info(info, format);
    }

    public void error(String error, Object... format) {
        if (BloodMagicAPI.loggingEnabled)
            logger.error(error, format);
    }

    public void debug(String debug, Object... format) {
        if (BloodMagicAPI.loggingEnabled)
            logger.debug(debug, format);
    }

    public void fatal(String fatal, Object... format) {
        logger.error(fatal, format);
    }

    public Logger getLogger() {
        return logger;
    }
}
