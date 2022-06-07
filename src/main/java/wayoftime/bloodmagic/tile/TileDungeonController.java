package wayoftime.bloodmagic.tile;

import java.util.List;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.item.dungeon.IDungeonKey;
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

	public int handleRequestForRoomPlacement(ItemStack keyStack, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, List<ResourceLocation> potentialRooms)
	{
		if (!world.isRemote && world instanceof ServerWorld)
		{
			if (!keyStack.isEmpty() && keyStack.getItem() instanceof IDungeonKey)
			{
				ResourceLocation roomType = ((IDungeonKey) keyStack.getItem()).getValidResourceLocation(potentialRooms);
				if (roomType == null)
				{
					return -1;
				}
				int placementState = dungeon.addNewRoomToExistingDungeon((ServerWorld) world, this.getPos(), roomType, world.rand, activatedDoorPos, doorFacing, activatedDoorType, potentialRooms);
				if (placementState == 0)
				{
					// Consume the key!
					keyStack.shrink(1);
					LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
//					LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
					lightningboltentity.setPosition(activatedDoorPos.getX(), activatedDoorPos.getY(), activatedDoorPos.getZ());
					lightningboltentity.setEffectOnly(true);
					world.addEntity(lightningboltentity);
				}
				return placementState;
			}
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
