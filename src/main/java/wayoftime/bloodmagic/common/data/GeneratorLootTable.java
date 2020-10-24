package wayoftime.bloodmagic.common.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

public class GeneratorLootTable extends LootTableProvider
{
	public GeneratorLootTable(DataGenerator dataGeneratorIn)
	{
		super(dataGeneratorIn);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
	{
		return ImmutableList.of(Pair.of(Blocks::new, LootParameterSets.BLOCK));
	}

	private static class Blocks extends BlockLootTables
	{
		@Override
		protected void addTables()
		{
			for (RegistryObject<Block> block : BloodMagicBlocks.BASICBLOCKS.getEntries())
			{
				this.registerDropSelfLootTable(block.get());
			}

			this.registerDropSelfLootTable(BloodMagicBlocks.BLOOD_ALTAR.get());
			registerNoDropLootTable(BloodMagicBlocks.ALCHEMY_ARRAY.get());
			registerNoDropLootTable(BloodMagicBlocks.BLOOD_LIGHT.get());
			this.registerDropSelfLootTable(BloodMagicBlocks.SOUL_FORGE.get());

//			LootPool.Builder builder = LootPool.builder().name(ModBlocks.GOO_BLOCK.get().getRegistryName().toString()).rolls(ConstantRange.of(1)).acceptCondition(SurvivesExplosion.builder()).addEntry(ItemLootEntry.builder(ModItems.GOO_RESIDUE.get()));
//			this.registerLootTable(ModBlocks.GOO_BLOCK.get(), LootTable.builder().addLootPool(builder));
//
//			LootPool.Builder builder2 = LootPool.builder().name(ModBlocks.GOO_BLOCK_TERRAIN.get().getRegistryName().toString()).rolls(ConstantRange.of(1)).acceptCondition(SurvivesExplosion.builder()).addEntry(ItemLootEntry.builder(ModItems.GOO_RESIDUE.get()));
//			this.registerLootTable(ModBlocks.GOO_BLOCK_TERRAIN.get(), LootTable.builder().addLootPool(builder2));
//
//			this.registerDropSelfLootTable(ModBlocks.GOO_BLOCK_POISON.get());
//			this.registerDropSelfLootTable(ModBlocks.GNT_BLOCK_T1.get());
//			this.registerDropSelfLootTable(ModBlocks.GNT_BLOCK_T2.get());
//			this.registerDropSelfLootTable(ModBlocks.GNT_BLOCK_T3.get());
//			this.registerDropSelfLootTable(ModBlocks.GNT_BLOCK_T4.get());
//			this.registerDropSelfLootTable(ModBlocks.TURRET_BLOCK.get());
//			this.registerDropSelfLootTable(ModBlocks.ZAPPER_TURRET_BLOCK.get());
//			this.registerDropSelfLootTable(ModBlocks.ANTI_GOO_BEACON.get());
//			this.registerDropSelfLootTable(ModBlocks.ANTI_GOO_FIELD_GEN.get());
//			this.registerDropSelfLootTable(ModBlocks.GOOLIMINATIONFIELDGEN.get());
//			this.registerDropSelfLootTable(ModBlocks.GOO_DETECTOR.get());
//			this.registerDropping(ModBlocks.GOO_RENDER.get(), ItemStack.EMPTY.getItem());
//			// this.registerDropping(ModBlocks.GOO_RENDER_BURST.get(),
//			// ItemStack.EMPTY.getItem());
//			this.registerDropping(ModBlocks.GOO_RENDER_TERRAIN.get(), ItemStack.EMPTY.getItem());

		}

		private void registerNoDropLootTable(Block block)
		{
			LootPool.Builder builder = LootPool.builder().name(block.getRegistryName().toString());
			this.registerLootTable(block, LootTable.builder().addLootPool(builder));
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