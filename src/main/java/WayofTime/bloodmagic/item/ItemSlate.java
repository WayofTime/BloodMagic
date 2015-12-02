package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class ItemSlate extends Item {

    public String[] names = { "blank", "reinforced", "imbued", "demonic" };

    public ItemSlate() {
        super();

        setCreativeTab(BloodMagic.tabBloodMagic);
        setUnlocalizedName(Constants.Mod.MODID + ".slate.");
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item id, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        list.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.BloodMagic.slate.desc"))));
    }
}
