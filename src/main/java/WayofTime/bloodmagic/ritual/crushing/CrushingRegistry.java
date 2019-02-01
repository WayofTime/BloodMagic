package WayofTime.bloodmagic.ritual.crushing;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;


public class CrushingRegistry {

    private static List<ICrushingHandler> crushingHandlerList = new ArrayList<>();

    public static void registerCuttingFluid(ICrushingHandler handler) {
        crushingHandlerList.add(handler);
    }

    public static List<ICrushingHandler> getCrushingHandlerList() {
        return crushingHandlerList;
    }
}
