package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.spell.EntitySpellProjectile;
import WayofTime.alchemicalWizardry.api.spell.ProjectileUpdateEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class ProjectileEnvironmentalEarth extends ProjectileUpdateEffect
{
    public ProjectileEnvironmentalEarth(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public void onUpdateEffect(Entity projectile)
    {
        BlockPos pos = projectile.getPosition();

        int horizRange = this.powerUpgrades + 1;
        int vertRange = (int) (0.5 * (this.powerUpgrades + 1));
        int maxBlocks = (int) (2 * Math.pow(3.47, this.potencyUpgrades));

        World worldObj = projectile.worldObj;

        if (projectile instanceof EntitySpellProjectile)
        {
            int blocksBroken = ((EntitySpellProjectile) projectile).getBlocksBroken();

            if (blocksBroken >= maxBlocks)
            {
                return;
            }

            for (int i = -horizRange; i <= horizRange; i++)
            {
                for (int j = -vertRange; j <= vertRange; j++)
                {
                    for (int k = -horizRange; k <= horizRange; k++)
                    {
                    	BlockPos newPos = pos.add(i, j, k);
                        if (!worldObj.isAirBlock(newPos) && blocksBroken < maxBlocks)
                        {
                        	IBlockState state = worldObj.getBlockState(newPos);
                            Block block = state.getBlock();
                            if (block == null || block.getBlockHardness(worldObj, newPos) == -1 || SpellHelper.isBlockFluid(block))
                            {
                                continue;
                            }

                            if (((EntitySpellProjectile) projectile).getIsSilkTouch() && block.canSilkHarvest(worldObj, newPos, state, ((EntitySpellProjectile) projectile).shootingEntity))
                            {
                                ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(state));
                                EntityItem itemEntity = new EntityItem(worldObj, newPos.getX() + 0.5, newPos.getY() + 0.5, newPos.getZ() + 0.5, stack);
                                worldObj.spawnEntityInWorld(itemEntity);
                                worldObj.setBlockToAir(newPos);
                            } else
                            {
                                worldObj.destroyBlock(newPos, true);
                            }
                            blocksBroken++;
                        }
                    }
                }
            }

            ((EntitySpellProjectile) projectile).setBlocksBroken(blocksBroken);
        }
    }
}
