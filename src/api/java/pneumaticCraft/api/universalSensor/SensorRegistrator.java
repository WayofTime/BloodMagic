package pneumaticCraft.api.universalSensor;

/**
 * With this class you can register your own sensors.
 */
public class SensorRegistrator{
    /**
     * This field will be initialized in the PreInit phase of PneumaticCraft's loading phase.
     * With this field you can register every Universal Sensor sensor you want. Just pass a new instance
     * to one of the registerSensor methods. Sensors are singletons.
     */
    public static ISensorRegistrator sensorRegistrator;

    public static interface ISensorRegistrator{
        /**
         * Registry for IPollSensorSetting, EntityPollSensor and IEventSensorSetting, and any other instance of ISensorSetting.
         * @param sensor
         */
        public void registerSensor(ISensorSetting sensor);

        /**
         * Registry for IBlockAndCoordinateEventSensor
         * @param sensor
         */
        public void registerSensor(IBlockAndCoordinateEventSensor sensor);

        /**
         * Registry for IBlockAndCoordinatePollSensor
         * @param sensor
         */
        public void registerSensor(IBlockAndCoordinatePollSensor sensor);
    }
}
