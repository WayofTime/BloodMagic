package WayofTime.alchemicalWizardry.common.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.IDemon;
import WayofTime.alchemicalWizardry.common.items.DemonPlacer;

public class EntityDemon extends EntityTameable implements IDemon
{
	private boolean isAggro;
	private int demonID;

	public EntityDemon(World par1World, int demonID)
	{
		super(par1World);
		this.demonID = demonID;
	}

	@Override
	public void setSummonedConditions()
	{
		setAggro(true);
	}

	@Override
	public boolean isAggro()
	{
		return isAggro;
	}

	@Override
	public void setAggro(boolean aggro)
	{
		isAggro = aggro;
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
		ItemStack drop = new ItemStack(AlchemicalWizardry.demonPlacer, 1, getDemonID());
		DemonPlacer.setOwnerName(drop, getOwnerName());

		if (hasCustomNameTag())
		{
			drop.setItemName(getCustomNameTag());
		}

		entityDropItem(drop, 0.0f);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		if (!isAggro() && worldObj.getWorldTime() % 100 == 0)
		{
			heal(1);
		}
	}

	public void sendSittingMessageToPlayer(EntityPlayer owner, boolean isSitting)
	{
		if (owner != null && owner.worldObj.isRemote)
		{
			ChatMessageComponent chatmessagecomponent = new ChatMessageComponent();

			if (isSitting)
			{
				chatmessagecomponent.addText("I will stay here for now, Master.");
			}
			else
			{
				chatmessagecomponent.addText("I shall follow and protect you!");
			}

			owner.sendChatToPlayer(chatmessagecomponent);
		}
	}

	public int getDemonID()
	{
		return demonID;
	}

	@Override
	public Entity getOwner() {
		return func_130012_q();
	}

}
