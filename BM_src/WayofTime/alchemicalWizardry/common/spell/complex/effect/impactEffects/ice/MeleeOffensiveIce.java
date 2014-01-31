package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ExtrapolatedMeleeEntityEffect;

public class MeleeOffensiveIce extends ExtrapolatedMeleeEntityEffect 
{
	public MeleeOffensiveIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		this.setMaxNumberHit(2);
		this.setRadius(1);
		this.setRange(2);
	}

	@Override
	protected boolean entityEffect(World world, Entity entity) 
	{
		Vec3 blockVector = SpellHelper.getEntityBlockVector(entity);
		
		int posX = (int)(blockVector.xCoord);
		int posY = (int)(blockVector.yCoord);
		int posZ = (int)(blockVector.zCoord);
		
		double yVel = 1*(0.4*this.powerUpgrades+0.75);
		
		entity.motionY = yVel;
		
		for(int i=0;i<2;i++)
		{
			if(world.isAirBlock(posX,posY+i,posZ))
			{
				world.setBlock(posX, posY+i, posZ, Block.ice.blockID);
			}
		}
		
		entity.fallDistance = 0.0f;
		return true;
	}

}
