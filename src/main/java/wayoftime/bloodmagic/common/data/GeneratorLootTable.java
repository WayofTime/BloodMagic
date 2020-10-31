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