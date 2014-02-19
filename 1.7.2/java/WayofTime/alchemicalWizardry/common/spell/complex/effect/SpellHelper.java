package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import java.util.List;
import java.util.Random;

import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.ForgeDirection;

public class SpellHelper 
{
	public static Random rand = new Random();
	public static final double root2 = Math.sqrt(2);
	
	public static void smeltBlockInWorld(World world, int posX, int posY, int posZ)
	{
		FurnaceRecipes recipes = FurnaceRecipes.smelting();
		
		Block block = world.getBlock(posX, posY, posZ);
		if(block==null)
		{
			return;
		}
		
		int meta = world.getBlockMetadata(posX, posY, posZ);
		
		ItemStack smeltedStack = recipes.getSmeltingResult(new ItemStack(block,1,meta));
		if(smeltedStack!=null && smeltedStack.getItem() instanceof ItemBlock)
		{
			world.setBlock(posX, posY, posZ, ((ItemBlock)(smeltedStack.getItem())).field_150939_a, smeltedStack.getItemDamage(), 3);
		}
	}
	
	public static List<Entity> getEntitiesInRange(World world, double posX, double posY, double posZ, double horizontalRadius, double verticalRadius)
	{
		return world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(posX-0.5f, posY-0.5f, posZ-0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(horizontalRadius, verticalRadius, horizontalRadius));
	}
	
	public static List<EntityPlayer> getPlayersInRange(World world, double posX, double posY, double posZ, double horizontalRadius, double verticalRadius)
	{
		return world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(posX-0.5f, posY-0.5f, posZ-0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(horizontalRadius, verticalRadius, horizontalRadius));
	}
	
	public static double gaussian(double d) 
	{
		return d * ((rand.nextFloat() - 0.5D));
	}
	
	public static Vec3 getEntityBlockVector(Entity entity)
	{
		int posX = (int) Math.round(entity.posX - 0.5f);
        int posY = (int) entity.posY;
        int posZ = (int) Math.round(entity.posZ - 0.5f);
        
		return entity.getLookVec().createVectorHelper(posX, posY, posZ);
	}
	
	public static ForgeDirection getDirectionForLookVector(Vec3 lookVec)
	{
		double distance = lookVec.lengthVector();
		
		if(lookVec.yCoord>distance*0.9)
		{
			return ForgeDirection.UP;
		}
		if(lookVec.yCoord<distance*-0.9)
		{
			return ForgeDirection.DOWN;
		}
		
		return getCompassDirectionForLookVector(lookVec);
	}
	
	public static ForgeDirection getCompassDirectionForLookVector(Vec3 lookVec)
	{
		double radius = Math.sqrt(Math.pow(lookVec.xCoord,2)+Math.pow(lookVec.zCoord,2));
		
		if(lookVec.zCoord>radius*1/root2)
		{
			return ForgeDirection.SOUTH;
		}
		if(lookVec.zCoord<-radius*1/root2)
		{
			return ForgeDirection.NORTH;
		}
		if(lookVec.xCoord>radius*1/root2)
		{
			return ForgeDirection.EAST;
		}
		if(lookVec.xCoord<-radius*1/root2)
		{
			return ForgeDirection.WEST;
		}
		
		return ForgeDirection.EAST;
	}
	
	public static void freezeWaterBlock(World world, int posX, int posY, int posZ)
	{
		Block block = world.getBlock(posX, posY, posZ);
		
		if(block == Blocks.water || block == Blocks.flowing_water)
		{
			world.setBlock(posX, posY, posZ, Blocks.ice);
		}
	}
	
	public static String getUsername(EntityPlayer player)
	{
		return player.getDisplayName();
	}
	
	public static void sendParticleToPlayer(EntityPlayer player, String str, double xCoord, double yCoord, double zCoord, double xVel, double yVel, double zVel)
	{
		if(player instanceof EntityPlayerMP)
		{
			NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getParticlePacket(str, xCoord, yCoord, zCoord, xVel, yVel, zVel),(EntityPlayerMP) player);
		}
	}
	
	public static void sendIndexedParticleToPlayer(EntityPlayer player, int index, double xCoord, double yCoord, double zCoord)
	{
		switch(index)
		{
		case 1:
			SpellHelper.sendParticleToPlayer(player, "mobSpell", xCoord + 0.5D + rand.nextGaussian() / 8, yCoord + 1.1D, zCoord + 0.5D + rand.nextGaussian() / 8, 0.5117D, 0.0117D, 0.0117D);
			break;
		case 2:
			SpellHelper.sendParticleToPlayer(player, "reddust", xCoord + 0.5D + rand.nextGaussian() / 8, yCoord + 1.1D, zCoord + 0.5D + rand.nextGaussian() / 8, 0.82D, 0.941D, 0.91D);
			break;
		case 3:
			SpellHelper.sendParticleToPlayer(player, "mobSpell", xCoord + 0.5D + rand.nextGaussian() / 8, yCoord + 1.1D, zCoord + 0.5D + rand.nextGaussian() / 8, 1.0D, 0.371D, 0.371D);
			break;
		case 4:
			float f = (float) 1.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;

            for (int l = 0; l < 8; ++l)
            {
                SpellHelper.sendParticleToPlayer(player,"reddust", xCoord + Math.random() - Math.random(), yCoord + Math.random() - Math.random(), zCoord + Math.random() - Math.random(), f1, f2, f3);
            }
            break;
		}
	}
	
	public static void sendParticleToAllAround(World world, double xPos, double yPos, double zPos, int radius, int dimension, String str, double xCoord, double yCoord, double zCoord, double xVel, double yVel, double zVel)
	{
		List<EntityPlayer> entities = SpellHelper.getPlayersInRange(world, xPos, yPos, zPos, radius, radius);
		
		if(entities==null)
		{
			return;
		}
		
		for(EntityPlayer player : entities)
		{
			SpellHelper.sendParticleToPlayer(player, str, xCoord, yCoord, zCoord, xVel, yVel, zVel);
		}
	}
	
	public static void sendIndexedParticleToAllAround(World world, double xPos, double yPos, double zPos, int radius, int dimension, int index, double xCoord, double yCoord, double zCoord)
	{
		List<EntityPlayer> entities = SpellHelper.getPlayersInRange(world, xPos, yPos, zPos, radius, radius);
		
		if(entities==null)
		{
			return;
		}
		
		for(EntityPlayer player : entities)
		{
			SpellHelper.sendIndexedParticleToPlayer(player, index, xCoord, yCoord, zCoord);
		}
	}
	
	public static void setPlayerSpeedFromServer(EntityPlayer player, double motionX, double motionY, double motionZ)
	{
		if(player instanceof EntityPlayerMP)
		{
			NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getVelSettingPacket(motionX, motionY, motionZ), (EntityPlayerMP) player);
		}
	}
}
