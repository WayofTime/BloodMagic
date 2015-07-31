package WayofTime.alchemicalWizardry.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ApprenticeBloodOrb extends EnergyBattery
{
    public ApprenticeBloodOrb(int damage)
    {
        super(damage);
        orbLevel = 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ApprenticeBloodOrb");
    }
}
