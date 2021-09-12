package wayoftime.bloodmagic.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.inventory.ContainerTrainingBracelet;
import wayoftime.bloodmagic.common.item.inventory.InventoryTrainingBracelet;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.util.Utils;

public class ItemLivingTrainer extends Item implements ILivingContainer, INamedContainerProvider
{
	public static final int MAX_SIZE = 9;

	public ItemLivingTrainer()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

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
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		ILivingContainer.appendLivingTooltip(stack, getLivingStats(stack), tooltip, false);
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity player)
	{
		// TODO Auto-generated method stub
		assert player.getEntityWorld() != null;
		return new ContainerTrainingBracelet(p_createMenu_1_, player, p_createMenu_2_, player.getHeldItemMainhand());
	}

	@Override
	public ITextComponent getDisplayName()
	{
		// TODO Auto-generated method stub
		return new StringTextComponent("Bracelet");
	}

	public void setTomeLevel(ItemStack trainerStack, int slot, int desiredLevel)
	{
		InventoryTrainingBracelet inv = new InventoryTrainingBracelet(trainerStack);
		ItemStack tomeStack = inv.getStackInSlot(slot);
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

				inv.setInventorySlotContents(slot, tomeStack);
			}
		}
	}

	public void fromItemStackList(ItemStack trainerStack, List<ItemStack> tomeList)
	{
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
		inv.clear();

		List<ItemStack> stackList = toItemStackList(trainerStack);
		for (int i = 0; i < Math.min(stackList.size(), inv.getSizeInventory()); i++)
		{
			inv.setInventorySlotContents(i, stackList.get(i));
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
