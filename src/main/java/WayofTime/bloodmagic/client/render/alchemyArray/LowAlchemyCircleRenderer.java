package WayofTime.bloodmagic.client.render.alchemyArray;

import net.minecraft.util.ResourceLocation;

public class LowAlchemyCircleRenderer extends SingleAlchemyCircleRenderer {
    public LowAlchemyCircleRenderer() {
        this(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret1.png"));
    }

    public LowAlchemyCircleRenderer(ResourceLocation arrayResource) {
        super(arrayResource);
    }

    @Override
    public float getVerticalOffset(float craftTime) {
        return 0;
    }
}
