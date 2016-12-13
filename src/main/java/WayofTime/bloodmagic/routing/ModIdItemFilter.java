package WayofTime.bloodmagic.routing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameData;

public class ModIdItemFilter extends TestItemFilter
{
    @Override
    public boolean doStacksMatch(ItemStack filterStack, ItemStack testStack)
    {
        if (ItemStack.areItemsEqualIgnoreDurability(filterStack, testStack))
        {
            String keyId = getModID(filterStack.getItem());
            String checkedId = getModID(testStack.getItem());
            return keyId.equals(checkedId);
        }

        return false;
    }

    public String getModID(Item item)
    {
        ResourceLocation resource = ForgeRegistries.ITEMS.getKey(item);
        return resource.getResourceDomain();
    }
}
