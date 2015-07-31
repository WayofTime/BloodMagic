package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class RuneOfSelfSacrifice extends BloodRune
{
    public RuneOfSelfSacrifice()
    {
        super();
        this.setBlockName("runeOfSelfSacrifice");
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:RuneOfSelfSacrifice");
    }

    @Override
    public int getRuneEffect(int metaData)
    {
        return 4;
    }
}
