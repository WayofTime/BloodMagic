package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class SpellHelper 
{
	public static Random rand = new Random();
	
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
}
