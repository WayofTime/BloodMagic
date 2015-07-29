package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockBloodLightSource extends Block
{
    public BlockBloodLightSource()
    {
        super(Material.cloth);
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos blockPos)
    {
        return 15;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState blockState, Random rand)
    {
        if (rand.nextInt(3) != 0)
        {
            float f = 1.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;
            world.spawnParticle(EnumParticleTypes.REDSTONE, blockPos.getX() + 0.5D + rand.nextGaussian() / 8, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D + rand.nextGaussian() / 8, f1, f2, f3);
        }
    }

    @Override
    public void addCollisionBoxesToList(World par1World, BlockPos blockPos, IBlockState blockState, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        this.setBlockBounds(0.40F, 0.40F, 0.40F, 0.60F, 0.60F, 0.60F);
    }

    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
}
