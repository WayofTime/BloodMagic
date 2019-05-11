package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.types.ISubItem;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ItemSlate extends ItemEnum.Variant<ItemSlate.SlateType> {

    public ItemSlate() {
        super(SlateType.class, "slate");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        list.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.slate.desc"))));
    }

    public enum SlateType implements ISubItem {

        BLANK,
        REINFORCED,
        IMBUED,
        DEMONIC,
        ETHEREAL,
        ;

        @Nonnull
        @Override
        public String getInternalName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Nonnull
        @Override
        public ItemStack getStack(int count) {
            return new ItemStack(RegistrarBloodMagicItems.SLATE, count, ordinal());
        }
    }
}
