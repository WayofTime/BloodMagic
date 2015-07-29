package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectBinding extends RitualEffect
{
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        if (currentEssence < this.getCostPerRefresh())
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            if (ritualStone.getVar1() == 0)
            {
                int d0 = 0;
                AxisAlignedBB axisalignedbb = new AxisAlignedBB(pos.offsetUp(), pos.add(1, 2, 1)).expand(d0, d0, d0);
                List list = world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
                Iterator iterator = list.iterator();
                EntityItem item;

                while (iterator.hasNext())
                {
                    item = (EntityItem) iterator.next();
                    ItemStack itemStack = item.getEntityItem();

                    if (itemStack == null)
                    {
                        continue;
                    }


                    if (BindingRegistry.isRequiredItemValid(itemStack))
                    {
                        ritualStone.setVar1(BindingRegistry.getIndexForItem(itemStack) + 1);
                        itemStack.stackSize--;
                        world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY() + 1, pos.getZ()));
                        ritualStone.setCooldown(ritualStone.getCooldown() - 1);
                        if(itemStack.stackSize <= 0)
                        {
                            item.setDead();
                        }
                        break;
                    }

                    if (world.rand.nextInt(10) == 0)
                    {
                        SpellHelper.sendIndexedParticleToAllAround(world, pos, 20, world.provider.getDimensionId(), 1, pos);
                    }
                }

                SoulNetworkHandler.syphonFromNetwork(owner, getCostPerRefresh());
            } else
            {
                ritualStone.setCooldown(ritualStone.getCooldown() - 1);

                if (world.rand.nextInt(20) == 0)
                {
                    int lightningPoint = world.rand.nextInt(8);

                    BlockPos newPos = getPositionForLightning(pos, lightningPoint);
                    world.addWeatherEffect(new EntityLightningBolt(world, newPos.getX(), newPos.getY(), newPos.getZ()));
                }

                if (ritualStone.getCooldown() <= 0)
                {

                    ItemStack spawnedItem = BindingRegistry.getOutputForIndex(ritualStone.getVar1() - 1);

                    if (spawnedItem != null)
                    {
                        EntityItem newItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, spawnedItem.copy());
                        world.spawnEntityInWorld(newItem);
                    }

                    ritualStone.setActive(false);
                }
            }
        }
    }

    public BlockPos getPositionForLightning(BlockPos pos, int lightning)
    {
    	switch (lightning)
        {
            case 0:
            	return pos.add(4, 3, 0);

            case 1:
            	return pos.add(-4, 3, 0);

            case 2:
            	return pos.add(0, 3, 4);

            case 3:
            	return pos.add(0, 3, -4);

            case 4:
            	return pos.add(3, 3, 3);

            case 5:
            	return pos.add(-3, 3, 3);

            case 6:
            	return pos.add(-3, 3, -3);

            case 7:
            	return pos.add(3, 3, -3);
            	
            default:
            	return pos.add(0, 3, 0);
        }
    }
    
    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

    @Override
    public int getInitialCooldown()
    {
        return 200;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> boundSoulRitual = new ArrayList();
        boundSoulRitual.add(new RitualComponent(3, 0, 0, 2));
        boundSoulRitual.add(new RitualComponent(-3, 0, 0, 2));
        boundSoulRitual.add(new RitualComponent(0, 0, 3, 2));
        boundSoulRitual.add(new RitualComponent(0, 0, -3, 2));
        boundSoulRitual.add(new RitualComponent(2, 0, 2, 4));
        boundSoulRitual.add(new RitualComponent(-2, 0, 2, 4));
        boundSoulRitual.add(new RitualComponent(2, 0, -2, 4));
        boundSoulRitual.add(new RitualComponent(-2, 0, -2, 4));
        boundSoulRitual.add(new RitualComponent(4, 2, 0, 1));
        boundSoulRitual.add(new RitualComponent(-4, 2, 0, 1));
        boundSoulRitual.add(new RitualComponent(0, 2, 4, 1));
        boundSoulRitual.add(new RitualComponent(0, 2, -4, 1));
        boundSoulRitual.add(new RitualComponent(3, 2, 3, 3));
        boundSoulRitual.add(new RitualComponent(3, 2, -3, 3));
        boundSoulRitual.add(new RitualComponent(-3, 2, 3, 3));
        boundSoulRitual.add(new RitualComponent(-3, 2, -3, 3));
        boundSoulRitual.add(new RitualComponent(4, 1, 0, 0));
        boundSoulRitual.add(new RitualComponent(-4, 1, 0, 0));
        boundSoulRitual.add(new RitualComponent(0, 1, 4, 0));
        boundSoulRitual.add(new RitualComponent(0, 1, -4, 0));
        boundSoulRitual.add(new RitualComponent(3, 1, 3, 0));
        boundSoulRitual.add(new RitualComponent(3, 1, -3, 0));
        boundSoulRitual.add(new RitualComponent(-3, 1, 3, 0));
        boundSoulRitual.add(new RitualComponent(-3, 1, -3, 0));
        return boundSoulRitual;
    }
}
