package wayoftime.bloodmagic.tile;

import java.util.List;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.structures.DungeonSynthesizer;
import wayoftime.bloodmagic.tile.base.TileBase;
import wayoftime.bloodmagic.util.Constants;

public class TileDungeonController extends TileBase
{
	@ObjectHolder("bloodmagic:dungeon_controller")
	public static TileEntityType<TileDungeonController> TYPE;

	private DungeonSynthesizer dungeon = null;

	public TileDungeonController(TileEntityType<?> type)
	{
		super(type);
	}

	public TileDungeonController()
	{
		this(TYPE);
	}

	public void setDungeonSynthesizer(DungeonSynthesizer dungeon)
	{
		this.dungeon = dungeon;
	}

	public int handleRequestForRoomPlacement(BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, List<ResourceLocation> potentialRooms)
	{
		if (!world.isRemote && world instanceof ServerWorld)
		{
			ResourceLocation roomType = potentialRooms.get(0);
			int placementState = dungeon.addNewRoomToExistingDungeon((ServerWorld) world, this.getPos(), roomType, world.rand, activatedDoorPos, doorFacing, activatedDoorType, potentialRooms);
			return placementState;
		}
		return -1;
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		if (tag.contains(Constants.NBT.DUNGEON_CONTROLLER))
		{
			CompoundNBT synthesizerTag = tag.getCompound(Constants.NBT.DUNGEON_CONTROLLER);
			dungeon = new DungeonSynthesizer();
			dungeon.readFromNBT(synthesizerTag);
		}
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		if (dungeon != null)
		{
			CompoundNBT synthesizerTag = new CompoundNBT();
			dungeon.writeToNBT(synthesizerTag);
			tag.put(Constants.NBT.DUNGEON_CONTROLLER, synthesizerTag);
		}
		return tag;
	}
}
