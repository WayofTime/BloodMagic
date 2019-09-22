package WayofTime.bloodmagic.alchemyArray;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.iface.IAlchemyArray;

public class AlchemyArrayEffectSpike extends AlchemyArrayEffect {
    public AlchemyArrayEffectSpike(String key) {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive) {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(IAlchemyArray array, World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.attackEntityFrom(DamageSource.CACTUS, 2);
        }
    }

    @Override
    public void writeToNBT(CompoundNBT tag) {

    }

    @Override
    public void readFromNBT(CompoundNBT tag) {

    }

    @Override
    public AlchemyArrayEffect getNewCopy() {
        return new AlchemyArrayEffectSpike(key);
    }
}