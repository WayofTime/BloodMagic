package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.OnBreakBlockEffect;

public class ToolEnvironmentalFire extends OnBreakBlockEffect
{
	public ToolEnvironmentalFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public int onBlockBroken(ItemStack container, World world, EntityPlayer player, Block block, int meta, int x, int y, int z, ForgeDirection sideBroken) 
	{
		int amount = 0;
		int cost = (int)(250 * (1 - 0.1f*powerUpgrades) * Math.pow(0.85, costUpgrades));
		int radius = this.powerUpgrades;
		float chance = 0.35f + 0.15f*this.potencyUpgrades;
		
		for(int i=-radius; i<=radius; i++)
		{
			for(int j=-radius; j<=radius; j++)
			{
				for(int k=-radius; k<=radius; k++)
				{
					Block blockAffected = world.getBlock(x + i -sideBroken.offsetX, y + j, z + k - sideBroken.offsetZ);
					
					if((new Random().nextFloat() <= chance) && (blockAffected == Blocks.gravel || blockAffected == Blocks.stone || blockAffected == Blocks.cobblestone))
					{
						world.setBlock(x + i -sideBroken.offsetX, y + j, z + k - sideBroken.offsetZ, Blocks.lava);
						amount += cost;
					}
				}
			}
		}
		
		return amount;
	}
}
