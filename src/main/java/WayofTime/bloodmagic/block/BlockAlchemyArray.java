package WayofTime.bloodmagic.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.util.Utils;

import javax.annotation.Nullable;

public class BlockAlchemyArray extends Block
{
    protected static final AxisAlignedBB ARRAY_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.1, 1);

    public BlockAlchemyArray()
    {
        super(Material.CLOTH);

        setUnlocalizedName(Constants.Mod.MODID + ".alchemyArray");
        setHardness(0.1f);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
    {
        // No-op
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAlchemyArray)
        {
            ((TileAlchemyArray) tile).onEntityCollidedWithBlock(state, entity);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return ARRAY_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean causesSuffocation(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        //TODO: Right click should rotate it
        TileAlchemyArray array = (TileAlchemyArray) world.getTileEntity(pos);

        if (array == null || player.isSneaking())
            return false;

        ItemStack playerItem = player.getHeldItem(hand);

        if (!playerItem.isEmpty())
        {
            if (array.getStackInSlot(0).isEmpty())
            {
                Utils.insertItemToTile(array, player, 0);
            } else if (!array.getStackInSlot(0).isEmpty())
            {
                Utils.insertItemToTile(array, player, 1);
                array.attemptCraft();
            } else
            {
                return true;
            }
        }

        world.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(ModItems.ARCANE_ASHES);
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileAlchemyArray alchemyArray = (TileAlchemyArray) world.getTileEntity(blockPos);
        if (alchemyArray != null)
            alchemyArray.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAlchemyArray();
    }
}
