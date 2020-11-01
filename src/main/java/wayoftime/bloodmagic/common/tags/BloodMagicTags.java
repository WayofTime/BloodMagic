package wayoftime.bloodmagic.common.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class BloodMagicTags
{
	public static final ITag.INamedTag<Item> ARC_TOOL = ItemTags.makeWrapperTag("bloodmagic:arc/tool");
	public static final ITag.INamedTag<Item> ARC_TOOL_FURNACE = ItemTags.makeWrapperTag("bloodmagic:arc/furnace");
	public static final ITag.INamedTag<Item> ARC_TOOL_SIEVE = ItemTags.makeWrapperTag("bloodmagic:arc/sieve");
	public static final ITag.INamedTag<Item> ARC_TOOL_REVERTER = ItemTags.makeWrapperTag("bloodmagic:arc/reverter");
	public static final ITag.INamedTag<Item> ARC_TOOL_EXPLOSIVE = ItemTags.makeWrapperTag("bloodmagic:arc/explosive");
	public static final ITag.INamedTag<Item> ARC_TOOL_HYDRATE = ItemTags.makeWrapperTag("bloodmagic:arc/hydrate");

	// Dusts
	public static final ITag.INamedTag<Item> DUST_IRON = getForgeDustTag("iron");
	public static final ITag.INamedTag<Item> DUST_GOLD = getForgeDustTag("gold");
	public static final ITag.INamedTag<Item> DUST_COAL = getForgeDustTag("coal");
	public static final ITag.INamedTag<Item> DUST_SALTPETER = getForgeDustTag("saltpeter");
	public static final ITag.INamedTag<Item> DUST_SULFUR = getForgeDustTag("sulfur");

	public static ITag.INamedTag<Item> getForgeDustTag(String name)
	{
		return ItemTags.makeWrapperTag("forge:dusts/" + name);
	}
}
