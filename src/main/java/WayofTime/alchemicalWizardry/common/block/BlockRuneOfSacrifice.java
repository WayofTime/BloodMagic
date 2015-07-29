package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockRuneOfSacrifice extends BlockBloodRune
{
    public BlockRuneOfSacrifice()
    {
        super();
        this.setBlockName("runeOfSacrifice");
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:RuneOfSacrifice");
    }

    @Override
    public int getRuneEffect(int metaData)
    {
        return 3;
    }
}
