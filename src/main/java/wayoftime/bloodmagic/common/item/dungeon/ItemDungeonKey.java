package wayoftime.bloodmagic.common.item.dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.util.ResourceLocation;

public class ItemDungeonKey extends ItemDungeonKeyBase
{
	private String resourceKey;

	public ItemDungeonKey(String desc, String resourceKey)
	{
		super(desc);
		this.resourceKey = resourceKey;
	}

	@Override
	public ResourceLocation getValidResourceLocation(List<ResourceLocation> list)
	{
		List<ResourceLocation> subList = new ArrayList<>();
		for (ResourceLocation testLocation : list)
		{
			if (testLocation.toString().contains(resourceKey))
			{
				subList.add(testLocation);
			}
		}

		if (subList.size() <= 0)
		{
			return null;
		}

		Collections.shuffle(subList);
		return subList.get(0);
	}
}
