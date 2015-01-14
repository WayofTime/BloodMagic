package WayofTime.alchemicalWizardry.common.items.armour;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.omega.OmegaParadigm;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class OmegaArmour extends BoundArmour
{
	public OmegaParadigm paradigm;
	public Reagent reagent;
	
	public OmegaArmour(int armorType) 
	{
		super(armorType);
	}
	
	public void setParadigm(OmegaParadigm paradigm)
	{
		this.paradigm = paradigm;
	}
	
	public void setReagent(Reagent reagent)
	{
		this.reagent = reagent;
	}
	
	@Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
		super.onArmorTick(world, player, itemStack);

		if(this.armorType == 1)
		{
			paradigm.onUpdate(world, player, itemStack);
		}
    }
	
	public void revertArmour(EntityPlayer player, ItemStack itemStack)
	{
		ItemStack stack = this.getContainedArmourStack(itemStack);
		player.inventory.armorInventory[3-this.armorType] = stack;
	}
	
	public ItemStack getSubstituteStack(ItemStack boundStack)
	{
		ItemStack omegaStack = new ItemStack(this);
		if(boundStack != null && boundStack.hasTagCompound())
		{
			NBTTagCompound tag = (NBTTagCompound) boundStack.getTagCompound().copy();
			omegaStack.setTagCompound(tag);
		}
		this.setContainedArmourStack(omegaStack, boundStack);
		SoulNetworkHandler.checkAndSetItemOwner(omegaStack, SoulNetworkHandler.getOwnerName(boundStack));
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
	
	public abstract ModelBiped getChestModel();
	
	public abstract ModelBiped getLegsModel();

	ModelBiped model1 = null;
    ModelBiped model2 = null;
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
}
