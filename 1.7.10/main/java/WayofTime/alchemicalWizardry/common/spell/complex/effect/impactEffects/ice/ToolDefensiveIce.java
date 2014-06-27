package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.SummonToolEffect;

public class ToolDefensiveIce extends SummonToolEffect
{
	public ToolDefensiveIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public int onSummonTool(ItemStack toolStack, World world, Entity entity) 
	{
		int horizRadius = this.powerUpgrades*2+2;
		int vertRadius = this.powerUpgrades * 3 + 2;
		List<Entity> entityList = SpellHelper.getEntitiesInRange(world, entity.posX, entity.posY, entity.posZ, horizRadius, vertRadius);

		for(Entity ent : entityList)
		{
			if(ent instanceof EntityLivingBase && !ent.equals(entity))
			{
				((EntityLivingBase)ent).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id,200,this.potencyUpgrades*2));
			}
		}
		
		Vec3 blockVec = SpellHelper.getEntityBlockVector(entity);
		
		int x = (int)(blockVec.xCoord);
		int y = (int)(blockVec.yCoord);
		int z = (int)(blockVec.zCoord);
		
		for(int posX = x-horizRadius; posX <= x+horizRadius; posX++)
		{
			for(int posY = y-vertRadius; posY <= y+vertRadius; posY++)
			{
				for(int posZ = z-horizRadius; posZ <= z+horizRadius; posZ++)
				{
					SpellHelper.freezeWaterBlock(world, posX, posY, posZ);
					if(world.isSideSolid(posX, posY, posZ, ForgeDirection.UP) && world.isAirBlock(posX, posY+1, posZ))
					{
						world.setBlock(posX, posY+1, posZ, Blocks.snow_layer);
					}
				}
			}
		}
		
		return 0;
	}
}
