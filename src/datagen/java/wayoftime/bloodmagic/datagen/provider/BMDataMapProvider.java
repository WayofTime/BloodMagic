package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import wayoftime.bloodmagic.datagen.content.datamap.TartaricGemMax;

import java.util.concurrent.CompletableFuture;

public class BMDataMapProvider extends DataMapProvider {
    protected BMDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        TartaricGemMax.bootstrap(this::builder);
    }
}
