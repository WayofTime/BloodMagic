package WayofTime.alchemicalWizardry.common.tileEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.demonVillage.BuildingSchematic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TESchematicSaver extends TileEntity
{
	public Block targetBlock = ModBlocks.largeBloodStoneBrick;
	
	public void rightClickBlock(EntityPlayer player, int side)
	{
		BuildingSchematic schematic = new BuildingSchematic();

		int negX = this.getNegXLimit();
		int negY = this.getNegYLimit();
		int negZ = this.getNegZLimit();
		int posX = this.getPosXLimit();
		int posY = this.getPosYLimit();
		int posZ = this.getPosZLimit();
		
		for(int i=-negX+1; i<=posX-1; i++)
		{
			for(int j=-negY+1; j<=posY-1; j++)
			{
				for(int k=-negZ+1; k<=posZ-1; k++)
				{
					int meta = worldObj.getBlockMetadata(xCoord + i, yCoord + j, zCoord + k);
					Block block = worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k);
					
					if(!block.isAir(worldObj, xCoord + i, yCoord + j, zCoord + k))
					{
						schematic.addBlockWithMeta(block, meta, i, j, k);
					}
					
				}
			}
			
			System.out.println("" + i);
		}
		
		System.out.println("I got here!");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(schematic);
		System.out.println("Here, too!");
		Writer writer;
		try 
		{
			writer = new FileWriter("config/BloodMagic/schematics/" + new Random().nextInt() + ".json");
			writer.write(json);
			writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
	
	public int getPosYLimit()
	{
		int i=1;
		while(i<100)
		{
			if(targetBlock == (worldObj.getBlock(xCoord, yCoord + i, zCoord)))
			{
				return i;
			}
			
			i++;
		}
		return 1;
	}
	
	public int getNegYLimit()
	{
		int i=1;
		while(i<100)
		{
			if(targetBlock == (worldObj.getBlock(xCoord, yCoord - i, zCoord)))
			{
				return i;
			}
			
			i++;
		}
		return 1;
	}
	
	public int getPosXLimit()
	{
		int i=1;
		while(i<100)
		{
			if(targetBlock == (worldObj.getBlock(xCoord + i, yCoord, zCoord)))
			{
				return i;
			}
			
			i++;
		}
		return 1;
	}
	
	public int getNegXLimit()
	{
		int i=1;
		while(i<100)
		{
			if(targetBlock == (worldObj.getBlock(xCoord - i, yCoord, zCoord)))
			{
				return i;
			}
			
			i++;
		}
		return 1;
	}
	
	public int getPosZLimit()
	{
		int i=1;
		while(i<100)
		{
			if(targetBlock == (worldObj.getBlock(xCoord, yCoord, zCoord + i)))
			{
				return i;
			}
			
			i++;
		}
		return 1;
	}
	
	public int getNegZLimit()
	{
		int i=1;
		while(i<100)
		{
			if(targetBlock == (worldObj.getBlock(xCoord, yCoord, zCoord - i)))
			{
				return i;
			}
			
			i++;
		}
		return 1;
	}
}