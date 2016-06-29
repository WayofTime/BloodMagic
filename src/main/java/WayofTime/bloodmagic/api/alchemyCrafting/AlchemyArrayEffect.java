package WayofTime.bloodmagic.api.alchemyCrafting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.iface.IAlchemyArray;

@RequiredArgsConstructor
public abstract class AlchemyArrayEffect
{
    @Getter
    public final String key;

    public abstract boolean update(TileEntity tile, int ticksActive);

    public abstract void writeToNBT(NBTTagCompound tag);

    public abstract void readFromNBT(NBTTagCompound tag);

    public abstract AlchemyArrayEffect getNewCopy();

    public void onEntityCollidedWithBlock(IAlchemyArray array, World world, BlockPos pos, IBlockState state, Entity entity)
    {

    }
}
