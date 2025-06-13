package wayoftime.bloodmagic.common.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.screen.TrainerScreen;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.menu.GhostItemHandler;
import wayoftime.bloodmagic.common.menu.TrainerMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public record UpgradeLimits(boolean allowOthers, Object2FloatOpenHashMap<Holder<LivingUpgrade>> limits) {
    public static final Codec<UpgradeLimits> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.BOOL.fieldOf("allow_others").forGetter(UpgradeLimits::allowOthers),
            BMDataComponents.UPGRADE_HOLDER_CODEC.fieldOf("limits").forGetter(UpgradeLimits::limits)
    ).apply(builder, UpgradeLimits::new));

    public static final UpgradeLimits EMPTY = new UpgradeLimits(false, LivingHelper.EMPTY_UPGRADE_MAP);

    public float getLimit(Holder<LivingUpgrade> upgrade) {
        float def = allowOthers ? -1 : 0;
        return limits.getOrDefault(upgrade, def);
    }

    public List<Pair<Integer, Integer>> fillData(GhostItemHandler handler) {
        int index = 0;
        List<Pair<Integer, Integer>> start = new ArrayList<>();
        start.add(Pair.of(1, allowOthers ? TrainerMenu.ALLOW : TrainerMenu.DENY));
        for (Map.Entry<Holder<LivingUpgrade>, Float> entry: limits.object2FloatEntrySet()) {
            if (!(index < handler.getSlots())) {
                break;
            }
            ItemStack tomeStack = new ItemStack(BMItems.UPGRADE_TOME);
            tomeStack.set(BMDataComponents.UPGRADE_TOME_DATA, new UpgradeTome(entry.getKey(), entry.getValue()));
            int level = LivingHelper.getLevelFromXp(entry.getKey(), entry.getValue());
            start.add(Pair.of(3 + index, level));
            handler.setStackInSlot(index++, tomeStack);
        }

        return start;
    }
}
