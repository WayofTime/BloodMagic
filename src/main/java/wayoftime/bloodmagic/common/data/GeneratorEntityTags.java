package wayoftime.bloodmagic.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

import java.util.concurrent.CompletableFuture;

public class GeneratorEntityTags extends EntityTypeTagsProvider {
    @Override
    protected void addTags(HolderLookup.Provider p_255894_) {
        tag(BloodMagicTags.TELEPOSE_BLACKLIST);
                //.addTag(Tags.EntityTypes.BOSSES);
    }

    public GeneratorEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookup, BloodMagic.MODID, existingFileHelper);
    }
}
