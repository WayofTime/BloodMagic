package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.client.button.FilterButtonTogglePress;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ItemEnchantFilterCore extends ItemItemRouterFilter implements INestableItemFilterProvider
{
	public ItemEnchantFilterCore()
	{
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack filterStack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
//		super.addInformation(filterStack, world, tooltip, flag);
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.enchantfilter.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
		}

		boolean sneaking = Screen.hasShiftDown();
		if (!sneaking)
		{
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.extraInfo").withStyle(ChatFormatting.BLUE));
		} else
		{
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

				List<Component> list = this.getTextForHoverItem(filterStack, Constants.BUTTONID.ENCHANT, i);
				List<Component> fuzzyList = this.getTextForHoverItem(filterStack, Constants.BUTTONID.ENCHANT_LVL, i);
				if (list.size() <= 0 || fuzzyList.size() <= 0)
				{
					continue;
				}

				TranslatableComponent fuzzyText = new TranslatableComponent("tooltip.bloodmagic.filter.enchant_combination", fuzzyList.get(0), list.get(0));

				if (isWhitelist)
				{
					int amount = GhostItemHelper.getItemGhostAmount(stack);
					if (amount > 0)
					{
						tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.count", amount, fuzzyText));
					} else
					{
						tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.all", fuzzyText));
					}

				} else
				{
					tooltip.add(fuzzyText);
				}

				for (int j = 1; j < list.size(); j++)
				{
					tooltip.add(list.get(j));
				}
			}
		}
	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount)
	{
		int index = getEnchantmentIndex(filterStack, slot);
		boolean isFuzzy = getIsFuzzy(filterStack, slot);
		if (index == 0 || index == 1)
		{
			Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(ghostStack);
			if (enchants.size() <= 0)
			{
				return new NoEnchantsFilterKey(amount);
			} else
			{
				boolean matchAll = index == 1;
				return new CollectionEnchantFilterKey(enchants, isFuzzy, matchAll, amount);
			}
		} else
		{
			Pair<Enchantment, Integer> enchant = getEnchantment(filterStack, slot);
			if (enchant != null)
			{
				return new EnchantFilterKey(enchant.getKey(), enchant.getRight(), isFuzzy, amount);
			} else
			{
				return new NoEnchantsFilterKey(amount);
			}
		}
	}

	public int getEnchantmentIndex(ItemStack filterStack, int slot)
	{
		CompoundTag tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundTag();
			filterStack.setTag(tag);
		}

		return tag.getInt(Constants.NBT.ENCHANT + slot);
	}

	public void setEnchantmentIndex(ItemStack filterStack, int slot, int index)
	{
		CompoundTag tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundTag();
			filterStack.setTag(tag);
		}

		tag.putInt(Constants.NBT.ENCHANT + slot, index);
	}

	public void cycleToNextEnchant(ItemStack filterStack, int slot)
	{
		ItemInventory inv = new InventoryFilter(filterStack);

		ItemStack ghostStack = inv.getItem(slot);
		if (ghostStack.isEmpty())
		{
			return;
		}

		int index = getEnchantmentIndex(filterStack, slot);
		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(ghostStack);

		index++;

		if (index >= enchants.size() + 2 || enchants.size() == 0)
		{
			index = 0;
		}

		if (enchants.size() == 1 && index == 1)
		{
			index = 2;
		}

		setEnchantmentIndex(filterStack, slot, index);
	}

	public Pair<Enchantment, Integer> getEnchantment(ItemStack filterStack, int slot)
	{
		int index = getEnchantmentIndex(filterStack, slot);
		if (index <= 0)
		{
			return null;
		}

		index -= 2;

		ItemInventory inv = new InventoryFilter(filterStack);

		ItemStack ghostStack = inv.getItem(slot);
		if (ghostStack.isEmpty())
		{
			return null;
		}

		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(ghostStack);

		if (enchants.size() < index)
		{
			return null;
		}

		ArrayList<Entry<Enchantment, Integer>> enchantList = new ArrayList<>(enchants.entrySet());
		Entry<Enchantment, Integer> entry = enchantList.get(index);

		return Pair.of(entry.getKey(), entry.getValue());
	}

	public boolean getIsFuzzy(ItemStack filterStack, int slot)
	{
		CompoundTag tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundTag();
			filterStack.setTag(tag);
		}

		return tag.getBoolean(Constants.NBT.ENCHANT_LVL + slot);
	}

	public void setIsFuzzy(ItemStack filterStack, int slot, boolean value)
	{
		CompoundTag tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundTag();
			filterStack.setTag(tag);
		}

		tag.putBoolean(Constants.NBT.ENCHANT_LVL + slot, value);
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

		if (buttonKey.equals(Constants.BUTTONID.ENCHANT))
		{
			cycleToNextEnchant(filterStack, ghostItemSlot);
		} else if (buttonKey.equals(Constants.BUTTONID.ENCHANT_LVL))
		{
			setIsFuzzy(filterStack, ghostItemSlot, currentButtonState == 0);
		}

		return super.receiveButtonPress(filterStack, buttonKey, ghostItemSlot, currentButtonState);
	}

	@Override
	public int getCurrentButtonState(ItemStack filterStack, String buttonKey, int ghostItemSlot)
	{
		CompoundTag tag = filterStack.getTag();
		if (tag != null)
		{
			if (buttonKey.equals(Constants.BUTTONID.ENCHANT))
			{
				int state = getEnchantmentIndex(filterStack, ghostItemSlot);
				return state;
			} else if (buttonKey.equals(Constants.BUTTONID.ENCHANT_LVL))
			{
				return getIsFuzzy(filterStack, ghostItemSlot) ? 1 : 0;
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
		if (buttonKey.equals(Constants.BUTTONID.ENCHANT))
		{
			if (currentState == 0 || currentState == 1)
			{
				ItemInventory inv = new InventoryFilter(filterStack);

				ItemStack ghostStack = inv.getItem(ghostItemSlot);
				if (ghostStack.isEmpty())
				{
					componentList.add(new TranslatableComponent("filter.bloodmagic.noenchant"));
					return componentList;
				}

				Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(ghostStack);

				if (enchants.size() > 0)
				{
					if (currentState == 0)
					{
						componentList.add(new TranslatableComponent("filter.bloodmagic.anyenchant"));
					} else
					{
						componentList.add(new TranslatableComponent("filter.bloodmagic.allenchant"));
					}
					for (Entry<Enchantment, Integer> entry : enchants.entrySet())
					{
						componentList.add(entry.getKey().getFullname(entry.getValue()));
					}
				} else
				{
					componentList.add(new TranslatableComponent("filter.bloodmagic.noenchant"));
					return componentList;
				}
			} else
			{
				Pair<Enchantment, Integer> enchant = getEnchantment(filterStack, ghostItemSlot);
				if (enchant != null)
				{
					componentList.add(enchant.getLeft().getFullname(enchant.getRight()));
				}
			}
		} else if (buttonKey.equals(Constants.BUTTONID.ENCHANT_LVL))
		{
			boolean isFuzzy = this.getIsFuzzy(filterStack, ghostItemSlot);
			if (isFuzzy)
			{
				componentList.add(new TranslatableComponent("filter.bloodmagic.enchantfuzzy"));
			} else
			{
				componentList.add(new TranslatableComponent("filter.bloodmagic.enchantnotfuzzy"));
			}
		}

		return componentList;
	}

//	public ITextComponent getItemDesignation(ItemStack filterStack, int ghostItemSlot)
//	{
//		int currentState = getCurrentButtonState(filterStack, Constants.BUTTONID.ENCHANT, ghostItemSlot);
//		if (currentState == 0 || currentState == 1)
//		{
//			ItemInventory inv = new InventoryFilter(filterStack);
//
//			ItemStack ghostStack = inv.getStackInSlot(ghostItemSlot);
//			if (ghostStack.isEmpty())
//			{
//				return new TranslationTextComponent("filter.bloodmagic.noenchant");
//
//			}
//
//			Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(ghostStack);
//
//			if (enchants.size() > 0)
//			{
//				if (currentState == 0)
//				{
//					return new TranslationTextComponent("filter.bloodmagic.anyenchant");
//				} else
//				{
//					return new TranslationTextComponent("filter.bloodmagic.allenchant");
//				}
//				for (Entry<Enchantment, Integer> entry : enchants.entrySet())
//				{
//					return entry.getKey().getDisplayName(entry.getValue());
//				}
//			} else
//			{
//				return new TranslationTextComponent("filter.bloodmagic.noenchant");
//			}
//		} else
//		{
//			Pair<Enchantment, Integer> enchant = getEnchantment(filterStack, ghostItemSlot);
//			if (enchant != null)
//			{
//				return enchant.getLeft().getDisplayName(enchant.getRight());
//			}
//		}
//
//		
//	}

	@OnlyIn(Dist.CLIENT)
	public List<Pair<String, Button.OnPress>> getButtonAction(ContainerFilter container)
	{
		List<Pair<String, Button.OnPress>> buttonList = super.getButtonAction(container);
		buttonList.add(Pair.of(Constants.BUTTONID.ENCHANT, new FilterButtonTogglePress(Constants.BUTTONID.ENCHANT, container)));
		buttonList.add(Pair.of(Constants.BUTTONID.ENCHANT_LVL, new FilterButtonTogglePress(Constants.BUTTONID.ENCHANT_LVL, container)));
		return buttonList;
	}

	@Override
	public Pair<Integer, Integer> getTexturePositionForState(ItemStack filterStack, String buttonKey, int currentButtonState)
	{
		if (buttonKey.equals(Constants.BUTTONID.ENCHANT))
		{
			switch (currentButtonState)
			{
			case 0:
				return Pair.of(216, 0);
			case 1:
				return Pair.of(216, 20);
			default:
				return Pair.of(216, 40);
			}

		} else if (buttonKey.equals(Constants.BUTTONID.ENCHANT_LVL))
		{
			switch (currentButtonState)
			{
			case 0:
				return Pair.of(236, 0);
			default:
				return Pair.of(236, 20);
			}
		}

		return super.getTexturePositionForState(filterStack, buttonKey, currentButtonState);
	}

	@Override
	public boolean isButtonGlobal(ItemStack filterStack, String buttonKey)
	{
		return super.isButtonGlobal(filterStack, buttonKey);
	}
}
