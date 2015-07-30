package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectFullStomach extends RitualEffect
{

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        if (world.getWorldTime() % 20 != 0)
        {
            return;
        }

        double horizRange = 16;
        double vertRange = 16;

        List<EntityPlayer> playerList = SpellHelper.getPlayersInRange(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, horizRange, vertRange);

        if (playerList == null)
        {
            return;
        }

        if (currentEssence < this.getCostPerRefresh() * playerList.size())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            TileEntity tile = world.getTileEntity(pos.offsetUp());
            IInventory inventory = null;
            if (tile instanceof IInventory)
            {
                inventory = (IInventory) tile;
            } else
            {
                tile = world.getTileEntity(pos.offsetDown());
                if (tile instanceof IInventory)
                {
                    inventory = (IInventory) tile;
                }
            }

            int count = 0;

            if (inventory != null)
            {
                for (EntityPlayer player : playerList)
                {
                    FoodStats foodStats = player.getFoodStats();
                    float satLevel = foodStats.getSaturationLevel();

                    for (int i = 0; i < inventory.getSizeInventory(); i++)
                    {
                        ItemStack stack = inventory.getStackInSlot(i);

                        if (stack != null && stack.getItem() instanceof ItemFood)
                        {
                            ItemFood foodItem = (ItemFood) stack.getItem();

                            int regularHeal = foodItem.getHealAmount(stack);
                            float saturatedHeal = foodItem.getSaturationModifier(stack) * regularHeal * 2.0f;

                            if (saturatedHeal + satLevel <= 20)
                            {
                            	NBTTagCompound nbt = new NBTTagCompound();
                            	foodStats.writeNBT(nbt);
                            	nbt.setFloat("foodSaturationLevel", saturatedHeal + satLevel);
                            	foodStats.readNBT(nbt);

                                inventory.decrStackSize(i, 1);
                                count++;
                                break;
                            }
                        }
                    }
                }
            }

            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * count);
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 100;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> fullRitual = new ArrayList<RitualComponent>();
        fullRitual.add(new RitualComponent(0, 0, 3, RitualComponent.FIRE));
        fullRitual.add(new RitualComponent(0, 0, -3, RitualComponent.FIRE));
        fullRitual.add(new RitualComponent(3, 0, 0, RitualComponent.FIRE));
        fullRitual.add(new RitualComponent(-3, 0, 0, RitualComponent.FIRE));
        fullRitual.add(new RitualComponent(1, 0, 1, RitualComponent.AIR));
        fullRitual.add(new RitualComponent(1, 0, -1, RitualComponent.AIR));
        fullRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.AIR));
        fullRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.AIR));

        fullRitual.add(new RitualComponent(2, 0, 1, RitualComponent.AIR));
        fullRitual.add(new RitualComponent(2, 0, -1, RitualComponent.AIR));
        fullRitual.add(new RitualComponent(-2, 0, -1, RitualComponent.AIR));
        fullRitual.add(new RitualComponent(-2, 0, 1, RitualComponent.AIR));
        fullRitual.add(new RitualComponent(1, 0, 2, RitualComponent.AIR));
        fullRitual.add(new RitualComponent(1, 0, -2, RitualComponent.AIR));
        fullRitual.add(new RitualComponent(-1, 0, -2, RitualComponent.AIR));
        fullRitual.add(new RitualComponent(-1, 0, 2, RitualComponent.AIR));

        fullRitual.add(new RitualComponent(4, 0, 4, RitualComponent.WATER));
        fullRitual.add(new RitualComponent(4, 0, -4, RitualComponent.WATER));
        fullRitual.add(new RitualComponent(-4, 0, -4, RitualComponent.WATER));
        fullRitual.add(new RitualComponent(-4, 0, 4, RitualComponent.WATER));

        fullRitual.add(new RitualComponent(4, 0, 3, RitualComponent.EARTH));
        fullRitual.add(new RitualComponent(3, 0, 4, RitualComponent.EARTH));
        fullRitual.add(new RitualComponent(-4, 0, 3, RitualComponent.EARTH));
        fullRitual.add(new RitualComponent(3, 0, -4, RitualComponent.EARTH));
        fullRitual.add(new RitualComponent(-4, 0, -3, RitualComponent.EARTH));
        fullRitual.add(new RitualComponent(-3, 0, -4, RitualComponent.EARTH));
        fullRitual.add(new RitualComponent(4, 0, -3, RitualComponent.EARTH));
        fullRitual.add(new RitualComponent(-3, 0, 4, RitualComponent.EARTH));

        return fullRitual;
    }
}
