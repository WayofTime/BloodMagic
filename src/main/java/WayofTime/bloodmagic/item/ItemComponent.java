package WayofTime.bloodmagic.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;

public class ItemComponent extends Item {

    public static String[] names = {"reagentWater", "reagentLava", "reagentAir", "reagentFastMiner", "reagentVoid", "reagentGrowth", "reagentAffinity", "reagentSight"};

    public ItemComponent() {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".baseComponent.");
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item id, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(id, 1, i));
    }
}
