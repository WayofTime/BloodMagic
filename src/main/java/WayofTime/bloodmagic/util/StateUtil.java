package WayofTime.bloodmagic.util;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class StateUtil
{
    public static IBlockState parseState(String blockInfo)
    {
        String[] split = blockInfo.split("\\[");
        split[1] = split[1].substring(0, split[1].lastIndexOf("]")); // Make sure brackets are removed from state

        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(split[0])); // Find the block
        if (block == Blocks.AIR)
            return Blocks.AIR.getDefaultState(); // The block is air, so we're looking at invalid data

        BlockStateContainer blockState = block.getBlockState();
        IBlockState returnState = blockState.getBaseState();

        // Force our values into the state
        String[] stateValues = split[1].split(","); // Splits up each value
        for (String value : stateValues)
        {
            String[] valueSplit = value.split("="); // Separates property and value
            IProperty property = blockState.getProperty(valueSplit[0]);
            if (property != null)
                returnState = returnState.withProperty(property, (Comparable) property.parseValue(valueSplit[1]).get()); // Force the property into the state
        }

        return returnState;
    }
}