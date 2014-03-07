package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.MeleeSpellCenteredWorldEffect;

public class MeleeDefaultEarth extends MeleeSpellCenteredWorldEffect
{
	public MeleeDefaultEarth(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		this.setRange(2);
	}

	@Override
	public void onCenteredWorldEffect(World world, int posX, int posY, int posZ) 
	{
		for(int i=-3; i<=3; i++)
		{
			for(int j=-3; j<=3; j++)
			{
				for(int k=-3; k<=3; k++)
				{
					if(!world.isAirBlock(posX + i, posY + j, posZ + k) && world.getTileEntity(posX + i, posY + j, posZ + k)==null)
					{
						Block block = world.getBlock(posX + i, posY + j, posZ + k);
						int meta = world.getBlockMetadata(posX + i, posY + j, posZ + k);
						
						EntityFallingBlock entity = new EntityFallingBlock(world, posX + i + 0.5f, posY + j + 0.5f, posZ + k + 0.5f, block, meta);
						world.spawnEntityInWorld(entity);
					}
				}
			}
		}
		
	}
}
