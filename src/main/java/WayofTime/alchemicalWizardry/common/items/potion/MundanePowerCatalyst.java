package WayofTime.alchemicalWizardry.common.items.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class MundanePowerCatalyst extends PowerCatalyst
{
    public MundanePowerCatalyst()
    {
        super(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:MundanePowerCatalyst");
    }
}
