package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.AnointmentData;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.data.recipe.builder.TartaricForgeRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.core.AnointmentRegistrar;

public class TartaricForgeRecipeProvider implements ISubRecipeProvider
{

	@Override
	public void addRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "soulforge/";
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.PETTY_GEM.get()), 1, 1, Ingredient.of(Tags.Items.DUSTS_REDSTONE), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.GLASS), Ingredient.of(Tags.Items.GEMS_LAPIS)).build(consumer, BloodMagic.rl(basePath + "pettytartaricgem"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.LESSER_GEM.get()), 60, 20, Ingredient.of(BloodMagicItems.PETTY_GEM.get()), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.STORAGE_BLOCKS_REDSTONE), Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS)).build(consumer, BloodMagic.rl(basePath + "lessertartaricgem"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.COMMON_GEM.get()), 240, 50, Ingredient.of(BloodMagicItems.LESSER_GEM.get()), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD), Ingredient.of(BloodMagicItems.IMBUED_SLATE.get())).build(consumer, BloodMagic.rl(basePath + "commontartaricgem"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.GREATER_GEM.get()), 1000, 100, Ingredient.of(BloodMagicItems.COMMON_GEM.get()), Ingredient.of(BloodMagicItems.DEMONIC_SLATE.get()), Ingredient.of(BloodMagicItems.WEAK_BLOOD_SHARD.get()), Ingredient.of(BloodMagicTags.CRYSTAL_DEMON)).build(consumer, BloodMagic.rl(basePath + "greatertartaricgem"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.SENTIENT_SWORD.get()), 0, 0, Ingredient.of(BloodMagicItems.PETTY_GEM.get()), Ingredient.of(Items.IRON_SWORD)).build(consumer, BloodMagic.rl(basePath + "sentientsword"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.SENTIENT_AXE.get()), 0, 0, Ingredient.of(BloodMagicItems.PETTY_GEM.get()), Ingredient.of(Items.IRON_AXE)).build(consumer, BloodMagic.rl(basePath + "sentientaxe"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.SENTIENT_PICKAXE.get()), 0, 0, Ingredient.of(BloodMagicItems.PETTY_GEM.get()), Ingredient.of(Items.IRON_PICKAXE)).build(consumer, BloodMagic.rl(basePath + "sentientpickaxe"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.SENTIENT_SHOVEL.get()), 0, 0, Ingredient.of(BloodMagicItems.PETTY_GEM.get()), Ingredient.of(Items.IRON_SHOVEL)).build(consumer, BloodMagic.rl(basePath + "sentientshovel"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.SENTIENT_SCYTHE.get()), 0, 0, Ingredient.of(BloodMagicItems.PETTY_GEM.get()), Ingredient.of(Items.IRON_HOE)).build(consumer, BloodMagic.rl(basePath + "sentientscythe"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.DEMON_CRUCIBLE.get()), 400, 100, Ingredient.of(Items.CAULDRON), Ingredient.of(Tags.Items.STONE), Ingredient.of(Tags.Items.GEMS_LAPIS), Ingredient.of(Tags.Items.GEMS_DIAMOND)).build(consumer, BloodMagic.rl(basePath + "demon_crucible"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.DEMON_CRYSTALLIZER.get()), 500, 100, Ingredient.of(BloodMagicBlocks.SOUL_FORGE.get()), Ingredient.of(Tags.Items.STONE), Ingredient.of(Tags.Items.GEMS_LAPIS), Ingredient.of(Tags.Items.GLASS)).build(consumer, BloodMagic.rl(basePath + "demon_crystallizer"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.DEMON_PYLON.get()), 400, 50, Ingredient.of(BloodMagicItems.RAW_CRYSTAL.get(), BloodMagicItems.CORROSIVE_CRYSTAL.get(), BloodMagicItems.VENGEFUL_CRYSTAL.get(), BloodMagicItems.DESTRUCTIVE_CRYSTAL.get(), BloodMagicItems.STEADFAST_CRYSTAL.get()), Ingredient.of(Tags.Items.STONE), Ingredient.of(Tags.Items.GEMS_LAPIS), Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON)).build(consumer, BloodMagic.rl(basePath + "demon_pylon"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.MASTER_NODE_UPGRADE.get()), 400, 50, Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(Tags.Items.GLASS), Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS)).build(consumer, BloodMagic.rl(basePath + "master_node_upgrade"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.DEMON_WILL_GAUGE.get()), 400, 50, Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.DUSTS_REDSTONE), Ingredient.of(Tags.Items.GLASS), Ingredient.of(BloodMagicTags.CRYSTAL_DEMON)).build(consumer, BloodMagic.rl(basePath + "demon_will_gauge"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.SANGUINE_REVERTER.get()), 350, 30, Ingredient.of(Items.SHEARS), Ingredient.of(Tags.Items.STONE), Ingredient.of(BloodMagicItems.IMBUED_SLATE.get()), Ingredient.of(Tags.Items.INGOTS_IRON)).build(consumer, BloodMagic.rl(basePath + "sanguine_reverter"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.RESONATOR.get()), 1200, 100, Ingredient.of(Tags.Items.STONE), Ingredient.of(Tags.Items.INGOTS_COPPER), Ingredient.of(BloodMagicItems.RAW_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "resonator"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.PRIMITIVE_CRYSTALLINE_RESONATOR.get()), 1200, 200, Ingredient.of(Tags.Items.GEMS_AMETHYST), Ingredient.of(Tags.Items.INGOTS), Ingredient.of(BloodMagicItems.RAW_CRYSTAL.get()), Ingredient.of(BloodMagicItems.TAU_OIL.get())).build(consumer, BloodMagic.rl(basePath + "primitive_resonator"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.HELLFORGED_RESONATOR.get()), 1200, 400, Ingredient.of(Tags.Items.GEMS_AMETHYST), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(BloodMagicItems.RAW_CRYSTAL.get()), Ingredient.of(BloodMagicItems.HELLFORGED_INGOT.get())).build(consumer, BloodMagic.rl(basePath + "hellforged_resonator"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get()), 1200, 100, Ingredient.of(BloodMagicItems.RAW_CRYSTAL.get()), Ingredient.of(BloodMagicItems.RAW_CRYSTAL.get()), Ingredient.of(BloodMagicItems.RAW_CRYSTAL.get()), Ingredient.of(BloodMagicItems.RAW_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "raw_crystal_block"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get()), 1200, 100, Ingredient.of(BloodMagicItems.CORROSIVE_CRYSTAL.get()), Ingredient.of(BloodMagicItems.CORROSIVE_CRYSTAL.get()), Ingredient.of(BloodMagicItems.CORROSIVE_CRYSTAL.get()), Ingredient.of(BloodMagicItems.CORROSIVE_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "corrosive_crystal_block"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get()), 1200, 100, Ingredient.of(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get()), Ingredient.of(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get()), Ingredient.of(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get()), Ingredient.of(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "destructive_crystal_block"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get()), 1200, 100, Ingredient.of(BloodMagicItems.VENGEFUL_CRYSTAL.get()), Ingredient.of(BloodMagicItems.VENGEFUL_CRYSTAL.get()), Ingredient.of(BloodMagicItems.VENGEFUL_CRYSTAL.get()), Ingredient.of(BloodMagicItems.VENGEFUL_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "vengeful_crystal_block"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get()), 1200, 100, Ingredient.of(BloodMagicItems.STEADFAST_CRYSTAL.get()), Ingredient.of(BloodMagicItems.STEADFAST_CRYSTAL.get()), Ingredient.of(BloodMagicItems.STEADFAST_CRYSTAL.get()), Ingredient.of(BloodMagicItems.STEADFAST_CRYSTAL.get())).build(consumer, BloodMagic.rl(basePath + "steadfast_crystal_block"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.SHAPED_CHARGE.get(), 8), 10, 0.5, Ingredient.of(Tags.Items.COBBLESTONE), Ingredient.of(Items.CHARCOAL), Ingredient.of(Tags.Items.SAND), Ingredient.of(Tags.Items.STONE)).build(consumer, BloodMagic.rl(basePath + "shaped_charge"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.DEFORESTER_CHARGE.get(), 8), 10, 0.5, Ingredient.of(Tags.Items.COBBLESTONE), Ingredient.of(Items.CHARCOAL), Ingredient.of(ItemTags.LOGS), Ingredient.of(ItemTags.PLANKS)).build(consumer, BloodMagic.rl(basePath + "deforester_charge"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.VEINMINE_CHARGE.get(), 8), 10, 0.5, Ingredient.of(Tags.Items.COBBLESTONE), Ingredient.of(Items.CHARCOAL), Ingredient.of(Tags.Items.SANDSTONE), Ingredient.of(Tags.Items.SAND)).build(consumer, BloodMagic.rl(basePath + "vein_charge"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.FUNGAL_CHARGE.get(), 8), 10, 0.5, Ingredient.of(Tags.Items.COBBLESTONE), Ingredient.of(Items.CHARCOAL), Ingredient.of(BloodMagicTags.MUSHROOM_HYPHAE), Ingredient.of(Tags.Items.MUSHROOMS)).build(consumer, BloodMagic.rl(basePath + "fungal_charge"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.DEFORESTER_CHARGE_2.get(), 4), 80, 2.5, Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER), Ingredient.of(Items.CHARCOAL), Ingredient.of(ItemTags.LOGS), Ingredient.of(ItemTags.PLANKS)).build(consumer, BloodMagic.rl(basePath + "deforester_charge_2"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.VEINMINE_CHARGE_2.get(), 4), 80, 2.5, Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER), Ingredient.of(Items.CHARCOAL), Ingredient.of(Tags.Items.SANDSTONE), Ingredient.of(Tags.Items.SAND)).build(consumer, BloodMagic.rl(basePath + "vein_charge_2"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.FUNGAL_CHARGE_2.get(), 4), 80, 2.5, Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER), Ingredient.of(Items.CHARCOAL), Ingredient.of(BloodMagicTags.MUSHROOM_HYPHAE), Ingredient.of(Tags.Items.MUSHROOMS)).build(consumer, BloodMagic.rl(basePath + "fungal_charge_2"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.AUG_SHAPED_CHARGE.get(), 6), 80, 2.5, Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER), Ingredient.of(Items.CHARCOAL), Ingredient.of(Tags.Items.SAND), Ingredient.of(Items.BRICK)).build(consumer, BloodMagic.rl(basePath + "aug_shaped_charge"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get(), 4), 80, 2.5, Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER), Ingredient.of(Items.CHARCOAL), Ingredient.of(Tags.Items.SAND), Ingredient.of(Tags.Items.STONE)).build(consumer, BloodMagic.rl(basePath + "shaped_charge_deep"));

		ItemStack stack = new ItemStack(BloodMagicBlocks.DEFORESTER_CHARGE.get());
		AnointmentHolder smeltingHolder = new AnointmentHolder();
		smeltingHolder.applyAnointment(stack, AnointmentRegistrar.ANOINTMENT_SMELTING.get(), new AnointmentData(1, 1, 1));

		AnointmentHolder fortune1Holder = new AnointmentHolder();
		fortune1Holder.applyAnointment(stack, AnointmentRegistrar.ANOINTMENT_FORTUNE.get(), new AnointmentData(1, 1, 1));

		AnointmentHolder fortune2Holder = new AnointmentHolder();
		fortune2Holder.applyAnointment(stack, AnointmentRegistrar.ANOINTMENT_FORTUNE.get(), new AnointmentData(2, 1, 1));

		AnointmentHolder silkHolder = new AnointmentHolder();
		silkHolder.applyAnointment(stack, AnointmentRegistrar.ANOINTMENT_SILK_TOUCH.get(), new AnointmentData(1, 1, 1));

		AnointmentHolder voidHolder = new AnointmentHolder();
		voidHolder.applyAnointment(stack, AnointmentRegistrar.ANOINTMENT_VOIDING.get(), new AnointmentData(1, 1, 1));

//		smeltingHolder.toItemStack(stack);

		String[] suffixArray = new String[] { "_smelting", "_fortune_1", "_silk_touch", "_voiding" };
		AnointmentHolder[] holderArray = new AnointmentHolder[] { smeltingHolder, fortune1Holder, silkHolder,
				voidHolder };
		Ingredient[] firstIngredientArray = new Ingredient[] { Ingredient.of(Tags.Items.CROPS_NETHER_WART),
				Ingredient.of(Tags.Items.CROPS_NETHER_WART), Ingredient.of(Tags.Items.CROPS_NETHER_WART),
				Ingredient.of(Tags.Items.CROPS_NETHER_WART) };
		Ingredient[] secondIngredientArray = new Ingredient[] { Ingredient.of(Items.FURNACE),
				Ingredient.of(Tags.Items.DUSTS_REDSTONE), Ingredient.of(Items.COBWEB),
				Ingredient.of(Blocks.NETHERRACK) };
		Ingredient[] thirdIngredientArray = new Ingredient[] { Ingredient.of(Items.CHARCOAL, Items.COAL),
				Ingredient.of(BloodMagicTags.DUST_COAL), Ingredient.of(Tags.Items.NUGGETS_GOLD),
				Ingredient.of(Blocks.COBBLED_DEEPSLATE) };

		for (int i = 0; i < suffixArray.length; i++)
		{
			ItemStack deforesterStack = new ItemStack(BloodMagicBlocks.DEFORESTER_CHARGE.get());
			ItemStack fungalStack = new ItemStack(BloodMagicBlocks.FUNGAL_CHARGE.get());
			ItemStack shapedStack = new ItemStack(BloodMagicBlocks.SHAPED_CHARGE.get());
			ItemStack veinStack = new ItemStack(BloodMagicBlocks.VEINMINE_CHARGE.get());

			AnointmentHolder holder = holderArray[i];
			holder.toItemStack(deforesterStack);
			holder.toItemStack(fungalStack);
			holder.toItemStack(shapedStack);
			holder.toItemStack(veinStack);

			TartaricForgeRecipeBuilder.tartaricForge(shapedStack, 60, 1, Ingredient.of(BloodMagicItems.SHAPED_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i]).build(consumer, BloodMagic.rl(basePath + "shaped_charge" + suffixArray[i]));
			TartaricForgeRecipeBuilder.tartaricForge(deforesterStack, 60, 1, Ingredient.of(BloodMagicItems.DEFORESTER_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i]).build(consumer, BloodMagic.rl(basePath + "deforester_charge" + suffixArray[i]));
			TartaricForgeRecipeBuilder.tartaricForge(veinStack, 60, 1, Ingredient.of(BloodMagicItems.VEINMINE_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i]).build(consumer, BloodMagic.rl(basePath + "vein_charge" + suffixArray[i]));
			TartaricForgeRecipeBuilder.tartaricForge(fungalStack, 60, 1, Ingredient.of(BloodMagicItems.FUNGAL_CHARGE_ITEM.get()), firstIngredientArray[i], secondIngredientArray[i], thirdIngredientArray[i]).build(consumer, BloodMagic.rl(basePath + "fungal_charge" + suffixArray[i]));

		}

		String[] suffixArray2 = new String[] { "_smelting_l", "_fortune_1_l", "_fortune_2_l", "_silk_touch_l",
				"_voiding" };
		AnointmentHolder[] holderArray2 = new AnointmentHolder[] { smeltingHolder, fortune1Holder, fortune2Holder,
				silkHolder, voidHolder };
		Ingredient[] ingredientArray2 = new Ingredient[] { Ingredient.of(BloodMagicItems.SMELTING_ANOINTMENT_L.get()),
				Ingredient.of(BloodMagicItems.FORTUNE_ANOINTMENT_L.get()),
				Ingredient.of(BloodMagicItems.FORTUNE_ANOINTMENT_2.get()),
				Ingredient.of(BloodMagicItems.SILK_TOUCH_ANOINTMENT_L.get()),
				Ingredient.of(BloodMagicItems.VOIDING_ANOINTMENT_L.get()) };

		for (int i = 0; i < suffixArray2.length; i++)
		{
			ItemStack deforester2Stack = new ItemStack(BloodMagicBlocks.DEFORESTER_CHARGE_2.get());
			ItemStack vein2Stack = new ItemStack(BloodMagicBlocks.VEINMINE_CHARGE_2.get());
			ItemStack fungal2Stack = new ItemStack(BloodMagicBlocks.FUNGAL_CHARGE_2.get());
			ItemStack shapedChargeDeepStack = new ItemStack(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get());
			ItemStack augShapedStack = new ItemStack(BloodMagicBlocks.AUG_SHAPED_CHARGE.get());
			AnointmentHolder holder = holderArray2[i];
			holder.toItemStack(deforester2Stack);
			holder.toItemStack(vein2Stack);
			holder.toItemStack(fungal2Stack);
			holder.toItemStack(shapedChargeDeepStack);
			holder.toItemStack(augShapedStack);

			TartaricForgeRecipeBuilder.tartaricForge(deforester2Stack, 300, 4, Ingredient.of(BloodMagicBlocks.DEFORESTER_CHARGE_2.get()), ingredientArray2[i]).build(consumer, BloodMagic.rl(basePath + "deforester_charge_2" + suffixArray2[i]));
			TartaricForgeRecipeBuilder.tartaricForge(vein2Stack, 300, 4, Ingredient.of(BloodMagicBlocks.VEINMINE_CHARGE_2.get()), ingredientArray2[i]).build(consumer, BloodMagic.rl(basePath + "vein_charge_2" + suffixArray2[i]));
			TartaricForgeRecipeBuilder.tartaricForge(fungal2Stack, 300, 4, Ingredient.of(BloodMagicBlocks.FUNGAL_CHARGE_2.get()), ingredientArray2[i]).build(consumer, BloodMagic.rl(basePath + "fungal_charge_2" + suffixArray2[i]));
			TartaricForgeRecipeBuilder.tartaricForge(shapedChargeDeepStack, 300, 4, Ingredient.of(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get()), ingredientArray2[i]).build(consumer, BloodMagic.rl(basePath + "shaped_charge_deep" + suffixArray2[i]));
			TartaricForgeRecipeBuilder.tartaricForge(augShapedStack, 300, 4, Ingredient.of(BloodMagicBlocks.AUG_SHAPED_CHARGE.get()), ingredientArray2[i]).build(consumer, BloodMagic.rl(basePath + "aug_shaped_charge" + suffixArray2[i]));

		}

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.THROWING_DAGGER.get(), 16), 32, 5, Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(Tags.Items.STRING)).build(consumer, BloodMagic.rl(basePath + "throwing_dagger"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.THROWING_DAGGER_COPPER.get(), 16), 32, 2, Ingredient.of(Tags.Items.INGOTS_COPPER), Ingredient.of(Tags.Items.INGOTS_COPPER), Ingredient.of(Tags.Items.GEMS_AMETHYST)).build(consumer, BloodMagic.rl(basePath + "throwing_dagger_copper"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.THROWING_DAGGER_SYRINGE.get(), 8), 10, 2, Ingredient.of(Tags.Items.STONE), Ingredient.of(Tags.Items.GLASS)).build(consumer, BloodMagic.rl(basePath + "throwing_dagger_syringe"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.NODE_ROUTER.get()), 400, 5, Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(BloodMagicItems.REINFORCED_SLATE.get()), Ingredient.of(Tags.Items.GEMS_LAPIS), Ingredient.of(Tags.Items.GEMS_LAPIS)).build(consumer, BloodMagic.rl(basePath + "node_router"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.ROUTING_NODE_BLOCK.get()), 400, 5, Ingredient.of(Tags.Items.STONE), Ingredient.of(Tags.Items.GLASS), Ingredient.of(Tags.Items.DUSTS_REDSTONE), Ingredient.of(Tags.Items.DUSTS_GLOWSTONE)).build(consumer, BloodMagic.rl(basePath + "routing_node"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.INPUT_ROUTING_NODE_BLOCK.get()), 400, 25, Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(BloodMagicBlocks.ROUTING_NODE_BLOCK.get()), Ingredient.of(Tags.Items.DUSTS_REDSTONE), Ingredient.of(Tags.Items.DUSTS_GLOWSTONE)).build(consumer, BloodMagic.rl(basePath + "input_routing_node"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.OUTPUT_ROUTING_NODE_BLOCK.get()), 400, 25, Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(BloodMagicBlocks.ROUTING_NODE_BLOCK.get()), Ingredient.of(Tags.Items.DUSTS_REDSTONE), Ingredient.of(Tags.Items.DUSTS_GLOWSTONE)).build(consumer, BloodMagic.rl(basePath + "output_routing_node"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicBlocks.MASTER_ROUTING_NODE_BLOCK.get()), 400, 200, Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(BloodMagicItems.IMBUED_SLATE.get())).build(consumer, BloodMagic.rl(basePath + "master_routing_node"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.DUNGEON_SIMPLE_KEY.get()), 300, 50, Ingredient.of(Tags.Items.STORAGE_BLOCKS_REDSTONE), Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(BloodMagicItems.IMBUED_SLATE.get())).build(consumer, BloodMagic.rl(basePath + "simple_key"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.DUNGEON_MINE_KEY.get()), 1200, 50, Ingredient.of(Tags.Items.INGOTS_COPPER), Ingredient.of(BloodMagicItems.CORROSIVE_CRYSTAL.get(), BloodMagicItems.DESTRUCTIVE_CRYSTAL.get(), BloodMagicItems.VENGEFUL_CRYSTAL.get(), BloodMagicItems.RAW_CRYSTAL.get(), BloodMagicItems.STEADFAST_CRYSTAL.get()), Ingredient.of(BloodMagicItems.HELLFORGED_INGOT.get()), Ingredient.of(BloodMagicItems.IMBUED_SLATE.get())).build(consumer, BloodMagic.rl(basePath + "mine_key"));

		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.RAW_CRYSTAL_CATALYST.get()), 400, 20, Ingredient.of(Tags.Items.CROPS_NETHER_WART), Ingredient.of(BloodMagicItems.TAU_OIL.get()), Ingredient.of(BloodMagicTags.DUST_SULFUR), Ingredient.of(Items.POTATO)).build(consumer, BloodMagic.rl(basePath + "raw_catalyst"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.CORROSIVE_CRYSTAL_CATALYST.get()), 400, 20, Ingredient.of(Tags.Items.CROPS_NETHER_WART), Ingredient.of(BloodMagicItems.TAU_OIL.get()), Ingredient.of(BloodMagicTags.DUST_SULFUR), Ingredient.of(Items.WHEAT_SEEDS)).build(consumer, BloodMagic.rl(basePath + "corrosive_catalyst"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.DESTRUCTIVE_CRYSTAL_CATALYST.get()), 400, 20, Ingredient.of(Tags.Items.CROPS_NETHER_WART), Ingredient.of(BloodMagicItems.TAU_OIL.get()), Ingredient.of(BloodMagicTags.DUST_SULFUR), Ingredient.of(Items.BEETROOT)).build(consumer, BloodMagic.rl(basePath + "destructive_catalyst"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.VENGEFUL_CRYSTAL_CATALYST.get()), 400, 20, Ingredient.of(Tags.Items.CROPS_NETHER_WART), Ingredient.of(BloodMagicItems.TAU_OIL.get()), Ingredient.of(BloodMagicTags.DUST_SULFUR), Ingredient.of(Items.MELON_SEEDS)).build(consumer, BloodMagic.rl(basePath + "vengeful_catalyst"));
		TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.STEADFAST_CRYSTAL_CATALYST.get()), 400, 20, Ingredient.of(Tags.Items.CROPS_NETHER_WART), Ingredient.of(BloodMagicItems.TAU_OIL.get()), Ingredient.of(BloodMagicTags.DUST_SULFUR), Ingredient.of(Items.PUMPKIN_SEEDS)).build(consumer, BloodMagic.rl(basePath + "steadfast_catalyst"));

		// Changed Recipes
		{
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.ARCANE_ASHES.get()), 0, 0, Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), Ingredient.fromTag(Tags.Items.DYES_WHITE), Ingredient.fromTag(Tags.Items.GUNPOWDER), Ingredient.fromTag(ItemTags.COALS)).build(consumer, BloodMagic.rl(basePath + "arcaneashes"));
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_AIR.get()), 128, 20, Ingredient.fromItems(Items.GHAST_TEAR), Ingredient.fromTag(Tags.Items.FEATHERS), Ingredient.fromTag(Tags.Items.FEATHERS)).build(consumer, BloodMagic.rl(basePath + "reagent_air"));
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_WATER.get()), 10, 3, Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.WATER_BUCKET), Ingredient.fromItems(Items.WATER_BUCKET)).build(consumer, BloodMagic.rl(basePath + "reagent_water"));
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_LAVA.get()), 32, 10, Ingredient.fromItems(Items.LAVA_BUCKET), Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), Ingredient.fromTag(Tags.Items.COBBLESTONE), Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_COAL)).build(consumer, BloodMagic.rl(basePath + "reagent_lava"));
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_VOID.get()), 64, 10, Ingredient.fromItems(Items.BUCKET), Ingredient.fromTag(Tags.Items.STRING), Ingredient.fromTag(Tags.Items.STRING), Ingredient.fromTag(Tags.Items.GUNPOWDER)).build(consumer, BloodMagic.rl(basePath + "reagent_void"));
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_GROWTH.get()), 128, 20, Ingredient.fromTag(ItemTags.SAPLINGS), Ingredient.fromTag(ItemTags.SAPLINGS), Ingredient.fromItems(Items.SUGAR_CANE), Ingredient.fromItems(Items.SUGAR)).build(consumer, BloodMagic.rl(basePath + "reagent_growth"));
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_MAGNETISM.get()), 600, 10, Ingredient.fromTag(Tags.Items.STRING), Ingredient.fromTag(Tags.Items.INGOTS_GOLD), Ingredient.fromTag(Tags.Items.INGOTS_GOLD), Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_IRON)).build(consumer, BloodMagic.rl(basePath + "reagent_magnetism"));
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_FAST_MINER.get()), 128, 20, Ingredient.fromItems(Items.IRON_PICKAXE), Ingredient.fromItems(Items.IRON_AXE), Ingredient.fromItems(Items.IRON_SHOVEL), Ingredient.fromTag(Tags.Items.GUNPOWDER)).build(consumer, BloodMagic.rl(basePath + "reagent_fastminer"));
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_BLOOD_LIGHT.get()), 300, 10, Ingredient.fromTag(Tags.Items.DUSTS_GLOWSTONE), Ingredient.fromItems(Items.TORCH), Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE)).build(consumer, BloodMagic.rl(basePath + "reagent_blood_light"));
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_SIGHT.get()), 64, 0, Ingredient.fromTag(Tags.Items.DUSTS_GLOWSTONE), Ingredient.fromTag(Tags.Items.GLASS), Ingredient.fromTag(Tags.Items.GLASS), Ingredient.fromItems(BloodMagicItems.DIVINATION_SIGIL.get())).build(consumer, BloodMagic.rl(basePath + "reagent_sight"));
//			TartaricForgeRecipeBuilder.tartaricForge(new ItemStack(BloodMagicItems.REAGENT_BINDING.get()), 400, 10, Ingredient.fromTag(Tags.Items.DUSTS_GLOWSTONE), Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE), Ingredient.fromTag(Tags.Items.GUNPOWDER), Ingredient.fromTag(Tags.Items.NUGGETS_GOLD)).build(consumer, BloodMagic.rl(basePath + "reagent_binding"));

		}
		{

		}
	}

}
