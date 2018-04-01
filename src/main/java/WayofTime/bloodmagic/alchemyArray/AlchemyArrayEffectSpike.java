package WayofTime.bloodmagic.alchemyArray;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.iface.IAlchemyArray;

public class AlchemyArrayEffectSpike extends AlchemyArrayEffect
{
    public AlchemyArrayEffectSpike(String key)
    {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive)
    {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(IAlchemyArray array, World world, BlockPos pos, IBlockState state, Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            entity.attackEntityFrom(DamageSource.CACTUS, 2);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {

    }

    @Override
    public AlchemyArrayEffect getNewCopy()
    {
        return new AlchemyArrayEffectSpike(key);
    }
}