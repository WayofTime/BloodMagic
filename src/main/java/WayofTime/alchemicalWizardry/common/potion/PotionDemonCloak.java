package WayofTime.alchemicalWizardry.common.potion;

import net.minecraft.potion.Potion;

public class PotionDemonCloak extends Potion
{
    public PotionDemonCloak(int par1, boolean par2, int par3)
    {
        super(par1, par2, par3);
    }

    @Override
    public Potion setIconIndex(int par1, int par2)
    {
        super.setIconIndex(par1, par2);
        return this;
    }
}
