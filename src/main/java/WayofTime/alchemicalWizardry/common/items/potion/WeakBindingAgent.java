package WayofTime.alchemicalWizardry.common.items.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class WeakBindingAgent extends StandardBindingAgent
{
    public WeakBindingAgent()
    {
        super();
    }

    @Override
    public float getSuccessRateForPotionNumber(int potions)
    {
        return (float) Math.pow(0.4, potions);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WeakBindingAgent");
    }
}
