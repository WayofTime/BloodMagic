package WayofTime.bloodmagic.core;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api_impl.BloodMagicRecipeRegistrar;
import WayofTime.bloodmagic.apiv2.IBloodMagicRecipeRegistrar;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.item.ItemDemonCrystal;
import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.item.types.ComponentType;
import WayofTime.bloodmagic.item.types.ReagentType;
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
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class RegistrarBloodMagicRecipes {

    @SubscribeEvent
    public static void registerCrafting(RegistryEvent.Register<IRecipe> event) {
        for (int i = 0; i < ItemSoulGem.names.length; i++) {
            for (int j = 0; j < ItemDemonCrystal.NAMES.size(); j++) {
                ItemStack baseGemStack = new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, i);
                ItemStack newGemStack = new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, i);

                ItemStack crystalStack = new ItemStack(RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL, 1, j);

                EnumDemonWillType willType = ((ItemDemonCrystal) RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL).getType(crystalStack);
                ((ItemSoulGem) RegistrarBloodMagicItems.SOUL_GEM).setCurrentType(willType, newGemStack);
                ShapelessOreRecipe shapeless  = new ShapelessOreRecipe(new ResourceLocation(BloodMagic.MODID, "soul_gem"), newGemStack, baseGemStack, crystalStack);
                event.getRegistry().register(shapeless.setRegistryName("soul_gem_" + willType.getName()));
            }
        }
    }

    public static void registerAltarRecipes(IBloodMagicRecipeRegistrar registrar) {
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

    public static void registerAlchemyTableRecipes(IBloodMagicRecipeRegistrar registrar) {

    }

    public static void registerTartaricForgeRecipes(BloodMagicRecipeRegistrar registrar) {
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), 1, 1, "dustRedstone", "ingotGold", "blockGlass", "dyeBlue");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), 60, 20, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), "gemDiamond", "blockRedstone", "blockLapis");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 2), 240, 50, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), "gemDiamond", "blockGold", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 3), 1000, 100, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 2), new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3), new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 4), 4000, 500, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 3), Items.NETHER_STAR);
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_SWORD), 0, 0, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), new ItemStack(Items.IRON_SWORD));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_AXE), 0, 0, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), new ItemStack(Items.IRON_AXE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_PICKAXE), 0, 0, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), new ItemStack(Items.IRON_PICKAXE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_SHOVEL), 0, 0, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM), new ItemStack(Items.IRON_SHOVEL));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_BOW), 70, 0, new ItemStack(Items.BOW), new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), "string", "string");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES), 0, 0, "dustRedstone", "dyeWhite", "gunpowder", Items.COAL);
        registrar.addTartaricForge(ReagentType.REAGENT_WATER.getStack(), 10, 3, new ItemStack(Items.SUGAR), new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.WATER_BUCKET));
        registrar.addTartaricForge(ReagentType.REAGENT_LAVA.getStack(), 32, 10, Items.LAVA_BUCKET, "dustRedstone", "cobblestone", "blockCoal");
        registrar.addTartaricForge(ReagentType.REAGENT_VOID.getStack(), 64, 10, Items.BUCKET, "string", "string", "gunpowder");
        registrar.addTartaricForge(ReagentType.REAGENT_GROWTH.getStack(), 128, 20, "treeSapling", "treeSapling", "sugarcane", Items.SUGAR);
        registrar.addTartaricForge(ReagentType.REAGENT_AIR.getStack(), 128, 20, Items.GHAST_TEAR, "feather", "feather");
        registrar.addTartaricForge(ReagentType.REAGENT_SIGHT.getStack(), 64, 0, RegistrarBloodMagicItems.SIGIL_DIVINATION, "blockGlass", "blockGlass", "dustGlowstone");
        registrar.addTartaricForge(ReagentType.REAGENT_HOLDING.getStack(), 64, 20, "chestWood", "leather", "string", "string");
        registrar.addTartaricForge(ReagentType.REAGENT_FASTMINER.getStack(), 128, 10, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.GUNPOWDER);
        registrar.addTartaricForge(ReagentType.REAGENT_AFFINITY.getStack(), 300, 30, RegistrarBloodMagicItems.SIGIL_WATER, RegistrarBloodMagicItems.SIGIL_AIR, RegistrarBloodMagicItems.SIGIL_LAVA, Blocks.OBSIDIAN);
        registrar.addTartaricForge(ReagentType.REAGENT_SUPPRESSION.getStack(), 500, 50, RegistrarBloodMagicBlocks.TELEPOSER, Items.WATER_BUCKET, Items.LAVA_BUCKET, Items.BLAZE_ROD);
        registrar.addTartaricForge(ReagentType.REAGENT_BINDING.getStack(), 400, 10, "dustGlowstone", "dustRedstone", "nuggetGold", Items.GUNPOWDER);
        registrar.addTartaricForge(ReagentType.REAGENT_BLOODLIGHT.getStack(), 300, 10, "glowstone", Blocks.TORCH, "dustRedstone", "dustRedstone");
        registrar.addTartaricForge(ReagentType.REAGENT_MAGNETISM.getStack(), 600, 10, "string", "ingotGold", "blockIron", "ingotGold");
        registrar.addTartaricForge(ReagentType.REAGENT_HASTE.getStack(), 1400, 100, Items.COOKIE, Items.SUGAR, Items.COOKIE, "stone");
        registrar.addTartaricForge(ReagentType.REAGENT_BRIDGE.getStack(), 600, 50, Blocks.SOUL_SAND, Blocks.SOUL_SAND, "stone", Blocks.OBSIDIAN);
        registrar.addTartaricForge(ReagentType.REAGENT_SEVERANCE.getStack(), 800, 70, Items.ENDER_EYE, Items.ENDER_PEARL, "ingotGold", "ingotGold");
        registrar.addTartaricForge(ReagentType.REAGENT_COMPRESSION.getStack(), 2000, 200, "blockIron", "blockGold", Blocks.OBSIDIAN, "cobblestone");
        registrar.addTartaricForge(ReagentType.REAGENT_TELEPOSITION.getStack(), 1500, 200, RegistrarBloodMagicBlocks.TELEPOSER, "glowstone", "blockRedstone", "ingotGold");
        registrar.addTartaricForge(ReagentType.REAGENT_TRANSPOSITION.getStack(), 1500, 200, RegistrarBloodMagicBlocks.TELEPOSER, "gemDiamond", Items.ENDER_PEARL, Blocks.OBSIDIAN);
        registrar.addTartaricForge(ReagentType.REAGENT_CLAW.getStack(), 800, 120, Items.FLINT, Items.FLINT, ItemCuttingFluid.getStack(ItemCuttingFluid.BASIC));
        registrar.addTartaricForge(ReagentType.REAGENT_BOUNCE.getStack(), 200, 20, Blocks.SLIME_BLOCK, Blocks.SLIME_BLOCK, Items.LEATHER, "string");
        registrar.addTartaricForge(ReagentType.REAGENT_FROST.getStack(), 80, 10, Blocks.ICE, Items.SNOWBALL, Items.SNOWBALL, "dustRedstone");

        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.SENTIENT_ARMOUR_GEM), 240, 150, Items.DIAMOND_CHESTPLATE, new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), Blocks.IRON_BLOCK, Blocks.OBSIDIAN);

        registrar.addTartaricForge(ComponentType.FRAME_PART.getStack(), 400, 10, "blockGlass", "stone", new ItemStack(RegistrarBloodMagicItems.SLATE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.NODE_ROUTER), 400, 5, "stickWood", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1), "gemLapis", "gemLapis");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE), 400, 5, "dustGlowstone", "dustRedstone", "blockGlass", "stone");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.OUTPUT_ROUTING_NODE), 400, 25, "dustGlowstone", "dustRedstone", "ingotIron", new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.INPUT_ROUTING_NODE), 400, 25, "dustGlowstone", "dustRedstone", "ingotGold", new ItemStack(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.MASTER_ROUTING_NODE), 400, 200, "blockIron", "gemDiamond", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2));

        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL, 1, 0), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL, 1, 1), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL, 1, 2), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL, 1, 3), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL));
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTAL, 1, 4), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST));

        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRUCIBLE), 400, 100, Items.CAULDRON, "stone", "gemLapis", "gemDiamond");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_PYLON), 400, 50, "blockIron", "stone", "gemLapis", RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL);
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicBlocks.DEMON_CRYSTALLIZER), 500, 100, RegistrarBloodMagicBlocks.SOUL_FORGE, "stone", "gemLapis", "blockGlass");
        registrar.addTartaricForge(new ItemStack(RegistrarBloodMagicItems.DEMON_WILL_GAUGE), 400, 50, "ingotGold", "dustRedstone", "blockGlass", RegistrarBloodMagicItems.ITEM_DEMON_CRYSTAL);
    }
}
