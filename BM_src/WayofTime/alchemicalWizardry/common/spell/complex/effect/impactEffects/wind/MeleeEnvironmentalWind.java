package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.MeleeSpellCenteredWorldEffect;

public class MeleeEnvironmentalWind extends MeleeSpellCenteredWorldEffect
{
	public MeleeEnvironmentalWind(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		this.setRange(3*power + 2);
	}

	@Override
	public void onCenteredWorldEffect(EntityPlayer player, World world, int posX, int posY, int posZ) 
	{
		int radius = this.potencyUpgrades;
		double wantedVel = 0.5d;
		
		for(int i=-radius; i<=radius; i++)
		{
			for(int j=-radius; j<=radius; j++)
			{
				for(int k=-radius; k<=radius; k++)
				{
					Block block = Block.blocksList[world.getBlockId(posX+i, posY+j, posZ+k)];
					int meta = world.getBlockMetadata(posX+i, posY+j, posZ+k);
					
					if(SpellHelper.isBlockFluid(block)&&world.rand.nextFloat()<0.9f)
					{
						EntityFallingSand liquid = new EntityFallingSand(world, posX+i, posY+j, posZ+k, block.blockID, meta);
						
						if(liquid!=null)
						{
							liquid.motionX = (world.rand.nextDouble()-world.rand.nextDouble())*wantedVel;
							liquid.motionY = (world.rand.nextDouble()-world.rand.nextDouble())*wantedVel;
							liquid.motionZ = (world.rand.nextDouble()-world.rand.nextDouble())*wantedVel;
							
							world.spawnEntityInWorld(liquid);
						}
					}
				}
			}
		}
	}
}
