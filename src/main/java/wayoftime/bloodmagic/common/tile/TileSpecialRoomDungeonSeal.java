package wayoftime.bloodmagic.common.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.structures.DungeonRoom;
import wayoftime.bloodmagic.structures.DungeonRoomRegistry;
import wayoftime.bloodmagic.structures.DungeonSynthesizer;
import wayoftime.bloodmagic.util.Constants;

public class TileSpecialRoomDungeonSeal extends TileDungeonSeal
{
	ResourceLocation chosenRoom = BloodMagic.rl("empty");
	BlockPos roomLocation = BlockPos.ZERO;
	Rotation rotation = Rotation.NONE;

	public TileSpecialRoomDungeonSeal(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public TileSpecialRoomDungeonSeal(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.SPECIAL_DUNGEON_SEAL_TYPE.get(), pos, state);
	}

	public void acceptSpecificDoorInformation(ServerLevel world, BlockPos controllerPos, ResourceLocation specialRoomType, Direction doorFacing, BlockPos activatedDoorPos, String activatedDoorType, int roomDepth, int highestBranchRoomDepth, DungeonRoom room, Rotation rotation, BlockPos roomLocation)
	{
		this.chosenRoom = room.key;
		this.roomLocation = roomLocation;
		this.rotation = rotation;
		List<ResourceLocation> roomPools = new ArrayList<>();
		roomPools.add(specialRoomType);
		this.acceptDoorInformation(controllerPos, activatedDoorPos, doorFacing, activatedDoorType, roomDepth, highestBranchRoomDepth, roomPools);
	}

	@Override
	public int requestRoomFromController(Player player, ItemStack heldStack)
	{
		if (DungeonSynthesizer.displayDetailedInformation)
			System.out.println("Potential rooms: " + potentialRoomTypes);
		if (!level.isClientSide && !potentialRoomTypes.isEmpty())
		{
			BlockEntity tile = level.getBlockEntity(controllerPos);
			if (tile instanceof TileDungeonController)
			{
				TileDungeonController tileController = (TileDungeonController) tile;
//				int state = tileController.handleRequestForRoomPlacement(heldStack, doorPos, doorDirection, doorType, activatedRoomDepth, highestBranchRoomDepth, potentialRoomTypes);
				DungeonRoom room = DungeonRoomRegistry.getDungeonRoom(chosenRoom);
				if (room == null)
				{
					System.out.println("The stored room is null!");
					return -1;
				}

				int state = tileController.handleRequestForPredesignatedRoomPlacement(player, heldStack, doorPos, doorDirection, doorType, activatedRoomDepth, highestBranchRoomDepth, potentialRoomTypes, room, rotation, roomLocation);

//				System.out.println("State is: " + state);
//				System.out.println("")
				if (state == -1)
				{
					return -1;
					// TODO: Spawn smoke particles, since the used item does not work.
				}
			}
		}

		return 3;
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);

		CompoundTag masterTag = tag.getCompound(Constants.NBT.ROOM_LOCATION);
		roomLocation = new BlockPos(masterTag.getInt(Constants.NBT.X_COORD), masterTag.getInt(Constants.NBT.Y_COORD), masterTag.getInt(Constants.NBT.Z_COORD));

		chosenRoom = new ResourceLocation(tag.getString(Constants.NBT.ROOM_NAME));

		rotation = Rotation.values()[Math.max(0, Math.min(3, tag.getInt(Constants.NBT.ROTATION)))];
//		rotation = Rotation.values()[tag.getInt(Constants.NBT.ROTATION)];

	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);

		CompoundTag roomTag = new CompoundTag();
		roomTag.putInt(Constants.NBT.X_COORD, roomLocation.getX());
		roomTag.putInt(Constants.NBT.Y_COORD, roomLocation.getY());
		roomTag.putInt(Constants.NBT.Z_COORD, roomLocation.getZ());
		tag.put(Constants.NBT.ROOM_LOCATION, roomTag);

		tag.putString(Constants.NBT.ROOM_NAME, chosenRoom.toString());

		tag.putInt(Constants.NBT.ROTATION, rotation.ordinal());

		return tag;
	}
}
