package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.client.button.FilterButtonTogglePress;
import wayoftime.bloodmagic.common.item.inventory.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.util.Constants;

public class ItemEnchantFilterCore extends ItemRouterFilter implements INestableItemFilterProvider
{
	public ItemEnchantFilterCore()
	{
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack filterStack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
//		super.addInformation(filterStack, world, tooltip, flag);
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.enchantfilter.desc").mergeStyle(TextFormatting.ITALIC).mergeStyle(TextFormatting.GRAY));
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
		CompoundNBT tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundNBT();
			filterStack.setTag(tag);
		}

		return tag.getInt(Constants.NBT.ENCHANT + slot);
	}

	public void setEnchantmentIndex(ItemStack filterStack, int slot, int index)
	{
		CompoundNBT tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundNBT();
			filterStack.setTag(tag);
		}

		tag.putInt(Constants.NBT.ENCHANT + slot, index);
	}

	public void cycleToNextEnchant(ItemStack filterStack, int slot)
	{
		ItemInventory inv = new InventoryFilter(filterStack);

		ItemStack ghostStack = inv.getStackInSlot(slot);
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

		ItemStack ghostStack = inv.getStackInSlot(slot);
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
		CompoundNBT tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundNBT();
			filterStack.setTag(tag);
		}

		return tag.getBoolean(Constants.NBT.ENCHANT_LVL + slot);
	}

	public void setIsFuzzy(ItemStack filterStack, int slot, boolean value)
	{
		CompoundNBT tag = filterStack.getTag();
		if (tag == null)
		{
			tag = new CompoundNBT();
			filterStack.setTag(tag);
		}

		tag.putBoolean(Constants.NBT.ENCHANT_LVL + slot, value);
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
		CompoundNBT tag = filterStack.getTag();
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
	public List<ITextComponent> getTextForHoverItem(ItemStack filterStack, String buttonKey, int ghostItemSlot)
	{
		List<ITextComponent> componentList = super.getTextForHoverItem(filterStack, buttonKey, ghostItemSlot);
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

				ItemStack ghostStack = inv.getStackInSlot(ghostItemSlot);
				if (ghostStack.isEmpty())
				{
					componentList.add(new TranslationTextComponent("filter.bloodmagic.noenchant"));
					return componentList;
				}

				Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(ghostStack);

				if (enchants.size() > 0)
				{
					if (currentState == 0)
					{
						componentList.add(new TranslationTextComponent("filter.bloodmagic.anyenchant"));
					} else
					{
						componentList.add(new TranslationTextComponent("filter.bloodmagic.allenchant"));
					}
					for (Entry<Enchantment, Integer> entry : enchants.entrySet())
					{
						componentList.add(entry.getKey().getDisplayName(entry.getValue()));
					}
				} else
				{
					componentList.add(new TranslationTextComponent("filter.bloodmagic.noenchant"));
					return componentList;
				}
			} else
			{
				Pair<Enchantment, Integer> enchant = getEnchantment(filterStack, ghostItemSlot);
				if (enchant != null)
				{
					componentList.add(enchant.getLeft().getDisplayName(enchant.getRight()));
				}
			}
		} else if (buttonKey.equals(Constants.BUTTONID.ENCHANT_LVL))
		{
			boolean isFuzzy = this.getIsFuzzy(filterStack, ghostItemSlot);
			if (isFuzzy)
			{
				componentList.add(new TranslationTextComponent("filter.bloodmagic.enchantfuzzy"));
			} else
			{
				componentList.add(new TranslationTextComponent("filter.bloodmagic.enchantnotfuzzy"));
			}
		}

		return componentList;
	}

	@OnlyIn(Dist.CLIENT)
	public List<Pair<String, Button.IPressable>> getButtonAction(ContainerFilter container)
	{
		List<Pair<String, Button.IPressable>> buttonList = super.getButtonAction(container);
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
