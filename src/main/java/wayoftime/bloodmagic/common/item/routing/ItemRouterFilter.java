package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.button.BlackWhitelistTogglePress;
import wayoftime.bloodmagic.common.item.inventory.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.common.routing.BasicItemFilter;
import wayoftime.bloodmagic.common.routing.IItemFilter;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;
import wayoftime.bloodmagic.util.Utils;

public class ItemRouterFilter extends Item implements INamedContainerProvider, IItemFilterProvider
{
	public static final int inventorySize = 9;

	public ItemRouterFilter()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public boolean hasEffect(ItemStack stack)
//	{
//		return true;
//	}

//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
//	{
//		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.experienceTome").mergeStyle(TextFormatting.GRAY));
//
//		if (!stack.hasTag())
//			return;
//
//		double storedExp = getStoredExperience(stack);
//
//		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.experienceTome.exp", (int) storedExp).mergeStyle(TextFormatting.GRAY));
//		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.experienceTome.expLevel", getLevelForExperience(storedExp)).mergeStyle(TextFormatting.GRAY));
//	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote)
		{
			Utils.setUUID(stack);

			if (player instanceof ServerPlayerEntity)
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, this, buf -> buf.writeItemStack(stack, false));
			}
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity player)
	{
		// TODO Auto-generated method stub
		assert player.getEntityWorld() != null;
		return new ContainerFilter(p_createMenu_1_, player, p_createMenu_2_, player.getHeldItemMainhand());
	}

	@Override
	public ITextComponent getDisplayName()
	{
		// TODO Auto-generated method stub
		return new StringTextComponent("Filter");
	}

	@Override
	public ItemStack getContainedStackForItem(ItemStack filterStack, ItemStack keyStack)
	{
		ItemStack copyStack = keyStack.copy();
		GhostItemHelper.setItemGhostAmount(copyStack, 0);
		copyStack.setCount(1);
		return copyStack;
	}

	@Override
	public IItemFilter getInputItemFilter(ItemStack filterStack, TileEntity tile, IItemHandler handler)
	{
		IItemFilter testFilter = new BasicItemFilter();

		List<IFilterKey> filteredList = new ArrayList<>();
		ItemInventory inv = new ItemInventory(filterStack, 9, "");
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			int amount = GhostItemHelper.getItemGhostAmount(stack);
			ItemStack ghostStack = GhostItemHelper.getSingleStackFromGhost(stack);

			filteredList.add(new BasicFilterKey(ghostStack, amount));
		}

		testFilter.initializeFilter(filteredList, tile, handler, false);
		return testFilter;
	}

	@Override
	public IItemFilter getOutputItemFilter(ItemStack filterStack, TileEntity tile, IItemHandler handler)
	{
		IItemFilter testFilter = new BasicItemFilter();

		List<IFilterKey> filteredList = new ArrayList<>();
		ItemInventory inv = new ItemInventory(filterStack, 9, ""); // TODO: Change to grab the filter from the Item
																	// later.
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			int amount = GhostItemHelper.getItemGhostAmount(stack);
			ItemStack ghostStack = GhostItemHelper.getSingleStackFromGhost(stack);
			if (amount == 0)
			{
				amount = Integer.MAX_VALUE;
			}

			filteredList.add(new BasicFilterKey(ghostStack, amount));
		}

		testFilter.initializeFilter(filteredList, tile, handler, true);
		return testFilter;
	}

	@Override
	public void setGhostItemAmount(ItemStack filterStack, int ghostItemSlot, int amount)
	{
		ItemInventory inv = new ItemInventory(filterStack, 9, "");
		ItemStack stack = inv.getStackInSlot(ghostItemSlot);
		if (!stack.isEmpty())
		{
			GhostItemHelper.setItemGhostAmount(stack, amount);

			inv.writeToStack(filterStack);
		}
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
		System.out.println("Button pressed: " + buttonKey);

		if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
		{
			int nextState = 0;
			switch (currentButtonState)
			{
			case 0:
				nextState = 1;
				break;
			default:
				nextState = 0;
			}

			tag.putInt(Constants.NBT.BLACKWHITELIST, nextState);
			System.out.println("Setting next state as: " + nextState);

			return nextState;
		}

		return -1;
	}

	@Override
	public int getCurrentButtonState(ItemStack filterStack, String buttonKey, int ghostItemSlot)
	{
		CompoundNBT tag = filterStack.getTag();
		if (tag != null)
		{
			if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
			{
				int state = tag.getInt(Constants.NBT.BLACKWHITELIST);
				return state;
			}
		}

		return 0;
	}

	@Override
	public ITextComponent getTextForHoverItem(ItemStack filterStack, String buttonKey, int ghostItemSlot)
	{
		int currentState = getCurrentButtonState(filterStack, buttonKey, ghostItemSlot);
		if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
		{
			switch (currentState)
			{
			case 1:
				return new StringTextComponent("Blacklist");
			default:
				return new StringTextComponent("Whitelist");
			}
		}

		return new StringTextComponent("ERROR");
	}

	@OnlyIn(Dist.CLIENT)
	public List<Pair<String, Button.IPressable>> getButtonAction(ContainerFilter container)
	{
		List<Pair<String, Button.IPressable>> buttonList = new ArrayList<Pair<String, IPressable>>();
		buttonList.add(Pair.of(Constants.BUTTONID.BLACKWHITELIST, new BlackWhitelistTogglePress(Constants.BUTTONID.BLACKWHITELIST, container)));
		return buttonList;
	}

	@Override
	public Pair<Integer, Integer> getTexturePositionForState(String buttonKey, int currentButtonState)
	{
		if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
		{
			switch (currentButtonState)
			{
			case 1:
				return Pair.of(176, 20);
			default:
				return Pair.of(176, 0);
			}
		}
		return Pair.of(176, 0);
	}

	@Override
	public boolean isButtonGlobal(ItemStack filterStack, String buttonKey)
	{
		return buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST);
	}
}
