package WayofTime.alchemicalWizardry.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class ApprenticeBloodOrb extends EnergyBattery {
    public ApprenticeBloodOrb(int id, int damage)
    {
        super(id, damage);
        orbLevel = 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ApprenticeBloodOrb");
    }
}
