package WayofTime.alchemicalWizardry.common.items.energy;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.items.interfaces.IReagentManipulator;
import WayofTime.alchemicalWizardry.common.Int3;
import WayofTime.alchemicalWizardry.common.tileEntity.TEReagentConduit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDestinationClearer extends Item implements IReagentManipulator
{	
	public ItemDestinationClearer()
	{
		super();
		this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		this.maxStackSize = 1;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:TankClearer");
    }
	
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Used to clear the destination");
        par3List.add("list for an alchemy container");
    }
		
	@Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
		if(world.isRemote)
		{
			return itemStack;
		}
		
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (movingobjectposition == null)
        {        	
            return itemStack;
        } else
        {
        	if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;
                
                TileEntity tile = world.getTileEntity(x, y, z);
                
                if(!(tile instanceof TEReagentConduit))
                {
                	return itemStack;
                }
                
                TEReagentConduit relay = (TEReagentConduit)tile;

                relay.reagentTargetList.clear();
                
                player.addChatComponentMessage(new ChatComponentText("Destination list now cleared."));
            }
        }
		
		return itemStack;
    }
}
