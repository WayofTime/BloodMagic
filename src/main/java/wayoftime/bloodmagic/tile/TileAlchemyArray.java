package wayoftime.bloodmagic.tile;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.alchemyarray.AlchemyArrayEffect;
import wayoftime.bloodmagic.core.registry.AlchemyArrayRegistry;
import wayoftime.bloodmagic.util.Constants;

public class TileAlchemyArray extends TileInventory implements ITickableTileEntity
{
	@ObjectHolder("bloodmagic:alchemyarray")
	public static TileEntityType<TileAlchemyArray> TYPE;

	public boolean isActive = false;
	public int activeCounter = 0;
	public Direction rotation = Direction.byHorizontalIndex(0);
	public int rotateCooldown = 0;

	private String key = "";
	public AlchemyArrayEffect arrayEffect;
	private boolean doDropIngredients = true;

	public TileAlchemyArray(TileEntityType<?> type)
	{
		super(type, 2, "alchemyarray");
//		this.bloodAltar = new BloodAltar(this);
	}

	public TileAlchemyArray()
	{
		this(TYPE);
	}

	@Override
	public void deserialize(CompoundNBT tagCompound)
	{
		super.deserialize(tagCompound);
		this.isActive = tagCompound.getBoolean("isActive");
		this.activeCounter = tagCompound.getInt("activeCounter");
		this.key = tagCompound.getString("stringKey");
		if (!tagCompound.contains("doDropIngredients")) // Check if the array is old
		{
			this.doDropIngredients = true;
		} else
		{
			this.doDropIngredients = tagCompound.getBoolean("doDropIngredients");
		}
		this.rotation = Direction.byHorizontalIndex(tagCompound.getInt(Constants.NBT.DIRECTION));

		CompoundNBT arrayTag = tagCompound.getCompound("arrayTag");
//		arrayEffect = AlchemyArrayRegistry.getEffect(world, this.getStackInSlot(0), this.getStackInSlot(1));
		if (arrayEffect != null)
		{
			arrayEffect.readFromNBT(arrayTag);
		}
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tagCompound)
	{
		super.serialize(tagCompound);
		tagCompound.putBoolean("isActive", isActive);
		tagCompound.putInt("activeCounter", activeCounter);
		tagCompound.putString("stringKey", "".equals(key) ? "empty" : key.toString());
		tagCompound.putBoolean("doDropIngredients", doDropIngredients);
		tagCompound.putInt(Constants.NBT.DIRECTION, rotation.getHorizontalIndex());

		CompoundNBT arrayTag = new CompoundNBT();
		if (arrayEffect != null)
		{
			arrayEffect.writeToNBT(arrayTag);
		}
		tagCompound.put("arrayTag", arrayTag);

		return tagCompound;
	}

	@Override
	public void tick()
	{
//		System.out.println("Active counter: " + this.activeCounter);
		if (isActive && attemptCraft())
		{
			activeCounter++;
		} else
		{
			isActive = false;
			doDropIngredients = true;
			activeCounter = 0;
			arrayEffect = null;
			key = "empty";
		}
		if (rotateCooldown > 0)
			rotateCooldown--;
	}

	public boolean attemptCraft()
	{
		if (arrayEffect != null)
		{
			isActive = true;

		} else
		{
			AlchemyArrayEffect effect = AlchemyArrayRegistry.getEffect(world, this.getStackInSlot(0), this.getStackInSlot(1));
			if (effect == null)
			{
//				key = effect.i
				return false;
			} else
			{
				arrayEffect = effect;
			}
		}

		if (arrayEffect != null)
		{
			isActive = true;
			if (arrayEffect.update(this, this.activeCounter))
			{
				this.decrStackSize(0, 1);
				this.decrStackSize(1, 1);
				this.getWorld().setBlockState(getPos(), Blocks.AIR.getDefaultState());
			}

			return true;
		}
		return false;
	}

//	@Override
	public Direction getRotation()
	{
		return rotation;
	}

	public void setRotation(Direction rotation)
	{
		this.rotation = rotation;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return slot == 0 || slot == 1;
	}
}
