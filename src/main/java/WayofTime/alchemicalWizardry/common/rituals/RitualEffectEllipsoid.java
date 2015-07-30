package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectEllipsoid extends RitualEffect
{
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        TileEntity tile = world.getTileEntity(pos.offsetUp());

        if (!(tile instanceof IInventory) || ((IInventory) tile).getSizeInventory() < 3)
        {
            return;
        }

        ItemStack item1 = ((IInventory) tile).getStackInSlot(0);
        ItemStack item2 = ((IInventory) tile).getStackInSlot(1);
        ItemStack item3 = ((IInventory) tile).getStackInSlot(2);

        int xSize = item1 == null ? 0 : item1.stackSize;
        int ySize = item2 == null ? 0 : item2.stackSize;
        int zSize = item3 == null ? 0 : item3.stackSize;

        int cost = 5;

        if (currentEssence < cost)
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
        	tile = world.getTileEntity(pos.offsetDown());
        	if(!(tile instanceof IInventory))
        	{
        		return;
        	}
        	
        	ItemBlock placedBlock = null;
        	ItemStack stack = null;
        	int slot = 0;
        	IInventory inv = (IInventory)tile;

        	while(slot < inv.getSizeInventory())
        	{
        		stack = inv.getStackInSlot(slot);
        		if(stack == null)
        		{
        			slot++;
        			continue;
        		}
        		
        		if(stack.getItem() instanceof ItemBlock)
        		{
        			placedBlock = (ItemBlock)stack.getItem();
        			break;
        		}
        		
        		slot++;
        	}
        	
        	if(placedBlock == null || stack == null || slot >= inv.getSizeInventory())
        	{
        		return;
        	}
        	
            int count = 10;
            
            Int3 lastPos = this.getLastPosition(ritualStone.getCustomRitualTag());
        	
        	int i = -xSize;
        	int j = -ySize;
        	int k = -zSize;
        	
        	if(lastPos != null)
        	{
        		i = Math.min(xSize, Math.max(-xSize, lastPos.xCoord));
        		j = Math.min(ySize, Math.max(-ySize, lastPos.yCoord));
        		k = Math.min(zSize, Math.max(-zSize, lastPos.zCoord));
        	}
            
        	boolean incrementNext = false;
        	
            while(j <= ySize)
            {
            	if(pos.getY() + j < 0)
            	{
            		j++;
            		continue;
            	}
            	while(i <= xSize)
                {
                    while(k <= zSize)
                    {
                        if (Math.pow(i * (ySize - 0.50f) * (zSize - 0.50f), 2) + Math.pow(j * (xSize - 0.50f) * (zSize - 0.50f), 2) + Math.pow(k * (xSize - 0.50f) * (ySize - 0.50f), 2) <= Math.pow((xSize - 1 + 0.50f) * (ySize - 1 + 0.50f) * (zSize - 1 + 0.50f), 2))
                        {
                        	k++;
                            continue;
                        }

                        if (Math.pow(i * (ySize + 0.50f) * (zSize + 0.50f), 2) + Math.pow(j * (xSize + 0.50f) * (zSize + 0.50f), 2) + Math.pow(k * (xSize + 0.50f) * (ySize + 0.50f), 2) >= Math.pow((xSize + 0.50f) * (ySize + 0.50f) * (zSize + 0.50f), 2))
                        {
                        	k++;
                            continue;
                        }
                        
                        if(incrementNext || count <= 0)
                    	{
                    		this.setLastPosition(ritualStone.getCustomRitualTag(), new Int3(i, j, k));
                    		return;
                    	}
                        
                        count--;

                        BlockPos newPos = pos.add(i, j, k);
                                                
                        if (!world.isAirBlock(newPos))
                        {
                        	k++;
                            continue;
                        } else
                        {
                        	//This is pulled from the ItemBlock's placing calls
                        	int newState = placedBlock.getMetadata(stack.getMetadata());
                            IBlockState iblockstate1 = placedBlock.block.onBlockPlaced(world, newPos, EnumFacing.UP, 0, 0, 0, newState, null);

                            if (placedBlock.placeBlockAt(stack, null, world, pos, EnumFacing.UP, 0, 0, 0, iblockstate1))
                            {
                                world.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), placedBlock.block.stepSound.getPlaceSound(), (placedBlock.block.stepSound.getVolume() + 1.0F) / 2.0F, placedBlock.block.stepSound.getFrequency() * 0.8F);
                                --stack.stackSize;
                            }
                            
                            this.setLastPosition(ritualStone.getCustomRitualTag(), new Int3(i, j, k));
                            
                            incrementNext = true;
                            SoulNetworkHandler.syphonFromNetwork(owner, cost); 
                                                    	
                            k++;
                        }                      
                    }
                    k = -zSize;
                    i++;
                }
            	i = -xSize;
            	j++;
                this.setLastPosition(ritualStone.getCustomRitualTag(), new Int3(i, j, k));
                return;
            }
            
            this.setLastPosition(ritualStone.getCustomRitualTag(), new Int3(-xSize, -ySize, -zSize));
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> ellipsoidRitual = new ArrayList<RitualComponent>();

        ellipsoidRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(1, 0, -1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(1, 0, 1, RitualComponent.DUSK));

        ellipsoidRitual.add(new RitualComponent(4, 0, 0, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(5, 0, 0, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(5, 0, -1, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(5, 0, -2, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(-4, 0, 0, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(-5, 0, 1, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(-5, 0, 2, RitualComponent.FIRE));

        ellipsoidRitual.add(new RitualComponent(0, 0, 4, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(0, 0, 5, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(1, 0, 5, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(2, 0, 5, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(0, 0, -4, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(0, 0, -5, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(-1, 0, -5, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(-2, 0, -5, RitualComponent.AIR));

        ellipsoidRitual.add(new RitualComponent(3, 0, 1, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(3, 0, 2, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(3, 0, 3, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(2, 0, 3, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(-3, 0, -2, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(-2, 0, -3, RitualComponent.EARTH));

        ellipsoidRitual.add(new RitualComponent(1, 0, -3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(2, 0, -3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(3, 0, -3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(3, 0, -2, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(-2, 0, 3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(-3, 0, 2, RitualComponent.WATER));

        return ellipsoidRitual;
    }
    
    public Int3 getLastPosition(NBTTagCompound tag)
    {
    	if(tag != null && tag.getBoolean("hasWorked"))
    	{
    		return Int3.readFromNBT(tag);
    	}
    	
    	return null;
    }
    
    public void setLastPosition(NBTTagCompound tag, Int3 pos)
    {
    	if(tag != null)
    	{
    		pos.writeToNBT(tag);
    		tag.setBoolean("hasWorked", true);
    	}
    }
}
