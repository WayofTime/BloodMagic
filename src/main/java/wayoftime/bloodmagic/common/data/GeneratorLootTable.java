package wayoftime.bloodmagic.common.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.EnchantRandomly;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.ILootFunction.IBuilder;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetDamage;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BlockDemonCrystal;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.loot.BMTableLootEntry;
import wayoftime.bloodmagic.common.loot.SetLivingUpgrade;
import wayoftime.bloodmagic.common.loot.SetWillFraction;
import wayoftime.bloodmagic.common.loot.SetWillRange;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;

public class GeneratorLootTable extends LootTableProvider
{
	public GeneratorLootTable(DataGenerator dataGeneratorIn)
	{
		super(dataGeneratorIn);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
	{
		return ImmutableList.of(Pair.of(BMBlocks::new, LootParameterSets.BLOCK), Pair.of(BMLootTables::new, LootParameterSets.CHEST));
	}

	private static class BMLootTables extends ChestLootTables
	{
		@Override
		public void accept(BiConsumer<ResourceLocation, LootTable.Builder> acceptor)
		{
			generateSimpleDungeonLoot(acceptor);
		}

		private void generateSimpleDungeonLoot(BiConsumer<ResourceLocation, LootTable.Builder> acceptor)
		{
			LootPool.Builder vanillaDungeon = LootPool.builder().name("vanilla_dungeon").rolls(ConstantRange.of(1)).addEntry(BMTableLootEntry.builder(LootTables.CHESTS_SIMPLE_DUNGEON).weight(1));
			LootPool.Builder key_pool = LootPool.builder().name("keys").rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(BloodMagicItems.DUNGEON_SIMPLE_KEY.get()).weight(1).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/entrance_chest"), LootTable.builder().addLootPool(key_pool).addLootPool(vanillaDungeon));

			Item[] baseAnointments = new Item[] { BloodMagicItems.BOW_POWER_ANOINTMENT.get(),
					BloodMagicItems.FORTUNE_ANOINTMENT.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get(),
					BloodMagicItems.HOLY_WATER_ANOINTMENT.get(), BloodMagicItems.LOOTING_ANOINTMENT.get(),
					BloodMagicItems.MELEE_DAMAGE_ANOINTMENT.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT.get(),
					BloodMagicItems.SILK_TOUCH_ANOINTMENT.get(), BloodMagicItems.SMELTING_ANOINTMENT.get() };

			Item[] weaponAnointments = new Item[] { BloodMagicItems.BOW_POWER_ANOINTMENT.get(),
					BloodMagicItems.HOLY_WATER_ANOINTMENT.get(), BloodMagicItems.LOOTING_ANOINTMENT.get(),
					BloodMagicItems.MELEE_DAMAGE_ANOINTMENT.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT.get(),
					BloodMagicItems.SMELTING_ANOINTMENT.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT.get() };

			LootPool.Builder stronghold_library_pool = LootPool.builder().name("vanilla_library").rolls(ConstantRange.of(2)).addEntry(BMTableLootEntry.builder(LootTables.CHESTS_STRONGHOLD_LIBRARY).weight(1));
			LootPool.Builder extraLibraryItems = LootPool.builder().name("extra").rolls(RandomValueRange.of(2, 3)).addEntry(ItemLootEntry.builder(BloodMagicItems.WEAK_TAU_ITEM.get()).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
			extraLibraryItems.addEntry(ItemLootEntry.builder(BloodMagicItems.MONSTER_SOUL_RAW.get()).weight(10).acceptFunction(SetWillRange.withRange(RandomValueRange.of(15, 30))));
			extraLibraryItems.addEntry(ItemLootEntry.builder(BloodMagicItems.LIVING_TOME.get()).weight(5).acceptFunction(SetLivingUpgrade.withRange(RandomValueRange.of(200, 400), LivingArmorRegistrar.UPGRADE_EXPERIENCE.get().getKey())));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/library"), LootTable.builder().addLootPool(stronghold_library_pool).addLootPool(extraLibraryItems));

			LootPool.Builder potionChest = LootPool.builder().rolls(RandomValueRange.of(5, 7)).addEntry(ItemLootEntry.builder(Items.NETHER_WART).weight(40).acceptFunction(SetCount.builder(RandomValueRange.of(3, 7)))).addEntry(ItemLootEntry.builder(Items.BLAZE_POWDER).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(7, 10)))).addEntry(ItemLootEntry.builder(Items.BLAZE_POWDER).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3, 7)))).addEntry(ItemLootEntry.builder(Items.POTION).weight(3).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), (nbt) -> {
				nbt.putString("Potion", "minecraft:water");
			})))).addEntry(ItemLootEntry.builder(Items.SLIME_BALL).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(10, 15)))).addEntry(ItemLootEntry.builder(Items.MAGMA_CREAM).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(6, 10)))).addEntry(ItemLootEntry.builder(Items.GUNPOWDER).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(6, 10)))).addEntry(ItemLootEntry.builder(Items.REDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(10, 20)))).addEntry(ItemLootEntry.builder(Items.GLOWSTONE_DUST).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(20, 30))));
			potionChest = addMultipleItemsWithSameParams(potionChest, baseAnointments, 1, RandomValueRange.of(1, 3));

//			potionChest.addEntry(BMTableLootEntry.builder(LootTables.BASTION_BRIDGE).weight(1000));
//			potionChest.addEntry(EmptyLootEntry.func_216167_a());

			LootPool.Builder armory_pool = LootPool.builder().rolls(RandomValueRange.of(5, 7));

			armory_pool.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(25).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
			armory_pool.addEntry(ItemLootEntry.builder(Items.IRON_NUGGET).weight(20).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(30, 50))));
			armory_pool.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(4).quality(2).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
			armory_pool.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(18).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(10, 20))));
			armory_pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(8).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

			addMultipleItemsWithQualitySameParams(armory_pool, new Item[] { Items.LEATHER_BOOTS,
					Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET,
					Items.LEATHER_LEGGINGS }, 4, -3, ConstantRange.of(1), EnchantWithLevels.func_215895_a(RandomValueRange.of(15, 25)).func_216059_e(), SetDamage.func_215931_a(RandomValueRange.of(0.3F, 0.9F)));
			addMultipleItemsWithSameParams(armory_pool, new Item[] { Items.LEATHER_BOOTS, Items.LEATHER_CHESTPLATE,
					Items.LEATHER_HELMET,
					Items.LEATHER_LEGGINGS }, 7, ConstantRange.of(1), SetDamage.func_215931_a(RandomValueRange.of(0.8F, 1.0F)));
			addMultipleItemsWithSameParams(armory_pool, new Item[] { Items.IRON_BOOTS, Items.IRON_CHESTPLATE,
					Items.IRON_HELMET,
					Items.IRON_LEGGINGS }, 6, ConstantRange.of(1), SetDamage.func_215931_a(RandomValueRange.of(0.2F, 0.5F)));

			addMultipleItemsWithQualitySameParams(armory_pool, new Item[] { Items.IRON_BOOTS, Items.IRON_CHESTPLATE,
					Items.IRON_LEGGINGS,
					Items.IRON_HELMET }, 4, -2, ConstantRange.of(1), EnchantWithLevels.func_215895_a(RandomValueRange.of(10, 20)).func_216059_e(), SetDamage.func_215931_a(RandomValueRange.of(0.9F, 1.0F)));
			addMultipleItemsWithSameParams(armory_pool, new Item[] { Items.DIAMOND_BOOTS, Items.DIAMOND_CHESTPLATE,
					Items.DIAMOND_HELMET,
					Items.DIAMOND_LEGGINGS }, 2, ConstantRange.of(1), SetDamage.func_215931_a(RandomValueRange.of(0.1F, 0.2F)));
			addMultipleItemsWithQualitySameParams(armory_pool, new Item[] { Items.DIAMOND_BOOTS,
					Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET,
					Items.DIAMOND_LEGGINGS }, 1, 2, ConstantRange.of(1), EnchantWithLevels.func_215895_a(RandomValueRange.of(20, 25)).func_216059_e(), SetDamage.func_215931_a(RandomValueRange.of(0.4F, 1.0F)));

			LootPool.Builder vanilla_blacksmith_pool = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(BMTableLootEntry.builder(LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH));
			LootPool.Builder blacksmith_pool = LootPool.builder().rolls(RandomValueRange.of(3, 7));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(25).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_NUGGET).weight(20).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(30, 50))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(4).quality(2).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(18).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(10, 20))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(8).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

//			blacksmith_pool.
			blacksmith_pool.addEntry(ItemLootEntry.builder(BloodMagicItems.WEAK_TAU_ITEM.get()).weight(15).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

			addMultipleItemsWithSameParams(blacksmith_pool, weaponAnointments, 3, RandomValueRange.of(2, 5));
			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(25).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_NUGGET).weight(20).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(20, 40))));
			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(4).quality(2).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(18).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(10, 20))));
			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(8).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

			addMultipleItemsWithSameParams(blacksmith_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 6, ConstantRange.of(1), SetDamage.func_215931_a(RandomValueRange.of(0.4F, 7F)));
			addMultipleItemsWithQualitySameParams(blacksmith_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 4, -2, ConstantRange.of(1), EnchantWithLevels.func_215895_a(RandomValueRange.of(10, 20)).func_216059_e(), SetDamage.func_215931_a(RandomValueRange.of(0.9F, 1.0F)));
			addMultipleItemsWithSameParams(blacksmith_pool, new Item[] { Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE,
					Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 2, ConstantRange.of(1), SetDamage.func_215931_a(RandomValueRange.of(0.1F, 0.2F)));
			addMultipleItemsWithQualitySameParams(blacksmith_pool, new Item[] { Items.DIAMOND_PICKAXE,
					Items.DIAMOND_AXE, Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 1, 2, ConstantRange.of(1), EnchantWithLevels.func_215895_a(RandomValueRange.of(20, 25)).func_216059_e(), SetDamage.func_215931_a(RandomValueRange.of(0.4F, 0.8F)));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/potion_ingredients"), LootTable.builder().addLootPool(potionChest).addLootPool(addMultipleItemsWithSameParams(LootPool.builder(), baseAnointments, 1, RandomValueRange.of(2, 4))));
//			acceptor.accept(BloodMagic.rl("existing_library"), testExistingGeneration());
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/simple_armoury"), LootTable.builder().addLootPool(armory_pool));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/simple_blacksmith"), LootTable.builder().addLootPool(blacksmith_pool).addLootPool(vanilla_blacksmith_pool));

			LootPool.Builder tartaricGemPool = LootPool.builder().rolls(RandomValueRange.of(1, 2)).addEntry(ItemLootEntry.builder(BloodMagicItems.PETTY_GEM.get()).weight(5).acceptFunction(SetWillFraction.withRange(RandomValueRange.of(0.5F, 0.7F))));
//			tartaricGemPool.addEntry(TableLootEntry.builder(BloodMagic.rl("chests/dungeon/library")));
			LootPool.Builder tartaricSoulPool = LootPool.builder().rolls(RandomValueRange.of(1, 2)).addEntry(ItemLootEntry.builder(BloodMagicItems.MONSTER_SOUL_RAW.get()).weight(5).acceptFunction(SetWillRange.withRange(RandomValueRange.of(20, 50))));
			LootPool.Builder upgradePool = LootPool.builder().rolls(RandomValueRange.of(1, 2)).addEntry(ItemLootEntry.builder(BloodMagicItems.LIVING_TOME.get()).weight(3).acceptFunction(SetLivingUpgrade.withRange(RandomValueRange.of(300, 600), LivingArmorRegistrar.UPGRADE_HEALTH.get().getKey())));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/test_gems"), LootTable.builder().addLootPool(tartaricGemPool).addLootPool(tartaricSoulPool).addLootPool(upgradePool));
//			potionChest.addEntry(TableLootEntry.builder(EntityType.SHEEP.getLootTable()));

			LootPool.Builder suppliesPool = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(BMTableLootEntry.builder(LootTables.CHESTS_SHIPWRECK_SUPPLY).weight(1));
			LootPool.Builder filledTauPool = LootPool.builder().rolls(RandomValueRange.of(2, 3)).addEntry(ItemLootEntry.builder(BloodMagicItems.WEAK_TAU_ITEM.get()).weight(15).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5)))).addEntry(ItemLootEntry.builder(BloodMagicItems.TAU_OIL.get()).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(3, 7)))).addEntry(ItemLootEntry.builder(BloodMagicItems.STRONG_TAU_ITEM.get()).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/food"), LootTable.builder().addLootPool(suppliesPool).addLootPool(filledTauPool));

			LootPool.Builder farmingToolPool = LootPool.builder().rolls(RandomValueRange.of(1, 3));
			farmingToolPool.addEntry(ItemLootEntry.builder(Items.IRON_AXE).weight(20).quality(-3).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.4f, 0.6f))).acceptFunction(EnchantRandomly.func_215900_c()));
			farmingToolPool.addEntry(ItemLootEntry.builder(Items.IRON_HOE).weight(20).quality(-3).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.6f, 0.8f))).acceptFunction(EnchantRandomly.func_215900_c()));
			farmingToolPool.addEntry(ItemLootEntry.builder(Items.DIAMOND_AXE).weight(5).quality(2).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.2f, 0.4f))).acceptFunction(EnchantRandomly.func_215900_c()));
			farmingToolPool.addEntry(ItemLootEntry.builder(Items.DIAMOND_HOE).weight(5).quality(2).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.3f, 0.6f))).acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(20F, 30F))));
			farmingToolPool.addEntry(ItemLootEntry.builder(Items.NETHERITE_HOE).weight(2).quality(3).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.2f, 0.4f))).acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(30F, 40F))));

			LootPool.Builder farmAnimalProductPool = LootPool.builder().rolls(RandomValueRange.of(3, 5));
			addMultipleItemsWithSameParams(farmAnimalProductPool, new Item[] { Items.BEEF, Items.PORKCHOP,
					Items.CHICKEN, Items.EGG, Items.MUTTON, Items.RABBIT }, 10, RandomValueRange.of(5, 10));
			addMultipleItemsWithSameParams(farmAnimalProductPool, new Item[] { Items.FEATHER, Items.LEATHER,
					Items.WHITE_WOOL, Items.BLACK_WOOL, Items.RABBIT_HIDE }, 5, RandomValueRange.of(4, 15));
			farmAnimalProductPool.addEntry(ItemLootEntry.builder(Items.LEAD).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
			farmAnimalProductPool.addEntry(ItemLootEntry.builder(Items.RABBIT_FOOT).weight(2).quality(3).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))));
			farmAnimalProductPool.addEntry(ItemLootEntry.builder(Items.NAME_TAG).weight(3).acceptFunction(SetCount.builder(ConstantRange.of(1))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/farm_tools"), LootTable.builder().addLootPool(suppliesPool).addLootPool(filledTauPool).addLootPool(farmingToolPool));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/farm_parts"), LootTable.builder().addLootPool(suppliesPool).addLootPool(filledTauPool).addLootPool(farmAnimalProductPool));

//			LootPool.Builder basicBastionPool = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(BMTableLootEntry.builder(LootTables.BASTION_BRIDGE));
			LootPool.Builder basicBastionPool = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(BMTableLootEntry.builder(LootTables.BASTION_OTHER));

			LootPool.Builder extraBastionItems = LootPool.builder().name("extra").rolls(RandomValueRange.of(2, 5)).addEntry(ItemLootEntry.builder(BloodMagicItems.WEAK_TAU_ITEM.get()).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
			extraBastionItems.addEntry(ItemLootEntry.builder(BloodMagicItems.MONSTER_SOUL_RAW.get()).weight(10).acceptFunction(SetWillRange.withRange(RandomValueRange.of(15, 30))));
			extraBastionItems.addEntry(ItemLootEntry.builder(BloodMagicItems.LIVING_TOME.get()).weight(5).acceptFunction(SetLivingUpgrade.withRange(RandomValueRange.of(200, 400), LivingArmorRegistrar.UPGRADE_PHYSICAL_PROTECT.get().getKey())));
			extraBastionItems.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(15).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
			extraBastionItems.addEntry(ItemLootEntry.builder(Items.GOLD_BLOCK).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))));
			extraBastionItems.addEntry(ItemLootEntry.builder(Items.COOKED_PORKCHOP).weight(7).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

			LootPool.Builder basicNetherPool = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(BMTableLootEntry.builder(LootTables.CHESTS_NETHER_BRIDGE));

			LootPool.Builder extraNetherItems = LootPool.builder().name("extra").rolls(RandomValueRange.of(2, 5)).addEntry(ItemLootEntry.builder(BloodMagicItems.WEAK_TAU_ITEM.get()).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
			extraNetherItems.addEntry(ItemLootEntry.builder(BloodMagicItems.MONSTER_SOUL_RAW.get()).weight(10).acceptFunction(SetWillRange.withRange(RandomValueRange.of(15, 30))));
			extraNetherItems.addEntry(ItemLootEntry.builder(BloodMagicItems.LIVING_TOME.get()).weight(5).acceptFunction(SetLivingUpgrade.withRange(RandomValueRange.of(50, 200), LivingArmorRegistrar.UPGRADE_FALL_PROTECT.get().getKey())));
			extraNetherItems.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(15).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
			extraNetherItems.addEntry(ItemLootEntry.builder(Items.GOLD_BLOCK).weight(3).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))));
			extraNetherItems.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(3, 5))));
			extraNetherItems.addEntry(ItemLootEntry.builder(Items.BLAZE_ROD).weight(4).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/bastion"), LootTable.builder().addLootPool(basicBastionPool).addLootPool(extraBastionItems));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/nether"), LootTable.builder().addLootPool(basicNetherPool).addLootPool(extraNetherItems));

			LootPool.Builder desertPyramidPool = LootPool.builder().rolls(ConstantRange.of(2)).addEntry(BMTableLootEntry.builder(LootTables.CHESTS_DESERT_PYRAMID).weight(1));

			LootPool.Builder crypt_pool = LootPool.builder().rolls(RandomValueRange.of(2, 4));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(25).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_NUGGET).weight(20).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(30, 50))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(4).quality(2).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(18).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(10, 20))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(8).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

//			blacksmith_pool.
			crypt_pool.addEntry(ItemLootEntry.builder(BloodMagicItems.WEAK_TAU_ITEM.get()).weight(15).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

			addMultipleItemsWithSameParams(crypt_pool, weaponAnointments, 3, RandomValueRange.of(2, 5));
			crypt_pool.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(25).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
			crypt_pool.addEntry(ItemLootEntry.builder(Items.IRON_NUGGET).weight(20).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(20, 40))));
			crypt_pool.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(4).quality(2).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
			crypt_pool.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(18).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(10, 20))));
			crypt_pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(8).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

			addMultipleItemsWithSameParams(crypt_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 6, ConstantRange.of(1), SetDamage.func_215931_a(RandomValueRange.of(0.4F, 7F)));
			addMultipleItemsWithQualitySameParams(blacksmith_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 4, -2, ConstantRange.of(1), EnchantWithLevels.func_215895_a(RandomValueRange.of(10, 20)).func_216059_e(), SetDamage.func_215931_a(RandomValueRange.of(0.9F, 1.0F)));
			addMultipleItemsWithSameParams(crypt_pool, new Item[] { Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE,
					Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 2, ConstantRange.of(1), SetDamage.func_215931_a(RandomValueRange.of(0.1F, 0.2F)));
			addMultipleItemsWithQualitySameParams(crypt_pool, new Item[] { Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE,
					Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 1, 2, ConstantRange.of(1), EnchantWithLevels.func_215895_a(RandomValueRange.of(20, 25)).func_216059_e(), SetDamage.func_215931_a(RandomValueRange.of(0.4F, 0.8F)));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/crypt"), LootTable.builder().addLootPool(desertPyramidPool).addLootPool(crypt_pool));
		}

		private LootPool.Builder addMultipleItemsWithSameParams(LootPool.Builder pool, Item[] items, int basicWeight, IRandomRange basicRange, IBuilder... functions)
		{
			return addMultipleItemsWithQualitySameParams(pool, items, basicWeight, 0, basicRange, functions);
		}

		private LootPool.Builder addMultipleItemsWithQualitySameParams(LootPool.Builder pool, Item[] items, int basicWeight, int quality, IRandomRange basicRange, IBuilder... functions)
		{
			if (basicWeight > 0)
			{
				for (Item item : items)
				{
					StandaloneLootEntry.Builder<?> entryBuilder = ItemLootEntry.builder(item).weight(basicWeight).quality(quality).acceptFunction(SetCount.builder(basicRange));
					for (IBuilder function : functions)
					{
						entryBuilder = entryBuilder.acceptFunction(function);
					}

					pool = pool.addEntry(entryBuilder);
				}
			}

			return pool;
		}
	}

	private static class BMBlocks extends BlockLootTables
	{
		@Override
		protected void addTables()
		{
			for (RegistryObject<Block> block : BloodMagicBlocks.BASICBLOCKS.getEntries())
			{
				this.registerDropSelfLootTable(block.get());
			}

			for (RegistryObject<Block> block : BloodMagicBlocks.DUNGEONBLOCKS.getEntries())
			{
				this.registerDropSelfLootTable(block.get());
			}

			registerDropSelfLootTable(BloodMagicBlocks.BLOOD_ALTAR.get());
			registerNoDropLootTable(BloodMagicBlocks.ALCHEMY_ARRAY.get());
			registerNoDropLootTable(BloodMagicBlocks.BLOOD_LIGHT.get());
			registerDropSelfLootTable(BloodMagicBlocks.SOUL_FORGE.get());
			registerDropSelfLootTable(BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			registerDropping(BloodMagicBlocks.AIR_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			registerDropping(BloodMagicBlocks.WATER_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			registerDropping(BloodMagicBlocks.FIRE_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			registerDropping(BloodMagicBlocks.EARTH_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			registerDropping(BloodMagicBlocks.DUSK_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			registerDropping(BloodMagicBlocks.DAWN_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			registerDropSelfLootTable(BloodMagicBlocks.ALCHEMY_TABLE.get());
			registerDropSelfLootTable(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get());
			registerDropSelfLootTable(BloodMagicBlocks.DEMON_CRUCIBLE.get());
			registerDropSelfLootTable(BloodMagicBlocks.DEMON_CRYSTALLIZER.get());
			registerDropSelfLootTable(BloodMagicBlocks.INCENSE_ALTAR.get());
//			registerNoDropLootTable(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get());
			registerDropping(BloodMagicBlocks.NETHER_SOIL.get(), Blocks.NETHERRACK);

			registerDropCrystalsLootTable(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get(), BloodMagicItems.RAW_CRYSTAL.get());
			registerDropCrystalsLootTable(BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get(), BloodMagicItems.CORROSIVE_CRYSTAL.get());
			registerDropCrystalsLootTable(BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get(), BloodMagicItems.DESTRUCTIVE_CRYSTAL.get());
			registerDropCrystalsLootTable(BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get(), BloodMagicItems.VENGEFUL_CRYSTAL.get());
			registerDropCrystalsLootTable(BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get(), BloodMagicItems.STEADFAST_CRYSTAL.get());

			registerDropSelfLootTable(BloodMagicBlocks.ROUTING_NODE_BLOCK.get());
			registerDropSelfLootTable(BloodMagicBlocks.INPUT_ROUTING_NODE_BLOCK.get());
			registerDropSelfLootTable(BloodMagicBlocks.OUTPUT_ROUTING_NODE_BLOCK.get());
			registerDropSelfLootTable(BloodMagicBlocks.MASTER_ROUTING_NODE_BLOCK.get());

			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_STONE.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_BRICK_STAIRS.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_POLISHED_STAIRS.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_PILLAR_CENTER.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_PILLAR_SPECIAL.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_PILLAR_CAP.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_BRICK_WALL.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_POLISHED_WALL.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_BRICK_GATE.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_POLISHED_GATE.get());
			registerDropSelfLootTable(BloodMagicBlocks.DUNGEON_SPIKE_TRAP.get());
			registerNoDropLootTable(BloodMagicBlocks.SPIKES.get());
			registerLootTable(BloodMagicBlocks.DUNGEON_BRICK_SLAB.get(), BlockLootTables::droppingSlab);
			registerLootTable(BloodMagicBlocks.DUNGEON_TILE_SLAB.get(), BlockLootTables::droppingSlab);

			registerNoDropLootTable(BloodMagicBlocks.DUNGEON_CONTROLLER.get());
			registerNoDropLootTable(BloodMagicBlocks.DUNGEON_SEAL.get());

			registerDropSelfLootTable(BloodMagicBlocks.MIMIC.get());
			registerDropSelfLootTable(BloodMagicBlocks.ETHEREAL_MIMIC.get());

			registerCropDropLootTable(BloodMagicBlocks.GROWING_DOUBT.get(), BloodMagicItems.GROWING_DOUBT_ITEM.get());
			registerCropDropLootTable(BloodMagicBlocks.WEAK_TAU.get(), BloodMagicItems.WEAK_TAU_ITEM.get());
			registerCropDropLootTableWithImmatureSeed(BloodMagicBlocks.STRONG_TAU.get(), BloodMagicItems.WEAK_TAU_ITEM.get(), BloodMagicItems.STRONG_TAU_ITEM.get());

			registerNoDropLootTable(BloodMagicBlocks.SHAPED_CHARGE.get());
			registerNoDropLootTable(BloodMagicBlocks.DEFORESTER_CHARGE.get());
			registerNoDropLootTable(BloodMagicBlocks.VEINMINE_CHARGE.get());
			registerNoDropLootTable(BloodMagicBlocks.FUNGAL_CHARGE.get());

			registerDropSelfLootTable(BloodMagicBlocks.INVERSION_PILLAR.get());
			registerDropSelfLootTable(BloodMagicBlocks.INVERSION_PILLAR_CAP.get());
		}

		private void registerNoDropLootTable(Block block)
		{
			LootPool.Builder builder = LootPool.builder().name(block.getRegistryName().toString());
			this.registerLootTable(block, LootTable.builder().addLootPool(builder));
		}

		private void registerCropDropLootTable(Block block, Item item)
		{
			LootTable.Builder builder = LootTable.builder();

			ILootCondition.IBuilder ageLootCondition = BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(CropsBlock.AGE, 7));
			builder = builder.addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(item))).addLootPool(LootPool.builder().acceptCondition(ageLootCondition).addEntry(ItemLootEntry.builder(item).acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3))));
//		      this.registerLootTable(Blocks.POTATOES, withExplosionDecay(Blocks.POTATOES, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(Items.POTATO))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POTATO).acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3)))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).acceptCondition(RandomChance.builder(0.02F))))));

			this.registerLootTable(block, builder);
		}

		private void registerCropDropLootTableWithImmatureSeed(Block block, Item seed, Item fruit)
		{
			LootTable.Builder builder = LootTable.builder();

			ILootCondition.IBuilder ageLootCondition = BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(CropsBlock.AGE, 7));
			builder = builder.addLootPool(LootPool.builder().acceptCondition(ageLootCondition.inverted()).addEntry(ItemLootEntry.builder(seed))).addLootPool(LootPool.builder().acceptCondition(ageLootCondition).addEntry(ItemLootEntry.builder(fruit))).addLootPool(LootPool.builder().acceptCondition(ageLootCondition).addEntry(ItemLootEntry.builder(fruit).acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3))));
//		      this.registerLootTable(Blocks.POTATOES, withExplosionDecay(Blocks.POTATOES, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(Items.POTATO))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POTATO).acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3)))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).acceptCondition(RandomChance.builder(0.02F))))));

			this.registerLootTable(block, builder);
		}

		private void registerDropCrystalsLootTable(Block block, Item item)
		{
			LootTable.Builder builder = LootTable.builder();

			for (int i = 0; i < 7; i++)
			{
				ILootCondition.IBuilder harvestAge = BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(BlockDemonCrystal.AGE, i));
				builder = builder.addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(item).acceptFunction(SetCount.builder(ConstantRange.of(i + 1))).acceptCondition(harvestAge)));
			}

			this.registerLootTable(block, builder);
		}

		protected static <T extends Comparable<T> & IStringSerializable> LootTable.Builder droppingWhen(Block block, Property<T> property, T value)
		{
			return LootTable.builder().addLootPool(withSurvivesExplosion(block, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block).acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(property, value))))));
		}

		@Override
		protected Iterable<Block> getKnownBlocks()
		{
			return ForgeRegistries.BLOCKS.getValues().stream().filter(b -> b.getRegistryName().getNamespace().equals(BloodMagic.MODID)).collect(Collectors.toList());
		}
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker)
	{
		map.forEach((name, table) -> LootTableManager.validateLootTable(validationtracker, name, table));
	}
}