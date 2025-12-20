package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.BMIdentifiers.Sigils;
import wayoftime.bloodmagic.api.sigil.SigilEffect;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.item.BMItems;

import java.util.function.Supplier;

public class BMItemModelProvider extends ItemModelProvider {
    public BMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BloodMagic.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        BMItems.BASIC_ITEMS.getEntries().stream().map(Supplier::get).forEach(this::basicItem);
        basicItem(BMItems.LIVING_PLATE.get());
        basicItem(BMItems.UPGRADE_TOME.get());
        BMItems.WILL_ITEMS.getEntries().forEach(item -> {
            String path = item.getId().getPath();
            ItemModelBuilder builder = getBuilder(path);
            for (EnumWillType type : EnumWillType.values()) {
                ModelFile modelFile = singleTexture(String.format("item/variant/%s_%s", path, type.getSerializedName()), mcLoc("item/handheld"), "layer0", modLoc(String.format("item/%s_%s", path, type.getSerializedName())));
                builder.override().predicate(BloodMagic.TYPE_PROPERTY, type.ordinal()).model(modelFile).end();
            }
        });

        ItemModelBuilder builder = getBuilder(BMItems.SACRIFICIAL_DAGGER.getId().getPath());
        ModelFile normalDagger = singleTexture("item/variant/sacrificial_dagger_normal", mcLoc("item/handheld"), "layer0", modLoc("item/sacrificial_dagger"));
        ModelFile chargedDagger = singleTexture("item/variant/sacrificial_dagger_charged", mcLoc("item/handheld"), "layer0", modLoc("item/sacrificial_dagger_charged"));
        builder.override().predicate(BloodMagic.INCENSE_PROPERTY, 0).model(normalDagger).end();
        builder.override().predicate(BloodMagic.INCENSE_PROPERTY, 1).model(chargedDagger).end();

        createSigilModels();
    }

    private void createSigilModels() {
        createStandard(Sigils.DIVINATION);
        createStandard(Sigils.SEER);
        createStandard(Sigils.LAVA);
        createStandard(Sigils.WATER);
        createStandard(Sigils.VOID);
        createToggle(Sigils.MINER);
    }

    private void createStandard(ResourceKey<SigilEffect> key) {
        String path = key.location().getPath();
        getBuilder("sigil_" + path)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", modLoc("item/sigil_" + path));
    }

    private void createToggle(ResourceKey<SigilEffect> key) {
        String path = key.location().getPath();
        getBuilder("sigil_" + path)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", modLoc("item/sigil_" + path + "_deactivated"))
                .override().predicate(BMIdentifiers.ItemProperties.SIGIL_ACTIVE, 1)
                .model(singleTexture("item/sigil_" + path + "_activated", mcLoc("item/handheld"), "layer0", modLoc("item/sigil_" + path + "_activated"))).end();
    }
}
