package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockInteger;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.block.ItemBlockBloodTank;
import WayofTime.bloodmagic.tile.TileBloodTank;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockBloodTank extends BlockInteger implements IVariantProvider, IBMBlock {
    public static final AxisAlignedBB BOX = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.8, 0.75);

    public BlockBloodTank() {
        super(Material.IRON, TileBloodTank.CAPACITIES.length - 1, "tier");

        setUnlocalizedName(BloodMagic.MODID + ".bloodTank");
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.GLASS);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(BloodMagic.TAB_BM);
        setLightOpacity(0);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return BOX;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return BOX;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tile, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, tile, stack);
        world.setBlockToAir(pos);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean success = FluidUtil.interactWithFluidHandler(player, hand, world, blockPos, side);
        if (success) {
            world.checkLight(blockPos);
            world.updateComparatorOutputLevel(blockPos, this);
            world.markAndNotifyBlock(blockPos, world.getChunkFromBlockCoords(blockPos), state, state, 3);
            return true;
        }

        return true;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode)
            this.dropBlockAsItem(worldIn, pos, state, 0);
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState blockState, int fortune) {
        List<ItemStack> list = Lists.newArrayList();

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloodTank) {
            TileBloodTank bloodTank = (TileBloodTank) tile;
            ItemStack drop = new ItemStack(this, 1, bloodTank.getBlockMetadata());
            NBTTagCompound tag = new NBTTagCompound();

            if (bloodTank.getTank().getFluid() != null)
                bloodTank.getTank().getFluid().writeToNBT(tag);

            drop.setTagCompound(tag);
            list.add(drop);
        }

        return list;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState blockState, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloodTank) {
            TileBloodTank bloodTank = (TileBloodTank) tile;
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tag);
                bloodTank.getTank().setFluid(fluidStack);
            }
        }

        world.checkLight(pos);
        world.updateComparatorOutputLevel(pos, this);
        world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), blockState, blockState, 3);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloodTank) {
            FluidStack fluidStack = ((TileBloodTank) tile).getTank().getFluid();
            return fluidStack == null || fluidStack.amount <= 0 ? 0 : fluidStack.getFluid().getLuminosity(fluidStack);
        }

        return super.getLightValue(state, world, pos);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, getMetaFromState(state));
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloodTank)
            return ((TileBloodTank) tile).getComparatorOutput();
        return 0;
    }

    @Override
    public TileEntity createTileEntity(World worldIn, IBlockState blockState) {
        return new TileBloodTank(getMetaFromState(blockState));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public ItemBlock getItem() {
        return new ItemBlockBloodTank(this);
    }

    // IVariantProvider

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        for (int i = 0; i < TileBloodTank.CAPACITIES.length; i++)
            variants.put(i, "inventory");
    }
}
