package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.items.ItemComplexSpellCrystal;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellParadigmBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpellParadigm extends BlockOrientable 
{
	public static final float minPos = (3f/16f);
	public static final float maxPos = (13f/16f);

	IIcon[] projectileIcons = new IIcon[7];
	
	public BlockSpellParadigm() 
	{
		super();
		this.setBlockName("blockSpellParadigm");
		//setBlockBounds(minPos, minPos, minPos, maxPos, maxPos, maxPos);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {      
        this.projectileIcons = this.registerIconsWithString(iconRegister, "projectileParadigmBlock");
    }
	
//	@Override
//	public Icon[] getIconsForMeta(int metadata)
//    {
//    	return this.projectileIcons;
//    }
	
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
	
	//TODO Need to make a renderer for the paradigm blocks and other spell blocks.
	/*
	@Override
	public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) 
	{

		setBlockBounds(minPos, minPos, minPos, maxPos, maxPos, maxPos);
		super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);


		TileEntity tile1 = world.getBlockTileEntity(i, j, k);
		if (tile1 instanceof TESpellParadigmBlock) 
		{
			TESpellParadigmBlock tileG = (TESpellParadigmBlock) tile1;


			if (tileG.isSideRendered(ForgeDirection.WEST)) 
			{
				setBlockBounds(0.0F, minPos, minPos, maxPos, maxPos, maxPos);
				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}


			if (tileG.isSideRendered(ForgeDirection.EAST)) 
			{
				setBlockBounds(minPos, minPos, minPos, 1.0F, maxPos, maxPos);
				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}


			if (tileG.isSideRendered(ForgeDirection.DOWN)) 
			{
				setBlockBounds(minPos, 0.0F, minPos, maxPos, maxPos, maxPos);
				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}


			if (tileG.isSideRendered(ForgeDirection.UP)) 
			{
				setBlockBounds(minPos, minPos, minPos, maxPos, 1.0F, maxPos);
				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}


			if (tileG.isSideRendered(ForgeDirection.NORTH)) 
			{
				setBlockBounds(minPos, minPos, 0.0F, maxPos, maxPos, maxPos);
				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}


			if (tileG.isSideRendered(ForgeDirection.SOUTH)) 
			{
				setBlockBounds(minPos, minPos, minPos, maxPos, maxPos, 1.0F);
				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}


//			float facadeThickness = TransportConstants.FACADE_THICKNESS;
//
//
//			if (tileG.hasFacade(ForgeDirection.EAST)) {
//				setBlockBounds(1 - facadeThickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
//			}
//
//
//			if (tileG.hasFacade(ForgeDirection.WEST)) {
//				setBlockBounds(0.0F, 0.0F, 0.0F, facadeThickness, 1.0F, 1.0F);
//				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
//			}
//
//
//			if (tileG.hasFacade(ForgeDirection.UP)) {
//				setBlockBounds(0.0F, 1 - facadeThickness, 0.0F, 1.0F, 1.0F, 1.0F);
//				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
//			}
//
//
//			if (tileG.hasFacade(ForgeDirection.DOWN)) {
//				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, facadeThickness, 1.0F);
//				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
//			}
//
//
//			if (tileG.hasFacade(ForgeDirection.SOUTH)) {
//				setBlockBounds(0.0F, 0.0F, 1 - facadeThickness, 1.0F, 1.0F, 1.0F);
//				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
//			}
//
//
//			if (tileG.hasFacade(ForgeDirection.NORTH)) {
//				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, facadeThickness);
//				super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
//			}
		}
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	*/
}
