package WayofTime.alchemicalWizardry.api.spell;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class APISpellHelper 
{
	/**
	 * Thanks Kihira! <3
	 * @param player
	 * @return persistent data tag
	 */
	private static NBTTagCompound getPersistentDataTag(EntityPlayer player) 
	{ 
		NBTTagCompound forgeData = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG); 
        NBTTagCompound beaconData = forgeData.getCompoundTag("BloodMagic"); 

        //Creates/sets the tags if they don't exist 
        if (!forgeData.hasKey("BloodMagic")) forgeData.setTag("BloodMagic", beaconData); 
        if (!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG)) player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, forgeData); 

        return beaconData; 
	} 
	
	public static float getCurrentIncense(EntityPlayer player)
	{
		NBTTagCompound data = player.getEntityData();
		if(data.hasKey("BM:CurrentIncense"))
		{
			return data.getFloat("BM:CurrentIncense");
		}
		
		return 0;
	}
	
	public static void setCurrentIncense(EntityPlayer player, float amount)
	{
		NBTTagCompound data = player.getEntityData();
		data.setFloat("BM:CurrentIncense", amount);
	}
	
	public static int getPlayerLPTag(EntityPlayer player)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		if(data.hasKey("BM:StoredLP"))
		{
			return data.getInteger("BM:StoredLP");
		}
		
		return 0;
	}
	
	public static void setPlayerLPTag(EntityPlayer player, int amount)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		data.setInteger("BM:StoredLP", amount);
	}
	
	public static int getPlayerMaxLPTag(EntityPlayer player)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		if(data.hasKey("BM:MaxStoredLP"))
		{
			return data.getInteger("BM:MaxStoredLP");
		}
		
		return 0;
	}
	
	public static void setPlayerMaxLPTag(EntityPlayer player, int amount)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		data.setInteger("BM:MaxStoredLP", amount);
	}
	
	public static float getPlayerCurrentReagentAmount(EntityPlayer player)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		if(data.hasKey("BM:StoredReagentAmount"))
		{
			return data.getFloat("BM:StoredReagentAmount");
		}
		
		return 0;
	}
	
	public static void setPlayerCurrentReagentAmount(EntityPlayer player, float amount)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		data.setFloat("BM:StoredReagentAmount", amount);
	}
	
	public static float getPlayerMaxReagentAmount(EntityPlayer player)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		if(data.hasKey("BM:MaxReagentAmount"))
		{
			return data.getFloat("BM:MaxReagentAmount");
		}
		
		return 0;
	}
	
	public static void setPlayerMaxReagentAmount(EntityPlayer player, float amount)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		data.setFloat("BM:MaxReagentAmount", amount);
	}
	
	public static Reagent getPlayerReagentType(EntityPlayer player)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		if(data.hasKey("BM:ReagentType"))
		{
			return ReagentRegistry.getReagentForKey(data.getString("BM:ReagentType"));
		}
		
		return null;
	}
	
	public static void setPlayerReagentType(EntityPlayer player, String str)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		data.setString("BM:ReagentType", str);
	}
	
	public static void setPlayerReagentType(EntityPlayer player, Reagent reagent)
	{
		setPlayerReagentType(player, ReagentRegistry.getKeyForReagent(reagent));
	}
	
	public static float getCurrentAdditionalHP(EntityPlayer player)
	{
		NBTTagCompound data = player.getEntityData();
		if(data.hasKey("BM:CurrentAddedHP"))
		{
			return data.getFloat("BM:CurrentAddedHP");
		}
		
		return 0;
	}
	
	public static void setCurrentAdditionalHP(EntityPlayer player, float amount)
	{
		NBTTagCompound data = player.getEntityData();
		data.setFloat("BM:CurrentAddedHP", amount);
	}
	
	public static float getCurrentAdditionalMaxHP(EntityPlayer player)
	{
		NBTTagCompound data = player.getEntityData();
		if(data.hasKey("BM:MaxAddedHP"))
		{
			return data.getFloat("BM:MaxAddedHP");
		}
		
		return 0;
	}
	
	public static void setCurrentAdditionalMaxHP(EntityPlayer player, float maxHP)
	{
		NBTTagCompound data = player.getEntityData();
		data.setFloat("BM:MaxAddedHP", maxHP);	
	}
	
	public static int getPlayerReagentRegenCooldownTag(EntityPlayer player)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		if(data.hasKey("BM:ReagentRegenCooldown"))
		{
			return data.getInteger("BM:ReagentRegenCooldown");
		}
		
		return 0;
	}
	
	public static void setPlayerReagentRegenCooldownTag(EntityPlayer player, int amount)
	{
		NBTTagCompound data = APISpellHelper.getPersistentDataTag(player);
		data.setInteger("BM:ReagentRegenCooldown", amount);
	}
	
	public static ItemStack getOrbForLevel(int level)
	{
		switch(level)
		{
		case 1:
			return new ItemStack(ModItems.weakBloodOrb);
		case 2:
			return new ItemStack(ModItems.apprenticeBloodOrb);
		case 3:
			return new ItemStack(ModItems.magicianBloodOrb);
		case 4:
			return new ItemStack(ModItems.masterBloodOrb);
		case 5:
			return new ItemStack(ModItems.archmageBloodOrb);
		case 6:
			return new ItemStack(ModItems.transcendentBloodOrb);
		default:
			return new ItemStack(Blocks.fire);
		}	
	}
	
	public static MovingObjectPosition raytraceFromEntity(World world, Entity player, boolean par3, double range)
    {
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f;
        if (!world.isRemote && player instanceof EntityPlayer)
            d1 += 1.62D;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        Vec3 vec3 = APISpellHelper.createVec3(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
//        if (player instanceof EntityPlayerMP)
        {
//            d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector((double) f7 * range, (double) f6 * range, (double) f8 * range);
        return world.func_147447_a(vec3, vec31, par3, !par3, par3);
    }
	
	public static Vec3 createVec3(double x, double y, double z)
	{
		return Vec3.createVectorHelper(x, y, z);
	}
	
	public static List<ItemStack> getItemsFromBlock(World world, Block block, int x, int y, int z, int meta, boolean silkTouch, int fortune)
    {
        boolean canSilk = block.canSilkHarvest(world, null, x, y, z, meta);

        if (canSilk && silkTouch)
        {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack item = createStackedBlock(block, meta);
            
            items.add(item);

            return items;
        } else
        {
            return block.getDrops(world, x, y, z, meta, fortune);
        }
    }
	
	public static ItemStack createStackedBlock(Block block, int meta)
    {
        int j = 0;
        if(block == Blocks.lit_redstone_ore)
        {
        	block = Blocks.redstone_ore;
        }
        
        Item item = Item.getItemFromBlock(block);

        if (item != null && item.getHasSubtypes())
        {
            j = meta;
        }

        return new ItemStack(item, 1, j);
    }
	
	public static void spawnItemListInWorld(List<ItemStack> items, World world, float x, float y, float z)
    {
        for (ItemStack stack : items)
        {
            EntityItem itemEntity = new EntityItem(world, x, y, z, stack);
            itemEntity.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(itemEntity);
        }
    }
	
	public static String getNumeralForInt(int num)
    {
        switch (num)
        {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return "";
        }
    }
	
	public static Block getBlockForString(String str)
    {
        String[] parts = str.split(":");
        String modId = parts[0];
        String name = parts[1];
        return GameRegistry.findBlock(modId, name);
    }

    public static Item getItemForString(String str)
    {
        String[] parts = str.split(":");
        String modId = parts[0];
        String name = parts[1];
        return GameRegistry.findItem(modId, name);
    }
    
    public static ItemStack getItemStackForString(String str)
    {
    	String[] parts = str.split(":");
    	int meta = 0;
    	if(parts.length >= 3)
    	{
    		meta = Integer.decode(parts[2]);
    	}else if(parts.length < 2)
    	{
    		return null;
    	}
        String modId = parts[0];
        String name = parts[1];
        
        String itemString = modId + ":" + name;
        Item item = APISpellHelper.getItemForString(itemString);
        if(item != null)
        {
        	return new ItemStack(item, 1, meta);
        }
        
        Block block = APISpellHelper.getBlockForString(itemString);
        if(block != null)
        {
        	return new ItemStack(block, 1, meta);
        }
        
        return null;
    }
    
    public static IRecipe getRecipeForItemStack(ItemStack reqStack) //Does not match NBT. Durrr! -smack-
    {
    	if(reqStack == null)
    	{
    		return null; //Why are you even doing this to yourself!? You know this can't be healthy!
    	}
    	List craftingList = CraftingManager.getInstance().getRecipeList();
    	for(Object posRecipe : craftingList)
    	{
    		if(posRecipe instanceof IRecipe)
    		{
    			ItemStack outputStack = ((IRecipe) posRecipe).getRecipeOutput();
    			if(outputStack != null)
    			{
    				if(outputStack.getItem() == reqStack.getItem() && (outputStack.getItem().getHasSubtypes() ? outputStack.getItemDamage() == reqStack.getItemDamage() : true))
    				{
    					return (IRecipe)posRecipe;
    				}
    			}
    		}
    	}
    	
    	return null;
    }
}
