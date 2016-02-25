package WayofTime.bloodmagic.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;

public class ItemBloodShard extends Item
{
    public String[] names = { "weak", "demon" };

    public ItemBloodShard()
    {
        super();

        setCreativeTab(BloodMagic.tabBloodMagic);
        setUnlocalizedName(Constants.Mod.MODID + ".bloodShard.");
        setRegistryName(Constants.BloodMagicItem.BLOOD_SHARD.getRegName());
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }
}
