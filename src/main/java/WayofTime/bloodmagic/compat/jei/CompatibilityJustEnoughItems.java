package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.compat.ICompatibility;
import WayofTime.bloodmagic.registry.ModBlocks;
import mezz.jei.api.JEIManager;
import net.minecraft.item.ItemStack;

public class CompatibilityJustEnoughItems implements ICompatibility {

    @Override
    public void loadCompatibility() {
        JEIManager.itemBlacklist.addItemToBlacklist(new ItemStack(ModBlocks.bloodLight));
    }

    @Override
    public String getModId() {
        return "JEI";
    }

    @Override
    public boolean enableCompat() {
        return true;
    }
}
