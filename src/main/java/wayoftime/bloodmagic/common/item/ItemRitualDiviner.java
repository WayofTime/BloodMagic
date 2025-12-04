package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.ritual.EnumRuneType;

import java.util.List;

// TODO: Implement full ritual diviner functionality:
// - Ritual selection and cycling
// - Ritual building automation
// - Direction cycling
// - Ritual hologram display
public class ItemRitualDiviner extends Item {
    private final int type;

    public ItemRitualDiviner(int type) {
        super(new Item.Properties().stacksTo(1));
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!world.isClientSide) {
            player.displayClientMessage(Component.literal("Ritual diviner not yet implemented").withStyle(ChatFormatting.RED), true);
        }

        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.bloodmagic.diviner.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Ritual building not yet implemented").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    public boolean canPlaceRitualStone(EnumRuneType rune, ItemStack stack) {
        return switch (rune) {
            case BLANK, AIR, EARTH, FIRE, WATER -> true;
            case DUSK -> type >= 1;
            case DAWN -> type >= 2;
        };
    }

    public int getDivinerType() {
        return type;
    }
}
