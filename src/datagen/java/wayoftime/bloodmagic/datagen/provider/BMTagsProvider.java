package wayoftime.bloodmagic.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class BMTagsProvider {
    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final ExistingFileHelper exFiHe;
    public BMTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper exFiHe) {
        this.output = output;
        this.registries = registries;
        this.exFiHe = exFiHe;
    }

    public <T> TagsProvider<T> setup(ResourceKey<Registry<T>> key, Consumer<Function<TagKey<T>, TagsProvider.TagAppender<T>>> consumer) {
        return new TagsProvider<T>(output, key, registries, BloodMagic.MODID, exFiHe) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                consumer.accept(this::tag);
            }
        };
    }
}
