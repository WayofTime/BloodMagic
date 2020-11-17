package wayoftime.bloodmagic.core;

import wayoftime.bloodmagic.impl.BloodMagicRecipeRegistrar;

public class RegistrarBloodMagicRecipes
{

//    @SubscribeEvent
//    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
//        for (int i = 0; i < ItemSoulGem.names.length; i++) {
//            for (EnumDemonWillType willType : EnumDemonWillType.values()) {
//                ItemStack baseGemStack = new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, i);
//                ItemStack newGemStack = new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, i);
//
//                ((ItemSoulGem) RegistrarBloodMagicItems.SOUL_GEM).setCurrentType(willType, newGemStack);
//                ShapelessOreRecipe shapeless = new ShapelessOreRecipe(new ResourceLocation(BloodMagic.MODID, "soul_gem"), newGemStack, baseGemStack, willType.getStack());
//                event.getRegistry().register(shapeless.setRegistryName("soul_gem_" + willType.getName()));
//            }
//        }
//
//        OreDictionary.registerOre("dustIron", ComponentTypes.SAND_IRON.getStack());
//        OreDictionary.registerOre("dustGold", ComponentTypes.SAND_GOLD.getStack());
//        OreDictionary.registerOre("dustCoal", ComponentTypes.SAND_COAL.getStack());
//
//        PluginUtil.handlePluginStep(PluginUtil.RegistrationStep.RECIPE_REGISTER);
//
//        RegistrarBloodMagicItems.SOUL_TOOL_MATERIAL.setRepairItem(EnumDemonWillType.DEFAULT.getStack());
//    }

	public static void registerAltarRecipes(BloodMagicRecipeRegistrar registrar)
	{
//		Ingredient d;
		// ONE
//		registrar.addBloodAltar(new OreIngredient("gemDiamond"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_WEAK), AltarTier.ONE.ordinal(), 2000, 2, 1);
//		registrar.addBloodAltar(new OreIngredient("stone"), ItemSlate.SlateType.BLANK.getStack(), AltarTier.ONE.ordinal(), 1000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromItem(Items.BUCKET), FluidUtil.getFilledBucket(new FluidStack(BlockLifeEssence.getLifeEssence(), Fluid.BUCKET_VOLUME)), AltarTier.ONE.ordinal(), 1000, 5, 0);
//		registrar.addBloodAltar(Ingredient.fromItem(Items.BOOK), new ItemStack(RegistrarBloodMagicItems.SANGUINE_BOOK), AltarTier.ONE.ordinal(), 1000, 20, 0);
//
//		// TWO
//		registrar.addBloodAltar(new OreIngredient("blockRedstone"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_APPRENTICE), AltarTier.TWO.ordinal(), 5000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.BLANK.getStack()), ItemSlate.SlateType.REINFORCED.getStack(), AltarTier.TWO.ordinal(), 2000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromItem(Items.IRON_SWORD), new ItemStack(RegistrarBloodMagicItems.DAGGER_OF_SACRIFICE), AltarTier.TWO.ordinal(), 3000, 5, 5);
//
//		// THREE
//		registrar.addBloodAltar(new OreIngredient("blockGold"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MAGICIAN), AltarTier.THREE.ordinal(), 25000, 20, 20);
//		registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.REINFORCED.getStack()), ItemSlate.SlateType.IMBUED.getStack(), AltarTier.THREE.ordinal(), 5000, 15, 10);
//		registrar.addBloodAltar(new OreIngredient("obsidian"), EnumRuneType.EARTH.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
//		registrar.addBloodAltar(new OreIngredient("blockLapis"), EnumRuneType.WATER.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromItem(Items.MAGMA_CREAM), EnumRuneType.FIRE.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromItem(Items.GHAST_TEAR), EnumRuneType.AIR.getStack(), AltarTier.THREE.ordinal(), 1000, 5, 5);
//		registrar.addBloodAltar(Ingredient.fromItem(RegistrarBloodMagicItems.LAVA_CRYSTAL), new ItemStack(RegistrarBloodMagicItems.ACTIVATION_CRYSTAL), AltarTier.THREE.ordinal(), 10000, 20, 10);
//
//		// FOUR
//		registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD)), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_MASTER), AltarTier.FOUR.ordinal(), 40000, 30, 50);
//		registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.IMBUED.getStack()), ItemSlate.SlateType.DEMONIC.getStack(), AltarTier.FOUR.ordinal(), 15000, 20, 20);
//		registrar.addBloodAltar(new OreIngredient("blockCoal"), EnumRuneType.DUSK.getStack(), AltarTier.FOUR.ordinal(), 2000, 20, 10);
//		registrar.addBloodAltar(new OreIngredient("enderpearl"), new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS), AltarTier.FOUR.ordinal(), 2000, 10, 10);
//		registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS)), new ItemStack(RegistrarBloodMagicItems.TELEPOSITION_FOCUS, 1, 1), AltarTier.FOUR.ordinal(), 10000, 20, 10);
//
//		// FIVE
//		registrar.addBloodAltar(new OreIngredient("netherStar"), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_ARCHMAGE), AltarTier.FIVE.ordinal(), 80000, 50, 100);
//		registrar.addBloodAltar(Ingredient.fromStacks(ItemSlate.SlateType.DEMONIC.getStack()), ItemSlate.SlateType.ETHEREAL.getStack(), AltarTier.FIVE.ordinal(), 30000, 40, 100);

//		// SIX
//		if (ConfigHandler.general.enableTierSixEvenThoughThereIsNoContent)
//		{
//			registrar.addBloodAltar(Ingredient.fromStacks(new ItemStack(RegistrarBloodMagicBlocks.DECORATIVE_BRICK, 1, 2)), OrbRegistry.getOrbStack(RegistrarBloodMagic.ORB_TRANSCENDENT), AltarTier.SIX.ordinal(), 200000, 100, 200);
//			registrar.addBloodAltar(new OreIngredient("glowstone"), EnumRuneType.DAWN.getStack(), AltarTier.SIX.ordinal(), 200000, 100, 200);
//		}
	}
}
