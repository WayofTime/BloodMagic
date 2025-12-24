package wayoftime.bloodmagic.api.datacomponent;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.api.BMTags;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record LivingStats(Object2FloatOpenHashMap<Holder<LivingUpgrade>> upgrades) implements TooltipProvider {
    public static final Codec<LivingStats> CODEC =
    Codec.unboundedMap(RegistryFixedCodec.create(BMIdentifiers.RegistryKeys.LIVING_UPGRADES), Codec.FLOAT)
            .xmap(Object2FloatOpenHashMap::new, Function.identity())
            .xmap(LivingStats::new, LivingStats::upgrades);

    public static final LivingStats EMPTY = new LivingStats(new Object2FloatOpenHashMap<>());

    public Object2FloatMap.FastEntrySet<Holder<LivingUpgrade>> object2FloatEntrySet() {
        return upgrades.object2FloatEntrySet();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        HolderSet<LivingUpgrade> order = getOrder(context.registries());
        for (Holder<LivingUpgrade> holder : order) {
            if (this.upgrades.containsKey(holder)) {
                float exp = this.upgrades.getFloat(holder);
                tooltipAdder.accept(LivingHelper.getTooltip(holder, exp, tooltipFlag.hasShiftDown()));
            }
        }

        for (Object2FloatMap.Entry<Holder<LivingUpgrade>> entry : this.upgrades.object2FloatEntrySet()) {
            if (!order.contains(entry.getKey()) && !entry.getKey().is(BMTags.Living.TOOLTIP_HIDE)) {
                tooltipAdder.accept(LivingHelper.getTooltip(entry.getKey(), entry.getFloatValue(), tooltipFlag.hasShiftDown()));
            }
        }
    }

    private static HolderSet<LivingUpgrade> getOrder(HolderLookup.Provider registries) {
        if (registries != null) {
            Optional<HolderSet.Named<LivingUpgrade>> optional = registries.lookupOrThrow(BMIdentifiers.RegistryKeys.LIVING_UPGRADES).get(BMTags.Living.TOOLTIP_ORDER);
            if (optional.isPresent()) {
                return optional.get();
            }
        }

        return HolderSet.empty();
    }
}
