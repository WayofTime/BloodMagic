package WayofTime.bloodmagic.block.base;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockEnumContainer<E extends Enum<E> & IStringSerializable> extends BlockEnum<E>
{
    public BlockEnumContainer(Material material, Class<E> enumClass, String propName)
    {
        super(material, enumClass, propName);
    }

    public BlockEnumContainer(Material material, Class<E> enumClass)
    {
        this(material, enumClass, "type");
    }

    @Override
    public abstract boolean hasTileEntity(IBlockState state);

    @Override
    public abstract TileEntity createTileEntity(World world, IBlockState state);

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam)
    {
        super.eventReceived(state, worldIn, pos, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }
}
