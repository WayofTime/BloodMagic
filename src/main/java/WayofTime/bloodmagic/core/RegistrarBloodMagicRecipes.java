package WayofTime.bloodmagic.core;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.impl.BloodMagicRecipeRegistrar;
import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.core.registry.OrbRegistry;
import WayofTime.bloodmagic.item.ItemSlate;
import WayofTime.bloodmagic.ritual.EnumRuneType;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import WayofTime.bloodmagic.item.alchemy.ItemLivingArmourPointsUpgrade;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import WayofTime.bloodmagic.util.PluginUtil;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class RegistrarBloodMagicRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        for (int i = 0; i < ItemSoulGem.names.length; i++) {
            for (EnumDemonWillType willType : EnumDemonWillType.values()) {
                ItemStack baseGemStack = new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, i);
                ItemStack newGemStack = new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, i);

                ((ItemSoulGem) RegistrarBloodMagicItems.SOUL_GEM).setCurrentType(willType, newGemStack);
                ShapelessOreRecipe shapeless = new ShapelessOreRecipe(new ResourceLocation(BloodMagic.MODID, "soul_gem"), newGemStack, baseGemStack, willType.getStack());
                event.getRegistry().register(shapeless.setRegistryName("soul_gem_" + willType.getName()));
            }
        }

        OreDictionary.registerOre("dustIron", ComponentTypes.SAND_IRON.getStack());
        OreDictionary.registerOre("dustGold", ComponentTypes.SAND_GOLD.getStack());
        OreDictionary.registerOre("dustCoal", ComponentTypes.SAND_COAL.getStack());

        PluginUtil.handlePluginStep(PluginUtil.RegistrationStep.RECIPE_REGISTER);
    }

    public static void registerAltarRecipes(BloodMagicRecipeRegistrar registrar) {
        // ONE
        registrar.addBloodAltar(new OreIngredient("gemDiamond"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_WEAK), AltarTier.ONE.ordinal(), 2000, 2, 1);
        registrar.addBloodAltar(new OreIngredient("stone"), ItemSlate.SlateType.BLANK.getStack(), AltarTier.ONE.ordinal(), 1000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(Items.BUCKET), FluidUtil.getFilledBucket(new FluidStack(BlockLifeEssence.getLifeEssence(), Fluid.BUCKET_VOLUME)), AltarTier.ONE.ordinal(), 1000, 5, 0);
        registrar.addBloodAltar(Ingredient.fromItem(Items.BOOK), new ItemStack(RegistrarBloodMagicItems.SANGUINE_BOOK), AltarTier.ONE.ordinal(), 1000, 20, 0);

        // TWO
        registrar.addBloodAltar(new OreIngredient("blockRedstone"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_APPRENTICE), AltarTier.TWO.ordinal(), 5000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.BLANK.getStack()), ItemSlate.SlateType.REINFORCED.getStack(), AltarTier.TWO.ordinal(), 2000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(Items.IRON_SWORD), new ItemStack(RegistrarBloodMagicItems.DAGGER_OF_SACRIFICE), AltarTier.TWO.ordinal(), 3000, 5, 5);

        // THREE
        registrar.addBloodAltar(new OreIngredient("blockGold"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MAGICIAN), AltarTier.THREE.ordinal(), 25000, 20, 20);
        registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.REINFORCED.getStack()), ItemSlate.SlateType.IMBUED.getStack(), AltarTier.THREE.ordinal(), 5000, 15, 10);
        registrar.addBloodAltar(new OreIngredient("obsidian"), EnumRuneType.EARTH.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
        registrar.addBloodAltar(new OreIngredient("blockLapis"), EnumRuneType.WATER.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(Items.MAGMA_CREAM), EnumRuneType.FIRE.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(Items.GHAST_TEAR), EnumRuneType.AIR.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(RegistrarBloodMagicItems.LAVA_CRYSTAL), new ItemStack(RegistrarBloodMagicItems.ACTIVATION_CRYSTAL), AltarTier.THREE.ordinal(), 10000, 20, 10);

        // FOUR
        registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD)), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MASTER), AltarTier.FOUR.ordinal(), 40000, 30, 50);
        registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.IMBUED.getStack()), ItemSlate.SlateType.DEMONIC.getStack(), AltarTier.FOUR.ordinal(), 15000, 20, 20);
        registrar.addBloodAltar(new OreIngredient("blockCoal"), EnumRuneType.DUSK.getStack(), AltarTier.FOUR.ordinal(), 2000, 20, 10);
        registrar.addBloodAltar(new OreIngredient("enderpearl"), new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS), AltarTier.FOUR.ordinal(), 2000, 10, 10);
        registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS)), new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS, 1, 1), AltarTier.FOUR.ordinal(), 10000, 20, 10);

        // FIVE
        registrar.addBloodAltar(new OreIngredient("netherStar"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_ARCHMAGE), AltarTier.FIVE.ordinal(), 80000, 50, 100);
        registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.DEMONIC.getStack()), ItemSlate.SlateType.ETHEREAL.getStack(), AltarTier.FIVE.ordinal(), 30000, 40, 100);

        // SIX
        registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicBlocks.DECORATIVE_BRICK, 1, 2)), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_TRANSCENDENT), AltarTier.SIX.ordinal(), 200000, 100, 200);
        registrar.addBloodAltar(new OreIngredient("glowstone"), EnumRuneType.DAWN.getStack(), AltarTier.SIX.ordinal(), 200000, 100, 200);
    }

    public static void registerAlchemyTableRecipes(BloodMagicRecipeRegistrar registrar) {
        registrar.addAlchemyTable(new ItemStack(Items.STRING, 4), 0, 100, 0, Blocks.WOOL, Items.FLINT);
        registrar.addAlchemyTable(new ItemStack(Items.FLINT, 2), 0, 20, 0, Blocks.GRAVEL, Items.FLINT);
        registrar.addAlchemyTable(new ItemStack(Items.LEATHER, 4), 100, 200, 1, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.FLINT, Items.WATER_BUCKET);
        registrar.addAlchemyTable(ItemCuttingFluid.FluidType.EXPLOSIVE.getStack(), 500, 200, 1, "gunpowder", "gunpowder", "dustCoal");
        registrar.addAlchemyTable(new ItemStack(Items.BREAD), 100, 200, 1, Items.WHEAT, Items.SUGAR);
        registrar.addAlchemyTable(new ItemStack(Blocks.GRASS), 200, 200, 1, Blocks.DIRT, new ItemStack(Items.DYE, 1, 15), Items.WHEAT_SEEDS);
        registrar.addAlchemyTable(new ItemStack(Items.CLAY_BALL, 4), 50, 100, 2, Items.WATER_BUCKET, "sand");
        registrar.addAlchemyTable(new ItemStack(Blocks.CLAY, 5), 200, 200, 1, Items.WATER_BUCKET, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY);
        registrar.addAlchemyTable(new ItemStack(Blocks.OBSIDIAN), 50, 50, 1, Items.WATER_BUCKET, Items.LAVA_BUCKET);
        registrar.addAlchemyTable(ComponentTypes.SULFUR.getStack(8), 0, 100, 0, Items.LAVA_BUCKET);
        registrar.addAlchemyTable(ComponentTypes.SALTPETER.getStack(4), 0, 100, 0, ComponentTypes.PLANT_OIL.getStack(), ComponentTypes.PLANT_OIL.getStack(), "dustCoal");
        registrar.addAlchemyTable(new ItemStack(Items.GUNPOWDER, 3), 0, 100, 0, "dustSaltpeter", "dustSulfur", new ItemStack(Items.COAL, 1, 1));
        registrar.addAlchemyTable(ComponentTypes.SAND_COAL.getStack(4), 100, 100, 1, new ItemStack(Items.COAL, 1, 0), new ItemStack(Items.COAL, 1, 0), Items.FLINT);
        registrar.addAlchemyTable(ItemCuttingFluid.FluidType.BASIC.getStack(), 1000, 400, 1, "dustCoal", "gunpowder", Items.REDSTONE, Items.SUGAR, ComponentTypes.PLANT_OIL.getStack(), new ItemStack(Items.POTIONITEM));
        registrar.addAlchemyTable(ComponentTypes.SAND_IRON.getStack(2), 400, 200, 1, "oreIron", ItemCuttingFluid.FluidType.BASIC.getStack());
        registrar.addAlchemyTable(ComponentTypes.SAND_GOLD.getStack(2), 400, 200, 1, "oreGold", ItemCuttingFluid.FluidType.BASIC.getStack());
        registrar.addAlchemyTable(new ItemStack(Items.REDSTONE, 8), 400, 200, 1, "oreRedstone", ItemCuttingFluid.FluidType.BASIC.getStack());
        registrar.addAlchemyTable(new ItemStack(Blocks.GRAVEL), 50, 50, 1, "cobblestone", ItemCuttingFluid.FluidType.EXPLOSIVE.getStack());
        registrar.addAlchemyTable(new ItemStack(Blocks.SAND), 50, 50, 1, Blocks.GRAVEL, ItemCuttingFluid.FluidType.EXPLOSIVE.getStack());
        registrar.addAlchemyTable(ComponentTypes.PLANT_OIL.getStack(), 100, 100, 1, "cropCarrot", "cropCarrot", "cropCarrot", new ItemStack(Items.DYE, 1, 15));
        registrar.addAlchemyTable(ComponentTypes.PLANT_OIL.getStack(), 100, 100, 1, "cropPotato", "cropPotato", new ItemStack(Items.DYE, 1, 15));
        registrar.addAlchemyTable(ComponentTypes.PLANT_OIL.getStack(), 100, 100, 1, "cropWheat", "cropWheat", new ItemStack(Items.DYE, 1, 15));
        registrar.addAlchemyTable(ComponentTypes.PLANT_OIL.getStack(), 100, 100, 1, Items.BEETROOT, Items.BEETROOT, Items.BEETROOT, new ItemStack(Items.DYE, 1, 15));
        registrar.addAlchemyTable(ComponentTypes.NEURO_TOXIN.getStack(), 1000, 100, 2, new ItemStack(Items.FISH, 1, 3));
        registrar.addAlchemyTable(ComponentTypes.ANTISEPTIC.getStack(2), 1000, 200, 2, ComponentTypes.PLANT_OIL.getStack(), "nuggetGold", "cropWheat", Items.SUGAR, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM);
        registrar.addAlchemyTable(ItemLivingArmourPointsUpgrade.UpgradeType.DRAFT_ANGELUS.getStack(), 20000, 400, 3, ComponentTypes.NEURO_TOXIN.getStack(), ComponentTypes.ANTISEPTIC.getStack(), "dustGold", Items.FERMENTED_SPIDER_EYE, new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD, 1, 0), Items.GHAST_TEAR);
        registrar.addAlchemyTable(new ItemStack(RegistrarBloodMagicItems.POTION_FLASK), 1000, 200, 2, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), "cropNetherWart", "dustRedstone", "dustGlowstone");
        registrar.addAlchemyTable(ComponentTypes.CATALYST_LENGTH_1.getStack(), 1000, 100, 2, "gunpowder", "cropNetherWart", "gemLapis");
        registrar.addAlchemyTable(ComponentTypes.CATALYST_POWER_1.getStack(), 1000, 100, 2, "gunpowder", "cropNetherWart", "dustRedstone");

        Set<String> addedOreRecipeList = Sets.newHashSet("oreIron", "oreGold", "oreCoal", "oreRedstone"); // We already added these above
        String[] oreList = OreDictionary.getOreNames().clone();
        for (String ore : oreList) {
            if (ore.startsWith("ore") && !addedOreRecipeList.contains(ore)) {
                String dustName = ore.replaceFirst("ore", "dust");

                List<ItemStack> discoveredOres = OreDictionary.getOres(ore);
                List<ItemStack> dustList = OreDictionary.getOres(dustName);
                if (dustList != null && !dustList.isEmpty() && discoveredOres != null && !discoveredOres.isEmpty()) {
                    ItemStack dustStack = dustList.get(0).copy();
                    dustStack.setCount(2);
                    registrar.addAlchemyTable(dustStack, 400, 200, 1, ore, ItemCuttingFluid.FluidType.BASIC.getStack());
                    addedOreRecipeList.add(ore);
                }
            }
        }
    }

    public static void registerTartaricForgeRecipes(BloodMagicRecipeRegistrar registrar) {
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), 1, 1, "dustRedstone", "ingotGold", "blockGlass", "dyeBlue");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), 60, 20, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), "gemDiamond", "blockRedstone", "blockLapis");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 2), 240, 50, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), "gemDiamond", "blockGold", ItemSlate.SlateType.IMBUED.getStack());
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 3), 1000, 100, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 2), ItemSlate.SlateType.DEMONIC.getStack(), new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD), EnumDemonWillType.DEFAULT.getStack());
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 4), 4000, 500, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 3), Items.NETHER_STAR);
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_SWORD), 0, 0, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), new ItemStack(Items.IRON_SWORD));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_AXE), 0, 0, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), new ItemStack(Items.IRON_AXE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_PICKAXE), 0, 0, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), new ItemStack(Items.IRON_PICKAXE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_SHOVEL), 0, 0, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), new ItemStack(Items.IRON_SHOVEL));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_BOW), 70, 0, new ItemStack(Items.BOW), new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), "string", "string");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES), 0, 0, "dustRedstone", "dyeWhite", "gunpowder", Items.COAL);
        registrar.addTartaricForge(ComponentTypes.REAGENT_WATER.getStack(), 10, 3, new ItemStack(Items.SUGAR), new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.WATER_BUCKET));
        registrar.addTartaricForge(ComponentTypes.REAGENT_LAVA.getStack(), 32, 10, Items.LAVA_BUCKET, "dustRedstone", "cobblestone", "blockCoal");
        registrar.addTartaricForge(ComponentTypes.REAGENT_VOID.getStack(), 64, 10, Items.BUCKET, "string", "string", "gunpowder");
        registrar.addTartaricForge(ComponentTypes.REAGENT_GROWTH.getStack(), 128, 20, "treeSapling", "treeSapling", "sugarcane", Items.SUGAR);
        registrar.addTartaricForge(ComponentTypes.REAGENT_AIR.getStack(), 128, 20, Items.GHAST_TEAR, "feather", "feather");
        registrar.addTartaricForge(ComponentTypes.REAGENT_SIGHT.getStack(), 64, 0, RegistrarBloodMagicItems.SIGIL_DIVINATION, "blockGlass", "blockGlass", "dustGlowstone");
        registrar.addTartaricForge(ComponentTypes.REAGENT_HOLDING.getStack(), 64, 20, "chestWood", "leather", "string", "string");
        registrar.addTartaricForge(ComponentTypes.REAGENT_FAST_MINER.getStack(), 128, 10, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.GUNPOWDER);
        registrar.addTartaricForge(ComponentTypes.REAGENT_AFFINITY.getStack(), 300, 30, RegistrarBloodMagicItems.SIGIL_WATER, RegistrarBloodMagicItems.SIGIL_AIR, RegistrarBloodMagicItems.SIGIL_LAVA, Blocks.OBSIDIAN);
        registrar.addTartaricForge(ComponentTypes.REAGENT_SUPPRESSION.getStack(), 500, 50, RegistrarBloodMagicBlocks.TELEPOSER, Items.WATER_BUCKET, Items.LAVA_BUCKET, Items.BLAZE_ROD);
        registrar.addTartaricForge(ComponentTypes.REAGENT_BINDING.getStack(), 400, 10, "dustGlowstone", "dustRedstone", "nuggetGold", Items.GUNPOWDER);
        registrar.addTartaricForge(ComponentTypes.REAGENT_BLOOD_LIGHT.getStack(), 300, 10, "glowstone", Blocks.TORCH, "dustRedstone", "dustRedstone");
        registrar.addTartaricForge(ComponentTypes.REAGENT_MAGNETISM.getStack(), 600, 10, "string", "ingotGold", "blockIron", "ingotGold");
        registrar.addTartaricForge(ComponentTypes.REAGENT_HASTE.getStack(), 1400, 100, Items.COOKIE, Items.SUGAR, Items.COOKIE, "stone");
        registrar.addTartaricForge(ComponentTypes.REAGENT_BRIDGE.getStack(), 600, 50, Blocks.SOUL_SAND, Blocks.SOUL_SAND, "stone", Blocks.OBSIDIAN);
        registrar.addTartaricForge(ComponentTypes.REAGENT_SEVERANCE.getStack(), 800, 70, Items.ENDER_EYE, Items.ENDER_PEARL, "ingotGold", "ingotGold");
        registrar.addTartaricForge(ComponentTypes.REAGENT_COMPRESSION.getStack(), 2000, 200, "blockIron", "blockGold", Blocks.OBSIDIAN, "cobblestone");
        registrar.addTartaricForge(ComponentTypes.REAGENT_TELEPOSITION.getStack(), 1500, 200, RegistrarBloodMagicBlocks.TELEPOSER, "glowstone", "blockRedstone", "ingotGold");
        registrar.addTartaricForge(ComponentTypes.REAGENT_TRANSPOSITION.getStack(), 1500, 200, RegistrarBloodMagicBlocks.TELEPOSER, "gemDiamond", Items.ENDER_PEARL, Blocks.OBSIDIAN);
        registrar.addTartaricForge(ComponentTypes.REAGENT_CLAW.getStack(), 800, 120, Items.FLINT, Items.FLINT, ItemCuttingFluid.FluidType.BASIC.getStack());
        registrar.addTartaricForge(ComponentTypes.REAGENT_BOUNCE.getStack(), 200, 20, Blocks.SLIME_BLOCK, Blocks.SLIME_BLOCK, Items.LEATHER, "string");
        registrar.addTartaricForge(ComponentTypes.REAGENT_FROST.getStack(), 80, 10, Blocks.ICE, Items.SNOWBALL, Items.SNOWBALL, "dustRedstone");

        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_ARMOUR_GEM), 240, 150, Items.DIAMOND_CHESTPLATE, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), Blocks.IRON_BLOCK, Blocks.OBSIDIAN);

        registrar.addTartaricForge(ComponentTypes.FRAME_PART.getStack(), 400, 10, "blockGlass", "stone", ItemSlate.SlateType.BLANK.getStack());
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.NODE_ROUTER), 400, 5, "stickWood", ItemSlate.SlateType.REINFORCED.getStack(), "gemLapis", "gemLapis");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE), 400, 5, "dustGlowstone", "dustRedstone", "blockGlass", "stone");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.OUTPUT_ROUTING_NODE), 400, 25, "dustGlowstone", "dustRedstone", "ingotIron", new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.INPUT_ROUTING_NODE), 400, 25, "dustGlowstone", "dustRedstone", "ingotGold", new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.MASTER_ROUTING_NODE), 400, 200, "blockIron", "gemDiamond", ItemSlate.SlateType.IMBUED.getStack());

        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL, 1, 0), 1200, 100, EnumDemonWillType.DEFAULT.getStack(), EnumDemonWillType.DEFAULT.getStack(), EnumDemonWillType.DEFAULT.getStack(), EnumDemonWillType.DEFAULT.getStack());
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL, 1, 1), 1200, 100, EnumDemonWillType.CORROSIVE.getStack(), EnumDemonWillType.CORROSIVE.getStack(), EnumDemonWillType.CORROSIVE.getStack(), EnumDemonWillType.CORROSIVE.getStack());
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL, 1, 2), 1200, 100, EnumDemonWillType.DESTRUCTIVE.getStack(), EnumDemonWillType.DESTRUCTIVE.getStack(), EnumDemonWillType.DESTRUCTIVE.getStack(), EnumDemonWillType.DESTRUCTIVE.getStack());
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL, 1, 3), 1200, 100, EnumDemonWillType.VENGEFUL.getStack(), EnumDemonWillType.VENGEFUL.getStack(), EnumDemonWillType.VENGEFUL.getStack(), EnumDemonWillType.VENGEFUL.getStack());
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL, 1, 4), 1200, 100, EnumDemonWillType.STEADFAST.getStack(), EnumDemonWillType.STEADFAST.getStack(), EnumDemonWillType.STEADFAST.getStack(), EnumDemonWillType.STEADFAST.getStack());

        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRUCIBLE), 400, 100, Items.CAULDRON, "stone", "gemLapis", "gemDiamond");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_PYLON), 400, 50, "blockIron", "stone", "gemLapis", RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL);
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTALLIZER), 500, 100, RegistrarBloodMagicBlocks.SOUL_FORGE, "stone", "gemLapis", "blockGlass");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.DEMON_WILL_GAUGE), 400, 50, "ingotGold", "dustRedstone", "blockGlass", RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL);
    }

    public static void registerAlchemyArrayRecipes(BloodMagicRecipeRegistrar registrar) {
        registrar.addAlchemyArray(new ItemStack(Items.REDSTONE), ItemSlate.SlateType.BLANK.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/DivinationSigil.png"));

        registrar.addAlchemyArray(ComponentTypes.REAGENT_WATER.getStack(), ItemSlate.SlateType.BLANK.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_WATER), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WaterSigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_LAVA.getStack(), ItemSlate.SlateType.BLANK.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_LAVA), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LavaSigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_AIR.getStack(), ItemSlate.SlateType.REINFORCED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_AIR), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/AirSigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_FAST_MINER.getStack(), ItemSlate.SlateType.REINFORCED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_FAST_MINER), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_VOID.getStack(), ItemSlate.SlateType.REINFORCED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_VOID), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/VoidSigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_GROWTH.getStack(), ItemSlate.SlateType.REINFORCED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_GREEN_GROVE), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/GrowthSigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_AFFINITY.getStack(), ItemSlate.SlateType.IMBUED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_ELEMENTAL_AFFINITY), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/ElementalAffinitySigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_SIGHT.getStack(), ItemSlate.SlateType.REINFORCED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_SEER), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SightSigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_HOLDING.getStack(), ItemSlate.SlateType.IMBUED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_HOLDING), null);
        registrar.addAlchemyArray(ComponentTypes.REAGENT_BLOOD_LIGHT.getStack(), ItemSlate.SlateType.IMBUED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_BLOOD_LIGHT), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LightSigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_MAGNETISM.getStack(), ItemSlate.SlateType.IMBUED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_MAGNETISM), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MagnetismSigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_SUPPRESSION.getStack(), ItemSlate.SlateType.DEMONIC.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_SUPPRESSION), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SuppressionSigil.png"));
        registrar.addAlchemyArray(ComponentTypes.REAGENT_HASTE.getStack(), ItemSlate.SlateType.DEMONIC.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_HASTE), null);
        registrar.addAlchemyArray(ComponentTypes.REAGENT_BRIDGE.getStack(), ItemSlate.SlateType.DEMONIC.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_PHANTOM_BRIDGE), null);
        registrar.addAlchemyArray(ComponentTypes.REAGENT_COMPRESSION.getStack(), ItemSlate.SlateType.DEMONIC.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_COMPRESSION), null);
        registrar.addAlchemyArray(ComponentTypes.REAGENT_SEVERANCE.getStack(), ItemSlate.SlateType.DEMONIC.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_ENDER_SEVERANCE), null);
        registrar.addAlchemyArray(ComponentTypes.REAGENT_TELEPOSITION.getStack(), ItemSlate.SlateType.DEMONIC.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_TELEPOSITION), null);
        registrar.addAlchemyArray(ComponentTypes.REAGENT_TRANSPOSITION.getStack(), ItemSlate.SlateType.DEMONIC.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_TRANSPOSITION), null);
        registrar.addAlchemyArray(ComponentTypes.REAGENT_CLAW.getStack(), ItemSlate.SlateType.IMBUED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_CLAW), null);
        registrar.addAlchemyArray(ComponentTypes.REAGENT_BOUNCE.getStack(), ItemSlate.SlateType.REINFORCED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_BOUNCE), null);
        registrar.addAlchemyArray(ComponentTypes.REAGENT_FROST.getStack(), ItemSlate.SlateType.REINFORCED.getStack(), new ItemStack(RegistrarBloodMagicItems.SIGIL_FROST), null);

    }
}
