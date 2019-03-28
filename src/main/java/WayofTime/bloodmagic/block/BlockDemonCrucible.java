package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.soul.IDemonWillGem;
import WayofTime.bloodmagic.soul.IDiscreteDemonWill;
import WayofTime.bloodmagic.tile.TileDemonCrucible;
import WayofTime.bloodmagic.util.Utils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockDemonCrucible extends Block implements IVariantProvider, IBMBlock {
    protected static final AxisAlignedBB BODY = new AxisAlignedBB(2 / 16F, 7 / 16F, 2 / 16F, 14 / 16F, 15 / 16F, 14 / 16F);
    private static final AxisAlignedBB[] ARMS = {
            new AxisAlignedBB(5 / 16F, 13 / 16F, 0 / 16F, 11 / 16F, 25 / 16F, 2 / 16F), // N
            new AxisAlignedBB(14 / 16F, 13 / 16F, 5 / 16F, 16 / 16F, 25 / 16F, 11 / 16F), // E
            new AxisAlignedBB(5 / 16F, 13 / 16F, 14 / 16F, 11 / 16F, 25 / 16F, 16 / 16F), // S
            new AxisAlignedBB(0 / 16F, 13 / 16F, 5 / 16F, 2 / 16F, 25 / 16F, 11 / 16F)  // W
    };
    private static final AxisAlignedBB[] FEET = {
            new AxisAlignedBB(10 / 16F, 0F, 2 / 16F, 14 / 16F, 7 / 16F, 6 / 16F), // NE
            new AxisAlignedBB(10 / 16F, 0F, 10 / 16F, 14 / 16F, 7 / 16F, 14 / 16F), // SE
            new AxisAlignedBB(2 / 16F, 0F, 10 / 16F, 6 / 16F, 7 / 16F, 14 / 16F), // SW
            new AxisAlignedBB(2 / 16F, 0F, 2 / 16F, 6 / 16F, 7 / 16F, 6 / 16F)  // NW
    };


    public BlockDemonCrucible() {
        super(Material.ROCK);

        setTranslationKey(BloodMagic.MODID + ".demonCrucible");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);

//        setBlockBounds(0.3F, 0F, 0.3F, 0.72F, 1F, 0.72F);
    }

    private static List<AxisAlignedBB> getCollisionBoxList(IBlockState state) {
        ArrayList<AxisAlignedBB> collBox = new ArrayList<>(Arrays.asList(ARMS));
        collBox.add(BODY);
        collBox.addAll(Arrays.asList(FEET));
        return collBox;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BODY;
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
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        TileDemonCrucible crucible = (TileDemonCrucible) world.getTileEntity(pos);

        if (crucible == null || player.isSneaking())
            return false;

        if (!heldItem.isEmpty()) {
            if (!(heldItem.getItem() instanceof IDiscreteDemonWill) && !(heldItem.getItem() instanceof IDemonWillGem)) {
                return true;
            }
        }

        Utils.insertItemToTile(crucible, player);

        world.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        TileDemonCrucible tile = (TileDemonCrucible) world.getTileEntity(blockPos);
        if (tile != null)
            tile.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDemonCrucible();
    }

    @Override
    public ItemBlock getItem() {
        return new ItemBlock(this);
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
