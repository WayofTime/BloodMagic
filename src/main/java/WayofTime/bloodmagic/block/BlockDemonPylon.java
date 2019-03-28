package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileDemonPylon;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
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

public class BlockDemonPylon extends BlockContainer implements IBMBlock, IVariantProvider {
    protected static final AxisAlignedBB BODY = new AxisAlignedBB(2 / 16F, 7 / 16F, 2 / 16F, 14 / 16F, 20 / 16F, 14 / 16F);
    private static final AxisAlignedBB[] FEET = {
            new AxisAlignedBB(10 / 16F, 0F, 2 / 16F, 14 / 16F, 7 / 16F, 6 / 16F), // NE
            new AxisAlignedBB(10 / 16F, 0F, 10 / 16F, 14 / 16F, 7 / 16F, 14 / 16F), // SE
            new AxisAlignedBB(2 / 16F, 0F, 10 / 16F, 6 / 16F, 7 / 16F, 14 / 16F), // SW
            new AxisAlignedBB(2 / 16F, 0F, 2 / 16F, 6 / 16F, 7 / 16F, 6 / 16F)  // NW
    };
    private static final AxisAlignedBB[] ARMS = {};

    public BlockDemonPylon() {
        super(Material.ROCK);

        setTranslationKey(BloodMagic.MODID + ".demonPylon");
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
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileDemonPylon();
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
