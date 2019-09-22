package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockInteger;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.block.ItemBlockBloodTank;
import WayofTime.bloodmagic.tile.TileBloodTank;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
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

public class BlockBloodTank extends BlockInteger implements IVariantProvider, IBMBlock {
    public static final AxisAlignedBB BOX = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.8, 0.75);

    public BlockBloodTank() {
        super(Material.IRON, TileBloodTank.CAPACITIES.length - 1, "tier");

        setTranslationKey(BloodMagic.MODID + ".bloodTank");
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.GLASS);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(BloodMagic.TAB_BM);
        setLightOpacity(0);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return BOX;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity tile, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, tile, stack);
        world.setBlockToAir(pos);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, BlockState state, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
        boolean success = FluidUtil.interactWithFluidHandler(player, hand, world, blockPos, side);
        if (success) {
            world.checkLight(blockPos);
            world.updateComparatorOutputLevel(blockPos, this);
            world.markAndNotifyBlock(blockPos, world.getChunk(blockPos), state, state, 3);
            return true;
        }

        return true;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.capabilities.isCreativeMode)
            this.dropBlockAsItem(worldIn, pos, state, 0);
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, BlockState blockState, int fortune) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloodTank) {
            TileBloodTank bloodTank = (TileBloodTank) tile;
            ItemStack drop = new ItemStack(this, 1, bloodTank.getBlockMetadata());
            CompoundNBT fluidTag = new CompoundNBT();

            if (bloodTank.getTank().getFluid() != null) {
                bloodTank.getTank().getFluid().writeToNBT(fluidTag);
                CompoundNBT dropTag = new CompoundNBT();
                dropTag.setTag("Fluid", fluidTag);
                drop.setTagCompound(dropTag);
            }

            drops.add(drop);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState blockState, LivingEntity placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloodTank) {
            TileBloodTank bloodTank = (TileBloodTank) tile;
            CompoundNBT tag = stack.getTagCompound();
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Fluid")) {
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("Fluid"));
                bloodTank.getTank().setFluid(fluidStack);
            }
        }

        world.checkLight(pos);
        world.updateComparatorOutputLevel(pos, this);
        world.markAndNotifyBlock(pos, world.getChunk(pos), blockState, blockState, 3);
    }

    @Override
    public int getLightValue(BlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloodTank) {
            FluidStack fluidStack = ((TileBloodTank) tile).getTank().getFluid();
            return fluidStack == null || fluidStack.amount <= 0 ? 0 : fluidStack.getFluid().getLuminosity(fluidStack);
        }

        return super.getLightValue(state, world, pos);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(this, 1, getMetaFromState(state));
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloodTank)
            return ((TileBloodTank) tile).getComparatorOutput();
        return 0;
    }

    @Override
    public TileEntity createTileEntity(World worldIn, BlockState blockState) {
        return new TileBloodTank(getMetaFromState(blockState));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public BlockItem getItem() {
        return new ItemBlockBloodTank(this);
    }

    // IVariantProvider

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        for (int i = 0; i < TileBloodTank.CAPACITIES.length; i++)
            variants.put(i, "inventory");
    }
}
