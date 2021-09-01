package wayoftime.bloodmagic.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.tile.base.TileBase;

public class TileDungeonController extends TileBase
{
	@ObjectHolder("bloodmagic:dungeon_controller")
	public static TileEntityType<TileDungeonController> TYPE;

	public TileDungeonController(TileEntityType<?> type)
	{
		super(type);
	}

	public TileDungeonController()
	{
		this(TYPE);
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{

	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{

		return tag;
	}
}
