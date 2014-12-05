package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.block.BloodRune;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class UpgradedAltars
{
    public static List<AltarComponent> secondTierAltar = new ArrayList();
    public static List<AltarComponent> thirdTierAltar = new ArrayList();
    public static List<AltarComponent> fourthTierAltar = new ArrayList();
    public static List<AltarComponent> fifthTierAltar = new ArrayList();
    public static List<AltarComponent> sixthTierAltar = new ArrayList();
    public static int highestAltar = 6;

    public static int isAltarValid(World world, int x, int y, int z)
    {
        for (int i = highestAltar; i >= 2; i--)
        {
            if (checkAltarIsValid(world, x, y, z, i))
            {
                return i;
            }
        }

        return 1;
    }

    public static boolean checkAltarIsValid(World world, int x, int y, int z, int altarTier)
    {
        switch (altarTier)
        {
            case 1:
                return true;

            case 2:
                for (AltarComponent ac : secondTierAltar)
                {
                    if (ac.isBloodRune())
                    {
                        Block testBlock = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (!(testBlock instanceof BloodRune))
                        {
                            return false;
                        }
                    } else
                    {
                        Block block = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());
                        int metadata = world.getBlockMetadata(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (((ac.getBlock() != block) || (ac.getMetadata() != metadata)) && !(ac.getBlock() == Blocks.stonebrick && !world.isAirBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ())))
                        {
                            return false;
                        }
                    }
                }

                return true;

            case 3:
                for (AltarComponent ac : thirdTierAltar)
                {
                    if (ac.isBloodRune())
                    {
                        Block testBlock = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (!(testBlock instanceof BloodRune))
                        {
                            return false;
                        }
                    } else
                    {
                        Block block = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());
                        int metadata = world.getBlockMetadata(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (((ac.getBlock() != block) || (ac.getMetadata() != metadata)) && !(ac.getBlock() == Blocks.stonebrick && !world.isAirBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ())))
                        {
                            return false;
                        }
                    }
                }

                return true;

            case 4:
                for (AltarComponent ac : fourthTierAltar)
                {
                    if (ac.isBloodRune())
                    {
                        Block testBlock = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (!(testBlock instanceof BloodRune))
                        {
                            return false;
                        }
                    } else
                    {
                        Block block = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());
                        int metadata = world.getBlockMetadata(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (((ac.getBlock() != block) || (ac.getMetadata() != metadata)) && !(ac.getBlock() == Blocks.stonebrick && !world.isAirBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ())))
                        {
                            return false;
                        }
                    }
                }

                return true;

            case 5:
                for (AltarComponent ac : fifthTierAltar)
                {
                    if (ac.isBloodRune())
                    {
                        Block testBlock = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (!(testBlock instanceof BloodRune))
                        {
                            return false;
                        }
                    } else
                    {
                        Block block = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());
                        int metadata = world.getBlockMetadata(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (((ac.getBlock() != block) || (ac.getMetadata() != metadata)) && !(ac.getBlock() == Blocks.stonebrick && !world.isAirBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ())))
                        {
                            return false;
                        }
                    }
                }

                return true;
                
            case 6:
                for (AltarComponent ac : sixthTierAltar)
                {
                    if (ac.isBloodRune())
                    {
                        Block testBlock = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (!(testBlock instanceof BloodRune))
                        {
                            return false;
                        }
                    } else
                    {
                        Block block = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());
                        int metadata = world.getBlockMetadata(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (((ac.getBlock() != block) || (ac.getMetadata() != metadata)) && !(ac.getBlock() == Blocks.stonebrick && !world.isAirBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ())))
                        {
                            return false;
                        }
                    }
                }

                return true;

            default:
                return false;
        }
    }

    public static AltarUpgradeComponent getUpgrades(World world, int x, int y, int z, int altarTier)
    {
        AltarUpgradeComponent upgrades = new AltarUpgradeComponent();
        List<AltarComponent> list = UpgradedAltars.getAltarUpgradeListForTier(altarTier);

        for (AltarComponent ac : list)
        {
            if (ac.isUpgradeSlot())
            {
                //Currently checks the getRuneEffect.
                //TODO Change so that it uses the metadata instead, with the BlockID.
                Block testBlock = world.getBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                if (testBlock instanceof BloodRune)
                {
                    if (!world.isRemote)
                    {
                        switch (((BloodRune) testBlock).getRuneEffect(world.getBlockMetadata(x + ac.getX(), y + ac.getY(), z + ac.getZ())))
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
        fifthTierAltar.add(new AltarComponent(8, -3, 8, Blocks.beacon, 0, false, false));
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
