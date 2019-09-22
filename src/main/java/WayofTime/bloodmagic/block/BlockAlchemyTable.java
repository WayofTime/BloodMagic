package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.item.block.ItemBlockAlchemyTable;
import WayofTime.bloodmagic.tile.TileAlchemyTable;
import WayofTime.bloodmagic.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAlchemyTable extends Block implements IBMBlock {
    public static final PropertyBool INVISIBLE = PropertyBool.create("invisible");
    public static final PropertyEnum<Direction> DIRECTION = PropertyEnum.create("direction", Direction.class);
    private static final AxisAlignedBB BODY = new AxisAlignedBB(0, 0, 0, 16 / 16F, 13 / 16F, 16 / 16F);

    public BlockAlchemyTable() {
        super(Material.ROCK);
//        this.setDefaultState(this.blockState.getBaseState().withProperty(DIRECTION, EnumFacing.DOWN).withProperty(INVISIBLE, false));

        setTranslationKey(BloodMagic.MODID + ".alchemyTable");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);

//        setBlockBounds(0.3F, 0F, 0.3F, 0.72F, 1F, 0.72F);
    }

    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return BODY;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(BlockState state) {
        return 0;
    }

    @Override
    public BlockState getActualState(BlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAlchemyTable) {
            return state.withProperty(INVISIBLE, ((TileAlchemyTable) tile).isInvisible()).withProperty(DIRECTION, ((TileAlchemyTable) tile).getDirection());
        }

        return state.withProperty(INVISIBLE, false);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DIRECTION, INVISIBLE);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
        BlockPos position = pos;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAlchemyTable) {
            if (((TileAlchemyTable) tile).isSlave()) {
                position = ((TileAlchemyTable) tile).getConnectedPos();
                tile = world.getTileEntity(position);
                if (!(tile instanceof TileAlchemyTable)) {
                    return false;
                }
            }
        }

        player.openGui(BloodMagic.instance, Constants.Gui.ALCHEMY_TABLE_GUI, world, position.getX(), position.getY(), position.getZ());

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, BlockState blockState) {
        TileAlchemyTable tile = (TileAlchemyTable) world.getTileEntity(pos);
        if (tile != null && !tile.isSlave()) {
            tile.dropItems();
        }

        super.breakBlock(world, pos, blockState);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, BlockState state) {
        return new TileAlchemyTable();
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        TileAlchemyTable tile = (TileAlchemyTable) world.getTileEntity(pos);
        if (tile != null) {
            BlockPos connectedPos = tile.getConnectedPos();
            TileEntity connectedTile = world.getTileEntity(connectedPos);
            if (!(connectedTile instanceof TileAlchemyTable && ((TileAlchemyTable) connectedTile).getConnectedPos().equals(pos))) {
                this.breakBlock(world, pos, state);
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public BlockItem getItem() {
        return new ItemBlockAlchemyTable(this);
    }
}
