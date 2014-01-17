package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EarthInk extends Item
{
    public EarthInk(int id)
    {
        super(id);
        this.maxStackSize = 1;
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ElementalInkEarth");
    }
}
