package WayofTime.bloodmagic.api.util.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import WayofTime.bloodmagic.api.BloodMagicAPI;

public class LogHelper
{
    private Logger logger;

    public LogHelper(String logger)
    {
        this.logger = LogManager.getLogger(logger);
    }

    public void info(String info, Object... format)
    {
        if (BloodMagicAPI.isLoggingEnabled())
            logger.info(info, format);
    }

    public void error(String error, Object... format)
    {
        if (BloodMagicAPI.isLoggingEnabled())
            logger.info(error, format);
    }

    public void debug(String debug, Object... format)
    {
        if (BloodMagicAPI.isLoggingEnabled())
            logger.info(debug, format);
    }

    public void fatal(String fatal, Object... format)
    {
        logger.fatal(fatal, format);
    }

    public Logger getLogger()
    {
        return logger;
    }
}
