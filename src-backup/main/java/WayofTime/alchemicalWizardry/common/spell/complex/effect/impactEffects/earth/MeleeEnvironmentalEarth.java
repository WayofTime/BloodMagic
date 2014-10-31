package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.MeleeSpellCenteredWorldEffect;

public class MeleeEnvironmentalEarth extends MeleeSpellCenteredWorldEffect
{
	public MeleeEnvironmentalEarth(int power, int potency, int cost) 
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
						ItemStack stack = new ItemStack(world.getBlock(posX+i, posY+j, posZ+k),1,world.getBlockMetadata(posX+i, posY+j, posZ+k));
						
						ItemStack dustStack = SpellHelper.getDustForOre(stack);
						
						if(dustStack!=null)
						{
							dustStack.stackSize *= 3;
							world.spawnEntityInWorld(new EntityItem(world,posX,posY,posZ,dustStack));
							
							world.setBlockToAir(posX+i, posY+j, posZ+k);
						}
					}
				}
			}
		}
	}
}
