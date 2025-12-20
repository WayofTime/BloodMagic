package wayoftime.bloodmagic.common.sigil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import wayoftime.bloodmagic.api.sigil.SigilEffect;
import wayoftime.bloodmagic.common.blockentity.BloodAltarTile;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.Binding;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.helper.SoulNetworkHelper;

import java.util.ArrayList;
import java.util.List;

public record DivinationEffect(boolean isAdvanced) implements SigilEffect {
    public static final MapCodec<DivinationEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.BOOL.fieldOf("is_advanced").forGetter(DivinationEffect::isAdvanced)
    ).apply(builder, DivinationEffect::new));

    @Override
    public int useOnAir(ItemStack sigil, Player player, InteractionHand usedHand) {
        Binding binding = sigil.getOrDefault(BMDataComponents.BINDING, Binding.EMPTY);
        if (binding.isEmpty()) {
            ChatUtil.sendChatNoSpam(player, List.of(Component.translatable("chat.bloodmagic.item_not_bound")));
            return 0;
        }

        List<Component> send = new ArrayList<>();
        if (!binding.uuid().equals(player.getUUID())) {
            send.add(Component.translatable("chat.bloodmagic.divination.other_network", binding.name()));
        }
        send.add(Component.translatable("chat.bloodmagic.divination.current_essence", SoulNetworkHelper.getSoulNetwork(binding).getCurrentEssence()));

        ChatUtil.sendChatNoSpam(player, send);

        return 0;
    }

    private static Component translateAltar(String thing, Object... args) {
        return Component.translatable("chat.bloodmagic.divination.altar." + thing, args);
    }

    @Override
    public int useOnBlock(ItemStack sigil, Player player, UseOnContext context) {
        Level level = context.getLevel();
        BlockEntity be = level.getBlockEntity(context.getClickedPos());

        if (be instanceof BloodAltarTile altar) {
            List<Component> send = new ArrayList<>();
            send.add(translateAltar("tier", Component.translatable("altar.bloodmagic.tier_" + altar.tier)));
            send.add(translateAltar("essence", altar.getFluidInTank(0)));
            send.add(translateAltar("max_essence", altar.getMainCapacity()));
            if (isAdvanced) {
                // TODO add I/O tank info? 1.20 both do the same here
            }

            ChatUtil.sendChatNoSpam(player, send);
            return 0;
        }

        // TODO IncenseAltarTile impl

        return 0;
    }

    @Override
    public MapCodec<? extends SigilEffect> codec() {
        return CODEC;
    }
}
