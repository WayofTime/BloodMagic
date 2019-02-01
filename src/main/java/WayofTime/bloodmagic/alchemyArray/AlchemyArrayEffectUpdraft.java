package WayofTime.bloodmagic.alchemyArray;

import WayofTime.bloodmagic.iface.IAlchemyArray;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AlchemyArrayEffectUpdraft extends AlchemyArrayEffect {
    public AlchemyArrayEffectUpdraft(String key) {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive) {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(IAlchemyArray array, World world, BlockPos pos, IBlockState state, Entity entity) {
        double motionY = 1;
        double motionYGlowstoneMod = 0.1;
        double motionYFeatherMod = 0.05;

        entity.fallDistance = 0;
        TileAlchemyArray tileArray = (TileAlchemyArray) array;
        motionY += motionYGlowstoneMod * (tileArray.getStackInSlot(0).getCount() - 1); // Glowstone Dust
        motionY += motionYFeatherMod * (tileArray.getStackInSlot(1).getCount() - 1); // Feathers

        entity.motionY = motionY;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public AlchemyArrayEffect getNewCopy() {
        return new AlchemyArrayEffectUpdraft(key);
    }
}