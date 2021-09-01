package wayoftime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.tile.base.TileBase;
import wayoftime.bloodmagic.util.Constants;

public class TileDungeonSeal extends TileBase
{
	@ObjectHolder("bloodmagic:dungeon_seal")
	public static TileEntityType<TileDungeonSeal> TYPE;

	public BlockPos controllerPos = BlockPos.ZERO;
	public BlockPos doorPos = BlockPos.ZERO;
	public Direction doorDirection = Direction.NORTH;

	public List<ResourceLocation> potentialRoomTypes = new ArrayList<>();

	public TileDungeonSeal(TileEntityType<?> type)
	{
		super(type);
	}

	public TileDungeonSeal()
	{
		this(TYPE);
	}

	public void acceptDoorInformation(BlockPos controllerPos, BlockPos doorPos, Direction doorDirection, List<ResourceLocation> potentialRoomTypes)
	{
		this.controllerPos = controllerPos;
		this.doorPos = doorPos;
		this.doorDirection = doorDirection;
		this.potentialRoomTypes = potentialRoomTypes;
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		CompoundNBT masterTag = tag.getCompound(Constants.NBT.DUNGEON_CONTROLLER);
		controllerPos = new BlockPos(masterTag.getInt(Constants.NBT.X_COORD), masterTag.getInt(Constants.NBT.Y_COORD), masterTag.getInt(Constants.NBT.Z_COORD));

		CompoundNBT doorTag = tag.getCompound(Constants.NBT.DUNGEON_DOOR);
		controllerPos = new BlockPos(doorTag.getInt(Constants.NBT.X_COORD), doorTag.getInt(Constants.NBT.Y_COORD), doorTag.getInt(Constants.NBT.Z_COORD));

		int dir = tag.getInt(Constants.NBT.DIRECTION);
		if (dir == 0)
		{
			doorDirection = Direction.NORTH;
		}

		doorDirection = Direction.values()[tag.getInt(Constants.NBT.DIRECTION)];

		ListNBT listnbt = tag.getList(Constants.NBT.DOOR_TYPES, 10);

		for (int i = 0; i < listnbt.size(); ++i)
		{
			CompoundNBT compoundnbt = listnbt.getCompound(i);
			String str = compoundnbt.getString(Constants.NBT.DOOR);
			potentialRoomTypes.add(new ResourceLocation(str));
		}
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		CompoundNBT masterTag = new CompoundNBT();
		masterTag.putInt(Constants.NBT.X_COORD, controllerPos.getX());
		masterTag.putInt(Constants.NBT.Y_COORD, controllerPos.getY());
		masterTag.putInt(Constants.NBT.Z_COORD, controllerPos.getZ());
		tag.put(Constants.NBT.DUNGEON_CONTROLLER, masterTag);

		CompoundNBT doorTag = new CompoundNBT();
		doorTag.putInt(Constants.NBT.X_COORD, controllerPos.getX());
		doorTag.putInt(Constants.NBT.Y_COORD, controllerPos.getY());
		doorTag.putInt(Constants.NBT.Z_COORD, controllerPos.getZ());
		tag.put(Constants.NBT.DUNGEON_DOOR, masterTag);

		tag.putInt(Constants.NBT.DIRECTION, doorDirection.getIndex());

		ListNBT listnbt = new ListNBT();
		for (int i = 0; i < potentialRoomTypes.size(); ++i)
		{
			String str = potentialRoomTypes.get(i).toString();
			CompoundNBT compoundnbt = new CompoundNBT();
			compoundnbt.putString(Constants.NBT.DOOR, str);
			listnbt.add(compoundnbt);
		}

		if (!listnbt.isEmpty())
		{
			tag.put(Constants.NBT.DOOR_TYPES, listnbt);
		}

		return tag;
	}
}
