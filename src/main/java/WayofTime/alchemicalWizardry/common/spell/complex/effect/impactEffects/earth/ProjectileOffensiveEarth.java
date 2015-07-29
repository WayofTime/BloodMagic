package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.ProjectileImpactEffect;

public class ProjectileOffensiveEarth extends ProjectileImpactEffect
{
    public ProjectileOffensiveEarth(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onEntityImpact(Entity mop, Entity proj)
    {
        int horizRange = (int) (this.powerUpgrades);
        int vertDepth = (int) (3 * this.potencyUpgrades + 1);

        BlockPos pos = proj.getPosition();

        World world = mop.worldObj;

        for (int i = -horizRange; i <= horizRange; i++)
        {
            for (int j = -vertDepth; j < 0; j++)
            {
                for (int k = -horizRange; k <= horizRange; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                    if (!world.isAirBlock(newPos))
                    {
                    	IBlockState state = world.getBlockState(newPos);
                        Block block = state.getBlock();
                        if (block == null || block.getBlockHardness(world, newPos) == -1)
                        {
                            continue;
                        }
                        if (block == Blocks.stone || block == Blocks.cobblestone || block == Blocks.sand || block == Blocks.gravel || block == Blocks.grass || block == Blocks.dirt)
                        {
                            world.setBlockToAir(newPos);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onTileImpact(World world, MovingObjectPosition mop)
    {
    }
}
