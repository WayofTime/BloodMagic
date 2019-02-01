package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.ItemDemonCrystal;
import WayofTime.bloodmagic.item.block.ItemBlockDemonCrystal;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.tile.TileDemonCrystal;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

public class BlockDemonCrystal extends Block implements IBMBlock, IVariantProvider {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 6);
    public static final PropertyEnum<EnumDemonWillType> TYPE = PropertyEnum.create("type", EnumDemonWillType.class);
    public static final PropertyEnum<EnumFacing> ATTACHED = PropertyEnum.create("attached", EnumFacing.class);
    private static final EnumMap<EnumFacing, AxisAlignedBB> bounds = new EnumMap<>(EnumFacing.class);

    // Bounding / Collision boxes
    private static final AxisAlignedBB[] UP = {
            new AxisAlignedBB(6 / 16F, 0, 5 / 16F, 10 / 16F, 13 / 16F, 9 / 16F),
            new AxisAlignedBB(7 / 16F, 0, 0, 13 / 16F, 6 / 16F, 5 / 16F),
            new AxisAlignedBB(9 / 16F, 0, 9 / 16F, 13 / 16F, 5 / 16F, 14 / 16F),
            new AxisAlignedBB(2 / 16F, 0, 1 / 16F, 7 / 16F, 6 / 16F, 7 / 16F),
            new AxisAlignedBB(5 / 16F, 0, 9 / 16F, 9 / 16F, 7 / 16F, 15 / 16F),
            new AxisAlignedBB(0, 0, 7 / 16F, 6 / 16F, 6 / 16F, 10 / 16F),
            new AxisAlignedBB(10 / 16F, 0, 6 / 16F, 15 / 16F, 6 / 16F, 9 / 16F)
    };
    private static final AxisAlignedBB[] DOWN = {
            new AxisAlignedBB(6 / 16F, 3 / 16F, 7 / 16F, 10 / 16F, 16 / 16F, 11 / 16F),
            new AxisAlignedBB(7 / 16F, 10 / 16F, 11 / 16F, 13 / 16F, 16 / 16F, 16 / 16F),
            new AxisAlignedBB(9 / 16F, 11 / 16F, 2 / 16F, 13 / 16F, 16 / 16F, 7 / 16F),
            new AxisAlignedBB(2 / 16F, 9 / 16F, 11 / 16F, 7 / 16F, 16 / 16F, 15 / 16F),
            new AxisAlignedBB(5 / 16F, 9 / 16F, 1 / 16F, 9 / 16F, 16 / 16F, 7 / 16F),
            new AxisAlignedBB(0, 10 / 16F, 6 / 16F, 6 / 16F, 16 / 16F, 9 / 16F),
            new AxisAlignedBB(10 / 16F, 11 / 16F, 7 / 16F, 15 / 16F, 16 / 16F, 10 / 16F)
    };
    private static final AxisAlignedBB[] NORTH = {
            new AxisAlignedBB(6 / 16F, 5 / 16F, 3 / 16F, 10 / 16F, 9 / 16F, 16 / 16F),
            new AxisAlignedBB(9 / 16F, 0, 6 / 16F, 13 / 16F, 5 / 16F, 16 / 16F),
            new AxisAlignedBB(8 / 16F, 9 / 16F, 11 / 16F, 13 / 16F, 14 / 16F, 16 / 16F),
            new AxisAlignedBB(2 / 16F, 1 / 16F, 9 / 16F, 7 / 16F, 7 / 16F, 16 / 16F),
            new AxisAlignedBB(5 / 16F, 9 / 16F, 9 / 16F, 9 / 16F, 15 / 16F, 16 / 16F),
            new AxisAlignedBB(0, 7 / 16F, 10 / 16F, 6 / 16F, 10 / 16F, 16 / 16F),
            new AxisAlignedBB(10 / 16F, 7 / 16F, 10 / 16F, 15 / 16F, 9 / 16F, 15 / 16F),
    };
    private static final AxisAlignedBB[] SOUTH = {
            new AxisAlignedBB(6 / 16F, 7 / 16F, 0 / 16F, 10 / 16F, 11 / 16F, 13 / 16F),
            new AxisAlignedBB(7 / 16F, 11 / 16F, 0, 13 / 16F, 16 / 16F, 6 / 16F),
            new AxisAlignedBB(8 / 16F, 2 / 16F, 9 / 16F, 13 / 16F, 7 / 16F, 14 / 16F),
            new AxisAlignedBB(2 / 16F, 9 / 16F, 1 / 16F, 7 / 16F, 14 / 16F, 7 / 16F),
            new AxisAlignedBB(5 / 16F, 1 / 16F, 9 / 16F, 9 / 16F, 7 / 16F, 9 / 16F),
            new AxisAlignedBB(0, 6 / 16F, 1 / 16F, 6 / 16F, 9 / 16F, 7 / 16F),
            new AxisAlignedBB(10 / 16F, 8 / 16F, 1 / 16F, 15 / 16F, 10 / 16F, 6 / 16F)
    };
    private static final AxisAlignedBB[] EAST = {
            new AxisAlignedBB(0, 6 / 16F, 5 / 16F, 13 / 16F, 10 / 16F, 9 / 16F),
            new AxisAlignedBB(0, 3 / 16F, 0, 6 / 16F, 9 / 16F, 5 / 16F),
            new AxisAlignedBB(0 / 16F, 3 / 16F, 9 / 16F, 5 / 16F, 8 / 16F, 14 / 16F),
            new AxisAlignedBB(1 / 16F, 9 / 16F, 1 / 16F, 7 / 16F, 13 / 16F, 7 / 16F),
            new AxisAlignedBB(1 / 16F, 0, 9 / 16F, 7 / 16F, 11 / 16F, 15 / 16F),
            new AxisAlignedBB(0, 10 / 16F, 7 / 16F, 6 / 16F, 16 / 16F, 10 / 16F),
            new AxisAlignedBB(0, 1 / 16F, 6 / 16F, 5 / 16F, 6 / 16F, 9 / 16F)
    };
    private static final AxisAlignedBB[] WEST = {
            new AxisAlignedBB(3 / 16F, 6 / 16F, 5 / 16F, 16 / 16F, 10 / 16F, 9 / 16F),
            new AxisAlignedBB(9 / 16F, 7 / 16F, 0, 16 / 16F, 12 / 16F, 5 / 16F),
            new AxisAlignedBB(11 / 16F, 4 / 16F, 9 / 16F, 16 / 16F, 13 / 16F, 14 / 16F),
            new AxisAlignedBB(9 / 16F, 3 / 16F, 1 / 16F, 16 / 16F, 8 / 16F, 7 / 16F),
            new AxisAlignedBB(9 / 16F, 6 / 16F, 9 / 16F, 16 / 16F, 8 / 16F, 15 / 16F),
            new AxisAlignedBB(10 / 16F, 1 / 16F, 7 / 16F, 16 / 16F, 6 / 16F, 10 / 16F),
            new AxisAlignedBB(10 / 16F, 6 / 16F, 6 / 16F, 15 / 16F, 15 / 16F, 9 / 16F)
    };

    public BlockDemonCrystal() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumDemonWillType.DEFAULT).withProperty(ATTACHED, EnumFacing.UP));

        setTranslationKey(BloodMagic.MODID + ".demonCrystal.");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
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

    // collects a sublist from 0 to age for the collision boxes
    private static List<AxisAlignedBB> getCollisionBoxList(IBlockState state) {
        int age = state.getValue(BlockDemonCrystal.AGE) + 1;
        switch (state.getValue(BlockDemonCrystal.ATTACHED)) {
            case DOWN:
                return Arrays.asList(DOWN).subList(0, age);
            case NORTH:
                return Arrays.asList(NORTH).subList(0, age);
            case SOUTH:
                return Arrays.asList(SOUTH).subList(0, age);
            case WEST:
                return Arrays.asList(WEST).subList(0, age);
            case EAST:
                return Arrays.asList(EAST).subList(0, age);
            case UP:
            default:
                return Arrays.asList(UP).subList(0, age);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        TileEntity tile = source.getTileEntity(pos);
        if (tile != null)
            state = getActualState(state, tile.getWorld(), pos);
        switch (state.getValue(ATTACHED)) {
            case DOWN:
                return DOWN[0];
            case NORTH:
                return NORTH[0];
            case SOUTH:
                return SOUTH[0];
            case EAST:
                return EAST[0];
            case WEST:
                return WEST[0];
            case UP:
            default:
                return UP[0];
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileDemonCrystal) {
                TileDemonCrystal crystal = (TileDemonCrystal) tile;
                boolean isCreative = player.capabilities.isCreativeMode;
                boolean holdsCrystal = player.getHeldItem(hand).getItem() instanceof ItemDemonCrystal;

                if (PlayerDemonWillHandler.getTotalDemonWill(EnumDemonWillType.DEFAULT, player) > 1024 && !(holdsCrystal && isCreative)) {
                    crystal.dropSingleCrystal();

                }
                if (!crystal.getWorld().isRemote && isCreative && holdsCrystal) {
                    if (crystal.crystalCount < 7) {
                        crystal.internalCounter = 0;
                        if (crystal.progressToNextCrystal > 0)
                            crystal.progressToNextCrystal--;
                        crystal.crystalCount++;
                        crystal.markDirty();
                        crystal.notifyUpdate();
                    }
                }
            }
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

    @Override
    public ItemBlock getItem() {
        return new ItemBlockDemonCrystal(this);
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        for (EnumDemonWillType willType : EnumDemonWillType.values())
            variants.put(willType.ordinal(), "age=3,attached=up,type=" + willType.getName());
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        List<RayTraceResult> list = Lists.newArrayList();


        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(this.getActualState(blockState, worldIn, pos))) {
            list.add(this.rayTrace(pos, start, end, axisalignedbb));
        }

        RayTraceResult rayTrace = null;
        double d1 = 0.0D;

        for (RayTraceResult raytraceresult : list) {
            if (raytraceresult != null) {
                double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if (d0 > d1) {
                    rayTrace = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return rayTrace;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        state = this.getActualState(state, worldIn, pos);

        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(state)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }

}