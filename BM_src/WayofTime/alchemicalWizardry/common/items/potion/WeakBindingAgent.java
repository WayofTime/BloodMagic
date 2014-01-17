package WayofTime.alchemicalWizardry.common.items.potion;

import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WeakBindingAgent extends StandardBindingAgent
{
    public WeakBindingAgent(int id)
    {
        super(id);
        // TODO Auto-generated constructor stub
    }

    @Override
    public float getSuccessRateForPotionNumber(int potions)
    {
        return (float) Math.pow(0.4, potions);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WeakBindingAgent");
    }
}
