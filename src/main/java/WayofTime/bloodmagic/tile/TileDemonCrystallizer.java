package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWillConduit;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.tile.base.TileTicking;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileDemonCrystallizer extends TileTicking implements IDemonWillConduit
{
    //The whole purpose of this block is to grow a crystal initially. The acceleration and crystal growing is up to the crystal itself afterwards.
    public DemonWillHolder holder = new DemonWillHolder();
    public static final int maxWill = 100;
    public static final double drainRate = 1;

    public static final double willToFormCrystal = 99;
    public static final double totalFormationTime = 1000;
    public double internalCounter = 0;

    public TileDemonCrystallizer()
    {

    }

    @Override
    public void onUpdate()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        BlockPos offsetPos = pos.offset(EnumFacing.UP);
        if (worldObj.isAirBlock(offsetPos)) //Room for a crystal to grow
        {
            EnumDemonWillType highestType = WorldDemonWillHandler.getHighestDemonWillType(worldObj, pos);
            double amount = WorldDemonWillHandler.getCurrentWill(worldObj, pos, highestType);
            if (amount >= willToFormCrystal)
            {
                internalCounter += getCrystalFormationRate(amount);
                if (internalCounter >= totalFormationTime)
                {
                    if (WorldDemonWillHandler.drainWill(worldObj, getPos(), highestType, willToFormCrystal, false) >= willToFormCrystal)
                    {
                        if (highestType == EnumDemonWillType.DEFAULT && formRandomSpecialCrystal(offsetPos) || formCrystal(highestType, offsetPos))
                        {
                            WorldDemonWillHandler.drainWill(worldObj, getPos(), highestType, willToFormCrystal, true);
                            internalCounter = 0;
                        }
                    }
                }
            }
        }
    }

    public boolean formCrystal(EnumDemonWillType type, BlockPos position)
    {
        worldObj.setBlockState(position, ModBlocks.DEMON_CRYSTAL.getStateFromMeta(type.ordinal()));
        TileEntity tile = worldObj.getTileEntity(position);
        if (tile instanceof TileDemonCrystal)
        {
            ((TileDemonCrystal) tile).setPlacement(EnumFacing.UP);
            return true;
        }

        return false;
    }

    public boolean formRandomSpecialCrystal(BlockPos position)
    {
        if (worldObj.rand.nextDouble() > 0.1)
        {
            return formCrystal(EnumDemonWillType.DEFAULT, position);
        }
        EnumDemonWillType crystalType = EnumDemonWillType.values()[worldObj.rand.nextInt(EnumDemonWillType.values().length - 1) + 1];
        return formCrystal(crystalType, position);
    }

    public double getCrystalFormationRate(double currentWill)
    {
        return 1;
    }

    @Override
    public void deserialize(NBTTagCompound tag)
    {
        holder.readFromNBT(tag, "Will");
        internalCounter = tag.getDouble("internalCounter");
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag)
    {
        holder.writeToNBT(tag, "Will");
        tag.setDouble("internalCounter", internalCounter);
        return tag;
    }

    // IDemonWillConduit

    @Override
    public int getWeight()
    {
        return 10;
    }

    @Override
    public double fillDemonWill(EnumDemonWillType type, double amount, boolean doFill)
    {
        if (amount <= 0)
        {
            return 0;
        }

        if (!canFill(type))
        {
            return 0;
        }

        if (!doFill)
        {
            return Math.min(maxWill - holder.getWill(type), amount);
        }

        return holder.addWill(type, amount, maxWill);
    }

    @Override
    public double drainDemonWill(EnumDemonWillType type, double amount, boolean doDrain)
    {
        double drained = amount;
        double current = holder.getWill(type);
        if (current < drained)
        {
            drained = current;
        }

        if (doDrain)
        {
            return holder.drainWill(type, amount);
        }

        return drained;
    }

    @Override
    public boolean canFill(EnumDemonWillType type)
    {
        return true;
    }

    @Override
    public boolean canDrain(EnumDemonWillType type)
    {
        return true;
    }

    @Override
    public double getCurrentWill(EnumDemonWillType type)
    {
        return holder.getWill(type);
    }
}