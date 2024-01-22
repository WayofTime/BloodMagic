package wayoftime.bloodmagic.common.tile;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tile.base.TileBase;
import wayoftime.bloodmagic.structures.DungeonSynthesizer;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class TileDungeonSeal extends TileBase
{
	public BlockPos controllerPos = BlockPos.ZERO;
	public BlockPos doorPos = BlockPos.ZERO;
	public Direction doorDirection = Direction.NORTH;
	public String doorType = "";
	public int activatedRoomDepth;
	public int highestBranchRoomDepth;

	public List<ResourceLocation> potentialRoomTypes = new ArrayList<>();

	public TileDungeonSeal(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public TileDungeonSeal(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.DUNGEON_SEAL_TYPE.get(), pos, state);
	}

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
				int state = tileController.handleRequestForRoomPlacement(player, heldStack, doorPos, doorDirection, doorType, activatedRoomDepth, highestBranchRoomDepth, potentialRoomTypes);

//				System.out.println("State is: " + state);
//				System.out.println("")
				if (state == -1)
				{
					return -1;
					// TODO: Spawn smoke particles, since the used item does not work.
				}

				if (state == 2)
				{
					if (player != null)
					{
						List<Component> toSend = Lists.newArrayList();
//						if (!binding.getOwnerId().equals(player.getGameProfile().getId()))
//							toSend.add(Component.translatable(tooltipBase + "otherNetwork", binding.getOwnerName()));
						toSend.add(Component.translatable("tooltip.bloodmagic.blockeddoor"));
						ChatUtil.sendNoSpam(player, toSend.toArray(new Component[toSend.size()]));
						level.setBlock(worldPosition, BloodMagicBlocks.DUNGEON_TILE_SPECIAL.get().defaultBlockState(), 3);
						level.playSound((Player) null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.4F + 0.8F);
					}
				}
			}
		}

		return 3;
	}

	public void acceptDoorInformation(BlockPos controllerPos, BlockPos doorPos, Direction doorDirection, String doorType, int activatedRoomDepth, int highestBranchRoomDepth, List<ResourceLocation> potentialRoomTypes)
	{
		this.controllerPos = controllerPos;
		this.doorPos = doorPos;
		this.doorDirection = doorDirection;
		this.doorType = doorType;
		this.potentialRoomTypes = potentialRoomTypes;
		this.activatedRoomDepth = activatedRoomDepth;
		this.highestBranchRoomDepth = highestBranchRoomDepth;

//		System.out.println("New block room depth info: " + activatedRoomDepth + "/" + highestBranchRoomDepth + ", pos: " + doorPos);
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
		activatedRoomDepth = tag.getInt(Constants.NBT.DEPTH);
		highestBranchRoomDepth = tag.getInt(Constants.NBT.MAX_DEPTH);
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

		tag.putInt(Constants.NBT.DEPTH, activatedRoomDepth);
		tag.putInt(Constants.NBT.MAX_DEPTH, highestBranchRoomDepth);

		return tag;
	}
}
