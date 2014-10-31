package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfSpellEffect;

public class SelfOffensiveWind extends SelfSpellEffect
{
	public SelfOffensiveWind(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onSelfUse(World world, EntityPlayer player) 
	{
		Vec3 vec = player.getLookVec();
        double wantedVelocity = 1.5 + this.powerUpgrades*0.4;

        SpellHelper.setPlayerSpeedFromServer(player, vec.xCoord * wantedVelocity, vec.yCoord * wantedVelocity, vec.zCoord * wantedVelocity);
        
        player.fallDistance = 0;
	}
}
