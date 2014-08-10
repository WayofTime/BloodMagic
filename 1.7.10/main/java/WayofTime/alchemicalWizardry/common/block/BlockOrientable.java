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
    public BlockOrientable()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        //setUnlocalizedName("bloodSocket");
        //func_111022_d("AlchemicalWizardry:blocks");
    }

//    @Override
//    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
//    {
//        return false;
//    }

    @Override
    public TileEntity createNewTileEntity(World world, int dunno)
    {
        return new TEOrientable();
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
