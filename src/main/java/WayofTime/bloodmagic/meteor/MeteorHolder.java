package WayofTime.bloodmagic.meteor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.util.Utils;

@AllArgsConstructor
public class MeteorHolder
{
    public static Random rand = new Random();
    public ResourceLocation resourceKey;
    public int metaKey = 0;
    public List<MeteorComponent> components = new ArrayList<MeteorComponent>();

    public float explosionStrength = 1;
    public int radius = 1;

    public int maxWeight = 1000;

    public ItemStack getKeyStack()
    {
        Item item = Utils.getItem(resourceKey);
        if (item != null)
        {
            return new ItemStack(item);
        } else
        {
            Block block = Utils.getBlock(resourceKey);
            if (block != null)
            {
                return new ItemStack(block);
            }
        }

        return null;
    }

    public void generateMeteor(World world, BlockPos pos, IBlockState fillerBlock)
    {
        world.newExplosion(null, pos.getX(), pos.getY(), pos.getZ(), explosionStrength, true, true);

        for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                    if (i * i + j * j + k * k > (radius + 0.5) * (radius + 0.5))
                    {
                        continue;
                    }

                    BlockPos newPos = pos.add(i, j, k);
                    IBlockState state = world.getBlockState(newPos);

                    if (world.isAirBlock(newPos) || Utils.isBlockLiquid(state))
                    {
                        IBlockState placedState = getRandomOreFromComponents(fillerBlock);
                        world.setBlockState(newPos, placedState);
                    }
                }
            }
        }
    }

    public IBlockState getRandomOreFromComponents(IBlockState fillerBlock)
    {
        int goal = rand.nextInt(maxWeight);

        for (MeteorComponent component : components)
        {
            goal -= component.getWeight();
            if (goal < 0)
            {
                IBlockState state = component.getStateFromOre();
                if (state != null)
                {
                    return state;
                } else
                {
                    return fillerBlock;
                }
            }
        }

        return fillerBlock;
    }
}