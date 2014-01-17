package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ModBlocks;
import WayofTime.alchemicalWizardry.common.block.BloodRune;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class UpgradedAltars
{
    public static List<AltarComponent> secondTierAltar = new ArrayList();
    public static List<AltarComponent> thirdTierAltar = new ArrayList();
    public static List<AltarComponent> fourthTierAltar = new ArrayList();
    public static List<AltarComponent> fifthTierAltar = new ArrayList();
    public static int highestAltar = 5;

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
                        Block testBlock = Block.blocksList[world.getBlockId(x + ac.getX(), y + ac.getY(), z + ac.getZ())];

                        if (!(testBlock instanceof BloodRune))
                        {
                            return false;
                        }
                    } else
                    {
                        int blockId = world.getBlockId(x + ac.getX(), y + ac.getY(), z + ac.getZ());
                        int metadata = world.getBlockMetadata(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (((ac.getBlockID() != blockId) || (ac.getMetadata() != metadata)) && !(ac.getBlockID() == Block.stoneBrick.blockID && !world.isAirBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ())))
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
                        Block testBlock = Block.blocksList[world.getBlockId(x + ac.getX(), y + ac.getY(), z + ac.getZ())];

                        if (!(testBlock instanceof BloodRune))
                        {
                            return false;
                        }
                    } else
                    {
                        int blockId = world.getBlockId(x + ac.getX(), y + ac.getY(), z + ac.getZ());
                        int metadata = world.getBlockMetadata(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (((ac.getBlockID() != blockId) || (ac.getMetadata() != metadata)) && !(ac.getBlockID() == Block.stoneBrick.blockID && !world.isAirBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ())))
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
                        Block testBlock = Block.blocksList[world.getBlockId(x + ac.getX(), y + ac.getY(), z + ac.getZ())];

                        if (!(testBlock instanceof BloodRune))
                        {
                            return false;
                        }
                    } else
                    {
                        int blockId = world.getBlockId(x + ac.getX(), y + ac.getY(), z + ac.getZ());
                        int metadata = world.getBlockMetadata(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (((ac.getBlockID() != blockId) || (ac.getMetadata() != metadata)) && !(ac.getBlockID() == Block.stoneBrick.blockID && !world.isAirBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ())))
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
                        Block testBlock = Block.blocksList[world.getBlockId(x + ac.getX(), y + ac.getY(), z + ac.getZ())];

                        if (!(testBlock instanceof BloodRune))
                        {
                            return false;
                        }
                    } else
                    {
                        int blockId = world.getBlockId(x + ac.getX(), y + ac.getY(), z + ac.getZ());
                        int metadata = world.getBlockMetadata(x + ac.getX(), y + ac.getY(), z + ac.getZ());

                        if (((ac.getBlockID() != blockId) || (ac.getMetadata() != metadata)) && !(ac.getBlockID() == Block.stoneBrick.blockID && !world.isAirBlock(x + ac.getX(), y + ac.getY(), z + ac.getZ())))
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
                Block testBlock = Block.blocksList[world.getBlockId(x + ac.getX(), y + ac.getY(), z + ac.getZ())];

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
                        }
                    }
                }
            }
        }

        return upgrades;
    }

    public static void loadAltars()
    {
        secondTierAltar.add(new AltarComponent(-1, -1, -1, AlchemicalWizardry.bloodRuneBlockID, 0, true, false));
        secondTierAltar.add(new AltarComponent(0, -1, -1, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        secondTierAltar.add(new AltarComponent(1, -1, -1, AlchemicalWizardry.bloodRuneBlockID, 0, true, false));
        secondTierAltar.add(new AltarComponent(-1, -1, 0, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        secondTierAltar.add(new AltarComponent(1, -1, 0, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        secondTierAltar.add(new AltarComponent(-1, -1, 1, AlchemicalWizardry.bloodRuneBlockID, 0, true, false));
        secondTierAltar.add(new AltarComponent(0, -1, 1, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        secondTierAltar.add(new AltarComponent(1, -1, 1, AlchemicalWizardry.bloodRuneBlockID, 0, true, false));
        thirdTierAltar.add(new AltarComponent(-1, -1, -1, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        thirdTierAltar.add(new AltarComponent(0, -1, -1, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, -1, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        thirdTierAltar.add(new AltarComponent(-1, -1, 0, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, 0, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        thirdTierAltar.add(new AltarComponent(-1, -1, 1, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        thirdTierAltar.add(new AltarComponent(0, -1, 1, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, 1, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        thirdTierAltar.add(new AltarComponent(-3, -1, -3, Block.stoneBrick.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 0, -3, Block.stoneBrick.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, -1, -3, Block.stoneBrick.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, 0, -3, Block.stoneBrick.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(-3, -1, 3, Block.stoneBrick.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 0, 3, Block.stoneBrick.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, -1, 3, Block.stoneBrick.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, 0, 3, Block.stoneBrick.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 1, -3, Block.glowStone.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, 1, -3, Block.glowStone.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 1, 3, Block.glowStone.blockID, 0, false, false));
        thirdTierAltar.add(new AltarComponent(3, 1, 3, Block.glowStone.blockID, 0, false, false));

        for (int i = -2; i <= 2; i++)
        {
            thirdTierAltar.add(new AltarComponent(3, -2, i, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
            thirdTierAltar.add(new AltarComponent(-3, -2, i, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
            thirdTierAltar.add(new AltarComponent(i, -2, 3, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
            thirdTierAltar.add(new AltarComponent(i, -2, -3, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        }

        fourthTierAltar.addAll(thirdTierAltar);

        for (int i = -3; i <= 3; i++)
        {
            fourthTierAltar.add(new AltarComponent(5, -3, i, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
            fourthTierAltar.add(new AltarComponent(-5, -3, i, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
            fourthTierAltar.add(new AltarComponent(i, -3, 5, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
            fourthTierAltar.add(new AltarComponent(i, -3, -5, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
        }

        for (int i = -2; i <= 1; i++)
        {
            fourthTierAltar.add(new AltarComponent(5, i, 5, Block.stoneBrick.blockID, 0, false, false));
            fourthTierAltar.add(new AltarComponent(5, i, -5, Block.stoneBrick.blockID, 0, false, false));
            fourthTierAltar.add(new AltarComponent(-5, i, -5, Block.stoneBrick.blockID, 0, false, false));
            fourthTierAltar.add(new AltarComponent(-5, i, 5, Block.stoneBrick.blockID, 0, false, false));
        }

        fourthTierAltar.add(new AltarComponent(5, 2, 5, ModBlocks.largeBloodStoneBrick.blockID, 0, false, false));
        fourthTierAltar.add(new AltarComponent(5, 2, -5, ModBlocks.largeBloodStoneBrick.blockID, 0, false, false));
        fourthTierAltar.add(new AltarComponent(-5, 2, -5, ModBlocks.largeBloodStoneBrick.blockID, 0, false, false));
        fourthTierAltar.add(new AltarComponent(-5, 2, 5, ModBlocks.largeBloodStoneBrick.blockID, 0, false, false));
        fifthTierAltar.addAll(fourthTierAltar);
        fifthTierAltar.add(new AltarComponent(-8, -3, 8, Block.beacon.blockID, 0, false, false));
        fifthTierAltar.add(new AltarComponent(-8, -3, -8, Block.beacon.blockID, 0, false, false));
        fifthTierAltar.add(new AltarComponent(8, -3, 8, Block.beacon.blockID, 0, false, false));
        fifthTierAltar.add(new AltarComponent(8, -3, 8, Block.beacon.blockID, 0, false, false));

        for (int i = -6; i <= 6; i++)
        {
            fifthTierAltar.add(new AltarComponent(8, -4, i, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
            fifthTierAltar.add(new AltarComponent(-8, -4, i, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
            fifthTierAltar.add(new AltarComponent(i, -4, 8, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
            fifthTierAltar.add(new AltarComponent(i, -4, -8, AlchemicalWizardry.bloodRuneBlockID, 0, true, true));
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
        }

        return null;
    }
}
