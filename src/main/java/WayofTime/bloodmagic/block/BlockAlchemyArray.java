package WayofTime.bloodmagic.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.util.Utils;

public class BlockAlchemyArray extends BlockContainer
{
    public BlockAlchemyArray()
    {
        super(Material.cloth);

        setUnlocalizedName(Constants.Mod.MODID + ".alchemyArray");
        setRegistryName(Constants.BloodMagicBlock.ALCHEMY_ARRAY.getRegName());
        setHardness(0.1f);
        setBlockBounds(0, 0, 0, 1, 0.1f, 1);
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
    {
        // No-op
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileAlchemyArray array = (TileAlchemyArray) world.getTileEntity(pos);

        if (array == null || player.isSneaking())
            return false;

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (array.getStackInSlot(0) == null)
            {
                Utils.insertItemToTile(array, player, 0);
            } else if (array.getStackInSlot(0) != null)
            {
                Utils.insertItemToTile(array, player, 1);
                array.attemptCraft();
            } else
            {
                return true;
            }
        }

        world.markBlockForUpdate(pos);
        return true;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(ModItems.arcaneAshes);
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileAlchemyArray();
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileAlchemyArray alchemyArray = (TileAlchemyArray) world.getTileEntity(blockPos);
        if (alchemyArray != null)
            alchemyArray.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }
}
