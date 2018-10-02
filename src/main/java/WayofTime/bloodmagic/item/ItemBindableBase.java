package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBindableBase extends Item implements IBindable {
    public ItemBindableBase() {
        super();

        setCreativeTab(BloodMagic.TAB_BM);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (stack.hasTagCompound()) {
            Binding binding = getBinding(stack);
            if (binding != null) {
                tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentOwner", binding.getOwnerName()));
                return;
            }
        }
        TextComponentTranslation tooltipUnbound = new TextComponentTranslation("tooltip.bloodmagic.unbound");
        tooltipUnbound.getStyle().setColor(TextFormatting.RED);
        tooltip.add(tooltipUnbound.getFormattedText());
    }
}
