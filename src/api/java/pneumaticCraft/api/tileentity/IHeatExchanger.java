package pneumaticCraft.api.tileentity;

import net.minecraftforge.common.util.ForgeDirection;
import pneumaticCraft.api.IHeatExchangerLogic;

/**
 * Implemented by TileEntities or Blocks which transport heat. Keep in mind that when a Block is implementing it you only can give off a constant
 * resistance/temperature (like Lava and Ice).
 * @author MineMaarten
 * www.minemaarten.com
 */
public interface IHeatExchanger{

    /**
     * Get an instance of IHeatExchangerLogic from PneumaticRegistry.getInstance().getHeatExchangerLogic() and keep a global reference.
     * Then return it in this method. You can return different exchanger logics for different sides. Keep in mind that when you change
     * a returned logic, you need to create a neighbor block change to notify the differences. You can return null to indicate no heat can
     * be exchanged on that side.
     * @param side
     * @return
     */
    public IHeatExchangerLogic getHeatExchangerLogic(ForgeDirection side);

}
