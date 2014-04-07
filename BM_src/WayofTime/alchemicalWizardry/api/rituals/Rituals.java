package WayofTime.alchemicalWizardry.api.rituals;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class Rituals
{
    private int crystalLevel;
    private int actCost;
    private RitualEffect effect;
    private String name;

    public static List<Rituals> ritualList = new ArrayList();

    public Rituals(int crystalLevel, int actCost, RitualEffect effect, String name)
    {
        this.crystalLevel = crystalLevel;
        this.actCost = actCost;
        this.effect = effect;
        this.name = name;
    }

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
    }

    public static boolean checkRitualIsValid(World world, int x, int y, int z, int ritualID)
    {
        int direction = Rituals.getDirectionOfRitual(world, x, y, z, ritualID);

        if (direction != -1)
        {
            return true;
        }

        return false;
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
                    test = Block.blocksList[world.getBlockId(x + rc.getX(), y + rc.getY(), z + rc.getZ())];

                    if (!(test instanceof IRitualStone))
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
                    test = Block.blocksList[world.getBlockId(x - rc.getZ(), y + rc.getY(), z + rc.getX())];

                    if (!(test instanceof IRitualStone))
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
                    test = Block.blocksList[world.getBlockId(x - rc.getX(), y + rc.getY(), z - rc.getZ())];

                    if (!(test instanceof IRitualStone))
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
                    test = Block.blocksList[world.getBlockId(x + rc.getZ(), y + rc.getY(), z - rc.getX())];

                    if (!(test instanceof IRitualStone))
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

    public static int getCostForActivation(int ritualID)
    {
        if (ritualID <= ritualList.size())
        {
            return ritualList.get(ritualID - 1).actCost;
        } else
        {
            return 0;
        }
    }

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
    }

    private List<RitualComponent> obtainComponents()
    {
        return this.effect.getRitualComponentList();
    }

    private int getCrystalLevel()
    {
        return this.crystalLevel;
    }

    public static void performEffect(IMasterRitualStone ritualStone, int ritualID)
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
