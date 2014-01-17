package WayofTime.alchemicalWizardry.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class MagicianBloodOrb extends EnergyBattery
{
    public MagicianBloodOrb(int id, int damage)
    {
        super(id, damage);
        orbLevel = 3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:MagicianBloodOrb");
    }
}
