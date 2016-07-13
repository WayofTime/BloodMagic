package WayofTime.bloodmagic.meteor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.ItemStackWrapper;

public class MeteorRegistry
{
    public static Map<ItemStackWrapper, MeteorHolder> meteorMap = new HashMap<ItemStackWrapper, MeteorHolder>();

    public static void registerMeteor(ItemStack stack, MeteorHolder holder)
    {
        ItemStackWrapper wrapper = ItemStackWrapper.getHolder(stack);
        if (wrapper != null)
        {
            meteorMap.put(wrapper, holder);
        }
    }

    public static void registerMeteor(ItemStack stack, List<MeteorComponent> componentList, float explosionStrength, int radius, int maxWeight)
    {
        MeteorHolder holder = new MeteorHolder(componentList, explosionStrength, radius, maxWeight);

        registerMeteor(stack, holder);
    }

    public static boolean hasMeteorForItem(ItemStack stack)
    {
        ItemStackWrapper wrapper = ItemStackWrapper.getHolder(stack);
        return wrapper != null && meteorMap.containsKey(wrapper);
    }

    public static MeteorHolder getMeteorForItem(ItemStack stack)
    {
        ItemStackWrapper wrapper = ItemStackWrapper.getHolder(stack);
        return wrapper != null ? meteorMap.get(wrapper) : null;
    }

    public static void generateMeteorForItem(ItemStack stack, World world, BlockPos pos, IBlockState fillerBlock)
    {
        MeteorHolder holder = getMeteorForItem(stack);
        if (holder != null)
        {
            holder.generateMeteor(world, pos, fillerBlock);
        }
    }
}
