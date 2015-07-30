package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.IFadedRune;
import WayofTime.alchemicalWizardry.common.block.BlockBloodRune;

public class UpgradedAltars
{
    public static List<AltarComponent> secondTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> thirdTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> fourthTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> fifthTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> sixthTierAltar = new ArrayList<AltarComponent>();
    public static int highestAltar = 6;

    public static int isAltarValid(World world, BlockPos pos)
    {
        for (int i = highestAltar; i >= 2; i--)
        {
            if (checkAltarIsValid(world, pos, i))
            {
                return i;
            }
        }

        return 1;
    }

    public static boolean checkAltarIsValid(World world, BlockPos pos, int altarTier)
    {
        List<AltarComponent> list = UpgradedAltars.getAltarUpgradeListForTier(altarTier);

        for (AltarComponent ac : list)
        {
        	BlockPos newPos = pos.add(ac.getX(), ac.getY(), ac.getZ());
        	IBlockState state = world.getBlockState(newPos);
        	Block block = state.getBlock();
        	
            if (ac.isBloodRune())
            {
                if (!(block instanceof BlockBloodRune))
                {
                    return false;
                }
            } else
            {
                int metadata = block.getMetaFromState(state);

                if (((ac.getBlock() != block) || (ac.getMetadata() != metadata)) && !(ac.getBlock() == Blocks.stonebrick && !world.isAirBlock(newPos)))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static AltarUpgradeComponent getUpgrades(World world, BlockPos pos, int altarTier)
    {
    	if(world.isRemote)
    	{
    		return null;
    	}
        AltarUpgradeComponent upgrades = new AltarUpgradeComponent();
        List<AltarComponent> list = UpgradedAltars.getAltarUpgradeListForTier(altarTier);

        for (AltarComponent ac : list)
        {
        	BlockPos newPos = pos.add(ac.getX(), ac.getY(), ac.getZ());

            if (ac.isUpgradeSlot())
            {
                //Currently checks the getRuneEffect.
                //TODO Change so that it uses the metadata instead, with the BlockID.
            	IBlockState state = world.getBlockState(newPos);
                Block testBlock = state.getBlock();
                int meta = testBlock.getMetaFromState(state);
                
                if (testBlock instanceof BlockBloodRune)
                {
                    if (testBlock instanceof IFadedRune && altarTier > ((IFadedRune)testBlock).getAltarTierLimit(meta))
                    {
                        return UpgradedAltars.getUpgrades(world, pos, ((IFadedRune)testBlock).getAltarTierLimit(meta));
                    }
                    
                    switch (((BlockBloodRune) testBlock).getRuneEffect(meta))
                    {
                        case 1:
                            upgrades.addSpeedUpgrade();
                            break;

                        case 2:
                            upgrades.addEfficiencyUpgrade();
                            break;

                        case 3:
                            upgrades.addSacrificeUpgrade();
                            break;

                        case 4:
                            upgrades.addSelfSacrificeUpgrade();
                            break;

                        case 5:
                            upgrades.addaltarCapacitiveUpgrade();
                            break;

                        case 6:
                            upgrades.addDisplacementUpgrade();
                            break;

                        case 7:
                            upgrades.addorbCapacitiveUpgrade();
                            break;

                        case 8:
                            upgrades.addBetterCapacitiveUpgrade();
                            break;
                            
                        case 9:
                        	upgrades.addAccelerationUpgrade();
                        	break;
                    }
                }
            }
        }

        return upgrades;
    }

    public static void loadAltars()
    {
        secondTierAltar.add(new AltarComponent(-1, -1, -1, ModBlocks.bloodRune, 0, true, false));
        secondTierAltar.add(new AltarComponent(0, -1, -1, ModBlocks.bloodRune, 0, true, true));
        secondTierAltar.add(new AltarComponent(1, -1, -1, ModBlocks.bloodRune, 0, true, false));
        secondTierAltar.add(new AltarComponent(-1, -1, 0, ModBlocks.bloodRune, 0, true, true));
        secondTierAltar.add(new AltarComponent(1, -1, 0, ModBlocks.bloodRune, 0, true, true));
        secondTierAltar.add(new AltarComponent(-1, -1, 1, ModBlocks.bloodRune, 0, true, false));
        secondTierAltar.add(new AltarComponent(0, -1, 1, ModBlocks.bloodRune, 0, true, true));
        secondTierAltar.add(new AltarComponent(1, -1, 1, ModBlocks.bloodRune, 0, true, false));
        thirdTierAltar.add(new AltarComponent(-1, -1, -1, ModBlocks.bloodRune, 0, true, true));
        thirdTierAltar.add(new AltarComponent(0, -1, -1, ModBlocks.bloodRune, 0, true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, -1, ModBlocks.bloodRune, 0, true, true));
        thirdTierAltar.add(new AltarComponent(-1, -1, 0, ModBlocks.bloodRune, 0, true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, 0, ModBlocks.bloodRune, 0, true, true));
        thirdTierAltar.add(new AltarComponent(-1, -1, 1, ModBlocks.bloodRune, 0, true, true));
        thirdTierAltar.add(new AltarComponent(0, -1, 1, ModBlocks.bloodRune, 0, true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, 1, ModBlocks.bloodRune, 0, true, true));
        thirdTierAltar.add(new AltarComponent(-3, -1, -3, Blocks.stonebrick, 0, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 0, -3, Blocks.stonebrick, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, -1, -3, Blocks.stonebrick, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, 0, -3, Blocks.stonebrick, 0, false, false));
        thirdTierAltar.add(new AltarComponent(-3, -1, 3, Blocks.stonebrick, 0, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 0, 3, Blocks.stonebrick, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, -1, 3, Blocks.stonebrick, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, 0, 3, Blocks.stonebrick, 0, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 1, -3, Blocks.glowstone, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, 1, -3, Blocks.glowstone, 0, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 1, 3, Blocks.glowstone, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, 1, 3, Blocks.glowstone, 0, false, false));

        for (int i = -2; i <= 2; i++)
        {
            thirdTierAltar.add(new AltarComponent(3, -2, i, ModBlocks.bloodRune, 0, true, true));
            thirdTierAltar.add(new AltarComponent(-3, -2, i, ModBlocks.bloodRune, 0, true, true));
            thirdTierAltar.add(new AltarComponent(i, -2, 3, ModBlocks.bloodRune, 0, true, true));
            thirdTierAltar.add(new AltarComponent(i, -2, -3, ModBlocks.bloodRune, 0, true, true));
        }

        fourthTierAltar.addAll(thirdTierAltar);

        for (int i = -3; i <= 3; i++)
        {
            fourthTierAltar.add(new AltarComponent(5, -3, i, ModBlocks.bloodRune, 0, true, true));
            fourthTierAltar.add(new AltarComponent(-5, -3, i, ModBlocks.bloodRune, 0, true, true));
            fourthTierAltar.add(new AltarComponent(i, -3, 5, ModBlocks.bloodRune, 0, true, true));
            fourthTierAltar.add(new AltarComponent(i, -3, -5, ModBlocks.bloodRune, 0, true, true));
        }

        for (int i = -2; i <= 1; i++)
        {
            fourthTierAltar.add(new AltarComponent(5, i, 5, Blocks.stonebrick, 0, false, false));
            fourthTierAltar.add(new AltarComponent(5, i, -5, Blocks.stonebrick, 0, false, false));
            fourthTierAltar.add(new AltarComponent(-5, i, -5, Blocks.stonebrick, 0, false, false));
            fourthTierAltar.add(new AltarComponent(-5, i, 5, Blocks.stonebrick, 0, false, false));
        }

        fourthTierAltar.add(new AltarComponent(5, 2, 5, ModBlocks.largeBloodStoneBrick, 0, false, false));
        fourthTierAltar.add(new AltarComponent(5, 2, -5, ModBlocks.largeBloodStoneBrick, 0, false, false));
        fourthTierAltar.add(new AltarComponent(-5, 2, -5, ModBlocks.largeBloodStoneBrick, 0, false, false));
        fourthTierAltar.add(new AltarComponent(-5, 2, 5, ModBlocks.largeBloodStoneBrick, 0, false, false));
        fifthTierAltar.addAll(fourthTierAltar);
        fifthTierAltar.add(new AltarComponent(-8, -3, 8, Blocks.beacon, 0, false, false));
        fifthTierAltar.add(new AltarComponent(-8, -3, -8, Blocks.beacon, 0, false, false));
        fifthTierAltar.add(new AltarComponent(8, -3, -8, Blocks.beacon, 0, false, false));
        fifthTierAltar.add(new AltarComponent(8, -3, 8, Blocks.beacon, 0, false, false));

        for (int i = -6; i <= 6; i++)
        {
            fifthTierAltar.add(new AltarComponent(8, -4, i, ModBlocks.bloodRune, 0, true, true));
            fifthTierAltar.add(new AltarComponent(-8, -4, i, ModBlocks.bloodRune, 0, true, true));
            fifthTierAltar.add(new AltarComponent(i, -4, 8, ModBlocks.bloodRune, 0, true, true));
            fifthTierAltar.add(new AltarComponent(i, -4, -8, ModBlocks.bloodRune, 0, true, true));
        }
        
        sixthTierAltar.addAll(fifthTierAltar);
        
        for(int i = -4; i <= 2; i++)
        {
        	sixthTierAltar.add(new AltarComponent(11, i, 11, Blocks.stonebrick, 0, false, false));
        	sixthTierAltar.add(new AltarComponent(-11, i, -11, Blocks.stonebrick, 0, false, false));
        	sixthTierAltar.add(new AltarComponent(11, i, -11, Blocks.stonebrick, 0, false, false));
        	sixthTierAltar.add(new AltarComponent(-11, i, 11, Blocks.stonebrick, 0, false, false));
        }
        
        sixthTierAltar.add(new AltarComponent(11, 3, 11, ModBlocks.blockCrystal, 0, false, false));
    	sixthTierAltar.add(new AltarComponent(-11, 3, -11, ModBlocks.blockCrystal, 0, false, false));
    	sixthTierAltar.add(new AltarComponent(11, 3, -11, ModBlocks.blockCrystal, 0, false, false));
    	sixthTierAltar.add(new AltarComponent(-11, 3, 11, ModBlocks.blockCrystal, 0, false, false));
        
        for (int i = -9; i <= 9; i++)
        {
            sixthTierAltar.add(new AltarComponent(11, -5, i, ModBlocks.bloodRune, 0, true, true));
            sixthTierAltar.add(new AltarComponent(-11, -5, i, ModBlocks.bloodRune, 0, true, true));
            sixthTierAltar.add(new AltarComponent(i, -5, 11, ModBlocks.bloodRune, 0, true, true));
            sixthTierAltar.add(new AltarComponent(i, -5, -11, ModBlocks.bloodRune, 0, true, true));
        }
    }

    public static List<AltarComponent> getAltarUpgradeListForTier(int tier)
    {
        switch (tier)
        {
            case 2:
                return secondTierAltar;

            case 3:
                return thirdTierAltar;

            case 4:
                return fourthTierAltar;

            case 5:
                return fifthTierAltar;
                
            case 6:
            	return sixthTierAltar;
        }

        return null;
    }
}
