package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;

public class ArmourInhibitor extends BindableItems
{
    public ArmourInhibitor()
    {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(0);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add(StatCollector.translateToLocal("tooltip.armorinhibitor.desc1"));
        list.add(StatCollector.translateToLocal("tooltip.armorinhibitor.desc2"));

        if (!(stack.getTagCompound() == null))
        {
            if (stack.getTagCompound().getBoolean("isActive"))
            {
                list.add(StatCollector.translateToLocal("tooltip.sigil.state.activated"));
            } else
            {
                list.add(StatCollector.translateToLocal("tooltip.sigil.state.deactivated"));
            }

            list.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        int tickDelay = 200;

        if (!BindableItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return stack;
        }

        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        tag.setBoolean("isActive", !(tag.getBoolean("isActive")));

        if (tag.getBoolean("isActive"))
        {
            stack.setItemDamage(1);
            tag.setInteger("worldTimeDelay", (int) (world.getWorldTime() - 1) % tickDelay);
        } else
        {
            stack.setItemDamage(stack.getMaxDamage());
        }

        return stack;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
    {
        if (!(entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;

        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        if (stack.getTagCompound().getBoolean("isActive"))
        {
//            if (world.getWorldTime() % tickDelay == stack.getTagCompound().getInteger("worldTimeDelay"))
            {
            }

            //TODO Do stuff
            player.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 2, 0, true, false));
        }
    }
}
