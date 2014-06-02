package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;

public class RitualEffectMagnetic extends RitualEffect
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

        if (world.getWorldTime() % 40 != 0)
        {
            return;
        }

        Block powerBlock = world.getBlock(x, y-1, z);
        int radius = this.getRadiusForModifierBlock(powerBlock);
        
        if (currentEssence < this.getCostPerRefresh())
        {
            EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            int xRep = 0;
            int yRep = 0;
            int zRep = 0;
            boolean replace = false;

            for (int j = 1; j <= 3; j++)
            {
                for (int i = -1; i <= 1; i++)
                {
                    for (int k = -1; k <= 1; k++)
                    {
                        if ((!replace) && world.isAirBlock(x + i, y + j, z + k))
                        {
                            xRep = x + i;
                            yRep = y + j;
                            zRep = z + k;
                            replace = true;
                        }
                    }
                }
            }

            if (replace)
            {
                //boolean hasReplaced = false;
                for (int j = y - 1; j >= 0; j--)
                {
                    for (int i = -radius; i <= radius; i++)
                    {
                        for (int k = -radius; k <= radius; k++)
                        {
                            Block block = world.getBlock(x + i, j, z + k);
                            int meta = world.getBlockMetadata(x + i, j, z + k);

                            if (block == null)
                            {
                                continue;
                            }

                            ItemStack itemStack = new ItemStack(block, 1, meta);
                            int id = OreDictionary.getOreID(itemStack);

                            if (id != -1)
                            {
                                String oreName = OreDictionary.getOreName(id);

                                if (oreName.contains("ore"))
                                {
                                    //TODO
                                    //Allow swapping code. This means the searched block is an ore.
                                    BlockTeleposer.swapBlocks(world, world, x + i, j, z + k, xRep, yRep, zRep);
                                    data.currentEssence = currentEssence - this.getCostPerRefresh();
                                    data.markDirty();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 50;
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
        magneticRitual.add(new RitualComponent(0, 2, -2, RitualComponent.FIRE));
        return magneticRitual;
	}
    
    public int getRadiusForModifierBlock(Block block)
	{
		if(block == null)
		{
			return 3;
		}
		
		if(block == Blocks.diamond_block)
		{
			return 31;
		}
		
		if(block == Blocks.gold_block)
		{
			return 15;
		}
		
		if(block == Blocks.iron_block)
		{
			return 7;
		}
		
		return 3;
	}
}
