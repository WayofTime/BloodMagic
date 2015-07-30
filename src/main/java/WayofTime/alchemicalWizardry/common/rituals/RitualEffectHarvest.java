package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.harvest.HarvestRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectHarvest extends RitualEffect
{
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();
        
        int maxCount = 100;

        if (currentEssence < this.getCostPerRefresh() * maxCount)
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            if (world.getWorldTime() % 5 != 0)
            {
                return;
            }

            IBlockState state = world.getBlockState(pos.offsetDown());
            Block block = state.getBlock();
            int flag = 0;
            int range = this.getRadiusForModifierBlock(block);
            int vertRange = 4;

            for (int i = -range; i <= range; i++)
            {
                for (int j = -vertRange; j <= vertRange; j++)
                {
                    for (int k = -range; k <= range; k++)
                    {
                        if (HarvestRegistry.harvestBlock(world, pos.add(i, j, k)) && flag < maxCount)
                        {
                            flag++;
                        }
                    }
                }
            }

            if (flag > 0)
            {
                SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * Math.min(maxCount, flag));
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 20;
    }

    public int getRadiusForModifierBlock(Block block)
    {
        if (block == null)
        {
            return 4;
        }

        if (block == Blocks.diamond_block)
        {
            return 15;
        }

        if (block == Blocks.gold_block)
        {
            return 10;
        }

        if (block == Blocks.iron_block)
        {
            return 6;
        }

        return 4;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> harvestRitual = new ArrayList();

        harvestRitual.add(new RitualComponent(1, 0, 1, RitualComponent.DUSK));
        harvestRitual.add(new RitualComponent(1, 0, -1, RitualComponent.DUSK));
        harvestRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.DUSK));
        harvestRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.DUSK));
        harvestRitual.add(new RitualComponent(2, 0, 0, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(0, 0, 2, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(0, 0, -2, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(3, 0, 1, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(3, 0, -1, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(1, 0, 3, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(1, 0, -3, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.EARTH));
        harvestRitual.add(new RitualComponent(2, 0, 3, RitualComponent.WATER));
        harvestRitual.add(new RitualComponent(3, 0, 2, RitualComponent.WATER));
        harvestRitual.add(new RitualComponent(2, 0, -3, RitualComponent.WATER));
        harvestRitual.add(new RitualComponent(-3, 0, 2, RitualComponent.WATER));
        harvestRitual.add(new RitualComponent(-2, 0, 3, RitualComponent.WATER));
        harvestRitual.add(new RitualComponent(3, 0, -2, RitualComponent.WATER));
        harvestRitual.add(new RitualComponent(-2, 0, -3, RitualComponent.WATER));
        harvestRitual.add(new RitualComponent(-3, 0, -2, RitualComponent.WATER));


        return harvestRitual;
    }
}
