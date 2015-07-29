package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectGrowth extends RitualEffect
{
    private static final int aquasalusDrain = 10;
    private static final int terraeDrain = 20;
    private static final int orbisTerraeDrain = 20;
    private static final int virtusDrain = 10;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        if (currentEssence < this.getCostPerRefresh() * 9)
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            boolean hasTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, false);
            boolean hasOrbisTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, orbisTerraeDrain, false);
            boolean hasVirtus = this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false);

            int speed = this.getSpeedForReagents(hasTerrae, hasOrbisTerrae);
            if (world.getWorldTime() % speed != 0)
            {
                return;
            }

            if (this.canDrainReagent(ritualStone, ReagentRegistry.aquasalusReagent, aquasalusDrain, false))
            {
                int hydrationRange = hasVirtus ? 4 : 1;
                for (int i = -hydrationRange; i <= hydrationRange; i++)
                {
                    for (int j = -hydrationRange; j <= hydrationRange; j++)
                    {
                        if (this.canDrainReagent(ritualStone, ReagentRegistry.aquasalusReagent, aquasalusDrain, false))
                        {
                        	BlockPos newPos = pos.add(i, 1, j);
                            if (SpellHelper.hydrateSoil(world, newPos))
                            {
                                this.canDrainReagent(ritualStone, ReagentRegistry.aquasalusReagent, aquasalusDrain, true);
                            }
                        }
                    }
                }
            }

            int flag = 0;

            int range = hasVirtus ? 4 : 1;
            for (int i = -range; i <= range; i++)
            {
                for (int j = -range; j <= range; j++)
                {
                	BlockPos newPos = pos.add(i, 2, j);
                	IBlockState state = world.getBlockState(newPos);
                    Block block = state.getBlock();

                    if (block instanceof IPlantable || block instanceof IGrowable)
                    {
                        {
                            SpellHelper.sendIndexedParticleToAllAround(world, pos, 20, world.provider.getDimensionId(), 3, pos);
                            block.updateTick(world, newPos, state, world.rand);
                            flag++;
                        }
                    }
                }
            }

            if (flag > 0)
            {
                if (hasTerrae)
                    this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, true);
                if (hasOrbisTerrae)
                    this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, orbisTerraeDrain, true);
                if (hasVirtus)
                    this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, true);

                SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * flag);
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 20;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> growthRitual = new ArrayList();
        growthRitual.add(new RitualComponent(1, 0, 0, 1));
        growthRitual.add(new RitualComponent(-1, 0, 0, 1));
        growthRitual.add(new RitualComponent(0, 0, 1, 1));
        growthRitual.add(new RitualComponent(0, 0, -1, 1));
        growthRitual.add(new RitualComponent(-1, 0, 1, 3));
        growthRitual.add(new RitualComponent(1, 0, 1, 3));
        growthRitual.add(new RitualComponent(-1, 0, -1, 3));
        growthRitual.add(new RitualComponent(1, 0, -1, 3));
        return growthRitual;
    }

    public int getSpeedForReagents(boolean hasTerrae, boolean hasOrbisTerrae)
    {
        if (hasOrbisTerrae)
        {
            if (hasTerrae)
            {
                return 10;
            } else
            {
                return 15;
            }
        } else
        {
            if (hasTerrae)
            {
                return 20;
            } else
            {
                return 30;
            }
        }
    }
}
