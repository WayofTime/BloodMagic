package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.common.block.RitualStone;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class Rituals
{
    private List<RitualComponent> components;
    private int crystalLevel;
    private int actCost;
    private RitualEffect effect;
    private String name;

    public static List<Rituals> ritualList = new ArrayList();

    public Rituals(List<RitualComponent> components, int crystalLevel, int actCost, RitualEffect effect, String name)
    {
        this.components = components;
        this.crystalLevel = crystalLevel;
        this.actCost = actCost;
        this.effect = effect;
        this.name = name;
    }

    public Rituals(List<RitualComponent> components, int crystalLevel, int actCost, RitualEffect effect)
    {
        this.components = components;
        this.crystalLevel = crystalLevel;
        this.actCost = actCost;
        this.effect = effect;
        this.name = "";
    }

    //public static final int totalRituals = 1;

    public static int checkValidRitual(World world, int x, int y, int z)
    {
        for (int i = 1; i <= ritualList.size(); i++)
        {
            if (checkRitualIsValid(world, x, y, z, i))
            {
                return i;
            }
        }

        return 0;
    }

    public static boolean canCrystalActivate(int ritual, int crystalLevel)
    {
        if (ritual <= ritualList.size())
        {
            return ritualList.get(ritual - 1).crystalLevel <= crystalLevel;
        } else
        {
            return false;
        }

//        switch (crystalLevel)
//        {
//            case 1:
//                return(ritual <= 8);
//
//            case 2:
//            	return(ritual <= 16);
//
//            default:
//                return false;
//        }
    }

    public static boolean checkRitualIsValid(World world, int x, int y, int z, int ritualID)
    {
        int direction = Rituals.getDirectionOfRitual(world, x, y, z, ritualID);

        if (direction != -1)
        {
            return true;
        }

        return false;
//        List<RitualComponent> ritual = Rituals.getRitualList(ritualID);
//
//        if (ritual == null)
//        {
//            return false;
//        }
//
//        Block test = null;
//
//        for (RitualComponent rc : ritual)
//        {
//            test = Block.blocksList[world.getBlockId(x + rc.getX(), y + rc.getY(), z + rc.getZ())];
//
//            if (!(test instanceof RitualStone))
//            {
//                return false;
//            }
//
//            if (world.getBlockMetadata(x + rc.getX(), y + rc.getY(), z + rc.getZ()) != rc.getStoneType())
//            {
//                return false;
//            }
//        }
//
//        return true;
    }

    /**
     * 1 - NORTH
     * 2 - EAST
     * 3 - SOUTH
     * 4 - WEST
     */
    public static boolean checkDirectionOfRitualValid(World world, int x, int y, int z, int ritualID, int direction)
    {
        List<RitualComponent> ritual = Rituals.getRitualList(ritualID);

        if (ritual == null)
        {
            return false;
        }

        Block test = null;

        switch (direction)
        {
            case 1:
                for (RitualComponent rc : ritual)
                {
                    test = world.getBlock(x + rc.getX(), y + rc.getY(), z + rc.getZ());

                    if (!(test instanceof RitualStone))
                    {
                        return false;
                    }

                    if (world.getBlockMetadata(x + rc.getX(), y + rc.getY(), z + rc.getZ()) != rc.getStoneType())
                    {
                        return false;
                    }
                }

                return true;

            case 2:
                for (RitualComponent rc : ritual)
                {
                	test = world.getBlock(x + rc.getX(), y + rc.getY(), z + rc.getZ());
                	
                    if (!(test instanceof RitualStone))
                    {
                        return false;
                    }

                    if (world.getBlockMetadata(x - rc.getZ(), y + rc.getY(), z + rc.getX()) != rc.getStoneType())
                    {
                        return false;
                    }
                }

                return true;

            case 3:
                for (RitualComponent rc : ritual)
                {
                	test = world.getBlock(x + rc.getX(), y + rc.getY(), z + rc.getZ());
                	
                    if (!(test instanceof RitualStone))
                    {
                        return false;
                    }

                    if (world.getBlockMetadata(x - rc.getX(), y + rc.getY(), z - rc.getZ()) != rc.getStoneType())
                    {
                        return false;
                    }
                }

                return true;

            case 4:
                for (RitualComponent rc : ritual)
                {
                	test = world.getBlock(x + rc.getX(), y + rc.getY(), z + rc.getZ());
                	
                	if (!(test instanceof RitualStone))
                    {
                        return false;
                    }

                    if (world.getBlockMetadata(x + rc.getZ(), y + rc.getY(), z - rc.getX()) != rc.getStoneType())
                    {
                        return false;
                    }
                }

                return true;
        }

        return false;
    }

    public static int getDirectionOfRitual(World world, int x, int y, int z, int ritualID)
    {
        for (int i = 1; i <= 4; i++)
        {
            if (Rituals.checkDirectionOfRitualValid(world, x, y, z, ritualID, i))
            {
                return i;
            }
        }

        return -1;
    }

    public static void loadRituals()
    {
        ArrayList<RitualComponent> waterRitual = new ArrayList();
        waterRitual.add(new RitualComponent(-1, 0, 1, 1));
        waterRitual.add(new RitualComponent(-1, 0, -1, 1));
        waterRitual.add(new RitualComponent(1, 0, -1, 1));
        waterRitual.add(new RitualComponent(1, 0, 1, 1));
        ArrayList<RitualComponent> lavaRitual = new ArrayList();
        lavaRitual.add(new RitualComponent(1, 0, 0, 2));
        lavaRitual.add(new RitualComponent(-1, 0, 0, 2));
        lavaRitual.add(new RitualComponent(0, 0, 1, 2));
        lavaRitual.add(new RitualComponent(0, 0, -1, 2));
        ArrayList<RitualComponent> growthRitual = new ArrayList();
        growthRitual.add(new RitualComponent(1, 0, 0, 1));
        growthRitual.add(new RitualComponent(-1, 0, 0, 1));
        growthRitual.add(new RitualComponent(0, 0, 1, 1));
        growthRitual.add(new RitualComponent(0, 0, -1, 1));
        growthRitual.add(new RitualComponent(-1, 0, 1, 3));
        growthRitual.add(new RitualComponent(1, 0, 1, 3));
        growthRitual.add(new RitualComponent(-1, 0, -1, 3));
        growthRitual.add(new RitualComponent(1, 0, -1, 3));
        ArrayList<RitualComponent> interdictionRitual = new ArrayList();
        interdictionRitual.add(new RitualComponent(1, 0, 0, 4));
        interdictionRitual.add(new RitualComponent(-1, 0, 0, 4));
        interdictionRitual.add(new RitualComponent(0, 0, 1, 4));
        interdictionRitual.add(new RitualComponent(0, 0, -1, 4));
        interdictionRitual.add(new RitualComponent(-1, 0, 1, 4));
        interdictionRitual.add(new RitualComponent(1, 0, 1, 4));
        interdictionRitual.add(new RitualComponent(-1, 0, -1, 4));
        interdictionRitual.add(new RitualComponent(1, 0, -1, 4));
        ArrayList<RitualComponent> containmentRitual = new ArrayList();
        containmentRitual.add(new RitualComponent(1, 0, 0, 3));
        containmentRitual.add(new RitualComponent(-1, 0, 0, 3));
        containmentRitual.add(new RitualComponent(0, 0, 1, 3));
        containmentRitual.add(new RitualComponent(0, 0, -1, 3));
        containmentRitual.add(new RitualComponent(2, 0, 2, 3));
        containmentRitual.add(new RitualComponent(2, 0, -2, 3));
        containmentRitual.add(new RitualComponent(-2, 0, 2, 3));
        containmentRitual.add(new RitualComponent(-2, 0, -2, 3));
        containmentRitual.add(new RitualComponent(1, 5, 0, 3));
        containmentRitual.add(new RitualComponent(-1, 5, 0, 3));
        containmentRitual.add(new RitualComponent(0, 5, 1, 3));
        containmentRitual.add(new RitualComponent(0, 5, -1, 3));
        containmentRitual.add(new RitualComponent(2, 5, 2, 3));
        containmentRitual.add(new RitualComponent(2, 5, -2, 3));
        containmentRitual.add(new RitualComponent(-2, 5, 2, 3));
        containmentRitual.add(new RitualComponent(-2, 5, -2, 3));
        //Bound soul ritual
        ArrayList<RitualComponent> boundSoulRitual = new ArrayList();
        boundSoulRitual.add(new RitualComponent(3, 0, 0, 2));
        boundSoulRitual.add(new RitualComponent(-3, 0, 0, 2));
        boundSoulRitual.add(new RitualComponent(0, 0, 3, 2));
        boundSoulRitual.add(new RitualComponent(0, 0, -3, 2));
        boundSoulRitual.add(new RitualComponent(2, 0, 2, 4));
        boundSoulRitual.add(new RitualComponent(-2, 0, 2, 4));
        boundSoulRitual.add(new RitualComponent(2, 0, -2, 4));
        boundSoulRitual.add(new RitualComponent(-2, 0, -2, 4));
        boundSoulRitual.add(new RitualComponent(4, 2, 0, 1));
        boundSoulRitual.add(new RitualComponent(-4, 2, 0, 1));
        boundSoulRitual.add(new RitualComponent(0, 2, 4, 1));
        boundSoulRitual.add(new RitualComponent(0, 2, -4, 1));
        boundSoulRitual.add(new RitualComponent(3, 2, 3, 3));
        boundSoulRitual.add(new RitualComponent(3, 2, -3, 3));
        boundSoulRitual.add(new RitualComponent(-3, 2, 3, 3));
        boundSoulRitual.add(new RitualComponent(-3, 2, -3, 3));
        boundSoulRitual.add(new RitualComponent(4, 1, 0, 0));
        boundSoulRitual.add(new RitualComponent(-4, 1, 0, 0));
        boundSoulRitual.add(new RitualComponent(0, 1, 4, 0));
        boundSoulRitual.add(new RitualComponent(0, 1, -4, 0));
        boundSoulRitual.add(new RitualComponent(3, 1, 3, 0));
        boundSoulRitual.add(new RitualComponent(3, 1, -3, 0));
        boundSoulRitual.add(new RitualComponent(-3, 1, 3, 0));
        boundSoulRitual.add(new RitualComponent(-3, 1, -3, 0));
        //
        ArrayList<RitualComponent> unbindingRitual = new ArrayList();
        unbindingRitual.add(new RitualComponent(-2, 0, 0, 4));
        unbindingRitual.add(new RitualComponent(2, 0, 0, 4));
        unbindingRitual.add(new RitualComponent(0, 0, 2, 4));
        unbindingRitual.add(new RitualComponent(0, 0, -2, 4));
        unbindingRitual.add(new RitualComponent(-2, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(-2, 0, -3, 3));
        unbindingRitual.add(new RitualComponent(-3, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, -3, 3));
        unbindingRitual.add(new RitualComponent(3, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(-2, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(-2, 0, 3, 3));
        unbindingRitual.add(new RitualComponent(-3, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, 3, 3));
        unbindingRitual.add(new RitualComponent(3, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(3, 1, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 1, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 1, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 1, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 2, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 2, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 2, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 2, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 3, 3, 2));
        unbindingRitual.add(new RitualComponent(3, 3, -3, 2));
        unbindingRitual.add(new RitualComponent(-3, 3, -3, 2));
        unbindingRitual.add(new RitualComponent(-3, 3, 3, 2));
        unbindingRitual.add(new RitualComponent(-5, 0, 0, 2));
        unbindingRitual.add(new RitualComponent(5, 0, 0, 2));
        unbindingRitual.add(new RitualComponent(0, 0, 5, 2));
        unbindingRitual.add(new RitualComponent(0, 0, -5, 2));
        ArrayList<RitualComponent> jumpingRitual = new ArrayList();

        for (int i = -1; i <= 1; i++)
        {
            jumpingRitual.add(new RitualComponent(1, i, 1, RitualComponent.AIR));
            jumpingRitual.add(new RitualComponent(-1, i, 1, RitualComponent.AIR));
            jumpingRitual.add(new RitualComponent(-1, i, -1, RitualComponent.AIR));
            jumpingRitual.add(new RitualComponent(1, i, -1, RitualComponent.AIR));
        }

        ArrayList<RitualComponent> magneticRitual = new ArrayList();
        magneticRitual.add(new RitualComponent(1, 0, 1, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(1, 0, -1, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(2, 1, 0, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(0, 1, 2, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(-2, 1, 0, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(0, 1, -2, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(2, 1, 2, RitualComponent.AIR));
        magneticRitual.add(new RitualComponent(2, 1, -2, RitualComponent.AIR));
        magneticRitual.add(new RitualComponent(-2, 1, 2, RitualComponent.AIR));
        magneticRitual.add(new RitualComponent(-2, 1, -2, RitualComponent.AIR));
        magneticRitual.add(new RitualComponent(2, 2, 0, RitualComponent.FIRE));
        magneticRitual.add(new RitualComponent(0, 2, 2, RitualComponent.FIRE));
        magneticRitual.add(new RitualComponent(-2, 2, 0, RitualComponent.FIRE));
        magneticRitual.add(new RitualComponent(0, 2, -2, RitualComponent.FIRE));
        ArrayList<RitualComponent> crushingRitual = new ArrayList();
        crushingRitual.add(new RitualComponent(0, 0, 1, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(1, 0, 0, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(0, 0, -1, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(2, 0, 0, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(0, 0, 2, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(0, 0, -2, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(2, 0, 2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(2, 0, -2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(2, 1, 0, RitualComponent.AIR));
        crushingRitual.add(new RitualComponent(-2, 1, 0, RitualComponent.AIR));
        crushingRitual.add(new RitualComponent(0, 1, 2, RitualComponent.AIR));
        crushingRitual.add(new RitualComponent(0, 1, -2, RitualComponent.AIR));
        ArrayList<RitualComponent> leapingRitual = new ArrayList();
        leapingRitual.add(new RitualComponent(0, 0, -2, RitualComponent.DUSK));
        leapingRitual.add(new RitualComponent(1, 0, -1, RitualComponent.AIR));
        leapingRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.AIR));

        for (int i = 0; i <= 2; i++)
        {
            leapingRitual.add(new RitualComponent(2, 0, i, RitualComponent.AIR));
            leapingRitual.add(new RitualComponent(-2, 0, i, RitualComponent.AIR));
        }

        //Animal Growth
        ArrayList<RitualComponent> animalGrowthRitual = new ArrayList();
        animalGrowthRitual.add(new RitualComponent(0, 0, 2, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(2, 0, 0, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(0, 0, -2, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.DUSK));
        animalGrowthRitual.add(new RitualComponent(0, 0, 1, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(1, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(0, 0, -1, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(1, 0, 2, RitualComponent.EARTH));
        animalGrowthRitual.add(new RitualComponent(-1, 0, 2, RitualComponent.EARTH));
        animalGrowthRitual.add(new RitualComponent(1, 0, -2, RitualComponent.EARTH));
        animalGrowthRitual.add(new RitualComponent(-1, 0, -2, RitualComponent.EARTH));
        animalGrowthRitual.add(new RitualComponent(2, 0, 1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(2, 0, -1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(-2, 0, 1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(-2, 0, -1, RitualComponent.AIR));
        //Well of Suffering
        ArrayList<RitualComponent> wellOfSufferingRitual = new ArrayList();
        wellOfSufferingRitual.add(new RitualComponent(1, 0, 1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(1, 0, -1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, 2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, -2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, 2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, -2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(0, -1, 2, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, 0, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(0, -1, -2, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, 0, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(-3, -1, -3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(3, -1, -3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(-3, -1, 3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(3, -1, 3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, -1, 2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, -1, -2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, -1, 2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, -1, -2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(1, 0, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, 0, 1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(1, 0, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, 0, 1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, 0, -1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, 0, -1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, 1, 0, RitualComponent.AIR));
        wellOfSufferingRitual.add(new RitualComponent(0, 1, 4, RitualComponent.AIR));
        wellOfSufferingRitual.add(new RitualComponent(-4, 1, 0, RitualComponent.AIR));
        wellOfSufferingRitual.add(new RitualComponent(0, 1, -4, RitualComponent.AIR));
        ArrayList<RitualComponent> healingRitual = new ArrayList();
        healingRitual.add(new RitualComponent(4, 0, 0, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(5, 0, -1, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(5, 0, 1, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(-4, 0, 0, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(-5, 0, -1, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(-5, 0, 1, RitualComponent.AIR));
        healingRitual.add(new RitualComponent(0, 0, 4, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(-1, 0, 5, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(1, 0, 5, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(0, 0, -4, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(-1, 0, -5, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(1, 0, -5, RitualComponent.FIRE));
        healingRitual.add(new RitualComponent(3, 0, 5, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(5, 0, 3, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(3, 0, -5, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(5, 0, -3, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(-3, 0, 5, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(-5, 0, 3, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(-3, 0, -5, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(-5, 0, -3, RitualComponent.WATER));
        healingRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.DUSK));
        healingRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.DUSK));
        healingRitual.add(new RitualComponent(3, 0, -3, RitualComponent.DUSK));
        healingRitual.add(new RitualComponent(3, 0, 3, RitualComponent.DUSK));
        healingRitual.add(new RitualComponent(4, 0, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(4, -1, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, 0, 4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, -1, 4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, 0, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(4, 0, -5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(4, -1, -5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, 0, -4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, -1, -4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(5, 0, -5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-4, 0, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-4, -1, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, 0, 4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, -1, 4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, 0, 5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-4, 0, -5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-4, -1, -5, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, 0, -4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, -1, -4, RitualComponent.EARTH));
        healingRitual.add(new RitualComponent(-5, 0, -5, RitualComponent.EARTH));
        ArrayList<RitualComponent> featheredKnifeRitual = new ArrayList();
        featheredKnifeRitual.add(new RitualComponent(1, 0, 0, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(0, 0, -1, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(2, -1, 0, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(-2, -1, 0, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(0, -1, 2, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(0, -1, -2, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(1, -1, 1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(1, -1, -1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-1, -1, 1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-1, -1, -1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(4, -1, 2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(2, -1, 4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-4, -1, 2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(2, -1, -4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(4, -1, -2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-2, -1, 4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-4, -1, -2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-2, -1, -4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(4, 0, 2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(2, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, 2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(2, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(4, 0, -2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-2, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, -2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-2, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(4, 0, 3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(3, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, 3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(3, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(4, 0, -3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, -3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(3, 0, 3, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(3, 0, -3, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.AIR));
        ArrayList<RitualComponent> featheredEarthRitual = new ArrayList();
        featheredEarthRitual.add(new RitualComponent(1, 0, 0, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(0, 0, -1, RitualComponent.DUSK));
        featheredEarthRitual.add(new RitualComponent(2, 0, 2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(2, 0, -2, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(1, 0, 3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(0, 0, 3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(1, 0, -3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(0, 0, -3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(3, 0, 1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(3, 0, 0, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(3, 0, -1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-3, 0, 0, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.EARTH));
        featheredEarthRitual.add(new RitualComponent(4, 4, 4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(-4, 4, 4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(-4, 4, -4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(4, 4, -4, RitualComponent.FIRE));
        featheredEarthRitual.add(new RitualComponent(4, 5, 5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(4, 5, 3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(5, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(3, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, 5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, 3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-5, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-3, 5, 4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(4, 5, -5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(4, 5, -3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(5, 5, -4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(3, 5, -4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, -5, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-4, 5, -3, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-5, 5, -4, RitualComponent.AIR));
        featheredEarthRitual.add(new RitualComponent(-3, 5, -4, RitualComponent.AIR));
        //Biome Changer
        ArrayList<RitualComponent> biomeChangerRitual = new ArrayList();
        biomeChangerRitual.add(new RitualComponent(1, 0, -2, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(1, 0, -3, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(2, 0, -1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(3, 0, -1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(1, 0, 2, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(1, 0, 3, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(2, 0, 1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(3, 0, 1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-1, 0, -2, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-2, 0, -1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-1, 0, 2, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-2, 0, 1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(3, 0, -3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(3, 0, -4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(4, 0, -3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(4, 0, -5, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(5, 0, -4, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(3, 0, 3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(3, 0, 4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(4, 0, 3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(4, 0, 5, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(5, 0, 4, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-3, 0, 4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-4, 0, 3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-4, 0, 5, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-5, 0, 4, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-3, 0, -4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-4, 0, -3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-4, 0, -5, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-5, 0, -4, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(0, 0, -5, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-1, 0, -6, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(1, 0, -6, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-1, 0, -8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(0, 0, -8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(1, 0, -8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(-1, 0, -10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(0, 0, -10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(1, 0, -10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(0, 0, 5, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-1, 0, 6, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(1, 0, 6, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-1, 0, 8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(0, 0, 8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(1, 0, 8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(-1, 0, 10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(0, 0, 10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(1, 0, 10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-6, 0, -1, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-6, 0, 1, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-8, 0, -1, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(-8, 0, 0, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(-8, 0, 1, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(-10, 0, -1, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-10, 0, 0, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-10, 0, 1, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(5, 0, 0, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(6, 0, -1, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(6, 0, 1, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(8, 0, -1, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(8, 0, 0, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(8, 0, 1, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(10, 0, -1, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(10, 0, 0, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(10, 0, 1, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(6, 0, -6, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(6, 0, -7, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(7, 0, -6, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(7, 0, -5, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(5, 0, -7, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(8, 0, -5, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(8, 0, -4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(9, 0, -4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(5, 0, -8, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(4, 0, -8, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(4, 0, -9, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-6, 0, 6, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-6, 0, 7, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-7, 0, 6, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-7, 0, 5, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-5, 0, 7, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-8, 0, 5, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-8, 0, 4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-9, 0, 4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-5, 0, 8, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-4, 0, 8, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-4, 0, 9, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(6, 0, 6, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(6, 0, 7, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(7, 0, 6, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(7, 0, 5, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(5, 0, 7, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(8, 0, 5, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(8, 0, 4, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(9, 0, 4, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(5, 0, 8, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(4, 0, 8, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(4, 0, 9, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-6, 0, -6, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-6, 0, -7, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-7, 0, -6, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-7, 0, -5, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-5, 0, -7, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-8, 0, -5, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-8, 0, -4, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-9, 0, -4, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-5, 0, -8, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-4, 0, -8, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-4, 0, -9, RitualComponent.WATER));
        ArrayList<RitualComponent> flightRitual = new ArrayList();
        flightRitual.add(new RitualComponent(1, 0, 0, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(0, 0, -1, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(2, 0, 2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(2, 0, -2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(1, 0, 3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(0, 0, 3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(1, 0, -3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(0, 0, -3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(3, 0, 1, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(3, 0, 0, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(3, 0, -1, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-3, 0, 0, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.EARTH));
        flightRitual.add(new RitualComponent(-3, 0, -4, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(-4, 0, -3, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(-3, 0, 4, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(4, 0, -3, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(3, 0, -4, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(-4, 0, 3, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(3, 0, 4, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(4, 0, 3, RitualComponent.WATER));
        flightRitual.add(new RitualComponent(-1, 1, 0, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(1, 1, 0, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(0, 1, -1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(0, 1, 1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-2, 1, 0, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(2, 1, 0, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(0, 1, -2, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(0, 1, 2, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(-4, 1, 0, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(4, 1, 0, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(0, 1, -4, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(0, 1, 4, RitualComponent.BLANK));
        flightRitual.add(new RitualComponent(-5, 1, 0, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(5, 1, 0, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(0, 1, -5, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(0, 1, 5, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(5, 0, 0, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(0, 0, 5, RitualComponent.DUSK));
        flightRitual.add(new RitualComponent(0, 0, -5, RitualComponent.DUSK));

        for (int i = 2; i <= 4; i++)
        {
            flightRitual.add(new RitualComponent(-i, 2, 0, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(i, 2, 0, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(0, 2, -i, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(0, 2, i, RitualComponent.EARTH));
        }

        flightRitual.add(new RitualComponent(2, 4, 1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(1, 4, 2, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-2, 4, 1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(1, 4, -2, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(2, 4, -1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-1, 4, 2, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-2, 4, -1, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-1, 4, -2, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(2, 4, 2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(-2, 4, 2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(2, 4, -2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(-2, 4, -2, RitualComponent.AIR));
        flightRitual.add(new RitualComponent(-4, 2, -4, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(4, 2, 4, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(4, 2, -4, RitualComponent.FIRE));
        flightRitual.add(new RitualComponent(-4, 2, 4, RitualComponent.FIRE));

        for (int i = -1; i <= 1; i++)
        {
            flightRitual.add(new RitualComponent(3, 4, i, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(-3, 4, i, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(i, 4, 3, RitualComponent.EARTH));
            flightRitual.add(new RitualComponent(i, 4, -3, RitualComponent.EARTH));
        }

        ArrayList<RitualComponent> meteorRitual = new ArrayList();
        meteorRitual.add(new RitualComponent(2, 0, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 0, 2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 0, -2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 0, 1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(3, 0, -1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(1, 0, 3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(1, 0, -3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(4, 0, 2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(4, 0, -2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-4, 0, 2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-4, 0, -2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(2, 0, 4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-2, 0, 4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(2, 0, -4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-2, 0, -4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(5, 0, 3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(5, 0, -3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-5, 0, 3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-5, 0, -3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(3, 0, 5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-3, 0, 5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(3, 0, -5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-3, 0, -5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-4, 0, -4, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-4, 0, 4, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(4, 0, 4, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(4, 0, -4, RitualComponent.DUSK));

        for (int i = 4; i <= 6; i++)
        {
            meteorRitual.add(new RitualComponent(i, 0, 0, RitualComponent.EARTH));
            meteorRitual.add(new RitualComponent(-i, 0, 0, RitualComponent.EARTH));
            meteorRitual.add(new RitualComponent(0, 0, i, RitualComponent.EARTH));
            meteorRitual.add(new RitualComponent(0, 0, -i, RitualComponent.EARTH));
        }

        meteorRitual.add(new RitualComponent(8, 0, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(-8, 0, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 0, 8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 0, -8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(8, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(-8, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, 8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, -8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(7, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(-7, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, 7, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, -7, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(7, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-7, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, 7, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, -7, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(6, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-6, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, 6, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, -6, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(6, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-6, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, 6, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, -6, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(5, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-5, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, 5, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, -5, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(5, 4, 0, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-5, 4, 0, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(0, 4, 5, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(0, 4, -5, RitualComponent.AIR));

        for (int i = -1; i <= 1; i++)
        {
            meteorRitual.add(new RitualComponent(i, 4, 4, RitualComponent.AIR));
            meteorRitual.add(new RitualComponent(i, 4, -4, RitualComponent.AIR));
            meteorRitual.add(new RitualComponent(4, 4, i, RitualComponent.AIR));
            meteorRitual.add(new RitualComponent(-4, 4, i, RitualComponent.AIR));
        }

        meteorRitual.add(new RitualComponent(2, 4, 4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(4, 4, 2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(2, 4, -4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-4, 4, 2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-2, 4, 4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(4, 4, -2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-2, 4, -4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-4, 4, -2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(2, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, 2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-2, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, -2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, -3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(2, 4, -3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, 2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-2, 4, -3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, -2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, -3, RitualComponent.FIRE));
        
        ArrayList<RitualComponent> apiaryRitual = new ArrayList();
        apiaryRitual.add(new RitualComponent(1,0,0, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(1,0,1, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(1,0,-1, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(-1,0,-1, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(-1,0,1, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(-1,0,0, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(0,0,-1, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(0,0,1, RitualComponent.DUSK));

        
        ritualList.add(new Rituals(waterRitual, 1, 500, new RitualEffectWater(), "Ritual of the Full Spring"));
        ritualList.add(new Rituals(lavaRitual, 1, 10000, new RitualEffectLava(), "Serenade of the Nether"));
        ritualList.add(new Rituals(growthRitual, 1, 1000, new RitualEffectGrowth(), "Ritual of the Green Grove"));
        ritualList.add(new Rituals(interdictionRitual, 1, 1000, new RitualEffectInterdiction(), "Interdiction Ritual"));
        ritualList.add(new Rituals(containmentRitual, 1, 2000, new RitualEffectContainment(), "Ritual of Containment"));
        ritualList.add(new Rituals(boundSoulRitual, 1, 5000, new RitualEffectSoulBound(), "Ritual of Binding"));
        ritualList.add(new Rituals(unbindingRitual, 1, 30000, new RitualEffectUnbinding(), "Ritual of Unbinding"));
        ritualList.add(new Rituals(jumpingRitual, 1, 1000, new RitualEffectJumping(), "Ritual of the High Jump"));
        ritualList.add(new Rituals(magneticRitual, 1, 5000, new RitualEffectMagnetic(), "Ritual of Magnetism"));
        ritualList.add(new Rituals(crushingRitual, 1, 2500, new RitualEffectCrushing(), "Ritual of the Crusher"));
        ritualList.add(new Rituals(leapingRitual, 1, 1000, new RitualEffectLeap(), "Ritual of Speed"));
        ritualList.add(new Rituals(animalGrowthRitual, 1, 10000, new RitualEffectAnimalGrowth(), "Ritual of the Shepherd"));
        ritualList.add(new Rituals(wellOfSufferingRitual, 1, 50000, new RitualEffectWellOfSuffering(), "Well of Suffering"));
        ritualList.add(new Rituals(healingRitual, 1, 25000, new RitualEffectHealing(), "Ritual of Regeneration"));
        ritualList.add(new Rituals(featheredKnifeRitual, 1, 50000, new RitualEffectFeatheredKnife(), "Ritual of the Feathered Knife"));
        ritualList.add(new Rituals(featheredEarthRitual, 2, 100000, new RitualEffectFeatheredEarth(), "Ritual of the Feathered Earth"));
        ritualList.add(new Rituals(biomeChangerRitual, 2, 1000000, new RitualEffectBiomeChanger(), "Ritual of Gaia's Transformation"));
        ritualList.add(new Rituals(flightRitual, 2, 1000000, new RitualEffectFlight(), "Reverence of the Condor"));
        ritualList.add(new Rituals(meteorRitual, 2, 1000000, new RitualEffectSummonMeteor(), "Mark of the Falling Tower"));
        ritualList.add(new Rituals(apiaryRitual,1,100,new RitualEffectApiaryOverclock(),"Apiary Overclock"));
    }

    public static int getCostForActivation(int ritualID)
    {
        if (ritualID <= ritualList.size())
        {
            return ritualList.get(ritualID - 1).actCost;
        } else
        {
            return 0;
        }

//        switch (ritualID)
//        {
//            case 1:
//                return 500;
//
//            case 2:
//                return 20000;
//
//            case 3:
//                return 250;
//
//            case 4:
//                return 1000;
//
//            case 5:
//                return 2000;
//
//            case 6:
//                return 5000;
//
//            case 7:
//            	return 50000;
//
//            case 8:
//            	return 1000;
//            default:
//                return 0;
//        }
    }

//    public static int getCostPerRefresh(int ritualID)
//    {
//        switch (ritualID)
//        {
//            case 1:
//                return 25;
//
//            case 2:
//                return 500;
//
//            case 3:
//                return 20;
//
//            case 4:
//                return 1;
//
//            case 5:
//                return 1;
//
//            case 6:
//                return 0;
//
//            case 7:
//            	return 0;
//
//            case 8:
//            	return 1;
//
//            default:
//                return 0;
//        }
//    }

    public static int getInitialCooldown(int ritualID)
    {
        if (ritualID <= ritualList.size())
        {
            RitualEffect ef = ritualList.get(ritualID - 1).effect;

            if (ef != null)
            {
                OreDictionary d;
                return ef.getInitialCooldown();
            }
        }

        return 0;
    }

    public static List<RitualComponent> getRitualList(int ritualID)
    {
        if (ritualID <= ritualList.size())
        {
            return ritualList.get(ritualID - 1).obtainComponents();
        } else
        {
            return null;
        }

//        switch (ritualID)
//        {
//            case 1:
//                return waterRitual;
//
//            case 2:
//                return lavaRitual;
//
//            case 3:
//                return growthRitual;
//
//            case 4:
//                return interdictionRitual;
//
//            case 5:
//                return containmentRitual;
//
//            case 6:
//                return boundSoulRitual;
//
//            case 7:
//            	return unbindingRitual;
//
//            case 8:
//            	return jumpingRitual;
//
//            default:
//                return null;
//        }
    }

    private List<RitualComponent> obtainComponents()
    {
        return this.components;
    }

    private int getCrystalLevel()
    {
        return this.crystalLevel;
    }

    public static void performEffect(TEMasterStone ritualStone, int ritualID)
    {
        if (ritualID <= ritualList.size())
        {
            RitualEffect ef = ritualList.get(ritualID - 1).effect;

            if (ef != null)
            {
                ef.performEffect(ritualStone);
            }
        }
    }

    public static int getNumberOfRituals()
    {
        return ritualList.size();
    }

    public String getRitualName()
    {
        return this.name;
    }

    public static String getNameOfRitual(int id)
    {
        if (ritualList.get(id) != null)
        {
            return ritualList.get(id).getRitualName();
        } else
        {
            return "";
        }
    }
}
