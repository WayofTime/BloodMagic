package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.inventory.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.common.routing.BasicItemFilter;
import wayoftime.bloodmagic.common.routing.IItemFilter;
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
		return new ContainerFilter(p_createMenu_1_, player, p_createMenu_2_, new InventoryFilter(player.getHeldItemMainhand()));
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

		List<ItemStack> filteredList = new ArrayList<>();
		ItemInventory inv = new ItemInventory(filterStack, 9, "");
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			ItemStack ghostStack = GhostItemHelper.getStackFromGhost(stack);

			filteredList.add(ghostStack);
		}

		testFilter.initializeFilter(filteredList, tile, handler, false);
		return testFilter;
	}

	@Override
	public IItemFilter getOutputItemFilter(ItemStack filterStack, TileEntity tile, IItemHandler handler)
	{
		IItemFilter testFilter = new BasicItemFilter();

		List<ItemStack> filteredList = new ArrayList<>();
		ItemInventory inv = new ItemInventory(filterStack, 9, ""); // TODO: Change to grab the filter from the Item
																	// later.
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			ItemStack ghostStack = GhostItemHelper.getStackFromGhost(stack);
			if (ghostStack.isEmpty())
			{
				ghostStack.setCount(Integer.MAX_VALUE);
			}

			filteredList.add(ghostStack);
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
}
