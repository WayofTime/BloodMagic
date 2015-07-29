package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockLifeEssence extends BlockFluidClassic
{
    public BlockLifeEssence()
    {
        super(AlchemicalWizardry.lifeEssenceFluid, Material.water);
        AlchemicalWizardry.lifeEssenceFluid.setBlock(this);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos blockPos)
    {
        return !world.getBlockState(blockPos).getBlock().getMaterial().isLiquid() && super.canDisplace(world, blockPos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos blockPos)
    {
        return !world.getBlockState(blockPos).getBlock().getMaterial().isLiquid() && super.displaceIfPossible(world, blockPos);
    }
}
