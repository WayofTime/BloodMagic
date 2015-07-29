package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
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

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        if (this.canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, false) && SoulNetworkHandler.canSyphonFromOnlyNetwork(owner, fireFuseCost))
        {
            boolean hasReductus = this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);
            boolean drainReductus = world.getWorldTime() % 100 == 0;

            int range = 5;
            List<EntityLivingBase> entityList = SpellHelper.getLivingEntitiesInRange(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, range, range);
            EntityPlayer player = SpellHelper.getPlayerForUsername(owner);

            for (EntityLivingBase entity : entityList)
            {
                if (entity != player && this.canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, false) && SoulNetworkHandler.canSyphonFromOnlyNetwork(owner, fireFuseCost) && !entity.isPotionActive(AlchemicalWizardry.customPotionFireFuse))
                {
                    if (hasReductus && this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false))
                    {
                        if (entity instanceof EntityPlayer)
                        {
                            if (drainReductus)
                            {
                                this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, true);
                            }

                            continue;
                        }
                    }

                    entity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionFireFuse.id, 100, 0));
                    this.canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, true);
                    SoulNetworkHandler.syphonFromNetwork(owner, fireFuseCost);
                }
            }
        }

        BlockPos newPos = pos.offsetUp();
        IBlockState state = world.getBlockState(newPos);
        Block block = state.getBlock();

        if (world.isAirBlock(newPos) && !(block instanceof BlockSpectralContainer))
        {
            if (currentEssence < this.getCostPerRefresh())
            {
                SoulNetworkHandler.causeNauseaToPlayer(owner);
            } else
            {
                for (int i = 0; i < 10; i++)
                {
                    SpellHelper.sendIndexedParticleToAllAround(world, pos, 20, world.provider.getDimensionId(), 3, pos);
                }

                world.setBlockState(newPos, Blocks.lava.getDefaultState());
                SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
            }
        } else
        {
            boolean hasSanctus = this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);
            if (!hasSanctus)
            {
                return;
            }
            TileEntity tile = world.getTileEntity(newPos);
            if (tile instanceof IFluidHandler)
            {
                int amount = ((IFluidHandler) tile).fill(EnumFacing.DOWN, new FluidStack(FluidRegistry.LAVA, 1000), false);
                if (amount >= 1000)
                {
                    ((IFluidHandler) tile).fill(EnumFacing.DOWN, new FluidStack(FluidRegistry.LAVA, 1000), true);

                    this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, true);

                    SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
                }
            }
        }


    }

    @Override
    public int getCostPerRefresh()
    {
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
