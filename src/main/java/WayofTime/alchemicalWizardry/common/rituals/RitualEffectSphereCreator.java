package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectSphereCreator extends RitualEffect
{
//    private static final int potentiaDrain = 10;
	public static int MAX_RADIUS = 32;
	private static final int terraeDrain = 1;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

//        boolean hasPotentia = this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);

        if (world.getWorldTime() % 1 != 0)
        {
            return;
        }

        if (currentEssence < this.getCostPerRefresh())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
        	TileEntity tile = world.getTileEntity(x, y+1, z);
        	if(!(tile instanceof IInventory))
        	{
        		return;
        	}
        	
        	int negYOffset = 0;
        	int radius = 0;
        	IInventory inv = (IInventory)tile;
        	
        	int invSize = inv.getSizeInventory();
            if(invSize < 1)
            {
            	return;
            }else 
            {
            	if(invSize >= 2)
            	{
            		ItemStack invStack2 = inv.getStackInSlot(1);
            		if(invStack2 != null)
            		{
            			negYOffset = invStack2.stackSize;
            		}
            	}
            	
            	ItemStack invStack1 = inv.getStackInSlot(0);
            	if(invStack1 == null)
            	{
            		return;
            	}

            	radius = invStack1.stackSize;
            }
        	
            if(radius <= 0)
            {
            	return;
            }
            
            radius = Math.min(radius, MAX_RADIUS);
            
            if(negYOffset < radius + 3)
            {
            	negYOffset = radius + 3;
            }
            
        	Int3 lastPos = this.getLastPosition(ritualStone.getCustomRitualTag(), radius);
        	
        	int j = -radius;
        	int i = -radius;
        	int k = -radius;
        	
        	if(lastPos != null)
        	{
        		j = Math.min(radius, Math.max(-radius, lastPos.yCoord));
        		i = Math.min(radius, Math.max(-radius, lastPos.xCoord));
        		k = Math.min(radius, Math.max(-radius, lastPos.zCoord));
        	}
        	
        	int yP = y + negYOffset;
        	int yN = y - negYOffset;
        	
        	boolean incrementNext = false;
        	
            
            while(i <= radius)
            {
            	while(j <= radius)
                {
                    while(k <= radius)
                    {
                    	if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
                        {
                    		k++;
                            continue;
                        }
                    	
                    	if(incrementNext)
                    	{
                    		this.setLastPosition(ritualStone.getCustomRitualTag(), new Int3(i, j, k));
                    		return;
                    	}
                    	
                    	Block blk = world.getBlock(x + i, yP + j, z + k);

                        if (world.isAirBlock(x + i, yN + j, z + k) || (!world.isAirBlock(x + i, yP + j, z + k) && !SpellHelper.isBlockFluid(blk)))
                        {
                        	
                        	k++;
                            continue;
                        }

                        if(BlockTeleposer.swapBlocks(this, world, world, x + i, yN + j, z + k, x + i, yP + j, z + k, false, 2))
                        {
                            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
                            if(this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, true))
                        	{
                        		world.setBlock(x + i, yN + j, z + k, Blocks.dirt, 0, 2);
                        	}
                        }
                        
                        this.setLastPosition(ritualStone.getCustomRitualTag(), new Int3(i, j, k));
                        
                        incrementNext = true;

                        k++;
                    }
                    k = -radius;
                    j++;
                }
                j = -radius;
                i++;
//                this.setLastPosition(ritualStone.getCustomRitualTag(), new Int3(i, j, k));
//                return;
            }
            
            ritualStone.setActive(false);
            this.setLastPosition(ritualStone.getCustomRitualTag(), new Int3(i, j, k));
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return AlchemicalWizardry.ritualCostSphereIsland[1];
    }
    
    public Int3 getLastPosition(NBTTagCompound tag, int radius)
    {
    	if(tag != null && tag.getBoolean("hasWorked"))
    	{
    		return Int3.readFromNBT(tag);
    	}
    	
    	return new Int3(-radius, -radius, -radius);
    }
    
    public void setLastPosition(NBTTagCompound tag, Int3 pos)
    {
    	if(tag != null)
    	{
    		pos.writeToNBT(tag);
    		tag.setBoolean("hasWorked", true);
    	}
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> magneticRitual = new ArrayList();
        magneticRitual.add(new RitualComponent(1, 0, 1, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(1, 0, -1, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(2, 1, 0, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(0, 1, 2, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(-2, 1, 0, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(0, 1, -2, RitualComponent.EARTH));
        magneticRitual.add(new RitualComponent(2, 1, 2, RitualComponent.AIR));
        magneticRitual.add(new RitualComponent(2, 1, -2, RitualComponent.AIR));
        magneticRitual.add(new RitualComponent(-2, 1, 2, RitualComponent.AIR));
        magneticRitual.add(new RitualComponent(-2, 1, -2, RitualComponent.AIR));
        magneticRitual.add(new RitualComponent(2, 2, 0, RitualComponent.FIRE));
        magneticRitual.add(new RitualComponent(0, 2, 2, RitualComponent.FIRE));
        magneticRitual.add(new RitualComponent(-2, 2, 0, RitualComponent.FIRE));
        magneticRitual.add(new RitualComponent(0, 2, -2, RitualComponent.DUSK));
        return magneticRitual;
    }
}
