package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BloodShard extends Item implements ArmourUpgrade
{
    public BloodShard()
    {
        super();
        this.maxStackSize = 64;
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
    }

    @Override
    public boolean isUpgrade()
    {
        return false;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 0;
    }
}
