package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.items.BlankSpell;
import WayofTime.alchemicalWizardry.common.items.ItemComplexSpellCrystal;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellParadigmBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpellParadigm extends BlockOrientable 
{
	Icon[] projectileIcons = new Icon[7];
	
	public BlockSpellParadigm(int id) 
	{
		super(id);
		setUnlocalizedName("blockSpellParadigm");
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {      
        this.projectileIcons = this.registerIconsWithString(iconRegister, "projectileParadigmBlock");
    }
	
//	@Override
//	public Icon[] getIconsForMeta(int metadata)
//    {
//    	return this.projectileIcons;
//    }
	
	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TESpellParadigmBlock();
    }
	
	@SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (this.blockID == ModBlocks.blockSpellParadigm.blockID)
        {
            par3List.add(new ItemStack(par1, 1, 0));
            par3List.add(new ItemStack(par1, 1, 1));
            par3List.add(new ItemStack(par1, 1, 2));
        } else
        {
            super.getSubBlocks(par1, par2CreativeTabs, par3List);
        }
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float what, float these, float are)
	{
		ItemStack stack = player.getCurrentEquippedItem();
		
		if(stack != null && stack.getItem() instanceof ItemComplexSpellCrystal)
		{
            if (stack.stackTagCompound == null)
            {
                stack.setTagCompound(new NBTTagCompound());
            }

            NBTTagCompound itemTag = stack.stackTagCompound;
            itemTag.setInteger("xCoord", x);
            itemTag.setInteger("yCoord", y);
            itemTag.setInteger("zCoord", z);
            itemTag.setInteger("dimensionId", world.provider.dimensionId);
            return true;
		}
		
		return super.onBlockActivated(world, x, y, z, player, side, what, these, are);
	}
}
