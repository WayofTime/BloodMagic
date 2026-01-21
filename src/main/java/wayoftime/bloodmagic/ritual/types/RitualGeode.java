package wayoftime.bloodmagic.ritual.types;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.ConfigManager;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.registries.BloodMagicDamageTypes;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.ritual.*;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.util.helper.BlockProtectionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RitualRegister("geode")
public class RitualGeode extends Ritual {

    public static final String ACCELERATION_RANGE = "acceleration";
    public static final String HARVEST_RANGE = "harvest";
    public static final String CHEST_RANGE = "chest";
    public static final String HARM_RANGE = "harm";

    public static final double MIN_DEFAULT = ConfigManager.COMMON.min_raw_will.get();
    public static final double MIN_DESTRUCTIVE = ConfigManager.COMMON.min_destructive_will.get();
    public static final double MIN_CORROSIVE = ConfigManager.COMMON.min_corrosive_will.get();
    public static final double MIN_STEADFAST = ConfigManager.COMMON.min_steadfast_will.get();
    public static final double MIN_VENGEFUL = ConfigManager.COMMON.min_vengeful_will.get();

    public static final double WILL_PER_SILK = ConfigManager.COMMON.silk_touch_cost.get();
    public static final double WILL_PER_FORTUNE = ConfigManager.COMMON.fortune_cost.get();
    public static final double WILL_PER_GROWTH = ConfigManager.COMMON.growth_cost.get();
    public static final double WILL_PER_STORE = ConfigManager.COMMON.store_cost.get();
    public static final double WILL_PER_HARM = ConfigManager.COMMON.harm_cost.get();

    public static final int ACTIVATION_COST = ConfigManager.COMMON.activation_cost.get();
    public static final int REFRESH_COST = ConfigManager.COMMON.refresh_cost.get();

    public static final int HURT_DAMAGE = ConfigManager.COMMON.hurt_damage.get();
    public static final int MAX_BLOCKS = ConfigManager.COMMON.max_budding_blocks.get();
    public static final int MAX_HARM = ConfigManager.COMMON.max_mobs_harmed.get();

    public static final ItemStack mockPick = new ItemStack(Items.NETHERITE_PICKAXE);
    public static final ItemStack fortunePick = new ItemStack(Items.NETHERITE_PICKAXE);
    public static final ItemStack silkPick = new ItemStack(Items.NETHERITE_PICKAXE);
    static {
        EnchantmentHelper.setEnchantments(Map.of(Enchantments.BLOCK_FORTUNE, 3), fortunePick);
        EnchantmentHelper.setEnchantments(Map.of(Enchantments.SILK_TOUCH, 1), silkPick);
    }

    public RitualGeode() {
        super("ritualGeode", 0, ACTIVATION_COST, "ritual." + BloodMagic.MODID + ".geode");
        addBlockRange(HARVEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 2, -1), 3, 3, 3));
        addBlockRange(ACCELERATION_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 3, 0), 1));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
        addBlockRange(HARM_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, -3, -1), 3, 3, 3));

        setMaximumVolumeAndDistanceOfRange(HARVEST_RANGE, 3 * 3 * (2 + MAX_BLOCKS), 15, 15);
        setMaximumVolumeAndDistanceOfRange(ACCELERATION_RANGE, MAX_BLOCKS, 15, 15);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 15, 15);
        setMaximumVolumeAndDistanceOfRange(HARM_RANGE, 5 * 5 * 3, 15, 15);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        Level world = masterRitualStone.getWorldObj();
        if (world.isClientSide)
        {
            return;
        }
        BlockPos pos = masterRitualStone.getMasterBlockPos();
        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
        double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

        BlockPos chestPos = masterRitualStone.getBlockRange(CHEST_RANGE).getContainedPositions(pos).get(0);
        BlockEntity inv = world.getBlockEntity(chestPos);
        boolean hasInv = inv != null && Utils.getNumberOfFreeSlots(inv, Direction.DOWN) >= 1;

        boolean doHarm = vengefulWill > MIN_VENGEFUL;
        boolean doAccel = corrosiveWill > MIN_CORROSIVE;
        boolean doStore = rawWill > MIN_DEFAULT && hasInv;
        boolean doFortune = false;
        boolean doSilk = false;

        ItemStack toolStack = mockPick;
        if (destructiveWill > MIN_DESTRUCTIVE) {
            toolStack = fortunePick;
            doFortune = true;
        }
        if (steadfastWill > MIN_STEADFAST) {
            toolStack = silkPick;
            doSilk = true;
            // if both are configured silk touch takes priority and fortune cost shouldnt be paid
            doFortune = false;
        }

        double fortuneWill = destructiveWill;
        double silkWill = steadfastWill;
        double storeWill = rawWill;
        List<ItemStack> drops = new ArrayList<>();
        for (BlockPos harvestPos : masterRitualStone.getBlockRange(HARVEST_RANGE).getContainedPositions(pos)) {
            BlockState state = world.getBlockState(harvestPos);
            if (state.is(BloodMagicTags.Blocks.GEODE_HARVESTABLE)) {
                if (doFortune) {
                    if (fortuneWill < WILL_PER_FORTUNE) {
                        continue;
                    }
                }
                if (doSilk) {
                    if (silkWill < WILL_PER_SILK) {
                        continue;
                    }
                }

                LootParams.Builder paramsBuilder = new LootParams.Builder((ServerLevel) world)
                        .withParameter(LootContextParams.ORIGIN, pos.getCenter())
                        .withParameter(LootContextParams.BLOCK_STATE, world.getBlockState(pos))
                        .withParameter(LootContextParams.TOOL, toolStack)
                        .withOptionalParameter(LootContextParams.BLOCK_ENTITY, world.getBlockEntity(pos))
                        .withOptionalParameter(LootContextParams.THIS_ENTITY, FakePlayerFactory.get((ServerLevel) world, new GameProfile(masterRitualStone.getOwner(), "[BM Geode]")));

                List<ItemStack> blockDrops = state.getDrops(paramsBuilder);
                drops.addAll(blockDrops);
                BlockProtectionHelper.tryBreakBlockNoDrops(world, harvestPos, masterRitualStone.getOwner());
                if (doFortune) {
                    fortuneWill -= WILL_PER_FORTUNE;
                }
                if (doSilk) {
                    silkWill -= WILL_PER_SILK;
                }

                for (ItemStack dropStack : drops) {
                    if (doStore && storeWill >= WILL_PER_STORE) {
                        dropStack = Utils.insertStackIntoTile(dropStack, inv, Direction.DOWN);
                        storeWill -= WILL_PER_STORE;
                    }
                    if (!dropStack.isEmpty()) {
                        Utils.spawnStackAtBlock(world, harvestPos, Direction.UP, dropStack);
                    }
                }
            }
        }

        double harmWill = vengefulWill;
        double harmTicks = 0;
        for (LivingEntity mob : world.getEntitiesOfClass(LivingEntity.class, masterRitualStone.getBlockRange(HARM_RANGE).getAABB(pos))) {
            if (doHarm && harmWill >= WILL_PER_HARM) {
                if (mob.hurt(world.damageSources().source(BloodMagicDamageTypes.SACRIFICE), HURT_DAMAGE)) {
                    harmTicks++;
                    harmWill -= WILL_PER_HARM;
                }
                if (harmTicks >= MAX_HARM) {
                    break;
                }
            }
        }

        double accelWill = corrosiveWill;
        for (BlockPos accelPos : masterRitualStone.getBlockRange(ACCELERATION_RANGE).getContainedPositions(pos)) {
            BlockState state = world.getBlockState(accelPos);
            if (state.is(BloodMagicTags.Blocks.GEODE_ACCELERATABLE)) {
                for (int i = 0; i < harmTicks; i++) {
                    state.randomTick((ServerLevel) world, accelPos, world.getRandom());
                }
                if (doAccel && accelWill >= WILL_PER_GROWTH) {
                    state.randomTick((ServerLevel) world, accelPos, world.getRandom());
                    state.randomTick((ServerLevel) world, accelPos, world.getRandom());
                    accelWill -= WILL_PER_GROWTH;
                }
            }
        }

        if (doStore) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawWill - storeWill, true);
        }

        if (doFortune) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DESTRUCTIVE, destructiveWill - fortuneWill, true);
        }

        if (doSilk) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastWill - silkWill, true);
        }

        if (doAccel) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveWill - accelWill, true);
        }

        if (doHarm) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulWill - harmWill, true);
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
    }

    @Override
    public int getRefreshCost() {
        return REFRESH_COST;
    }

    @Override
    public int getRefreshTime() {
        return 20;
    }

    @Override
    public Component[] provideInformationOfRitualToPlayer(Player player)
    {
        return new Component[] {
                Component.translatable("%s.info".formatted(this.getTranslationKey())),
                Component.translatable(this.getTranslationKey() + ".default.info"),
                Component.translatable(this.getTranslationKey() + ".corrosive.info"),
                Component.translatable(this.getTranslationKey() + ".steadfast.info"),
                Component.translatable(this.getTranslationKey() + ".destructive.info"),
                Component.translatable(this.getTranslationKey() + ".vengeful.info")
        };
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
        addParallelRunes(components, 1, 0, EnumRuneType.AIR);
        addParallelRunes(components, 2, 0, EnumRuneType.FIRE);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualGeode();
    }
}
