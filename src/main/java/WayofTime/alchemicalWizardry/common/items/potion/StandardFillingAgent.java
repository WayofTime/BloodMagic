package WayofTime.alchemicalWizardry.common.items.potion;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class StandardFillingAgent extends WeakFillingAgent
{
    public StandardFillingAgent()
    {
        super();
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public int getFilledAmountForPotionNumber(int potionEffects)
    {
        if (potionEffects == 0)
        {
            return 8;
        }

        if (potionEffects >= 1 && potionEffects <= 3)
        {
            return (int) (4 * (Math.pow(0.5f, potionEffects - 1) + 0.01f));
        }

        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:StandardFillingAgent");
    }
}
