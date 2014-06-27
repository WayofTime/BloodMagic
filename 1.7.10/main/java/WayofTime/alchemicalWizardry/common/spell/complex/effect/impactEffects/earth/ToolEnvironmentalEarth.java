package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.items.spell.ItemSpellMultiTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.DigAreaEffect;

public class ToolEnvironmentalEarth extends DigAreaEffect
{
	public ToolEnvironmentalEarth(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public int digSurroundingArea(ItemStack container, World world, EntityPlayer player, MovingObjectPosition blockPos, String usedToolClass, float blockHardness, int harvestLvl, ItemSpellMultiTool itemTool) 
	{
		if(!blockPos.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK))
		{
			return 0;
		}
		
		int x = blockPos.blockX;
		int y = blockPos.blockY;
		int z = blockPos.blockZ;
		ForgeDirection sidehit = ForgeDirection.getOrientation(blockPos.sideHit);
		
		int radius = 2;
		int depth = 5;
		
		depth--;
		
		int posX = radius;
		int negX = radius;
		int posY = radius;
		int negY = radius;
		int posZ = radius;
		int negZ = radius;
		
		switch(sidehit)
		{
		case UP:
			posY = 0;
			negY = depth;
			break;
		case DOWN:
			negY = 0;
			posY = depth;
			break;
		case SOUTH:
			posZ = 0;
			negZ = depth;
			break;
		case NORTH:
			negZ = 0;
			posZ = depth;
			break;
		case WEST:
			negX = 0;
			posX = depth;
			break;
		case EAST:
			posX = 0;
			negX = depth;
			break;
			
		default:
		}
		
		for(int xPos = x-negX; xPos <= x+posX; xPos++)
		{
			for(int yPos = y-negY; yPos <= y+posY; yPos++)
			{
				for(int zPos = z-negZ; zPos <= z+posZ; zPos++)
				{
					this.breakBlock(container, world, player, blockHardness, xPos, yPos, zPos, itemTool); 
				}
			}
		}
		
		return 0;
	}
}
