package wayoftime.bloodmagic.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.tile.contailer.ContainerAlchemicalReactionChamber;
import wayoftime.bloodmagic.util.Constants;

public class TileAlchemicalReactionChamber extends TileInventory implements ITickableTileEntity, INamedContainerProvider
{
	@ObjectHolder("bloodmagic:alchemicalreactionchamber")
	public static TileEntityType<TileAlchemicalReactionChamber> TYPE;

	public static final int ARC_TOOL_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int NUM_OUTPUTS = 5;
	public static final int INPUT_SLOT = 6;
	public static final int INPUT_BUCKET_SLOT = 7;
	public static final int OUTPUT_BUCKET_SLOT = 8;

	public FluidTank inputTank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 2);
	public FluidTank outputTank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 2);

// Input slots are from 0 to 3.

	public int burnTime = 0;

	public TileAlchemicalReactionChamber(TileEntityType<?> type)
	{
		super(type, 9, "alchemicalreactionchamber");
	}

	public TileAlchemicalReactionChamber()
	{
		this(TYPE);
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		super.deserialize(tag);

		burnTime = tag.getInt(Constants.NBT.SOUL_FORGE_BURN);
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		super.serialize(tag);

		tag.putInt(Constants.NBT.SOUL_FORGE_BURN, burnTime);
		return tag;
	}

	@Override
	public void tick()
	{

	}

//	private boolean canCraft(RecipeTartaricForge recipe)
//	{
//		if (recipe == null)
//			return false;
//
//		ItemStack currentOutputStack = getStackInSlot(outputSlot);
//		if (recipe.getOutput().isEmpty())
//			return false;
//		if (currentOutputStack.isEmpty())
//			return true;
//		if (!currentOutputStack.isItemEqual(recipe.getOutput()))
//			return false;
//		int result = currentOutputStack.getCount() + recipe.getOutput().getCount();
//		return result <= getInventoryStackLimit() && result <= currentOutputStack.getMaxStackSize();
//
//	}
//
//	public void craftItem(RecipeTartaricForge recipe)
//	{
//		if (this.canCraft(recipe))
//		{
//			ItemStack currentOutputStack = getStackInSlot(outputSlot);
//
//			List<ItemStack> inputList = new ArrayList<>();
//			for (int i = 0; i < 4; i++) if (!getStackInSlot(i).isEmpty())
//				inputList.add(getStackInSlot(i).copy());
//
//			BloodMagicCraftedEvent.SoulForge event = new BloodMagicCraftedEvent.SoulForge(recipe.getOutput().copy(), inputList.toArray(new ItemStack[0]));
//			MinecraftForge.EVENT_BUS.post(event);
//
//			if (currentOutputStack.isEmpty())
//			{
//				setInventorySlotContents(outputSlot, event.getOutput());
//			} else if (ItemHandlerHelper.canItemStacksStack(currentOutputStack, event.getOutput()))
//			{
//				currentOutputStack.grow(event.getOutput().getCount());
//			}
//
//			consumeInventory();
//		}
//	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
	{
		assert world != null;
		return new ContainerAlchemicalReactionChamber(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("Alchemical Reaction Chamber");
	}

	public double getProgressForGui()
	{
		return 0;
	}
}