package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class BlockDemonPortal extends BlockContainer
{
    public BlockDemonPortal()
    {
        super(Material.rock);
        setHardness(1000);
        setResistance(10000);
    }
    
    @Override
    public void onBlockHarvested(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player)
    {
    	TileEntity tile = world.getTileEntity(blockPos);
		if(tile instanceof TEDemonPortal)
		{
			((TEDemonPortal) tile).notifyPortalOfBreak();
		}
		
    	super.onBlockHarvested(world, blockPos, blockState, player);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TEDemonPortal();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return false;
        }

        TEDemonPortal tileEntity = (TEDemonPortal) world.getTileEntity(blockPos);

        tileEntity.rightClickBlock(player, side.getIndex());

        return false;
    }
}
