package wayoftime.bloodmagic.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.Locale;

public class GeneratorItemModels extends ItemModelProvider {
    public GeneratorItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BloodMagic.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
//		registerBlockModel(BloodMagicBlocks.TARTARICFORGE.get());
//		registerBlockModel(BloodMagicBlocks.SPEED_RUNE.get());

        for (RegistryObject<Item> item : BloodMagicItems.BASICITEMS.getEntries()) {
            registerBasicItem(item.get());
        }

        for (RegistryObject<Block> block : BloodMagicBlocks.BASICBLOCKS.getEntries()) {
            registerBlockModel(block.get());
        }

        for (RegistryObject<Block> block : BloodMagicBlocks.DUNGEONBLOCKS.getEntries()) {
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
        registerBlockModel(BloodMagicBlocks.NETHER_SOIL.get());

        registerCustomBlockPath(BloodMagicBlocks.RAW_CRYSTAL_BLOCK.get(), "crystal/defaultcrystal1");
        registerCustomBlockPath(BloodMagicBlocks.CORROSIVE_CRYSTAL_BLOCK.get(), "crystal/corrosivecrystal1");
        registerCustomBlockPath(BloodMagicBlocks.DESTRUCTIVE_CRYSTAL_BLOCK.get(), "crystal/destructivecrystal1");
        registerCustomBlockPath(BloodMagicBlocks.VENGEFUL_CRYSTAL_BLOCK.get(), "crystal/vengefulcrystal1");
        registerCustomBlockPath(BloodMagicBlocks.STEADFAST_CRYSTAL_BLOCK.get(), "crystal/steadfastcrystal1");

        registerCustomBlockPath(BloodMagicBlocks.SPECTRAL.get(), "spectralblock");

        registerCustomBlockPath(BloodMagicBlocks.ROUTING_NODE_BLOCK.get(), "routingnodecombined");
        registerCustomBlockPathWithTextures(BloodMagicBlocks.INPUT_ROUTING_NODE_BLOCK.get(), "routingnodecombined", "core", "bloodmagic:models/modelinputroutingnode", "base", "bloodmagic:models/modelinputroutingnode");
        registerCustomBlockPathWithTextures(BloodMagicBlocks.OUTPUT_ROUTING_NODE_BLOCK.get(), "routingnodecombined", "core", "bloodmagic:models/modeloutputroutingnode", "base", "bloodmagic:models/modeloutputroutingnode");
        registerCustomBlockPath(BloodMagicBlocks.MASTER_ROUTING_NODE_BLOCK.get(), "masterroutingnodecombined");

        registerCustomBlockPath(BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get(), "dungeon_brick1");
        registerBlockModel(BloodMagicBlocks.DUNGEON_STONE.get());
        registerBlockModel(BloodMagicBlocks.DUNGEON_BRICK_STAIRS.get());
        registerBlockModel(BloodMagicBlocks.DUNGEON_POLISHED_STAIRS.get());
        registerBlockModel(BloodMagicBlocks.DUNGEON_PILLAR_CENTER.get());
        registerBlockModel(BloodMagicBlocks.DUNGEON_PILLAR_SPECIAL.get());
        registerBlockModel(BloodMagicBlocks.DUNGEON_PILLAR_CAP.get());
        registerCustomBlockPath(BloodMagicBlocks.DUNGEON_BRICK_WALL.get(), "dungeon_brick_wall_inventory");
        registerCustomBlockPath(BloodMagicBlocks.DUNGEON_POLISHED_WALL.get(), "dungeon_polished_wall_inventory");
        registerBlockModel(BloodMagicBlocks.DUNGEON_BRICK_GATE.get());
        registerBlockModel(BloodMagicBlocks.DUNGEON_POLISHED_GATE.get());
        registerBlockModel(BloodMagicBlocks.DUNGEON_BRICK_SLAB.get());
        registerBlockModel(BloodMagicBlocks.DUNGEON_TILE_SLAB.get());

//		registerBlockModel(BloodMagicBlocks.INVERSION_PILLAR_CAP.get());

        registerBlockModel(BloodMagicBlocks.DUNGEON_CONTROLLER.get());
        registerBlockModel(BloodMagicBlocks.DUNGEON_SEAL.get());
        registerCustomBlockPath(BloodMagicBlocks.SPECIAL_DUNGEON_SEAL.get(), "special_dungeon_seal_standard");

        registerBlockModel(BloodMagicBlocks.DUNGEON_ORE.get());

        registerBlockModel(BloodMagicBlocks.TELEPOSER.get());

        registerToggleableItem(BloodMagicItems.GREEN_GROVE_SIGIL.get());
        registerToggleableItem(BloodMagicItems.FAST_MINER_SIGIL.get());
        registerToggleableItem(BloodMagicItems.MAGNETISM_SIGIL.get());
        registerToggleableItem(BloodMagicItems.ICE_SIGIL.get());
        registerToggleableItem(BloodMagicItems.SUPPRESSION_SIGIL.get());
        registerDemonWillVariantItem(BloodMagicItems.PETTY_GEM.get());
        registerDemonWillVariantItem(BloodMagicItems.LESSER_GEM.get());
        registerDemonWillVariantItem(BloodMagicItems.COMMON_GEM.get());
        registerDemonWillVariantItem(BloodMagicItems.GREATER_GEM.get());
        registerDemonSword(BloodMagicItems.SENTIENT_SWORD.get());
        registerDemonTool(BloodMagicItems.SENTIENT_AXE.get());
        registerDemonTool(BloodMagicItems.SENTIENT_PICKAXE.get());
        registerDemonTool(BloodMagicItems.SENTIENT_SHOVEL.get());
        registerDemonTool(BloodMagicItems.SENTIENT_SCYTHE.get());
        registerSacrificialKnife(BloodMagicItems.SACRIFICIAL_DAGGER.get());

        registerCustomFullTexture(BloodMagicBlocks.MIMIC.get(), "solidopaquemimic");
        registerCustomFullTexture(BloodMagicBlocks.ETHEREAL_MIMIC.get(), "etherealopaquemimic");
//		this.crop(BloodMagicBlocks.GROWING_DOUBT.get().getRegistryName().getPath(), modLoc("block/creeping_doubt_8"));
        registerBasicItem(BloodMagicItems.GROWING_DOUBT_ITEM.get(), modLoc("item/doubt_seed"));
        registerBasicItem(BloodMagicItems.WEAK_TAU_ITEM.get(), modLoc("item/weak_tau"));
        registerBasicItem(BloodMagicItems.STRONG_TAU_ITEM.get(), modLoc("item/strong_tau"));

        createTexturedModelWithCustomPath("sigilofholding_base", modLoc("item/sigilofholding"));

        registerBlockModel(BloodMagicBlocks.SHAPED_CHARGE.get());
        registerBlockModel(BloodMagicBlocks.DEFORESTER_CHARGE.get());
        registerBlockModel(BloodMagicBlocks.VEINMINE_CHARGE.get());
        registerBlockModel(BloodMagicBlocks.FUNGAL_CHARGE.get());

        registerBlockModel(BloodMagicBlocks.AUG_SHAPED_CHARGE.get());
        registerBlockModel(BloodMagicBlocks.DEFORESTER_CHARGE_2.get());
        registerBlockModel(BloodMagicBlocks.VEINMINE_CHARGE_2.get());
        registerBlockModel(BloodMagicBlocks.FUNGAL_CHARGE_2.get());
        registerBlockModel(BloodMagicBlocks.SHAPED_CHARGE_DEEP.get());

//		registerBlockModel(BloodMagicBlocks.INVERSION_PILLAR.get());
//		registerBlockModel(BloodMagicBlocks.INVERSION_PILLAR_CAP.get());

//		registerBlockModel(BloodMagicBlocks.INVERSION_PILLAR.get());

        registerMultiLayerItem(BloodMagicItems.SLATE_VIAL.get(), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.MELEE_DAMAGE_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.SILK_TOUCH_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.FORTUNE_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.HOLY_WATER_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.QUICK_DRAW_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.LOOTING_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.BOW_POWER_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.WILL_POWER_ANOINTMENT.get(), modLoc("item/alchemic_vial_will"), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_ribbon_will"));
        registerMultiLayerItem(BloodMagicItems.SMELTING_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.BOW_VELOCITY_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.VOIDING_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.WEAPON_REPAIR_ANOINTMENT.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon"));

        registerMultiLayerItem(BloodMagicItems.BOW_POWER_ANOINTMENT_STRONG.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_two"));

        registerMultiLayerItem(BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.SILK_TOUCH_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.FORTUNE_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.HOLY_WATER_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.QUICK_DRAW_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.LOOTING_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.BOW_POWER_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.SMELTING_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.VOIDING_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.BOW_VELOCITY_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.WEAPON_REPAIR_ANOINTMENT_L.get(), modLoc("item/alchemic_liquid"), modLoc("item/labeledalchemic_vial"), modLoc("item/alchemic_ribbon"));

        registerMultiLayerItem(BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_2.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_two"));
        registerMultiLayerItem(BloodMagicItems.FORTUNE_ANOINTMENT_2.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_two"));
        registerMultiLayerItem(BloodMagicItems.HOLY_WATER_ANOINTMENT_2.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_two"));
        registerMultiLayerItem(BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_2.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_two"));
        registerMultiLayerItem(BloodMagicItems.QUICK_DRAW_ANOINTMENT_2.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_two"));
        registerMultiLayerItem(BloodMagicItems.LOOTING_ANOINTMENT_2.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_two"));
        registerMultiLayerItem(BloodMagicItems.BOW_POWER_ANOINTMENT_2.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_two"));
        registerMultiLayerItem(BloodMagicItems.BOW_VELOCITY_ANOINTMENT_2.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_two"));
        registerMultiLayerItem(BloodMagicItems.WEAPON_REPAIR_ANOINTMENT_2.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_two"));

        registerMultiLayerItem(BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.SILK_TOUCH_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.FORTUNE_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.HOLY_WATER_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.QUICK_DRAW_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.LOOTING_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.BOW_POWER_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.SMELTING_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.VOIDING_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.BOW_VELOCITY_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));
        registerMultiLayerItem(BloodMagicItems.WEAPON_REPAIR_ANOINTMENT_XL.get(), modLoc("item/alchemic_liquid"), modLoc("item/greenlabeledalchemic_vial"), modLoc("item/alchemic_ribbon"));

        registerMultiLayerItem(BloodMagicItems.MELEE_DAMAGE_ANOINTMENT_3.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_three"));
        registerMultiLayerItem(BloodMagicItems.FORTUNE_ANOINTMENT_3.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_three"));
        registerMultiLayerItem(BloodMagicItems.HOLY_WATER_ANOINTMENT_3.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_three"));
        registerMultiLayerItem(BloodMagicItems.HIDDEN_KNOWLEDGE_ANOINTMENT_3.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_three"));
        registerMultiLayerItem(BloodMagicItems.QUICK_DRAW_ANOINTMENT_3.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_three"));
        registerMultiLayerItem(BloodMagicItems.LOOTING_ANOINTMENT_3.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_three"));
        registerMultiLayerItem(BloodMagicItems.BOW_POWER_ANOINTMENT_3.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_three"));
        registerMultiLayerItem(BloodMagicItems.BOW_VELOCITY_ANOINTMENT_3.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_three"));
        registerMultiLayerItem(BloodMagicItems.WEAPON_REPAIR_ANOINTMENT_3.get(), modLoc("item/alchemic_liquid"), modLoc("item/alchemic_vial"), modLoc("item/alchemic_ribbon_three"));

        registerMultiLayerItem(BloodMagicItems.ALCHEMY_FLASK.get(), modLoc("item/potionflask_underlay"), modLoc("item/potionflask_outline"), modLoc("item/potionflask_overlay"));
        registerMultiLayerItem(BloodMagicItems.ALCHEMY_FLASK_THROWABLE.get(), modLoc("item/potionflask_underlay"), modLoc("item/potionflask_outline_throwable"), modLoc("item/potionflask_overlay_throwable"));
        registerMultiLayerItem(BloodMagicItems.ALCHEMY_FLASK_LINGERING.get(), modLoc("item/potionflask_underlay"), modLoc("item/potionflask_outline_lingering"), modLoc("item/potionflask_overlay_lingering"));

        registerMultiLayerItem(BloodMagicItems.THROWING_DAGGER_COPPER_POTION.get(), modLoc("item/amethystthrowingdagger_partial"), modLoc("item/dagger_potion"));
    }

    private void createTexturedModelWithCustomPath(String path, ResourceLocation texturePath) {

//		String path = item.getRegistryName().getPath();
        ModelFile activatedFile = singleTexture("item/" + path, mcLoc("item/handheld"), "layer0", texturePath);
//		getBuilder(path).parent(new ModelFile.UncheckedModelFile(rl));
    }

    private void registerCustomFullTexture(Block block, String texturePath) {
        String path = ForgeRegistries.BLOCKS.getKey(block).getPath();
        getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + texturePath)));
    }

    private void registerCustomBlockPath(Block block, String newPath) {
        String path = ForgeRegistries.BLOCKS.getKey(block).getPath();
        getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + newPath)));
    }

    private void registerCustomBlockPathWithTextures(Block block, String newPath, String... textures) {
        String path = ForgeRegistries.BLOCKS.getKey(block).getPath();
        ItemModelBuilder builder = getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + newPath)));
        int length = textures.length / 2;
        for (int i = 1; i < length; i++) {
            builder.texture(textures[i * 2], textures[i * 2 + 1]);
        }
    }

    private void registerBlockModel(Block block) {
        String path = ForgeRegistries.BLOCKS.getKey(block).getPath();
        getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
    }

    private void registerBasicItem(Item item) {
        String path = ForgeRegistries.ITEMS.getKey(item).getPath();
        singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
    }

    private void registerBasicItem(Item item, ResourceLocation separateLocation) {
        String path = ForgeRegistries.ITEMS.getKey(item).getPath();
        singleTexture(path, mcLoc("item/generated"), "layer0", separateLocation);
    }

    private void registerMultiLayerItem(Item item, ResourceLocation... locations) {
        String path = ForgeRegistries.ITEMS.getKey(item).getPath();
        ItemModelBuilder builder = getBuilder(path).parent(getExistingFile(mcLoc("item/generated")));
//		ModelFile model = withExistingParent(path, mcLoc("item/handheld"));

        for (int i = 0; i < locations.length; i++) {
            builder.texture("layer" + i, locations[i]);
        }

        builder.assertExistence();
    }

    private void registerToggleableItem(Item item) {
        String path = ForgeRegistries.ITEMS.getKey(item).getPath();
        ModelFile activatedFile = singleTexture("item/variants/" + path + "_activated", mcLoc("item/handheld"), "layer0", modLoc("item/" + path + "_activated"));
        ModelFile deactivatedFile = singleTexture("item/variants/" + path + "_deactivated", mcLoc("item/handheld"), "layer0", modLoc("item/" + path + "_deactivated"));
        getBuilder(path).override().predicate(BloodMagic.rl("active"), 0).model(deactivatedFile).end().override().predicate(BloodMagic.rl("active"), 1).model(activatedFile).end();
    }

    private void registerDemonWillVariantItem(Item item) {
        String path = ForgeRegistries.ITEMS.getKey(item).getPath();
        ItemModelBuilder builder = getBuilder(path);

        for (EnumDemonWillType type : EnumDemonWillType.values()) {
            String name = "";
            if (type.ordinal() != 0) {
                name = "_" + type.name().toLowerCase(Locale.ROOT);
            }
            ModelFile willFile = singleTexture("item/variants/" + path + name, mcLoc("item/handheld"), "layer0", modLoc("item/" + path + name));
            builder = builder.override().predicate(BloodMagic.rl("type"), type.ordinal()).model(willFile).end();
        }
    }

    private void registerDemonSword(Item item) {
        String path = ForgeRegistries.ITEMS.getKey(item).getPath();
        ItemModelBuilder builder = getBuilder(path);

        for (int i = 0; i <= 1; i++) {
            for (EnumDemonWillType type : EnumDemonWillType.values()) {
                String name = i == 0 ? "_deactivated" : "_activated";
                if (type.ordinal() != 0) {
                    name = "_" + type.name().toLowerCase(Locale.ROOT) + name;
                }
                ModelFile willFile = singleTexture("item/variants/" + path + name, mcLoc("item/handheld"), "layer0", modLoc("item/" + path + name));
                builder = builder.override().predicate(BloodMagic.rl("type"), type.ordinal()).predicate(BloodMagic.rl("active"), i).model(willFile).end();
            }
        }
    }

    private void registerDemonTool(Item item) {
        String path = ForgeRegistries.ITEMS.getKey(item).getPath();
        ItemModelBuilder builder = getBuilder(path);

        for (EnumDemonWillType type : EnumDemonWillType.values()) {
            String name = "";
            if (type.ordinal() != 0) {
                name = "_" + type.name().toLowerCase(Locale.ROOT) + name;
            }
            ModelFile willFile = singleTexture("item/variants/" + path + name, mcLoc("item/handheld"), "layer0", modLoc("item/" + path + name));
            builder = builder.override().predicate(BloodMagic.rl("type"), type.ordinal()).model(willFile).end();

        }
    }

    private void registerSacrificialKnife(Item item) {
        String path = ForgeRegistries.ITEMS.getKey(item).getPath();
        ItemModelBuilder builder = getBuilder(path);

        ModelFile baseKnifeFile = singleTexture("item/variants/" + path, mcLoc("item/handheld"), "layer0", modLoc("item/" + path));
        ModelFile ceremonialKnifeFile = singleTexture("item/variants/" + path + "_ceremonial", mcLoc("item/handheld"), "layer0", modLoc("item/" + path + "_ceremonial"));
        builder = builder.override().predicate(BloodMagic.rl("incense"), 0).model(baseKnifeFile).end().override().predicate(BloodMagic.rl("incense"), 1).model(ceremonialKnifeFile).end();
    }

    private void registerRoutingNode(Block block) {

    }
}
