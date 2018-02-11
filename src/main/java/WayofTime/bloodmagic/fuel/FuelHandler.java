package WayofTime.bloodmagic.fuel;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel != null && fuel.getItem() == RegistrarBloodMagicItems.COMPONENT && fuel.getMetadata() == ComponentTypes.SAND_COAL.ordinal()) {
            return 1600;
        }

        return 0;
    }
}
