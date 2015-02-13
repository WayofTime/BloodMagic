package WayofTime.alchemicalWizardry.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

import java.util.ArrayList;

public class PotionSoulFray extends Potion
{
    public PotionSoulFray(int par1, boolean par2, int par3)
    {
        super(par1, par2, par3);
    }

    @Override
    public Potion setIconIndex(int par1, int par2)
    {
        super.setIconIndex(par1, par2);
        return this;
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBase, int level)
    {
        entityLivingBase.getActivePotionEffect(this).setCurativeItems(new ArrayList<ItemStack>());
    }

    @Override
    public boolean isReady(int duration, int level)
    {
        return true;
    }
}
