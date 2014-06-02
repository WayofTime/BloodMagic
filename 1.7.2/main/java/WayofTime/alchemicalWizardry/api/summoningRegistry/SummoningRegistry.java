package WayofTime.alchemicalWizardry.api.summoningRegistry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SummoningRegistry
{
    public static List<SummoningRegistryComponent> summoningList = new ArrayList();

    public static void registerSummon(SummoningHelper s, ItemStack[] ring1, ItemStack[] ring2, ItemStack[] ring3, int amountUsed, int bloodOrbLevel)
    {
        summoningList.add(new SummoningRegistryComponent(s, ring1, ring2, ring3, amountUsed, bloodOrbLevel));
    }

    public static boolean isRecipeValid(int bloodOrbLevel, ItemStack[] test1, ItemStack[] test2, ItemStack[] test3)
    {
        for (SummoningRegistryComponent src : summoningList)
        {
            if (src.getBloodOrbLevel() <= bloodOrbLevel && src.compareRing(1, test1) && src.compareRing(2, test2) && src.compareRing(3, test3))
            {
                return true;
            }
        }

        return false;
    }

    public static SummoningRegistryComponent getRegistryComponent(int bloodOrbLevel, ItemStack[] test1, ItemStack[] test2, ItemStack[] test3)
    {
        for (SummoningRegistryComponent src : summoningList)
        {
            if (src.getBloodOrbLevel() <= bloodOrbLevel && src.compareRing(1, test1) && src.compareRing(2, test2) && src.compareRing(3, test3))
            {
                return src;
            }
        }

        return null;
    }

    public static EntityLivingBase getEntity(World worldObj, int bloodOrbLevel, ItemStack[] test1, ItemStack[] test2, ItemStack[] test3)
    {
        for (SummoningRegistryComponent src : summoningList)
        {
            if (src.getBloodOrbLevel() <= bloodOrbLevel && src.compareRing(1, test1) && src.compareRing(2, test2) && src.compareRing(3, test3))
            {
                return src.getEntity(worldObj);
            }
        }

        return null;
    }

    public static EntityLivingBase getEntityWithID(World worldObj, int id)
    {
        for (SummoningRegistryComponent src : summoningList)
        {
            if (src.getSummoningHelperID() == id)
            {
                return src.getEntity(worldObj);
            }
        }

        return null;
    }
}
