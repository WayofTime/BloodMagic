package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;

public class AWBaseItems extends Item
{
    public AWBaseItems()
    {
        super();
        setMaxStackSize(64);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    public void registerIcons(IIconRegister iconRegister)
    {
        if (this.equals(ModItems.blankSlate))
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BlankSlate");
        } else if (this.equals(ModItems.reinforcedSlate))
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ReinforcedSlate");
        } else if (this.equals(ModItems.imbuedSlate))
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:InfusedSlate");
        } else if (this.equals(ModItems.demonicSlate))
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DemonSlate");
        }
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Infused stone inside of");
        par3List.add("a blood altar");
    }
}
