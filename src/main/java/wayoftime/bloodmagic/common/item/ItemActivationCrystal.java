package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Locale;

// TODO: Implement IBindable interface and ritual activation functionality
public class ItemActivationCrystal extends Item {
    private final CrystalType type;

    public ItemActivationCrystal(CrystalType type) {
        super(new Item.Properties().stacksTo(1));
        this.type = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.bloodmagic.activationcrystal." + type.name().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Ritual activation not yet implemented").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    public int getCrystalLevel(ItemStack stack) {
        return this.type.equals(CrystalType.CREATIVE) ? Integer.MAX_VALUE : type.ordinal() + 1;
    }

    public CrystalType getCrystalType() {
        return type;
    }

    public enum CrystalType {
        WEAK,
        AWAKENED,
        CREATIVE;

        public static ItemStack getStack(int level) {
            if (level < 0) {
                level = 0;
            }
            return switch (level) {
                case 0 -> new ItemStack(BMItems.ACTIVATION_CRYSTAL_WEAK.get());
                case 1 -> new ItemStack(BMItems.ACTIVATION_CRYSTAL_AWAKENED.get());
                default -> new ItemStack(BMItems.ACTIVATION_CRYSTAL_CREATIVE.get());
            };
        }
    }
}
