package WayofTime.bloodmagic.meteor;

import WayofTime.bloodmagic.api.ItemStackWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeteorRegistry {
    public static Map<ItemStackWrapper, Meteor> meteorMap = new HashMap<ItemStackWrapper, Meteor>();

    public static void registerMeteor(ItemStack stack, Meteor holder) {
        ItemStackWrapper wrapper = ItemStackWrapper.getHolder(stack);
        if (wrapper != null) {
            meteorMap.put(wrapper, holder);
        }
    }

    public static void registerMeteor(ItemStack stack, List<MeteorComponent> componentList, float explosionStrength, int radius) {
        Meteor holder = new Meteor(stack, componentList, explosionStrength, radius);
        registerMeteor(stack, holder);
    }

    public static boolean hasMeteorForItem(ItemStack stack) {
        ItemStackWrapper wrapper = ItemStackWrapper.getHolder(stack);
        return wrapper != null && meteorMap.containsKey(wrapper);
    }

    public static Meteor getMeteorForItem(ItemStack stack) {
        ItemStackWrapper wrapper = ItemStackWrapper.getHolder(stack);
        return wrapper != null ? meteorMap.get(wrapper) : null;
    }

    public static void generateMeteorForItem(ItemStack stack, World world, BlockPos pos, IBlockState fillerBlock, double radiusModifier, double explosionModifier, double fillerChance) {
        Meteor holder = getMeteorForItem(stack);
        if (holder != null) {
            holder.generateMeteor(world, pos, fillerBlock, radiusModifier, explosionModifier, fillerChance);
        }
    }
}
