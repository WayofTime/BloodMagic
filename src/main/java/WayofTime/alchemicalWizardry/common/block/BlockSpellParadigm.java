package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.items.ItemComplexSpellCrystal;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellParadigmBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public class BlockSpellParadigm extends BlockOrientable
{
    public BlockSpellParadigm()
    {
        super();
        this.setBlockName("blockSpellParadigm");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TESpellParadigmBlock();
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.equals(ModBlocks.blockSpellParadigm))
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float what, float these, float are)
    {
        ItemStack stack = player.getCurrentEquippedItem();

        if (stack != null && stack.getItem() instanceof ItemComplexSpellCrystal)
        {
            if (stack.getTagCompound() == null)
            {
                stack.setTagCompound(new NBTTagCompound());
            }

            NBTTagCompound itemTag = stack.getTagCompound();
            itemTag.setInteger("xCoord", x);
            itemTag.setInteger("yCoord", y);
            itemTag.setInteger("zCoord", z);
            itemTag.setInteger("dimensionId", world.provider.getDimensionId());
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, side, what, these, are);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }
}
