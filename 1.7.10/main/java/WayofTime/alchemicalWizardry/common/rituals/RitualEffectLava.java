package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.block.BlockSpectralContainer;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectLava extends RitualEffect
{
	public static final int sanctusDrain = 20;
	public static final int offensaDrain = 50;
	public static final int reductusDrain = 5;
	
	public static final int fireFuseCost = 1000;
	
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

        Block block = world.getBlock(x, y + 1, z);
        
        if (world.isAirBlock(x, y + 1, z) && !(block instanceof BlockSpectralContainer))
        {
            if (currentEssence < this.getCostPerRefresh())
            {
            	SoulNetworkHandler.causeNauseaToPlayer(owner);
            } else
            {
                for (int i = 0; i < 10; i++)
                {
                    SpellHelper.sendIndexedParticleToAllAround(world, x, y, z, 20, world.provider.dimensionId, 3, x, y, z);
                }

                world.setBlock(x, y + 1, z, Blocks.lava, 0, 3);
                data.currentEssence = currentEssence - this.getCostPerRefresh();
                data.markDirty();
            }
        }else
        {
        	boolean hasSanctus = this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);
        	if(!hasSanctus)
        	{
        		return;
        	}
        	TileEntity tile = world.getTileEntity(x, y + 1, z);
        	if(tile instanceof IFluidHandler)
        	{
        		int amount = ((IFluidHandler) tile).fill(ForgeDirection.DOWN, new FluidStack(FluidRegistry.LAVA, 1000), false);
        		if(amount >= 1000)
        		{
        			((IFluidHandler) tile).fill(ForgeDirection.DOWN, new FluidStack(FluidRegistry.LAVA, 1000), true);
        			
        			this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, true);
        			
        			data.currentEssence = currentEssence - this.getCostPerRefresh();
                    data.markDirty();
        		}
        	}
        }
        
        
        
        if(this.canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, false) && SoulNetworkHandler.canSyphonFromOnlyNetwork(owner, fireFuseCost))
        {
        	boolean hasReductus = this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);
        	boolean drainReductus = world.getWorldTime() % 100 == 0;
        	
        	int range = 5;
            List<EntityLivingBase> entityList = SpellHelper.getLivingEntitiesInRange(world, x + 0.5, y + 0.5, z + 0.5, range, range);
            EntityPlayer player = SpellHelper.getPlayerForUsername(owner);
            
            for(EntityLivingBase entity : entityList)
            {
            	if(entity != player && this.canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, false) && SoulNetworkHandler.canSyphonFromOnlyNetwork(owner, fireFuseCost) && !entity.isPotionActive(AlchemicalWizardry.customPotionFireFuse))
            	{
            		if(hasReductus && this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false))
                	{
                		if(entity instanceof EntityPlayer)
                		{
                			if(drainReductus)
                			{
                    			this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, true);
                			}
                			
                			continue;
                		}
                	}
            		entity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionFireFuse.id,100,0));
            		this.canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, true);
            		SoulNetworkHandler.syphonFromNetwork(owner, fireFuseCost);
            	}
            }
        }
        
    }

    @Override
    public int getCostPerRefresh()
    {
        // TODO Auto-generated method stub
        return 500;
    }

    @Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> lavaRitual = new ArrayList();
        lavaRitual.add(new RitualComponent(1, 0, 0, 2));
        lavaRitual.add(new RitualComponent(-1, 0, 0, 2));
        lavaRitual.add(new RitualComponent(0, 0, 1, 2));
        lavaRitual.add(new RitualComponent(0, 0, -1, 2));
        return lavaRitual;
	}
}
