package WayofTime.bloodmagic.fuel;

import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.registry.RegistrarBloodMagicItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler
{
    @Override
    public int getBurnTime(ItemStack fuel)
    {
        if (fuel != null && fuel.getItem() == RegistrarBloodMagicItems.ITEM_COMPONENT && fuel.getMetadata() == ItemComponent.getStack(ItemComponent.SAND_COAL).getMetadata())
        {
            return 1600;
        }

        return 0;
    }
}
