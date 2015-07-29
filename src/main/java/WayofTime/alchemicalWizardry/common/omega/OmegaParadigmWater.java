package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMimicBlock;

public class OmegaParadigmWater extends OmegaParadigm
{
	public OmegaParadigmWater(OmegaArmour helmet, OmegaArmour chestPiece, OmegaArmour leggings, OmegaArmour boots) 
	{
		super(ReagentRegistry.aquasalusReagent,  helmet, chestPiece, leggings, boots, new ReagentRegenConfiguration(50, 1, 10));
	}
	
	@Override
	public float getCostPerTickOfUse(EntityPlayer player)
	{
		if(player.isInWater())
		{
			return 0.5f;
		}else
		{
			return 1;
		}
	}
	
	@Override
	public void onUpdate(World world, EntityPlayer player, ItemStack stack)
	{
		player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 3, 0, true, false));
		player.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionAmphibian.id, 3, 0, true, false));
		
		if(world.getWorldTime() % 100 == 0 && !world.isRemote)
		{
			if(player.isInWater() && player.getHealth() < player.getMaxHealth())
			{
				player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0, true, false));
			}
		}
	}
	
	@Override
	public boolean getBlockEffectWhileInside(Entity entity, BlockPos pos)
	{
		if(entity instanceof EntityLivingBase)
		{
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionDrowning.id, 100, 1, true, false));
		}
		return true;
	}
	
	@Override
	public void onOmegaKeyPressed(EntityPlayer player, ItemStack stack)
	{
		World world = player.worldObj;
		
		int x = (int) Math.round(player.posX);
		int y = (int) Math.round(player.posY);
		int z = (int) Math.round(player.posZ);

		int range = 3;
		
		for(int i=-range; i<=range; i++)
		{
			for(int j=-range; j<=range; j++)
			{
				for(int k=-range; k<=range; k++)
				{
					TEMimicBlock.createMimicBlockAtLocation(world, new BlockPos(x+i, y+j, z+k), 300, Blocks.water.getDefaultState(), ReagentRegistry.aquasalusReagent);
				}
			}
		}
	}
}
