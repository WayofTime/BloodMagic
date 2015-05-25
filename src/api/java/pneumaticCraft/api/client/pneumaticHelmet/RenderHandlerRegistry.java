package pneumaticCraft.api.client.pneumaticHelmet;

import java.util.List;

public class RenderHandlerRegistry{
    /**
     * With this field you can register your render handlers. This field is initialized in the PreInit phase of PneumaticCraft's loading phase.
     */
    public static List<IUpgradeRenderHandler> renderHandlers;
}
