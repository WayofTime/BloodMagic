package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MagicianBloodOrb extends EnergyBattery
{
    public MagicianBloodOrb(int damage)
    {
        super(damage);
        orbLevel = 3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:MagicianBloodOrb");
    }
}
