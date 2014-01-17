package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WaterScribeTool extends ScribeTool
{
    public WaterScribeTool(int id)
    {
        super(id, 1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WaterScribeTool");
    }
}
