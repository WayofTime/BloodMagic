package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemActivationCrystal extends ItemBindable {

    public static String[] names = {"weak", "awakened", "creative"};

    public ItemActivationCrystal() {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".activationCrystal.");
        setHasSubtypes(true);
        setEnergyUsed(100);
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

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.activationCrystal." + names[stack.getItemDamage()]));

        super.addInformation(stack, player, tooltip, advanced);

//        if (stack.getItemDamage() == 1) {
//            if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
//                ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(stack);
//
//                if (recipe != null) {
//                    tooltip.add(TextHelper.getFormattedText(StatCollector.translateToLocal("tooltip.alchemy.recipe")));
//
//                    for (ItemStack item : recipe)
//                        if (item != null)
//                            tooltip.add(item.getDisplayName());
//                }
//            } else {
//                tooltip.add(TextHelper.getFormattedText(StatCollector.translateToLocal("tooltip.alchemy.pressShift")));
//            }
//        }
    }

    public int getCrystalLevel(ItemStack stack) {
        return stack.getItemDamage() > 1 ? Integer.MAX_VALUE : stack.getItemDamage() + 1;
    }
}
