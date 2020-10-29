package wayoftime.bloodmagic.common.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.will.EnumDemonWillType;

public class GeneratorItemModels extends ItemModelProvider
{
	public GeneratorItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, BloodMagic.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
//		registerBlockModel(BloodMagicBlocks.TARTARICFORGE.get());
//		registerBlockModel(BloodMagicBlocks.SPEED_RUNE.get());

		for (RegistryObject<Item> item : BloodMagicItems.BASICITEMS.getEntries())
		{
			registerBasicItem(item.get());
		}

		for (RegistryObject<Block> block : BloodMagicBlocks.BASICBLOCKS.getEntries())
		{
			registerBlockModel(block.get());
		}

		registerBlockModel(BloodMagicBlocks.BLANK_RITUAL_STONE.get());
		registerBlockModel(BloodMagicBlocks.AIR_RITUAL_STONE.get());
		registerBlockModel(BloodMagicBlocks.WATER_RITUAL_STONE.get());
		registerBlockModel(BloodMagicBlocks.FIRE_RITUAL_STONE.get());
		registerBlockModel(BloodMagicBlocks.EARTH_RITUAL_STONE.get());
		registerBlockModel(BloodMagicBlocks.DUSK_RITUAL_STONE.get());
		registerBlockModel(BloodMagicBlocks.DAWN_RITUAL_STONE.get());
		registerBlockModel(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get());

		registerToggleableItem(BloodMagicItems.GREEN_GROVE_SIGIL.get());
		registerToggleableItem(BloodMagicItems.FAST_MINER_SIGIL.get());
		registerToggleableItem(BloodMagicItems.MAGNETISM_SIGIL.get());
		registerToggleableItem(BloodMagicItems.ICE_SIGIL.get());
		registerDemonWillVariantItem(BloodMagicItems.PETTY_GEM.get());
		registerDemonWillVariantItem(BloodMagicItems.LESSER_GEM.get());
		registerDemonWillVariantItem(BloodMagicItems.COMMON_GEM.get());
		registerDemonSword(BloodMagicItems.SENTIENT_SWORD.get());
	}

	private void registerBlockModel(Block block)
	{
		String path = block.getRegistryName().getPath();
		getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
	}

	private void registerBasicItem(Item item)
	{
		String path = item.getRegistryName().getPath();
		singleTexture(path, mcLoc("item/handheld"), "layer0", modLoc("item/" + path));
	}

	private void registerToggleableItem(Item item)
	{
		String path = item.getRegistryName().getPath();
		ModelFile activatedFile = singleTexture("item/variants/" + path + "_activated", mcLoc("item/handheld"), "layer0", modLoc("item/" + path + "_activated"));
		ModelFile deactivatedFile = singleTexture("item/variants/" + path + "_deactivated", mcLoc("item/handheld"), "layer0", modLoc("item/" + path + "_deactivated"));
		getBuilder(path).override().predicate(BloodMagic.rl("active"), 0).model(deactivatedFile).end().override().predicate(BloodMagic.rl("active"), 1).model(activatedFile).end();
	}

	private void registerDemonWillVariantItem(Item item)
	{
		String path = item.getRegistryName().getPath();
		ItemModelBuilder builder = getBuilder(path);

		for (EnumDemonWillType type : EnumDemonWillType.values())
		{
			String name = "";
			if (type.ordinal() != 0)
			{
				name = "_" + type.name().toLowerCase();
			}
			ModelFile willFile = singleTexture("item/variants/" + path + name, mcLoc("item/handheld"), "layer0", modLoc("item/" + path + name));
			builder = builder.override().predicate(BloodMagic.rl("type"), type.ordinal()).model(willFile).end();
		}
	}

	private void registerDemonSword(Item item)
	{
		String path = item.getRegistryName().getPath();
		ItemModelBuilder builder = getBuilder(path);

		for (int i = 0; i <= 1; i++)
		{
			for (EnumDemonWillType type : EnumDemonWillType.values())
			{
				String name = i == 0 ? "_deactivated" : "_activated";
				if (type.ordinal() != 0)
				{
					name = "_" + type.name().toLowerCase() + name;
				}
				ModelFile willFile = singleTexture("item/variants/" + path + name, mcLoc("item/handheld"), "layer0", modLoc("item/" + path + name));
				builder = builder.override().predicate(BloodMagic.rl("type"), type.ordinal()).predicate(BloodMagic.rl("active"), i).model(willFile).end();
			}
		}
	}
}
