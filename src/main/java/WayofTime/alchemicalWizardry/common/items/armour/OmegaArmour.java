package WayofTime.alchemicalWizardry.common.items.armour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.omega.OmegaParadigm;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class OmegaArmour extends BoundArmour
{
	public OmegaParadigm paradigm;
	public Reagent reagent;
	protected boolean storeBiomeID = false;
	protected boolean storeDimensionID = false;
	protected boolean storeYLevel = false;
	protected boolean storeSeesSky = false;
	
	public OmegaArmour(int armorType) 
	{
		super(armorType);
	}
	
	public void setParadigm(OmegaParadigm paradigm)
	{
		this.paradigm = paradigm;
	}
	
	public OmegaParadigm getOmegaParadigm()
	{
		return this.paradigm;
	}
	
	public void setReagent(Reagent reagent)
	{
		this.reagent = reagent;
	}
	
	@Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
		super.onArmorTick(world, player, itemStack);
		
		if(this.storeBiomeID())
		{
			int xCoord = (int) Math.floor(player.posX);
			int zCoord = (int) Math.floor(player.posZ);
			
			BiomeGenBase biome = world.getBiomeGenForCoords(xCoord, zCoord);
			if(biome != null)
			{
				this.setBiomeIDStored(itemStack, biome.biomeID);
			}
		}
		
		if(this.storeDimensionID())
		{
			this.setDimensionIDStored(itemStack, world.provider.dimensionId);
		}
		
		if(this.storeYLevel())
		{
			this.setYLevelStored(itemStack, (int) Math.floor(player.posY));
		}
		
		if(this.armorType == 1)
		{
			paradigm.onUpdate(world, player, itemStack);
			int duration = this.getOmegaStallingDuration(itemStack);
			if(duration > 0)
			{
				this.setOmegaStallingDuration(itemStack, duration - 1);
			}
		}
    }
	
	public void revertArmour(EntityPlayer player, ItemStack itemStack)
	{
		ItemStack stack = this.getContainedArmourStack(itemStack);
		player.inventory.armorInventory[3-this.armorType] = stack;
	}
	
	public ItemStack getSubstituteStack(ItemStack boundStack, int stability, int affinity, Random rand)
	{
		ItemStack omegaStack = new ItemStack(this);
		if(boundStack != null && boundStack.hasTagCompound())
		{
			NBTTagCompound tag = (NBTTagCompound) boundStack.getTagCompound().copy();
			omegaStack.setTagCompound(tag);
		}
		this.setContainedArmourStack(omegaStack, boundStack);
		SoulNetworkHandler.checkAndSetItemOwner(omegaStack, SoulNetworkHandler.getOwnerName(boundStack));
		this.setItemEnchantability(omegaStack, 50);
		
		List enchantList = new ArrayList();
		
		for(int i=0; i<10; i++)
		{
			enchantList.addAll(EnchantmentHelper.buildEnchantmentList(rand, omegaStack, 30));
		}
		
		Map<Enchantment, Map<Integer, Integer>> map = new HashMap();
		
		for(Object obj : enchantList)
		{
            EnchantmentData enchantmentdata = (EnchantmentData)obj;
            
            if(!map.containsKey(enchantmentdata.enchantmentobj))
            {
            	map.put(enchantmentdata.enchantmentobj, new HashMap());
            }
            
            Map<Integer, Integer> numMap = map.get(enchantmentdata.enchantmentobj);
            if(numMap.containsKey(enchantmentdata.enchantmentLevel))
            {
            	numMap.put(enchantmentdata.enchantmentLevel, numMap.get(enchantmentdata.enchantmentLevel)+1);
            }else
            {
            	numMap.put(enchantmentdata.enchantmentLevel, 1);
            }
		}
		
		List newEnchantList = new ArrayList();
		
		for(Entry<Enchantment, Map<Integer, Integer>> entry : map.entrySet()) //Assume enchant # 0 is level 1 enchant
		{
			Enchantment ench = entry.getKey();
			Map<Integer, Integer> numMap = entry.getValue();
			
			if(numMap.isEmpty())
			{
				continue;
			}

			int[] enchantValues = new int[1];
			
			for(Entry<Integer, Integer> entry1 : numMap.entrySet())
			{
				int enchantLevel = entry1.getKey();
				int number = entry1.getValue();
				
				if(enchantLevel >= enchantValues.length)
				{
					int[] newEnchantValues = new int[enchantLevel+1];
					for(int i=0; i< enchantValues.length; i++)
					{
						newEnchantValues[i] = enchantValues[i];
					}
					
					enchantValues = newEnchantValues;
				}
				
				enchantValues[enchantLevel] += number;
			}
			
			int size = enchantValues.length;
			int i = 0;
			while(i<size)
			{
				int number = enchantValues[i];
				if(number >= 2 && i+1 >= size)
				{
					int[] newEnchantValues = new int[i+2];
					for(int z=0; z< enchantValues.length; z++)
					{
						newEnchantValues[z] = enchantValues[z];
					}
					
					enchantValues = newEnchantValues;
					enchantValues[i+1] += number/2;
					size = enchantValues.length;
				}
				i++;
			}
			
			newEnchantList.add(new EnchantmentData(ench, enchantValues.length-1));
		}
		
		Iterator iterator = newEnchantList.iterator();

        while (iterator.hasNext())
        {
            EnchantmentData enchantmentdata = (EnchantmentData)iterator.next();

            {
                omegaStack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
            }
        }
		
		for(int i=0; i<1; i++)
		{
//			omegaStack = EnchantmentHelper.addRandomEnchantment(new Random(), omegaStack, 30);
		}
		return omegaStack;
	}
	
	public void setContainedArmourStack(ItemStack omegaStack, ItemStack boundStack)
	{
		if(omegaStack == null || boundStack == null)
		{
			return;
		}
		
		NBTTagCompound tag = new NBTTagCompound();
		boundStack.writeToNBT(tag);
		
		NBTTagCompound omegaTag = omegaStack.getTagCompound();
		if(omegaTag == null)
		{
			omegaTag = new NBTTagCompound();
			omegaStack.setTagCompound(omegaTag);
		}
		
		omegaTag.setTag("armour", tag);
	}
	
	public ItemStack getContainedArmourStack(ItemStack omegaStack)
	{
		NBTTagCompound omegaTag = omegaStack.getTagCompound();
		if(omegaTag == null)
		{
			return null;
		}
		
		NBTTagCompound tag = omegaTag.getCompoundTag("armour");
		ItemStack armourStack = ItemStack.loadItemStackFromNBT(tag);
		
		return armourStack;
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
		return "alchemicalwizardry:models/armor/OmegaWater.png";
    }
	
	@SideOnly(Side.CLIENT)
	public abstract ModelBiped getChestModel();
	@SideOnly(Side.CLIENT)
	public abstract ModelBiped getLegsModel();
	@SideOnly(Side.CLIENT)
	ModelBiped model1 = null;
	@SideOnly(Side.CLIENT)
    ModelBiped model2 = null;
	@SideOnly(Side.CLIENT)
    ModelBiped model = null;

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
    {
        if (tryComplexRendering)
        {
            int type = ((ItemArmor) itemStack.getItem()).armorType;
            if (this.model1 == null)
            {
                this.model1 = getChestModel();
            }
            if (this.model2 == null)
            {
                this.model2 = getLegsModel();
            }

            if (type == 1 || type == 3 || type == 0)
            {
                this.model = model1;
            } else
            {
                this.model = model2;
            }

            if (this.model != null)
            {
                this.model.bipedHead.showModel = (type == 0);
                this.model.bipedHeadwear.showModel = (type == 0);
                this.model.bipedBody.showModel = ((type == 1) || (type == 2));
                this.model.bipedLeftArm.showModel = (type == 1);
                this.model.bipedRightArm.showModel = (type == 1);
                this.model.bipedLeftLeg.showModel = (type == 2 || type == 3);
                this.model.bipedRightLeg.showModel = (type == 2 || type == 3);
                this.model.isSneak = entityLiving.isSneaking();

                this.model.isRiding = entityLiving.isRiding();
                this.model.isChild = entityLiving.isChild();

                this.model.aimedBow = false;
                this.model.heldItemRight = (entityLiving.getHeldItem() != null ? 1 : 0);

                if ((entityLiving instanceof EntityPlayer))
                {
                    if (((EntityPlayer) entityLiving).getItemInUseDuration() > 0)
                    {
                        EnumAction enumaction = ((EntityPlayer) entityLiving).getItemInUse().getItemUseAction();
                        if (enumaction == EnumAction.block)
                        {
                            this.model.heldItemRight = 3;
                        } else if (enumaction == EnumAction.bow)
                        {
                            this.model.aimedBow = true;
                        }
                    }
                }
            }

            return model;

        } else
        {
            return super.getArmorModel(entityLiving, itemStack, armorSlot);
        }
    }
    
    public void onOmegaKeyPressed(EntityPlayer player, ItemStack stack)
    {
    	if(paradigm != null)
    	{
    		paradigm.onOmegaKeyPressed(player, stack);
    	}
    }
    
    public void setOmegaStallingDuration(ItemStack stack, int duration)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	tag.setInteger("OmegaStallDuration", duration);
    }
    
    public int getOmegaStallingDuration(ItemStack stack)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	return tag.getInteger("OmegaStallDuration");
    }
    
    public boolean hasOmegaStalling(ItemStack stack)
    {
    	return this.getOmegaStallingDuration(stack) > 0;
    }
    
    public boolean storeBiomeID()
    {
    	return this.storeBiomeID;
    }
    
    public boolean storeDimensionID()
    {
    	return this.storeDimensionID;
    }
    
    public boolean storeYLevel()
    {
    	return this.storeYLevel;
    }
    
    public boolean storeSeesSky()
    {
    	return this.storeSeesSky;
    }
    
    public int getBiomeIDStored(ItemStack stack)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	return tag.getInteger("biomeID");
    }
    
    public void setBiomeIDStored(ItemStack stack, int id)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	tag.setInteger("biomeID", id);
    }
    
    public int getDimensionIDStored(ItemStack stack)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	return tag.getInteger("dimensionID");
    }
    
    public void setDimensionIDStored(ItemStack stack, int id)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	tag.setInteger("dimensionID", id);
    }
    
    public int getYLevelStored(ItemStack stack)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	return tag.getInteger("yLevel");
    }
    
    public void setYLevelStored(ItemStack stack, int level)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	tag.setInteger("yLevel", level);
    }
}
