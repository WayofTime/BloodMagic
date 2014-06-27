package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BloodShard extends Item implements ArmourUpgrade
{
    public BloodShard()
    {
        super();
        this.maxStackSize = 64;
        //setEnergyUsed(100);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        if (this.equals(ModItems.weakBloodShard))
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WeakBloodShard");
            return;
        }

        if (this.equals(ModItems.demonBloodShard))
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DemonBloodShard");
            return;
        }
    }

    public int getBloodShardLevel()
    {
    	if (this.equals(ModItems.weakBloodShard))
    	{
            return 1;
        } else if (this.equals(ModItems.demonBloodShard))
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
