package WayofTime.alchemicalWizardry.common;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageKeyPressed implements IMessage, IMessageHandler<MessageKeyPressed, IMessage>
{
    private byte keyPressed;

    public MessageKeyPressed()
    {
    }

    public MessageKeyPressed(Key key)
    {
        if (key == Key.OMEGA_ACTIVE)
        {
            this.keyPressed = (byte) Key.OMEGA_ACTIVE.ordinal();
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.keyPressed = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(keyPressed);
    }

    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx)
    {
        EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;

        if(message.keyPressed == Key.OMEGA_ACTIVE.ordinal())
        {
        	if(entityPlayer != null)
            {
            	ItemStack[] armourInventory = entityPlayer.inventory.armorInventory;
            	if(armourInventory[2] != null)
            	{
            		ItemStack chestStack = armourInventory[2];
            		if(chestStack.getItem() instanceof OmegaArmour)
            		{
            			((OmegaArmour)chestStack.getItem()).onOmegaKeyPressed(entityPlayer, chestStack);
            		}
            	}
            }
        }

        return null;
    }
    
    public enum Key
    {
    	OMEGA_ACTIVE
    }
}
