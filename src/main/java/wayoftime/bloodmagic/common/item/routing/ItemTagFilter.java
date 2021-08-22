package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.client.button.FilterButtonTogglePress;
import wayoftime.bloodmagic.common.item.inventory.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.common.routing.BasicItemFilter;
import wayoftime.bloodmagic.common.routing.BlacklistItemFilter;
import wayoftime.bloodmagic.common.routing.IItemFilter;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ItemTagFilter extends ItemRouterFilter
{
	protected IItemFilter getFilterTypeFromConfig(ItemStack filterStack)
	{
		int state = getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
		if (state == 1)
		{
			return new BlacklistItemFilter();
		}

		return new BasicItemFilter();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack filterStack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.tagfilter.desc").mergeStyle(TextFormatting.ITALIC).mergeStyle(TextFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
		}

		int whitelistState = this.getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
		boolean isWhitelist = whitelistState == 0;

		if (isWhitelist)
		{
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.whitelist"));
		} else
		{
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.blacklist"));
		}

		ItemInventory inv = new InventoryFilter(filterStack);
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			ResourceLocation tag = getItemTagResource(filterStack, i);

			ITextComponent display = stack.getDisplayName();
			if (tag != null)
			{
				display = new StringTextComponent(tag.toString());
			} else
			{
				display = new TranslationTextComponent("tooltip.bloodmagic.filter.anytag", display);
			}

			if (isWhitelist)
			{
				int amount = GhostItemHelper.getItemGhostAmount(stack);
				if (amount > 0)
				{
					tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.count", amount, display));
				} else
				{
					tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.all", display));
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
			List<ITag<Item>> tagList = getAllItemTags(filterStack, slot);
			if (tagList != null && !tagList.isEmpty())
			{
				return new CollectionTagFilterKey(tagList, amount);
			}
		} else
		{
			ITag<Item> tag = getItemTag(filterStack, slot);
			if (tag != null)
			{
				return new TagFilterKey(tag, amount);
			}
		}

		return new BasicFilterKey(ghostStack, amount);
	}

	public int getItemTagIndex(ItemStack filterStack, int slot)
	{
		CompoundNBT tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundNBT();
			filterStack.setTag(tag);
		}

		return tag.getInt(Constants.NBT.ITEMTAG + slot);
	}

	public void setItemTagIndex(ItemStack filterStack, int slot, int index)
	{
		CompoundNBT tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundNBT();
			filterStack.setTag(tag);
		}

		tag.putInt(Constants.NBT.ITEMTAG + slot, index);
	}

	public void cycleToNextTag(ItemStack filterStack, int slot)
	{
		ItemInventory inv = new InventoryFilter(filterStack);

		ItemStack ghostStack = inv.getStackInSlot(slot);
		if (ghostStack.isEmpty())
		{
			return;
		}

		int index = getItemTagIndex(filterStack, slot);
		Set<ResourceLocation> tagRLs = ghostStack.getItem().getTags();
		index++;

		if (index > tagRLs.size())
		{
			index = 0;
		}

		setItemTagIndex(filterStack, slot, index);
	}

	public ITag<Item> getItemTag(ItemStack filterStack, int slot)
	{
		ResourceLocation rl = getItemTagResource(filterStack, slot);
		if (rl == null)
		{
			return null;
		}

		return TagCollectionManager.getManager().getItemTags().get(rl);
	}

	public ResourceLocation getItemTagResource(ItemStack filterStack, int slot)
	{
		int index = getItemTagIndex(filterStack, slot);
		if (index <= 0)
		{
			return null;
		}

		index--;

		ItemInventory inv = new InventoryFilter(filterStack);

		ItemStack ghostStack = inv.getStackInSlot(slot);
		if (ghostStack.isEmpty())
		{
			return null;
		}

		List<ResourceLocation> tagRLs = new ArrayList<ResourceLocation>(ghostStack.getItem().getTags());

		if (tagRLs.size() < index)
		{
			return null;
		}

		ResourceLocation rl = tagRLs.get(index);

		return rl;
	}

	public List<ITag<Item>> getAllItemTags(ItemStack filterStack, int slot)
	{
		ItemInventory inv = new InventoryFilter(filterStack);

		ItemStack ghostStack = inv.getStackInSlot(slot);
		if (ghostStack.isEmpty())
		{
			return null;
		}

		Set<ResourceLocation> tagRLs = ghostStack.getItem().getTags();
		List<ITag<Item>> tagList = new ArrayList<ITag<Item>>();

		for (ResourceLocation rl : tagRLs)
		{
			tagList.add(TagCollectionManager.getManager().getItemTags().get(rl));
		}

		return tagList;
	}

	@Override
	public int receiveButtonPress(ItemStack filterStack, String buttonKey, int ghostItemSlot, int currentButtonState)
	{
		// Returns new state that the pressed button is in. -1 for an invalid button.
		CompoundNBT tag = filterStack.getTag();
		if (tag == null)
		{
			filterStack.setTag(new CompoundNBT());
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
		CompoundNBT tag = filterStack.getTag();
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
	public List<ITextComponent> getTextForHoverItem(ItemStack filterStack, String buttonKey, int ghostItemSlot)
	{
		List<ITextComponent> componentList = super.getTextForHoverItem(filterStack, buttonKey, ghostItemSlot);
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

				ItemStack ghostStack = inv.getStackInSlot(ghostItemSlot);
				if (ghostStack.isEmpty())
				{
					componentList.add(new TranslationTextComponent("filter.bloodmagic.novalidtag"));
					return componentList;
				}

				Set<ResourceLocation> locations = ghostStack.getItem().getTags();

				if (locations.size() > 0)
				{
					componentList.add(new TranslationTextComponent("filter.bloodmagic.anytag"));
					for (ResourceLocation rl : locations)
					{
						componentList.add(new StringTextComponent(rl.toString()));
					}
				} else
				{
					componentList.add(new TranslationTextComponent("filter.bloodmagic.novalidtag"));
					return componentList;
				}
			} else
			{
				ResourceLocation rl = getItemTagResource(filterStack, ghostItemSlot);
				if (rl != null)
				{
					componentList.add(new TranslationTextComponent("filter.bloodmagic.specifiedtag"));
					componentList.add(new StringTextComponent(rl.toString()));
				}
			}
		}

		return componentList;
	}

	@OnlyIn(Dist.CLIENT)
	public List<Pair<String, Button.IPressable>> getButtonAction(ContainerFilter container)
	{
		List<Pair<String, Button.IPressable>> buttonList = super.getButtonAction(container);
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
