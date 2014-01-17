package WayofTime.alchemicalWizardry.common.items.potion;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class StandardFillingAgent extends WeakFillingAgent
{
    public StandardFillingAgent(int par1)
    {
        super(par1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public int getFilledAmountForPotionNumber(int potionEffects)
    {
        //Random rand = new Random();
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
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:StandardFillingAgent");
    }
}
