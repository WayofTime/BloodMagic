package WayofTime.alchemicalWizardry.common.achievements;

import WayofTime.alchemicalWizardry.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class AchievementsRegistry
{
    public static List<Item> list = new ArrayList();

    public void addItemsToList()
    {
        list.add(ModItems.sacrificialDagger);
    }
}
