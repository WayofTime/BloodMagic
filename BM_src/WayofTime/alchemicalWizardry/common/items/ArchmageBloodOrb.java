package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArchmageBloodOrb extends EnergyBattery
{
    public ArchmageBloodOrb(int id, int damage)
    {
        super(id, damage);
        orbLevel = 5;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ArchmageBloodOrb");
    }
}
