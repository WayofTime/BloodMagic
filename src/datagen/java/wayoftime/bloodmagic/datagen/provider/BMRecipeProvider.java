package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.fluid.BMFluids;
import wayoftime.bloodmagic.common.item.BMItems;
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

        // Blank Rune
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BMBlocks.RUNE_BLANK.block().get())
                .pattern("sos")
                .pattern("s s")
                .pattern("sss")
                .define('s', Items.STONE)
                .define('o', BMItems.ORB_WEAK.get())
                .unlockedBy("has_weak_orb", has(BMItems.ORB_WEAK.get()))
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
        // Note: demon crystal tag not implemented yet, using weak blood shard x2 as placeholder
        ForgeRecipeBuilder.build(BMItems.SOUL_GEM_GREATER.get())
                .requires(BMItems.SOUL_GEM_COMMON.get())
                .requires(BMItems.SLATE_DEMONIC.get())
                .requires(BMItems.WEAK_BLOOD_SHARD.get())
                .requires(BMItems.WEAK_BLOOD_SHARD.get())  // TODO: Replace with demon crystal tag when available
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
}
