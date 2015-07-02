package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IOnBanishTool
{
    int onBanishTool(ItemStack toolStack, World world, Entity entity, int invSlot, boolean inHand);
}
