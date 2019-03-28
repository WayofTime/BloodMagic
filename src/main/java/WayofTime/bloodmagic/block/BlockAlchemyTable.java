package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.item.block.ItemBlockAlchemyTable;
import WayofTime.bloodmagic.tile.TileAlchemyTable;
import WayofTime.bloodmagic.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAlchemyTable extends Block implements IBMBlock {
    public static final PropertyBool INVISIBLE = PropertyBool.create("invisible");
    public static final PropertyEnum<EnumFacing> DIRECTION = PropertyEnum.create("direction", EnumFacing.class);
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

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BODY;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
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
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        TileAlchemyTable tile = (TileAlchemyTable) world.getTileEntity(pos);
        if (tile != null && !tile.isSlave()) {
            tile.dropItems();
        }

        super.breakBlock(world, pos, blockState);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAlchemyTable();
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
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
    public ItemBlock getItem() {
        return new ItemBlockAlchemyTable(this);
    }
}
