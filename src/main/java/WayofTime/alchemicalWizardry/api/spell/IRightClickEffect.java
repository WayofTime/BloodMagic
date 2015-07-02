package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public interface IRightClickEffect
{
    //public abstract int onRightClickEntity(ItemStack stack, EntityLivingBase attacked, EntityLivingBase weilder);

    int onRightClickBlock(ItemStack stack, EntityLivingBase weilder, World world, MovingObjectPosition mop);

    int onRightClickAir(ItemStack stack, EntityLivingBase weilder);
}
