package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.api.compat.IAltarReader;
import wayoftime.bloodmagic.client.key.IKeybindable;
import wayoftime.bloodmagic.client.key.KeyBindings;
import wayoftime.bloodmagic.common.container.item.ContainerHolding;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.common.item.inventory.InventoryHolding;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSigilHolding extends ItemSigilBase implements IKeybindable, IAltarReader, ISigil.Holding, MenuProvider
{
	public static final int inventorySize = 5;

	public ItemSigilHolding()
	{
		super("holding");
	}

	@Override
	public void onKeyPressed(ItemStack stack, Player player, KeyBindings key, boolean showInChat)
	{
		if (stack == player.getMainHandItem() && stack.getItem() instanceof ItemSigilHolding && key.equals(KeyBindings.OPEN_HOLDING))
		{
			Utils.setUUID(stack);

			if (player instanceof ServerPlayer)
			{
				NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeItemStack(stack, false));
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
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		super.appendHoverText(stack, world, tooltip, flag);
		tooltip.add(Component.translatable("tooltip.bloodmagic.sigil.holding.press", Component.translatable(KeyBindings.OPEN_HOLDING.getKey().saveString()).withStyle(ChatFormatting.ITALIC)).withStyle(ChatFormatting.GRAY));

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
					tooltip.add(Component.translatable("tooltip.bloodmagic.sigil.holding.sigilInSlot", i + 1, (invStack.getHoverName().plainCopy()).withStyle(ChatFormatting.ITALIC, ChatFormatting.UNDERLINE)));
//					tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.sigil.holding.sigilInSlot", i + 1, new TranslationTextComponent(invStack.getDisplayName()).mergeStyle(TextFormatting.ITALIC, TextFormatting.UNDERLINE)));

				} else
					tooltip.add(Component.translatable("tooltip.bloodmagic.sigil.holding.sigilInSlot", i + 1, invStack.getHoverName()));
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
//		BlockPos pos = context.getPos();
//		Direction facing = context.getFace();
//		pos = pos.offset(facing);
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		ItemStack stack = player.getItemInHand(hand);

//		ItemStack stack = player.getHeldItem(hand);
		if (PlayerHelper.isFakePlayer(player))
			return InteractionResult.FAIL;

		int currentSlot = getCurrentItemOrdinal(stack);
		NonNullList<ItemStack> inv = getInternalInventory(stack);
		ItemStack itemUsing = inv.get(currentSlot);

		if (itemUsing.isEmpty() || ((IBindable) itemUsing.getItem()).getBinding(itemUsing) == null)
			return InteractionResult.PASS;

		InteractionResult result = itemUsing.getItem().useOn(context);
		saveInventory(stack, inv);

		return result;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (PlayerHelper.isFakePlayer(player))
			return InteractionResultHolder.fail(stack);

		int currentSlot = getCurrentItemOrdinal(stack);
		NonNullList<ItemStack> inv = getInternalInventory(stack);
		ItemStack itemUsing = inv.get(currentSlot);

		if (itemUsing.isEmpty() || ((IBindable) itemUsing.getItem()).getBinding(itemUsing) == null)
			return InteractionResultHolder.pass(stack);

		itemUsing.getItem().use(world, player, hand);

		saveInventory(stack, inv);

		return InteractionResultHolder.pass(stack);
	}

	@Nonnull
	@Override
	public ItemStack getHeldItem(ItemStack holdingStack, @Nullable Player player)
	{
		return getInternalInventory(holdingStack).get(getCurrentItemOrdinal(holdingStack));
	}

	public void saveInventory(ItemStack itemStack, NonNullList<ItemStack> inventory)
	{
		CompoundTag itemTag = itemStack.getTag();

		if (itemTag == null)
			itemStack.setTag(itemTag = new CompoundTag());

		ContainerHelper.saveAllItems(itemTag, inventory);

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
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (stack.hasTag())
			tickInternalInventory(stack, world, entity, itemSlot, isSelected);
	}

	public void tickInternalInventory(ItemStack itemStack, Level world, Entity entity, int itemSlot, boolean isSelected)
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
			currentSigil = Mth.clamp(currentSigil, 0, inventorySize - 1);
			return currentSigil;
		}

		return 0;
	}

	public static NonNullList<ItemStack> getInternalInventory(ItemStack stack)
	{
		initModeTag(stack);
		CompoundTag tagCompound = stack.getTag();

		if (tagCompound == null)
		{
			return NonNullList.withSize(inventorySize, ItemStack.EMPTY);
		}

		NonNullList<ItemStack> inv = NonNullList.withSize(inventorySize, ItemStack.EMPTY);

		ContainerHelper.loadAllItems(tagCompound, inv);

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
	public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player player)
	{
		// TODO Auto-generated method stub
		assert player.getCommandSenderWorld() != null;
		return new ContainerHolding(p_createMenu_1_, player, p_createMenu_2_, new InventoryHolding(player.getMainHandItem()));
	}

	@Override
	public Component getDisplayName()
	{
		// TODO Auto-generated method stub
		return Component.literal("Sigil of Holding");
	}

}