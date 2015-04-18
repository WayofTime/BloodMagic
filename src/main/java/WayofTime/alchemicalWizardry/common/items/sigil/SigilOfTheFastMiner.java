package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class SigilOfTheFastMiner extends EnergyItems implements ArmourUpgrade
{
    @SideOnly(Side.CLIENT)
    private static IIcon activeIcon;
    @SideOnly(Side.CLIENT)
    private static IIcon passiveIcon;

    public SigilOfTheFastMiner()
    {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(100);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofthefastminer.desc"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            if (par1ItemStack.getTagCompound().getBoolean("isActive"))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.activated"));
            } else
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.deactivated"));
            }

            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:MiningSigil_deactivated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:MiningSigil_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:MiningSigil_deactivated");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        if (tag.getBoolean("isActive"))
        {
            return this.activeIcon;
        } else
        {
            return this.passiveIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        if (par1 == 1)
        {
            return this.activeIcon;
        } else
        {
            return this.passiveIcon;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = par1ItemStack.getTagCompound();
        tag.setBoolean("isActive", !(tag.getBoolean("isActive")));

        if (tag.getBoolean("isActive") && EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
        {
            par1ItemStack.setItemDamage(1);
            tag.setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % 200);
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 2, 1, true));
        } else
        {
            par1ItemStack.setItemDamage(par1ItemStack.getMaxDamage());
        }

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!(par3Entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (par1ItemStack.getTagCompound().getBoolean("isActive"))
        {
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 2, 1, true));
        }

        if (par2World.getWorldTime() % 200 == par1ItemStack.getTagCompound().getInteger("worldTimeDelay") && par1ItemStack.getTagCompound().getBoolean("isActive"))
        {
            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if (!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                {
                	par1ItemStack.getTagCompound().setBoolean("isActive", false);
                }
            }
        }

        return;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 3, 1, true));
    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 50;
    }
}
