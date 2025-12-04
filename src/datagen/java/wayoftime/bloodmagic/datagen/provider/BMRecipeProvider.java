package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.fluid.BMFluids;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.tag.BMTags;
import wayoftime.bloodmagic.datagen.builder.AlchemyArrayRecipeBuilder;
import wayoftime.bloodmagic.datagen.builder.AlchemyTableRecipeBuilder;
import wayoftime.bloodmagic.datagen.builder.recipe.ARCRecipeBuilder;
import wayoftime.bloodmagic.datagen.builder.recipe.AltarRecipeBuilder;
import wayoftime.bloodmagic.datagen.builder.recipe.ForgeRecipeBuilder;
import wayoftime.bloodmagic.datagen.builder.recipe.TieredRecipeBuilder;

import java.util.concurrent.CompletableFuture;

public class BMRecipeProvider extends RecipeProvider {

    public BMRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        addVanillaCraftingRecipes(output);
        addTieredRecipes(output);
        addBloodAltarRecipes(output);
        addSoulForgeRecipes(output);
        addAlchemyArrayRecipes(output);
        addAlchemyTableRecipes(output);
        addARCRecipes(output);
    }

    private void addVanillaCraftingRecipes(RecipeOutput output) {
        // Sacrificial Dagger - diagonal dagger shape
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BMItems.SACRIFICIAL_DAGGER.get())
                .pattern("ggg")
                .pattern(" Gg")
                .pattern("i g")
                .define('g', Tags.Items.GLASS_BLOCKS)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('i', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
                .save(output);

        // Blood Altar
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BMBlocks.BLOOD_ALTAR.block().get())
                .pattern("s s")
                .pattern("sfs")
                .pattern("gsg")
                .define('s', Tags.Items.STONES)
                .define('f', Items.FURNACE)
                .define('g', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_furnace", has(Items.FURNACE))
                .save(output);

        // Hellfire Forge (Soul Forge)
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BMBlocks.HELLFIRE_FORGE.block().get())
                .pattern("i i")
                .pattern("sbs")
                .pattern("sos")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('s', Tags.Items.STONES)
                .define('b', Items.IRON_BLOCK)
                .define('o', BMItems.ORB_WEAK.get())
                .unlockedBy("has_weak_orb", has(BMItems.ORB_WEAK.get()))
                .save(output);

        // Bloodstone
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BMBlocks.BLOODSTONE.block().get(), 8)
                .pattern("sss")
                .pattern("sbs")
                .pattern("sss")
                .define('s', Items.STONE)
                .define('b', BMFluids.LIFE_ESSENCE_BUCKET.get())
                .unlockedBy("has_life_essence", has(BMFluids.LIFE_ESSENCE_BUCKET.get()))
                .save(output);

        // Bloodstone Brick (from bloodstone)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BMBlocks.BLOODSTONE_BRICK.block().get(), 4)
                .pattern("ss")
                .pattern("ss")
                .define('s', BMBlocks.BLOODSTONE.block().get())
                .unlockedBy("has_bloodstone", has(BMBlocks.BLOODSTONE.block().get()))
                .save(output);

        // Hellforged Block (storage block)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BMBlocks.HELLFORGED_BLOCK.block().get())
                .pattern("iii")
                .pattern("iii")
                .pattern("iii")
                .define('i', Tags.Items.INGOTS_IRON) // TODO: Replace with hellforged ingot when available
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output, BloodMagic.rl("hellforged_block_from_ingots"));

        // Synthetic Point
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BMItems.SYNTHETIC_POINT.get(), 2)
                .pattern("imi")
                .pattern("mrm")
                .pattern("imi")
                .define('i', Tags.Items.NUGGETS_IRON)
                .define('m', ItemTags.MEAT)
                .define('r', Items.REDSTONE)
                .unlockedBy("has_meat", has(ItemTags.MEAT))
                .save(output);

        // Blank Rune - stone around, blank slate at top center, blood orb in center
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BMBlocks.RUNE_BLANK.block().get())
                .pattern("asa")
                .pattern("aoa")
                .pattern("aaa")
                .define('a', Tags.Items.STONES)
                .define('s', BMItems.SLATE_BLANK.get())
                .define('o', BMItems.ORB_WEAK.get())
                .unlockedBy("has_blank_slate", has(BMItems.SLATE_BLANK.get()))
                .save(output);

        // Tier 1 Runes
        addRuneRecipe(output, BMBlocks.RUNE_SPEED.block().get(), Items.SUGAR, BMItems.ORB_WEAK.get());
        addRuneRecipe(output, BMBlocks.RUNE_EFFICIENCY.block().get(), Items.REDSTONE, BMItems.ORB_WEAK.get());
        addRuneRecipe(output, BMBlocks.RUNE_SACRIFICE.block().get(), Items.GOLD_INGOT, BMItems.ORB_APPRENTICE.get());
        addRuneRecipe(output, BMBlocks.RUNE_SELF_SACRIFICE.block().get(), Items.GLOWSTONE_DUST, BMItems.ORB_APPRENTICE.get());
        addRuneRecipeWithBucket(output, BMBlocks.RUNE_CAPACITY.block().get(), Items.BUCKET, BMItems.ORB_MAGICIAN.get());
        addRuneRecipeWithBucket(output, BMBlocks.RUNE_DISLOCATION.block().get(), Items.WATER_BUCKET, BMItems.ORB_MAGICIAN.get());
        addRuneRecipe(output, BMBlocks.RUNE_CHARGING.block().get(), Items.GLOWSTONE, BMItems.ORB_MASTER.get());
        addRuneRecipeWithBucket(output, BMBlocks.RUNE_ACCELERATION.block().get(), Items.GOLD_BLOCK, BMItems.ORB_MASTER.get());
        addRuneRecipeWithBucket(output, BMBlocks.RUNE_CAPACITY_AUGMENTED.block().get(), Items.OBSIDIAN, BMItems.ORB_MASTER.get());

        // Orb Rune requires both weak and master orb
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BMBlocks.RUNE_ORB.block().get())
                .pattern("ror")
                .pattern("ObO")
                .pattern("ror")
                .define('r', BMBlocks.RUNE_BLANK.block().get())
                .define('o', BMItems.ORB_WEAK.get())
                .define('O', BMItems.ORB_MASTER.get())
                .define('b', BMBlocks.BLOODSTONE.block().get())
                .unlockedBy("has_master_orb", has(BMItems.ORB_MASTER.get()))
                .save(output);

        // Tier 2 Runes (require bloodstone and netherite)
        addTier2RuneRecipe(output, BMBlocks.RUNE_2_SPEED.block().get(), BMBlocks.RUNE_SPEED.block().get());
        addTier2RuneRecipe(output, BMBlocks.RUNE_2_EFFICIENCY.block().get(), BMBlocks.RUNE_EFFICIENCY.block().get());
        addTier2RuneRecipe(output, BMBlocks.RUNE_2_SACRIFICE.block().get(), BMBlocks.RUNE_SACRIFICE.block().get());
        addTier2RuneRecipe(output, BMBlocks.RUNE_2_SELF_SACRIFICE.block().get(), BMBlocks.RUNE_SELF_SACRIFICE.block().get());
        addTier2RuneRecipe(output, BMBlocks.RUNE_2_CAPACITY.block().get(), BMBlocks.RUNE_CAPACITY.block().get());
        addTier2RuneRecipe(output, BMBlocks.RUNE_2_DISLOCATION.block().get(), BMBlocks.RUNE_DISLOCATION.block().get());
        addTier2RuneRecipe(output, BMBlocks.RUNE_2_CHARGING.block().get(), BMBlocks.RUNE_CHARGING.block().get());
        addTier2RuneRecipe(output, BMBlocks.RUNE_2_ACCELERATION.block().get(), BMBlocks.RUNE_ACCELERATION.block().get());
        addTier2RuneRecipe(output, BMBlocks.RUNE_2_CAPACITY_AUGMENTED.block().get(), BMBlocks.RUNE_CAPACITY_AUGMENTED.block().get());
        addTier2RuneRecipe(output, BMBlocks.RUNE_2_ORB.block().get(), BMBlocks.RUNE_ORB.block().get());

        // Crystal Cluster (from amethyst and life essence)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BMBlocks.CRYSTAL_CLUSTER.block().get(), 4)
                .pattern("aba")
                .pattern("bab")
                .pattern("aba")
                .define('a', Items.AMETHYST_SHARD)
                .define('b', BMFluids.LIFE_ESSENCE_BUCKET.get())
                .unlockedBy("has_life_essence", has(BMFluids.LIFE_ESSENCE_BUCKET.get()))
                .save(output);

        // Crystal Cluster Brick (from crystal cluster)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BMBlocks.CRYSTAL_CLUSTER_BRICK.block().get(), 4)
                .pattern("cc")
                .pattern("cc")
                .define('c', BMBlocks.CRYSTAL_CLUSTER.block().get())
                .unlockedBy("has_crystal_cluster", has(BMBlocks.CRYSTAL_CLUSTER.block().get()))
                .save(output);

        // Teleposer block
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BMBlocks.TELEPOSER.block().get())
                .pattern("ggg")
                .pattern("ete")
                .pattern("ggg")
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('e', Tags.Items.ENDER_PEARLS)
                .define('t', BMItems.TELEPOSER_FOCUS.get())
                .unlockedBy("has_teleposer_focus", has(BMItems.TELEPOSER_FOCUS.get()))
                .save(output);

        // Reinforced Teleposer Focus
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BMItems.TELEPOSER_FOCUS_REINFORCED.get())
                .requires(BMItems.TELEPOSER_FOCUS_ENHANCED.get())
                .requires(BMItems.WEAK_BLOOD_SHARD.get())
                .unlockedBy("has_enhanced_focus", has(BMItems.TELEPOSER_FOCUS_ENHANCED.get()))
                .save(output);

        // Lava Crystal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BMItems.LAVA_CRYSTAL.get())
                .pattern("aba")
                .pattern("bcb")
                .pattern("ded")
                .define('a', Tags.Items.GLASS_BLOCKS)
                .define('b', Items.LAVA_BUCKET)
                .define('c', BMItems.ORB_WEAK.get())
                .define('d', Tags.Items.OBSIDIANS)
                .define('e', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_weak_orb", has(BMItems.ORB_WEAK.get()))
                .save(output);

        // Blank Ritual Stone - obsidian around, reinforced slate corners, apprentice orb center
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BMBlocks.BLANK_RITUAL_STONE.block().get(), 4)
                .pattern("sos")
                .pattern("oco")
                .pattern("sos")
                .define('o', Tags.Items.OBSIDIANS)
                .define('s', BMItems.SLATE_REINFORCED.get())
                .define('c', BMItems.ORB_APPRENTICE.get())
                .unlockedBy("has_reinforced_slate", has(BMItems.SLATE_REINFORCED.get()))
                .save(output, BloodMagic.rl("ritual_stone_blank"));

        // Master Ritual Stone - obsidian around, ritual stones corners, magician orb center
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BMBlocks.MASTER_RITUAL_STONE.block().get())
                .pattern("oso")
                .pattern("scs")
                .pattern("oso")
                .define('o', Tags.Items.OBSIDIANS)
                .define('s', BMBlocks.BLANK_RITUAL_STONE.block().get())
                .define('c', BMItems.ORB_MAGICIAN.get())
                .unlockedBy("has_ritual_stone", has(BMBlocks.BLANK_RITUAL_STONE.block().get()))
                .save(output, BloodMagic.rl("ritual_stone_master"));
    }

    private void addTieredRecipes(RecipeOutput output) {
        // Blood Tank Tier Upgrade - combines two tanks to upgrade tier
        TieredRecipeBuilder.fluid(RecipeCategory.MISC, BMBlocks.BLOOD_TANK.block().get())
                .pattern("g g")
                .pattern("tgt")
                .pattern("gsg")
                .define('g', Items.GLASS)
                .define('t', BMBlocks.BLOOD_TANK.block().get())
                .define('s', BMBlocks.BLOODSTONE.block().get())
                .primary(3) // slot index for primary tank
                .secondary(5) // slot index for secondary tank
                .unlockedBy("has_blood_tank", has(BMBlocks.BLOOD_TANK.block().get()))
                .save(output, BloodMagic.rl("blood_tank_upgrade"));
    }

    private void addBloodAltarRecipes(RecipeOutput output) {
        // Blood Orb progression - each orb is made from different materials, NOT from previous orb
        AltarRecipeBuilder.build(BMItems.ORB_WEAK.get())
                .from(Tags.Items.GEMS_DIAMOND)
                .minTier(0)
                .bloodNeeded(2000)
                .consumption(5)
                .drain(1)
                .unlockedBy("has_altar", has(BMBlocks.BLOOD_ALTAR.block().get()))
                .save(output, BloodMagic.rl("weak_blood_orb"));

        AltarRecipeBuilder.build(BMItems.ORB_APPRENTICE.get())
                .from(Tags.Items.STORAGE_BLOCKS_REDSTONE)  // Redstone Block
                .minTier(1)
                .bloodNeeded(5000)
                .consumption(5)
                .drain(5)
                .unlockedBy("has_weak_orb", has(BMItems.ORB_WEAK.get()))
                .save(output, BloodMagic.rl("apprentice_blood_orb"));

        AltarRecipeBuilder.build(BMItems.ORB_MAGICIAN.get())
                .from(Tags.Items.STORAGE_BLOCKS_GOLD)  // Gold Block
                .minTier(2)
                .bloodNeeded(25000)
                .consumption(20)
                .drain(20)
                .unlockedBy("has_apprentice_orb", has(BMItems.ORB_APPRENTICE.get()))
                .save(output, BloodMagic.rl("magician_blood_orb"));

        AltarRecipeBuilder.build(BMItems.ORB_MASTER.get())
                .from(BMItems.WEAK_BLOOD_SHARD.get())  // Weak Blood Shard
                .minTier(3)
                .bloodNeeded(40000)
                .consumption(30)
                .drain(50)
                .unlockedBy("has_magician_orb", has(BMItems.ORB_MAGICIAN.get()))
                .save(output, BloodMagic.rl("master_blood_orb"));

        AltarRecipeBuilder.build(BMItems.ORB_ARCHMAGE.get())
                .from(BMBlocks.HELLFORGED_BLOCK.block().get())  // Hellforged Block
                .minTier(4)
                .bloodNeeded(80000)
                .consumption(50)
                .drain(100)
                .unlockedBy("has_master_orb", has(BMItems.ORB_MASTER.get()))
                .save(output, BloodMagic.rl("archmage_blood_orb"));

        // Note: Transcendent orb doesn't exist in 1.20.1 - removed

        // Slates
        AltarRecipeBuilder.build(BMItems.SLATE_BLANK.get())
                .from(Tags.Items.STONES)
                .minTier(0)
                .bloodNeeded(1000)
                .consumption(5)
                .drain(5)
                .unlockedBy("has_altar", has(BMBlocks.BLOOD_ALTAR.block().get()))
                .save(output, BloodMagic.rl("blank_slate"));

        AltarRecipeBuilder.build(BMItems.SLATE_REINFORCED.get())
                .from(BMItems.SLATE_BLANK.get())
                .minTier(1)
                .bloodNeeded(2000)
                .consumption(5)
                .drain(5)
                .unlockedBy("has_blank_slate", has(BMItems.SLATE_BLANK.get()))
                .save(output, BloodMagic.rl("reinforced_slate"));

        AltarRecipeBuilder.build(BMItems.SLATE_IMBUED.get())
                .from(BMItems.SLATE_REINFORCED.get())
                .minTier(2)
                .bloodNeeded(5000)
                .consumption(15)
                .drain(10)
                .unlockedBy("has_reinforced_slate", has(BMItems.SLATE_REINFORCED.get()))
                .save(output, BloodMagic.rl("imbued_slate"));

        AltarRecipeBuilder.build(BMItems.SLATE_DEMONIC.get())
                .from(BMItems.SLATE_IMBUED.get())
                .minTier(3)
                .bloodNeeded(15000)
                .consumption(20)
                .drain(20)
                .unlockedBy("has_imbued_slate", has(BMItems.SLATE_IMBUED.get()))
                .save(output, BloodMagic.rl("demonic_slate"));

        AltarRecipeBuilder.build(BMItems.SLATE_ETHEREAL.get())
                .from(BMItems.SLATE_DEMONIC.get())
                .minTier(4)
                .bloodNeeded(30000)
                .consumption(40)
                .drain(100)
                .unlockedBy("has_demonic_slate", has(BMItems.SLATE_DEMONIC.get()))
                .save(output, BloodMagic.rl("ethereal_slate"));

        // Additional Blood Altar recipes
        AltarRecipeBuilder.build(BMItems.SOUL_SNARE.get())
                .from(Tags.Items.STRINGS)
                .minTier(0)
                .bloodNeeded(500)
                .consumption(5)
                .drain(1)
                .unlockedBy("has_altar", has(BMBlocks.BLOOD_ALTAR.block().get()))
                .save(output, BloodMagic.rl("soul_snare"));

        AltarRecipeBuilder.build(BMItems.DAGGER_OF_SACRIFICE.get())
                .from(Items.IRON_SWORD)
                .minTier(1)
                .bloodNeeded(3000)
                .consumption(5)
                .drain(5)
                .unlockedBy("has_altar", has(BMBlocks.BLOOD_ALTAR.block().get()))
                .save(output, BloodMagic.rl("dagger_of_sacrifice"));

        AltarRecipeBuilder.build(BMFluids.LIFE_ESSENCE_BUCKET.get())
                .from(Items.BUCKET)
                .minTier(0)
                .bloodNeeded(1000)
                .consumption(5)
                .drain(0)
                .unlockedBy("has_altar", has(BMBlocks.BLOOD_ALTAR.block().get()))
                .save(output, BloodMagic.rl("bucket_life"));

        // Teleposer Focus - ender pearl on tier 3 altar
        AltarRecipeBuilder.build(BMItems.TELEPOSER_FOCUS.get())
                .from(Tags.Items.ENDER_PEARLS)
                .minTier(3)
                .bloodNeeded(2000)
                .consumption(10)
                .drain(10)
                .unlockedBy("has_demonic_slate", has(BMItems.SLATE_DEMONIC.get()))
                .save(output, BloodMagic.rl("teleposer_focus"));

        // Enhanced Teleposer Focus - from teleposer focus on tier 3 altar
        AltarRecipeBuilder.build(BMItems.TELEPOSER_FOCUS_ENHANCED.get())
                .from(BMItems.TELEPOSER_FOCUS.get())
                .minTier(3)
                .bloodNeeded(10000)
                .consumption(20)
                .drain(10)
                .unlockedBy("has_teleposer_focus", has(BMItems.TELEPOSER_FOCUS.get()))
                .save(output, BloodMagic.rl("enhanced_teleposer_focus"));

        // Inscription Tools
        AltarRecipeBuilder.build(BMItems.INSCRIPTION_TOOL_AIR.get())
                .from(Items.GHAST_TEAR)
                .minTier(2)
                .bloodNeeded(1000)
                .consumption(5)
                .drain(5)
                .unlockedBy("has_reinforced_slate", has(BMItems.SLATE_REINFORCED.get()))
                .save(output, BloodMagic.rl("air_tool"));

        AltarRecipeBuilder.build(BMItems.INSCRIPTION_TOOL_FIRE.get())
                .from(Items.MAGMA_CREAM)
                .minTier(2)
                .bloodNeeded(1000)
                .consumption(5)
                .drain(5)
                .unlockedBy("has_reinforced_slate", has(BMItems.SLATE_REINFORCED.get()))
                .save(output, BloodMagic.rl("fire_tool"));

        AltarRecipeBuilder.build(BMItems.INSCRIPTION_TOOL_WATER.get())
                .from(Tags.Items.STORAGE_BLOCKS_LAPIS)
                .minTier(2)
                .bloodNeeded(1000)
                .consumption(5)
                .drain(5)
                .unlockedBy("has_reinforced_slate", has(BMItems.SLATE_REINFORCED.get()))
                .save(output, BloodMagic.rl("water_tool"));

        AltarRecipeBuilder.build(BMItems.INSCRIPTION_TOOL_EARTH.get())
                .from(Tags.Items.OBSIDIANS)
                .minTier(2)
                .bloodNeeded(1000)
                .consumption(5)
                .drain(5)
                .unlockedBy("has_reinforced_slate", has(BMItems.SLATE_REINFORCED.get()))
                .save(output, BloodMagic.rl("earth_tool"));

        AltarRecipeBuilder.build(BMItems.INSCRIPTION_TOOL_DUSK.get())
                .from(Tags.Items.STORAGE_BLOCKS_COAL)
                .minTier(3)
                .bloodNeeded(2000)
                .consumption(20)
                .drain(10)
                .unlockedBy("has_demonic_slate", has(BMItems.SLATE_DEMONIC.get()))
                .save(output, BloodMagic.rl("dusk_tool"));
    }

    private void addSoulForgeRecipes(RecipeOutput output) {
        // Petty Soul Gem - redstone dust, gold ingot, glass, lapis gem
        ForgeRecipeBuilder.build(BMItems.SOUL_GEM_PETTY.get())
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(Tags.Items.GLASS_BLOCKS)
                .requires(Tags.Items.GEMS_LAPIS)
                .minWill(1)
                .drain(1)
                .unlockedBy("has_raw_will", has(BMItems.RAW_WILL.get()))
                .save(output, BloodMagic.rl("soul_gem_petty"));

        // Lesser Soul Gem - petty gem, diamond, redstone block, lapis block
        ForgeRecipeBuilder.build(BMItems.SOUL_GEM_LESSER.get())
                .requires(BMItems.SOUL_GEM_PETTY.get())
                .requires(Tags.Items.GEMS_DIAMOND)
                .requires(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .requires(Tags.Items.STORAGE_BLOCKS_LAPIS)
                .minWill(60)
                .drain(20)
                .unlockedBy("has_petty_gem", has(BMItems.SOUL_GEM_PETTY.get()))
                .save(output, BloodMagic.rl("soul_gem_lesser"));

        // Common Soul Gem - lesser gem, diamond, gold block, imbued slate
        ForgeRecipeBuilder.build(BMItems.SOUL_GEM_COMMON.get())
                .requires(BMItems.SOUL_GEM_LESSER.get())
                .requires(Tags.Items.GEMS_DIAMOND)
                .requires(Tags.Items.STORAGE_BLOCKS_GOLD)
                .requires(BMItems.SLATE_IMBUED.get())
                .minWill(240)
                .drain(50)
                .unlockedBy("has_lesser_gem", has(BMItems.SOUL_GEM_LESSER.get()))
                .save(output, BloodMagic.rl("soul_gem_common"));

        // Greater Soul Gem - common gem, demonic slate, weak blood shard, demon crystal
        ForgeRecipeBuilder.build(BMItems.SOUL_GEM_GREATER.get())
                .requires(BMItems.SOUL_GEM_COMMON.get())
                .requires(BMItems.SLATE_DEMONIC.get())
                .requires(BMItems.WEAK_BLOOD_SHARD.get())
                .requires(BMTags.Items.DEMON_CRYSTALS)
                .minWill(1000)
                .drain(100)
                .unlockedBy("has_common_gem", has(BMItems.SOUL_GEM_COMMON.get()))
                .save(output, BloodMagic.rl("soul_gem_greater"));

        // Note: Grand Soul Gem doesn't exist in 1.20.1 - removed

        // ARC Block (shaped crafting recipe)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BMBlocks.ARC_BLOCK.block().get())
                .pattern("sss")
                .pattern("SoS")
                .pattern("IfI")
                .define('s', Tags.Items.STONES)
                .define('S', BMItems.SLATE_IMBUED.get())
                .define('o', BMItems.ORB_MAGICIAN.get())
                .define('I', Items.IRON_BLOCK)
                .define('f', Items.FURNACE)
                .unlockedBy("has_magician_orb", has(BMItems.ORB_MAGICIAN.get()))
                .save(output, BloodMagic.rl("arc_block"));

        // Blood Tank
        ForgeRecipeBuilder.build(BMBlocks.BLOOD_TANK.block().get())
                .requires(Items.GLASS, 3)
                .requires(BMBlocks.BLOODSTONE.block().get())
                .minWill(30)
                .drain(5)
                .unlockedBy("has_bloodstone", has(BMBlocks.BLOODSTONE.block().get()))
                .save(output, BloodMagic.rl("blood_tank"));

        // Living Station
        ForgeRecipeBuilder.build(BMBlocks.LIVING_STATION.block().get())
                .requires(BMBlocks.BLOODSTONE.block().get(), 2)
                .requires(Items.STONE, 2)
                .minWill(50)
                .drain(10)
                .unlockedBy("has_bloodstone", has(BMBlocks.BLOODSTONE.block().get()))
                .save(output, BloodMagic.rl("living_station"));

        // Training Bracelet
        ForgeRecipeBuilder.build(BMItems.TRAINING_BRACELET.get())
                .requires(Items.GOLD_INGOT, 2)
                .requires(Items.STRING)
                .requires(BMItems.RAW_WILL.get())
                .minWill(10)
                .drain(5)
                .unlockedBy("has_raw_will", has(BMItems.RAW_WILL.get()))
                .save(output, BloodMagic.rl("training_bracelet"));

        // Sentient Tools
        ForgeRecipeBuilder.build(BMItems.SENTIENT_SWORD.get())
                .requires(BMItems.SOUL_GEM_PETTY.get())
                .requires(Items.IRON_SWORD)
                .minWill(0)
                .drain(0)
                .unlockedBy("has_petty_gem", has(BMItems.SOUL_GEM_PETTY.get()))
                .save(output, BloodMagic.rl("sentient_sword"));

        ForgeRecipeBuilder.build(BMItems.SENTIENT_AXE.get())
                .requires(BMItems.SOUL_GEM_PETTY.get())
                .requires(Items.IRON_AXE)
                .minWill(0)
                .drain(0)
                .unlockedBy("has_petty_gem", has(BMItems.SOUL_GEM_PETTY.get()))
                .save(output, BloodMagic.rl("sentient_axe"));

        ForgeRecipeBuilder.build(BMItems.SENTIENT_PICKAXE.get())
                .requires(BMItems.SOUL_GEM_PETTY.get())
                .requires(Items.IRON_PICKAXE)
                .minWill(0)
                .drain(0)
                .unlockedBy("has_petty_gem", has(BMItems.SOUL_GEM_PETTY.get()))
                .save(output, BloodMagic.rl("sentient_pickaxe"));

        ForgeRecipeBuilder.build(BMItems.SENTIENT_SHOVEL.get())
                .requires(BMItems.SOUL_GEM_PETTY.get())
                .requires(Items.IRON_SHOVEL)
                .minWill(0)
                .drain(0)
                .unlockedBy("has_petty_gem", has(BMItems.SOUL_GEM_PETTY.get()))
                .save(output, BloodMagic.rl("sentient_shovel"));

        ForgeRecipeBuilder.build(BMItems.SENTIENT_SCYTHE.get())
                .requires(BMItems.SOUL_GEM_PETTY.get())
                .requires(Items.IRON_HOE)
                .minWill(0)
                .drain(0)
                .unlockedBy("has_petty_gem", has(BMItems.SOUL_GEM_PETTY.get()))
                .save(output, BloodMagic.rl("sentient_scythe"));

        // Demon Will Blocks
        ForgeRecipeBuilder.build(BMBlocks.DEMON_CRUCIBLE.block().get())
                .requires(Items.CAULDRON)
                .requires(Tags.Items.STONES)
                .requires(Tags.Items.GEMS_LAPIS)
                .requires(Tags.Items.GEMS_DIAMOND)
                .minWill(400)
                .drain(100)
                .unlockedBy("has_common_gem", has(BMItems.SOUL_GEM_COMMON.get()))
                .save(output, BloodMagic.rl("demon_crucible"));

        ForgeRecipeBuilder.build(BMBlocks.DEMON_CRYSTALLIZER.block().get())
                .requires(BMBlocks.HELLFIRE_FORGE.block().get())
                .requires(Tags.Items.STONES)
                .requires(Tags.Items.GEMS_LAPIS)
                .requires(Tags.Items.GLASS_BLOCKS)
                .minWill(500)
                .drain(100)
                .unlockedBy("has_hellfire_forge", has(BMBlocks.HELLFIRE_FORGE.block().get()))
                .save(output, BloodMagic.rl("demon_crystallizer"));

        ForgeRecipeBuilder.build(BMBlocks.DEMON_PYLON.block().get())
                .requires(BMTags.Items.DEMON_CRYSTALS)
                .requires(Tags.Items.STONES)
                .requires(Tags.Items.GEMS_LAPIS)
                .requires(Tags.Items.STORAGE_BLOCKS_IRON)
                .minWill(400)
                .drain(50)
                .unlockedBy("has_demon_crystal", has(BMItems.RAW_CRYSTAL.get()))
                .save(output, BloodMagic.rl("demon_pylon"));

        // Crystal Blocks
        ForgeRecipeBuilder.build(BMBlocks.RAW_CRYSTAL_BLOCK.block().get())
                .requires(BMItems.RAW_CRYSTAL.get(), 4)
                .minWill(1200)
                .drain(100)
                .unlockedBy("has_raw_crystal", has(BMItems.RAW_CRYSTAL.get()))
                .save(output, BloodMagic.rl("raw_crystal_block"));

        ForgeRecipeBuilder.build(BMBlocks.CORROSIVE_CRYSTAL_BLOCK.block().get())
                .requires(BMItems.CORROSIVE_CRYSTAL.get(), 4)
                .minWill(1200)
                .drain(100)
                .unlockedBy("has_corrosive_crystal", has(BMItems.CORROSIVE_CRYSTAL.get()))
                .save(output, BloodMagic.rl("corrosive_crystal_block"));

        ForgeRecipeBuilder.build(BMBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.block().get())
                .requires(BMItems.DESTRUCTIVE_CRYSTAL.get(), 4)
                .minWill(1200)
                .drain(100)
                .unlockedBy("has_destructive_crystal", has(BMItems.DESTRUCTIVE_CRYSTAL.get()))
                .save(output, BloodMagic.rl("destructive_crystal_block"));

        ForgeRecipeBuilder.build(BMBlocks.VENGEFUL_CRYSTAL_BLOCK.block().get())
                .requires(BMItems.VENGEFUL_CRYSTAL.get(), 4)
                .minWill(1200)
                .drain(100)
                .unlockedBy("has_vengeful_crystal", has(BMItems.VENGEFUL_CRYSTAL.get()))
                .save(output, BloodMagic.rl("vengeful_crystal_block"));

        ForgeRecipeBuilder.build(BMBlocks.STEADFAST_CRYSTAL_BLOCK.block().get())
                .requires(BMItems.STEADFAST_CRYSTAL.get(), 4)
                .minWill(1200)
                .drain(100)
                .unlockedBy("has_steadfast_crystal", has(BMItems.STEADFAST_CRYSTAL.get()))
                .save(output, BloodMagic.rl("steadfast_crystal_block"));

        // Routing Nodes
        ForgeRecipeBuilder.build(BMBlocks.ROUTING_NODE.block().get())
                .requires(Ingredient.of(Tags.Items.STONES), 2)
                .requires(Tags.Items.INGOTS_IRON)
                .requires(Tags.Items.GLASS_BLOCKS)
                .minWill(100)
                .drain(5)
                .unlockedBy("has_lesser_gem", has(BMItems.SOUL_GEM_LESSER.get()))
                .save(output, BloodMagic.rl("routing_node"));

        ForgeRecipeBuilder.build(BMBlocks.INPUT_ROUTING_NODE.block().get())
                .requires(BMBlocks.ROUTING_NODE.block().get())
                .requires(Items.HOPPER)
                .minWill(200)
                .drain(10)
                .unlockedBy("has_routing_node", has(BMBlocks.ROUTING_NODE.block().get()))
                .save(output, BloodMagic.rl("input_routing_node"));

        ForgeRecipeBuilder.build(BMBlocks.OUTPUT_ROUTING_NODE.block().get())
                .requires(BMBlocks.ROUTING_NODE.block().get())
                .requires(Items.DISPENSER)
                .minWill(200)
                .drain(10)
                .unlockedBy("has_routing_node", has(BMBlocks.ROUTING_NODE.block().get()))
                .save(output, BloodMagic.rl("output_routing_node"));

        ForgeRecipeBuilder.build(BMBlocks.MASTER_ROUTING_NODE.block().get())
                .requires(BMBlocks.ROUTING_NODE.block().get())
                .requires(Tags.Items.GEMS_DIAMOND)
                .requires(Tags.Items.STORAGE_BLOCKS_LAPIS)
                .minWill(400)
                .drain(25)
                .unlockedBy("has_routing_node", has(BMBlocks.ROUTING_NODE.block().get()))
                .save(output, BloodMagic.rl("master_routing_node"));

        // Node Upgrades
        ForgeRecipeBuilder.build(BMItems.MASTER_NODE_UPGRADE.get())
                .requires(Ingredient.of(Tags.Items.INGOTS_IRON), 2)
                .requires(Tags.Items.GLASS_BLOCKS)
                .requires(Tags.Items.STORAGE_BLOCKS_LAPIS)
                .minWill(400)
                .drain(50)
                .unlockedBy("has_master_routing_node", has(BMBlocks.MASTER_ROUTING_NODE.block().get()))
                .save(output, BloodMagic.rl("master_node_upgrade"));

        ForgeRecipeBuilder.build(BMItems.MASTER_NODE_UPGRADE_SPEED.get())
                .requires(Ingredient.of(Tags.Items.INGOTS_GOLD), 2)
                .requires(Tags.Items.GLASS_BLOCKS)
                .requires(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .minWill(400)
                .drain(50)
                .unlockedBy("has_master_routing_node", has(BMBlocks.MASTER_ROUTING_NODE.block().get()))
                .save(output, BloodMagic.rl("master_node_upgrade_speed"));

        // Node Router
        ForgeRecipeBuilder.build(BMItems.NODE_ROUTER.get())
                .requires(Ingredient.of(Tags.Items.STONES), 2)
                .requires(Tags.Items.INGOTS_IRON)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .minWill(50)
                .drain(5)
                .unlockedBy("has_routing_node", has(BMBlocks.ROUTING_NODE.block().get()))
                .save(output, BloodMagic.rl("node_router"));

        // Demon Will Gauge
        ForgeRecipeBuilder.build(BMItems.DEMON_WILL_GAUGE.get())
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.GLASS_BLOCKS)
                .requires(BMTags.Items.DEMON_CRYSTALS)
                .minWill(400)
                .drain(50)
                .unlockedBy("has_demon_crystal", has(BMItems.RAW_CRYSTAL.get()))
                .save(output, BloodMagic.rl("demon_will_gauge"));

        // Sanguine Reverter (soul forge recipe)
        ForgeRecipeBuilder.build(BMItems.SANGUINE_REVERTER.get())
                .requires(Items.SHEARS)
                .requires(Tags.Items.STONES)
                .requires(BMItems.SLATE_IMBUED.get())
                .requires(Tags.Items.INGOTS_IRON)
                .minWill(350)
                .drain(30)
                .unlockedBy("has_imbued_slate", has(BMItems.SLATE_IMBUED.get()))
                .save(output, BloodMagic.rl("sanguine_reverter"));

        // Resonator (soul forge recipe)
        ForgeRecipeBuilder.build(BMItems.RESONATOR.get())
                .requires(Tags.Items.STONES)
                .requires(Tags.Items.INGOTS_COPPER)
                .requires(BMItems.RAW_CRYSTAL.get())
                .minWill(1200)
                .drain(100)
                .unlockedBy("has_raw_crystal", has(BMItems.RAW_CRYSTAL.get()))
                .save(output, BloodMagic.rl("resonator"));

        // Primitive Crystalline Resonator
        ForgeRecipeBuilder.build(BMItems.PRIMITIVE_CRYSTALLINE_RESONATOR.get())
                .requires(Tags.Items.GEMS_AMETHYST)
                .requires(Tags.Items.INGOTS)
                .requires(BMItems.RAW_CRYSTAL.get())
                .requires(BMItems.TAU_OIL.get())
                .minWill(1200)
                .drain(200)
                .unlockedBy("has_tau_oil", has(BMItems.TAU_OIL.get()))
                .save(output, BloodMagic.rl("primitive_resonator"));

        // Hellforged Resonator
        ForgeRecipeBuilder.build(BMItems.HELLFORGED_RESONATOR.get())
                .requires(Tags.Items.GEMS_AMETHYST)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(BMItems.RAW_CRYSTAL.get())
                .requires(BMItems.HELLFORGED_INGOT.get())
                .minWill(1200)
                .drain(400)
                .unlockedBy("has_hellforged_ingot", has(BMItems.HELLFORGED_INGOT.get()))
                .save(output, BloodMagic.rl("hellforged_resonator"));

        // Throwing Daggers
        ForgeRecipeBuilder.build(BMItems.THROWING_DAGGER.get())
                .requires(Tags.Items.INGOTS_IRON)
                .requires(Tags.Items.RODS_WOODEN)
                .minWill(100)
                .drain(5)
                .unlockedBy("has_lesser_gem", has(BMItems.SOUL_GEM_LESSER.get()))
                .save(output, BloodMagic.rl("throwing_dagger"));

        ForgeRecipeBuilder.build(BMItems.THROWING_DAGGER_AMETHYST.get())
                .requires(Tags.Items.INGOTS_COPPER)
                .requires(Tags.Items.GEMS_AMETHYST)
                .minWill(100)
                .drain(5)
                .unlockedBy("has_lesser_gem", has(BMItems.SOUL_GEM_LESSER.get()))
                .save(output, BloodMagic.rl("throwing_dagger_copper"));

        ForgeRecipeBuilder.build(BMItems.THROWING_DAGGER_SYRINGE.get())
                .requires(BMItems.THROWING_DAGGER_AMETHYST.get())
                .requires(Items.GLASS_BOTTLE)
                .minWill(200)
                .drain(10)
                .unlockedBy("has_amethyst_dagger", has(BMItems.THROWING_DAGGER_AMETHYST.get()))
                .save(output, BloodMagic.rl("throwing_dagger_syringe"));

        // Keys
        ForgeRecipeBuilder.build(BMItems.SIMPLE_KEY.get())
                .requires(Ingredient.of(Tags.Items.INGOTS_IRON), 2)
                .requires(Tags.Items.NUGGETS_GOLD)
                .minWill(100)
                .drain(10)
                .unlockedBy("has_lesser_gem", has(BMItems.SOUL_GEM_LESSER.get()))
                .save(output, BloodMagic.rl("simple_key"));

        ForgeRecipeBuilder.build(BMItems.MINE_KEY.get())
                .requires(Ingredient.of(Tags.Items.INGOTS_GOLD), 2)
                .requires(Tags.Items.GEMS_DIAMOND)
                .minWill(200)
                .drain(25)
                .unlockedBy("has_common_gem", has(BMItems.SOUL_GEM_COMMON.get()))
                .save(output, BloodMagic.rl("mine_key"));

        // Crystal Catalysts
        ForgeRecipeBuilder.build(BMItems.RAW_CRYSTAL_CATALYST.get())
                .requires(BMItems.RAW_CRYSTAL.get())
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .minWill(500)
                .drain(50)
                .unlockedBy("has_raw_crystal", has(BMItems.RAW_CRYSTAL.get()))
                .save(output, BloodMagic.rl("raw_catalyst"));

        ForgeRecipeBuilder.build(BMItems.CORROSIVE_CRYSTAL_CATALYST.get())
                .requires(BMItems.CORROSIVE_CRYSTAL.get())
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .minWill(500)
                .drain(50)
                .unlockedBy("has_corrosive_crystal", has(BMItems.CORROSIVE_CRYSTAL.get()))
                .save(output, BloodMagic.rl("corrosive_catalyst"));

        ForgeRecipeBuilder.build(BMItems.DESTRUCTIVE_CRYSTAL_CATALYST.get())
                .requires(BMItems.DESTRUCTIVE_CRYSTAL.get())
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .minWill(500)
                .drain(50)
                .unlockedBy("has_destructive_crystal", has(BMItems.DESTRUCTIVE_CRYSTAL.get()))
                .save(output, BloodMagic.rl("destructive_catalyst"));

        ForgeRecipeBuilder.build(BMItems.VENGEFUL_CRYSTAL_CATALYST.get())
                .requires(BMItems.VENGEFUL_CRYSTAL.get())
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .minWill(500)
                .drain(50)
                .unlockedBy("has_vengeful_crystal", has(BMItems.VENGEFUL_CRYSTAL.get()))
                .save(output, BloodMagic.rl("vengeful_catalyst"));

        ForgeRecipeBuilder.build(BMItems.STEADFAST_CRYSTAL_CATALYST.get())
                .requires(BMItems.STEADFAST_CRYSTAL.get())
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .minWill(500)
                .drain(50)
                .unlockedBy("has_steadfast_crystal", has(BMItems.STEADFAST_CRYSTAL.get()))
                .save(output, BloodMagic.rl("steadfast_catalyst"));

        // Explosive Charges
        ForgeRecipeBuilder.build(BMBlocks.SHAPED_CHARGE.item().get(), 8)
                .requires(Tags.Items.COBBLESTONES)
                .requires(Items.CHARCOAL)
                .requires(Tags.Items.SANDS)
                .requires(Tags.Items.STONES)
                .minWill(10)
                .drain(0.5)
                .unlockedBy("has_charcoal", has(Items.CHARCOAL))
                .save(output, BloodMagic.rl("shaped_charge"));

        ForgeRecipeBuilder.build(BMBlocks.DEFORESTER_CHARGE.item().get(), 8)
                .requires(Tags.Items.COBBLESTONES)
                .requires(Items.CHARCOAL)
                .requires(ItemTags.LOGS)
                .requires(ItemTags.PLANKS)
                .minWill(10)
                .drain(0.5)
                .unlockedBy("has_charcoal", has(Items.CHARCOAL))
                .save(output, BloodMagic.rl("deforester_charge"));

        ForgeRecipeBuilder.build(BMBlocks.VEINMINE_CHARGE.item().get(), 8)
                .requires(Tags.Items.COBBLESTONES)
                .requires(Items.CHARCOAL)
                .requires(Tags.Items.SANDSTONE_BLOCKS)
                .requires(Tags.Items.SANDS)
                .minWill(10)
                .drain(0.5)
                .unlockedBy("has_charcoal", has(Items.CHARCOAL))
                .save(output, BloodMagic.rl("veinmine_charge"));

        ForgeRecipeBuilder.build(BMBlocks.FUNGAL_CHARGE.item().get(), 8)
                .requires(Tags.Items.COBBLESTONES)
                .requires(Items.CHARCOAL)
                .requires(ItemTags.CRIMSON_STEMS)
                .requires(Tags.Items.MUSHROOMS)
                .minWill(10)
                .drain(0.5)
                .unlockedBy("has_charcoal", has(Items.CHARCOAL))
                .save(output, BloodMagic.rl("fungal_charge"));

        // Tier 2 charges
        ForgeRecipeBuilder.build(BMBlocks.AUG_SHAPED_CHARGE.item().get(), 6)
                .requires(Tags.Items.STORAGE_BLOCKS_COPPER)
                .requires(Items.CHARCOAL)
                .requires(Tags.Items.SANDS)
                .requires(Items.BRICK)
                .minWill(80)
                .drain(2.5)
                .unlockedBy("has_copper_block", has(Tags.Items.STORAGE_BLOCKS_COPPER))
                .save(output, BloodMagic.rl("aug_shaped_charge"));

        ForgeRecipeBuilder.build(BMBlocks.DEFORESTER_CHARGE_2.item().get(), 4)
                .requires(Tags.Items.STORAGE_BLOCKS_COPPER)
                .requires(Items.CHARCOAL)
                .requires(ItemTags.LOGS)
                .requires(ItemTags.PLANKS)
                .minWill(80)
                .drain(2.5)
                .unlockedBy("has_copper_block", has(Tags.Items.STORAGE_BLOCKS_COPPER))
                .save(output, BloodMagic.rl("deforester_charge_2"));

        ForgeRecipeBuilder.build(BMBlocks.VEINMINE_CHARGE_2.item().get(), 4)
                .requires(Tags.Items.STORAGE_BLOCKS_COPPER)
                .requires(Items.CHARCOAL)
                .requires(Tags.Items.SANDSTONE_BLOCKS)
                .requires(Tags.Items.SANDS)
                .minWill(80)
                .drain(2.5)
                .unlockedBy("has_copper_block", has(Tags.Items.STORAGE_BLOCKS_COPPER))
                .save(output, BloodMagic.rl("veinmine_charge_2"));

        ForgeRecipeBuilder.build(BMBlocks.FUNGAL_CHARGE_2.item().get(), 4)
                .requires(Tags.Items.STORAGE_BLOCKS_COPPER)
                .requires(Items.CHARCOAL)
                .requires(ItemTags.CRIMSON_STEMS)
                .requires(Tags.Items.MUSHROOMS)
                .minWill(80)
                .drain(2.5)
                .unlockedBy("has_copper_block", has(Tags.Items.STORAGE_BLOCKS_COPPER))
                .save(output, BloodMagic.rl("fungal_charge_2"));

        ForgeRecipeBuilder.build(BMBlocks.SHAPED_CHARGE_DEEP.item().get(), 4)
                .requires(Tags.Items.STORAGE_BLOCKS_COPPER)
                .requires(Items.CHARCOAL)
                .requires(Tags.Items.SANDS)
                .requires(Tags.Items.STONES)
                .minWill(80)
                .drain(2.5)
                .unlockedBy("has_copper_block", has(Tags.Items.STORAGE_BLOCKS_COPPER))
                .save(output, BloodMagic.rl("shaped_charge_deep"));
    }

    // Helper methods

    private void addRuneRecipe(RecipeOutput output, ItemLike result, ItemLike catalyst, ItemLike orb) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("scs")
                .pattern("coc")
                .pattern("srs")
                .define('s', Items.STONE)
                .define('c', catalyst)
                .define('o', orb)
                .define('r', BMBlocks.RUNE_BLANK.block().get())
                .unlockedBy("has_orb", has(orb))
                .save(output);
    }

    private void addRuneRecipeWithBucket(RecipeOutput output, ItemLike result, ItemLike catalyst, ItemLike orb) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("scs")
                .pattern("ror")
                .pattern("srs")
                .define('s', Items.STONE)
                .define('c', catalyst)
                .define('o', orb)
                .define('r', BMBlocks.RUNE_BLANK.block().get())
                .unlockedBy("has_orb", has(orb))
                .save(output);
    }

    private void addTier2RuneRecipe(RecipeOutput output, ItemLike result, ItemLike tier1Rune) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("nbn")
                .pattern("brb")
                .pattern("nbn")
                .define('n', Items.NETHERITE_SCRAP)
                .define('b', BMBlocks.BLOODSTONE.block().get())
                .define('r', tier1Rune)
                .unlockedBy("has_tier1_rune", has(tier1Rune))
                .save(output);
    }

    private void addAlchemyArrayRecipes(RecipeOutput output) {
        // Divination Sigil - base: redstone, added: blank slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_DIVINATION.get())
                .base(Items.REDSTONE)
                .added(BMItems.SLATE_BLANK.get())
                .texture("textures/models/alchemyarrays/divinationsigil.png")
                .save(output, "divination_sigil");

        // Seer Sigil - base: reagent_sight, added: reinforced slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_SEER.get())
                .base(BMItems.REAGENT_SIGHT.get())
                .added(BMItems.SLATE_REINFORCED.get())
                .texture("textures/models/alchemyarrays/sightsigil.png")
                .save(output, "seer_sigil");

        // Water Sigil - base: reagent_water, added: blank slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_WATER.get())
                .base(BMItems.REAGENT_WATER.get())
                .added(BMItems.SLATE_BLANK.get())
                .texture("textures/models/alchemyarrays/watersigil.png")
                .save(output, "water_sigil");

        // Lava Sigil - base: reagent_lava, added: blank slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_LAVA.get())
                .base(BMItems.REAGENT_LAVA.get())
                .added(BMItems.SLATE_BLANK.get())
                .texture("textures/models/alchemyarrays/lavasigil.png")
                .save(output, "lava_sigil");

        // Void Sigil - base: reagent_void, added: reinforced slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_VOID.get())
                .base(BMItems.REAGENT_VOID.get())
                .added(BMItems.SLATE_REINFORCED.get())
                .texture("textures/models/alchemyarrays/voidsigil.png")
                .save(output, "void_sigil");

        // Green Grove Sigil - base: reagent_growth, added: reinforced slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_GREEN_GROVE.get())
                .base(BMItems.REAGENT_GROWTH.get())
                .added(BMItems.SLATE_REINFORCED.get())
                .texture("textures/models/alchemyarrays/growthsigil.png")
                .save(output, "green_grove_sigil");

        // Fast Miner Sigil - base: reagent_fast_miner, added: reinforced slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_FAST_MINER.get())
                .base(BMItems.REAGENT_FAST_MINER.get())
                .added(BMItems.SLATE_REINFORCED.get())
                .texture("textures/models/alchemyarrays/fastminersigil.png")
                .save(output, "fast_miner_sigil");

        // Air Sigil - base: reagent_air, added: reinforced slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_AIR.get())
                .base(BMItems.REAGENT_AIR.get())
                .added(BMItems.SLATE_REINFORCED.get())
                .texture("textures/models/alchemyarrays/airsigil.png")
                .save(output, "air_sigil");

        // Blood Light Sigil - base: reagent_blood_light, added: imbued slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_BLOOD_LIGHT.get())
                .base(BMItems.REAGENT_BLOOD_LIGHT.get())
                .added(BMItems.SLATE_IMBUED.get())
                .texture("textures/models/alchemyarrays/bloodlightsigil.png")
                .save(output, "blood_light_sigil");

        // Magnetism Sigil - base: reagent_magnetism, added: imbued slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_MAGNETISM.get())
                .base(BMItems.REAGENT_MAGNETISM.get())
                .added(BMItems.SLATE_IMBUED.get())
                .texture("textures/models/alchemyarrays/magnetismsigil.png")
                .save(output, "magnetism_sigil");

        // Holding Sigil - base: reagent_holding, added: imbued slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_HOLDING.get())
                .base(BMItems.REAGENT_HOLDING.get())
                .added(BMItems.SLATE_IMBUED.get())
                .texture("textures/models/alchemyarrays/holdingsigil.png")
                .save(output, "holding_sigil");

        // Suppression Sigil - base: reagent_suppression, added: demonic slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_SUPPRESSION.get())
                .base(BMItems.REAGENT_SUPPRESSION.get())
                .added(BMItems.SLATE_DEMONIC.get())
                .texture("textures/models/alchemyarrays/suppressionsigil.png")
                .save(output, "suppression_sigil");

        // Teleposition Sigil - base: reagent_teleposition, added: demonic slate
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_TELEPOSITION.get())
                .base(BMItems.REAGENT_TELEPOSITION.get())
                .added(BMItems.SLATE_DEMONIC.get())
                .texture("textures/models/alchemyarrays/telepositionsigil.png")
                .save(output, "teleposition_sigil");

        // Frost/Ice Sigil - base: reagent_frost (using lava bucket as temp), added: imbued slate
        // Note: Frost sigil uses special reagent that may not exist, using placeholder
        AlchemyArrayRecipeBuilder.build(BMItems.SIGIL_FROST.get())
                .base(Items.POWDER_SNOW_BUCKET)
                .added(BMItems.SLATE_IMBUED.get())
                .texture("textures/models/alchemyarrays/frostsigil.png")
                .save(output, "frost_sigil");

        // Living Armor - reagent_binding + iron armor pieces
        AlchemyArrayRecipeBuilder.build(BMItems.LIVING_HELMET.get())
                .base(BMItems.REAGENT_BINDING.get())
                .added(Items.IRON_HELMET)
                .texture("textures/models/alchemyarrays/bindingarray.png")
                .save(output, "living_helmet");

        AlchemyArrayRecipeBuilder.build(BMItems.LIVING_PLATE.get())
                .base(BMItems.REAGENT_BINDING.get())
                .added(Items.IRON_CHESTPLATE)
                .texture("textures/models/alchemyarrays/bindingarray.png")
                .save(output, "living_plate");

        AlchemyArrayRecipeBuilder.build(BMItems.LIVING_LEGGINGS.get())
                .base(BMItems.REAGENT_BINDING.get())
                .added(Items.IRON_LEGGINGS)
                .texture("textures/models/alchemyarrays/bindingarray.png")
                .save(output, "living_leggings");

        AlchemyArrayRecipeBuilder.build(BMItems.LIVING_BOOTS.get())
                .base(BMItems.REAGENT_BINDING.get())
                .added(Items.IRON_BOOTS)
                .texture("textures/models/alchemyarrays/bindingarray.png")
                .save(output, "living_boots");

        // Training Bracelet
        AlchemyArrayRecipeBuilder.build(BMItems.TRAINING_BRACELET.get())
                .base(BMItems.REAGENT_BINDING.get())
                .added(Items.DIAMOND)
                .texture("textures/models/alchemyarrays/bindingarray.png")
                .save(output, "living_trainer");
    }

    private void addAlchemyTableRecipes(RecipeOutput output) {
        // Reagent Water - sugar, water bucket x2
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_WATER.get())
                .input(Items.SUGAR)
                .input(Items.WATER_BUCKET)
                .input(Items.WATER_BUCKET)
                .syphon(300)
                .ticks(200)
                .minimumTier(1)
                .save(output, "reagent_water");

        // Reagent Lava - lava bucket, redstone dust, cobblestone, coal block
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_LAVA.get())
                .input(Items.LAVA_BUCKET)
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Ingredient.of(Tags.Items.COBBLESTONES))
                .input(Items.COAL_BLOCK)
                .syphon(1000)
                .ticks(200)
                .minimumTier(1)
                .save(output, "reagent_lava");

        // Reagent Air - ghast tear, feather x2
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_AIR.get())
                .input(Items.GHAST_TEAR)
                .input(Items.FEATHER)
                .input(Items.FEATHER)
                .syphon(2000)
                .ticks(200)
                .minimumTier(2)
                .save(output, "reagent_air");

        // Reagent Void - ender pearl, obsidian, bucket
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_VOID.get())
                .input(Items.ENDER_PEARL)
                .input(Items.OBSIDIAN)
                .input(Items.BUCKET)
                .syphon(1000)
                .ticks(200)
                .minimumTier(2)
                .save(output, "reagent_void");

        // Reagent Growth - sugar, bonemeal, sapling, tall grass
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_GROWTH.get())
                .input(Items.SUGAR)
                .input(Items.BONE_MEAL)
                .input(Ingredient.of(ItemTags.SAPLINGS))
                .input(Items.SHORT_GRASS)
                .syphon(1000)
                .ticks(200)
                .minimumTier(2)
                .save(output, "reagent_growth");

        // Reagent Fast Miner - gold nugget, iron pickaxe, iron shovel
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_FAST_MINER.get())
                .input(Ingredient.of(Tags.Items.NUGGETS_GOLD))
                .input(Items.IRON_PICKAXE)
                .input(Items.IRON_SHOVEL)
                .syphon(2000)
                .ticks(200)
                .minimumTier(2)
                .save(output, "reagent_fast_miner");

        // Reagent Magnetism - gold ingot, gold block, iron block
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_MAGNETISM.get())
                .input(Ingredient.of(Tags.Items.INGOTS_GOLD))
                .input(Items.GOLD_BLOCK)
                .input(Items.IRON_BLOCK)
                .syphon(3000)
                .ticks(200)
                .minimumTier(3)
                .save(output, "reagent_magnetism");

        // Reagent Blood Light - torch, glowstone dust, redstone
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_BLOOD_LIGHT.get())
                .input(Items.TORCH)
                .input(Ingredient.of(Tags.Items.DUSTS_GLOWSTONE))
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .syphon(1000)
                .ticks(200)
                .minimumTier(3)
                .save(output, "reagent_blood_light");

        // Reagent Sight - glass x2, divination sigil
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_SIGHT.get())
                .input(Ingredient.of(Tags.Items.GLASS_BLOCKS))
                .input(Ingredient.of(Tags.Items.GLASS_BLOCKS))
                .input(BMItems.SIGIL_DIVINATION.get())
                .syphon(500)
                .ticks(200)
                .minimumTier(2)
                .save(output, "reagent_sight");

        // Reagent Binding - skeleton skull, leather
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_BINDING.get())
                .input(Items.SKELETON_SKULL)
                .input(Items.LEATHER)
                .syphon(2000)
                .ticks(200)
                .minimumTier(2)
                .save(output, "reagent_binding");

        // Reagent Holding - chest, leather, string
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_HOLDING.get())
                .input(Ingredient.of(Tags.Items.CHESTS))
                .input(Items.LEATHER)
                .input(Ingredient.of(Tags.Items.STRINGS))
                .syphon(2000)
                .ticks(200)
                .minimumTier(3)
                .save(output, "reagent_holding");

        // Reagent Suppression - water bucket, lava bucket, nether star
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_SUPPRESSION.get())
                .input(Items.WATER_BUCKET)
                .input(Items.LAVA_BUCKET)
                .input(Items.NETHER_STAR)
                .syphon(5000)
                .ticks(200)
                .minimumTier(4)
                .save(output, "reagent_suppression");

        // Reagent Teleposition - ender pearl, diamond, soul snare
        AlchemyTableRecipeBuilder.build(BMItems.REAGENT_TELEPOSITION.get())
                .input(Items.ENDER_PEARL)
                .input(Ingredient.of(Tags.Items.GEMS_DIAMOND))
                .input(BMItems.SOUL_SNARE.get())
                .syphon(3000)
                .ticks(200)
                .minimumTier(4)
                .save(output, "reagent_teleposition");

        // Arcane Ash - redstone x4, bone x2, coal
        AlchemyTableRecipeBuilder.build(BMItems.ARCANE_ASHES.get())
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Ingredient.of(Tags.Items.BONES))
                .input(Items.COAL)
                .syphon(200)
                .ticks(200)
                .minimumTier(1)
                .save(output, "arcane_ash");

        // Tau Oil - 3x weak tau + bone meal
        AlchemyTableRecipeBuilder.build(BMItems.TAU_OIL.get())
                .input(BMBlocks.WEAK_TAU.item().get())
                .input(BMBlocks.WEAK_TAU.item().get())
                .input(BMBlocks.WEAK_TAU.item().get())
                .input(Items.BONE_MEAL)
                .syphon(500)
                .ticks(200)
                .minimumTier(3)
                .save(output, "tau_oil");

        // Utility recipes
        // Leather from rotten flesh
        AlchemyTableRecipeBuilder.build(new ItemStack(Items.LEATHER, 4))
                .input(Items.ROTTEN_FLESH)
                .input(Items.ROTTEN_FLESH)
                .input(Items.ROTTEN_FLESH)
                .input(Items.ROTTEN_FLESH)
                .input(Items.FLINT)
                .input(Items.WATER_BUCKET)
                .syphon(100)
                .ticks(200)
                .minimumTier(1)
                .save(output, "leather_from_flesh");

        // String from wool
        AlchemyTableRecipeBuilder.build(new ItemStack(Items.STRING, 4))
                .input(Ingredient.of(ItemTags.WOOL))
                .input(Items.FLINT)
                .syphon(100)
                .ticks(100)
                .minimumTier(0)
                .save(output, "string");

        // Flint duplication
        AlchemyTableRecipeBuilder.build(new ItemStack(Items.FLINT, 2))
                .input(Items.GRAVEL)
                .input(Items.FLINT)
                .syphon(50)
                .ticks(20)
                .minimumTier(0)
                .save(output, "flint_from_gravel");

        // Bread from wheat
        AlchemyTableRecipeBuilder.build(Items.BREAD)
                .input(Ingredient.of(Tags.Items.CROPS_WHEAT))
                .input(Items.SUGAR)
                .syphon(100)
                .ticks(100)
                .minimumTier(1)
                .save(output, "bread");

        // Explosive Powder - gunpowder x2 + coal
        AlchemyTableRecipeBuilder.build(BMItems.EXPLOSIVE_POWDER.get())
                .input(Items.GUNPOWDER)
                .input(Items.GUNPOWDER)
                .input(Items.COAL)
                .syphon(500)
                .ticks(200)
                .minimumTier(1)
                .save(output, "explosive_powder");

        // Sulfur from lava bucket + cobblestone
        AlchemyTableRecipeBuilder.build(new ItemStack(BMItems.SULFUR.get(), 4))
                .input(Items.LAVA_BUCKET)
                .input(Ingredient.of(Tags.Items.COBBLESTONES))
                .syphon(200)
                .ticks(100)
                .minimumTier(0)
                .save(output, "sulfur_from_lava");

        // Saltpeter from plant oil x2 + coal dust
        AlchemyTableRecipeBuilder.build(new ItemStack(BMItems.SALTPETER.get(), 3))
                .input(BMItems.PLANT_OIL.get())
                .input(BMItems.PLANT_OIL.get())
                .input(Ingredient.of(BMTags.Items.DUSTS_COAL))
                .syphon(200)
                .ticks(200)
                .minimumTier(1)
                .save(output, "saltpeter");

        // Gunpowder from sulfur + saltpeter + coal
        AlchemyTableRecipeBuilder.build(new ItemStack(Items.GUNPOWDER, 3))
                .input(Ingredient.of(BMTags.Items.DUSTS_SULFUR))
                .input(Ingredient.of(BMTags.Items.DUSTS_SALTPETER))
                .input(Ingredient.of(ItemTags.COALS))
                .syphon(0)
                .ticks(100)
                .minimumTier(0)
                .save(output, "gunpowder");

        // Plant Oil recipes - from various crops
        AlchemyTableRecipeBuilder.build(BMItems.PLANT_OIL.get())
                .input(Ingredient.of(Tags.Items.CROPS_CARROT))
                .input(Ingredient.of(Tags.Items.CROPS_CARROT))
                .input(Ingredient.of(Tags.Items.CROPS_CARROT))
                .input(Items.BONE_MEAL)
                .syphon(100)
                .ticks(100)
                .minimumTier(1)
                .save(output, "plantoil_from_carrots");

        AlchemyTableRecipeBuilder.build(BMItems.PLANT_OIL.get())
                .input(Ingredient.of(Tags.Items.CROPS_POTATO))
                .input(Ingredient.of(Tags.Items.CROPS_POTATO))
                .input(Items.BONE_MEAL)
                .syphon(100)
                .ticks(100)
                .minimumTier(1)
                .save(output, "plantoil_from_potatoes");

        AlchemyTableRecipeBuilder.build(BMItems.PLANT_OIL.get())
                .input(Ingredient.of(Tags.Items.CROPS_WHEAT))
                .input(Ingredient.of(Tags.Items.CROPS_WHEAT))
                .input(Items.BONE_MEAL)
                .syphon(100)
                .ticks(100)
                .minimumTier(1)
                .save(output, "plantoil_from_wheat");

        AlchemyTableRecipeBuilder.build(BMItems.PLANT_OIL.get())
                .input(Ingredient.of(Tags.Items.CROPS_BEETROOT))
                .input(Ingredient.of(Tags.Items.CROPS_BEETROOT))
                .input(Ingredient.of(Tags.Items.CROPS_BEETROOT))
                .input(Items.BONE_MEAL)
                .syphon(100)
                .ticks(100)
                .minimumTier(1)
                .save(output, "plantoil_from_beets");

        // Basic Cutting Fluid - plant oil + redstone + gunpowder + sugar + coal dust + water
        AlchemyTableRecipeBuilder.build(BMItems.BASIC_CUTTING_FLUID.get())
                .input(BMItems.PLANT_OIL.get())
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Items.GUNPOWDER)
                .input(Items.SUGAR)
                .input(Ingredient.of(BMTags.Items.DUSTS_COAL))
                .input(Items.WATER_BUCKET)
                .syphon(1000)
                .ticks(200)
                .minimumTier(1)
                .save(output, "basic_cutting_fluid");

        // Slate Vial - blank slate + 5 glass
        AlchemyTableRecipeBuilder.build(new ItemStack(BMItems.SLATE_VIAL.get(), 8))
                .input(BMItems.SLATE_BLANK.get())
                .input(Ingredient.of(Tags.Items.GLASS_BLOCKS))
                .input(Ingredient.of(Tags.Items.GLASS_BLOCKS))
                .input(Ingredient.of(Tags.Items.GLASS_BLOCKS))
                .input(Ingredient.of(Tags.Items.GLASS_BLOCKS))
                .input(Ingredient.of(Tags.Items.GLASS_BLOCKS))
                .syphon(500)
                .ticks(200)
                .minimumTier(1)
                .save(output, "slate_vial");

        // Anointment Recipes
        AlchemyTableRecipeBuilder.build(BMItems.FORTUNE_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Ingredient.of(BMTags.Items.DUSTS_COAL))
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "fortune_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.SILK_TOUCH_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Items.COBWEB)
                .input(Ingredient.of(Tags.Items.NUGGETS_GOLD))
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "silk_touch_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.MELEE_DAMAGE_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Items.BLAZE_POWDER)
                .input(Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "melee_damage_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.HOLY_WATER_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Items.GLISTERING_MELON_SLICE)
                .input(Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "holy_water_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Items.GLASS_BOTTLE)
                .input(Items.ENCHANTED_BOOK)
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "hidden_knowledge_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.QUICK_DRAW_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Ingredient.of(Tags.Items.STRINGS))
                .input(Items.SPECTRAL_ARROW)
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "quick_draw_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.LOOTING_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Ingredient.of(Tags.Items.GEMS_LAPIS))
                .input(Ingredient.of(Tags.Items.BONES))
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "looting_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.BOW_POWER_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Ingredient.of(Tags.Items.INGOTS_IRON))
                .input(Items.BOW)
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "bow_power_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.SMELTING_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Items.FURNACE)
                .input(Items.CHARCOAL)
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "smelting_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.VOIDING_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Items.NETHERRACK)
                .input(Items.COBBLED_DEEPSLATE)
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "voiding_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.BOW_VELOCITY_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Ingredient.of(Tags.Items.NUGGETS_GOLD))
                .input(Items.BOW)
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "bow_velocity_anointment");

        AlchemyTableRecipeBuilder.build(BMItems.WEAPON_REPAIR_ANOINTMENT.get())
                .input(BMItems.SLATE_VIAL.get())
                .input(Ingredient.of(Tags.Items.CROPS_NETHER_WART))
                .input(Ingredient.of(Tags.Items.INGOTS_COPPER))
                .input(Ingredient.of(BMTags.Items.DUSTS_GOLD))
                .syphon(500)
                .ticks(100)
                .minimumTier(1)
                .save(output, "weapon_repair_anointment");

        // Frame Parts and Filter Recipes
        AlchemyTableRecipeBuilder.build(new ItemStack(BMItems.FRAME_PARTS.get(), 2))
                .input(Ingredient.of(Tags.Items.GLASS_BLOCKS))
                .input(Ingredient.of(Tags.Items.STONES))
                .input(BMItems.SLATE_BLANK.get())
                .syphon(1000)
                .ticks(100)
                .minimumTier(3)
                .save(output, "component_frame_parts");

        AlchemyTableRecipeBuilder.build(BMItems.ITEM_ROUTER_FILTER.get())
                .input(BMItems.FRAME_PARTS.get())
                .input(Ingredient.of(Tags.Items.LEATHERS))
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Ingredient.of(Tags.Items.DYES_RED))
                .syphon(500)
                .ticks(100)
                .minimumTier(3)
                .save(output, "router_filter");

        AlchemyTableRecipeBuilder.build(BMItems.ITEM_TAG_FILTER.get())
                .input(BMItems.FRAME_PARTS.get())
                .input(Ingredient.of(Tags.Items.INGOTS))
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Items.CLAY_BALL)
                .syphon(500)
                .ticks(100)
                .minimumTier(3)
                .save(output, "tag_router_filter");

        AlchemyTableRecipeBuilder.build(BMItems.ITEM_MOD_FILTER.get())
                .input(BMItems.FRAME_PARTS.get())
                .input(BMItems.SLATE_REINFORCED.get())
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Ingredient.of(Tags.Items.DYES_YELLOW))
                .syphon(500)
                .ticks(100)
                .minimumTier(3)
                .save(output, "mod_router_filter");

        AlchemyTableRecipeBuilder.build(BMItems.ITEM_ENCHANT_FILTER.get())
                .input(BMItems.FRAME_PARTS.get())
                .input(Items.ENCHANTED_BOOK)
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Items.PAPER)
                .syphon(500)
                .ticks(100)
                .minimumTier(3)
                .save(output, "enchant_router_filter");

        AlchemyTableRecipeBuilder.build(BMItems.ITEM_COMPOSITE_FILTER.get())
                .input(BMItems.FRAME_PARTS.get())
                .input(Ingredient.of(Tags.Items.DUSTS_GLOWSTONE))
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(BMItems.SLATE_IMBUED.get())
                .syphon(1000)
                .ticks(200)
                .minimumTier(3)
                .save(output, "composite_router_filter");

        // Intermediate Cutting Fluid - tau oil + glowstone + gunpowder + sugar + sulfur + water
        AlchemyTableRecipeBuilder.build(BMItems.INTERMEDIATE_CUTTING_FLUID.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.DUSTS_GLOWSTONE))
                .input(Items.GUNPOWDER)
                .input(Items.SUGAR)
                .input(Ingredient.of(BMTags.Items.DUSTS_SULFUR))
                .input(Items.WATER_BUCKET)
                .syphon(2000)
                .ticks(200)
                .minimumTier(3)
                .save(output, "intermediate_cutting_fluid");

        // Advanced Cutting Fluid - tau oil + hellforged dust + glow berries + saltpeter + sulfur + water
        AlchemyTableRecipeBuilder.build(BMItems.ADVANCED_CUTTING_FLUID.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(BMTags.Items.DUSTS_HELLFORGED))
                .input(Items.GLOW_BERRIES)
                .input(BMItems.SALTPETER.get())
                .input(Ingredient.of(BMTags.Items.DUSTS_SULFUR))
                .input(Items.WATER_BUCKET)
                .syphon(4000)
                .ticks(200)
                .minimumTier(4)
                .save(output, "advanced_cutting_fluid");

        // Anointment _L variants (extended duration - use tau oil)
        AlchemyTableRecipeBuilder.build(BMItems.FORTUNE_ANOINTMENT_L.get())
                .input(BMItems.FORTUNE_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Ingredient.of(BMTags.Items.DUSTS_COAL))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "fortune_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.SILK_TOUCH_ANOINTMENT_L.get())
                .input(BMItems.SILK_TOUCH_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.COBWEB)
                .input(Ingredient.of(Tags.Items.NUGGETS_GOLD))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "silk_touch_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.MELEE_DAMAGE_ANOINTMENT_L.get())
                .input(BMItems.MELEE_DAMAGE_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.BLAZE_POWDER)
                .input(Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "melee_damage_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.HOLY_WATER_ANOINTMENT_L.get())
                .input(BMItems.HOLY_WATER_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.GLISTERING_MELON_SLICE)
                .input(Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "holy_water_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.HIDDEN_KNOWLEDGE_ANOINTMENT_L.get())
                .input(BMItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.GLASS_BOTTLE)
                .input(Items.ENCHANTED_BOOK)
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "hidden_knowledge_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.QUICK_DRAW_ANOINTMENT_L.get())
                .input(BMItems.QUICK_DRAW_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.STRINGS))
                .input(Items.SPECTRAL_ARROW)
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "quick_draw_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.LOOTING_ANOINTMENT_L.get())
                .input(BMItems.LOOTING_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.GEMS_LAPIS))
                .input(Ingredient.of(Tags.Items.BONES))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "looting_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.BOW_POWER_ANOINTMENT_L.get())
                .input(BMItems.BOW_POWER_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.INGOTS_IRON))
                .input(Items.BOW)
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "bow_power_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.SMELTING_ANOINTMENT_L.get())
                .input(BMItems.SMELTING_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.FURNACE)
                .input(Items.CHARCOAL)
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "smelting_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.VOIDING_ANOINTMENT_L.get())
                .input(BMItems.VOIDING_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.NETHERRACK)
                .input(Items.COBBLED_DEEPSLATE)
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "voiding_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.BOW_VELOCITY_ANOINTMENT_L.get())
                .input(BMItems.BOW_VELOCITY_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.NUGGETS_GOLD))
                .input(Items.BOW)
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "bow_velocity_anointment_l");

        AlchemyTableRecipeBuilder.build(BMItems.WEAPON_REPAIR_ANOINTMENT_L.get())
                .input(BMItems.WEAPON_REPAIR_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.INGOTS_COPPER))
                .input(Ingredient.of(BMTags.Items.DUSTS_GOLD))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "weapon_repair_anointment_l");

        // Anointment _2 variants (level 2 - use strong tau)
        AlchemyTableRecipeBuilder.build(BMItems.FORTUNE_ANOINTMENT_2.get())
                .input(BMItems.FORTUNE_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(Ingredient.of(Tags.Items.DUSTS_REDSTONE))
                .input(Ingredient.of(BMTags.Items.DUSTS_COAL))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "fortune_anointment_2");

        AlchemyTableRecipeBuilder.build(BMItems.MELEE_DAMAGE_ANOINTMENT_2.get())
                .input(BMItems.MELEE_DAMAGE_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(Items.BLAZE_POWDER)
                .input(Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "melee_damage_anointment_2");

        AlchemyTableRecipeBuilder.build(BMItems.HOLY_WATER_ANOINTMENT_2.get())
                .input(BMItems.HOLY_WATER_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(Items.GLISTERING_MELON_SLICE)
                .input(Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "holy_water_anointment_2");

        AlchemyTableRecipeBuilder.build(BMItems.HIDDEN_KNOWLEDGE_ANOINTMENT_2.get())
                .input(BMItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(Items.GLASS_BOTTLE)
                .input(Items.ENCHANTED_BOOK)
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "hidden_knowledge_anointment_2");

        AlchemyTableRecipeBuilder.build(BMItems.QUICK_DRAW_ANOINTMENT_2.get())
                .input(BMItems.QUICK_DRAW_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(Ingredient.of(Tags.Items.STRINGS))
                .input(Items.SPECTRAL_ARROW)
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "quick_draw_anointment_2");

        AlchemyTableRecipeBuilder.build(BMItems.LOOTING_ANOINTMENT_2.get())
                .input(BMItems.LOOTING_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(Ingredient.of(Tags.Items.GEMS_LAPIS))
                .input(Ingredient.of(Tags.Items.BONES))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "looting_anointment_2");

        AlchemyTableRecipeBuilder.build(BMItems.BOW_POWER_ANOINTMENT_2.get())
                .input(BMItems.BOW_POWER_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(Ingredient.of(Tags.Items.INGOTS_IRON))
                .input(Items.BOW)
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "bow_power_anointment_2");

        AlchemyTableRecipeBuilder.build(BMItems.BOW_VELOCITY_ANOINTMENT_2.get())
                .input(BMItems.BOW_VELOCITY_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(Ingredient.of(Tags.Items.NUGGETS_GOLD))
                .input(Items.BOW)
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "bow_velocity_anointment_2");

        AlchemyTableRecipeBuilder.build(BMItems.WEAPON_REPAIR_ANOINTMENT_2.get())
                .input(BMItems.WEAPON_REPAIR_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(Ingredient.of(Tags.Items.INGOTS_COPPER))
                .input(Ingredient.of(BMTags.Items.DUSTS_GOLD))
                .syphon(1000).ticks(100).minimumTier(3)
                .save(output, "weapon_repair_anointment_2");

        // Anointment _XL variants (extra long - use tau oil + hellforged sand + amethyst)
        AlchemyTableRecipeBuilder.build(BMItems.FORTUNE_ANOINTMENT_XL.get())
                .input(BMItems.FORTUNE_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(BMTags.Items.DUSTS_COAL))
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "fortune_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.SILK_TOUCH_ANOINTMENT_XL.get())
                .input(BMItems.SILK_TOUCH_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.COBWEB)
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "silk_touch_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.MELEE_DAMAGE_ANOINTMENT_XL.get())
                .input(BMItems.MELEE_DAMAGE_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "melee_damage_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.HOLY_WATER_ANOINTMENT_XL.get())
                .input(BMItems.HOLY_WATER_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.GLISTERING_MELON_SLICE)
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "holy_water_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.HIDDEN_KNOWLEDGE_ANOINTMENT_XL.get())
                .input(BMItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.ENCHANTED_BOOK)
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "hidden_knowledge_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.QUICK_DRAW_ANOINTMENT_XL.get())
                .input(BMItems.QUICK_DRAW_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.SPECTRAL_ARROW)
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "quick_draw_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.LOOTING_ANOINTMENT_XL.get())
                .input(BMItems.LOOTING_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.GEMS_LAPIS))
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "looting_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.BOW_POWER_ANOINTMENT_XL.get())
                .input(BMItems.BOW_POWER_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.INGOTS_IRON))
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "bow_power_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.SMELTING_ANOINTMENT_XL.get())
                .input(BMItems.SMELTING_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.CHARCOAL)
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "smelting_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.VOIDING_ANOINTMENT_XL.get())
                .input(BMItems.VOIDING_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Items.COBBLED_DEEPSLATE)
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "voiding_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.BOW_VELOCITY_ANOINTMENT_XL.get())
                .input(BMItems.BOW_VELOCITY_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.NUGGETS_GOLD))
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "bow_velocity_anointment_xl");

        AlchemyTableRecipeBuilder.build(BMItems.WEAPON_REPAIR_ANOINTMENT_XL.get())
                .input(BMItems.WEAPON_REPAIR_ANOINTMENT.get())
                .input(BMItems.TAU_OIL.get())
                .input(Ingredient.of(Tags.Items.INGOTS_COPPER))
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.AMETHYST_SHARD)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "weapon_repair_anointment_xl");

        // Anointment _3 variants (level 3 - use strong tau + hellforged sand + glow berries)
        AlchemyTableRecipeBuilder.build(BMItems.FORTUNE_ANOINTMENT_3.get())
                .input(BMItems.FORTUNE_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.GLOW_BERRIES)
                .input(Ingredient.of(BMTags.Items.DUSTS_COAL))
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "fortune_anointment_3");

        AlchemyTableRecipeBuilder.build(BMItems.MELEE_DAMAGE_ANOINTMENT_3.get())
                .input(BMItems.MELEE_DAMAGE_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.GLOW_BERRIES)
                .input(Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "melee_damage_anointment_3");

        AlchemyTableRecipeBuilder.build(BMItems.HOLY_WATER_ANOINTMENT_3.get())
                .input(BMItems.HOLY_WATER_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.GLOW_BERRIES)
                .input(Items.GLISTERING_MELON_SLICE)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "holy_water_anointment_3");

        AlchemyTableRecipeBuilder.build(BMItems.HIDDEN_KNOWLEDGE_ANOINTMENT_3.get())
                .input(BMItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.GLOW_BERRIES)
                .input(Items.ENCHANTED_BOOK)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "hidden_knowledge_anointment_3");

        AlchemyTableRecipeBuilder.build(BMItems.QUICK_DRAW_ANOINTMENT_3.get())
                .input(BMItems.QUICK_DRAW_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.GLOW_BERRIES)
                .input(Items.SPECTRAL_ARROW)
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "quick_draw_anointment_3");

        AlchemyTableRecipeBuilder.build(BMItems.LOOTING_ANOINTMENT_3.get())
                .input(BMItems.LOOTING_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.GLOW_BERRIES)
                .input(Ingredient.of(Tags.Items.GEMS_LAPIS))
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "looting_anointment_3");

        AlchemyTableRecipeBuilder.build(BMItems.BOW_POWER_ANOINTMENT_3.get())
                .input(BMItems.BOW_POWER_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.GLOW_BERRIES)
                .input(Ingredient.of(Tags.Items.INGOTS_IRON))
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "bow_power_anointment_3");

        AlchemyTableRecipeBuilder.build(BMItems.BOW_VELOCITY_ANOINTMENT_3.get())
                .input(BMItems.BOW_VELOCITY_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.GLOW_BERRIES)
                .input(Ingredient.of(Tags.Items.NUGGETS_GOLD))
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "bow_velocity_anointment_3");

        AlchemyTableRecipeBuilder.build(BMItems.WEAPON_REPAIR_ANOINTMENT_3.get())
                .input(BMItems.WEAPON_REPAIR_ANOINTMENT.get())
                .input(BMBlocks.STRONG_TAU.item().get())
                .input(BMItems.HELLFORGED_SAND.get())
                .input(Items.GLOW_BERRIES)
                .input(Ingredient.of(Tags.Items.INGOTS_COPPER))
                .syphon(2000).ticks(100).minimumTier(4)
                .save(output, "weapon_repair_anointment_3");

        // Alchemy Table recipe (crafting recipe for the table itself)
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BMBlocks.ALCHEMY_TABLE.block().get())
                .pattern("sss")
                .pattern("S S")
                .pattern("gog")
                .define('s', Tags.Items.STONES)
                .define('S', BMItems.SLATE_BLANK.get())
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('o', BMItems.ORB_WEAK.get())
                .unlockedBy("has_weak_orb", has(BMItems.ORB_WEAK.get()))
                .save(output, BloodMagic.rl("alchemy_table"));
    }

    private void addARCRecipes(RecipeOutput output) {
        // Iron processing chain
        // Ore -> Sand (3x) with cutting fluid
        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Tags.Items.ORES_IRON))
                .guaranteedOutput(new ItemStack(BMItems.IRON_SAND.get(), 3))
                .save(output, BloodMagic.rl("dustsfrom_ore_iron"));

        // Raw material -> Fragment (2x + 25% extra) with explosive
        ARCRecipeBuilder.build(BMTags.Items.EXPLOSIVES)
                .input(Ingredient.of(Tags.Items.RAW_MATERIALS_IRON))
                .guaranteedOutput(new ItemStack(BMItems.IRON_FRAGMENT.get(), 2))
                .chancedOutput(new ItemStack(BMItems.IRON_FRAGMENT.get()), 0.25)
                .save(output, BloodMagic.rl("fragmentsiron"));

        // Ore -> Fragment (3x) with explosive
        ARCRecipeBuilder.build(BMTags.Items.EXPLOSIVES)
                .input(Ingredient.of(Tags.Items.ORES_IRON))
                .guaranteedOutput(new ItemStack(BMItems.IRON_FRAGMENT.get(), 3))
                .save(output, BloodMagic.rl("fragmentsfrom_ore_iron"));

        // Fragment -> Gravel (1x + 50% corrupted tinydust) with resonator
        ARCRecipeBuilder.build(BMTags.Items.RESONATOR)
                .input(Ingredient.of(BMTags.Items.FRAGMENTS_IRON))
                .guaranteedOutput(new ItemStack(BMItems.IRON_GRAVEL.get()))
                .chancedOutput(new ItemStack(BMItems.CORRUPTED_DUST_TINY.get()), 0.5)
                .save(output, BloodMagic.rl("gravelsiron"));

        // Gravel -> Sand (1x) with cutting fluid
        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(BMTags.Items.GRAVELS_IRON))
                .guaranteedOutput(new ItemStack(BMItems.IRON_SAND.get()))
                .save(output, BloodMagic.rl("dustsfrom_gravel_iron"));

        // Ingot -> Sand (1x) with cutting fluid
        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Tags.Items.INGOTS_IRON))
                .guaranteedOutput(new ItemStack(BMItems.IRON_SAND.get()))
                .save(output, BloodMagic.rl("dustsfrom_ingot_iron"));

        // Gold processing chain
        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Tags.Items.ORES_GOLD))
                .guaranteedOutput(new ItemStack(BMItems.GOLD_SAND.get(), 3))
                .save(output, BloodMagic.rl("dustsfrom_ore_gold"));

        ARCRecipeBuilder.build(BMTags.Items.EXPLOSIVES)
                .input(Ingredient.of(Tags.Items.RAW_MATERIALS_GOLD))
                .guaranteedOutput(new ItemStack(BMItems.GOLD_FRAGMENT.get(), 2))
                .chancedOutput(new ItemStack(BMItems.GOLD_FRAGMENT.get()), 0.25)
                .save(output, BloodMagic.rl("fragmentsgold"));

        ARCRecipeBuilder.build(BMTags.Items.EXPLOSIVES)
                .input(Ingredient.of(Tags.Items.ORES_GOLD))
                .guaranteedOutput(new ItemStack(BMItems.GOLD_FRAGMENT.get(), 3))
                .save(output, BloodMagic.rl("fragmentsfrom_ore_gold"));

        ARCRecipeBuilder.build(BMTags.Items.RESONATOR)
                .input(Ingredient.of(BMTags.Items.FRAGMENTS_GOLD))
                .guaranteedOutput(new ItemStack(BMItems.GOLD_GRAVEL.get()))
                .chancedOutput(new ItemStack(BMItems.CORRUPTED_DUST_TINY.get()), 0.5)
                .save(output, BloodMagic.rl("gravelsgold"));

        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(BMTags.Items.GRAVELS_GOLD))
                .guaranteedOutput(new ItemStack(BMItems.GOLD_SAND.get()))
                .save(output, BloodMagic.rl("dustsfrom_gravel_gold"));

        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Tags.Items.INGOTS_GOLD))
                .guaranteedOutput(new ItemStack(BMItems.GOLD_SAND.get()))
                .save(output, BloodMagic.rl("dustsfrom_ingot_gold"));

        // Copper processing chain
        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Tags.Items.ORES_COPPER))
                .guaranteedOutput(new ItemStack(BMItems.COPPER_SAND.get(), 3))
                .save(output, BloodMagic.rl("dustsfrom_ore_copper"));

        ARCRecipeBuilder.build(BMTags.Items.EXPLOSIVES)
                .input(Ingredient.of(Tags.Items.RAW_MATERIALS_COPPER))
                .guaranteedOutput(new ItemStack(BMItems.COPPER_FRAGMENT.get(), 2))
                .chancedOutput(new ItemStack(BMItems.COPPER_FRAGMENT.get()), 0.25)
                .save(output, BloodMagic.rl("fragmentscopper"));

        ARCRecipeBuilder.build(BMTags.Items.EXPLOSIVES)
                .input(Ingredient.of(Tags.Items.ORES_COPPER))
                .guaranteedOutput(new ItemStack(BMItems.COPPER_FRAGMENT.get(), 3))
                .save(output, BloodMagic.rl("fragmentsfrom_ore_copper"));

        ARCRecipeBuilder.build(BMTags.Items.RESONATOR)
                .input(Ingredient.of(BMTags.Items.FRAGMENTS_COPPER))
                .guaranteedOutput(new ItemStack(BMItems.COPPER_GRAVEL.get()))
                .chancedOutput(new ItemStack(BMItems.CORRUPTED_DUST_TINY.get()), 0.5)
                .save(output, BloodMagic.rl("gravelscopper"));

        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(BMTags.Items.GRAVELS_COPPER))
                .guaranteedOutput(new ItemStack(BMItems.COPPER_SAND.get()))
                .save(output, BloodMagic.rl("dustsfrom_gravel_copper"));

        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Tags.Items.INGOTS_COPPER))
                .guaranteedOutput(new ItemStack(BMItems.COPPER_SAND.get()))
                .save(output, BloodMagic.rl("dustsfrom_ingot_copper"));

        // Netherite scrap processing chain
        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Items.ANCIENT_DEBRIS))
                .guaranteedOutput(new ItemStack(BMItems.NETHERITE_SCRAP_SAND.get(), 3))
                .save(output, BloodMagic.rl("dustsfrom_ore_netherite_scrap"));

        ARCRecipeBuilder.build(BMTags.Items.EXPLOSIVES)
                .input(Ingredient.of(Items.NETHERITE_SCRAP))
                .guaranteedOutput(new ItemStack(BMItems.NETHERITE_SCRAP_FRAGMENT.get(), 2))
                .chancedOutput(new ItemStack(BMItems.NETHERITE_SCRAP_FRAGMENT.get()), 0.25)
                .save(output, BloodMagic.rl("fragmentsnetherite_scrap"));

        ARCRecipeBuilder.build(BMTags.Items.RESONATOR)
                .input(Ingredient.of(BMTags.Items.FRAGMENTS_NETHERITE_SCRAP))
                .guaranteedOutput(new ItemStack(BMItems.NETHERITE_SCRAP_GRAVEL.get()))
                .chancedOutput(new ItemStack(BMItems.CORRUPTED_DUST_TINY.get()), 0.5)
                .save(output, BloodMagic.rl("gravelsnetherite_scrap"));

        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(BMTags.Items.GRAVELS_NETHERITE_SCRAP))
                .guaranteedOutput(new ItemStack(BMItems.NETHERITE_SCRAP_SAND.get()))
                .save(output, BloodMagic.rl("dustsfrom_gravel_netherite_scrap"));

        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Items.NETHERITE_SCRAP))
                .guaranteedOutput(new ItemStack(BMItems.NETHERITE_SCRAP_SAND.get()))
                .save(output, BloodMagic.rl("dustsfrom_ingot_netherite_scrap"));

        // Hellforged/Demonite processing (only gravel->sand, others need hellforged ore/ingot)
        ARCRecipeBuilder.build(BMTags.Items.RESONATOR)
                .input(Ingredient.of(BMTags.Items.FRAGMENTS_HELLFORGED))
                .guaranteedOutput(new ItemStack(BMItems.DEMONITE_GRAVEL.get()))
                .chancedOutput(new ItemStack(BMItems.CORRUPTED_DUST_TINY.get()), 0.5)
                .save(output, BloodMagic.rl("gravelshellforged"));

        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(BMTags.Items.GRAVELS_HELLFORGED))
                .guaranteedOutput(new ItemStack(BMItems.HELLFORGED_SAND.get()))
                .save(output, BloodMagic.rl("dustsfrom_gravel_hellforged"));

        // Coal processing - coal -> coal sand with cutting fluid
        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Tags.Items.ORES_COAL))
                .guaranteedOutput(new ItemStack(BMItems.COAL_SAND.get(), 6))
                .save(output, BloodMagic.rl("coalsand_from_ore"));

        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Items.COAL))
                .guaranteedOutput(new ItemStack(BMItems.COAL_SAND.get()))
                .save(output, BloodMagic.rl("coalsand_from_coal"));

        // Utility recipes - hydration
        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Tags.Items.SANDS))
                .guaranteedOutput(new ItemStack(Items.CLAY_BALL, 4))
                .save(output, BloodMagic.rl("clay_from_sand"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.TERRACOTTA))
                .guaranteedOutput(new ItemStack(Items.CLAY, 1))
                .save(output, BloodMagic.rl("clay_from_terracotta"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.DIRT))
                .guaranteedOutput(new ItemStack(Items.MUD))
                .save(output, BloodMagic.rl("mud_from_dirt"));

        // Furnace recipes - sand to ingot
        ARCRecipeBuilder.build(BMTags.Items.ARC_SMELTING)
                .input(Ingredient.of(BMItems.IRON_SAND.get()))
                .guaranteedOutput(new ItemStack(Items.IRON_INGOT))
                .save(output, BloodMagic.rl("iron_ingot_from_sand"));

        ARCRecipeBuilder.build(BMTags.Items.ARC_SMELTING)
                .input(Ingredient.of(BMItems.GOLD_SAND.get()))
                .guaranteedOutput(new ItemStack(Items.GOLD_INGOT))
                .save(output, BloodMagic.rl("gold_ingot_from_sand"));

        ARCRecipeBuilder.build(BMTags.Items.ARC_SMELTING)
                .input(Ingredient.of(BMItems.COPPER_SAND.get()))
                .guaranteedOutput(new ItemStack(Items.COPPER_INGOT))
                .save(output, BloodMagic.rl("copper_ingot_from_sand"));

        // Netherite ingot from 4x scrap sand + 4x gold sand
        ARCRecipeBuilder.build(BMTags.Items.ARC_SMELTING)
                .input(Ingredient.of(BMItems.NETHERITE_SCRAP_SAND.get()))
                .input(Ingredient.of(BMItems.NETHERITE_SCRAP_SAND.get()))
                .input(Ingredient.of(BMItems.NETHERITE_SCRAP_SAND.get()))
                .input(Ingredient.of(BMItems.NETHERITE_SCRAP_SAND.get()))
                .input(Ingredient.of(BMItems.GOLD_SAND.get()))
                .input(Ingredient.of(BMItems.GOLD_SAND.get()))
                .input(Ingredient.of(BMItems.GOLD_SAND.get()))
                .input(Ingredient.of(BMItems.GOLD_SAND.get()))
                .guaranteedOutput(new ItemStack(Items.NETHERITE_INGOT))
                .save(output, BloodMagic.rl("netherite_ingot"));

        // Hellforged ingot from hellforged sand
        ARCRecipeBuilder.build(BMTags.Items.ARC_SMELTING)
                .input(Ingredient.of(BMItems.HELLFORGED_SAND.get()))
                .guaranteedOutput(new ItemStack(BMItems.HELLFORGED_INGOT.get()))
                .save(output, BloodMagic.rl("hellforged_ingot_from_sand"));

        // Netherrack to sulfur
        ARCRecipeBuilder.build(BMTags.Items.CUTTING_FLUIDS)
                .input(Ingredient.of(Items.NETHERRACK))
                .guaranteedOutput(new ItemStack(BMItems.SULFUR.get(), 2))
                .save(output, BloodMagic.rl("netherrack_to_sulfur"));

        // Mossify recipes - use hydration with water
        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.COBBLESTONE))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.MOSSY_COBBLESTONE))
                .save(output, BloodMagic.rl("mossify_cobblestone"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.COBBLESTONE_SLAB))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.MOSSY_COBBLESTONE_SLAB))
                .save(output, BloodMagic.rl("mossify_cobblestone_slab"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.COBBLESTONE_STAIRS))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.MOSSY_COBBLESTONE_STAIRS))
                .save(output, BloodMagic.rl("mossify_cobblestone_stairs"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.COBBLESTONE_WALL))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.MOSSY_COBBLESTONE_WALL))
                .save(output, BloodMagic.rl("mossify_cobblestone_wall"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.STONE_BRICKS))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.MOSSY_STONE_BRICKS))
                .save(output, BloodMagic.rl("mossify_stone_bricks"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.STONE_BRICK_SLAB))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.MOSSY_STONE_BRICK_SLAB))
                .save(output, BloodMagic.rl("mossify_stone_brick_slab"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.STONE_BRICK_STAIRS))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.MOSSY_STONE_BRICK_STAIRS))
                .save(output, BloodMagic.rl("mossify_stone_brick_stairs"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.STONE_BRICK_WALL))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.MOSSY_STONE_BRICK_WALL))
                .save(output, BloodMagic.rl("mossify_stone_brick_wall"));

        // Solidify concrete recipes (16 colors)
        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.WHITE_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.WHITE_CONCRETE))
                .save(output, BloodMagic.rl("solidify_white_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.ORANGE_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.ORANGE_CONCRETE))
                .save(output, BloodMagic.rl("solidify_orange_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.MAGENTA_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.MAGENTA_CONCRETE))
                .save(output, BloodMagic.rl("solidify_magenta_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.LIGHT_BLUE_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.LIGHT_BLUE_CONCRETE))
                .save(output, BloodMagic.rl("solidify_light_blue_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.YELLOW_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.YELLOW_CONCRETE))
                .save(output, BloodMagic.rl("solidify_yellow_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.LIME_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.LIME_CONCRETE))
                .save(output, BloodMagic.rl("solidify_lime_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.PINK_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.PINK_CONCRETE))
                .save(output, BloodMagic.rl("solidify_pink_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.GRAY_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.GRAY_CONCRETE))
                .save(output, BloodMagic.rl("solidify_gray_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.LIGHT_GRAY_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.LIGHT_GRAY_CONCRETE))
                .save(output, BloodMagic.rl("solidify_light_gray_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.CYAN_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.CYAN_CONCRETE))
                .save(output, BloodMagic.rl("solidify_cyan_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.PURPLE_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.PURPLE_CONCRETE))
                .save(output, BloodMagic.rl("solidify_purple_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.BLUE_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.BLUE_CONCRETE))
                .save(output, BloodMagic.rl("solidify_blue_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.BROWN_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.BROWN_CONCRETE))
                .save(output, BloodMagic.rl("solidify_brown_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.GREEN_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.GREEN_CONCRETE))
                .save(output, BloodMagic.rl("solidify_green_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.RED_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.RED_CONCRETE))
                .save(output, BloodMagic.rl("solidify_red_concrete"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.BLACK_CONCRETE_POWDER))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.BLACK_CONCRETE))
                .save(output, BloodMagic.rl("solidify_black_concrete"));

        // Wash recipes - washing colors back to white
        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(ItemTags.WOOL))
                .fluidInput(new FluidStack(Fluids.WATER, 333))
                .guaranteedOutput(new ItemStack(Items.WHITE_WOOL))
                .save(output, BloodMagic.rl("wash_wool"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(ItemTags.BEDS))
                .fluidInput(new FluidStack(Fluids.WATER, 333))
                .guaranteedOutput(new ItemStack(Items.WHITE_BED))
                .save(output, BloodMagic.rl("wash_bed"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(ItemTags.WOOL_CARPETS))
                .fluidInput(new FluidStack(Fluids.WATER, 333))
                .guaranteedOutput(new ItemStack(Items.WHITE_CARPET))
                .save(output, BloodMagic.rl("wash_carpet"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Tags.Items.GLASS_BLOCKS))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.GLASS))
                .save(output, BloodMagic.rl("wash_glass"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Tags.Items.GLASS_PANES))
                .fluidInput(new FluidStack(Fluids.WATER, 200))
                .guaranteedOutput(new ItemStack(Items.GLASS_PANE))
                .save(output, BloodMagic.rl("wash_glass_pane"));

        // Copper oxidization recipes
        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.COPPER_BLOCK))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.EXPOSED_COPPER))
                .save(output, BloodMagic.rl("copper_block_to_exposed_copper"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.EXPOSED_COPPER))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.WEATHERED_COPPER))
                .save(output, BloodMagic.rl("exposed_copper_to_weathered_copper"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.WEATHERED_COPPER))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.OXIDIZED_COPPER))
                .save(output, BloodMagic.rl("weathered_copper_to_oxidized_copper"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.CUT_COPPER))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.EXPOSED_CUT_COPPER))
                .save(output, BloodMagic.rl("cut_copper_to_exposed_cut_copper"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.EXPOSED_CUT_COPPER))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.WEATHERED_CUT_COPPER))
                .save(output, BloodMagic.rl("exposed_cut_copper_to_weathered_cut_copper"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.WEATHERED_CUT_COPPER))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.OXIDIZED_CUT_COPPER))
                .save(output, BloodMagic.rl("weathered_cut_copper_to_oxidized_cut_copper"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.CUT_COPPER_STAIRS))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.EXPOSED_CUT_COPPER_STAIRS))
                .save(output, BloodMagic.rl("cut_copper_stairs_to_exposed_cut_copper_stairs"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.EXPOSED_CUT_COPPER_STAIRS))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.WEATHERED_CUT_COPPER_STAIRS))
                .save(output, BloodMagic.rl("exposed_cut_copper_stairs_to_weathered_cut_copper_stairs"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.WEATHERED_CUT_COPPER_STAIRS))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.OXIDIZED_CUT_COPPER_STAIRS))
                .save(output, BloodMagic.rl("weathered_cut_copper_stairs_to_oxidized_cut_copper_stairs"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.CUT_COPPER_SLAB))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.EXPOSED_CUT_COPPER_SLAB))
                .save(output, BloodMagic.rl("cut_copper_slab_to_exposed_cut_copper_slab"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.EXPOSED_CUT_COPPER_SLAB))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.WEATHERED_CUT_COPPER_SLAB))
                .save(output, BloodMagic.rl("exposed_cut_copper_slab_to_weathered_cut_copper_slab"));

        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(Ingredient.of(Items.WEATHERED_CUT_COPPER_SLAB))
                .fluidInput(new FluidStack(Fluids.WATER, 500))
                .guaranteedOutput(new ItemStack(Items.OXIDIZED_CUT_COPPER_SLAB))
                .save(output, BloodMagic.rl("weathered_cut_copper_slab_to_oxidized_cut_copper_slab"));

        // Tau strengthening - weak tau + life essence -> strong tau
        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(BMBlocks.WEAK_TAU.item().get())
                .fluidInput(new FluidStack(BMFluids.LIFE_ESSENCE_SOURCE.get(), 1600))
                .guaranteedOutput(new ItemStack(BMBlocks.STRONG_TAU.item().get()))
                .save(output, BloodMagic.rl("strengthen_tau"));

        // Weak blood shard from tau + life essence
        ARCRecipeBuilder.build(BMTags.Items.HYDRATION)
                .input(BMBlocks.STRONG_TAU.item().get())
                .fluidInput(new FluidStack(BMFluids.LIFE_ESSENCE_SOURCE.get(), 3200))
                .guaranteedOutput(new ItemStack(BMItems.WEAK_BLOOD_SHARD.get()))
                .save(output, BloodMagic.rl("weakbloodshard_tau"));
    }
}
