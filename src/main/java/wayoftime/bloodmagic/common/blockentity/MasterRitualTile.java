package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableBoolean;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.datacomponent.Binding;
import wayoftime.bloodmagic.api.helper.SoulNetworkHelper;
import wayoftime.bloodmagic.api.ritual.ActivationResult;
import wayoftime.bloodmagic.api.ritual.Ritual;
import wayoftime.bloodmagic.api.ritual.RitualConfig;
import wayoftime.bloodmagic.api.ritual.RitualStructure;
import wayoftime.bloodmagic.api.soulnetwork.SoulTicket;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.datacomponent.SoulNetwork;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class MasterRitualTile extends BaseTile {
    public MasterRitualTile(BlockPos pos, BlockState blockState) {
        super(BMTiles.MASTER_RITUAL_TYPE.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MasterRitualTile tile) {
        impl
    }

    private Holder<Ritual> ritual;
    private Binding owner = Binding.EMPTY;
    private RitualConfig config;
    public ActivationResult tryActivate(Binding binding) {
        if (binding.isEmpty()) {
            return ActivationResult.NOT_BOUND;
        }
        Optional<Map.Entry<ResourceKey<RitualStructure>, RitualStructure>> potentialRitual = level.registryAccess().registryOrThrow(BMIdentifiers.RegistryKeys.RITUAL_STRUCTURES).entrySet().stream()
                .filter(entry -> {
                    MutableBoolean ret = new MutableBoolean(true);
                    entry.getValue().positions().forEach(pair -> {
                        BlockPos offset = pair.getFirst();
                        if (!level.getBlockState(getBlockPos().offset(offset.getX(), offset.getY(), offset.getZ())).is(pair.getSecond())) {
                            ret.setFalse();
                        }
                    });
                    return ret.getValue();
                })
                .findFirst();

        if (potentialRitual.isPresent()) {
            Registry<Ritual> ritualRegistry =  level.registryAccess().registryOrThrow(BMIdentifiers.RegistryKeys.RITUALS);
            if (ritualRegistry.containsKey(potentialRitual.get().getKey().location())) {
                Holder<Ritual> rit = ritualRegistry.getHolderOrThrow(ResourceKey.create(BMIdentifiers.RegistryKeys.RITUALS, potentialRitual.get().getKey().location()));
                SoulNetwork ownerNetwork = SoulNetworkHelper.getSoulNetwork(binding);
                if (ownerNetwork.getCurrentEssence() >= rit.value().getActivationCost()) {
                    ownerNetwork.syphon(SoulTicket.block(level, getBlockPos(), rit.value().getActivationCost()));
                    owner = binding;
                    ritual = rit;
                    config = new RitualConfig(ritual.value().getDefaultRanges(this), new EnumMap<>(EnumWillType.class));
                    setChanged();
                    return ActivationResult.ACTIVATED;
                } else {
                    return ActivationResult.TOO_WEAK;
                }
            } else {
                return ActivationResult.NO_RITUAL;
            }
        } else {
            return ActivationResult.INVALID_STRUCTURE;
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put()
    }

    public Binding getBinding() {
        return owner;
    }

    public RitualConfig getConfig() {
        return config;
    }
}
