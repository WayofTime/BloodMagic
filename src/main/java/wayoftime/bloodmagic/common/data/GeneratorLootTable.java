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
			LootPool.Builder vanillaDungeon = LootPool.lootPool().name("vanilla_dungeon").setRolls(ConstantRange.exactly(1)).add(BMTableLootEntry.builder(LootTables.SIMPLE_DUNGEON).setWeight(1));
			LootPool.Builder key_pool = LootPool.lootPool().name("keys").setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(BloodMagicItems.DUNGEON_SIMPLE_KEY.get()).setWeight(1).apply(SetCount.setCount(RandomValueRange.between(1, 3))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/entrance_chest"), LootTable.lootTable().withPool(key_pool).withPool(vanillaDungeon));

			Item[] baseAnointments = new Item[] { BloodMagicItems.BOW_POWER_ANOINTMENT.get(),
					BloodMagicItems.FORTUNE_ANOINTMENT.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get(),
					BloodMagicItems.HOLY_WATER_ANOINTMENT.get(), BloodMagicItems.LOOTING_ANOINTMENT.get(),
					BloodMagicItems.MELEE_DAMAGE_ANOINTMENT.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT.get(),
					BloodMagicItems.SILK_TOUCH_ANOINTMENT.get(), BloodMagicItems.SMELTING_ANOINTMENT.get() };

			Item[] weaponAnointments = new Item[] { BloodMagicItems.BOW_POWER_ANOINTMENT.get(),
					BloodMagicItems.HOLY_WATER_ANOINTMENT.get(), BloodMagicItems.LOOTING_ANOINTMENT.get(),
					BloodMagicItems.MELEE_DAMAGE_ANOINTMENT.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT.get(),
					BloodMagicItems.SMELTING_ANOINTMENT.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT.get() };

			LootPool.Builder stronghold_library_pool = LootPool.lootPool().name("vanilla_library").setRolls(ConstantRange.exactly(2)).add(BMTableLootEntry.builder(LootTables.STRONGHOLD_LIBRARY).setWeight(1));
			LootPool.Builder extraLibraryItems = LootPool.lootPool().name("extra").setRolls(RandomValueRange.between(2, 3)).add(ItemLootEntry.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(20).apply(SetCount.setCount(RandomValueRange.between(2, 5))));
			extraLibraryItems.add(ItemLootEntry.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(10).apply(SetWillRange.withRange(RandomValueRange.between(15, 30))));
			extraLibraryItems.add(ItemLootEntry.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(5).apply(SetLivingUpgrade.withRange(RandomValueRange.between(200, 400), LivingArmorRegistrar.UPGRADE_EXPERIENCE.get().getKey())));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/library"), LootTable.lootTable().withPool(stronghold_library_pool).withPool(extraLibraryItems));

			LootPool.Builder potionChest = LootPool.lootPool().setRolls(RandomValueRange.between(5, 7)).add(ItemLootEntry.lootTableItem(Items.NETHER_WART).setWeight(40).apply(SetCount.setCount(RandomValueRange.between(3, 7)))).add(ItemLootEntry.lootTableItem(Items.BLAZE_POWDER).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(7, 10)))).add(ItemLootEntry.lootTableItem(Items.BLAZE_POWDER).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(3, 7)))).add(ItemLootEntry.lootTableItem(Items.POTION).setWeight(3).apply(SetNBT.setTag(Util.make(new CompoundNBT(), (nbt) -> {
				nbt.putString("Potion", "minecraft:water");
			})))).add(ItemLootEntry.lootTableItem(Items.SLIME_BALL).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(10, 15)))).add(ItemLootEntry.lootTableItem(Items.MAGMA_CREAM).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(6, 10)))).add(ItemLootEntry.lootTableItem(Items.GUNPOWDER).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(6, 10)))).add(ItemLootEntry.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(10, 20)))).add(ItemLootEntry.lootTableItem(Items.GLOWSTONE_DUST).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(20, 30))));
			potionChest = addMultipleItemsWithSameParams(potionChest, baseAnointments, 1, RandomValueRange.between(1, 3));

//			potionChest.addEntry(BMTableLootEntry.builder(LootTables.BASTION_BRIDGE).weight(1000));
//			potionChest.addEntry(EmptyLootEntry.emptyItem());

			LootPool.Builder armory_pool = LootPool.lootPool().setRolls(RandomValueRange.between(5, 7));

			armory_pool.add(ItemLootEntry.lootTableItem(Items.IRON_INGOT).setWeight(25).setQuality(1).apply(SetCount.setCount(RandomValueRange.between(7, 15))));
			armory_pool.add(ItemLootEntry.lootTableItem(Items.IRON_NUGGET).setWeight(20).setQuality(-4).apply(SetCount.setCount(RandomValueRange.between(30, 50))));
			armory_pool.add(ItemLootEntry.lootTableItem(Items.DIAMOND).setWeight(4).setQuality(2).apply(SetCount.setCount(RandomValueRange.between(2, 5))));
			armory_pool.add(ItemLootEntry.lootTableItem(Items.LEATHER).setWeight(18).setQuality(-4).apply(SetCount.setCount(RandomValueRange.between(10, 20))));
			armory_pool.add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT).setWeight(8).setQuality(1).apply(SetCount.setCount(RandomValueRange.between(4, 7))));

			addMultipleItemsWithQualitySameParams(armory_pool, new Item[] { Items.LEATHER_BOOTS,
					Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET,
					Items.LEATHER_LEGGINGS }, 4, -3, ConstantRange.exactly(1), EnchantWithLevels.enchantWithLevels(RandomValueRange.between(15, 25)).allowTreasure(), SetDamage.setDamage(RandomValueRange.between(0.3F, 0.9F)));
			addMultipleItemsWithSameParams(armory_pool, new Item[] { Items.LEATHER_BOOTS, Items.LEATHER_CHESTPLATE,
					Items.LEATHER_HELMET,
					Items.LEATHER_LEGGINGS }, 7, ConstantRange.exactly(1), SetDamage.setDamage(RandomValueRange.between(0.8F, 1.0F)));
			addMultipleItemsWithSameParams(armory_pool, new Item[] { Items.IRON_BOOTS, Items.IRON_CHESTPLATE,
					Items.IRON_HELMET,
					Items.IRON_LEGGINGS }, 6, ConstantRange.exactly(1), SetDamage.setDamage(RandomValueRange.between(0.2F, 0.5F)));

			addMultipleItemsWithQualitySameParams(armory_pool, new Item[] { Items.IRON_BOOTS, Items.IRON_CHESTPLATE,
					Items.IRON_LEGGINGS,
					Items.IRON_HELMET }, 4, -2, ConstantRange.exactly(1), EnchantWithLevels.enchantWithLevels(RandomValueRange.between(10, 20)).allowTreasure(), SetDamage.setDamage(RandomValueRange.between(0.9F, 1.0F)));
			addMultipleItemsWithSameParams(armory_pool, new Item[] { Items.DIAMOND_BOOTS, Items.DIAMOND_CHESTPLATE,
					Items.DIAMOND_HELMET,
					Items.DIAMOND_LEGGINGS }, 2, ConstantRange.exactly(1), SetDamage.setDamage(RandomValueRange.between(0.1F, 0.2F)));
			addMultipleItemsWithQualitySameParams(armory_pool, new Item[] { Items.DIAMOND_BOOTS,
					Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET,
					Items.DIAMOND_LEGGINGS }, 1, 2, ConstantRange.exactly(1), EnchantWithLevels.enchantWithLevels(RandomValueRange.between(20, 25)).allowTreasure(), SetDamage.setDamage(RandomValueRange.between(0.4F, 1.0F)));

			LootPool.Builder vanilla_blacksmith_pool = LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(BMTableLootEntry.builder(LootTables.VILLAGE_WEAPONSMITH));
			LootPool.Builder blacksmith_pool = LootPool.lootPool().setRolls(RandomValueRange.between(3, 7));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(25).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_NUGGET).weight(20).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(30, 50))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(4).quality(2).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(18).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(10, 20))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(8).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

//			blacksmith_pool.
			blacksmith_pool.add(ItemLootEntry.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(4, 7))));

			addMultipleItemsWithSameParams(blacksmith_pool, weaponAnointments, 3, RandomValueRange.between(2, 5));
			blacksmith_pool.add(ItemLootEntry.lootTableItem(Items.IRON_INGOT).setWeight(25).setQuality(1).apply(SetCount.setCount(RandomValueRange.between(7, 15))));
			blacksmith_pool.add(ItemLootEntry.lootTableItem(Items.IRON_NUGGET).setWeight(20).setQuality(-4).apply(SetCount.setCount(RandomValueRange.between(20, 40))));
			blacksmith_pool.add(ItemLootEntry.lootTableItem(Items.DIAMOND).setWeight(4).setQuality(2).apply(SetCount.setCount(RandomValueRange.between(2, 5))));
			blacksmith_pool.add(ItemLootEntry.lootTableItem(Items.LEATHER).setWeight(18).setQuality(-4).apply(SetCount.setCount(RandomValueRange.between(10, 20))));
			blacksmith_pool.add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT).setWeight(8).setQuality(1).apply(SetCount.setCount(RandomValueRange.between(4, 7))));

			addMultipleItemsWithSameParams(blacksmith_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 6, ConstantRange.exactly(1), SetDamage.setDamage(RandomValueRange.between(0.4F, 7F)));
			addMultipleItemsWithQualitySameParams(blacksmith_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 4, -2, ConstantRange.exactly(1), EnchantWithLevels.enchantWithLevels(RandomValueRange.between(10, 20)).allowTreasure(), SetDamage.setDamage(RandomValueRange.between(0.9F, 1.0F)));
			addMultipleItemsWithSameParams(blacksmith_pool, new Item[] { Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE,
					Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 2, ConstantRange.exactly(1), SetDamage.setDamage(RandomValueRange.between(0.1F, 0.2F)));
			addMultipleItemsWithQualitySameParams(blacksmith_pool, new Item[] { Items.DIAMOND_PICKAXE,
					Items.DIAMOND_AXE, Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 1, 2, ConstantRange.exactly(1), EnchantWithLevels.enchantWithLevels(RandomValueRange.between(20, 25)).allowTreasure(), SetDamage.setDamage(RandomValueRange.between(0.4F, 0.8F)));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/potion_ingredients"), LootTable.lootTable().withPool(potionChest).withPool(addMultipleItemsWithSameParams(LootPool.lootPool(), baseAnointments, 1, RandomValueRange.between(2, 4))));
//			acceptor.accept(BloodMagic.rl("existing_library"), testExistingGeneration());
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/simple_armoury"), LootTable.lootTable().withPool(armory_pool));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/simple_blacksmith"), LootTable.lootTable().withPool(blacksmith_pool).withPool(vanilla_blacksmith_pool));

			LootPool.Builder tartaricGemPool = LootPool.lootPool().setRolls(RandomValueRange.between(1, 2)).add(ItemLootEntry.lootTableItem(BloodMagicItems.PETTY_GEM.get()).setWeight(5).apply(SetWillFraction.withRange(RandomValueRange.between(0.5F, 0.7F))));
//			tartaricGemPool.addEntry(TableLootEntry.builder(BloodMagic.rl("chests/dungeon/library")));
			LootPool.Builder tartaricSoulPool = LootPool.lootPool().setRolls(RandomValueRange.between(1, 2)).add(ItemLootEntry.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(5).apply(SetWillRange.withRange(RandomValueRange.between(20, 50))));
			LootPool.Builder upgradePool = LootPool.lootPool().setRolls(RandomValueRange.between(1, 2)).add(ItemLootEntry.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(3).apply(SetLivingUpgrade.withRange(RandomValueRange.between(300, 600), LivingArmorRegistrar.UPGRADE_HEALTH.get().getKey())));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/test_gems"), LootTable.lootTable().withPool(tartaricGemPool).withPool(tartaricSoulPool).withPool(upgradePool));
//			potionChest.addEntry(TableLootEntry.builder(EntityType.SHEEP.getLootTable()));

			LootPool.Builder suppliesPool = LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(BMTableLootEntry.builder(LootTables.SHIPWRECK_SUPPLY).setWeight(1));
			LootPool.Builder filledTauPool = LootPool.lootPool().setRolls(RandomValueRange.between(2, 3)).add(ItemLootEntry.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(2, 5)))).add(ItemLootEntry.lootTableItem(BloodMagicItems.TAU_OIL.get()).setWeight(10).apply(SetCount.setCount(RandomValueRange.between(3, 7)))).add(ItemLootEntry.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(5).apply(SetCount.setCount(RandomValueRange.between(2, 5))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/food"), LootTable.lootTable().withPool(suppliesPool).withPool(filledTauPool));

			LootPool.Builder farmingToolPool = LootPool.lootPool().setRolls(RandomValueRange.between(1, 3));
			farmingToolPool.add(ItemLootEntry.lootTableItem(Items.IRON_AXE).setWeight(20).setQuality(-3).apply(SetDamage.setDamage(RandomValueRange.between(0.4f, 0.6f))).apply(EnchantRandomly.randomApplicableEnchantment()));
			farmingToolPool.add(ItemLootEntry.lootTableItem(Items.IRON_HOE).setWeight(20).setQuality(-3).apply(SetDamage.setDamage(RandomValueRange.between(0.6f, 0.8f))).apply(EnchantRandomly.randomApplicableEnchantment()));
			farmingToolPool.add(ItemLootEntry.lootTableItem(Items.DIAMOND_AXE).setWeight(5).setQuality(2).apply(SetDamage.setDamage(RandomValueRange.between(0.2f, 0.4f))).apply(EnchantRandomly.randomApplicableEnchantment()));
			farmingToolPool.add(ItemLootEntry.lootTableItem(Items.DIAMOND_HOE).setWeight(5).setQuality(2).apply(SetDamage.setDamage(RandomValueRange.between(0.3f, 0.6f))).apply(EnchantWithLevels.enchantWithLevels(RandomValueRange.between(20F, 30F))));
			farmingToolPool.add(ItemLootEntry.lootTableItem(Items.NETHERITE_HOE).setWeight(2).setQuality(3).apply(SetDamage.setDamage(RandomValueRange.between(0.2f, 0.4f))).apply(EnchantWithLevels.enchantWithLevels(RandomValueRange.between(30F, 40F))));

			LootPool.Builder farmAnimalProductPool = LootPool.lootPool().setRolls(RandomValueRange.between(3, 5));
			addMultipleItemsWithSameParams(farmAnimalProductPool, new Item[] { Items.BEEF, Items.PORKCHOP,
					Items.CHICKEN, Items.EGG, Items.MUTTON, Items.RABBIT }, 10, RandomValueRange.between(5, 10));
			addMultipleItemsWithSameParams(farmAnimalProductPool, new Item[] { Items.FEATHER, Items.LEATHER,
					Items.WHITE_WOOL, Items.BLACK_WOOL, Items.RABBIT_HIDE }, 5, RandomValueRange.between(4, 15));
			farmAnimalProductPool.add(ItemLootEntry.lootTableItem(Items.LEAD).setWeight(6).apply(SetCount.setCount(RandomValueRange.between(2, 5))));
			farmAnimalProductPool.add(ItemLootEntry.lootTableItem(Items.RABBIT_FOOT).setWeight(2).setQuality(3).apply(SetCount.setCount(RandomValueRange.between(1, 3))));
			farmAnimalProductPool.add(ItemLootEntry.lootTableItem(Items.NAME_TAG).setWeight(3).apply(SetCount.setCount(ConstantRange.exactly(1))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/farm_tools"), LootTable.lootTable().withPool(suppliesPool).withPool(filledTauPool).withPool(farmingToolPool));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/farm_parts"), LootTable.lootTable().withPool(suppliesPool).withPool(filledTauPool).withPool(farmAnimalProductPool));

//			LootPool.Builder basicBastionPool = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(BMTableLootEntry.builder(LootTables.BASTION_BRIDGE));
			LootPool.Builder basicBastionPool = LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(BMTableLootEntry.builder(LootTables.BASTION_OTHER));

			LootPool.Builder extraBastionItems = LootPool.lootPool().name("extra").setRolls(RandomValueRange.between(2, 5)).add(ItemLootEntry.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(20).apply(SetCount.setCount(RandomValueRange.between(2, 5))));
			extraBastionItems.add(ItemLootEntry.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(10).apply(SetWillRange.withRange(RandomValueRange.between(15, 30))));
			extraBastionItems.add(ItemLootEntry.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(5).apply(SetLivingUpgrade.withRange(RandomValueRange.between(200, 400), LivingArmorRegistrar.UPGRADE_PHYSICAL_PROTECT.get().getKey())));
			extraBastionItems.add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(7, 15))));
			extraBastionItems.add(ItemLootEntry.lootTableItem(Items.GOLD_BLOCK).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(1, 3))));
			extraBastionItems.add(ItemLootEntry.lootTableItem(Items.COOKED_PORKCHOP).setWeight(7).apply(SetCount.setCount(RandomValueRange.between(4, 7))));

			LootPool.Builder basicNetherPool = LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(BMTableLootEntry.builder(LootTables.NETHER_BRIDGE));

			LootPool.Builder extraNetherItems = LootPool.lootPool().name("extra").setRolls(RandomValueRange.between(2, 5)).add(ItemLootEntry.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(20).apply(SetCount.setCount(RandomValueRange.between(2, 5))));
			extraNetherItems.add(ItemLootEntry.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(10).apply(SetWillRange.withRange(RandomValueRange.between(15, 30))));
			extraNetherItems.add(ItemLootEntry.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(5).apply(SetLivingUpgrade.withRange(RandomValueRange.between(50, 200), LivingArmorRegistrar.UPGRADE_FALL_PROTECT.get().getKey())));
			extraNetherItems.add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(7, 15))));
			extraNetherItems.add(ItemLootEntry.lootTableItem(Items.GOLD_BLOCK).setWeight(3).apply(SetCount.setCount(RandomValueRange.between(1, 3))));
			extraNetherItems.add(ItemLootEntry.lootTableItem(Items.DIAMOND).setWeight(2).apply(SetCount.setCount(RandomValueRange.between(3, 5))));
			extraNetherItems.add(ItemLootEntry.lootTableItem(Items.BLAZE_ROD).setWeight(4).apply(SetCount.setCount(RandomValueRange.between(4, 7))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/bastion"), LootTable.lootTable().withPool(basicBastionPool).withPool(extraBastionItems));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/nether"), LootTable.lootTable().withPool(basicNetherPool).withPool(extraNetherItems));

			LootPool.Builder desertPyramidPool = LootPool.lootPool().setRolls(ConstantRange.exactly(2)).add(BMTableLootEntry.builder(LootTables.DESERT_PYRAMID).setWeight(1));

			LootPool.Builder crypt_pool = LootPool.lootPool().setRolls(RandomValueRange.between(2, 4));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(25).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_NUGGET).weight(20).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(30, 50))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(4).quality(2).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(18).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(10, 20))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(8).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

//			blacksmith_pool.
			crypt_pool.add(ItemLootEntry.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(15).apply(SetCount.setCount(RandomValueRange.between(4, 7))));

			addMultipleItemsWithSameParams(crypt_pool, weaponAnointments, 3, RandomValueRange.between(2, 5));
			crypt_pool.add(ItemLootEntry.lootTableItem(Items.IRON_INGOT).setWeight(25).setQuality(1).apply(SetCount.setCount(RandomValueRange.between(7, 15))));
			crypt_pool.add(ItemLootEntry.lootTableItem(Items.IRON_NUGGET).setWeight(20).setQuality(-4).apply(SetCount.setCount(RandomValueRange.between(20, 40))));
			crypt_pool.add(ItemLootEntry.lootTableItem(Items.DIAMOND).setWeight(4).setQuality(2).apply(SetCount.setCount(RandomValueRange.between(2, 5))));
			crypt_pool.add(ItemLootEntry.lootTableItem(Items.LEATHER).setWeight(18).setQuality(-4).apply(SetCount.setCount(RandomValueRange.between(10, 20))));
			crypt_pool.add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT).setWeight(8).setQuality(1).apply(SetCount.setCount(RandomValueRange.between(4, 7))));

			addMultipleItemsWithSameParams(crypt_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 6, ConstantRange.exactly(1), SetDamage.setDamage(RandomValueRange.between(0.4F, 7F)));
			addMultipleItemsWithQualitySameParams(blacksmith_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 4, -2, ConstantRange.exactly(1), EnchantWithLevels.enchantWithLevels(RandomValueRange.between(10, 20)).allowTreasure(), SetDamage.setDamage(RandomValueRange.between(0.9F, 1.0F)));
			addMultipleItemsWithSameParams(crypt_pool, new Item[] { Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE,
					Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 2, ConstantRange.exactly(1), SetDamage.setDamage(RandomValueRange.between(0.1F, 0.2F)));
			addMultipleItemsWithQualitySameParams(crypt_pool, new Item[] { Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE,
					Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 1, 2, ConstantRange.exactly(1), EnchantWithLevels.enchantWithLevels(RandomValueRange.between(20, 25)).allowTreasure(), SetDamage.setDamage(RandomValueRange.between(0.4F, 0.8F)));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/crypt"), LootTable.lootTable().withPool(desertPyramidPool).withPool(crypt_pool));
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
					StandaloneLootEntry.Builder<?> entryBuilder = ItemLootEntry.lootTableItem(item).setWeight(basicWeight).setQuality(quality).apply(SetCount.setCount(basicRange));
					for (IBuilder function : functions)
					{
						entryBuilder = entryBuilder.apply(function);
					}

					pool = pool.add(entryBuilder);
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
				this.dropSelf(block.get());
			}

			for (RegistryObject<Block> block : BloodMagicBlocks.DUNGEONBLOCKS.getEntries())
			{
				this.dropSelf(block.get());
			}

			dropSelf(BloodMagicBlocks.BLOOD_ALTAR.get());
			registerNoDropLootTable(BloodMagicBlocks.ALCHEMY_ARRAY.get());
			registerNoDropLootTable(BloodMagicBlocks.BLOOD_LIGHT.get());
			dropSelf(BloodMagicBlocks.SOUL_FORGE.get());
			dropSelf(BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			dropOther(BloodMagicBlocks.AIR_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			dropOther(BloodMagicBlocks.WATER_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			dropOther(BloodMagicBlocks.FIRE_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			dropOther(BloodMagicBlocks.EARTH_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			dropOther(BloodMagicBlocks.DUSK_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			dropOther(BloodMagicBlocks.DAWN_RITUAL_STONE.get(), BloodMagicBlocks.BLANK_RITUAL_STONE.get());
			dropSelf(BloodMagicBlocks.ALCHEMY_TABLE.get());
			dropSelf(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get());
			dropSelf(BloodMagicBlocks.DEMON_CRUCIBLE.get());
			dropSelf(BloodMagicBlocks.DEMON_CRYSTALLIZER.get());
			dropSelf(BloodMagicBlocks.DEMON_PYLON.get());
			dropSelf(BloodMagicBlocks.INCENSE_ALTAR.get());
//			registerNoDropLootTable(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get());
			dropOther(BloodMagicBlocks.NETHER_SOIL.get(), Blocks.NETHERRACK);

			registerDropCrystalsLootTable(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get(), BloodMagicItems.RAW_CRYSTAL.get());
			registerDropCrystalsLootTable(BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get(), BloodMagicItems.CORROSIVE_CRYSTAL.get());
			registerDropCrystalsLootTable(BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get(), BloodMagicItems.DESTRUCTIVE_CRYSTAL.get());
			registerDropCrystalsLootTable(BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get(), BloodMagicItems.VENGEFUL_CRYSTAL.get());
			registerDropCrystalsLootTable(BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get(), BloodMagicItems.STEADFAST_CRYSTAL.get());

			dropSelf(BloodMagicBlocks.ROUTING_NODE_BLOCK.get());
			dropSelf(BloodMagicBlocks.INPUT_ROUTING_NODE_BLOCK.get());
			dropSelf(BloodMagicBlocks.OUTPUT_ROUTING_NODE_BLOCK.get());
			dropSelf(BloodMagicBlocks.MASTER_ROUTING_NODE_BLOCK.get());

			dropSelf(BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get());
			dropSelf(BloodMagicBlocks.DUNGEON_STONE.get());
			dropSelf(BloodMagicBlocks.DUNGEON_BRICK_STAIRS.get());
			dropSelf(BloodMagicBlocks.DUNGEON_POLISHED_STAIRS.get());
			dropSelf(BloodMagicBlocks.DUNGEON_PILLAR_CENTER.get());
			dropSelf(BloodMagicBlocks.DUNGEON_PILLAR_SPECIAL.get());
			dropSelf(BloodMagicBlocks.DUNGEON_PILLAR_CAP.get());
			dropSelf(BloodMagicBlocks.DUNGEON_BRICK_WALL.get());
			dropSelf(BloodMagicBlocks.DUNGEON_POLISHED_WALL.get());
			dropSelf(BloodMagicBlocks.DUNGEON_BRICK_GATE.get());
			dropSelf(BloodMagicBlocks.DUNGEON_POLISHED_GATE.get());
			dropSelf(BloodMagicBlocks.DUNGEON_SPIKE_TRAP.get());
			registerNoDropLootTable(BloodMagicBlocks.SPIKES.get());
			add(BloodMagicBlocks.DUNGEON_BRICK_SLAB.get(), BlockLootTables::createSlabItemTable);
			add(BloodMagicBlocks.DUNGEON_TILE_SLAB.get(), BlockLootTables::createSlabItemTable);

			registerNoDropLootTable(BloodMagicBlocks.DUNGEON_CONTROLLER.get());
			registerNoDropLootTable(BloodMagicBlocks.DUNGEON_SEAL.get());

			dropSelf(BloodMagicBlocks.TELEPOSER.get());

			dropSelf(BloodMagicBlocks.MIMIC.get());
			dropSelf(BloodMagicBlocks.ETHEREAL_MIMIC.get());

			registerNoDropLootTable(BloodMagicBlocks.SPECTRAL.get());

			registerCropDropLootTable(BloodMagicBlocks.GROWING_DOUBT.get(), BloodMagicItems.GROWING_DOUBT_ITEM.get());
			registerCropDropLootTable(BloodMagicBlocks.WEAK_TAU.get(), BloodMagicItems.WEAK_TAU_ITEM.get());
			registerCropDropLootTableWithImmatureSeed(BloodMagicBlocks.STRONG_TAU.get(), BloodMagicItems.WEAK_TAU_ITEM.get(), BloodMagicItems.STRONG_TAU_ITEM.get());

			registerNoDropLootTable(BloodMagicBlocks.SHAPED_CHARGE.get());
			registerNoDropLootTable(BloodMagicBlocks.DEFORESTER_CHARGE.get());
			registerNoDropLootTable(BloodMagicBlocks.VEINMINE_CHARGE.get());
			registerNoDropLootTable(BloodMagicBlocks.FUNGAL_CHARGE.get());

			registerNoDropLootTable(BloodMagicBlocks.INVERSION_PILLAR.get());
			registerNoDropLootTable(BloodMagicBlocks.INVERSION_PILLAR_CAP.get());
		}

		private void registerNoDropLootTable(Block block)
		{
			LootPool.Builder builder = LootPool.lootPool().name(block.getRegistryName().toString());
			this.add(block, LootTable.lootTable().withPool(builder));
		}

		private void registerCropDropLootTable(Block block, Item item)
		{
			LootTable.Builder builder = LootTable.lootTable();

			ILootCondition.IBuilder ageLootCondition = BlockStateProperty.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropsBlock.AGE, 7));
			builder = builder.withPool(LootPool.lootPool().add(ItemLootEntry.lootTableItem(item))).withPool(LootPool.lootPool().when(ageLootCondition).add(ItemLootEntry.lootTableItem(item).apply(ApplyBonus.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))));
//		      this.registerLootTable(Blocks.POTATOES, withExplosionDecay(Blocks.POTATOES, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(Items.POTATO))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POTATO).acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3)))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).acceptCondition(RandomChance.builder(0.02F))))));

			this.add(block, builder);
		}

		private void registerCropDropLootTableWithImmatureSeed(Block block, Item seed, Item fruit)
		{
			LootTable.Builder builder = LootTable.lootTable();

			ILootCondition.IBuilder ageLootCondition = BlockStateProperty.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropsBlock.AGE, 7));
			builder = builder.withPool(LootPool.lootPool().when(ageLootCondition.invert()).add(ItemLootEntry.lootTableItem(seed))).withPool(LootPool.lootPool().when(ageLootCondition).add(ItemLootEntry.lootTableItem(fruit))).withPool(LootPool.lootPool().when(ageLootCondition).add(ItemLootEntry.lootTableItem(fruit).apply(ApplyBonus.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))));
//		      this.registerLootTable(Blocks.POTATOES, withExplosionDecay(Blocks.POTATOES, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(Items.POTATO))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POTATO).acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3)))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).acceptCondition(RandomChance.builder(0.02F))))));

			this.add(block, builder);
		}

		private void registerDropCrystalsLootTable(Block block, Item item)
		{
			LootTable.Builder builder = LootTable.lootTable();

			for (int i = 0; i < 7; i++)
			{
				ILootCondition.IBuilder harvestAge = BlockStateProperty.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockDemonCrystal.AGE, i));
				builder = builder.withPool(LootPool.lootPool().add(ItemLootEntry.lootTableItem(item).apply(SetCount.setCount(ConstantRange.exactly(i + 1))).when(harvestAge)));
			}

			this.add(block, builder);
		}

		protected static <T extends Comparable<T> & IStringSerializable> LootTable.Builder droppingWhen(Block block, Property<T> property, T value)
		{
			return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(block).when(BlockStateProperty.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, value))))));
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
		map.forEach((name, table) -> LootTableManager.validate(validationtracker, name, table));
	}
}