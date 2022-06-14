package wayoftime.bloodmagic.common.item.sigil;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.api.compat.IAltarReader;
import wayoftime.bloodmagic.client.key.IKeybindable;
import wayoftime.bloodmagic.client.key.KeyBindings;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.common.item.inventory.ContainerHolding;
import wayoftime.bloodmagic.common.item.inventory.InventoryHolding;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilHolding extends ItemSigilBase implements IKeybindable, IAltarReader, ISigil.Holding, INamedContainerProvider
{
	public static final int inventorySize = 5;

	public ItemSigilHolding()
	{
		super("holding");
	}

	@Override
	public void onKeyPressed(ItemStack stack, PlayerEntity player, KeyBindings key, boolean showInChat)
	{
		if (stack == player.getMainHandItem() && stack.getItem() instanceof ItemSigilHolding && key.equals(KeyBindings.OPEN_HOLDING))
		{
			Utils.setUUID(stack);

			if (player instanceof ServerPlayerEntity)
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, this, buf -> buf.writeItemStack(stack, false));
			}
//			player.openGui(BloodMagic.instance, Constants.Gui.SIGIL_HOLDING_GUI, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
		}
	}

//	@Override
//	public String getHighlightTip(ItemStack stack, String displayName)
//	{
//		List<ItemStack> inv = getInternalInventory(stack);
//		int currentSlot = getCurrentItemOrdinal(stack);
//		ItemStack item = inv.get(currentSlot);
//
//		if (item.isEmpty())
//			return displayName;
//		else
//			return TextHelper.localizeEffect("item.bloodmagic.sigil.holding.display", displayName, item.getDisplayName());
//	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		super.appendHoverText(stack, world, tooltip, flag);
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sigil.holding.press", new TranslationTextComponent(KeyBindings.OPEN_HOLDING.getKey().saveString()).withStyle(TextFormatting.ITALIC)).withStyle(TextFormatting.GRAY));

		if (!stack.hasTag())
			return;

		List<ItemStack> inv = getInternalInventory(stack);
		int currentSlot = getCurrentItemOrdinal(stack);
		ItemStack item = inv.get(currentSlot);

		for (int i = 0; i < inventorySize; i++)
		{
			ItemStack invStack = inv.get(i);
			if (!invStack.isEmpty())
				if (!item.isEmpty() && invStack == item)
				{
					tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sigil.holding.sigilInSlot", i + 1, (invStack.getHoverName().plainCopy()).withStyle(TextFormatting.ITALIC, TextFormatting.UNDERLINE)));
//					tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sigil.holding.sigilInSlot", i + 1, new TranslationTextComponent(invStack.getDisplayName()).mergeStyle(TextFormatting.ITALIC, TextFormatting.UNDERLINE)));

				} else
					tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sigil.holding.sigilInSlot", i + 1, invStack.getHoverName()));
		}
	}

	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
//		BlockPos pos = context.getPos();
//		Direction facing = context.getFace();
//		pos = pos.offset(facing);
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		ItemStack stack = player.getItemInHand(hand);

//		ItemStack stack = player.getHeldItem(hand);
		if (PlayerHelper.isFakePlayer(player))
			return ActionResultType.FAIL;

		int currentSlot = getCurrentItemOrdinal(stack);
		NonNullList<ItemStack> inv = getInternalInventory(stack);
		ItemStack itemUsing = inv.get(currentSlot);

		if (itemUsing.isEmpty() || ((IBindable) itemUsing.getItem()).getBinding(itemUsing) == null)
			return ActionResultType.PASS;

		ActionResultType result = itemUsing.getItem().useOn(context);
		saveInventory(stack, inv);

		return result;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (PlayerHelper.isFakePlayer(player))
			return ActionResult.fail(stack);

		int currentSlot = getCurrentItemOrdinal(stack);
		NonNullList<ItemStack> inv = getInternalInventory(stack);
		ItemStack itemUsing = inv.get(currentSlot);

		if (itemUsing.isEmpty() || ((IBindable) itemUsing.getItem()).getBinding(itemUsing) == null)
			return ActionResult.pass(stack);

		itemUsing.getItem().use(world, player, hand);

		saveInventory(stack, inv);

		return ActionResult.pass(stack);
	}

	@Nonnull
	@Override
	public ItemStack getHeldItem(ItemStack holdingStack, PlayerEntity player)
	{
		return getInternalInventory(holdingStack).get(getCurrentItemOrdinal(holdingStack));
	}

	public void saveInventory(ItemStack itemStack, NonNullList<ItemStack> inventory)
	{
		CompoundNBT itemTag = itemStack.getTag();

		if (itemTag == null)
			itemStack.setTag(itemTag = new CompoundNBT());

		ItemStackHelper.saveAllItems(itemTag, inventory);

//		CompoundNBT inventoryTag = new CompoundNBT();
//		ListNBT itemList = new ListNBT();
//
//		for (int i = 0; i < inventorySize; i++)
//		{
//			if (!inventory.get(i).isEmpty())
//			{
//				CompoundNBT tag = new CompoundNBT();
//				tag.putByte(Constants.NBT.SLOT, (byte) i);
//				inventory.get(i).writeToNBT(tag);
//				itemList.appendTag(tag);
//			}
//		}
//
//		inventoryTag.put(Constants.NBT.ITEMS, itemList);
//		itemTag.put(Constants.NBT.ITEM_INVENTORY, inventoryTag);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (stack.hasTag())
			tickInternalInventory(stack, world, entity, itemSlot, isSelected);
	}

	public void tickInternalInventory(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		for (ItemStack stack : getInternalInventory(itemStack))
		{
			if (stack.isEmpty() || !(stack.getItem() instanceof IBindable) || !(stack.getItem() instanceof ISigil))
				continue;

			Binding binding = ((IBindable) stack.getItem()).getBinding(stack);
			if (binding == null)
				continue;

			stack.getItem().inventoryTick(stack, world, entity, itemSlot, isSelected);
		}
	}

//	@Override
//	public void gatherVariants(@Nonnull Int2ObjectMap<String> variants)
//	{
//		// No-op - Just here to stop the super from running since we're using a mesh
//		// provider
//	}

//	@Override
//	public ItemMeshDefinition getMeshDefinition()
//	{
//		return stack -> {
//			if (stack.hasTag() && stack.getTag().hasKey("color"))
//				return new ModelResourceLocation(getRegistryName(), "type=color");
//			return new ModelResourceLocation(getRegistryName(), "type=normal");
//		};
//	}
//
//	@Override
//	public void gatherVariants(Consumer<String> variants)
//	{
//		variants.accept("type=normal");
//		variants.accept("type=color");
//	}

	public static int next(int mode)
	{
		int index = mode + 1;

		if (index >= inventorySize)
		{
			index = 0;
		}

		return index;
	}

	public static int prev(int mode)
	{
		int index = mode - 1;

		if (index < 0)
		{
			index = inventorySize;
		}

		return index;
	}

	private static void initModeTag(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack = NBTHelper.checkNBT(stack);
			stack.getTag().putInt(Constants.NBT.CURRENT_SIGIL, inventorySize);
		}
	}

	public static ItemStack getItemStackInSlot(ItemStack itemStack, int slot)
	{
		if (itemStack.getItem() instanceof ItemSigilHolding)
		{
			List<ItemStack> inv = getInternalInventory(itemStack);
			if (inv != null)
				return inv.get(slot == 5 ? 4 : slot);
			else
				return ItemStack.EMPTY;
		}

		return ItemStack.EMPTY;
	}

	public static int getCurrentItemOrdinal(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemSigilHolding)
		{
			initModeTag(stack);
			int currentSigil = stack.getTag().getInt(Constants.NBT.CURRENT_SIGIL);
			currentSigil = MathHelper.clamp(currentSigil, 0, inventorySize - 1);
			return currentSigil;
		}

		return 0;
	}

	public static NonNullList<ItemStack> getInternalInventory(ItemStack stack)
	{
		initModeTag(stack);
		CompoundNBT tagCompound = stack.getTag();

		if (tagCompound == null)
		{
			return NonNullList.withSize(inventorySize, ItemStack.EMPTY);
		}

		NonNullList<ItemStack> inv = NonNullList.withSize(inventorySize, ItemStack.EMPTY);

		ItemStackHelper.loadAllItems(tagCompound, inv);

//		CompoundNBT inventoryTag = tagCompound.getCompound(Constants.NBT.ITEM_INVENTORY);
//		ListNBT tagList = inventoryTag.getList(Constants.NBT.ITEMS, 10);
//
//		if (tagList.isEmpty())
//		{
//			return NonNullList.withSize(inventorySize, ItemStack.EMPTY);
//		}
//
//		List<ItemStack> inv = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
//
//		for (int i = 0; i < tagList.tagCount(); i++)
//		{
//			CompoundNBT data = tagList.getCompoundTagAt(i);
//			byte j = data.getByte(Constants.NBT.SLOT);
//
//			if (j >= 0 && j < inv.size())
//			{
//				inv.set(j, new ItemStack(data));
//			}
//		}

		return inv;
	}

	public static void cycleToNextSigil(ItemStack itemStack, int mode)
	{
		if (itemStack.getItem() instanceof ItemSigilHolding)
		{
			initModeTag(itemStack);

			int index = mode;
			if (mode == 120 || mode == -120)
			{
				int currentIndex = getCurrentItemOrdinal(itemStack);
				ItemStack currentItemStack = getItemStackInSlot(itemStack, currentIndex);
				if (currentItemStack.isEmpty())
					return;
				if (mode < 0)
				{
					index = next(currentIndex);
					currentItemStack = getItemStackInSlot(itemStack, index);

					while (currentItemStack.isEmpty())
					{
						index = next(index);
						currentItemStack = getItemStackInSlot(itemStack, index);
					}
				} else
				{
					index = prev(currentIndex);
					currentItemStack = getItemStackInSlot(itemStack, index);

					while (currentItemStack.isEmpty())
					{
						index = prev(index);
						currentItemStack = getItemStackInSlot(itemStack, index);
					}
				}
			}

			itemStack.getTag().putInt(Constants.NBT.CURRENT_SIGIL, index);
		}
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity player)
	{
		// TODO Auto-generated method stub
		assert player.getCommandSenderWorld() != null;
		return new ContainerHolding(p_createMenu_1_, player, p_createMenu_2_, new InventoryHolding(player.getMainHandItem()));
	}

	@Override
	public ITextComponent getDisplayName()
	{
		// TODO Auto-generated method stub
		return new StringTextComponent("Sigil of Holding");
	}

}