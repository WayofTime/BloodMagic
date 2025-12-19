package wayoftime.bloodmagic.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.caps.BMCaps;
import wayoftime.bloodmagic.common.caps.IRunePowers;
import wayoftime.bloodmagic.common.damagesource.BMDamageSources;
import wayoftime.bloodmagic.common.registry.AltarTier;
import wayoftime.bloodmagic.common.registry.BMRegistries;
import wayoftime.bloodmagic.common.tag.BMTags;

import java.util.*;

@EventBusSubscriber
public class AltarUtil {

    static List<AltarTier> TIERS = null;
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        TIERS = event.getServer().registryAccess().registry(BMRegistries.Keys.ALTAR_TIER_KEY).orElseThrow().stream()
                .sorted()
                .toList();
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        TIERS = null;
    }

    public static BlockPos findAltar(Level level, BlockPos pos, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos testPos = pos.offset(x, y, z);
                    BlockState testState = level.getBlockState(testPos);
                    if (testState.is(BMBlocks.BLOOD_ALTAR.block())) {
                        return testPos;
                    }
                }
            }
        }
        return null;
    }

    public static DamageSource sacrificeDamage(Player causer) {
        DamageSources sources = causer.level().damageSources();
        return sources.source(BMDamageSources.SELF_SACRIFICE, causer);
    }

    public static int getTier(Level level, BlockPos altarPos) {
        int tier = -1;
        if (!level.getBlockState(altarPos).is(BMBlocks.BLOOD_ALTAR.block())) {
            return tier;
        } else {
            tier = 1;
        }
        BlockPos centerPos = altarPos.below();
        int distance = 0;
        for (AltarTier current : TIERS) {
            distance += current.distance();
            for (Direction side : Direction.Plane.HORIZONTAL) {
                BlockPos centerRune = centerPos.relative(side, distance);
                if (!level.getBlockState(centerRune).is(BMTags.Blocks.RUNES)) {
                    // BloodMagic.LOGGER.info("missing rune at {}", centerRune);
                    return tier;
                }
                for (int j = 0; j < current.sideRunes(); j++) {
                    if (!level.getBlockState(centerRune.relative(side.getClockWise(), 1 + j)).is(BMTags.Blocks.RUNES)) {
                        // BloodMagic.LOGGER.info("missing side rune at CW {}, {}", j, side);
                        return tier;
                    }
                    if (!level.getBlockState(centerRune.relative(side.getCounterClockWise(), 1 + j)).is(BMTags.Blocks.RUNES)) {
                        // BloodMagic.LOGGER.info("missing side rune at CCW {}, {}", j, side);
                        return tier;
                    }
                }
                BlockPos bottomPillar = centerRune.relative(side.getClockWise(), current.sideRunes() + current.sideBlocks() + 1).above(1 + current.pillarOffset());
                for (int j = 0; j < current.pillarHeight(); j++) {
                    BlockPos pillarPos = bottomPillar.above(j);
                    BlockState pillarState = level.getBlockState(pillarPos);
                    if (BuiltInRegistries.BLOCK.getOrCreateTag(BMTags.Blocks.PILLARS).size() > 0) {
                        if (!pillarState.is(BMTags.Blocks.PILLARS)) {
                            // BloodMagic.LOGGER.info("missing pillar at {}, '{}' is not in pillar tag", pillarPos, pillarState);
                            return tier;
                        }
                    } else {
                        if (!pillarState.isFaceSturdy(level, pillarPos, Direction.DOWN, SupportType.CENTER)
                                || !pillarState.isFaceSturdy(level, pillarPos, Direction.UP, SupportType.CENTER)) {
                            // BloodMagic.LOGGER.info("missing pillar at {}, '{}'", pillarPos, pillarState);
                            return tier;
                        }
                    }
                }

                boolean isTag = current.capstone().tag();
                ResourceLocation req = current.capstone().id();
                BlockPos capPos = bottomPillar.above(current.pillarHeight());
                BlockState capState = level.getBlockState(capPos);
                if (isTag) {
                    TagKey<Block> requiredTag = TagKey.create(Registries.BLOCK, req);
                    if (!capState.is(requiredTag)) {
                        // BloodMagic.LOGGER.info("missing capstone (tag) {}/{}, has '{}', want: '{}'", capPos, side, capState, req);
                        return tier;
                    }
                } else {
                    // if wanted state is air, we dont check
                    Block requiredBlock = BuiltInRegistries.BLOCK.get(req);
                    if (!capState.is(requiredBlock) && !requiredBlock.defaultBlockState().isEmpty()) {
                        // BloodMagic.LOGGER.info("missing capstone (block) {}/{}, has '{}', want: '{}'", capPos, side, capState, req);
                        return tier;
                    }
                }
            }
            tier = current.tier();
            centerPos = centerPos.below();
        }

        return tier;
    }

    public static Map<EnumRuneType, Integer> getUpgrades(int tier, Level level, BlockPos altarPos) {
        BlockPos centerPos = altarPos.below();
        int distance = 0;
        Set<BlockPos> upgradePositions = new HashSet<>();
        for (int i = 0; i < tier - 1; i++) {
            AltarTier current = TIERS.get(i);
            distance += current.distance();
            for (Direction side : Direction.Plane.HORIZONTAL) {
                BlockPos centerRune = centerPos.relative(side, distance);
                upgradePositions.add(centerRune);
                for (int j = 0; j < current.sideRunes(); j++) {
                    upgradePositions.add(centerRune.relative(side.getClockWise(), 1 + j));
                    upgradePositions.add(centerRune.relative(side.getCounterClockWise(), 1 + j));
                }
            }
            centerPos = centerPos.below();
        }

        Map<EnumRuneType, Integer> upgrades = new HashMap<>();
        upgradePositions.forEach(pos -> {
            IRunePowers rune = level.getCapability(BMCaps.RUNE_POWERS, pos);
            if (rune == null) {
                return;
            }
            rune.getRunePowers().forEach((type, amount) -> {
                upgrades.compute(type, (k, v) -> v == null ? amount : v + amount);
            });
        });

        // BloodMagic.LOGGER.info("got '{}'", upgrades);

        return upgrades;
    }
}
