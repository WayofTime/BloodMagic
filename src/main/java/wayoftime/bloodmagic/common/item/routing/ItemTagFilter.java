package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.client.button.FilterButtonTogglePress;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ItemTagFilter extends ItemItemRouterFilter implements INestableItemFilterProvider
{
//	protected IRoutingFilter<ItemStack> getFilterTypeFromConfig(ItemStack filterStack)
//	{
//		int state = getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
//		if (state == 1)
//		{
//			return new BlacklistItemFilter();
//		}
//
//		return new BasicItemFilter();
//	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack filterStack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.tagfilter.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
		}

		int whitelistState = this.getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
		boolean isWhitelist = whitelistState == 0;

		if (isWhitelist)
		{
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.whitelist").withStyle(ChatFormatting.GRAY));
		} else
		{
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.blacklist").withStyle(ChatFormatting.GRAY));
		}

		ItemInventory inv = new InventoryFilter(filterStack);
		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty())
			{
				continue;
			}

			ResourceLocation tag = getItemTagResource(filterStack, i);

			Component display = stack.getHoverName();
			if (tag != null)
			{
				display = new TextComponent(tag.toString());
			} else
			{
				display = new TranslatableComponent("tooltip.bloodmagic.filter.anytag", display);
			}

			if (isWhitelist)
			{
				int amount = GhostItemHelper.getItemGhostAmount(stack);
				if (amount > 0)
				{
					tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.count", amount, display));
				} else
				{
					tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.all", display));
				}
			} else
			{
				tooltip.add(display);
			}
		}
	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount)
	{
		int index = getItemTagIndex(filterStack, slot);
		if (index == 0)
		{
			List<TagKey<Item>> tagList = getAllItemTags(filterStack, slot);
			if (tagList != null && !tagList.isEmpty())
			{
				return new CollectionTagFilterKey(tagList, amount);
			}
		} else
		{
			TagKey<Item> tag = getItemTag(filterStack, slot);
			if (tag != null)
			{
				return new TagFilterKey(tag, amount);
			}
		}

		return new BasicFilterKey(ghostStack, amount);
	}

	public int getItemTagIndex(ItemStack filterStack, int slot)
	{
		CompoundTag tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundTag();
			filterStack.setTag(tag);
		}

		return tag.getInt(Constants.NBT.ITEMTAG + slot);
	}

	public void setItemTagIndex(ItemStack filterStack, int slot, int index)
	{
		CompoundTag tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundTag();
			filterStack.setTag(tag);
		}

		tag.putInt(Constants.NBT.ITEMTAG + slot, index);
	}

	public void cycleToNextTag(ItemStack filterStack, int slot)
	{
		ItemInventory inv = new InventoryFilter(filterStack);

		ItemStack ghostStack = inv.getItem(slot);
		if (ghostStack.isEmpty())
		{
			return;
		}

		int index = getItemTagIndex(filterStack, slot);
		List<TagKey<Item>> tags = getAllItemTags(ghostStack);
		index++;

		if (index > tags.size())
		{
			index = 0;
		}

		setItemTagIndex(filterStack, slot, index);

		setItemTag(filterStack, slot, index > 0 ? tags.get(index - 1) : null);
	}

	public void setItemTag(ItemStack filterStack, int slot, TagKey<Item> tag)
	{
		CompoundTag nbt = filterStack.getOrCreateTag();
		if (tag == null)
		{
			nbt.putString(Constants.NBT.TAG + slot, "");
			return;
		}

		ResourceLocation rl = tag.location();

		nbt.putString(Constants.NBT.TAG + slot, rl.toString());
	}

	public TagKey<Item> getItemTag(ItemStack filterStack, int slot)
	{
//		ResourceLocation rl = getItemTagResource(filterStack, slot);
//		if (rl == null)
//		{
//			return null;
//		}
//
//		return SerializationTags.getInstance().getItems().getTag(rl);

		int index = getItemTagIndex(filterStack, slot);
		if (index <= 0)
		{
			return null;
		}

		index--;

		ItemInventory inv = new InventoryFilter(filterStack);

		ItemStack ghostStack = inv.getItem(slot);
		if (ghostStack.isEmpty())
		{
			return null;
		}

//		List<ResourceLocation> tagRLs = new ArrayList<ResourceLocation>(ghostStack.getItem().getTags());

		String tagName = filterStack.getOrCreateTag().getString(Constants.NBT.TAG + slot);
		if (tagName.isEmpty())
		{
			List<TagKey<Item>> tags = getAllItemTags(ghostStack);

			if (tags.size() <= index)
			{
				return null;
			}
			TagKey<Item> tag = tags.get(index);
			setItemTag(filterStack, slot, tag);
			return tag;
		} else
		{
			ResourceLocation rl = new ResourceLocation(tagName);
			TagKey<Item> tag = TagKey.create(Registry.ITEM_REGISTRY, rl);
			return tag;
		}
	}

	public ResourceLocation getItemTagResource(ItemStack filterStack, int slot)
	{
		TagKey<Item> tag = getItemTag(filterStack, slot);
		if (tag == null)
		{
			return null;
		}

		return tag.location();
	}

	public List<TagKey<Item>> getAllItemTags(ItemStack filterStack, int slot)
	{
		ItemInventory inv = new InventoryFilter(filterStack);

		ItemStack ghostStack = inv.getItem(slot);
		if (ghostStack.isEmpty())
		{
			return new ArrayList<>();
		}

		return getAllItemTags(ghostStack);
	}

	public List<TagKey<Item>> getAllItemTags(ItemStack ghostStack)
	{
		if (ghostStack.isEmpty())
		{
			return new ArrayList<>();
		}

		List<TagKey<Item>> tagList = new ArrayList<>();
		Stream<TagKey<Item>> stream = ghostStack.getTags();

		stream.forEach(a -> {
			tagList.add(a);
		});

		return tagList;
	}

	@Override
	public int receiveButtonPress(ItemStack filterStack, String buttonKey, int ghostItemSlot, int currentButtonState)
	{
		// Returns new state that the pressed button is in. -1 for an invalid button.
		CompoundTag tag = filterStack.getTag();
		if (tag == null)
		{
			filterStack.setTag(new CompoundTag());
			tag = filterStack.getTag();
		}

		if (buttonKey.equals(Constants.BUTTONID.ITEMTAG))
		{
			cycleToNextTag(filterStack, ghostItemSlot);
		}

		return super.receiveButtonPress(filterStack, buttonKey, ghostItemSlot, currentButtonState);
	}

	@Override
	public int getCurrentButtonState(ItemStack filterStack, String buttonKey, int ghostItemSlot)
	{
		CompoundTag tag = filterStack.getTag();
		if (tag != null)
		{
			if (buttonKey.equals(Constants.BUTTONID.ITEMTAG))
			{
				int state = getItemTagIndex(filterStack, ghostItemSlot);
				return state;
			}
		}

		return super.getCurrentButtonState(filterStack, buttonKey, ghostItemSlot);
	}

	@Override
	public List<Component> getTextForHoverItem(ItemStack filterStack, String buttonKey, int ghostItemSlot)
	{
		List<Component> componentList = super.getTextForHoverItem(filterStack, buttonKey, ghostItemSlot);
		if (ghostItemSlot < 0)
		{
			return componentList;
		}

		int currentState = getCurrentButtonState(filterStack, buttonKey, ghostItemSlot);
		if (buttonKey.equals(Constants.BUTTONID.ITEMTAG))
		{
			if (currentState == 0)
			{
				ItemInventory inv = new InventoryFilter(filterStack);

				ItemStack ghostStack = inv.getItem(ghostItemSlot);
				if (ghostStack.isEmpty())
				{
					componentList.add(new TranslatableComponent("filter.bloodmagic.novalidtag"));
					return componentList;
				}

				Stream<TagKey<Item>> stream = ghostStack.getTags();

				List<ResourceLocation> locations = new ArrayList<>();
				stream.forEach(a -> {
					locations.add(a.registry().location());
				});

//				Set<ResourceLocation> locations = ghostStack.getItem().getTags();

				if (locations.size() > 0)
				{
					componentList.add(new TranslatableComponent("filter.bloodmagic.anytag"));
					for (ResourceLocation rl : locations)
					{
						componentList.add(new TextComponent(rl.toString()));
					}
				} else
				{
					componentList.add(new TranslatableComponent("filter.bloodmagic.novalidtag"));
					return componentList;
				}
			} else
			{
				ResourceLocation rl = getItemTagResource(filterStack, ghostItemSlot);
				if (rl != null)
				{
					componentList.add(new TranslatableComponent("filter.bloodmagic.specifiedtag"));
					componentList.add(new TextComponent(rl.toString()));
				}
			}
		}

		return componentList;
	}

	@OnlyIn(Dist.CLIENT)
	public List<Pair<String, Button.OnPress>> getButtonAction(ContainerFilter container)
	{
		List<Pair<String, Button.OnPress>> buttonList = super.getButtonAction(container);
		buttonList.add(Pair.of(Constants.BUTTONID.ITEMTAG, new FilterButtonTogglePress(Constants.BUTTONID.ITEMTAG, container)));
		return buttonList;
	}

	@Override
	public Pair<Integer, Integer> getTexturePositionForState(ItemStack filterStack, String buttonKey, int currentButtonState)
	{
		if (buttonKey.equals(Constants.BUTTONID.ITEMTAG))
		{
			switch (currentButtonState)
			{
			case 0:
				return Pair.of(196, 20);
			default:
				return Pair.of(196, 0);
			}

		}

		return super.getTexturePositionForState(filterStack, buttonKey, currentButtonState);
	}
}
