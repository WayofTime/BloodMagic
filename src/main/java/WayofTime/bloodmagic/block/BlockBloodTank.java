package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileBloodTank;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class BlockBloodTank extends BlockContainer implements IVariantProvider
{
    public static final PropertyInteger TIER = PropertyInteger.create("tier", 0, TileBloodTank.capacities.length - 1);

    public BlockBloodTank()
    {
        super(Material.IRON);

        setUnlocalizedName(Constants.Mod.MODID + ".bloodTank");
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.GLASS);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(BloodMagic.tabBloodMagic);
        setLightOpacity(0);

        setDefaultState(blockState.getBaseState().withProperty(TIER, 0));
    }

    // This is important!!! - DON'T DELETE - idk why
    @Override
    public TileEntity createTileEntity(World worldIn, IBlockState blockState)
    {
        return new TileBloodTank(getMetaFromState(blockState));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileBloodTank(meta);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TIER, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(TIER);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (world.getTileEntity(pos) == null)
            return state;
        return state.withProperty(TIER, world.getTileEntity(pos).getBlockMetadata());
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { TIER });
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileBloodTank fluidHandler = (TileBloodTank) world.getTileEntity(blockPos);
        if (FluidUtil.interactWithFluidHandler(heldItem, fluidHandler.getTank(), player))
        {
            world.notifyBlockUpdate(blockPos, state, state, 3);
            return true;
        }

        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (!player.capabilities.isCreativeMode)
            this.dropBlockAsItem(worldIn, pos, state, 0);
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos blockPos, IBlockState blockState, int fortune)
    {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();

        if (world.getTileEntity(blockPos) instanceof TileBloodTank)
        {
            TileBloodTank bloodTank = (TileBloodTank) world.getTileEntity(blockPos);
            ItemStack drop = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            bloodTank.serialize(tag);
            drop.setTagCompound(tag);
            drop.setItemDamage(getMetaFromState(blockState));
            list.add(drop);
        }

        return list;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase placer, ItemStack stack)
    {
        if (world.getTileEntity(blockPos) != null && world.getTileEntity(blockPos) instanceof TileBloodTank)
        {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null)
            {
                ((TileBloodTank) world.getTileEntity(blockPos)).deserialize(tag);
                blockState.withProperty(TIER, stack.getMetadata());
            }
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBloodTank)
        {
            FluidStack fluidStack = ((TileBloodTank) tile).getTank().getFluid();
            return fluidStack == null || fluidStack.amount <= 0 ? 0 : fluidStack.getFluid().getLuminosity(fluidStack);
        }

        return super.getLightValue(state, world, pos);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return getDrops(world, pos, world.getBlockState(pos), 0).get(0);
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < TileBloodTank.capacities.length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "tier=" + i));
        return ret;
    }
}
