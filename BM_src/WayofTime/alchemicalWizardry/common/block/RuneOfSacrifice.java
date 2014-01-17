package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;

public class RuneOfSacrifice extends BloodRune
{
    public RuneOfSacrifice(int id)
    {
        super(id);
        setUnlocalizedName("runeOfSacrifice");
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:RuneOfSacrifice");
    }

    @Override
    public int getRuneEffect(int metaData)
    {
        return 3;
    }
}
