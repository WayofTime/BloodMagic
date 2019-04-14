package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.types.ISubItem;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemActivationCrystal extends ItemEnum.Variant<ItemActivationCrystal.CrystalType> implements IBindable {

    public ItemActivationCrystal() {
        super(CrystalType.class, "activation_crystal");

        setMaxStackSize(1);
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextHelper.localize("tooltip.bloodmagic.activation_crystal." + getItemType(stack).getInternalName()));

        if (!stack.hasTagCompound())
            return;

        Binding binding = getBinding(stack);
        if (binding != null)
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentOwner", binding.getOwnerName()));

        super.addInformation(stack, world, tooltip, flag);
    }

    public int getCrystalLevel(ItemStack stack) {
        return stack.getItemDamage() > 1 ? Integer.MAX_VALUE : stack.getItemDamage() + 1;
    }

    public enum CrystalType implements ISubItem {
        WEAK,
        AWAKENED,
        CREATIVE,
        ;

        @Nonnull
        @Override
        public String getInternalName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Nonnull
        @Override
        public ItemStack getStack(int count) {
            return new ItemStack(RegistrarBloodMagicItems.ACTIVATION_CRYSTAL, count, ordinal());
        }
    }
}
