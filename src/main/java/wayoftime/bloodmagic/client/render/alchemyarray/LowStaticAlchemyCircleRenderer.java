package wayoftime.bloodmagic.client.render.alchemyarray;

import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;

public class LowStaticAlchemyCircleRenderer extends LowAlchemyCircleRenderer
{
	public LowStaticAlchemyCircleRenderer()
	{
		this(BloodMagic.rl("textures/models/AlchemyArrays/SkeletonTurret1.png"));
	}

	public LowStaticAlchemyCircleRenderer(ResourceLocation arrayResource)
	{
		super(arrayResource);
	}

	@Override
	public float getRotation(float craftTime)
	{

		return 0;
	}

	public float getSecondaryRotation(float craftTime)
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
