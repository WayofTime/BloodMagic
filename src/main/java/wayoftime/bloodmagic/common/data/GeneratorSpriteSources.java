package wayoftime.bloodmagic.common.data;

import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;
import wayoftime.bloodmagic.BloodMagic;

public class GeneratorSpriteSources extends SpriteSourceProvider {

    public GeneratorSpriteSources(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, BloodMagic.MODID);
    }

    @Override
    protected void addSources() {
        atlas(BLOCKS_ATLAS).addSource(new DirectoryLister("models", "models/"));
    }
}
