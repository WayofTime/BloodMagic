package WayofTime.bloodmagic.alchemyArray;

import WayofTime.bloodmagic.iface.IAlchemyArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AlchemyArrayEffect {
    public final String key;

    public AlchemyArrayEffect(String key) {
        this.key = key;
    }

    public abstract boolean update(TileEntity tile, int ticksActive);

    public abstract void writeToNBT(NBTTagCompound tag);

    public abstract void readFromNBT(NBTTagCompound tag);

    public abstract AlchemyArrayEffect getNewCopy();

    public void onEntityCollidedWithBlock(IAlchemyArray array, World world, BlockPos pos, IBlockState state, Entity entity) {

    }

    public String getKey() {
        return key;
    }
}
