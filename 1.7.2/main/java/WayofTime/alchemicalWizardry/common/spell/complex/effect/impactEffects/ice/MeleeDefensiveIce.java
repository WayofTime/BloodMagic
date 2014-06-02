package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.MeleeSpellWorldEffect;

public class MeleeDefensiveIce extends MeleeSpellWorldEffect 
{
	public MeleeDefensiveIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onWorldEffect(World world, EntityPlayer entityPlayer) 
	{
		ForgeDirection look = SpellHelper.getCompassDirectionForLookVector(entityPlayer.getLookVec());
		
		int width = this.powerUpgrades;
		int height = this.powerUpgrades + 2;
		
		int xOffset = look.offsetX;
		int zOffset = look.offsetZ;
		
		int range = this.potencyUpgrades + 1;
		
		Vec3 lookVec = SpellHelper.getEntityBlockVector(entityPlayer);
		
		int xStart = (int)(lookVec.xCoord) + range * xOffset;
		int zStart = (int)(lookVec.zCoord) + range * zOffset;
		int yStart = (int)(lookVec.yCoord);
		
		for(int i=-width; i<=width; i++)
		{
			for(int j=0; j<height;j++)
			{
				if(world.isAirBlock(xStart + i*(zOffset), yStart + j, zStart + i*(xOffset)))
				{
					world.setBlock(xStart + i*(zOffset), yStart + j, zStart + i*(xOffset), Blocks.ice);
				}
			}
		}
	}
}
