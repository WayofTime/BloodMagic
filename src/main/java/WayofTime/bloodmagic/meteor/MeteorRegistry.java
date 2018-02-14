package WayofTime.bloodmagic.meteor;

import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class MeteorRegistry {

    public static Map<ItemStack, Meteor> meteorMap = Maps.newHashMap();

    public static void registerMeteor(ItemStack stack, Meteor holder) {
        if (!stack.isEmpty())
            meteorMap.put(stack, holder);
    }

    public static void registerMeteor(ItemStack stack, List<MeteorComponent> componentList, float explosionStrength, int radius) {
        Meteor holder = new Meteor(stack, componentList, explosionStrength, radius);
        registerMeteor(stack, holder);
    }

    @Nullable
    public static Meteor getMeteorForItem(ItemStack stack) {
        if (stack.isEmpty())
            return null;

        for (Map.Entry<ItemStack, Meteor> entry : meteorMap.entrySet())
            if (ItemStack.areItemsEqual(stack, entry.getKey()))
                return entry.getValue();

        return null;
    }

    public static void generateMeteorForItem(ItemStack stack, World world, BlockPos pos, IBlockState fillerBlock, double radiusModifier, double explosionModifier, double fillerChance) {
        Meteor meteor = getMeteorForItem(stack);
        if (meteor != null)
            meteor.generateMeteor(world, pos, fillerBlock, radiusModifier, explosionModifier, fillerChance);
    }
}
