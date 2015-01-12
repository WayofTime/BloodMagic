package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectItemSuction extends RitualEffect
{
    public static final int reductusDrain = 1;

    public static final int timeDelayMin = 60;

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
        IInventory tileEntity;

        if (tile instanceof IInventory)
        {
            tileEntity = (IInventory) tile;
        } else
        {
            return;
        }

        if (tileEntity.getSizeInventory() <= 0)
        {
            return;
        }

        if (currentEssence < this.getCostPerRefresh() * 100)
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            List<EntityItem> itemDropList = SpellHelper.getItemsInRange(world, x + 0.5f, y + 0.5f, z + 0.5f, 10, 10);

            boolean hasReductus = this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);

            int count = 0;

            if (itemDropList != null)
            {
                int invSize = tileEntity.getSizeInventory();

                for (EntityItem itemEntity : itemDropList)
                {
                    hasReductus = hasReductus && this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);
                    if (hasReductus && itemEntity.age < this.timeDelayMin)
                    {
                        continue;
                    }
                    ItemStack item = itemEntity.getEntityItem();
                    ItemStack copyStack = itemEntity.getEntityItem().copy();

                    int pastAmount = copyStack.stackSize;
                    ItemStack newStack = SpellHelper.insertStackIntoInventory(copyStack, tileEntity, ForgeDirection.DOWN);

                    if (newStack != null && newStack.stackSize < pastAmount)
                    {
                        count++;
                        if (newStack.stackSize <= 0)
                        {
                            itemEntity.setDead();
                            itemEntity.getEntityItem().stackSize = newStack.stackSize;
                        }

                        if (newStack.stackSize > 0)
                        {
                            itemEntity.getEntityItem().stackSize = newStack.stackSize;
                        }
                        if (hasReductus)
                        {
                            this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, true);
                        }
                    }
                }
            }

            if (count > 0)
            {
                SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * Math.min(count, 100));
                return;
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 5;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> suctionRitual = new ArrayList();
        suctionRitual.add(new RitualComponent(2, 0, 0, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(0, 0, 2, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(0, 0, -2, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(1, 1, 1, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(1, 1, -1, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(-1, 1, 1, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(-1, 1, -1, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(1, -1, 0, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(-1, -1, 0, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(0, -1, 1, RitualComponent.AIR));
        suctionRitual.add(new RitualComponent(0, -1, -1, RitualComponent.AIR));
        return suctionRitual;
    }
}
