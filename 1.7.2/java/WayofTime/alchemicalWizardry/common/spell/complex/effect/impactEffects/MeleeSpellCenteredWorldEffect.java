package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class MeleeSpellCenteredWorldEffect extends MeleeSpellWorldEffect 
{
	protected float range;
	
	public MeleeSpellCenteredWorldEffect(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		range = 0;
	}

	@Override
	public void onWorldEffect(World world, EntityPlayer entityPlayer) 
	{
		Vec3 lookVec = entityPlayer.getLook(range);
		int x = (int)(entityPlayer.posX + lookVec.xCoord);
		int y = (int)(entityPlayer.posY + entityPlayer.getEyeHeight() + lookVec.yCoord);
		int z = (int)(entityPlayer.posZ + lookVec.zCoord);
		
		this.onCenteredWorldEffect(world, x, y, z);
	}

	public void setRange(float range)
	{
		this.range = range;
	}
	
	public abstract void onCenteredWorldEffect(World world, int posX, int posY, int posZ);
}
