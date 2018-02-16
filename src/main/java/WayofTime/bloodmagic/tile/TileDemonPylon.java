package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.IDemonWillConduit;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.tile.base.TileTicking;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileDemonPylon extends TileTicking implements IDemonWillConduit {
    public final int maxWill = 100;
    public final double drainRate = 1;
    public DemonWillHolder holder = new DemonWillHolder();

    public TileDemonPylon() {

    }

    @Override
    public void onUpdate() {
        if (getWorld().isRemote) {
            return;
        }

        for (EnumDemonWillType type : EnumDemonWillType.values()) {
            double currentAmount = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, type);

            for (EnumFacing side : EnumFacing.HORIZONTALS) {
                BlockPos offsetPos = pos.offset(side, 16);
                double sideAmount = WorldDemonWillHandler.getCurrentWill(getWorld(), offsetPos, type);
                if (sideAmount > currentAmount) {
                    double drainAmount = Math.min((sideAmount - currentAmount) / 2, drainRate);
                    double drain = WorldDemonWillHandler.drainWill(getWorld(), offsetPos, type, drainAmount, true);
                    WorldDemonWillHandler.fillWill(getWorld(), pos, type, drain, true);
                }
            }
        }
    }

    @Override
    public void deserialize(NBTTagCompound tag) {
        holder.readFromNBT(tag, "Will");
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag) {
        holder.writeToNBT(tag, "Will");
        return tag;
    }

    // IDemonWillConduit

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public double fillDemonWill(EnumDemonWillType type, double amount, boolean doFill) {
        if (amount <= 0) {
            return 0;
        }

        if (!canFill(type)) {
            return 0;
        }

        if (!doFill) {
            return Math.min(maxWill - holder.getWill(type), amount);
        }

        return holder.addWill(type, amount, maxWill);
    }

    @Override
    public double drainDemonWill(EnumDemonWillType type, double amount, boolean doDrain) {
        double drained = amount;
        double current = holder.getWill(type);
        if (current < drained) {
            drained = current;
        }

        if (doDrain) {
            return holder.drainWill(type, amount);
        }

        return drained;
    }

    @Override
    public boolean canFill(EnumDemonWillType type) {
        return true;
    }

    @Override
    public boolean canDrain(EnumDemonWillType type) {
        return true;
    }

    @Override
    public double getCurrentWill(EnumDemonWillType type) {
        return holder.getWill(type);
    }
}