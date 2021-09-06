package wayoftime.bloodmagic.common.item.dungeon;

import java.util.List;

import net.minecraft.util.ResourceLocation;

public interface IDungeonKey
{
	public abstract ResourceLocation getValidResourceLocation(List<ResourceLocation> list);
}
