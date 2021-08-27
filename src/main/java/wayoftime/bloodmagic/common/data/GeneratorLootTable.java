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
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.Property;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BlockDemonCrystal;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

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
			acceptor.accept(BloodMagic.rl("test"), testLootTableGeneration());
		}

		private LootTable.Builder testLootTableGeneration()
		{
			LootTable.Builder table = LootTable.builder();
			LootPool.Builder pool = LootPool.builder().name("test").addEntry(ItemLootEntry.builder(Items.BOOK).weight(10).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30)).func_216059_e()));

			table.addLootPool(pool);
//			table.build();

			return table;
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

			registerDropSelfLootTable(BloodMagicBlocks.MIMIC.get());
			registerDropSelfLootTable(BloodMagicBlocks.ETHEREAL_MIMIC.get());

			registerCropDropLootTable(BloodMagicBlocks.GROWING_DOUBT.get(), BloodMagicItems.WEAK_BLOOD_SHARD.get());

			registerNoDropLootTable(BloodMagicBlocks.SHAPED_CHARGE.get());
			registerNoDropLootTable(BloodMagicBlocks.DEFORESTER_CHARGE.get());
			registerNoDropLootTable(BloodMagicBlocks.VEINMINE_CHARGE.get());
			registerNoDropLootTable(BloodMagicBlocks.FUNGAL_CHARGE.get());
		}

		private void registerNoDropLootTable(Block block)
		{
			LootPool.Builder builder = LootPool.builder().name(block.getRegistryName().toString());
			this.registerLootTable(block, LootTable.builder().addLootPool(builder));
		}

		private void registerCropDropLootTable(Block block, Item item)
		{
			LootTable.Builder builder = LootTable.builder();

			for (int i = 0; i < 7; i++)
			{
				ILootCondition.IBuilder harvestAge = BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(CropsBlock.AGE, i));
				builder = builder.addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(item).acceptFunction(SetCount.builder(ConstantRange.of(1))).acceptCondition(harvestAge)));
			}

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