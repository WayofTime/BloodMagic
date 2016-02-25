package WayofTime.bloodmagic.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.TilePhantomBlock;

public class BlockPhantom extends BlockContainer
{
    public BlockPhantom()
    {
        super(Material.cloth);

        setUnlocalizedName(Constants.Mod.MODID + ".phantom");
        setRegistryName(Constants.BloodMagicBlock.PHANTOM.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isTranslucent()
    {
        return true;
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate)
        {
            return true;
        }

        return block != this && super.shouldSideBeRendered(worldIn, pos, side);
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TilePhantomBlock(100);
    }
}
