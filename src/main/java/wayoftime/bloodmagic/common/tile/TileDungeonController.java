package wayoftime.bloodmagic.common.tile;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.item.dungeon.IDungeonKey;
import wayoftime.bloodmagic.common.tile.base.TileBase;
import wayoftime.bloodmagic.structures.DungeonRoom;
import wayoftime.bloodmagic.structures.DungeonSynthesizer;
import wayoftime.bloodmagic.util.Constants;

public class TileDungeonController extends TileBase
{
	private DungeonSynthesizer dungeon = null;

	public TileDungeonController(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public TileDungeonController(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.DUNGEON_CONTROLLER_TYPE.get(), pos, state);
	}

	public void setDungeonSynthesizer(DungeonSynthesizer dungeon)
	{
		this.dungeon = dungeon;
		this.dungeon.setDungeonController(this);
		setChanged();
	}

	public int handleRequestForRoomPlacement(Player player, ItemStack keyStack, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, int activatedRoomDepth, int highestBranchRoomDepth, List<ResourceLocation> potentialRooms)
	{
		if (!level.isClientSide && level instanceof ServerLevel)
		{
			if (!keyStack.isEmpty() && keyStack.getItem() instanceof IDungeonKey)
			{
				ResourceLocation roomType = ((IDungeonKey) keyStack.getItem()).getValidResourceLocation(potentialRooms);
				if (roomType == null)
				{
					return -1;
				}
				int placementState = dungeon.addNewRoomToExistingDungeon(player, (ServerLevel) level, this.getBlockPos(), roomType, level.random, activatedDoorPos, doorFacing, activatedDoorType, potentialRooms, activatedRoomDepth, highestBranchRoomDepth);
				if (placementState == 0)
				{
					// Consume the key!
					keyStack.shrink(1);
					LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(level);
//					LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
					lightningboltentity.setPos(activatedDoorPos.getX(), activatedDoorPos.getY(), activatedDoorPos.getZ());
					lightningboltentity.setVisualOnly(true);
					level.addFreshEntity(lightningboltentity);
				}

				return placementState;
			}
		}
		return -1;
	}

	public int handleRequestForPredesignatedRoomPlacement(Player player, ItemStack keyStack, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, int activatedRoomDepth, int highestBranchRoomDepth, List<ResourceLocation> potentialRooms, DungeonRoom room, Rotation rotation, BlockPos roomLocation)
	{
		if (!level.isClientSide && level instanceof ServerLevel)
		{
			if (!keyStack.isEmpty() && keyStack.getItem() instanceof IDungeonKey)
			{
				ResourceLocation roomType = ((IDungeonKey) keyStack.getItem()).getValidResourceLocation(potentialRooms);
				if (roomType == null)
				{
					return -1;
				}
				boolean didPlace = dungeon.forcePlacementOfRoom(player, (ServerLevel) level, this.getBlockPos(), doorFacing, activatedDoorPos, activatedDoorType, activatedRoomDepth, highestBranchRoomDepth, room, rotation, roomLocation);
				if (didPlace)
				{
					// Consume the key!
					keyStack.shrink(1);
					LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(level);
//					LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
					lightningboltentity.setPos(activatedDoorPos.getX(), activatedDoorPos.getY(), activatedDoorPos.getZ());
					lightningboltentity.setVisualOnly(true);
					level.addFreshEntity(lightningboltentity);
				}
			}
		}

		return 1;
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		if (tag.contains(Constants.NBT.DUNGEON_CONTROLLER))
		{
			CompoundTag synthesizerTag = tag.getCompound(Constants.NBT.DUNGEON_CONTROLLER);
			dungeon = new DungeonSynthesizer();
			dungeon.readFromNBT(synthesizerTag);
		} else
		{
			dungeon = new DungeonSynthesizer();
		}

		dungeon.setDungeonController(this);
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);
		if (dungeon != null)
		{
			CompoundTag synthesizerTag = new CompoundTag();
			dungeon.writeToNBT(synthesizerTag);
			tag.put(Constants.NBT.DUNGEON_CONTROLLER, synthesizerTag);
		}
		return tag;
	}
}
