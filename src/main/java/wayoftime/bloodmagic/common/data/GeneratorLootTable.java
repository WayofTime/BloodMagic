package wayoftime.bloodmagic.common.data;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


import net.minecraft.Util;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.data.loot.packs.VanillaChestLoot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction.Builder;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BlockDemonCrystal;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.fluid.BloodMagicFluids;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.loot.BMTableLootEntry;
import wayoftime.bloodmagic.common.loot.SetLivingUpgrade;
import wayoftime.bloodmagic.common.loot.SetWillFraction;
import wayoftime.bloodmagic.common.loot.SetWillRange;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;

public class GeneratorLootTable extends LootTableProvider
{
	public GeneratorLootTable(PackOutput output)
	{
		super(output, Collections.emptySet(), List.of(new SubProviderEntry(BMBlocks::new, LootContextParamSets.BLOCK), new SubProviderEntry(BMLootTables::new, LootContextParamSets.CHEST)));
	}

	private static class BMLootTables extends VanillaChestLoot
	{
		@Override
		public void generate(BiConsumer<ResourceLocation, LootTable.Builder> acceptor)
		{
			generateSimpleDungeonLoot(acceptor);
		}

		private void generateSimpleDungeonLoot(BiConsumer<ResourceLocation, LootTable.Builder> acceptor)
		{
			LootPool.Builder vanillaDungeon = LootPool.lootPool().name("vanilla_dungeon").setRolls(ConstantValue.exactly(1)).add(BMTableLootEntry.builder(BuiltInLootTables.SIMPLE_DUNGEON).setWeight(1));
			LootPool.Builder key_pool = LootPool.lootPool().name("keys").setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(BloodMagicItems.DUNGEON_SIMPLE_KEY.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));

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

			LootPool.Builder stronghold_library_pool = LootPool.lootPool().name("vanilla_library").setRolls(ConstantValue.exactly(2)).add(BMTableLootEntry.builder(BuiltInLootTables.STRONGHOLD_LIBRARY).setWeight(1));
			LootPool.Builder extraLibraryItems = LootPool.lootPool().name("extra").setRolls(UniformGenerator.between(2, 3)).add(LootItem.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			extraLibraryItems.add(LootItem.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(10).apply(SetWillRange.withRange(UniformGenerator.between(15, 30))));
			extraLibraryItems.add(LootItem.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(5).apply(SetLivingUpgrade.withRange(UniformGenerator.between(200, 400), LivingArmorRegistrar.UPGRADE_EXPERIENCE.get().getKey())));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/library"), LootTable.lootTable().withPool(stronghold_library_pool).withPool(extraLibraryItems));

			LootPool.Builder potionChest = LootPool.lootPool().setRolls(UniformGenerator.between(5, 7)).add(LootItem.lootTableItem(Items.NETHER_WART).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7)))).add(LootItem.lootTableItem(Items.BLAZE_POWDER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 10)))).add(LootItem.lootTableItem(Items.BLAZE_POWDER).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7)))).add(LootItem.lootTableItem(Items.POTION).setWeight(3).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
				nbt.putString("Potion", "minecraft:water");
			})))).add(LootItem.lootTableItem(Items.SLIME_BALL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 15)))).add(LootItem.lootTableItem(Items.MAGMA_CREAM).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 10)))).add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 10)))).add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20)))).add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(20, 30))));
			potionChest = addMultipleItemsWithSameParams(potionChest, baseAnointments, 1, UniformGenerator.between(1, 3));

//			potionChest.addEntry(BMTableLootEntry.builder(LootTables.BASTION_BRIDGE).weight(1000));
//			potionChest.addEntry(EmptyLootEntry.emptyItem());

			LootPool.Builder armory_pool = LootPool.lootPool().setRolls(UniformGenerator.between(5, 7));

			armory_pool.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(25).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 15))));
			armory_pool.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(20).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(30, 50))));
			armory_pool.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(4).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			armory_pool.add(LootItem.lootTableItem(Items.LEATHER).setWeight(18).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))));
			armory_pool.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(8).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));

			addMultipleItemsWithQualitySameParams(armory_pool, new Item[] { Items.LEATHER_BOOTS,
					Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET,
					Items.LEATHER_LEGGINGS }, 4, -3, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(15, 25)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.3F, 0.9F)));
			addMultipleItemsWithSameParams(armory_pool, new Item[] { Items.LEATHER_BOOTS, Items.LEATHER_CHESTPLATE,
					Items.LEATHER_HELMET,
					Items.LEATHER_LEGGINGS }, 7, ConstantValue.exactly(1), SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F)));
			addMultipleItemsWithSameParams(armory_pool, new Item[] { Items.IRON_BOOTS, Items.IRON_CHESTPLATE,
					Items.IRON_HELMET,
					Items.IRON_LEGGINGS }, 6, ConstantValue.exactly(1), SetItemDamageFunction.setDamage(UniformGenerator.between(0.2F, 0.5F)));

			addMultipleItemsWithQualitySameParams(armory_pool, new Item[] { Items.IRON_BOOTS, Items.IRON_CHESTPLATE,
					Items.IRON_LEGGINGS,
					Items.IRON_HELMET }, 4, -2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 20)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.9F, 1.0F)));
			addMultipleItemsWithSameParams(armory_pool, new Item[] { Items.DIAMOND_BOOTS, Items.DIAMOND_CHESTPLATE,
					Items.DIAMOND_HELMET,
					Items.DIAMOND_LEGGINGS }, 2, ConstantValue.exactly(1), SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.2F)));
			addMultipleItemsWithQualitySameParams(armory_pool, new Item[] { Items.DIAMOND_BOOTS,
					Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET,
					Items.DIAMOND_LEGGINGS }, 1, 2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20, 25)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.4F, 1.0F)));

			LootPool.Builder vanilla_blacksmith_pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(BMTableLootEntry.builder(BuiltInLootTables.VILLAGE_WEAPONSMITH));
			LootPool.Builder blacksmith_pool = LootPool.lootPool().setRolls(UniformGenerator.between(3, 7));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(25).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_NUGGET).weight(20).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(30, 50))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(4).quality(2).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(18).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(10, 20))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(8).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

//			blacksmith_pool.
			blacksmith_pool.add(LootItem.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));

			addMultipleItemsWithSameParams(blacksmith_pool, weaponAnointments, 3, UniformGenerator.between(2, 5));
			blacksmith_pool.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(25).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 15))));
			blacksmith_pool.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(20).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(20, 40))));
			blacksmith_pool.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(4).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			blacksmith_pool.add(LootItem.lootTableItem(Items.LEATHER).setWeight(18).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))));
			blacksmith_pool.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(8).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));

			addMultipleItemsWithSameParams(blacksmith_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 6, ConstantValue.exactly(1), SetItemDamageFunction.setDamage(UniformGenerator.between(0.4F, 7F)));
			addMultipleItemsWithQualitySameParams(blacksmith_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 4, -2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 20)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.9F, 1.0F)));
			addMultipleItemsWithSameParams(blacksmith_pool, new Item[] { Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE,
					Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 2, ConstantValue.exactly(1), SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.2F)));
			addMultipleItemsWithQualitySameParams(blacksmith_pool, new Item[] { Items.DIAMOND_PICKAXE,
					Items.DIAMOND_AXE, Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 1, 2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20, 25)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.4F, 0.8F)));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/potion_ingredients"), LootTable.lootTable().withPool(potionChest).withPool(addMultipleItemsWithSameParams(LootPool.lootPool(), baseAnointments, 1, UniformGenerator.between(2, 4))));
//			acceptor.accept(BloodMagic.rl("existing_library"), testExistingGeneration());
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/simple_armoury"), LootTable.lootTable().withPool(armory_pool));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/simple_blacksmith"), LootTable.lootTable().withPool(blacksmith_pool).withPool(vanilla_blacksmith_pool));

			LootPool.Builder tartaricGemPool = LootPool.lootPool().setRolls(UniformGenerator.between(1, 2)).add(LootItem.lootTableItem(BloodMagicItems.PETTY_GEM.get()).setWeight(5).apply(SetWillFraction.withRange(UniformGenerator.between(0.5F, 0.7F))));
//			tartaricGemPool.addEntry(TableLootEntry.builder(BloodMagic.rl("chests/dungeon/library")));
			LootPool.Builder tartaricSoulPool = LootPool.lootPool().setRolls(UniformGenerator.between(1, 2)).add(LootItem.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(5).apply(SetWillRange.withRange(UniformGenerator.between(20, 50))));
			LootPool.Builder upgradePool = LootPool.lootPool().setRolls(UniformGenerator.between(1, 2)).add(LootItem.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(3).apply(SetLivingUpgrade.withRange(UniformGenerator.between(300, 600), LivingArmorRegistrar.UPGRADE_HEALTH.get().getKey())));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/test_gems"), LootTable.lootTable().withPool(tartaricGemPool).withPool(tartaricSoulPool).withPool(upgradePool));
//			potionChest.addEntry(TableLootEntry.builder(EntityType.SHEEP.getLootTable()));

			LootPool.Builder suppliesPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(BMTableLootEntry.builder(BuiltInLootTables.SHIPWRECK_SUPPLY).setWeight(1));
			LootPool.Builder filledTauPool = LootPool.lootPool().setRolls(UniformGenerator.between(2, 3)).add(LootItem.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))).add(LootItem.lootTableItem(BloodMagicItems.TAU_OIL.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7)))).add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/food"), LootTable.lootTable().withPool(suppliesPool).withPool(filledTauPool));

			LootPool.Builder farmingToolPool = LootPool.lootPool().setRolls(UniformGenerator.between(1, 3));
			farmingToolPool.add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(20).setQuality(-3).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.4f, 0.6f))).apply(EnchantRandomlyFunction.randomApplicableEnchantment()));
			farmingToolPool.add(LootItem.lootTableItem(Items.IRON_HOE).setWeight(20).setQuality(-3).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.6f, 0.8f))).apply(EnchantRandomlyFunction.randomApplicableEnchantment()));
			farmingToolPool.add(LootItem.lootTableItem(Items.DIAMOND_AXE).setWeight(5).setQuality(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.2f, 0.4f))).apply(EnchantRandomlyFunction.randomApplicableEnchantment()));
			farmingToolPool.add(LootItem.lootTableItem(Items.DIAMOND_HOE).setWeight(5).setQuality(2).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.3f, 0.6f))).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20F, 30F))));
			farmingToolPool.add(LootItem.lootTableItem(Items.NETHERITE_HOE).setWeight(2).setQuality(3).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.2f, 0.4f))).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(30F, 40F))));

			LootPool.Builder farmAnimalProductPool = LootPool.lootPool().setRolls(UniformGenerator.between(3, 5));
			addMultipleItemsWithSameParams(farmAnimalProductPool, new Item[] { Items.BEEF, Items.PORKCHOP,
					Items.CHICKEN, Items.EGG, Items.MUTTON, Items.RABBIT }, 10, UniformGenerator.between(5, 10));
			addMultipleItemsWithSameParams(farmAnimalProductPool, new Item[] { Items.FEATHER, Items.LEATHER,
					Items.WHITE_WOOL, Items.BLACK_WOOL, Items.RABBIT_HIDE }, 5, UniformGenerator.between(4, 15));
			farmAnimalProductPool.add(LootItem.lootTableItem(Items.LEAD).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			farmAnimalProductPool.add(LootItem.lootTableItem(Items.RABBIT_FOOT).setWeight(2).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));
			farmAnimalProductPool.add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(3).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/farm_tools"), LootTable.lootTable().withPool(suppliesPool).withPool(filledTauPool).withPool(farmingToolPool));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/farm_parts"), LootTable.lootTable().withPool(suppliesPool).withPool(filledTauPool).withPool(farmAnimalProductPool));

//			LootPool.Builder basicBastionPool = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(BMTableLootEntry.builder(LootTables.BASTION_BRIDGE));
			LootPool.Builder basicBastionPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(BMTableLootEntry.builder(BuiltInLootTables.BASTION_OTHER));

			LootPool.Builder extraBastionItems = LootPool.lootPool().name("extra").setRolls(UniformGenerator.between(2, 5)).add(LootItem.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			extraBastionItems.add(LootItem.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(10).apply(SetWillRange.withRange(UniformGenerator.between(15, 30))));
			extraBastionItems.add(LootItem.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(5).apply(SetLivingUpgrade.withRange(UniformGenerator.between(200, 400), LivingArmorRegistrar.UPGRADE_PHYSICAL_PROTECT.get().getKey())));
			extraBastionItems.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 15))));
			extraBastionItems.add(LootItem.lootTableItem(Items.GOLD_BLOCK).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));
			extraBastionItems.add(LootItem.lootTableItem(Items.COOKED_PORKCHOP).setWeight(7).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));

			LootPool.Builder basicNetherPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(BMTableLootEntry.builder(BuiltInLootTables.NETHER_BRIDGE));

			LootPool.Builder extraNetherItems = LootPool.lootPool().name("extra").setRolls(UniformGenerator.between(2, 5)).add(LootItem.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			extraNetherItems.add(LootItem.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(10).apply(SetWillRange.withRange(UniformGenerator.between(15, 30))));
			extraNetherItems.add(LootItem.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(5).apply(SetLivingUpgrade.withRange(UniformGenerator.between(50, 200), LivingArmorRegistrar.UPGRADE_FALL_PROTECT.get().getKey())));
			extraNetherItems.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 15))));
			extraNetherItems.add(LootItem.lootTableItem(Items.GOLD_BLOCK).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));
			extraNetherItems.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))));
			extraNetherItems.add(LootItem.lootTableItem(Items.BLAZE_ROD).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/bastion"), LootTable.lootTable().withPool(basicBastionPool).withPool(extraBastionItems));
			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/nether"), LootTable.lootTable().withPool(basicNetherPool).withPool(extraNetherItems));

			LootPool.Builder desertPyramidPool = LootPool.lootPool().setRolls(ConstantValue.exactly(2)).add(BMTableLootEntry.builder(BuiltInLootTables.DESERT_PYRAMID).setWeight(1));

			LootPool.Builder crypt_pool = LootPool.lootPool().setRolls(UniformGenerator.between(2, 4));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(25).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(7, 15))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.IRON_NUGGET).weight(20).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(30, 50))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(4).quality(2).acceptFunction(SetCount.builder(RandomValueRange.of(2, 5))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.LEATHER).weight(18).quality(-4).acceptFunction(SetCount.builder(RandomValueRange.of(10, 20))));
//			blacksmith_pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT).weight(8).quality(1).acceptFunction(SetCount.builder(RandomValueRange.of(4, 7))));

//			blacksmith_pool.
			crypt_pool.add(LootItem.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));

			addMultipleItemsWithSameParams(crypt_pool, weaponAnointments, 3, UniformGenerator.between(2, 5));
			crypt_pool.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(25).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 15))));
			crypt_pool.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(20).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(20, 40))));
			crypt_pool.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(4).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			crypt_pool.add(LootItem.lootTableItem(Items.LEATHER).setWeight(18).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))));
			crypt_pool.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(8).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));

			addMultipleItemsWithSameParams(crypt_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 6, ConstantValue.exactly(1), SetItemDamageFunction.setDamage(UniformGenerator.between(0.4F, 7F)));
			addMultipleItemsWithQualitySameParams(blacksmith_pool, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL,
					Items.IRON_HOE }, 4, -2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 20)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.9F, 1.0F)));
			addMultipleItemsWithSameParams(crypt_pool, new Item[] { Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE,
					Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 2, ConstantValue.exactly(1), SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.2F)));
			addMultipleItemsWithQualitySameParams(crypt_pool, new Item[] { Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE,
					Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_HOE }, 1, 2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20, 25)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.4F, 0.8F)));

			acceptor.accept(BloodMagic.rl("chests/simple_dungeon/crypt"), LootTable.lootTable().withPool(desertPyramidPool).withPool(crypt_pool));

			Item[] empoweredAnointments = new Item[] { BloodMagicItems.BOW_POWER_ANOINTMENT_2.get(),
					BloodMagicItems.FORTUNE_ANOINTMENT_2.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_2.get(),
					BloodMagicItems.HOLY_WATER_ANOINTMENT_2.get(), BloodMagicItems.LOOTING_ANOINTMENT_2.get(),
					BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_2.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_2.get(),
					BloodMagicItems.SILK_TOUCH_ANOINTMENT_L.get(), BloodMagicItems.SMELTING_ANOINTMENT_L.get() };

			Item[] empoweredWeaponAnointments = new Item[] { BloodMagicItems.BOW_POWER_ANOINTMENT_2.get(),
					BloodMagicItems.HOLY_WATER_ANOINTMENT_2.get(), BloodMagicItems.LOOTING_ANOINTMENT_2.get(),
					BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_2.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_2.get(),
					BloodMagicItems.SMELTING_ANOINTMENT_L.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT_2.get() };

			Item[] empoweredAnointments3 = new Item[] { BloodMagicItems.BOW_POWER_ANOINTMENT_3.get(),
					BloodMagicItems.FORTUNE_ANOINTMENT_3.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_3.get(),
					BloodMagicItems.HOLY_WATER_ANOINTMENT_3.get(), BloodMagicItems.LOOTING_ANOINTMENT_3.get(),
					BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_3.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_3.get(),
					BloodMagicItems.SILK_TOUCH_ANOINTMENT_XL.get(), BloodMagicItems.SMELTING_ANOINTMENT_XL.get() };

			Item[] empoweredWeaponAnointments3 = new Item[] { BloodMagicItems.BOW_POWER_ANOINTMENT_3.get(),
					BloodMagicItems.HOLY_WATER_ANOINTMENT_3.get(), BloodMagicItems.LOOTING_ANOINTMENT_3.get(),
					BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_3.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_3.get(),
					BloodMagicItems.SMELTING_ANOINTMENT_XL.get(), BloodMagicItems.BOW_VELOCITY_ANOINTMENT_3.get() };

			Item[] longAnointments3 = new Item[] { BloodMagicItems.BOW_POWER_ANOINTMENT_XL.get(),
					BloodMagicItems.FORTUNE_ANOINTMENT_XL.get(), BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_XL.get(),
					BloodMagicItems.HOLY_WATER_ANOINTMENT_XL.get(), BloodMagicItems.LOOTING_ANOINTMENT_XL.get(),
					BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_XL.get(), BloodMagicItems.QUICK_DRAW_ANOINTMENT_XL.get(),
					BloodMagicItems.SILK_TOUCH_ANOINTMENT_XL.get(), BloodMagicItems.SMELTING_ANOINTMENT_XL.get() };

			LootPool.Builder decent_loot = LootPool.lootPool().setRolls(UniformGenerator.between(3, 6));
			decent_loot.add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(15).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))));
			decent_loot.add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(20).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8))));
			decent_loot.add(LootItem.lootTableItem(Items.RAW_GOLD).setWeight(25).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))));
			decent_loot.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))));
			decent_loot.add(LootItem.lootTableItem(Items.EMERALD).setWeight(3).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))));
			decent_loot.add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(6).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))));
			decent_loot = addMultipleItemsWithSameParams(decent_loot, empoweredAnointments, 1, UniformGenerator.between(2, 4));
			addMultipleItemsWithQualitySameParams(decent_loot, new Item[] { Items.DIAMOND_BOOTS,
					Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET,
					Items.DIAMOND_LEGGINGS }, 3, 2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20, 39)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.7F, 1.0F)));
			addMultipleItemsWithQualitySameParams(decent_loot, new Item[] { Items.IRON_BOOTS, Items.IRON_CHESTPLATE,
					Items.IRON_HELMET,
					Items.IRON_LEGGINGS }, 4, 0, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20, 39)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.5F, 0.8F)));
			decent_loot.add(LootItem.lootTableItem(BloodMagicItems.NETHERITE_SCRAP_SAND.get()).setWeight(4).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))));
			decent_loot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_SAND.get()).setWeight(3).setQuality(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));

			decent_loot.add(LootItem.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(8).apply(SetWillRange.withRange(UniformGenerator.between(20, 50))));
			decent_loot = addMultipleItemsWithSameParams(decent_loot, new Item[] {
					BloodMagicItems.CORROSIVE_CRYSTAL.get(), BloodMagicItems.STEADFAST_CRYSTAL.get(),
					BloodMagicItems.VENGEFUL_CRYSTAL.get(), BloodMagicItems.DESTRUCTIVE_CRYSTAL.get(),
					BloodMagicItems.RAW_CRYSTAL.get() }, 2, UniformGenerator.between(2, 5));
			decent_loot.add(LootItem.lootTableItem(BloodMagicItems.SYNTHETIC_POINT.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 12))));
			// Maybe make it include BuiltInLootTables.END_CITY_TREASURE
			acceptor.accept(BloodMagic.rl("chests/standard_dungeon/decent_loot"), LootTable.lootTable().withPool(decent_loot));

			acceptor.accept(BloodMagic.rl("chests/standard_dungeon/great_loot"), LootTable.lootTable().withPool(decent_loot).withPool(decent_loot));

			LootPool.Builder enchanting_loot = LootPool.lootPool().setRolls(UniformGenerator.between(3, 6));
			enchanting_loot.add(LootItem.lootTableItem(Items.PAPER).setWeight(30).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 24))));
			enchanting_loot.add(LootItem.lootTableItem(Items.BOOK).setWeight(15).setQuality(-2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 10))));
			enchanting_loot.add(LootItem.lootTableItem(Items.BOOK).setWeight(12).setQuality(1).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30.0F)).allowTreasure()));
			enchanting_loot.add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(18).setQuality(-2).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 22))));
			enchanting_loot.add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(10).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))));
			addMultipleItemsWithQualitySameParams(enchanting_loot, new Item[] { Items.DIAMOND_BOOTS,
					Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET, Items.DIAMOND_LEGGINGS, Items.DIAMOND_PICKAXE,
					Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL,
					Items.DIAMOND_SWORD }, 1, 2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(25, 39)).allowTreasure());
			addMultipleItemsWithQualitySameParams(enchanting_loot, new Item[] { BloodMagicItems.SENTIENT_SWORD.get(),
					BloodMagicItems.SENTIENT_PICKAXE.get(), BloodMagicItems.SENTIENT_SCYTHE.get(),
					BloodMagicItems.SENTIENT_AXE.get(),
					BloodMagicItems.SENTIENT_SHOVEL.get() }, 1, 2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(25, 39)).allowTreasure());
			enchanting_loot.add(LootItem.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(4).apply(SetLivingUpgrade.withRange(UniformGenerator.between(200, 400), LivingArmorRegistrar.UPGRADE_EXPERIENCE.get().getKey())));
			enchanting_loot.add(LootItem.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));
			enchanting_loot.add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));
			addMultipleItemsWithSameParams(enchanting_loot, new Item[] { BloodMagicItems.CORROSIVE_CRYSTAL.get(),
					BloodMagicItems.STEADFAST_CRYSTAL.get(), BloodMagicItems.VENGEFUL_CRYSTAL.get(),
					BloodMagicItems.DESTRUCTIVE_CRYSTAL.get(),
					BloodMagicItems.RAW_CRYSTAL.get() }, 1, UniformGenerator.between(2, 5));

			acceptor.accept(BloodMagic.rl("chests/standard_dungeon/enchanting_loot"), LootTable.lootTable().withPool(enchanting_loot));

			LootPool.Builder poor_loot = LootPool.lootPool().setRolls(UniformGenerator.between(2, 4));
			poor_loot.add(LootItem.lootTableItem(BloodMagicItems.COPPER_FRAGMENT.get()).setWeight(25).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 15))));
			poor_loot.add(LootItem.lootTableItem(BloodMagicItems.IRON_FRAGMENT.get()).setWeight(20).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8))));
			poor_loot.add(LootItem.lootTableItem(BloodMagicItems.GOLD_FRAGMENT.get()).setWeight(15).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))));
			poor_loot.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(3).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))));
			poor_loot.add(LootItem.lootTableItem(Items.EMERALD).setWeight(1).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))));
			poor_loot.add(LootItem.lootTableItem(Items.WHEAT).setWeight(8).setQuality(-2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))));
			poor_loot.add(LootItem.lootTableItem(Items.FEATHER).setWeight(8).setQuality(-2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))));
			poor_loot.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(8).setQuality(-2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			poor_loot.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(8).setQuality(-2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))));
			poor_loot.add(LootItem.lootTableItem(Blocks.WARPED_STEM).setWeight(20).setQuality(-1).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 12))));
			poor_loot.add(LootItem.lootTableItem(Items.STICK).setWeight(15).setQuality(-1).apply(SetItemCountFunction.setCount(UniformGenerator.between(9, 15))));
			poor_loot.add(LootItem.lootTableItem(Items.SUGAR_CANE).setWeight(8).setQuality(-1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 10))));
			poor_loot.add(LootItem.lootTableItem(BloodMagicItems.SULFUR.get()).setWeight(6).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 7))));
			poor_loot.add(LootItem.lootTableItem(BloodMagicItems.DUNGEON_SIMPLE_KEY.get()).setWeight(3).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));
			poor_loot.add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(3).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))));
			poor_loot.add(LootItem.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(5).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))));

			acceptor.accept(BloodMagic.rl("chests/standard_dungeon/poor_loot"), LootTable.lootTable().withPool(poor_loot));

			LootPool.Builder potions_loot = LootPool.lootPool().setRolls(UniformGenerator.between(3, 6));
			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.IRON_SAND.get()).setWeight(20).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.GOLD_SAND.get()).setWeight(15).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.COAL_SAND.get()).setWeight(8).setQuality(-2).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 15))));
			potions_loot.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			potions_loot.add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			potions_loot.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(8).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			potions_loot.add(LootItem.lootTableItem(Items.NETHER_WART).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 14))));
			potions_loot.add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(4).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			potions_loot.add(LootItem.lootTableItem(Items.PHANTOM_MEMBRANE).setWeight(4).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			potions_loot.add(LootItem.lootTableItem(Items.RABBIT_FOOT).setWeight(3).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.PLANT_OIL.get()).setWeight(6).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(8).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));

			addMultipleItemsWithQualitySameParams(potions_loot, new Item[] { Items.BLAZE_POWDER, Items.MAGMA_CREAM,
					Items.SLIME_BALL, Items.FERMENTED_SPIDER_EYE, Items.SPIDER_EYE, Items.SUGAR, Items.HONEYCOMB,
					Items.GLISTERING_MELON_SLICE }, 5, -1, UniformGenerator.between(2, 5));

			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.BASIC_CUTTING_FLUID.get()).setWeight(6).setQuality(4));
			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.MUNDANE_POWER_CATALYST.get()).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));
			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get()).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));
			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.COMBINATIONAL_CATALYST.get()).setWeight(7).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));
			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.SIMPLE_CATALYST.get()).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))));
//			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.AVERAGE_POWER_CATALYST.get()).setWeight(2).setQuality(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
//			potions_loot.add(LootItem.lootTableItem(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get()).setWeight(1).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			potions_loot.add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(5).apply(SetPotionFunction.setPotion(Potions.POISON)));

			acceptor.accept(BloodMagic.rl("chests/standard_dungeon/decent_alchemy"), LootTable.lootTable().withPool(potions_loot));

			LootPool.Builder strong_potions_loot = LootPool.lootPool().setRolls(UniformGenerator.between(4, 6));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.IRON_SAND.get()).setWeight(10).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.GOLD_SAND.get()).setWeight(8).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.COAL_SAND.get()).setWeight(3).setQuality(-2).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 15))));
			strong_potions_loot.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(3).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			strong_potions_loot.add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).setWeight(3).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			strong_potions_loot.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(3).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));
			strong_potions_loot.add(LootItem.lootTableItem(Items.NETHER_WART).setWeight(4).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 14))));
			strong_potions_loot.add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(2).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(Items.PHANTOM_MEMBRANE).setWeight(3).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(Items.RABBIT_FOOT).setWeight(2).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(Items.GHAST_TEAR).setWeight(5).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(Items.GLOW_BERRIES).setWeight(5).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(5).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(8).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))));

			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.MUNDANE_POWER_CATALYST.get()).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get()).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.COMBINATIONAL_CATALYST.get()).setWeight(4).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.AVERAGE_POWER_CATALYST.get()).setWeight(2).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get()).setWeight(2).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.STRENGTHENED_CATALYST.get()).setWeight(6).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.AVERAGE_FILLING_AGENT.get()).setWeight(4).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			strong_potions_loot.add(LootItem.lootTableItem(BloodMagicItems.INTERMEDIATE_CUTTING_FLUID.get()).setWeight(9).setQuality(4));
			strong_potions_loot.add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(3).apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING)));
			strong_potions_loot.add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(5).apply(SetPotionFunction.setPotion(Potions.HEALING)));
			strong_potions_loot.add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(3).apply(SetPotionFunction.setPotion(Potions.STRONG_REGENERATION)));
			strong_potions_loot.add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(4).apply(SetPotionFunction.setPotion(Potions.LONG_REGENERATION)));

			addMultipleItemsWithQualitySameParams(strong_potions_loot, new Item[] {
					BloodMagicItems.CORROSIVE_CRYSTAL.get(), BloodMagicItems.RAW_CRYSTAL.get(),
					BloodMagicItems.STEADFAST_CRYSTAL.get(), BloodMagicItems.VENGEFUL_CRYSTAL.get(),
					BloodMagicItems.DESTRUCTIVE_CRYSTAL.get() }, 2, 3, UniformGenerator.between(2, 5));

			acceptor.accept(BloodMagic.rl("chests/standard_dungeon/strong_alchemy"), LootTable.lootTable().withPool(strong_potions_loot));

			LootPool.Builder smithy_loot = LootPool.lootPool().setRolls(UniformGenerator.between(4, 7));
			smithy_loot.add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(15).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))));
			smithy_loot.add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(20).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 12))));
			smithy_loot.add(LootItem.lootTableItem(Items.RAW_GOLD).setWeight(16).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 11))));
			smithy_loot.add(LootItem.lootTableItem(Items.COAL).setWeight(20).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 14))));
			smithy_loot.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(7).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))));
			smithy_loot.add(LootItem.lootTableItem(Items.EMERALD).setWeight(3).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))));
			smithy_loot.add(LootItem.lootTableItem(BloodMagicItems.NETHERITE_SCRAP_SAND.get()).setWeight(4).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			smithy_loot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_SAND.get()).setWeight(3).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))));
			smithy_loot.add(LootItem.lootTableItem(BloodMagicItems.CORRUPTED_DUST.get()).setWeight(6).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			smithy_loot.add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(8).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))));
			smithy_loot.add(LootItem.lootTableItem(BloodMagicItems.PRIMITIVE_EXPLOSIVE_CELL.get()).setWeight(10).setQuality(5));

			addMultipleItemsWithSameParams(smithy_loot, empoweredWeaponAnointments, 3, UniformGenerator.between(1, 4));
			smithy_loot.add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(12).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(20, 40))));
			smithy_loot.add(LootItem.lootTableItem(Items.LEATHER).setWeight(14).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 20))));
			smithy_loot.add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(6).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));

			addMultipleItemsWithQualitySameParams(smithy_loot, new Item[] { Items.IRON_PICKAXE, Items.IRON_AXE,
					Items.IRON_SWORD, Items.IRON_SHOVEL, Items.IRON_HOE, Items.IRON_HELMET, Items.IRON_CHESTPLATE,
					Items.IRON_LEGGINGS,
					Items.IRON_BOOTS }, 3, -2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 20)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.9F, 1.0F)));
			addMultipleItemsWithQualitySameParams(smithy_loot, new Item[] { Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE,
					Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE, Items.DIAMOND_HELMET,
					Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS,
					Items.DIAMOND_BOOTS }, 1, 2, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20, 25)).allowTreasure(), SetItemDamageFunction.setDamage(UniformGenerator.between(0.7F, 0.9F)));

			smithy_loot.add(LootItem.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(4).apply(SetLivingUpgrade.withRange(UniformGenerator.between(150, 225), LivingArmorRegistrar.UPGRADE_DIGGING.get().getKey())));
			smithy_loot.add(LootItem.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(4).apply(SetLivingUpgrade.withRange(UniformGenerator.between(250, 350), LivingArmorRegistrar.UPGRADE_MELEE_DAMAGE.get().getKey())));
			addMultipleItemsWithQualitySameParams(smithy_loot, new Item[] { BloodMagicItems.CORROSIVE_CRYSTAL.get(),
					BloodMagicItems.RAW_CRYSTAL.get(), BloodMagicItems.STEADFAST_CRYSTAL.get(),
					BloodMagicItems.VENGEFUL_CRYSTAL.get(),
					BloodMagicItems.DESTRUCTIVE_CRYSTAL.get() }, 2, 3, UniformGenerator.between(1, 3));

			acceptor.accept(BloodMagic.rl("chests/standard_dungeon/decent_smithy"), LootTable.lootTable().withPool(smithy_loot));

			LootPool.Builder mines_loot = LootPool.lootPool().setRolls(UniformGenerator.between(3, 6));
			mines_loot.add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(15).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))));
			mines_loot.add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(20).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8))));
			mines_loot.add(LootItem.lootTableItem(Items.RAW_GOLD).setWeight(25).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))));
			mines_loot.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))));
			mines_loot.add(LootItem.lootTableItem(Items.EMERALD).setWeight(3).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))));
			mines_loot.add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(6).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))));
			mines_loot = addMultipleItemsWithSameParams(decent_loot, empoweredAnointments, 1, UniformGenerator.between(2, 4));

			LootPool.Builder mines_key = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(BloodMagicItems.DUNGEON_MINE_ENTRANCE_KEY.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));

			acceptor.accept(BloodMagic.rl("chests/standard_dungeon/mines_key"), LootTable.lootTable().withPool(mines_loot).withPool(mines_key));

			// Fragment loot table that can be a base for stronger loot
			LootPool.Builder fragmentLoot = LootPool.lootPool().setRolls(UniformGenerator.between(1, 2));
			fragmentLoot.add(LootItem.lootTableItem(BloodMagicItems.COPPER_FRAGMENT.get()).setWeight(25).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(15, 22))));
			fragmentLoot.add(LootItem.lootTableItem(BloodMagicItems.IRON_FRAGMENT.get()).setWeight(20).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 15))));
			fragmentLoot.add(LootItem.lootTableItem(BloodMagicItems.GOLD_FRAGMENT.get()).setWeight(15).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 13))));
			fragmentLoot.add(LootItem.lootTableItem(BloodMagicItems.NETHERITE_SCRAP_FRAGMENT.get()).setWeight(3).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));

			LootPool.Builder miningOreLoot = LootPool.lootPool().setRolls(UniformGenerator.between(3, 5));
			miningOreLoot.add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(15).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(12, 26))));
			miningOreLoot.add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(20).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 15))));
			miningOreLoot.add(LootItem.lootTableItem(Items.RAW_GOLD).setWeight(16).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 14))));
			miningOreLoot.add(LootItem.lootTableItem(Items.COAL).setWeight(20).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 16))));
			miningOreLoot.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(7).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))));
			miningOreLoot.add(LootItem.lootTableItem(Items.EMERALD).setWeight(4).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 10))));
			miningOreLoot.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 15))));
			miningOreLoot.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(4).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 11))));
			miningOreLoot.add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 15))));
			miningOreLoot.add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(6).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8))));
			miningOreLoot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_SAND.get()).setWeight(12).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 7))));
			addMultipleItemsWithQualitySameParams(miningOreLoot, new Item[] { Items.IRON_PICKAXE,
					Items.IRON_SHOVEL }, 4, 1, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20, 40)).allowTreasure());
			addMultipleItemsWithQualitySameParams(miningOreLoot, new Item[] { Items.DIAMOND_PICKAXE,
					Items.DIAMOND_SHOVEL }, 3, 3, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(30, 40)).allowTreasure());
			addMultipleItemsWithQualitySameParams(miningOreLoot, new Item[] { Items.NETHERITE_PICKAXE,
					Items.NETHERITE_SHOVEL,
					Items.NETHERITE_AXE }, 1, 6, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(30, 40)).allowTreasure());

			addMultipleItemsWithQualitySameParams(miningOreLoot, new Item[] { BloodMagicItems.CORROSIVE_CRYSTAL.get(),
					BloodMagicItems.RAW_CRYSTAL.get(), BloodMagicItems.STEADFAST_CRYSTAL.get(),
					BloodMagicItems.VENGEFUL_CRYSTAL.get(),
					BloodMagicItems.DESTRUCTIVE_CRYSTAL.get() }, 2, 3, UniformGenerator.between(2, 5));
			miningOreLoot.add(LootItem.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(4).apply(SetLivingUpgrade.withRange(UniformGenerator.between(200, 300), LivingArmorRegistrar.UPGRADE_DIGGING.get().getKey())));
//			miningOreLoot.add(LootItem.lootTableItem(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get()).setWeight(4).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))));
//			miningOreLoot.add(LootItem.lootTableItem(BloodMagicItems.FORTUNE_ANOINTMENT_2.get()).setWeight(4).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			addMultipleItemsWithQualitySameParams(miningOreLoot, empoweredWeaponAnointments, 3, 2, UniformGenerator.between(2, 5));
			miningOreLoot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_PARTS.get()).setWeight(1).setQuality(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			miningOreLoot.add(LootItem.lootTableItem(BloodMagicItems.BLEEDING_EDGE_MUSIC.get()).setWeight(3).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));

			acceptor.accept(BloodMagic.rl("chests/mines/ore_loot"), LootTable.lootTable().withPool(fragmentLoot).withPool(miningOreLoot));

			LootPool.Builder minesSmithyLoot = LootPool.lootPool().setRolls(UniformGenerator.between(3, 5));
			minesSmithyLoot.add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(10).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(12, 26))));
			minesSmithyLoot.add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(16).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 15))));
			minesSmithyLoot.add(LootItem.lootTableItem(Items.RAW_GOLD).setWeight(14).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 14))));
			minesSmithyLoot.add(LootItem.lootTableItem(Items.COAL).setWeight(10).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 16))));
			minesSmithyLoot.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(6).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))));
			minesSmithyLoot.add(LootItem.lootTableItem(Items.ANCIENT_DEBRIS).setWeight(5).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(4).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8))));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_SAND.get()).setWeight(12).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 7))));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicBlocks.DUNGEON_ORE.get()).setWeight(8).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));

			addMultipleItemsWithQualitySameParams(minesSmithyLoot, new Item[] { Items.DIAMOND_PICKAXE,
					Items.DIAMOND_SHOVEL, Items.DIAMOND_AXE, Items.DIAMOND_HOE,
					Items.DIAMOND_SWORD }, 3, 3, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(30, 40)).allowTreasure());
			addMultipleItemsWithQualitySameParams(minesSmithyLoot, new Item[] { Items.NETHERITE_PICKAXE,
					Items.NETHERITE_SHOVEL, Items.NETHERITE_AXE, Items.NETHERITE_SWORD,
					Items.NETHERITE_HOE }, 1, 6, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(30, 40)).allowTreasure());
			addMultipleItemsWithQualitySameParams(minesSmithyLoot, new Item[] { Items.NETHERITE_HELMET,
					Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS,
					Items.NETHERITE_BOOTS }, 1, 6, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(25, 35)).allowTreasure());

			addMultipleItemsWithQualitySameParams(minesSmithyLoot, new Item[] { BloodMagicItems.CORROSIVE_CRYSTAL.get(),
					BloodMagicItems.RAW_CRYSTAL.get(), BloodMagicItems.STEADFAST_CRYSTAL.get(),
					BloodMagicItems.VENGEFUL_CRYSTAL.get(),
					BloodMagicItems.DESTRUCTIVE_CRYSTAL.get() }, 2, 3, UniformGenerator.between(2, 5));

			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(4).apply(SetLivingUpgrade.withRange(UniformGenerator.between(200, 300), LivingArmorRegistrar.UPGRADE_ARROW_PROTECT.get().getKey())));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.LIVING_TOME.get()).setWeight(4).apply(SetLivingUpgrade.withRange(UniformGenerator.between(200, 300), LivingArmorRegistrar.UPGRADE_PHYSICAL_PROTECT.get().getKey())));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(10).apply(SetWillRange.withRange(UniformGenerator.between(40, 75))));

			addMultipleItemsWithQualitySameParams(minesSmithyLoot, empoweredWeaponAnointments3, 2, 3, UniformGenerator.between(1, 3));
			minesSmithyLoot.add(LootItem.lootTableItem(Items.BOOK).setWeight(10).setQuality(3).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(25, 35)).allowTreasure()));

			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_PARTS.get()).setWeight(2).setQuality(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.PRIMITIVE_EXPLOSIVE_CELL.get()).setWeight(6).setQuality(3).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.PRIMITIVE_CRYSTALLINE_RESONATOR.get()).setWeight(6).setQuality(3).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.INTERMEDIATE_CUTTING_FLUID.get()).setWeight(6).setQuality(3).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_EXPLOSIVE_CELL.get()).setWeight(4).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.3F, 0.7F))));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_RESONATOR.get()).setWeight(4).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.3F, 0.7F))));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.ADVANCED_CUTTING_FLUID.get()).setWeight(4).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.3F, 0.7F))));
			minesSmithyLoot.add(LootItem.lootTableItem(BloodMagicItems.BLEEDING_EDGE_MUSIC.get()).setWeight(3).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));

			acceptor.accept(BloodMagic.rl("chests/mines/smithy_loot"), LootTable.lootTable().withPool(fragmentLoot).withPool(minesSmithyLoot));

			LootPool.Builder minesRawLoot = LootPool.lootPool().setRolls(UniformGenerator.between(1, 2));
			minesRawLoot.add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(20).setQuality(-4).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))));
			minesRawLoot.add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(16).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8))));
			minesRawLoot.add(LootItem.lootTableItem(Items.RAW_GOLD).setWeight(14).setQuality(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))));
			minesRawLoot.add(LootItem.lootTableItem(Items.NETHERITE_SCRAP).setWeight(3).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			minesRawLoot.add(LootItem.lootTableItem(BloodMagicItems.CORRUPTED_DUST.get()).setWeight(8).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));

			LootPool.Builder minesDecentLoot = LootPool.lootPool().setRolls(UniformGenerator.between(3, 5));
			minesDecentLoot.add(LootItem.lootTableItem(Items.COAL).setWeight(10).setQuality(-2).apply(SetItemCountFunction.setCount(UniformGenerator.between(12, 18))));
			minesDecentLoot.add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(12).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 17))));
			minesDecentLoot.add(LootItem.lootTableItem(Items.QUARTZ).setWeight(15).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(10, 17))));
			minesDecentLoot.add(LootItem.lootTableItem(Items.REDSTONE).setWeight(8).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(14, 20))));
			minesDecentLoot.add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(8).setQuality(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(14, 20))));
			minesDecentLoot.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).setQuality(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))));
			minesDecentLoot.add(LootItem.lootTableItem(Items.EMERALD).setWeight(3).setQuality(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))));

			minesDecentLoot.add(LootItem.lootTableItem(BloodMagicItems.MONSTER_SOUL_RAW.get()).setWeight(10).apply(SetWillRange.withRange(UniformGenerator.between(40, 75))));
			minesDecentLoot.add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(6).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))));
			addMultipleItemsWithSameParams(minesDecentLoot, empoweredAnointments3, 2, UniformGenerator.between(2, 5));
			addMultipleItemsWithSameParams(minesDecentLoot, longAnointments3, 2, UniformGenerator.between(2, 5));

			addMultipleItemsWithQualitySameParams(minesDecentLoot, new Item[] { BloodMagicItems.CORROSIVE_CRYSTAL.get(),
					BloodMagicItems.RAW_CRYSTAL.get(), BloodMagicItems.STEADFAST_CRYSTAL.get(),
					BloodMagicItems.VENGEFUL_CRYSTAL.get(),
					BloodMagicItems.DESTRUCTIVE_CRYSTAL.get() }, 2, 3, UniformGenerator.between(2, 5));

			addMultipleItemsWithQualitySameParams(minesDecentLoot, new Item[] { Items.NETHERITE_PICKAXE,
					Items.NETHERITE_SHOVEL, Items.NETHERITE_AXE, Items.NETHERITE_SWORD,
					Items.NETHERITE_HOE }, 1, 6, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(30, 40)).allowTreasure());
			addMultipleItemsWithQualitySameParams(minesDecentLoot, new Item[] { Items.NETHERITE_HELMET,
					Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS,
					Items.NETHERITE_BOOTS }, 1, 6, ConstantValue.exactly(1), EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(30, 35)).allowTreasure());

			minesDecentLoot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_PARTS.get()).setWeight(6).setQuality(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));
			minesDecentLoot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_EXPLOSIVE_CELL.get()).setWeight(4).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			minesDecentLoot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_RESONATOR.get()).setWeight(4).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			minesDecentLoot.add(LootItem.lootTableItem(BloodMagicItems.ADVANCED_CUTTING_FLUID.get()).setWeight(4).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			minesDecentLoot.add(LootItem.lootTableItem(BloodMagicItems.BLEEDING_EDGE_MUSIC.get()).setWeight(3).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));

			acceptor.accept(BloodMagic.rl("chests/mines/decent_loot"), LootTable.lootTable().withPool(minesRawLoot).withPool(minesDecentLoot));

			LootPool.Builder minesFoodLoot = LootPool.lootPool().setRolls(UniformGenerator.between(3, 5));
			addMultipleItemsWithSameParams(minesFoodLoot, new Item[] { Items.BEEF, Items.PORKCHOP, Items.CHICKEN,
					Items.EGG, Items.MUTTON, Items.RABBIT }, 10, UniformGenerator.between(5, 10));
			addMultipleItemsWithSameParams(minesFoodLoot, new Item[] { Items.FEATHER, Items.LEATHER, Items.WHITE_WOOL,
					Items.BLACK_WOOL, Items.RABBIT_HIDE }, 5, UniformGenerator.between(4, 15));
			addMultipleItemsWithSameParams(minesFoodLoot, new Item[] { Items.MELON_SEEDS, Items.PUMPKIN_SEEDS,
					Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS }, 5, UniformGenerator.between(5, 10));
			minesFoodLoot.add(LootItem.lootTableItem(Items.LEAD).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))));
			minesFoodLoot.add(LootItem.lootTableItem(Items.RABBIT_FOOT).setWeight(2).setQuality(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));
			minesFoodLoot.add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(3).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			minesFoodLoot.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 11))));
			minesFoodLoot.add(LootItem.lootTableItem(Items.BONE).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))));
			minesFoodLoot.add(LootItem.lootTableItem(BloodMagicItems.WEAK_TAU_ITEM.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 11))));
			minesFoodLoot.add(LootItem.lootTableItem(BloodMagicItems.STRONG_TAU_ITEM.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 11))));
			minesFoodLoot.add(LootItem.lootTableItem(BloodMagicItems.PLANT_OIL.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 11))));
			minesFoodLoot.add(LootItem.lootTableItem(BloodMagicItems.ADVANCED_CUTTING_FLUID.get()).setWeight(3).setQuality(5).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));
			minesFoodLoot.add(LootItem.lootTableItem(BloodMagicItems.HELLFORGED_PARTS.get()).setWeight(3).setQuality(10).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));

			acceptor.accept(BloodMagic.rl("chests/mines/food_loot"), LootTable.lootTable().withPool(fragmentLoot).withPool(minesFoodLoot));

			LootPool.Builder mineKeyLoot = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(BloodMagicItems.DUNGEON_MINE_KEY.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))));

			acceptor.accept(BloodMagic.rl("chests/mines/mine_key_loot"), LootTable.lootTable().withPool(minesRawLoot).withPool(minesDecentLoot).withPool(mineKeyLoot));
		}

		private LootPool.Builder addMultipleItemsWithSameParams(LootPool.Builder pool, Item[] items, int basicWeight, NumberProvider basicRange, Builder... functions)
		{
			return addMultipleItemsWithQualitySameParams(pool, items, basicWeight, 0, basicRange, functions);
		}

		private LootPool.Builder addMultipleItemsWithQualitySameParams(LootPool.Builder pool, Item[] items, int basicWeight, int quality, NumberProvider basicRange, Builder... functions)
		{
			if (basicWeight > 0)
			{
				for (Item item : items)
				{
					LootPoolSingletonContainer.Builder<?> entryBuilder = LootItem.lootTableItem(item).setWeight(basicWeight).setQuality(quality).apply(SetItemCountFunction.setCount(basicRange));
					for (Builder function : functions)
					{
						entryBuilder = entryBuilder.apply(function);
					}

					pool = pool.add(entryBuilder);
				}
			}

			return pool;
		}
	}

	private static class BMBlocks extends VanillaBlockLoot
	{
		@Override
		protected void generate()
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
			add(BloodMagicBlocks.DUNGEON_BRICK_SLAB.get(), this::createSlabItemTable);
			add(BloodMagicBlocks.DUNGEON_TILE_SLAB.get(), this::createSlabItemTable);

			registerNoDropLootTable(BloodMagicBlocks.DUNGEON_CONTROLLER.get());
			registerNoDropLootTable(BloodMagicBlocks.DUNGEON_SEAL.get());
			registerNoDropLootTable(BloodMagicBlocks.SPECIAL_DUNGEON_SEAL.get());

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

			registerNoDropLootTable(BloodMagicBlocks.AUG_SHAPED_CHARGE.get());
			registerNoDropLootTable(BloodMagicBlocks.DEFORESTER_CHARGE_2.get());
			registerNoDropLootTable(BloodMagicBlocks.VEINMINE_CHARGE_2.get());
			registerNoDropLootTable(BloodMagicBlocks.FUNGAL_CHARGE_2.get());
			registerNoDropLootTable(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get());

			registerNoDropLootTable(BloodMagicBlocks.INVERSION_PILLAR.get());
			registerNoDropLootTable(BloodMagicBlocks.INVERSION_PILLAR_CAP.get());

			registerNoDropLootTable(BloodMagicFluids.LIFE_ESSENCE_BLOCK.get());
			registerNoDropLootTable(BloodMagicFluids.DOUBT_BLOCK.get());

			add(BloodMagicBlocks.DUNGEON_ORE.get(), (block) -> {
				return createOreDrop(block, BloodMagicItems.DEMONITE_RAW.get());
			});
		}

		private void registerNoDropLootTable(Block block)
		{
			LootPool.Builder builder = LootPool.lootPool().name(ForgeRegistries.BLOCKS.getKey(block).toString());
			this.add(block, LootTable.lootTable().withPool(builder));
		}

		private void registerCropDropLootTable(Block block, Item item)
		{
			LootTable.Builder builder = LootTable.lootTable();

			LootItemCondition.Builder ageLootCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7));
			builder = builder.withPool(LootPool.lootPool().add(LootItem.lootTableItem(item))).withPool(LootPool.lootPool().when(ageLootCondition).add(LootItem.lootTableItem(item).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))));
//		      this.registerLootTable(Blocks.POTATOES, withExplosionDecay(Blocks.POTATOES, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(Items.POTATO))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POTATO).acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3)))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).acceptCondition(RandomChance.builder(0.02F))))));

			this.add(block, builder);
		}

		private void registerCropDropLootTableWithImmatureSeed(Block block, Item seed, Item fruit)
		{
			LootTable.Builder builder = LootTable.lootTable();

			LootItemCondition.Builder ageLootCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7));
			builder = builder.withPool(LootPool.lootPool().when(ageLootCondition.invert()).add(LootItem.lootTableItem(seed))).withPool(LootPool.lootPool().when(ageLootCondition).add(LootItem.lootTableItem(fruit))).withPool(LootPool.lootPool().when(ageLootCondition).add(LootItem.lootTableItem(fruit).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))));
//		      this.registerLootTable(Blocks.POTATOES, withExplosionDecay(Blocks.POTATOES, LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(Items.POTATO))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POTATO).acceptFunction(ApplyBonus.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3)))).addLootPool(LootPool.builder().acceptCondition(ilootcondition$ibuilder3).addEntry(ItemLootEntry.builder(Items.POISONOUS_POTATO).acceptCondition(RandomChance.builder(0.02F))))));

			this.add(block, builder);
		}

		private void registerDropCrystalsLootTable(Block block, Item item)
		{
			LootTable.Builder builder = LootTable.lootTable();

			for (int i = 0; i < 7; i++)
			{
				LootItemCondition.Builder harvestAge = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockDemonCrystal.AGE, i));
				builder = builder.withPool(LootPool.lootPool().add(LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly(i + 1))).when(harvestAge)));
			}

			this.add(block, builder);
		}

//		protected static <T extends Comparable<T> & StringRepresentable> LootTable.Builder droppingWhen(Block block, Property<T> property, T value)
//		{
//			return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, value))))));
//		}

		@Override
		protected Iterable<Block> getKnownBlocks()
		{
			return ForgeRegistries.BLOCKS.getValues().stream().filter(b -> ForgeRegistries.BLOCKS.getKey(b).getNamespace().equals(BloodMagic.MODID)).collect(Collectors.toList());
		}
	}

//	@Override
//	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker)
//	{
//		map.forEach((name, table) -> validate(validationtracker, name, table));
//	}
}