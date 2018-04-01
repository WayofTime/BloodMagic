package WayofTime.bloodmagic.client.render.alchemyArray;

import net.minecraft.util.ResourceLocation;

public class LowStaticAlchemyCircleRenderer extends LowAlchemyCircleRenderer
{
    public LowStaticAlchemyCircleRenderer()
    {
        this(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret1.png"));
    }

    public LowStaticAlchemyCircleRenderer(ResourceLocation arrayResource)
    {
        super(arrayResource);
    }

    @Override
    public float getRotation(float craftTime)
    {
        float offset = 2;
        float duration = 180;
        if (craftTime >= offset && craftTime < offset + duration)
        {
            float modifier = (craftTime - offset) * 2f;
            return modifier * 1f;
        }
        return 0;
    }
}
