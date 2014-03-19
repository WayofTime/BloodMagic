package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;

public class MeteorParadigm
{
    public List<MeteorParadigmComponent> componentList = new ArrayList();
    public ItemStack focusStack;
    public int radius;
    public static int maxChance = 1000;

    public MeteorParadigm(ItemStack focusStack, int radius)
    {
        this.focusStack = focusStack;
        this.radius = radius;
    }

    public void parseStringArray(String[] oreArray)
    {
        for (int i = 0; i + 1 < oreArray.length; i += 2)
        {
            String oreName = oreArray[i];
            int oreChance = Integer.parseInt(oreArray[i + 1]);
            MeteorParadigmComponent mpc = new MeteorParadigmComponent(oreName, oreChance);
            componentList.add(mpc);
        }
    }

    public void createMeteorImpact(World world, int x, int y, int z)
    {
        world.createExplosion(null, x, y, z, radius * 4, AlchemicalWizardry.doMeteorsDestroyBlocks);

        for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                    if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
                    {
                        continue;
                    }

                    if (!world.isAirBlock(x + i, y + j, z + k))
                    {
                        continue;
                    }

                    int randNum = world.rand.nextInt(maxChance);
                    boolean hasPlacedBlock = false;

                    for (MeteorParadigmComponent mpc : componentList)
                    {
                        if (mpc == null || !mpc.isValidBlockParadigm())
                        {
                            continue;
                        }

                        randNum -= mpc.getChance();

                        if (randNum < 0)
                        {
                            ItemStack blockStack = mpc.getValidBlockParadigm();
                            world.setBlock(x + i, y + j, z + k, Block.getBlockById(Item.getIdFromItem(blockStack.getItem())), blockStack.getItemDamage(), 3);
                            hasPlacedBlock = true;
                            break;
                        }
                    }

                    if (!hasPlacedBlock)
                    {
                        world.setBlock(x + i, y + j, z + k, Blocks.stone, 0, 3);
                    }
                }
            }
        }
    }
}
