package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayer;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class SpellHelper 
{
	public static Random rand = new Random();
	public static final double root2 = Math.sqrt(2);
	
	public static void smeltBlockInWorld(World world, int posX, int posY, int posZ)
	{
		FurnaceRecipes recipes = FurnaceRecipes.smelting();
		
		Block block = Block.blocksList[world.getBlockId(posX, posY, posZ)];
		if(block==null)
		{
			return;
		}
		
		int meta = world.getBlockMetadata(posX, posY, posZ);
		
		ItemStack smeltedStack = recipes.getSmeltingResult(new ItemStack(block,1,meta));
		if(smeltedStack!=null && smeltedStack.getItem() instanceof ItemBlock)
		{
			world.setBlock(posX, posY, posZ, smeltedStack.itemID, smeltedStack.getItemDamage(), 3);
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
		int id = world.getBlockId(posX, posY, posZ);
		
		if(id == Block.waterStill.blockID || id == Block.waterMoving.blockID)
		{
			world.setBlock(posX, posY, posZ, Block.ice.blockID);
		}
	}
	
	public static String getUsername(EntityPlayer player)
	{
		return player.getDisplayName();
	}
	
	public static boolean isFakePlayer(World world, EntityPlayer player)
	{
		if(world.isRemote)
		{
			return false;
		}
		
		if(player instanceof FakePlayer || player.username.contains("[CoFH]"))
		{
			return true;
		}
		
		String str = player.getClass().getSimpleName();
		if(str.contains("GC"))
		{
			return false;
		}
		
		if(player.getClass().equals(EntityPlayerMP.class))
		{
			return false;
		}
		
		return false;
	}
	
	public static void smashBlock(World world, int posX, int posY, int posZ)
	{
		Block block = Block.blocksList[world.getBlockId(posX, posY, posZ)];
		
		if(block==Block.stone)
		{
			world.setBlock(posX, posY, posZ, Block.cobblestone.blockID);
			return;
		}
		else if(block==Block.cobblestone)
		{
			world.setBlock(posX, posY, posZ, Block.gravel.blockID);
			return;
		}
		else if(block==Block.gravel)
		{
			world.setBlock(posX, posY, posZ, Block.sand.blockID);
			return;
		}
	}
	
	public static boolean isBlockFluid(Block block)
	{
		return block instanceof BlockFluid || block instanceof IFluidBlock;
	}
	
	public static void evaporateWaterBlock(World world, int posX, int posY, int posZ)
	{
		int id = world.getBlockId(posX, posY, posZ);
		Block block = Block.blocksList[id];
		
		if(block == Block.waterMoving || block == Block.waterStill)
		{
			world.setBlockToAir(posX, posY, posZ);
		}
	}
	
	public static ItemStack getDustForOre(ItemStack item)
	{
		String oreName = OreDictionary.getOreName(OreDictionary.getOreID(item));
		
		if(oreName.contains("ore"))
		{
			String lowercaseOre = oreName.toLowerCase();
			boolean isAllowed = false;

			for(String str : AlchemicalWizardry.allowedCrushedOresArray)
			{ 
				String testStr = str.toLowerCase();

				if(lowercaseOre.contains(testStr))
				{
					isAllowed = true;
					break;
				}
			}
			
			if(!isAllowed)
			{
				return null;
			}
			
			String dustName = oreName.replace("ore", "dust");
			
			ArrayList<ItemStack> items = OreDictionary.getOres(dustName);
			
			if(items!=null && items.size()>=1)
			{
				return(items.get(0).copy());
			}
		}
		
		return null;
	}

	public static void setPlayerSpeedFromServer(EntityPlayer player, double xVel, double yVel, double zVel) 
	{
		PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(xVel, yVel, zVel), (Player)player);
		
	}
}
