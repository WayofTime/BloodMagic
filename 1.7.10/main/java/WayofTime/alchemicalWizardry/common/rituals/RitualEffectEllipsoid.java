package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualEffectEllipsoid extends RitualEffect
{
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        TileEntity tile = world.getTileEntity(x, y + 1, z);

        if (!(tile instanceof IInventory) || ((IInventory) tile).getSizeInventory() < 3)
        {
            return;
        }

        ItemStack item1 = ((IInventory) tile).getStackInSlot(0);
        ItemStack item2 = ((IInventory) tile).getStackInSlot(1);
        ItemStack item3 = ((IInventory) tile).getStackInSlot(2);

        int xSize = item1 == null ? 0 : item1.stackSize;
        int ySize = item2 == null ? 0 : item2.stackSize;
        int zSize = item3 == null ? 0 : item3.stackSize;

        int cost = (int) Math.pow((xSize + 1) * (ySize + 1) * (zSize + 1), 0.333);

        if (currentEssence < cost)
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            int refresh = 1000;
            int j = (int) (world.getWorldTime() % (ySize * 2 + 1)) - ySize;
            for (int i = -xSize; i <= xSize; i++)
            {
                {
                    for (int k = -zSize; k <= zSize; k++)
                    {
                        if (Math.pow(i * (ySize - 0.50f) * (zSize - 0.50f), 2) + Math.pow(j * (xSize - 0.50f) * (zSize - 0.50f), 2) + Math.pow(k * (xSize - 0.50f) * (ySize - 0.50f), 2) <= Math.pow((xSize - 1 + 0.50f) * (ySize - 1 + 0.50f) * (zSize - 1 + 0.50f), 2))
                        {
                            continue;
                        }

                        if (Math.pow(i * (ySize + 0.50f) * (zSize + 0.50f), 2) + Math.pow(j * (xSize + 0.50f) * (zSize + 0.50f), 2) + Math.pow(k * (xSize + 0.50f) * (ySize + 0.50f), 2) >= Math.pow((xSize + 0.50f) * (ySize + 0.50f) * (zSize + 0.50f), 2))
                        {
                            continue;
                        }

                        Block block = world.getBlock(x + i, y + j, z + k);

                        if (block.isAir(world, x + i, y + j, z + k))
                        {
                            TESpectralBlock.createSpectralBlockAtLocation(world, x + i, y + j, z + k, refresh);
                        } else
                        {
                            TileEntity tile1 = world.getTileEntity(x + i, y + j, z + k);
                            if (tile instanceof TESpectralBlock)
                            {
                                ((TESpectralBlock) tile1).resetDuration(refresh);
                            }
                        }
                    }
                }
            }


            SoulNetworkHandler.syphonFromNetwork(owner, cost);
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
        ArrayList<RitualComponent> ellipsoidRitual = new ArrayList();

        ellipsoidRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(1, 0, -1, RitualComponent.DUSK));
        ellipsoidRitual.add(new RitualComponent(1, 0, 1, RitualComponent.DUSK));

        ellipsoidRitual.add(new RitualComponent(4, 0, 0, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(5, 0, 0, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(5, 0, -1, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(5, 0, -2, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(-4, 0, 0, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(-5, 0, 1, RitualComponent.FIRE));
        ellipsoidRitual.add(new RitualComponent(-5, 0, 2, RitualComponent.FIRE));

        ellipsoidRitual.add(new RitualComponent(0, 0, 4, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(0, 0, 5, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(1, 0, 5, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(2, 0, 5, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(0, 0, -4, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(0, 0, -5, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(-1, 0, -5, RitualComponent.AIR));
        ellipsoidRitual.add(new RitualComponent(-2, 0, -5, RitualComponent.AIR));

        ellipsoidRitual.add(new RitualComponent(3, 0, 1, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(3, 0, 2, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(3, 0, 3, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(2, 0, 3, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(-3, 0, -2, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.EARTH));
        ellipsoidRitual.add(new RitualComponent(-2, 0, -3, RitualComponent.EARTH));

        ellipsoidRitual.add(new RitualComponent(1, 0, -3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(2, 0, -3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(3, 0, -3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(3, 0, -2, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(-2, 0, 3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.WATER));
        ellipsoidRitual.add(new RitualComponent(-3, 0, 2, RitualComponent.WATER));

        return ellipsoidRitual;
    }
}
