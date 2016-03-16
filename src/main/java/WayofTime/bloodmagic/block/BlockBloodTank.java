package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.TileBloodTank;
import WayofTime.bloodmagic.util.Utils;

public class BlockBloodTank extends BlockContainer
{
    public BlockBloodTank()
    {
        super(Material.iron);

        setUnlocalizedName(Constants.Mod.MODID + ".bloodTank");
        setRegistryName(Constants.BloodMagicBlock.BLOOD_TANK.getRegName());
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeGlass);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileBloodTank();
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileBloodTank fluidHandler = (TileBloodTank) world.getTileEntity(blockPos);
        if (Utils.fillHandlerWithContainer(world, fluidHandler, player))
        {
            world.markBlockForUpdate(blockPos);
            return true;
        }
        if (Utils.fillContainerFromHandler(world, fluidHandler, player, fluidHandler.tank.getFluid()))
        {
            world.markBlockForUpdate(blockPos);
            return true;
        }
        if (FluidContainerRegistry.isContainer(player.getCurrentEquippedItem()))
        {
            world.markBlockForUpdate(blockPos);
            return true;
        }

        return super.onBlockActivated(world, blockPos, blockState, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
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
            bloodTank.writeToNBT(tag);
            drop.setTagCompound(tag);
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
                world.getTileEntity(blockPos).readFromNBT(tag);
            }
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileBloodTank)
        {
            TileBloodTank tank = (TileBloodTank) tile;
            FluidStack fluid = tank.tank.getFluid();
            if (fluid != null)
            {
                return fluid.getFluid().getLuminosity(fluid);
            }
        }
        return 0;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        return getDrops(world, pos, world.getBlockState(pos), 0).get(0);
    }
}
