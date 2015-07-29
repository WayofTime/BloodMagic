package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralContainer;

public class RitualEffectSupression extends RitualEffect
{
    public static final int aquasalusDrain = 15;
    public static final int aetherDrain = 15;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        IBlockState stateish = world.getBlockState(pos.offsetDown());
        Block blockish = stateish.getBlock();

        boolean hasAquasalus = this.canDrainReagent(ritualStone, ReagentRegistry.aquasalusReagent, aquasalusDrain, false);
        boolean hasAether = this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, false);

        int costMod = this.getCostModifier(blockish);
        int radius = this.getRadiusForReagents(hasAether, hasAquasalus);
        int masterRadius = radius;

        int yIndex = (int) (world.getWorldTime() % (2 * radius + 1)) - radius;
        boolean expansion = false;

        if (ritualStone.getVar1() < (radius + 1))
        {
            expansion = true;
            radius = ritualStone.getVar1();
            ritualStone.setVar1(ritualStone.getVar1() + 1);
        }

        if (currentEssence < this.getCostPerRefresh() * costMod)
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
                    for (int k = -radius; k <= radius; k++)
                    {
                        if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
                        {
                            continue;
                        }
                        
                        BlockPos newPos = pos.add(i, j, k);
                        IBlockState state = world.getBlockState(newPos);
                        
                        Block block = state.getBlock();


                        if (SpellHelper.isBlockFluid(block))
                        {
                            TESpectralContainer.createSpectralBlockAtLocation(world, newPos, 3 * masterRadius);
                        } else
                        {
                            TileEntity tile = world.getTileEntity(newPos);
                            if (tile instanceof TESpectralContainer)
                            {
                                ((TESpectralContainer) tile).resetDuration(3 * masterRadius);
                            }
                        }
                    }
                }
            }


            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * costMod);

            if (world.getWorldTime() % 100 == 0)
            {
                if (hasAquasalus)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.aquasalusReagent, aquasalusDrain, true);
                }
                if (hasAether)
                {
                    this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, true);
                }
            }
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
        supressionRitual.add(new RitualComponent(2, 0, 2, RitualComponent.WATER));
        supressionRitual.add(new RitualComponent(2, 0, -2, RitualComponent.WATER));
        supressionRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.WATER));
        supressionRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.WATER));
        supressionRitual.add(new RitualComponent(-2, 0, -1, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(-1, 0, -2, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(-2, 0, 1, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(1, 0, -2, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(2, 0, 1, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(1, 0, 2, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(2, 0, -1, RitualComponent.AIR));
        supressionRitual.add(new RitualComponent(-1, 0, 2, RitualComponent.AIR));
        return supressionRitual;
    }

    public int getCostModifier(Block block)
    {
        return 1;
    }

    public int getRadiusForReagents(boolean hasAether, boolean hasAquasalus)
    {
        if (hasAether)
        {
            if (hasAquasalus)
            {
                return 30;
            } else
            {
                return 20;
            }
        } else
        {
            if (hasAquasalus)
            {
                return 15;
            } else
            {
                return 10;
            }
        }
    }
}
