package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.MeleeSpellWorldEffect;

public class MeleeDefensiveFire extends MeleeSpellWorldEffect
{
	public MeleeDefensiveFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onWorldEffect(World world, EntityPlayer entityPlayer) 
	{
		ForgeDirection look = SpellHelper.getCompassDirectionForLookVector(entityPlayer.getLookVec());
		
		int width = this.potencyUpgrades + 1;
		int length = 5*this.powerUpgrades + 3;
		
		int xOffset = look.offsetX;
		int zOffset = look.offsetZ;
		
		Vec3 lookVec = SpellHelper.getEntityBlockVector(entityPlayer);
		
		int xStart = (int)(lookVec.xCoord)+1*xOffset;
		int zStart = (int)(lookVec.zCoord)+1*zOffset;
		int yStart = (int)(lookVec.yCoord)-1;
		
		for(int i=-width; i<=width; i++)
		{
			for(int j=0; j<length;j++)
			{
				for(int k=0;k<3;k++)
				{
					if(world.isAirBlock(xStart + i*(zOffset) + j*(xOffset), yStart+k, zStart + i*(xOffset) + j*(zOffset)))
					{
						world.setBlock(xStart + i*(zOffset) + j*(xOffset), yStart+k, zStart + i*(xOffset) + j*(zOffset), Blocks.fire);
					}
				}
			}
		}
	}
}
