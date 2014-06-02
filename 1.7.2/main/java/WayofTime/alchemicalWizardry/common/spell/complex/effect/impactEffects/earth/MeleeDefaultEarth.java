package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.MeleeSpellCenteredWorldEffect;

public class MeleeDefaultEarth extends MeleeSpellCenteredWorldEffect
{
	public MeleeDefaultEarth(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		this.setRange(3*power + 2);
	}

	@Override
	public void onCenteredWorldEffect(EntityPlayer player, World world, int posX, int posY, int posZ) 
	{
		int radius = this.potencyUpgrades;
		
		for(int i=-radius; i<=radius; i++)
		{
			for(int j=-radius; j<=radius; j++)
			{
				for(int k=-radius; k<=radius; k++)
				{
					if(!world.isAirBlock(posX + i, posY + j, posZ + k) && world.getTileEntity(posX + i, posY + j, posZ + k)==null)
					{
						Block block = world.getBlock(posX + i, posY + j, posZ + k);
						
						if(block.getBlockHardness(world, posX + i, posY + j, posZ + k)==-1)
						{
							continue;
						}
						int meta = world.getBlockMetadata(posX + i, posY + j, posZ + k);
						
						EntityFallingBlock entity = new EntityFallingBlock(world, posX + i + 0.5f, posY + j + 0.5f, posZ + k + 0.5f, block, meta);
						world.spawnEntityInWorld(entity);
					}
				}
			}
		}
	}
}
