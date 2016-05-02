package WayofTime.bloodmagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.TileAlchemyTable;

public class BlockAlchemyTable extends BlockContainer
{
    public static final PropertyBool INVISIBLE = PropertyBool.create("invisible");
    public static final PropertyEnum<EnumFacing> DIRECTION = PropertyEnum.<EnumFacing>create("direction", EnumFacing.class);

    public BlockAlchemyTable()
    {
        super(Material.ROCK);
//        this.setDefaultState(this.blockState.getBaseState().withProperty(DIRECTION, EnumFacing.DOWN).withProperty(INVISIBLE, false));

        setUnlocalizedName(Constants.Mod.MODID + ".alchemyTable");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);

//        setBlockBounds(0.3F, 0F, 0.3F, 0.72F, 1F, 0.72F);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAlchemyTable)
        {
            return state.withProperty(INVISIBLE, ((TileAlchemyTable) tile).isInvisible()).withProperty(DIRECTION, ((TileAlchemyTable) tile).getDirection());
        }

        return state.withProperty(INVISIBLE, false);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { DIRECTION, INVISIBLE });
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileAlchemyTable();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        BlockPos position = pos;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAlchemyTable)
        {
            if (((TileAlchemyTable) tile).isSlave())
            {
                position = ((TileAlchemyTable) tile).getConnectedPos();
                tile = world.getTileEntity(position);
                if (!(tile instanceof TileAlchemyTable))
                {
                    return false;
                }
            }
        }

        player.openGui(BloodMagic.instance, Constants.Gui.ALCHEMY_TABLE_GUI, world, position.getX(), position.getY(), position.getZ());

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState)
    {
        TileAlchemyTable tile = (TileAlchemyTable) world.getTileEntity(pos);
        if (tile != null && !tile.isSlave())
        {
            tile.dropItems();
        }

        super.breakBlock(world, pos, blockState);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        TileAlchemyTable tile = (TileAlchemyTable) world.getTileEntity(pos);
        if (tile != null)
        {
            BlockPos connectedPos = tile.getConnectedPos();
            TileEntity connectedTile = world.getTileEntity(connectedPos);
            if (!(connectedTile instanceof TileAlchemyTable && ((TileAlchemyTable) connectedTile).getConnectedPos().equals(pos)))
            {
                this.breakBlock(world, pos, state);
                world.setBlockToAir(pos);
            }
        }
    }
}
