package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.items.spell.ItemSpellMultiTool;

public class DigAreaTunnel extends DigAreaEffect
{
	Random rand = new Random();
	
	public DigAreaTunnel(int power, int potency, int cost) 
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
		
		List<Vec3> vectorLine = new LinkedList();
		
		double initialX = blockPos.blockX;
		double initialY = blockPos.blockY;
		double initialZ = blockPos.blockZ;
		ForgeDirection sidehit = ForgeDirection.getOrientation(blockPos.sideHit);
		ForgeDirection opposite = sidehit.getOpposite();
		
		System.out.println(opposite.toString());
		
		double initialLength = this.getRandomVectorLength();
		
		Vec3 initialVector = Vec3.createVectorHelper(opposite.offsetX*initialLength, opposite.offsetY*initialLength, opposite.offsetZ*initialLength);
		
		Vec3 lastVec = Vec3.createVectorHelper(initialVector.xCoord, initialVector.yCoord, initialVector.zCoord);
		vectorLine.add(initialVector);
		
		double currentLength = lastVec.lengthVector();
		double totalLength = this.totalLength();
		
		while(currentLength < totalLength-0.01)
		{
			Vec3 tempVec = lastVec.addVector(0, 0, 0);
			
			tempVec = tempVec.normalize();
			
			double varr = this.varyRate();
			
			tempVec = tempVec.addVector(varr*(rand.nextFloat() - rand.nextFloat()), varr*(rand.nextFloat() - rand.nextFloat()), varr*(rand.nextFloat() - rand.nextFloat()));
			
			tempVec = tempVec.normalize();
			
			double length = Math.min(this.getRandomVectorLength(), totalLength-currentLength);
			
			tempVec.xCoord = tempVec.xCoord*length;
			tempVec.yCoord = tempVec.yCoord*length;
			tempVec.zCoord = tempVec.zCoord*length;
			
			vectorLine.add(tempVec);
			
			lastVec = tempVec;
			
			currentLength += tempVec.lengthVector();
		}
		
		for(Vec3 testVec : vectorLine)
		{
			this.travelVector(testVec, world, initialX, initialY, initialZ);
			
			initialX += testVec.xCoord;
			initialY += testVec.yCoord;
			initialZ += testVec.zCoord;
		}
		
		this.travelVector(lastVec, world, initialX, initialY, initialZ);
		
		return 0;
	}
	
	public double totalLength()
	{
		return 100;
	}
	
	public double getStepSize()
	{
		return 1;
	}
	
	public double varyRate()
	{
		return 0.5;
	}
	
	public double getRandomVectorLength()
	{
		return 10;
	}
	
	public double getRandomStepLength()
	{
		return 0.5;
	}
	
	public int getRandomRadius()
	{
		return 3;
	}
	
	public void destroySphereOfMundane(World world, double x, double y, double z, int radius)
	{
		for(int i=-radius; i<=radius; i++)
		{
			for(int j=-radius; j<=radius; j++)
			{
				for(int k=-radius; k<=radius; k++)
				{
					if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
					{
						continue;
					}
					
					int newX = (int)(i + x + 0.5);
					int newY = (int)(j + y + 0.5);
					int newZ = (int)(k + z + 0.5);
					
					this.destroyMunadeAt(world, newX, newY, newZ);
				}
			}
		}
	}
	
	public void destroyMunadeAt(World world, int x, int y, int z)
	{
		world.setBlockToAir(x, y, z);
	}
	
	public void travelVector(Vec3 vector, World world, double x, double y, double z)
	{
		double vecLength = vector.lengthVector();
		System.out.println(vecLength);
		Vec3 normVec = Vec3.createVectorHelper(vector.xCoord, vector.yCoord, vector.zCoord);
		normVec = normVec.normalize();
		
		Vec3 prevVec = Vec3.createVectorHelper(0, 0, 0);
		double distanceTravelled = 0;
		
		while(distanceTravelled < vecLength)
		{
			System.out.println(distanceTravelled);
			double stepLength = this.getRandomStepLength();
			
			prevVec = prevVec.addVector(stepLength*normVec.xCoord, stepLength*normVec.yCoord, normVec.zCoord);
			
			this.destroySphereOfMundane(world, prevVec.xCoord + x, prevVec.yCoord + y, prevVec.zCoord + z, this.getRandomRadius());
			
			distanceTravelled = prevVec.lengthVector();
		}
	}
}
