package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class OrbOfTesting extends EnergyItems
{
    public OrbOfTesting()
    {
        super();
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabMisc);
        setUnlocalizedName("orbOfTesting");
        setMaxDamage(100);
        setFull3D();
        this.setEnergyUsed(100);
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.shouldHeal())
        {
            return par1ItemStack;
        }

        if (syphonBatteries(par1ItemStack, par3EntityPlayer, this.getEnergyUsed()))
        {
            par3EntityPlayer.heal(1);
        }

        return par1ItemStack;
    }
}
