package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.Int3;

public class OmegaStructureHandler 
{
	public static final OmegaStructureParameters emptyParam = new OmegaStructureParameters(0, 0);
	
	public static boolean isStructureIntact(World world, int x, int y, int z)
	{
		return true;
	}
	
	public static OmegaStructureParameters getStructureStabilityFactor(World world, int x, int y, int z, int expLim, Int3 offset)
	{
		int range = expLim;

        int[][][] boolList = new int[range * 2 + 1][range * 2 + 1][range * 2 + 1]; //0 indicates unchecked, 1 indicates checked and is air, -1 indicates checked to be right next to air blocks in question but is NOT air

        for (int i = 0; i < 2 * range + 1; i++)
        {
            for (int j = 0; j < 2 * range + 1; j++)
            {
                for (int k = 0; k < 2 * range + 1; k++)
                {
                    boolList[i][j][k] = 0;
                }
            }
        }

        boolList[range + offset.xCoord][range + offset.yCoord][range + offset.zCoord] = 1;
        boolean isReady = false;

        while (!isReady)
        {
            isReady = true;

            for (int i = 0; i < 2 * range + 1; i++)
            {
                for (int j = 0; j < 2 * range + 1; j++)
                {
                    for (int k = 0; k < 2 * range + 1; k++)
                    {
                        if (boolList[i][j][k] == 1)
                        {
                            if (i - 1 >= 0 && !(boolList[i - 1][j][k] == 1 || boolList[i - 1][j][k] == -1))
                            {
                                Block block = world.getBlock(x - range + i - 1, y - range + j, z - range + k);
                                if (world.isAirBlock(x - range + i - 1, y - range + j, z - range + k) || block == ModBlocks.blockSpectralContainer)
                                {
                                	if(i - 1 == 0) //One of the found air blocks is at the range boundary, and thus the container is incomplete
                                	{
                                		return emptyParam;
                                	}
                                    boolList[i - 1][j][k] = 1;
                                    isReady = false;
                                }else
                                {
                                	boolList[i - 1][j][k] = -1;
                                }
                            }

                            if (j - 1 >= 0 && !(boolList[i][j - 1][k] == 1 || boolList[i][j - 1][k] == -1))
                            {
                                Block block = world.getBlock(x - range + i, y - range + j - 1, z - range + k);
                                if (world.isAirBlock(x - range + i, y - range + j - 1, z - range + k) || block == ModBlocks.blockSpectralContainer)
                                {
                                	if(j - 1 == 0)
                                	{
                                		return emptyParam;
                                	}
                                    boolList[i][j - 1][k] = 1;
                                    isReady = false;
                                }else
                                {
                                	boolList[i][j - 1][k] = -1;
                                }
                            }

                            if (k - 1 >= 0 && !(boolList[i][j][k - 1] == 1 || boolList[i][j][k - 1] == -1))
                            {
                                Block block = world.getBlock(x - range + i, y - range + j, z - range + k - 1);
                                if (world.isAirBlock(x - range + i, y - range + j, z - range + k - 1) || block == ModBlocks.blockSpectralContainer)
                                {
                                	if(k - 1 == 0)
                                	{
                                		return emptyParam;
                                	}
                                    boolList[i][j][k - 1] = 1;
                                    isReady = false;
                                }else
                                {
                                	boolList[i][j][k - 1] = -1;
                                }
                            }

                            if (i + 1 <= 2 * range && !(boolList[i + 1][j][k] == 1 || boolList[i + 1][j][k] == -1))
                            {
                                Block block = world.getBlock(x - range + i + 1, y - range + j, z - range + k);
                                if (world.isAirBlock(x - range + i + 1, y - range + j, z - range + k) || block == ModBlocks.blockSpectralContainer)
                                {
                                	if(i + 1 == range * 2)
                                	{
                                		return emptyParam;
                                	}
                                    boolList[i + 1][j][k] = 1;
                                    isReady = false;
                                }else
                                {
                                	boolList[i + 1][j][k] = -1;
                                }
                            }

                            if (j + 1 <= 2 * range && !(boolList[i][j + 1][k] == 1 || boolList[i][j + 1][k] == -1))
                            {
                                Block block = world.getBlock(x - range + i, y - range + j + 1, z - range + k);
                                if (world.isAirBlock(x - range + i, y - range + j + 1, z - range + k) || block == ModBlocks.blockSpectralContainer)
                                {
                                	if(j + 1 == range * 2)
                                	{
                                		return emptyParam;
                                	}
                                    boolList[i][j + 1][k] = 1;
                                    isReady = false;
                                }else
                                {
                                	boolList[i][j + 1][k] = -1;
                                }
                            }

                            if (k + 1 <= 2 * range && !(boolList[i][j][k + 1] == 1 || boolList[i][j][k + 1] == -1))
                            {
                                Block block = world.getBlock(x - range + i, y - range + j, z - range + k + 1);
                                if (world.isAirBlock(x - range + i, y - range + j, z - range + k + 1) || block == ModBlocks.blockSpectralContainer)
                                {
                                	if(k + 1 == range * 2)
                                	{
                                		return emptyParam;
                                	}
                                    boolList[i][j][k + 1] = 1;
                                    isReady = false;
                                }else
                                {
                                	boolList[i][j][k + 1] = -1;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        int tally = 0;
        int enchantability = 0;
        
        for (int i = 0; i < 2 * range + 1; i++)
        {
            for (int j = 0; j < 2 * range + 1; j++)
            {
                for (int k = 0; k < 2 * range + 1; k++)
                {
                    if (boolList[i][j][k] != -1)
                    {
                        continue;
                    }
                    
                    int indTally = 0;

                    if (i - 1 >= 0 && boolList[i - 1][j][k] == 1)
                    {
                    	indTally++;
                    }
                    
                    if (j - 1 >= 0 && boolList[i][j - 1][k] == 1)
                    {
                    	indTally++;
                    }
                    
                    if (k - 1 >= 0 && boolList[i][j][k - 1] == 1)
                    {
                    	indTally++;
                    }
                    
                    if (i + 1 <= 2 * range && boolList[i + 1][j][k] == 1)
                    {
                    	indTally++;
                    }
                    
                    if (j + 1 <= 2 * range && boolList[i][j + 1][k] == 1)
                    {
                    	indTally++;
                    }
                    
                    if (k + 1 <= 2 * range && boolList[i][j][k + 1] == 1)
                    {
                    	indTally++;
                    }
                    
                    Block block = world.getBlock(x - range + i, y - range + j, z - range + k);
                    int meta = 0;
                    if(block instanceof IEnchantmentGlyph)
                    {
                    	tally -= ((IEnchantmentGlyph)block).getSubtractedStabilityForFaceCount(world, x-range+i, y-range+j, z-range+k, meta, indTally);
                    	enchantability += ((IEnchantmentGlyph)block).getEnchantability(world, x-range+i, y-range+j, z-range+k, meta);
                    }else
                    {
                        tally += indTally;
                    }
                }
            }
        }
    
	
		return new OmegaStructureParameters(tally, enchantability);
	}
	
	public static OmegaStructureParameters getStructureStabilityFactor(World world, int x, int y, int z, int expLim)
	{
        return getStructureStabilityFactor(world, x, y, z, expLim, new Int3(0, 0, 0));
	}
}
