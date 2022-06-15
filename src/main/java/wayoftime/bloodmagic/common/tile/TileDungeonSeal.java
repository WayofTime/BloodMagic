package wayoftime.bloodmagic.common.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.base.TileBase;
import wayoftime.bloodmagic.structures.DungeonSynthesizer;
import wayoftime.bloodmagic.util.Constants;

public class TileDungeonSeal extends TileBase
{
	public BlockPos controllerPos = BlockPos.ZERO;
	public BlockPos doorPos = BlockPos.ZERO;
	public Direction doorDirection = Direction.NORTH;
	public String doorType = "";

	public List<ResourceLocation> potentialRoomTypes = new ArrayList<>();

	public TileDungeonSeal(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public TileDungeonSeal(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.DUNGEON_SEAL_TYPE.get(), pos, state);
	}

	public int requestRoomFromController(ItemStack heldStack)
	{
		if (DungeonSynthesizer.displayDetailedInformation)
			System.out.println("Potential rooms: " + potentialRoomTypes);
		if (!level.isClientSide && !potentialRoomTypes.isEmpty())
		{
			BlockEntity tile = level.getBlockEntity(controllerPos);
			if (tile instanceof TileDungeonController)
			{
				TileDungeonController tileController = (TileDungeonController) tile;
				int state = tileController.handleRequestForRoomPlacement(heldStack, doorPos, doorDirection, doorType, potentialRoomTypes);

//				System.out.println("State is: " + state);
//				System.out.println("")
				if (state == -1)
				{
					// TODO: Spawn smoke particles, since the used item does not work.
				}
			}
		}

		return 3;
	}

	public void acceptDoorInformation(BlockPos controllerPos, BlockPos doorPos, Direction doorDirection, String doorType, List<ResourceLocation> potentialRoomTypes)
	{
		this.controllerPos = controllerPos;
		this.doorPos = doorPos;
		this.doorDirection = doorDirection;
		this.doorType = doorType;
		this.potentialRoomTypes = potentialRoomTypes;
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		CompoundTag masterTag = tag.getCompound(Constants.NBT.DUNGEON_CONTROLLER);
		controllerPos = new BlockPos(masterTag.getInt(Constants.NBT.X_COORD), masterTag.getInt(Constants.NBT.Y_COORD), masterTag.getInt(Constants.NBT.Z_COORD));

		CompoundTag doorTag = tag.getCompound(Constants.NBT.DUNGEON_DOOR);
		doorPos = new BlockPos(doorTag.getInt(Constants.NBT.X_COORD), doorTag.getInt(Constants.NBT.Y_COORD), doorTag.getInt(Constants.NBT.Z_COORD));

		int dir = tag.getInt(Constants.NBT.DIRECTION);
		if (dir == 0)
		{
			doorDirection = Direction.NORTH;
		}

		doorDirection = Direction.values()[tag.getInt(Constants.NBT.DIRECTION)];

		ListTag listnbt = tag.getList(Constants.NBT.DOOR_TYPES, 10);

		for (int i = 0; i < listnbt.size(); ++i)
		{
			CompoundTag compoundnbt = listnbt.getCompound(i);
			String str = compoundnbt.getString(Constants.NBT.DOOR);
			potentialRoomTypes.add(new ResourceLocation(str));
		}

		this.doorType = tag.getString(Constants.NBT.TYPE);
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		CompoundTag masterTag = new CompoundTag();
		masterTag.putInt(Constants.NBT.X_COORD, controllerPos.getX());
		masterTag.putInt(Constants.NBT.Y_COORD, controllerPos.getY());
		masterTag.putInt(Constants.NBT.Z_COORD, controllerPos.getZ());
		tag.put(Constants.NBT.DUNGEON_CONTROLLER, masterTag);

		CompoundTag doorTag = new CompoundTag();
		doorTag.putInt(Constants.NBT.X_COORD, doorPos.getX());
		doorTag.putInt(Constants.NBT.Y_COORD, doorPos.getY());
		doorTag.putInt(Constants.NBT.Z_COORD, doorPos.getZ());
		tag.put(Constants.NBT.DUNGEON_DOOR, doorTag);

		tag.putInt(Constants.NBT.DIRECTION, doorDirection.get3DDataValue());

		ListTag listnbt = new ListTag();
		for (int i = 0; i < potentialRoomTypes.size(); ++i)
		{
			String str = potentialRoomTypes.get(i).toString();
			CompoundTag compoundnbt = new CompoundTag();
			compoundnbt.putString(Constants.NBT.DOOR, str);
			listnbt.add(compoundnbt);
		}

		if (!listnbt.isEmpty())
		{
			tag.put(Constants.NBT.DOOR_TYPES, listnbt);
		}

		tag.putString(Constants.NBT.TYPE, doorType);

		return tag;
	}
}
