package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.tileEntity.TEOrientable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockOrientable extends BlockContainer
{
    public BlockOrientable()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int dunno)
    {
        return new TEOrientable();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        //Right-click orients the output face. Shift-right-click orients the input face.
        if (world.isRemote)
        {
            return false;
        }

        TileEntity tile = world.getTileEntity(blockPos);

        if (tile instanceof TEOrientable)
        {
            TEOrientable newTile = (TEOrientable) tile;
            if (player.isSneaking())
            {
                int nextSide = TEOrientable.getIntForEnumFacing(newTile.getInputDirection()) + 1;

                if (nextSide > 5)
                {
                    nextSide = 0;
                }
                if (EnumFacing.getFront(nextSide) == newTile.getOutputDirection())
                {
                    nextSide++;
                    if (nextSide > 5)
                    {
                        nextSide = 0;
                    }
                }

                newTile.setInputDirection(EnumFacing.getFront(nextSide));
            } else
            {
                int nextSide = TEOrientable.getIntForEnumFacing(newTile.getOutputDirection()) + 1;

                if (nextSide > 5)
                {
                    nextSide = 0;
                }
                if (EnumFacing.getFront(nextSide) == newTile.getInputDirection())
                {
                    nextSide++;
                    if (nextSide > 5)
                    {
                        nextSide = 0;
                    }
                }

                newTile.setOutputDirection(EnumFacing.getFront(nextSide));
            }
        }

        world.markBlockForUpdate(blockPos);
        return true;
    }

    @Override
    public int damageDropped(IBlockState blockState)
    {
        return blockState.getBlock().damageDropped(blockState);
    }
}
