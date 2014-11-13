package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IToolUpdateEffect
{
    public abstract int onUpdate(ItemStack toolStack, World world, Entity par3Entity, int invSlot, boolean inHand);
}
