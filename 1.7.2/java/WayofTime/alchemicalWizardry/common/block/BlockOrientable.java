package WayofTime.alchemicalWizardry.common.block;

import javax.swing.Icon;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEOrientable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOrientable extends BlockContainer
{    
    @SideOnly(Side.CLIENT)
    private static IIcon[] fireIcons;

    public BlockOrientable()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        //setUnlocalizedName("bloodSocket");
        //func_111022_d("AlchemicalWizardry:blocks");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {      
        this.fireIcons = this.registerIconsWithString(iconRegister, "fireEffectBlock");
    }

    @SideOnly(Side.CLIENT)
    public static IIcon[] registerIconsWithString(IIconRegister iconRegister, String blockString)
    {
    	IIcon[] icons = new IIcon[7];
    	
    	icons[0] = iconRegister.registerIcon("AlchemicalWizardry:" + blockString + "_input");
    	icons[1] = iconRegister.registerIcon("AlchemicalWizardry:" + blockString + "_output");
    	icons[2] = iconRegister.registerIcon("AlchemicalWizardry:" + blockString + "_upArrow");
    	icons[3] = iconRegister.registerIcon("AlchemicalWizardry:" + blockString + "_downArrow");
    	icons[4] = iconRegister.registerIcon("AlchemicalWizardry:" + blockString + "_leftArrow");
    	icons[5] = iconRegister.registerIcon("AlchemicalWizardry:" + blockString + "_rightArrow");
    	icons[6] = iconRegister.registerIcon("AlchemicalWizardry:" + blockString + "_blank");
    	
    	return icons;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
    	IIcon[] icons = this.getIconsForMeta(meta);
        switch (side)
        {
            case 4: return icons[1];
            default: return icons[6];
        }
    }

//    @Override
//    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
//    {
//        return false;
//    }
    
    @SideOnly(Side.CLIENT)

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public IIcon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {
    	TileEntity tile = par1IBlockAccess.getTileEntity(x, y, z);
    	int meta = par1IBlockAccess.getBlockMetadata(x, y, z);
    	
    	if(tile instanceof TEOrientable)
    	{
    		ForgeDirection input = ((TEOrientable)tile).getInputDirection();
    		ForgeDirection output = ((TEOrientable)tile).getOutputDirection();
    		
    		return this.getIconsForMeta(meta)[this.getTextureIndexForSideAndOrientation(side, input, output)];
    	}
    	
        return this.getIcon(side, meta);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int dunno)
    {
        return new TEOrientable();
    }
    
    public IIcon[] getIconsForMeta(int metadata)
    {
    	return this.fireIcons;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float what, float these, float are)
    {
    	//Right-click orients the output face. Shift-right-click orients the input face.
        if (world.isRemote)
        {
            return false;
        }

        ForgeDirection sideClicked = ForgeDirection.getOrientation(side);
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TEOrientable)
        {
        	TEOrientable newTile = (TEOrientable)tile;
        	if(player.isSneaking())
        	{
    			int nextSide = TEOrientable.getIntForForgeDirection(newTile.getInputDirection())+1;
    			
    			if(nextSide>5)
    			{
    				nextSide = 0;
    			}
    			if(ForgeDirection.getOrientation(nextSide)==newTile.getOutputDirection())
    			{
    				nextSide++;
    				if(nextSide>5)
    				{
    					nextSide = 0;
    				}
    			}
    			
    			newTile.setInputDirection(ForgeDirection.getOrientation(nextSide));
        	}else
        	{
    			int nextSide = TEOrientable.getIntForForgeDirection(newTile.getOutputDirection())+1;
    			
    			if(nextSide>5)
    			{
    				nextSide = 0;
    			}
    			if(ForgeDirection.getOrientation(nextSide)==newTile.getInputDirection())
    			{
    				nextSide++;
    				if(nextSide>5)
    				{
    					nextSide = 0;
    				}
    			}
    			
    			newTile.setOutputDirection(ForgeDirection.getOrientation(nextSide));
        	}
        }

        world.markBlockForUpdate(x, y, z);
        return true;
    }
    
    public int getTextureIndexForSideAndOrientation(int side, ForgeDirection input, ForgeDirection output)
    {
    	if(ForgeDirection.getOrientation(side) == input)
    	{
    		return 0;
    	}
    	if(ForgeDirection.getOrientation(side) == output)
    	{
    		return 1;
    	}
    	if(ForgeDirection.getOrientation(side) == output.getOpposite())
    	{
    		return 6;
    	}
    	
    	switch(side)
    	{
    	case 0: //BOTTOM
    		switch(output)
    		{
    		case NORTH: return 2; //UP
    		case SOUTH: return 3; //DOWN
    		case EAST: 	return 4; //LEFT
    		case WEST: 	return 5; //RIGHT
    		default: 	break;
    		}
    		break;
    	case 1: //TOP
    		switch(output)
    		{
    		case NORTH: return 2; //UP
    		case SOUTH: return 3; //DOWN
    		case EAST:	return 5;
    		case WEST:	return 4;
    		default: 	break;
    		}
    		break;
    	case 2: //NORTH
    		switch(output)
    		{
    		case DOWN:	return 3;
    		case UP: 	return 2;
    		case EAST:	return 4;
    		case WEST: 	return 5;
    		default: 	break;
    		}
    		break;
    	case 3: //SOUTH
    		switch(output)
    		{
    		case DOWN:	return 3;
    		case UP: 	return 2;
    		case EAST:	return 5;
    		case WEST: 	return 4;
    		default: 	break;
    		}
    		break;
    	case 4: //WEST
    		switch(output)
    		{
    		case DOWN:	return 3;
    		case UP: 	return 2;
    		case NORTH:	return 5;
    		case SOUTH: return 4;
    		default:	break;
    		}
    		break;
    	case 5: //EAST
    		switch(output)
    		{
    		case DOWN:	return 3;
    		case UP: 	return 2;
    		case NORTH:	return 4;
    		case SOUTH: return 5;
    		default: break;
    		}
    		break;
    	}
    	
    	return 0;
    }
    
    @Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }
    
    
}
