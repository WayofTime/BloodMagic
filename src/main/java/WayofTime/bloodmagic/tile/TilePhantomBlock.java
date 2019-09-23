package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.item.sigil.ItemSigilPhantomBridge;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.tile.base.TileTicking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class TilePhantomBlock extends TileTicking {
    private int ticksRemaining = 10;

    public TilePhantomBlock() {
    }

    public TilePhantomBlock(int ticksRemaining) {
        this.ticksRemaining = ticksRemaining;
    }

    @Override
    public void deserialize(CompoundNBT tagCompound) {
        this.ticksRemaining = tagCompound.getInt(Constants.NBT.TICKS_REMAINING);
    }

    @Override
    public CompoundNBT serialize(CompoundNBT tagCompound) {
        tagCompound.putInt(Constants.NBT.TICKS_REMAINING, ticksRemaining);
        return tagCompound;
    }

    @Override
    public void onUpdate() {
        if (!world.isRemote) {
            PlayerEntity player = world.getClosestPlayer(getPos().getX(), getPos().getY(), getPos().getZ(), 10.0D, ItemSigilPhantomBridge.IS_PHANTOM_ACTIVE);
            if (player != null && !player.isSneaking())
                return;
            ticksRemaining--;
        }


        if (ticksRemaining <= 0) {
            world.setBlockToAir(getPos());
            world.removeTileEntity(getPos());
        }

    }
}
