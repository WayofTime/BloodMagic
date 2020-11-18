package wayoftime.bloodmagic.client.model;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import wayoftime.bloodmagic.tile.TileMimic;

public class MimicColor implements IBlockColor
{
	@Override
	public int getColor(BlockState blockState, @Nullable IBlockDisplayReader world, @Nullable BlockPos pos, int tint)
	{
		TileEntity te = world.getTileEntity(pos);
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
