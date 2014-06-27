package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfDefaultIce extends SelfSpellEffect 
{
	public SelfDefaultIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		Vec3 blockVector = SpellHelper.getEntityBlockVector(player);
		
		int posX = (int)(blockVector.xCoord);
		int posY = (int)(blockVector.yCoord);
		int posZ = (int)(blockVector.zCoord);
		
		double yVel = 1*(0.4*this.powerUpgrades+0.75);
		
		//PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(player.motionX, yVel, player.motionZ),(Player)player);
		SpellHelper.setPlayerSpeedFromServer(player, player.motionX, yVel, player.motionZ);
		
		for(int i=0;i<2;i++)
		{
			if(world.isAirBlock(posX,posY+i,posZ))
			{
				world.setBlock(posX, posY+i, posZ, Blocks.ice);
			}
		}
		
		player.fallDistance = 0.0f;
	}
}
