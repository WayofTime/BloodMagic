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
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectEvaporation extends RitualEffect
{
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

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
        	IBlockState state1 = world.getBlockState(pos.offsetDown());
            Block block1 = state1.getBlock();
            int range = this.getRadiusForModifierBlock(block1);

            boolean[][][] boolList = new boolean[range * 2 + 1][range * 2 + 1][range * 2 + 1];

            for (int i = 0; i < 2 * range + 1; i++)
            {
                for (int j = 0; j < 2 * range + 1; j++)
                {
                    for (int k = 0; k < 2 * range + 1; k++)
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
                        for (int k = 0; k < 2 * range + 1; k++)
                        {
                            if (boolList[i][j][k])
                            {
                            	BlockPos position = pos.add(i - range, j - range, k - range);
                            	
                            	for(EnumFacing face : EnumFacing.VALUES)
                            	{
                            		int iP = i + face.getFrontOffsetX();
                            		int jP = j + face.getFrontOffsetY();
                            		int kP = k + face.getFrontOffsetZ();
                            		
                            		if(iP >= 0 && iP <= 2 * range && jP >= 0 && jP <= 2 * range && kP >= 0 && kP <= 2 * range && !boolList[iP][jP][kP])
                            		{
                                		BlockPos newPos = position.add(face.getDirectionVec());
                                		IBlockState state = world.getBlockState(newPos);
                                		Block block = state.getBlock();
                                		if (world.isAirBlock(newPos) || block == ModBlocks.blockSpectralContainer)
                                        {
                                            boolList[iP][jP][kP] = true;
                                            isReady = false;
                                        }
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
                    for (int k = 0; k < 2 * range + 1; k++)
                    {
                        if (!boolList[i][j][k])
                        {
                            continue;
                        }

                        BlockPos newPos = pos.add(i - range, j - range, k - range);
                        IBlockState state = world.getBlockState(newPos);
                        Block block = state.getBlock();

                        if (block == ModBlocks.blockSpectralContainer)
                        {
                            world.setBlockToAir(newPos);
                        }
                    }
                }
            }

            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());

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
        ArrayList<RitualComponent> ellipsoidRitual = new ArrayList<RitualComponent>();

        ellipsoidRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(1, 0, -1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(1, 0, 1, RitualComponent.DUSK));

        ellipsoidRitual.add(new RitualComponent(4, 0, 0, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(5, 0, 0, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(5, 0, -1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(5, 0, -2, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-4, 0, 0, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-5, 0, 1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-5, 0, 2, RitualComponent.DUSK));

        ellipsoidRitual.add(new RitualComponent(0, 0, 4, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(0, 0, 5, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(1, 0, 5, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(2, 0, 5, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(0, 0, -4, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(0, 0, -5, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-1, 0, -5, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-2, 0, -5, RitualComponent.DUSK));

        ellipsoidRitual.add(new RitualComponent(3, 0, 1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(3, 0, 2, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(3, 0, 3, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(2, 0, 3, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-3, 0, -2, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-2, 0, -3, RitualComponent.DUSK));

        ellipsoidRitual.add(new RitualComponent(1, 0, -3, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(2, 0, -3, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(3, 0, -3, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(3, 0, -2, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-2, 0, 3, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-3, 0, 2, RitualComponent.DUSK));

        return ellipsoidRitual;
    }

    public int getRadiusForModifierBlock(Block block)
    {
        if (block == null)
        {
            return 10;
        }

        if (block == Blocks.diamond_block)
        {
            return 30;
        }

        if (block == Blocks.gold_block)
        {
            return 20;
        }

        if (block == Blocks.iron_block)
        {
            return 15;
        }

        return 10;
    }
}
