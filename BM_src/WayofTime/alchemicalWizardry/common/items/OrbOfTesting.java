package WayofTime.alchemicalWizardry.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class OrbOfTesting extends EnergyItems
{
    public OrbOfTesting(int id)
    {
        super(id);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabMisc);
        setUnlocalizedName("orbOfTesting");
        setMaxDamage(100);
        setFull3D();
        this.setEnergyUsed(100);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Untitled");
    }

    //Heals the player using the item. If the player is at full health, or if the durability cannot be used any more,
    //the item is not used.
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.shouldHeal())
        {
            return par1ItemStack;
        }

        if (this.syphonBatteries(par1ItemStack, par3EntityPlayer, this.getEnergyUsed()))
        {
            par3EntityPlayer.heal(1);
        }

        return par1ItemStack;
    }
}
