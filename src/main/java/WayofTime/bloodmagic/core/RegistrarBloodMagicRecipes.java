package WayofTime.bloodmagic.core;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.impl.BloodMagicRecipeRegistrar;
import WayofTime.bloodmagic.apibutnotreally.altar.EnumAltarTier;
import WayofTime.bloodmagic.apibutnotreally.registry.OrbRegistry;
import WayofTime.bloodmagic.apibutnotreally.ritual.EnumRuneType;
import WayofTime.bloodmagic.apibutnotreally.soul.EnumDemonWillType;
import WayofTime.bloodmagic.block.BlockLifeEssence;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.item.types.ComponentTypes;
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
}
