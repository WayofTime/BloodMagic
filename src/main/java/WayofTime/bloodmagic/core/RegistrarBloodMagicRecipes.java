package WayofTime.bloodmagic.core;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.impl.BloodMagicRecipeRegistrar;
import WayofTime.bloodmagic.apibutnotreally.altar.EnumAltarTier;
import WayofTime.bloodmagic.apibutnotreally.registry.OrbRegistry;
import WayofTime.bloodmagic.apibutnotreally.ritual.EnumRuneType;
import WayofTime.bloodmagic.apibutnotreally.soul.EnumDemonWillType;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
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

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class RegistrarBloodMagicRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        for (int i = 0; i < ItemSoulGem.names.length; i++) {
            for (EnumDemonWillType willType : EnumDemonWillType.values()) {
                ItemStack baseGemStack = new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, i);
                ItemStack newGemStack = new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, i);

                ((ItemSoulGem) RegistrarBloodMagicItems.SOUL_GEM).setCurrentType(willType, newGemStack);
                ShapelessOreRecipe shapeless  = new ShapelessOreRecipe(new ResourceLocation(BloodMagic.MODID, "soul_gem"), newGemStack, baseGemStack, willType.getStack());
                event.getRegistry().register(shapeless.setRegistryName("soul_gem_" + willType.getName()));
            }
        }

        OreDictionary.registerOre("dustIron", ComponentTypes.SAND_IRON.getStack());
        OreDictionary.registerOre("dustGold", ComponentTypes.SAND_GOLD.getStack());
        OreDictionary.registerOre("dustCoal", ComponentTypes.SAND_COAL.getStack());
    }

    public static void registerAltarRecipes(BloodMagicRecipeRegistrar registrar) {
        // ONE
        registrar.addBloodAltar(new OreIngredient("gemDiamond"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_WEAK), EnumAltarTier.ONE.ordinal(), 2000, 2, 1);
        registrar.addBloodAltar(new OreIngredient("stone"), new ItemStack(RegistrarBloodMagicItems.SLATE), EnumAltarTier.ONE.ordinal(), 1000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(Items.BUCKET), FluidUtil.getFilledBucket(new FluidStack(BlockLifeEssence.getLifeEssence(), Fluid.BUCKET_VOLUME)), EnumAltarTier.ONE.ordinal(), 1000, 5, 0);
        registrar.addBloodAltar(Ingredient.fromItem(Items.BOOK), new ItemStack(RegistrarBloodMagicItems.SANGUINE_BOOK), EnumAltarTier.ONE.ordinal(), 1000, 20, 0);

        // TWO
        registrar.addBloodAltar(new OreIngredient("blockRedstone"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_APPRENTICE), EnumAltarTier.TWO.ordinal(), 5000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(RegistrarBloodMagicItems.SLATE), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1), EnumAltarTier.TWO.ordinal(), 2000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(Items.IRON_SWORD), new ItemStack(RegistrarBloodMagicItems.DAGGER_OF_SACRIFICE), EnumAltarTier.TWO.ordinal(), 3000, 5, 5);

        // THREE
        registrar.addBloodAltar(new OreIngredient("blockGold"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MAGICIAN), EnumAltarTier.THREE.ordinal(), 25000, 20, 20);
        registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1)), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2), EnumAltarTier.THREE.ordinal(), 5000, 15, 10);
        registrar.addBloodAltar(new OreIngredient("obsidian"), EnumRuneType.EARTH.getScribeStack(), EnumAltarTier.THREE.ordinal(), 1000, 5, 5);
        registrar.addBloodAltar(new OreIngredient("blockLapis"), EnumRuneType.WATER.getScribeStack(), EnumAltarTier.THREE.ordinal(), 1000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(Items.MAGMA_CREAM), EnumRuneType.FIRE.getScribeStack(), EnumAltarTier.THREE.ordinal(), 1000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(Items.GHAST_TEAR), EnumRuneType.AIR.getScribeStack(), EnumAltarTier.THREE.ordinal(), 1000, 5, 5);
        registrar.addBloodAltar(Ingredient.fromItem(RegistrarBloodMagicItems.LAVA_CRYSTAL), new ItemStack(RegistrarBloodMagicItems.ACTIVATION_CRYSTAL), EnumAltarTier.THREE.ordinal(), 10000, 20, 10);

        // FOUR
        registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD)), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MASTER), EnumAltarTier.FOUR.ordinal(), 25000, 30, 50);
        registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2)), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3), EnumAltarTier.FOUR.ordinal(), 15000, 20, 20);
        registrar.addBloodAltar(new OreIngredient("blockCoal"), EnumRuneType.DUSK.getScribeStack(), EnumAltarTier.FOUR.ordinal(), 2000, 20, 10);
        registrar.addBloodAltar(new OreIngredient("enderpearl"), new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS), EnumAltarTier.FOUR.ordinal(), 2000, 10, 10);
        registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS)), new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS, 1, 1), EnumAltarTier.FOUR.ordinal(), 10000, 20, 10);

        // FIVE
        registrar.addBloodAltar(new OreIngredient("netherStar"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_ARCHMAGE), EnumAltarTier.FIVE.ordinal(), 80000, 50, 100);
        registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3)), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 4), EnumAltarTier.FIVE.ordinal(), 30000, 40, 100);

        // SIX
        registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicBlocks.DECORATIVE_BRICK, 1, 2)), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_TRANSCENDENT), EnumAltarTier.SIX.ordinal(), 200000, 100, 200);
        registrar.addBloodAltar(new OreIngredient("glowstone"), EnumRuneType.DAWN.getScribeStack(), EnumAltarTier.SIX.ordinal(), 200000, 100, 200);
    }

    public static void registerTartaricForgeRecipes(BloodMagicRecipeRegistrar registrar) {
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), 1, 1, "dustRedstone", "ingotGold", "blockGlass", "dyeBlue");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), 60, 20, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), "gemDiamond", "blockRedstone", "blockLapis");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 2), 240, 50, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), "gemDiamond", "blockGold", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 3), 1000, 100, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 2), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3), new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD), EnumDemonWillType.DEFAULT.getStack());
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
        registrar.addTartaricForge(ComponentTypes.REAGENT_CLAW.getStack(), 800, 120, Items.FLINT, Items.FLINT, ItemCuttingFluid.getStack(ItemCuttingFluid.BASIC));
        registrar.addTartaricForge(ComponentTypes.REAGENT_BOUNCE.getStack(), 200, 20, Blocks.SLIME_BLOCK, Blocks.SLIME_BLOCK, Items.LEATHER, "string");
        registrar.addTartaricForge(ComponentTypes.REAGENT_FROST.getStack(), 80, 10, Blocks.ICE, Items.SNOWBALL, Items.SNOWBALL, "dustRedstone");

        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_ARMOUR_GEM), 240, 150, Items.DIAMOND_CHESTPLATE, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), Blocks.IRON_BLOCK, Blocks.OBSIDIAN);

        registrar.addTartaricForge(ComponentTypes.FRAME_PART.getStack(), 400, 10, "blockGlass", "stone", new ItemStack(RegistrarBloodMagicItems.SLATE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.NODE_ROUTER), 400, 5, "stickWood", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1), "gemLapis", "gemLapis");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE), 400, 5, "dustGlowstone", "dustRedstone", "blockGlass", "stone");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.OUTPUT_ROUTING_NODE), 400, 25, "dustGlowstone", "dustRedstone", "ingotIron", new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.INPUT_ROUTING_NODE), 400, 25, "dustGlowstone", "dustRedstone", "ingotGold", new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.MASTER_ROUTING_NODE), 400, 200, "blockIron", "gemDiamond", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2));

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
        registrar.addAlchemyArray(new ItemStack(Items.REDSTONE), new ItemStack(RegistrarBloodMagicItems.SLATE), new ItemStack(RegistrarBloodMagicItems.SIGIL_DIVINATION), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/DivinationSigil.png"));

        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_WATER), new ItemStack(RegistrarBloodMagicItems.SLATE), new ItemStack(RegistrarBloodMagicItems.SIGIL_WATER), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WaterSigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), new ItemStack(RegistrarBloodMagicItems.SLATE), new ItemStack(RegistrarBloodMagicItems.SIGIL_LAVA), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LavaSigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_AIR), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1), new ItemStack(RegistrarBloodMagicItems.SIGIL_AIR), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/AirSigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1), new ItemStack(RegistrarBloodMagicItems.SIGIL_FAST_MINER), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_VOID), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1), new ItemStack(RegistrarBloodMagicItems.SIGIL_VOID), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/VoidSigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1), new ItemStack(RegistrarBloodMagicItems.SIGIL_GREEN_GROVE), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/GrowthSigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2), new ItemStack(RegistrarBloodMagicItems.SIGIL_ELEMENTAL_AFFINITY), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/ElementalAffinitySigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1), new ItemStack(RegistrarBloodMagicItems.SIGIL_SEER), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SightSigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_HOLDING), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2), new ItemStack(RegistrarBloodMagicItems.SIGIL_HOLDING), null);
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_BLOODLIGHT), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2), new ItemStack(RegistrarBloodMagicItems.SIGIL_BLOOD_LIGHT), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LightSigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_MAGNETISM), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2), new ItemStack(RegistrarBloodMagicItems.SIGIL_MAGNETISM), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MagnetismSigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_SUPPRESSION), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3), new ItemStack(RegistrarBloodMagicItems.SIGIL_SUPPRESSION), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SuppressionSigil.png"));
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_HASTE), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3), new ItemStack(RegistrarBloodMagicItems.SIGIL_HASTE), null);
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_BRIDGE), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3), new ItemStack(RegistrarBloodMagicItems.SIGIL_PHANTOM_BRIDGE), null);
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_COMPRESSION), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3), new ItemStack(RegistrarBloodMagicItems.SIGIL_COMPRESSION), null);
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_SEVERANCE), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3), new ItemStack(RegistrarBloodMagicItems.SIGIL_ENDER_SEVERANCE), null);
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_TELEPOSITION), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3), new ItemStack(RegistrarBloodMagicItems.SIGIL_TELEPOSITION), null);
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_TRANSPOSITION), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3), new ItemStack(RegistrarBloodMagicItems.SIGIL_TRANSPOSITION), null);
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_CLAW), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2), new ItemStack(RegistrarBloodMagicItems.SIGIL_CLAW), null);
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_BOUNCE), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1), new ItemStack(RegistrarBloodMagicItems.SIGIL_BOUNCE), null);
        registrar.addAlchemyArray(ItemComponent.getStack(ItemComponent.REAGENT_FROST), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1), new ItemStack(RegistrarBloodMagicItems.SIGIL_FROST), null);

    }
}
