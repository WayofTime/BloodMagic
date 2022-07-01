package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import wayoftime.bloodmagic.common.block.type.SpecialSealType;
import wayoftime.bloodmagic.common.tile.TileSpecialRoomDungeonSeal;

public class BlockSpecialDungeonSeal extends BlockDungeonSeal
{
	public static final EnumProperty<SpecialSealType> SEAL = EnumProperty.create("special", SpecialSealType.class);

	public BlockSpecialDungeonSeal()
	{
		super();
		this.registerDefaultState(this.stateDefinition.any().setValue(SEAL, SpecialSealType.STANDARD));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(SEAL);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileSpecialRoomDungeonSeal(pos, state);
	}
}
