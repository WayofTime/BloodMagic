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

public class RitualEffectCrushing extends RitualEffect
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

        if (world.getWorldTime() % 40 != 20)
        {
            return;
        }


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

        boolean isSilkTouch = this.isSilkTouch(world, x, y, z);
    	int fortuneLevel = this.getFortuneLevel(world, x, y, z);
        
        if (currentEssence < this.getCostPerRefresh())
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            //boolean flag = false;
            for (int j = -3; j < 0; j++)
            {
                for (int i = -1; i <= 1; i++)
                {
                    for (int k = -1; k <= 1; k++)
                    {
                        Block block = world.getBlock(x + i, y + j, z + k);
                        int meta = world.getBlockMetadata(x + i, y + j, z + k);

                        if (block != null && !world.isAirBlock(x + i, y + j, z + k))
                        {
                            if ((block.equals(ModBlocks.ritualStone) || block.equals(ModBlocks.blockMasterStone)))
                            {
                                continue;
                            }

                            if(isSilkTouch && block.canSilkHarvest(world, null, x + i, y + j, z + k, meta))
                            {
                            	int invSize = tileEntity.getSizeInventory();
                            	
                            	ItemStack item =  new ItemStack(block,1,meta);
                                ItemStack copyStack = item.copyItemStack(item);

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
                                            if (itemStack.getItem().equals(copyStack.getItem()) && itemStack.getItemDamage() == copyStack.getItemDamage())
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

                                if (copyStack.stackSize > 0)
                                {
                                    world.spawnEntityInWorld(new EntityItem(world, x + 0.4, y + 2, z + 0.5, copyStack));
                                    //flag=true;
                                }
                                
                            }
                            else
                            {
	                            ArrayList<ItemStack> itemDropList = block.getDrops(world, x + i, y + j, z + k, meta, fortuneLevel);
	
	                            if (itemDropList != null)
	                            {
	                                int invSize = tileEntity.getSizeInventory();
	
	                                for (ItemStack item : itemDropList)
	                                {
	                                    ItemStack copyStack = item.copyItemStack(item);
	
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
	                                                if (itemStack.getItem().equals(copyStack.getItem()) && itemStack.getItemDamage() == copyStack.getItemDamage())
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
	
	                                    if (copyStack.stackSize > 0)
	                                    {
	                                        world.spawnEntityInWorld(new EntityItem(world, x + 0.4, y + 2, z + 0.5, copyStack));
	                                        //flag=true;
	                                    }
	                                }
	                            }
                            
                            }

                            //if(flag)
                            world.setBlockToAir(x + i, y + j, z + k);
                            world.playSoundEffect(x + i, y + j, z + k, "mob.endermen.portal", 1.0F, 1.0F);
                            data.currentEssence = currentEssence - this.getCostPerRefresh();
                            data.markDirty();
                            return;
                        }
                    }
                }
            }
        }
    }
    
    public boolean isSilkTouch(World world, int x, int y, int z)
    {
    	int index = 0;
    	for(int i=-2; i<=2; i++)
    	{
    		for(int j=-2; j<=2; j++)
    		{
    			int index1 = Math.abs(i);
    			int index2 = Math.abs(j);
    			
    			if((index1 == 2 && (index2 == 2 || index2 == 1)) || (index1 == 1 && index2 == 2))
    			{
    				Block block = world.getBlock(x + i, y + 1, z + j);
                    if(block == Blocks.gold_block)
                    {
                    	index++;
                    }
    			}
    		}
    	}
    	
    	return index>=12;
    }
    
    public int getFortuneLevel(World world, int x, int y, int z)
    {
    	int index = 0;
    	for(int i=-2; i<=2; i++)
    	{
    		for(int j=-2; j<=2; j++)
    		{
    			int index1 = Math.abs(i);
    			int index2 = Math.abs(j);
    			
    			if((index1 == 2 && (index2 == 2 || index2 == 1)) || (index1 == 1 && index2 == 2))
    			{
    				Block block = world.getBlock(x + i, y + 1, z + j);
                    if(block == Blocks.emerald_block || block == Blocks.diamond_block)
                    {
                    	index++;
                    }
    			}
    		}
    	}
    	
    	if(index>=12)
    	{
    		return 3;
    	}else if(index>=8)
    	{
    		return 2;
    	}else if(index>=4)
    	{
    		return 1;
    	}
    	
    	return 0;
    }

    @Override
    public int getCostPerRefresh()
    {
        return 7;
    }

    @Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> crushingRitual = new ArrayList();
        crushingRitual.add(new RitualComponent(0, 0, 1, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(1, 0, 0, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(0, 0, -1, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(2, 0, 0, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(0, 0, 2, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(0, 0, -2, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(2, 0, 2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(2, 0, -2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(2, 1, 0, RitualComponent.AIR));
        crushingRitual.add(new RitualComponent(-2, 1, 0, RitualComponent.AIR));
        crushingRitual.add(new RitualComponent(0, 1, 2, RitualComponent.AIR));
        crushingRitual.add(new RitualComponent(0, 1, -2, RitualComponent.AIR));
        return crushingRitual;
	}
}
