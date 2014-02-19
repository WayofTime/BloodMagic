package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BloodRune extends Block
{
    //private Icon bloodRuneIcon;
    private IIcon altarCapacityRuneIcon;
    private IIcon dislocationRuneIcon;
    private IIcon orbCapacityRuneIcon;

    public BloodRune()
    {
        super(Material.iron);
        this.setBlockName("bloodRune");
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:BlankRune");
        this.altarCapacityRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:AltarCapacityRune");
        this.dislocationRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:DislocationRune");
        this.orbCapacityRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:OrbCapacityRune");
    }

    public int getRuneEffect(int metaData)
    {
        switch (metaData)
        {
            case 0:
                return 0;

            case 1: //Altar Capacity rune
                return 5;

            case 2: //Filling/emptying rune
                return 6;

            case 3: //Orb Capacity rune
                return 7;
        }

        return 0;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.equals(ModBlocks.bloodRune))
        {
            par3List.add(new ItemStack(par1, 1, 0));
            par3List.add(new ItemStack(par1, 1, 1));
            par3List.add(new ItemStack(par1, 1, 2));
            par3List.add(new ItemStack(par1, 1, 3));
        } else
        {
            super.getSubBlocks(par1, par2CreativeTabs, par3List);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        switch (meta)
        {
            case 0:
                return blockIcon;

            case 1:
                return altarCapacityRuneIcon;

            case 2:
                return dislocationRuneIcon;

            case 3:
                return this.orbCapacityRuneIcon;

            default:
                return blockIcon;
        }
    }

    @Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }
}
