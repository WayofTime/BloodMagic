package WayofTime.alchemicalWizardry.common.achievements;

import WayofTime.alchemicalWizardry.ModItems;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.stats.Achievement;

import java.util.ArrayList;
import java.util.List;

public class AchievementsRegistry
{
    public final static List<Item> craftinglist = new ArrayList();
    public final static List<Item> pickupList = new ArrayList();

    public static void init()
    {
        addItemsToCraftingList();
        addItemsToPickupList();
    }

    public static void addItemsToCraftingList()
    {
        craftinglist.add(ModItems.sacrificialDagger);
    }

    public static void addItemsToPickupList()
    {
        pickupList.add(ModItems.weakBloodOrb);
    }

    public static Achievement getAchievementForItem(Item item)
    {
        if (item == ModItems.sacrificialDagger)
        {
            return ModAchievements.firstPrick;
        }
        if (item == ModItems.weakBloodOrb)
        {
            return ModAchievements.weakOrb;
        }
        return null;
    }
    
    public static Achievement getAchievementForBlock(Block block)
    {
        return null;
    }
}
