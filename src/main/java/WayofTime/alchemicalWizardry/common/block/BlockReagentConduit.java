package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.tileEntity.TEReagentConduit;

public class BlockReagentConduit extends BlockContainer
{
    public BlockReagentConduit()
    {
        super(Material.cloth);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TEReagentConduit();
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return super.onBlockActivated(world, blockPos, state, player, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

//    @Override
//    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
//    {
//        return null;
//    }
}
