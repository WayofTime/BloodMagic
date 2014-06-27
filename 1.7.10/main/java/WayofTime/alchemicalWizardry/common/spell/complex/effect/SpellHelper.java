package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;

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
	
	public static List<EntityItem> getItemsInRange(World world, double posX, double posY, double posZ, double horizontalRadius, double verticalRadius)
	{
		return world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(posX-0.5f, posY-0.5f, posZ-0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f).expand(horizontalRadius, verticalRadius, horizontalRadius));
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
		return SoulNetworkHandler.getUsername(player);
	}
	
	public static EntityPlayer getPlayerForUsername(String str)
	{
		if(MinecraftServer.getServer() == null)
		{
			return null;
		}
		return MinecraftServer.getServer().getConfigurationManager().func_152612_a(str);
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
	
	public static boolean isFakePlayer(World world, EntityPlayer player)
	{
		if(world.isRemote)
		{
			return false;
		}
		
		if(player instanceof FakePlayer || SpellHelper.getUsername(player).contains("[CoFH]"))
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
		Block block = world.getBlock(posX, posY, posZ);
		
		if(block==Blocks.stone)
		{
			world.setBlock(posX, posY, posZ, Blocks.cobblestone);
			return;
		}
		else if(block==Blocks.cobblestone)
		{
			world.setBlock(posX, posY, posZ, Blocks.gravel);
			return;
		}
		else if(block==Blocks.gravel)
		{
			world.setBlock(posX, posY, posZ, Blocks.sand);
			return;
		}
	}
	
	public static boolean isBlockFluid(Block block)
	{
		return block instanceof BlockLiquid;
	}
	
	public static void evaporateWaterBlock(World world, int posX, int posY, int posZ)
	{
		Block block = world.getBlock(posX, posY, posZ);
		
		if(block == Blocks.water || block == Blocks.flowing_water)
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
	
	public static List<ItemStack> getItemsFromBlock(World world, Block block, int x, int y, int z, int meta, boolean silkTouch, int fortune)
	{
		boolean canSilk = block.canSilkHarvest(world, null, x, y, z, meta);

		if(canSilk && silkTouch)
		{
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			ItemStack item = new ItemStack(block, 1, meta);
			
			items.add(item);
			
			return items;
		}else
		{
			return block.getDrops(world, x, y, z, meta, fortune);
		}
	}
	
	public static void spawnItemListInWorld(List<ItemStack> items, World world, float x, float y, float z)
	{
		for(ItemStack stack : items)
		{
			EntityItem itemEntity = new EntityItem(world, x, y, z, stack);
			itemEntity.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(itemEntity);
		}
	}
	
	public static MovingObjectPosition raytraceFromEntity (World world, Entity player, boolean par3, double range)
    {
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f;
        if (!world.isRemote && player instanceof EntityPlayer)
            d1 += 1.62D;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = range;
        if (player instanceof EntityPlayerMP)
        {
            d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        return world.func_147447_a(vec3, vec31, par3, !par3, par3);
    }

	public static String getNumeralForInt(int num)
	{
		switch(num)
		{
		case 1: return "I";
		case 2: return "II";
		case 3: return "III";
		case 4: return "IV";
		case 5: return "V";
		case 6: return "VI";
		case 7: return "VII";
		case 8: return "VIII";
		case 9: return "IX";
		case 10: return "X";
		default: return "";
		}
	}
}
