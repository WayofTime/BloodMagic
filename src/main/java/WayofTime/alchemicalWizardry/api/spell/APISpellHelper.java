package WayofTime.alchemicalWizardry.api.spell;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;

public class APISpellHelper 
{
	public static int getPlayerLPTag(EntityPlayer player)
	{
		NBTTagCompound data = player.getEntityData();
		if(data.hasKey("BM:StoredLP"))
		{
			return data.getInteger("BM:StoredLP");
		}
		
		return 0;
	}
	
	public static void setPlayerLPTag(EntityPlayer player, int amount)
	{
		NBTTagCompound data = player.getEntityData();
		data.setInteger("BM:StoredLP", amount);
	}
	
	public static int getPlayerMaxLPTag(EntityPlayer player)
	{
		NBTTagCompound data = player.getEntityData();
		if(data.hasKey("BM:MaxStoredLP"))
		{
			return data.getInteger("BM:MaxStoredLP");
		}
		
		return 0;
	}
	
	public static void setPlayerMaxLPTag(EntityPlayer player, int amount)
	{
		NBTTagCompound data = player.getEntityData();
		data.setInteger("BM:MaxStoredLP", amount);
	}
	
	public static float getPlayerCurrentReagentAmount(EntityPlayer player)
	{
		NBTTagCompound data = player.getEntityData();
		if(data.hasKey("BM:StoredReagentAmount"))
		{
			return data.getFloat("BM:StoredReagentAmount");
		}
		
		return 0;
	}
	
	public static void setPlayerCurrentReagentAmount(EntityPlayer player, float amount)
	{
		NBTTagCompound data = player.getEntityData();
		data.setFloat("BM:StoredReagentAmount", amount);
	}
	
	public static float getPlayerMaxReagentAmount(EntityPlayer player)
	{
		NBTTagCompound data = player.getEntityData();
		if(data.hasKey("BM:MaxReagentAmount"))
		{
			return data.getFloat("BM:MaxReagentAmount");
		}
		
		return 0;
	}
	
	public static void setPlayerMaxReagentAmount(EntityPlayer player, float amount)
	{
		NBTTagCompound data = player.getEntityData();
		data.setFloat("BM:MaxReagentAmount", amount);
	}
	
	public static Reagent getPlayerReagentType(EntityPlayer player)
	{
		NBTTagCompound data = player.getEntityData();
		if(data.hasKey("BM:ReagentType"))
		{
			return ReagentRegistry.getReagentForKey(data.getString("BM:ReagentType"));
		}
		
		return null;
	}
	
	public static void setPlayerReagentType(EntityPlayer player, String str)
	{
		NBTTagCompound data = player.getEntityData();
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
//            d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        return world.func_147447_a(vec3, vec31, par3, !par3, par3);
    }
	
	public static List<ItemStack> getItemsFromBlock(World world, Block block, int x, int y, int z, int meta, boolean silkTouch, int fortune)
    {
        boolean canSilk = block.canSilkHarvest(world, null, x, y, z, meta);

        if (canSilk && silkTouch)
        {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack item = new ItemStack(block, 1, meta);

            items.add(item);

            return items;
        } else
        {
            return block.getDrops(world, x, y, z, meta, fortune);
        }
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
}
