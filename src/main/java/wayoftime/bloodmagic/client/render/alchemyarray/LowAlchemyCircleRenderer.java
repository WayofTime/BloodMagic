package wayoftime.bloodmagic.client.render.alchemyarray;

import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;

public class LowAlchemyCircleRenderer extends SingleAlchemyCircleRenderer
{
	public LowAlchemyCircleRenderer()
	{
		this(BloodMagic.rl("textures/models/AlchemyArrays/SkeletonTurret1.png"));
	}

	public LowAlchemyCircleRenderer(ResourceLocation arrayResource)
	{
		super(arrayResource);
	}

	@Override
	public float getVerticalOffset(float craftTime)
	{
		return -0.4f;
	}
}
