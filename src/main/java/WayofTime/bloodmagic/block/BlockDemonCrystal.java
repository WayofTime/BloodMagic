package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.block.ItemBlockDemonCrystal;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.tile.TileDemonCrystal;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockDemonCrystal extends Block implements IBMBlock, IVariantProvider {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 6);
    public static final PropertyEnum<EnumDemonWillType> TYPE = PropertyEnum.create("type", EnumDemonWillType.class);
    public static final PropertyEnum<EnumFacing> ATTACHED = PropertyEnum.create("attached", EnumFacing.class);

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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileDemonCrystal) {
            TileDemonCrystal crystal = (TileDemonCrystal) tile;

            if (PlayerDemonWillHandler.getTotalDemonWill(EnumDemonWillType.DEFAULT, player) > 1024)
                crystal.dropSingleCrystal();
        }

        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileDemonCrystal) {
            EnumDemonWillType type = state.getValue(TYPE);
            int number = ((TileDemonCrystal) tile).getCrystalCount();

            drops.add(getItemStackDropped(type, number));
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileDemonCrystal) {
            TileDemonCrystal crystal = (TileDemonCrystal) tile;
            state = state.withProperty(AGE, crystal.getCrystalCountForRender());
            state = state.withProperty(ATTACHED, crystal.getPlacement());
        }
        return state;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileDemonCrystal) {
            TileDemonCrystal crystal = (TileDemonCrystal) tile;
            EnumFacing placement = crystal.getPlacement();
            BlockPos offsetPos = pos.offset(placement.getOpposite());
            IBlockState offsetState = world.getBlockState(offsetPos);

            if (!offsetState.isSideSolid(world, offsetPos, placement))
                world.destroyBlock(pos, true);
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
        BlockPos offsetPos = pos.offset(side.getOpposite());
        IBlockState offsetState = world.getBlockState(offsetPos);

        return offsetState.isSideSolid(world, offsetPos, side) && this.canPlaceBlockAt(world, pos);
    }

    @Override
    public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        for (EnumDemonWillType willType : EnumDemonWillType.values())
            list.add(new ItemStack(this, 1, willType.ordinal()));
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tile, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, tile, stack);
        world.setBlockToAir(pos);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
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

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumDemonWillType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE, AGE, ATTACHED);
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

    public static ItemStack getItemStackDropped(EnumDemonWillType type, int crystalNumber) {
        ItemStack stack = ItemStack.EMPTY;
        switch (type) {
            case CORROSIVE:
                stack = EnumDemonWillType.CORROSIVE.getStack();
                break;
            case DEFAULT:
                stack = EnumDemonWillType.DEFAULT.getStack();
                break;
            case DESTRUCTIVE:
                stack = EnumDemonWillType.DESTRUCTIVE.getStack();
                break;
            case STEADFAST:
                stack = EnumDemonWillType.STEADFAST.getStack();
                break;
            case VENGEFUL:
                stack = EnumDemonWillType.VENGEFUL.getStack();
                break;
        }

        stack.setCount(crystalNumber);
        return stack;
    }

    @Override
    public ItemBlock getItem() {
        return new ItemBlockDemonCrystal(this);
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        for (EnumDemonWillType willType : EnumDemonWillType.values())
            variants.put(willType.ordinal(), "age=3,attached=up,type=" + willType.getName());
    }
}