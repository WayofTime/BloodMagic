package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ILeftClickEffect
{
    int onLeftClickEntity(ItemStack stack, EntityLivingBase attacked, EntityLivingBase weilder);
}
