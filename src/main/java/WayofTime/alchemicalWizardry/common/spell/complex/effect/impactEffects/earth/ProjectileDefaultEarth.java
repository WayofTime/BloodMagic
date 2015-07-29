package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.ProjectileImpactEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class ProjectileDefaultEarth extends ProjectileImpactEffect
{
    public ProjectileDefaultEarth(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onEntityImpact(Entity mop, Entity proj)
    {
    }

    @Override
    public void onTileImpact(World world, MovingObjectPosition mop)
    {
    	BlockPos pos = mop.func_178782_a();
    	
        int horizRange = (int) (0.5 * (this.powerUpgrades) + 1);
        int vertRange = (int) (0.5 * (this.powerUpgrades) + 1);

        for (int i = -horizRange; i <= horizRange; i++)
        {
            for (int j = -vertRange; j <= vertRange; j++)
            {
                for (int k = -horizRange; k <= horizRange; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                    if (!world.isAirBlock(newPos))
                    {
                    	IBlockState state = world.getBlockState(newPos);
                        Block block = state.getBlock();
                        if (block == null || block.getBlockHardness(world, newPos) == -1 || SpellHelper.isBlockFluid(block))
                        {
                            continue;
                        }
                        //block.breakBlock(world, posX+i, posY+j, posZ+k, block.blockID, world.getBlockMetadata(posX+i, posY+j, posZ+k));
                        //world.destroyBlock(posX+i, posY+j, posZ+k, true);
                        world.destroyBlock(newPos, false);
                    }
                }
            }
        }
    }
}
