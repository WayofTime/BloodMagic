package WayofTime.bloodmagic.alchemyArray;

import WayofTime.bloodmagic.iface.IAlchemyArray;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AlchemyArrayEffectBounce extends AlchemyArrayEffect {
    public AlchemyArrayEffectBounce(String key) {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive) {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(IAlchemyArray array, World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity.isSneaking()) {
            entity.fallDistance = 0;
        } else if (entity.motionY < 0.0D) {
            entity.motionY = -entity.motionY;

            if (!(entity instanceof LivingEntity)) {
                entity.motionY *= 0.8D;
            }

            entity.fallDistance = 0;
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
        return new AlchemyArrayEffectBounce(key);
    }
}