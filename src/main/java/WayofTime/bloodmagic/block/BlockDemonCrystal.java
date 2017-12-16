package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.item.ItemDemonCrystal;
import WayofTime.bloodmagic.item.block.ItemBlockDemonCrystal;
import WayofTime.bloodmagic.tile.TileDemonCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDemonCrystal extends Block implements IBMBlock {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 6);
    public static final PropertyEnum<EnumDemonWillType> TYPE = PropertyEnum.<EnumDemonWillType>create("type", EnumDemonWillType.class);
    public static final PropertyEnum<EnumFacing> ATTACHED = PropertyEnum.<EnumFacing>create("attached", EnumFacing.class);

    public BlockDemonCrystal() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumDemonWillType.DEFAULT).withProperty(ATTACHED, EnumFacing.UP));

        setUnlocalizedName(BloodMagic.MODID + ".demonCrystal.");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
        BlockPos offsetPos = pos.offset(side.getOpposite());
        IBlockState offsetState = world.getBlockState(offsetPos);
        Block offsetBlock = offsetState.getBlock();

        return offsetBlock.isSideSolid(offsetState, world, offsetPos, side) && this.canPlaceBlockAt(world, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        TileDemonCrystal tile = (TileDemonCrystal) world.getTileEntity(pos);
        EnumFacing placement = tile.getPlacement();
        BlockPos offsetPos = pos.offset(placement.getOpposite());
        IBlockState offsetState = world.getBlockState(offsetPos);
        Block offsetBlock = offsetState.getBlock();

        if (!offsetBlock.isSideSolid(offsetState, world, offsetPos, placement)) {
            world.setBlockToAir(pos);
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (world.getTileEntity(pos) == null) {
            return state;
        }
        TileDemonCrystal tile = (TileDemonCrystal) world.getTileEntity(pos);
        return state.withProperty(AGE, tile.getCrystalCountForRender()).withProperty(ATTACHED, tile.getPlacement());
    }

    @Override
    public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        for (int i = 0; i < EnumDemonWillType.values().length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
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

//    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
//    {
//        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
//    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumDemonWillType.values()[meta]);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE, AGE, ATTACHED);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileDemonCrystal) {
            EnumDemonWillType type = state.getValue(TYPE);
            int number = ((TileDemonCrystal) tile).getCrystalCount();

            spawnAsEntity(world, pos, getItemStackDropped(type, number));
            world.removeTileEntity(pos);
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        TileDemonCrystal crystal = (TileDemonCrystal) world.getTileEntity(pos);
        if (crystal == null)
            return false;

        if (PlayerDemonWillHandler.getTotalDemonWill(EnumDemonWillType.DEFAULT, player) > 1024) {
            crystal.dropSingleCrystal();

            world.notifyBlockUpdate(pos, state, state, 3);
        }

        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDemonCrystal();
    }

    @Nullable
    @Override
    public ItemBlock getItem() {
        return new ItemBlockDemonCrystal(this);
    }

    public static ItemStack getItemStackDropped(EnumDemonWillType type, int crystalNumber) {
        return type.getStack(crystalNumber);
    }
}