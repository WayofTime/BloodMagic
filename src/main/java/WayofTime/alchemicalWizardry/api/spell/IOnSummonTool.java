package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IOnSummonTool
{
    public abstract int onSummonTool(ItemStack toolStack, World world, Entity entity);
}
