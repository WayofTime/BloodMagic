package wayoftime.bloodmagic.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.alchemyarray.AlchemyArrayEffect;
import wayoftime.bloodmagic.core.registry.AlchemyArrayRegistry;
import wayoftime.bloodmagic.util.Constants;

public class TileAlchemyArray extends TileInventory
{
	public boolean isActive = false;
	public int activeCounter = 0;
	public Direction rotation = Direction.from2DDataValue(0);
	public int rotateCooldown = 0;

	private String key = "";
	public AlchemyArrayEffect arrayEffect;
	private boolean doDropIngredients = true;

	public TileAlchemyArray(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, 2, "alchemyarray", pos, state);
	}

	public TileAlchemyArray(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.ALCHEMY_ARRAY_TYPE.get(), pos, state);
	}

	public void onEntityCollidedWithBlock(BlockState state, Entity entity)
	{
		if (arrayEffect != null)
		{
			arrayEffect.onEntityCollidedWithBlock(this, getLevel(), worldPosition, state, entity);
		}
	}

	@Override
	public void deserialize(CompoundTag tagCompound)
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
		this.rotation = Direction.from2DDataValue(tagCompound.getInt(Constants.NBT.DIRECTION));

		CompoundTag arrayTag = tagCompound.getCompound("arrayTag");
//		arrayEffect = AlchemyArrayRegistry.getEffect(world, this.getStackInSlot(0), this.getStackInSlot(1));
		if (arrayEffect != null)
		{
			arrayEffect.readFromNBT(arrayTag);
		}
	}

	public void doDropIngredients(boolean drop)
	{
		this.doDropIngredients = drop;
	}

	@Override
	public CompoundTag serialize(CompoundTag tagCompound)
	{
		super.serialize(tagCompound);
		tagCompound.putBoolean("isActive", isActive);
		tagCompound.putInt("activeCounter", activeCounter);
		tagCompound.putString("stringKey", "".equals(key) ? "empty" : key.toString());
		tagCompound.putBoolean("doDropIngredients", doDropIngredients);
		tagCompound.putInt(Constants.NBT.DIRECTION, rotation.get2DDataValue());

		CompoundTag arrayTag = new CompoundTag();
		if (arrayEffect != null)
		{
			arrayEffect.writeToNBT(arrayTag);
		}
		tagCompound.put("arrayTag", arrayTag);

		return tagCompound;
	}

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
			AlchemyArrayEffect effect = AlchemyArrayRegistry.getEffect(level, this.getItem(0), this.getItem(1));
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
				this.removeItem(0, 1);
				this.removeItem(1, 1);
				this.getLevel().setBlockAndUpdate(getBlockPos(), Blocks.AIR.defaultBlockState());
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
	public boolean canPlaceItem(int slot, ItemStack itemstack)
	{
		return slot == 0 || slot == 1;
	}
}
