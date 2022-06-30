package wayoftime.bloodmagic.common.item.dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.resources.ResourceLocation;

public class ItemDungeonKey extends ItemDungeonKeyBase
{
	private String[] resourceKeys;

	public ItemDungeonKey(String desc, String... resourceKeys)
	{
		super(desc);
		this.resourceKeys = resourceKeys;
	}

	@Override
	public ResourceLocation getValidResourceLocation(List<ResourceLocation> list)
	{
		List<ResourceLocation> subList = new ArrayList<>();
		for (ResourceLocation testLocation : list)
		{
			for (String key : resourceKeys)
			{
				if (testLocation.toString().contains(key))
				{
					subList.add(testLocation);
				}
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
