package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.Int3;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralContainer;

public class RitualEffectEvaporation extends RitualEffect
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
        
        if (currentEssence < 0)
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
        	Block block1 = world.getBlock(x, y-1, z);
        	int range = this.getRadiusForModifierBlock(block1);
        	
        	boolean[][][] boolList = new boolean[range * 2 + 1][range * 2 + 1][range * 2 + 1];

            for (int i = 0; i < 2 * range + 1; i++)
            {
                for (int j = 0; j < 2 * range + 1; j++)
                {
                	for(int k = 0; k < 2 * range + 1; k++)
                	{
                        boolList[i][j][k] = false;
                	}
                }
            }
           
            boolList[range][range][range] = true;
            boolean isReady = false;

            while (!isReady)
            {
                isReady = true;

                for (int i = 0; i < 2 * range + 1; i++)
                {
                    for (int j = 0; j < 2 * range + 1; j++)
                    {
                    	for(int k=0; k<2*range+1;k++)
                    	{
                            if (boolList[i][j][k])
                            {
                                if (i - 1 >= 0 && !boolList[i - 1][j][k])
                                {
                                    Block block = world.getBlock(x - range + i - 1, y - range + j, z - range + k);
                                    if(world.isAirBlock(x - range + i - 1, y - range + j, z - range + k) || block == ModBlocks.blockSpectralContainer)
                                    {
                                        boolList[i - 1][j][k] = true;
                                        isReady = false;
                                    }
                                }

                                if (j - 1 >= 0 && !boolList[i][j - 1][k])
                                {
                                    Block block = world.getBlock(x - range + i, y - range + j - 1, z - range + k);
                                    if(world.isAirBlock(x - range + i, y - range + j - 1, z - range + k) || block == ModBlocks.blockSpectralContainer)
                                    {
                                        boolList[i][j - 1][k] = true;
                                        isReady = false;
                                    }
                                }
                                
                                if(k - 1 >=0 && !boolList[i][j][k - 1])
                                {
                                	Block block = world.getBlock(x - range + i, y - range + j, z - range + k - 1);
                                    if(world.isAirBlock(x - range + i, y - range + j, z - range + k - 1) || block == ModBlocks.blockSpectralContainer)
                                    {
                                        boolList[i][j][k - 1] = true;
                                        isReady = false;
                                    }
                                }

                                if (i + 1 <= 2 * range && !boolList[i + 1][j][k])
                                {
                                    Block block = world.getBlock(x - range + i + 1, y - range + j, z - range + k);
                                    if(world.isAirBlock(x - range + i + 1, y - range + j, z - range + k) || block == ModBlocks.blockSpectralContainer)
                                    {
                                        boolList[i + 1][j][k] = true;
                                        isReady = false;
                                    }
                                }

                                if (j + 1 <= 2 * range && !boolList[i][j + 1][k])
                                {
                                    Block block = world.getBlock(x - range + i, y - range + j + 1, z - range + k);
                                    if(world.isAirBlock(x - range + i, y - range + j + 1, z - range + k) || block == ModBlocks.blockSpectralContainer)
                                    {
                                        boolList[i][j + 1][k] = true;
                                        isReady = false;
                                    }
                                }
                                
                                if(k + 1 <= 2*range && !boolList[i][j][k+1])
                                {
                                	Block block = world.getBlock(x - range + i, y - range + j, z - range + k + 1);
                                    if(world.isAirBlock(x - range + i, y - range + j, z - range + k + 1) || block == ModBlocks.blockSpectralContainer)
                                    {
                                        boolList[i][j][k+1] = true;
                                        isReady = false;
                                    }
                                }
                            }
                    	}
                    }
                }
            }
            
            for (int i = 0; i < 2 * range + 1; i++)
            {
                for (int j = 0; j < 2 * range + 1; j++)
                {
                	for(int k=0; k<2*range+1;k++)
			        {          	
			        	if(!boolList[i][j][k])
			    		{
			    			continue;
			    		}
			        	
			        	Block block = world.getBlock(x+i-range, y+j-range, z+k-range);
			        	
			        	if(block == ModBlocks.blockSpectralContainer)
			        	{
				    		world.setBlockToAir(x+i-range, y+j-range, z+k-range);
			        	}
			        }
                }
            }
            
            data.currentEssence = currentEssence - this.getCostPerRefresh();
	        data.markDirty();
	        
	        ritualStone.setActive(false);
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
		ArrayList<RitualComponent> ellipsoidRitual = new ArrayList();
		
        ellipsoidRitual.add(new RitualComponent(-1,0,-1,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-1,0,1,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(1,0,-1,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(1,0,1,RitualComponent.DUSK));
        
        ellipsoidRitual.add(new RitualComponent(4,0,0,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(5,0,0,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(5,0,-1,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(5,0,-2,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-4,0,0,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-5,0,0,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-5,0,1,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-5,0,2,RitualComponent.DUSK));
        
        ellipsoidRitual.add(new RitualComponent(0,0,4,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(0,0,5,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(1,0,5,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(2,0,5,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(0,0,-4,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(0,0,-5,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-1,0,-5,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-2,0,-5,RitualComponent.DUSK));
        
        ellipsoidRitual.add(new RitualComponent(3,0,1,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(3,0,2,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(3,0,3,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(2,0,3,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-3,0,-1,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-3,0,-2,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-3,0,-3,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-2,0,-3,RitualComponent.DUSK));
        
        ellipsoidRitual.add(new RitualComponent(1,0,-3,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(2,0,-3,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(3,0,-3,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(3,0,-2,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-1,0,3,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-2,0,3,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-3,0,3,RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-3,0,2,RitualComponent.DUSK));
        
        return ellipsoidRitual;
	}
    
    public int getRadiusForModifierBlock(Block block)
	{
		if(block == null)
		{
			return 10;
		}
		
		if(block == Blocks.diamond_block)
		{
			return 30;
		}
		
		if(block == Blocks.gold_block)
		{
			return 20;
		}
		
		if(block == Blocks.iron_block)
		{
			return 15;
		}
		
		return 10;
	}
}
