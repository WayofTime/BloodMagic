package WayofTime.alchemicalWizardry.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;

public class LifeEssenceStill extends BlockStationary
{
    protected LifeEssenceStill(int par1)
    {
        super(par1, Material.water);
        this.blockHardness = 100.0F;
        this.setLightOpacity(3);
        //this.setLightValue(10);
        this.disableStats();
        //setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.theIcon = new Icon[]
        {
            iconRegister.registerIcon("AlchemicalWizardry:lifeEssenceStill"),
            iconRegister.registerIcon("AlchemicalWizardry:lifeEssenceFlowing")
        };
    }
}