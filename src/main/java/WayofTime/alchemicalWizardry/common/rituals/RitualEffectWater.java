package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.block.BlockSpectralContainer;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class RitualEffectWater extends RitualEffect
{
    public static final int aquasalusDrain = 5;
    public static final int offensaDrain = 20;
    public static final int sanctusDrain = 5;
    public static final int reductusDrain = 2;
    public static final int crystallosDrain = 10;

    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        boolean hasCrystallos = this.canDrainReagent(ritualStone, ReagentRegistry.crystallosReagent, crystallosDrain, false);
        boolean hasAquasalus = this.canDrainReagent(ritualStone, ReagentRegistry.aquasalusReagent, aquasalusDrain, false);
        boolean hasOffensa = this.canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, false);

        if (hasAquasalus)
        {
            int hydrationRange = 4;
            int vertRange = 3;

            for (int i = -hydrationRange; i <= hydrationRange; i++)
            {
                for (int j = -vertRange; j <= vertRange; j++)
                {
                    for (int k = -hydrationRange; k <= hydrationRange; k++)
                    {
                        if (SpellHelper.hydrateSoil(world, x + i, y + j, z + k))
                        {
                            this.canDrainReagent(ritualStone, ReagentRegistry.aquasalusReagent, aquasalusDrain, true);
                        }
                    }
                }
            }
        }

        if (hasOffensa)
        {
            boolean hasReductus = this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);
            boolean drainReductus = world.getWorldTime() % 100 == 0;

            int range = 10;
            List<Entity> list = SpellHelper.getEntitiesInRange(world, x + 0.5, y + 0.5, z + 0.5, range, range);
            for (Entity entity : list)
            {
                if (entity instanceof EntityLivingBase)
                {
                    EntityLivingBase livingEntity = (EntityLivingBase) entity;

                    if (livingEntity == SpellHelper.getPlayerForUsername(owner))
                    {
                        continue;
                    }

                    if (hasReductus && this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false))
                    {
                        if (livingEntity instanceof EntityPlayer)
                        {
                            if (drainReductus)
                            {
                                this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, true);
                            }

                            continue;
                        }
                    }

                    if (!livingEntity.isPotionActive(AlchemicalWizardry.customPotionDrowning))
                    {
                        livingEntity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionDrowning.id, 100, 0));
                        this.canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, true);
                    }
                }
            }
        }

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

                world.setBlock(x, y + 1, z, Blocks.water, 0, 3);
                SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
            }
        } else
        {
            boolean hasSanctus = this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);
            if (!hasSanctus)
            {
                return;
            }
            TileEntity tile = world.getTileEntity(x, y + 1, z);
            if (tile instanceof IFluidHandler)
            {
                int amount = ((IFluidHandler) tile).fill(ForgeDirection.DOWN, new FluidStack(FluidRegistry.WATER, 1000), false);
                if (amount >= 1000)
                {
                    ((IFluidHandler) tile).fill(ForgeDirection.DOWN, new FluidStack(FluidRegistry.WATER, 1000), true);

                    this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, true);

                    SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
                }
            }
        }

        if (hasCrystallos)
        {
            int range = 2;
            for (int i = -range; i <= range; i++)
            {
                for (int j = -range; j <= range; j++)
                {
                    for (int k = -range; k <= range; k++)
                    {
                        hasCrystallos = hasCrystallos && this.canDrainReagent(ritualStone, ReagentRegistry.crystallosReagent, crystallosDrain, false);

                        if (hasCrystallos)
                        {
                            boolean success = false;
                            if (!world.isAirBlock(x + i, y + j, z + k) && SpellHelper.freezeWaterBlock(world, x + i, y + j, z + k))
                            {
                                success = true;
                            } else
                            {
                                if (world.rand.nextInt(100) == 0 && world.isSideSolid(x + i, y + j - 1, z + k, ForgeDirection.UP))
                                {
                                    success = true;
                                }
                            }

                            if (success)
                            {
                                this.canDrainReagent(ritualStone, ReagentRegistry.crystallosReagent, crystallosDrain, true);
                            }
                        }
                    }
                }
            }
        }
    }

    public int getCostPerRefresh()
    {
        return AlchemicalWizardry.ritualCostWater[1];
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> waterRitual = new ArrayList();
        waterRitual.add(new RitualComponent(-1, 0, 1, 1));
        waterRitual.add(new RitualComponent(-1, 0, -1, 1));
        waterRitual.add(new RitualComponent(1, 0, -1, 1));
        waterRitual.add(new RitualComponent(1, 0, 1, 1));
        return waterRitual;
    }
}
