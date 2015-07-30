package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.IAltarManipulator;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

public class ItemBloodLetterPack extends ItemArmor implements ArmourUpgrade, IAltarManipulator
{    
    public int conversionRate = 100; //LP / half heart
    public float activationPoint = 0.5f;
    public int tickRate = 20;
    public int maxStored = 10000;

    public ItemBloodLetterPack()
    {
        super(ArmorMaterial.CHAIN, 0, 1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.bloodletterpack.desc"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.lp.storedlp") + " " + this.getStoredLP(par1ItemStack));
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return "alchemicalwizardry:models/armor/bloodPack_layer_1.png";
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return itemStack;
        }

        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (movingobjectposition == null)
        {
        	return super.onItemRightClick(itemStack, world, player);
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                BlockPos pos = movingobjectposition.func_178782_a();

                TileEntity tile = world.getTileEntity(pos);

                if (!(tile instanceof TEAltar))
                {
                    return super.onItemRightClick(itemStack, world, player);
                }
                
                TEAltar altar = (TEAltar)tile;
                
                if(!altar.isActive())
                {
                	int amount = this.getStoredLP(itemStack);
                	if(amount > 0)
                	{
                		int filledAmount = altar.fillMainTank(amount);
                		amount -= filledAmount;
                		this.setStoredLP(itemStack, amount);
                		
                		world.markBlockForUpdate(pos);
                	}
                }
            }
        }
        
        return itemStack;
    }
    
    public void setStoredLP(ItemStack itemStack, int lp)
    {
    	if(!itemStack.hasTagCompound())
    	{
    		itemStack.setTagCompound(new NBTTagCompound());
    	}
    	
    	NBTTagCompound tag = itemStack.getTagCompound();
    	
    	tag.setInteger("storedLP", lp);
    }
    
    public int getStoredLP(ItemStack itemStack)
    {
    	if(!itemStack.hasTagCompound())
    	{
    		itemStack.setTagCompound(new NBTTagCompound());
    	}
    	
    	NBTTagCompound tag = itemStack.getTagCompound();
    	
    	return tag.getInteger("storedLP");
    }
    
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
    	//This is where I need to do the updating
    	if(world.isRemote)
    	{
    		return;
    	}
    	
    	if(world.getWorldTime() % tickRate == 0)
    	{
    		boolean shouldExecute = player.getHealth() / player.getMaxHealth() > activationPoint && this.getStoredLP(itemStack) < maxStored;
    		
    		if(shouldExecute)
    		{
    			SoulNetworkHandler.hurtPlayer(player, 1.0f);
    			this.setStoredLP(itemStack, Math.min(this.getStoredLP(itemStack) + conversionRate, maxStored));
    		}
    	}
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {

    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 0;
    }
}