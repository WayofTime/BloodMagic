package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class RitualEffectMagnetic extends RitualEffect
{
    private static final int potentiaDrain = 10;
    private static final int terraeDrain = 10;
    private static final int orbisTerraeDrain = 10;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        boolean hasPotentia = this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);

        if (world.getWorldTime() % (hasPotentia ? 10 : 40) != 0)
        {
            return;
        }

        boolean hasTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, false);
        boolean hasOrbisTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, orbisTerraeDrain, false);

        int radius = this.getRadiusForReagents(hasTerrae, hasOrbisTerrae);

        if (currentEssence < this.getCostPerRefresh())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
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
                                    //Allow swapping code. This means the searched block is an ore.
                                    BlockTeleposer.swapBlocks(this, world, world, x + i, j, z + k, xRep, yRep, zRep);
                                    SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());

                                    if (hasPotentia)
                                    {
                                        this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, true);
                                    }

                                    if (hasTerrae)
                                    {
                                        this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, true);
                                    }

                                    if (hasOrbisTerrae)
                                    {
                                        this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, orbisTerraeDrain, true);
                                    }

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

    public int getRadiusForReagents(boolean hasTerrae, boolean hasOrbisTerrae)
    {
        if (hasTerrae)
        {
            if (hasOrbisTerrae)
            {
                return 31;
            } else
            {
                return 7;
            }
        } else
        {
            if (hasOrbisTerrae)
            {
                return 12;
            } else
            {
                return 3;
            }
        }
    }
}
