package wayoftime.bloodmagic.common.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.api.TriPredicate;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.registry.AltarComponent;
import wayoftime.bloodmagic.common.registry.AltarTier;
import wayoftime.bloodmagic.common.registry.BMRegistries;
import wayoftime.bloodmagic.common.tag.BMTags;

import java.util.*;

public class BMMultiblock {

    // TODO keeping this until I get around to adding modopedia as replacement
    public static void register(IEventBus eventBus) {
        //eventBus.addListener(BMMultiblock::onServerStarted); // FU world crashing because it "already exists" y u no clear on server stop patchouli >:(
        //eventBus.addListener(BMMultiblock::onServerStopped);
    }

    public static ResourceLocation[] TIER_KEYS = new ResourceLocation[]{};
    public static AltarTier[] TIER_LIST = new AltarTier[]{};
    public static void onServerStarted(ServerStartedEvent event) {
        List<Holder<AltarTier>> tierList = event.getServer().registryAccess().registryOrThrow(BMRegistries.Keys.ALTAR_TIER_KEY).getOrCreateTag(BMTags.Tiers.VALID_TIERS).stream().toList();
        ResourceLocation[] keys = new ResourceLocation[tierList.size()];
        AltarTier[] tiers = new AltarTier[tierList.size()];
        PatchouliAPI.IPatchouliAPI patchouliAPI = PatchouliAPI.get();

        for (Holder<AltarTier> holder : tierList) {
            int tier = holder.value().tier();
            keys[tier] = holder.getKey().location();
            tiers[tier] = holder.value();
            Map<BlockPos, IStateMatcher> multiblock = new HashMap<>();
            for(AltarComponent component : tiers[tier].components()) {
                multiblock.put(component.pos(), new StateMatcher(component, event.getServer().registryAccess()));
            }
            TestMultiblock struct = new TestMultiblock(multiblock);
            struct.setOffset(0, 1, 0).setSymmetrical(true);
            /*
            Iterable<BlockPos> test = BlockPos.betweenClosed(BlockPos.ZERO, new BlockPos(struct.getSize().getX() - 1, struct.getSize().getY() - 1, struct.getSize().getZ() - 1));
            BloodMagic.LOGGER.info("iterating over {}", keys[tier]);
            for (BlockPos testPos : test) {
                struct.getBlockState(testPos);
            }
            */
            patchouliAPI.registerMultiblock(keys[tier], struct);

        }
        TIER_KEYS = keys;
        TIER_LIST = tiers;
    }

    public static void onServerStopped(ServerStoppedEvent event) {
        TIER_KEYS = new ResourceLocation[]{};
        TIER_LIST = new AltarTier[]{};
    }

    public static class StateMatcher implements IStateMatcher {

        private TriPredicate<BlockGetter, BlockPos, BlockState> predicate;
        private List<BlockState> display;
        public StateMatcher(AltarComponent component, RegistryAccess registries) {
            Registry<Block> blockRegistry = registries.registryOrThrow(Registries.BLOCK);
            List<BlockState> stateList = new ArrayList<>();
            if (component.material().tag()) {
                predicate = (getter, pos, state) -> state.is(TagKey.create(Registries.BLOCK, component.material().id()));
                blockRegistry.getOrCreateTag(TagKey.create(Registries.BLOCK, component.material().id())).stream().map(Holder::value).map(Block::defaultBlockState).forEach(stateList::add);
                if (component.material().id().equals(BMTags.Blocks.PILLARS.location())) {
                    if (stateList.isEmpty()) {
                        predicate = (getter, pos, state) -> state.isSolid();
                        stateList.add(Blocks.STONE_BRICKS.defaultBlockState());
                    }
                }
                if (component.material().id().equals(BMTags.Blocks.RUNES.location())) {
                    // using blank/non-blank runes to signify whether this is an upgrade spot... since the actual rune benefits are configurable this is suboptimal
                    // probably fine just mentioning this behaviour in the book
                    if (component.isUpgrade()) {
                        stateList.remove(BMBlocks.RUNE_BLANK.block().get().defaultBlockState());
                    } else {
                        stateList = List.of(BMBlocks.RUNE_BLANK.block().get().defaultBlockState());
                    }
                }
            } else {
                Block block = blockRegistry.getOrThrow(ResourceKey.create(Registries.BLOCK, component.material().id()));
                predicate = (getter, pos, state) -> state.is(block);
                stateList.add(block.defaultBlockState());
            }

            display = stateList;
        }

        @Override
        public BlockState getDisplayedState(long ticks) {
            if (display.isEmpty()) {
                return Blocks.BEDROCK.defaultBlockState();
            } else {
                int idx = (int) ((ticks / 20) % display.size());
                return display.get(idx);
            }
        }

        @Override
        public TriPredicate<BlockGetter, BlockPos, BlockState> getStatePredicate() {
            return predicate;
        }
    }
}
