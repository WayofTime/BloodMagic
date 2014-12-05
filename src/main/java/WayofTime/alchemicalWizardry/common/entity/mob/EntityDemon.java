package WayofTime.alchemicalWizardry.common.entity.mob;

import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.IDemon;
import WayofTime.alchemicalWizardry.common.items.DemonPlacer;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class EntityDemon extends EntityTameable implements IDemon
{
    private boolean isAggro;
    private String demonID;
    
    protected boolean dropCrystal = true;

    public EntityDemon(World par1World, String demonID)
    {
        super(par1World);
        this.demonID = demonID;
    }
    
    @Override
    public boolean getDoesDropCrystal()
    {
    	return dropCrystal;
    }
    
    @Override
    public void setDropCrystal(boolean crystal)
    {
    	this.dropCrystal = crystal;
    }

    @Override
    public void setSummonedConditions()
    {
        this.setAggro(true);
    }

    @Override
    public boolean isAggro()
    {
        return this.isAggro;
    }

    @Override
    public void setAggro(boolean aggro)
    {
        this.isAggro = aggro;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
    	if(this.getDoesDropCrystal())
    	{
    		ItemStack drop = new ItemStack(ModItems.demonPlacer);
            
            DemonPlacer.setDemonString(drop, this.getDemonID());

            if ((this.getOwner() instanceof EntityPlayer))
            {
                DemonPlacer.setOwnerName(drop, SpellHelper.getUsername((EntityPlayer) this.getOwner()));
            }

            if (this.hasCustomNameTag())
            {
                drop.setStackDisplayName(this.getCustomNameTag());
            }

            this.entityDropItem(drop, 0.0f);
    	} 
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.isAggro() && worldObj.getWorldTime() % 100 == 0)
        {
            this.heal(1);
        }
    }

    public void sendSittingMessageToPlayer(EntityPlayer owner, boolean isSitting)
    {
        if (owner != null && owner.worldObj.isRemote)
        {
            ChatComponentText chatmessagecomponent;

            if (isSitting)
            {
                chatmessagecomponent = new ChatComponentText("I will stay here for now, Master.");
            } else
            {
                chatmessagecomponent = new ChatComponentText("I shall follow and protect you!");
            }

            owner.addChatComponentMessage(chatmessagecomponent);
        }
    }

    public String getDemonID()
    {
        return this.demonID;
    }
    
    protected void setDemonID(String id)
    {
    	this.demonID = id;
    }
}
