package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfEnvironmentalIce extends SelfSpellEffect
{
	public SelfEnvironmentalIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		ForgeDirection look = SpellHelper.getCompassDirectionForLookVector(player.getLookVec());
		
		int width = this.potencyUpgrades + 1;
		int length = 5*this.powerUpgrades + 3;
		
		int xOffset = look.offsetX;
		int zOffset = look.offsetZ;
		
		Vec3 lookVec = SpellHelper.getEntityBlockVector(player);
		
		int xStart = (int)(lookVec.xCoord);
		int zStart = (int)(lookVec.zCoord);
		int yStart = (int)(lookVec.yCoord)-1;
		
		for(int i=-width; i<=width; i++)
		{
			for(int j=0; j<length;j++)
			{
				if(world.isAirBlock(xStart + i*(zOffset) + j*(xOffset), yStart, zStart + i*(xOffset) + j*(zOffset)))
				{
					world.setBlock(xStart + i*(zOffset) + j*(xOffset), yStart, zStart + i*(xOffset) + j*(zOffset), Blocks.ice);
				}
			}
		}
	}
}
