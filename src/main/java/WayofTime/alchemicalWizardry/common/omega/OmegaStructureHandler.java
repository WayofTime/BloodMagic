package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.Int3;

public class OmegaStructureHandler 
{
	public static final OmegaStructureParameters emptyParam = new OmegaStructureParameters(0, 0, 0);
	
	public static boolean isStructureIntact(World world, int x, int y, int z)
	{
		return true;
	}
	
	public static OmegaStructureParameters getStructureStabilityFactor(World world, BlockPos pos, int expLim, Int3 offset)
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
                        	BlockPos position = pos.add(i - range, j - range, k - range);
                        	
                        	for(EnumFacing face : EnumFacing.VALUES)
                        	{
                        		int iP = i + face.getFrontOffsetX();
                        		int jP = j + face.getFrontOffsetY();
                        		int kP = k + face.getFrontOffsetZ();
                        		
                        		if(iP >= 0 && iP <= 2 * range && jP >= 0 && jP <= 2 * range && kP >= 0 && kP <= 2 * range && !(boolList[iP][jP][kP] == 1 || boolList[iP][jP][kP] == -1))
                        		{
                            		BlockPos newPos = position.add(face.getDirectionVec());
                            		IBlockState state = world.getBlockState(newPos);
                            		Block block = state.getBlock();
                            		if (world.isAirBlock(newPos) || block == ModBlocks.blockSpectralContainer)
                                    {
                            			if(iP == 0 && iP == 2 * range && jP == 0 && jP == 2 * range && kP == 0 && kP == 2 * range)
                            			{
                            				return emptyParam;
                            			}
                                        boolList[iP][jP][kP] = 1;
                                        isReady = false;
                                    }else
                                    {
                                    	boolList[iP][jP][kP] = -1;
                                    }
                        		}
                        	}
                        }
                    }
                }
            }
        }
        
        int tally = 0;
        int enchantability = 0;
        int enchantmentLevel = 0;
        
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
                                        
                    BlockPos newPos = pos.add(i - range, j - range, k - range);
                    
                    IBlockState state = world.getBlockState(newPos);
                    Block block = state.getBlock();
                    int meta = block.getMetaFromState(state);
                    
                    if(block instanceof IEnchantmentGlyph)
                    {
                    	tally += ((IEnchantmentGlyph)block).getAdditionalStabilityForFaceCount(world, newPos, meta, indTally);
                    	enchantability += ((IEnchantmentGlyph)block).getEnchantability(world, newPos, meta);
                    	enchantmentLevel += ((IEnchantmentGlyph)block).getEnchantmentLevel(world, newPos, meta);
                    }else if(block instanceof IStabilityGlyph)
                    {
                    	tally += ((IStabilityGlyph)block).getAdditionalStabilityForFaceCount(world, newPos, meta, indTally);
                    }else
                    {
                        tally += indTally;
                    } 
                }
            }
        }
	
		return new OmegaStructureParameters(tally, enchantability, enchantmentLevel);
	}
	
	public static OmegaStructureParameters getStructureStabilityFactor(World world, BlockPos pos, int expLim)
	{
        return getStructureStabilityFactor(world, pos, expLim, new Int3(0, 0, 0));
	}
}
