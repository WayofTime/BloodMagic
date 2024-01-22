package wayoftime.bloodmagic.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.ItemHandlerHelper;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWill;
import wayoftime.bloodmagic.api.compat.IDemonWillConduit;
import wayoftime.bloodmagic.api.compat.IDemonWillGem;
import wayoftime.bloodmagic.api.event.BloodMagicCraftedEvent;
import wayoftime.bloodmagic.common.container.tile.ContainerSoulForge;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class TileSoulForge extends TileInventory implements MenuProvider, IDemonWillConduit
{
//	@ObjectHolder("bloodmagic:soulforge")
//	public static BlockEntityType<TileSoulForge> TYPE;

	public static final int ticksRequired = 100;
	public static final double worldWillTransferRate = 1;

	public static final int soulSlot = 4;
	public static final int outputSlot = 5;

	// Input slots are from 0 to 3.

	public int burnTime = 0;

	public TileSoulForge(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, 6, "soulforge", pos, state);
	}

	public TileSoulForge(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.SOUL_FORGE_TYPE.get(), pos, state);
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);

		burnTime = tag.getInt(Constants.NBT.SOUL_FORGE_BURN);
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);

		tag.putInt(Constants.NBT.SOUL_FORGE_BURN, burnTime);
		return tag;
	}

	public final ContainerData TileData = new ContainerData()
	{
		@Override
		public int get(int index)
		{
			switch (index)
			{
			case 0:
				return burnTime;
			case 1:
				return ticksRequired;
			case 2:
				return 0;
			default:
				throw new IllegalArgumentException("Invalid index: " + index);
			}
		}

		@Override
		public void set(int index, int value)
		{
			throw new IllegalStateException("Cannot set values through IIntArray");
		}

		@Override
		public int getCount()
		{
			return 3;
		}
	};

	public void tick()
	{
		if (!hasSoulGemOrSoul())
		{
			burnTime = 0;
			return;
		}

		if (!getLevel().isClientSide)
		{
			for (EnumDemonWillType type : EnumDemonWillType.values())
			{
				double willInWorld = WorldDemonWillHandler.getCurrentWill(getLevel(), worldPosition, type);
				double filled = Math.min(willInWorld, worldWillTransferRate);

				if (filled > 0)
				{
					filled = this.fillDemonWill(type, filled, false);
					filled = WorldDemonWillHandler.drainWill(getLevel(), worldPosition, type, filled, false);

					if (filled > 0)
					{
						this.fillDemonWill(type, filled, true);
						WorldDemonWillHandler.drainWill(getLevel(), worldPosition, type, filled, true);
					}
				}
			}
		}

		List<ItemStack> inputList = new ArrayList<>();

		for (int i = 0; i < 4; i++) if (!getItem(i).isEmpty())
			inputList.add(getItem(i));

		RecipeTartaricForge recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getTartaricForge(level, inputList);
		if (recipe != null)
		{
			double soulsInGem = 0;
			EnumDemonWillType typeInGem = EnumDemonWillType.DEFAULT;
			for (EnumDemonWillType type : EnumDemonWillType.values())
			{
				double quantityOfType = getWill(type);
				if (quantityOfType > soulsInGem)
				{
					soulsInGem = quantityOfType;
					typeInGem = type;
				}
			}
			if (soulsInGem >= recipe.getMinimumSouls() || burnTime > 0)
			{
				if (canCraft(recipe))
				{
					burnTime++;

					if (burnTime == ticksRequired)
					{
						if (!getLevel().isClientSide)
						{
							double requiredSouls = recipe.getSoulDrain();
							if (requiredSouls > 0)
							{
								if (!getLevel().isClientSide && soulsInGem >= recipe.getMinimumSouls())
								{
									consumeSouls(typeInGem, requiredSouls);
								}
							}

							if (!getLevel().isClientSide && soulsInGem >= recipe.getMinimumSouls())
								craftItem(recipe);
						}

						burnTime = 0;
					} else if (burnTime > ticksRequired + 10)
					{
						burnTime = 0;
					}
				} else
				{
					burnTime = 0;
				}
			} else
			{
				burnTime = 0;
			}
		} else
		{
			burnTime = 0;
		}
	}

	private boolean canCraft(RecipeTartaricForge recipe)
	{
		if (recipe == null)
			return false;

		ItemStack currentOutputStack = getItem(outputSlot);
		if (recipe.getOutput().isEmpty())
			return false;
		if (currentOutputStack.isEmpty())
			return true;
		if (!ItemStack.isSameItem(currentOutputStack, recipe.getOutput()))
			return false;
		int result = currentOutputStack.getCount() + recipe.getOutput().getCount();
		return result <= getMaxStackSize() && result <= currentOutputStack.getMaxStackSize();

	}

	public void craftItem(RecipeTartaricForge recipe)
	{
		if (this.canCraft(recipe))
		{
			ItemStack currentOutputStack = getItem(outputSlot);

			List<ItemStack> inputList = new ArrayList<>();
			for (int i = 0; i < 4; i++) if (!getItem(i).isEmpty())
				inputList.add(getItem(i).copy());

			BloodMagicCraftedEvent.SoulForge event = new BloodMagicCraftedEvent.SoulForge(recipe.getOutput().copy(), inputList.toArray(new ItemStack[0]));
			MinecraftForge.EVENT_BUS.post(event);

			if (currentOutputStack.isEmpty())
			{
				setItem(outputSlot, event.getOutput());
			} else if (ItemHandlerHelper.canItemStacksStack(currentOutputStack, event.getOutput()))
			{
				currentOutputStack.grow(event.getOutput().getCount());
			}

			moveRemainingWillInConsumedInv();

			consumeInventory();
		}
	}

	@Override
	public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_)
	{
		assert level != null;
		return new ContainerSoulForge(this, TileData, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public Component getDisplayName()
	{
		return Component.literal("Hellfire Forge");
	}

	public boolean hasSoulGemOrSoul()
	{
		for (int i = 0; i <= 4; i++)
		{
			ItemStack soulStack = getItem(i);

			if (!soulStack.isEmpty())
			{
				if (soulStack.getItem() instanceof IDemonWill || soulStack.getItem() instanceof IDemonWillGem)
				{
					return true;
				}
			}
		}

		return false;
	}

	public double getProgressForGui()
	{
		return ((double) burnTime) / ticksRequired;
	}

	public double getWill(EnumDemonWillType type)
	{
		double will = 0;
		for (int i = 0; i <= 4; i++)
		{
			ItemStack soulStack = getItem(i);

			if (soulStack != null)
			{
				if (soulStack.getItem() instanceof IDemonWill && ((IDemonWill) soulStack.getItem()).getType(soulStack) == type)
				{
					IDemonWill soul = (IDemonWill) soulStack.getItem();
					will += soul.getWill(type, soulStack);
				}

				if (soulStack.getItem() instanceof IDemonWillGem)
				{
					IDemonWillGem soul = (IDemonWillGem) soulStack.getItem();
					will += soul.getWill(type, soulStack);
				}
			}
		}

		return will;
	}

	public void moveRemainingWillInConsumedInv()
	{
		ItemStack outputStack = getItem(outputSlot);
		if (outputStack != null)
		{
			if (outputStack.getItem() instanceof IDemonWillGem)
			{
				IDemonWillGem filledGem = (IDemonWillGem) outputStack.getItem();
				for (int i = 0; i < 4; i++)
				{
					ItemStack soulStack = getItem(i);
					if (soulStack != null && soulStack.getItem() instanceof IDemonWillGem)
					{
						IDemonWillGem syphonedGem = (IDemonWillGem) soulStack.getItem();
						for (EnumDemonWillType type : EnumDemonWillType.values())
						{
							// Skipped a few possibly redundant checks. Also could blow up in my face rooVV
							double willInGem = syphonedGem.getWill(type, soulStack);
							if (willInGem > 0)
							{
								filledGem.fillWill(type, outputStack, willInGem, true);
							}
						}
					}

				}
			}
		}
	}

	public double consumeSouls(EnumDemonWillType type, double requested)
	{
		double consumed = 0;

		for (int i = 0; i <= 4; i++)
		{
			ItemStack soulStack = getItem(i);
			if (soulStack != null)
			{
				if (soulStack.getItem() instanceof IDemonWill && ((IDemonWill) soulStack.getItem()).getType(soulStack) == type)
				{
					IDemonWill soul = (IDemonWill) soulStack.getItem();
					double souls = soul.drainWill(type, soulStack, requested - consumed);
					if (soul.getWill(type, soulStack) <= 0)
					{
						setItem(i, ItemStack.EMPTY);
					}
					consumed += souls;
//					return souls;
				}

				if (soulStack.getItem() instanceof IDemonWillGem)
				{
					IDemonWillGem soul = (IDemonWillGem) soulStack.getItem();
					double souls = soul.drainWill(type, soulStack, requested - consumed, true);
					consumed += souls;
				}
			}

			if (consumed >= requested)
			{
				return consumed;
			}
		}

		return consumed;
	}

	public void consumeInventory()
	{
		for (int i = 0; i < 4; i++)
		{
			ItemStack inputStack = getItem(i);
			if (!inputStack.isEmpty())
			{
				if (inputStack.getItem().hasCraftingRemainingItem(inputStack))
				{
					setItem(i, inputStack.getItem().getCraftingRemainingItem(inputStack));
					continue;
				}

				inputStack.shrink(1);
				if (inputStack.isEmpty())
				{
					setItem(i, ItemStack.EMPTY);
				}
			}
		}
	}

	@Override
	public int getWeight()
	{
		return 50;
	}

	@Override
	public double fillDemonWill(EnumDemonWillType type, double amount, boolean doFill)
	{
		if (amount <= 0)
		{
			return 0;
		}

		if (!canFill(type))
		{
			return 0;
		}

		ItemStack stack = this.getItem(soulSlot);
		if (stack.isEmpty() || !(stack.getItem() instanceof IDemonWillGem))
		{
			return 0;
		}

		IDemonWillGem willGem = (IDemonWillGem) stack.getItem();
		return willGem.fillWill(type, stack, amount, doFill);
	}

	@Override
	public double drainDemonWill(EnumDemonWillType type, double amount, boolean doDrain)
	{
		ItemStack stack = this.getItem(soulSlot);
		if (stack.isEmpty() || !(stack.getItem() instanceof IDemonWillGem))
		{
			return 0;
		}

		IDemonWillGem willGem = (IDemonWillGem) stack.getItem();

		double drained = amount;
		double current = willGem.getWill(type, stack);
		if (current < drained)
		{
			drained = current;
		}

		if (doDrain)
		{
			drained = willGem.drainWill(type, stack, drained, true);
		}

		return drained;
	}

	@Override
	public boolean canFill(EnumDemonWillType type)
	{
		return true;
	}

	@Override
	public boolean canDrain(EnumDemonWillType type)
	{
		return true;
	}

	@Override
	public double getCurrentWill(EnumDemonWillType type)
	{
		return 0;
	}
}
