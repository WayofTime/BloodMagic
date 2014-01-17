package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ModItems;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class AWBaseItems extends Item {
    public AWBaseItems(int id)
    {
        super(id);
        setMaxStackSize(64);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    public void registerIcons(IconRegister iconRegister)
    {
        if (this.itemID == ModItems.blankSlate.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BlankSlate");
        } else if (this.itemID == ModItems.reinforcedSlate.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ReinforcedSlate");
        } else if (this.itemID == ModItems.imbuedSlate.itemID)
        {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:InfusedSlate");
        } else if (this.itemID == ModItems.demonicSlate.itemID)
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
