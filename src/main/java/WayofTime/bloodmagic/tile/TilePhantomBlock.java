package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.tile.base.TileTicking;
import WayofTime.bloodmagic.util.Constants;
import net.minecraft.nbt.NBTTagCompound;

public class TilePhantomBlock extends TileTicking {
    private int ticksRemaining = 10;

    public TilePhantomBlock() {
    }

    public TilePhantomBlock(int ticksRemaining) {
        this.ticksRemaining = ticksRemaining;
    }

    @Override
    public void deserialize(NBTTagCompound tagCompound) {
        this.ticksRemaining = tagCompound.getInteger(Constants.NBT.TICKS_REMAINING);
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tagCompound) {
        tagCompound.setInteger(Constants.NBT.TICKS_REMAINING, ticksRemaining);
        return tagCompound;
    }

    @Override
    public void onUpdate() {
        //failed attempt at making phantom blocks not disappear when a player with active sigil is nearby.
        //works on the server side but blocks disappear on the client side
        /*EntityPlayer player = world.getClosestPlayer(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 5, false);
        if(player != null && player.getEntityData().getBoolean(Constants.NBT.SIGIL_PHANTOM))
            return;*/

        ticksRemaining--;

        if (ticksRemaining <= 0) {
            getWorld().setBlockToAir(getPos());
            getWorld().removeTileEntity(getPos());
        }
    }
}
