package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;

public class AWBaseItems extends Item
{
    public AWBaseItems()
    {
        super();
        setMaxStackSize(64);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.infusedstone.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.infusedstone.desc2"));
    }
}
