package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BloodShard extends Item implements ArmourUpgrade
{
    public BloodShard(int par1)
    {
        super(par1);
        this.maxStackSize = 64;
        //setEnergyUsed(100);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        if (this.itemID == ModItems.weakBloodShard.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WeakBloodShard");
            return;
        }

        if (this.itemID == ModItems.demonBloodShard.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DemonBloodShard");
            return;
        }
    }

    public int getBloodShardLevel()
    {
        if (this.itemID == ModItems.weakBloodShard.itemID)
        {
            return 1;
        } else if (this.itemID == ModItems.demonBloodShard.itemID)
        {
            return 2;
        }

        return 0;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isUpgrade()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
