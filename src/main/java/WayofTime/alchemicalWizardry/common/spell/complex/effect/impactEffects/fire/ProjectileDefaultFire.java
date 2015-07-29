package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.ProjectileImpactEffect;

public class ProjectileDefaultFire extends ProjectileImpactEffect
{
    public ProjectileDefaultFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onEntityImpact(Entity mop, Entity proj)
    {
        BlockPos pos = proj.getPosition();
        
        World world = mop.worldObj;

        int horizRange = 0;
        int vertRange = 0;

        for (int i = -horizRange; i <= horizRange; i++)
        {
            for (int j = -vertRange; j <= vertRange; j++)
            {
                for (int k = -horizRange; k <= horizRange; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                	
                    if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.fire.getDefaultState());
                    }
                }
            }
        }
    }

    @Override
    public void onTileImpact(World world, MovingObjectPosition mop)
    {
    	BlockPos pos = mop.func_178782_a();
        
        int horizRange = 0;
        int vertRange = 0;

        for (int i = -horizRange; i <= horizRange; i++)
        {
            for (int j = -vertRange; j <= vertRange; j++)
            {
                for (int k = -horizRange; k <= horizRange; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                	
                    if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.fire.getDefaultState());
                    }
                }
            }
        }
    }
}
