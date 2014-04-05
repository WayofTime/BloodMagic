package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.EntitySpellProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileUpdateEffect;

public class ProjectileEnvironmentalEarth extends ProjectileUpdateEffect 
{
	public ProjectileEnvironmentalEarth(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onUpdateEffect(Entity projectile) 
	{
		Vec3 posVec = SpellHelper.getEntityBlockVector(projectile);
		
		int horizRange = this.powerUpgrades+1;
		int vertRange = (int)(0.5*(this.powerUpgrades+1));
		int maxBlocks = (int)(2*Math.pow(3.47, this.potencyUpgrades));
		
		int posX = (int)(posVec.xCoord);
		int posY = (int)(posVec.yCoord);
		int posZ = (int)(posVec.zCoord);
		
		World worldObj = projectile.worldObj;
		
		if(projectile instanceof EntitySpellProjectile)
		{
			int blocksBroken = ((EntitySpellProjectile) projectile).getBlocksBroken();
			
			if(blocksBroken>=maxBlocks)
			{
				return;
			}
			
			for(int i=-horizRange; i<=horizRange; i++)
			{
				for(int j=-vertRange; j<=vertRange; j++)
				{
					for(int k=-horizRange; k<=horizRange; k++)
					{
						if(!worldObj.isAirBlock(posX+i, posY+j, posZ+k)&&blocksBroken<maxBlocks)
						{
							Block block = worldObj.getBlock(posX+i, posY+j, posZ+k);
							int meta = worldObj.getBlockMetadata(posX+i, posY+j, posZ+k);
							if(block == null || block.getBlockHardness(worldObj, posX+i, posY+j, posZ+k)==-1 || SpellHelper.isBlockFluid(block))
							{
								continue;
							}
							
							if(((EntitySpellProjectile)projectile).getIsSilkTouch()&&block.canSilkHarvest(worldObj, ((EntitySpellProjectile)projectile).shootingEntity, posX+i, posY+j, posZ+k, meta))
							{
								ItemStack stack = new ItemStack(block,1,meta);
								EntityItem itemEntity = new EntityItem(worldObj,posX+i+0.5, posY+j+0.5, posZ+k+0.5,stack);
								worldObj.spawnEntityInWorld(itemEntity);
								worldObj.setBlockToAir(posX+i, posY+j, posZ+k);
							}else
							{
								worldObj.func_147480_a(posX+i, posY+j, posZ+k, true);
							}
							//block.breakBlock(worldObj, posX+i, posY+j, posZ+k, block.blockID, worldObj.getBlockMetadata(posX+i, posY+j, posZ+k));
							//worldObj.destroyBlock(posX+i, posY+j, posZ+k, true);
							

							blocksBroken++;
						}
					}
				}
			}
			
			((EntitySpellProjectile) projectile).setBlocksBroken(blocksBroken);
		}
	}
}
