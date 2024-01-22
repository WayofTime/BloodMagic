package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.container.item.ContainerTrainingBracelet;
import wayoftime.bloodmagic.common.item.inventory.InventoryTrainingBracelet;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ItemLivingTrainer extends Item implements ILivingContainer, MenuProvider
{
	public static final int MAX_SIZE = 16;

	public ItemLivingTrainer()
	{
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide)
		{
			Utils.setUUID(stack);
			ILivingContainer.setDisplayIfZero(stack, true);

			if (player instanceof ServerPlayer)
			{
				NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeItemStack(stack, false));
			}
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		if (stack.getTag() != null)
		{
			LivingStats stats = getLivingStats(stack);

			if (stats == null)
			{
				return;
			}

//			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.livingtrainer.upgrade.points", stats.getUsedPoints()).mergeStyle(TextFormatting.GOLD));

			boolean isWhitelist = getIsWhitelist(stack);
			if (isWhitelist)
			{
				tooltip.add(Component.translatable("tooltip.bloodmagic.trainer.whitelist"));
			} else
			{
				tooltip.add(Component.translatable("tooltip.bloodmagic.trainer.blacklist"));
			}

			Map<LivingUpgrade, Integer> positiveUpgradeMap = new HashMap<>();
			List<LivingUpgrade> zeroUpgradeList = new ArrayList<>();

			stats.getUpgrades().forEach((k, v) -> {
				int level = k.getLevel(v.intValue());
				if (level > 0)
					positiveUpgradeMap.put(k, level);
				else
					zeroUpgradeList.add(k);
			});

			positiveUpgradeMap.forEach((k, v) -> {
				tooltip.add(Component.translatable("%s %s", Component.translatable(k.getTranslationKey()), Component.translatable("enchantment.level." + v)).withStyle(ChatFormatting.GRAY));
			});

			if (!zeroUpgradeList.isEmpty() && !isWhitelist)
			{
				tooltip.add(Component.translatable("tooltip.bloodmagic.trainer.deny"));
				for (LivingUpgrade upgrade : zeroUpgradeList)
				{
					tooltip.add(Component.translatable(upgrade.getTranslationKey()).withStyle(ChatFormatting.GRAY));
				}
			}
		}
	}

	public void setIsWhitelist(ItemStack trainerStack, boolean isWhitelist)
	{
		if (trainerStack.getTag() == null)
		{
			trainerStack.setTag(new CompoundTag());
		}

		trainerStack.getTag().putBoolean(Constants.NBT.BLACKWHITELIST, isWhitelist);
	}

	public boolean getIsWhitelist(ItemStack trainerStack)
	{
		if (trainerStack.getTag() == null)
		{
			trainerStack.setTag(new CompoundTag());
		}

		return trainerStack.getTag().getBoolean(Constants.NBT.BLACKWHITELIST);
	}

	@Override
	public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player player)
	{
		// TODO Auto-generated method stub
		assert player.getCommandSenderWorld() != null;
		return new ContainerTrainingBracelet(p_createMenu_1_, player, p_createMenu_2_, player.getMainHandItem());
	}

	@Override
	public Component getDisplayName()
	{
		// TODO Auto-generated method stub
		return Component.literal("Bracelet");
	}

	public void setTomeLevel(ItemStack trainerStack, int slot, int desiredLevel)
	{
//		System.out.println("Called!");
		InventoryTrainingBracelet inv = new InventoryTrainingBracelet(trainerStack);
		ItemStack tomeStack = inv.getItem(slot);
		if (!tomeStack.isEmpty() && tomeStack.getItem() instanceof ILivingContainer)
		{
			LivingStats tomeStats = ((ILivingContainer) tomeStack.getItem()).getLivingStats(tomeStack);
			if (tomeStats != null)
			{
				LivingStats newStats = new LivingStats();

				for (Entry<LivingUpgrade, Double> entry : tomeStats.getUpgrades().entrySet())
				{
					int maxLevel = entry.getKey().getLevel(Integer.MAX_VALUE);
					desiredLevel = Math.min(desiredLevel, maxLevel);
					double wantedExp = entry.getKey().getLevelExp(desiredLevel);
					newStats.addExperience(entry.getKey().getKey(), wantedExp);
				}

				((ILivingContainer) tomeStack.getItem()).updateLivingStats(tomeStack, newStats);

				inv.setItem(slot, tomeStack);
				inv.save();
				fromInventory(trainerStack, inv);
			} else
			{
				System.out.println("Stats are null");
			}
		} else
		{
			System.out.println("Something went wrong when setting tomeLevel");
		}
	}

	public void fromInventory(ItemStack trainerStack, InventoryTrainingBracelet inv)
	{
		List<ItemStack> invList = new ArrayList<>();
		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			invList.add(inv.getItem(i));
		}
		fromItemStackList(trainerStack, invList);
	}

	public void fromItemStackList(ItemStack trainerStack, List<ItemStack> tomeList)
	{
//		System.out.println("Saving trainer information on server: " + EffectiveSide.get().isServer());
		LivingStats stats = new LivingStats();

		int n = 0;

		tomeLoop: for (int i = 0; i < tomeList.size(); i++)
		{
			ItemStack tomeStack = tomeList.get(i);
			if (!tomeStack.isEmpty() && tomeStack.getItem() instanceof ILivingContainer)
			{
				LivingStats tomeStats = ((ILivingContainer) tomeStack.getItem()).getLivingStats(tomeStack);
				if (tomeStats != null)
				{
					for (Entry<LivingUpgrade, Double> entry : tomeStats.getUpgrades().entrySet())
					{
						stats.addExperience(entry.getKey().getKey(), entry.getValue());
						n++;
						if (n >= MAX_SIZE)
						{
							break tomeLoop;
						}
					}
				}
			}
		}

		updateLivingStats(trainerStack, stats);
	}

	public InventoryTrainingBracelet toInventory(ItemStack trainerStack)
	{
		InventoryTrainingBracelet inv = new InventoryTrainingBracelet(trainerStack);
		inv.clearContent();

		List<ItemStack> stackList = toItemStackList(trainerStack);
		for (int i = 0; i < Math.min(stackList.size(), inv.getContainerSize()); i++)
		{
			inv.setItem(i, stackList.get(i));
		}

		return inv;
	}

	public List<ItemStack> toItemStackList(ItemStack trainerStack)
	{
		List<ItemStack> tomeList = new ArrayList<>();

		LivingStats stats = getLivingStats(trainerStack);
		if (stats == null)
		{
			return tomeList;
		}

		Map<LivingUpgrade, Double> upgradeMap = stats.getUpgrades();
		for (Entry<LivingUpgrade, Double> upgrade : upgradeMap.entrySet())
		{
			ItemStack tomeStack = new ItemStack(BloodMagicItems.LIVING_TOME.get());
			LivingStats tomeStats = new LivingStats();
			tomeStats.addExperience(upgrade.getKey().getKey(), upgrade.getValue());
			((ILivingContainer) tomeStack.getItem()).updateLivingStats(tomeStack, tomeStats);
			ILivingContainer.setDisplayIfZero(tomeStack, true);
			tomeList.add(tomeStack);
		}

		return tomeList;
	}
}
