package WayofTime.alchemicalWizardry.common.items.potion;

import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GreaterLengtheningCatalyst extends LengtheningCatalyst
{
    public GreaterLengtheningCatalyst(int id)
    {
        super(id, 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:GreaterLengtheningCatalyst");
    }
}