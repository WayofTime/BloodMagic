package wayoftime.bloodmagic.client.model;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.tile.TileMimic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

public class MimicColor implements BlockColor
{
	@Override
	public int getColor(BlockState blockState, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tint)
	{
		if (world == null)
		{
			return -1;
		}

		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TileMimic)
		{
			TileMimic fancy = (TileMimic) te;
			BlockState mimic = fancy.getMimic();
			if (mimic != null)
			{
				return Minecraft.getInstance().getBlockColors().getColor(mimic, world, pos, tint);
			}
		}

		return -1;
	}

}
