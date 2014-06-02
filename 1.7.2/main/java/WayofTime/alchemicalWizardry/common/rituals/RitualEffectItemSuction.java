package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectItemSuction extends RitualEffect
{
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;
        World world = ritualStone.getWorld();

        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();
        TileEntity tile = world.getTileEntity(x, y + 1, z);
        IInventory tileEntity;

        if (tile instanceof IInventory)
        {
            tileEntity = (IInventory) tile;
        } else
        {
            return;
        }

        if (tileEntity.getSizeInventory() <= 0)
        {
            return;
        }
        
        if (currentEssence < this.getCostPerRefresh()*100)
        {
            EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            List<EntityItem> itemDropList = SpellHelper.getItemsInRange(world, x+0.5f, y+0.5f, z+0.5f, 10, 10);

            int count = 0;
            
            if (itemDropList != null)
            {
                int invSize = tileEntity.getSizeInventory();

                for (EntityItem itemEntity : itemDropList)
                {
                	ItemStack item = itemEntity.getEntityItem();
                    ItemStack copyStack = itemEntity.getEntityItem().copy();
                    
                    count++;

                    for (int n = 0; n < invSize; n++)
                    {
                        if (tileEntity.isItemValidForSlot(n, copyStack) && copyStack.stackSize != 0)
                        {
                            ItemStack itemStack = tileEntity.getStackInSlot(n);

                            if (itemStack == null)
                            {
                                tileEntity.setInventorySlotContents(n, item);
                                copyStack.stackSize = 0;
                            } else
                            {
                                if (itemStack.getItem().equals(copyStack.getItem()))
                                {
                                    int itemSize = itemStack.stackSize;
                                    int copySize = copyStack.stackSize;
                                    int maxSize = itemStack.getMaxStackSize();

                                    if (copySize + itemSize < maxSize)
                                    {
                                        copyStack.stackSize = 0;
                                        itemStack.stackSize = itemSize + copySize;
                                        tileEntity.setInventorySlotContents(n, itemStack);
                                    } else
                                    {
                                        copyStack.stackSize = itemSize + copySize - maxSize;
                                        itemStack.stackSize = maxSize;
                                    }
                                }
                            }
                        }
                    }

                    if(copyStack.stackSize<=0)
                    {
                    	itemEntity.setDead();
                    }
                    
                    if (copyStack.stackSize > 0)
                    {
                        world.spawnEntityInWorld(new EntityItem(world, x + 0.4, y + 2, z + 0.5, copyStack));
                        //flag=true;
                    }
                }
            }
            
            if(count>0)
            {
            	data.currentEssence = currentEssence - this.getCostPerRefresh()*Math.min(count, 100);
                data.markDirty();
                return;
            }
        }                
    }

    @Override
    public int getCostPerRefresh()
    {
        return 5;
    }

    @Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> suctionRitual = new ArrayList();
        suctionRitual.add(new RitualComponent(2, 0, 0, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(0, 0, 2, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(0, 0, -2, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(1, 1, 1, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(1, 1, -1, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(-1, 1, 1, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(-1, 1, -1, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(1, -1, 0, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(-1, -1, 0, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(0, -1, 1, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(0, -1, -1, RitualComponent.AIR));
        return suctionRitual;
	}
}
