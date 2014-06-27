package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralContainer;

public class RitualEffectSupression extends RitualEffect
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
        
        Block blockish = world.getBlock(x, y-1, z);
        int costMod = this.getCostModifier(blockish);
        int radius = this.getRadiusForModifierBlock(blockish);
        int masterRadius = radius;
        
        int yIndex = (int)(world.getWorldTime() % (2*radius + 1))-radius;
        boolean expansion = false;
        
        if(ritualStone.getVar1()<(radius+1))
        {
        	expansion = true;
        	radius = ritualStone.getVar1();
        	ritualStone.setVar1(ritualStone.getVar1() + 1);
        }
        
        if (currentEssence < this.getCostPerRefresh()*costMod)
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            for (int i = -radius; i <= radius; i++)
            {
                for (int j = (expansion ? -radius : yIndex); j <= (expansion ? radius : yIndex); j++)
                {
                    for(int k = -radius; k <= radius; k++)
                    {
                    	if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
                        {
                            continue;
                        }
                    	
                    	Block block = world.getBlock(x+i, y+j, z+k);
                    	
                    	
                    	if(SpellHelper.isBlockFluid(block))
                    	{
                    		TESpectralContainer.createSpectralBlockAtLocation(world, x+i, y+j, z+k, 3*masterRadius);
                    	}
                    	else
                    	{
                    		TileEntity tile = world.getTileEntity(x+i, y+j, z+k);
                    		if(tile instanceof TESpectralContainer)
                    		{
                    			((TESpectralContainer) tile).resetDuration(3*masterRadius);
                    		}
                    	}		
                    }
                }
            }

            
            data.currentEssence = currentEssence - this.getCostPerRefresh()*costMod;
            data.markDirty();
            
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 2;
    }

	@Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> supressionRitual = new ArrayList();
        supressionRitual.add(new RitualComponent(2,0,2, RitualComponent.WATER));
        supressionRitual.add(new RitualComponent(2,0,-2, RitualComponent.WATER));
        supressionRitual.add(new RitualComponent(-2,0,2, RitualComponent.WATER));
        supressionRitual.add(new RitualComponent(-2,0,-2, RitualComponent.WATER));
        supressionRitual.add(new RitualComponent(-2,0,-1, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(-1,0,-2, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(-2,0,1, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(1,0,-2, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(2,0,1, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(1,0,2, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(2,0,-1, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(-1,0,2, RitualComponent.AIR));
        return supressionRitual;
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
	
	public int getCostModifier(Block block)
	{
		if(block == null)
		{
			return 1;
		}
		
		if(block == Blocks.diamond_block)
		{
			return 20;
		}
		
		if(block == Blocks.gold_block)
		{
			return 10;
		}
		
		if(block == Blocks.iron_block)
		{
			return 5;
		}
		
		return 1;
	}
}
