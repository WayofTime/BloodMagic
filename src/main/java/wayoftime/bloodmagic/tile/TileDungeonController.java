package wayoftime.bloodmagic.tile;

import java.util.List;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.item.dungeon.IDungeonKey;
import wayoftime.bloodmagic.structures.DungeonSynthesizer;
import wayoftime.bloodmagic.tile.base.TileBase;
import wayoftime.bloodmagic.util.Constants;

public class TileDungeonController extends TileBase
{
	@ObjectHolder("bloodmagic:dungeon_controller")
	public static BlockEntityType<TileDungeonController> TYPE;

	private DungeonSynthesizer dungeon = null;

	public TileDungeonController(BlockEntityType<?> type)
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

	public int handleRequestForRoomPlacement(ItemStack keyStack, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, List<ResourceLocation> potentialRooms)
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
				int placementState = dungeon.addNewRoomToExistingDungeon((ServerLevel) level, this.getBlockPos(), roomType, level.random, activatedDoorPos, doorFacing, activatedDoorType, potentialRooms);
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

	@Override
	public void deserialize(CompoundTag tag)
	{
		if (tag.contains(Constants.NBT.DUNGEON_CONTROLLER))
		{
			CompoundTag synthesizerTag = tag.getCompound(Constants.NBT.DUNGEON_CONTROLLER);
			dungeon = new DungeonSynthesizer();
			dungeon.readFromNBT(synthesizerTag);
		}
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		if (dungeon != null)
		{
			CompoundTag synthesizerTag = new CompoundTag();
			dungeon.writeToNBT(synthesizerTag);
			tag.put(Constants.NBT.DUNGEON_CONTROLLER, synthesizerTag);
		}
		return tag;
	}
}
