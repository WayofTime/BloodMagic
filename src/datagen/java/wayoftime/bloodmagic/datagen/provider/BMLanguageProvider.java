package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.fluid.BMFluids;

public class BMLanguageProvider extends LanguageProvider {

    public BMLanguageProvider(PackOutput output, String locale) {
        super(output, BloodMagic.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add(BMFluids.LIFE_ESSENCE_TYPE.get().getDescriptionId(), "Life Essence");
        add(BMFluids.LIFE_ESSENCE_BUCKET.get(), "Bucket of Life");
        add(BMFluids.LIFE_ESSENCE_BLOCK.get(), "Life Essence");

        add(BMFluids.DOUBT_TYPE.get().getDescriptionId(), "Liquid Doubt");
        add(BMFluids.DOUBT_BUCKET.get(), "Doubt Bucket");
        add(BMFluids.DOUBT_BLOCK.get(), "Liquid Doubt");
    }
}
