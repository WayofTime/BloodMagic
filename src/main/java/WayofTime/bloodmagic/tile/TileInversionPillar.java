package WayofTime.bloodmagic.tile;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.inversion.InversionPillarHandler;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.tile.base.TileTicking;

@Getter
@Setter
public class TileInversionPillar extends TileTicking
{
    public static double willPerOperation = 2.5;
    public static double inversionPerOperation = 2;
    public static double inversionToIncreaseRadius = 100;
    public static double inversionToAddPillar = 200;
    public static double operationThreshold = 20;

    public EnumDemonWillType type;
    public double currentInversion = 0;
    public int consecutiveFailedChecks = 0; //If you fail enough checks, increase the radius.
    public int currentInfectionRadius = 1;

    public int counter = 0;

    public boolean isRegistered = false;

    public static final double maxWillForChunk = 200;

    public TileInversionPillar()
    {
        this(EnumDemonWillType.DEFAULT);
    }

    public TileInversionPillar(EnumDemonWillType type)
    {
        this.type = type;
    }

    @Override
    public void onUpdate()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        if (!isRegistered)
        {
            isRegistered = InversionPillarHandler.addPillarToMap(worldObj, getType(), getPos());
        }

        counter++;

        double currentWill = WorldDemonWillHandler.getCurrentWill(worldObj, pos, type);
        if (counter % 1 == 0)
        {
            List<BlockPos> pillarList = getNearbyPillarsExcludingThis();
            generateWillForNearbyPillars(currentWill, pillarList);
            generateInversionForNearbyPillars(currentWill, pillarList);
            int pollute = polluteNearbyBlocks(currentWill);
            if (pollute == 1)
            {
                consecutiveFailedChecks++;
            } else if (pollute == 0)
            {
                consecutiveFailedChecks = 0;
            }

            if (consecutiveFailedChecks > 5 * currentInfectionRadius && currentInversion >= inversionToIncreaseRadius)
            {
                currentInfectionRadius++;
                consecutiveFailedChecks = 0;
                currentInversion -= inversionToIncreaseRadius;
                System.out.println("Increasing radius!");
            }

            if (currentInfectionRadius >= 10 && currentInversion >= inversionToAddPillar)
            {
                //TODO: Spawn pillar
            }
        }
    }

    public void removePillarFromMap()
    {

    }

    public List<BlockPos> getNearbyPillarsExcludingThis()
    {
        return InversionPillarHandler.getNearbyPillars(worldObj, type, pos);
    }

    @Override
    public void deserialize(NBTTagCompound tag)
    {
        super.deserialize(tag);

        if (!tag.hasKey(Constants.NBT.WILL_TYPE))
        {
            type = EnumDemonWillType.DEFAULT;
        }

        type = EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE));
        currentInversion = tag.getDouble("currentInversion");
        currentInfectionRadius = tag.getInteger("currentInfectionRadius");
        consecutiveFailedChecks = tag.getInteger("consecutiveFailedChecks");
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag)
    {
        super.serialize(tag);

        tag.setString(Constants.NBT.WILL_TYPE, type.toString());
        tag.setDouble("currentInversion", currentInversion);
        tag.setInteger("currentInfectionRadius", currentInfectionRadius);
        tag.setInteger("consecutiveFailedChecks", consecutiveFailedChecks);

        return tag;
    }

    public void generateWillForNearbyPillars(double currentWillInChunk, List<BlockPos> offsetPositions)
    {
        double totalGeneratedWill = 0;
        double willFactor = currentWillInChunk / 200;

        for (BlockPos offsetPos : offsetPositions)
        {
            double distanceSquared = offsetPos.distanceSq(pos);

            totalGeneratedWill += willFactor * 343 / (343 + Math.pow(distanceSquared, 3 / 2));
        }

        if (totalGeneratedWill > 0)
        {
            WorldDemonWillHandler.fillWillToMaximum(worldObj, pos, type, totalGeneratedWill, maxWillForChunk, true); //TODO: Find out why this method doesn't work.
        }
    }

    public void generateInversionForNearbyPillars(double currentWillInChunk, List<BlockPos> offsetPositions)
    {
        double willFactor = currentWillInChunk / 400;
        double totalGeneratedInversion = willFactor;

        for (BlockPos offsetPos : offsetPositions)
        {
            double distanceSquared = offsetPos.distanceSq(pos);

            totalGeneratedInversion += 3125 / (3125 + Math.pow(distanceSquared, 5 / 2));
        }

        currentInversion = Math.max(0, currentInversion + totalGeneratedInversion);
    }

    /**
     * 
     * @param currentWillInChunk
     * @return 0 if the block is successfully placed, 1 if the block is not
     *         placed due to the selected place being invalid, 2 if the block is
     *         not placed due to there not being enough Will or Inversion
     */
    public int polluteNearbyBlocks(double currentWillInChunk)
    {
//        System.out.println("Hai! :D Current Inversion: " + currentInversion + ", Current Will: " + currentWillInChunk);
        if (currentWillInChunk < operationThreshold || currentInversion < inversionPerOperation)
        {
            return 2;
        }

        double xOff = worldObj.rand.nextGaussian() * currentInfectionRadius;
        double yOff = worldObj.rand.nextGaussian() * currentInfectionRadius;
        double zOff = worldObj.rand.nextGaussian() * currentInfectionRadius;
        double r2 = xOff * xOff + yOff * yOff + zOff * zOff;
        int maxInfectionRadius2 = (4 * currentInfectionRadius * currentInfectionRadius);
        if (r2 > maxInfectionRadius2)
        {
            double factor = Math.sqrt(maxInfectionRadius2 / r2);
            xOff *= factor;
            yOff *= factor;
            zOff *= factor;
        }

        BlockPos offsetPos = pos.add(xOff + 0.5, yOff + 0.5, zOff + 0.5);
        if (offsetPos.equals(pos))
        {
            return 1;
        }

        IBlockState state = worldObj.getBlockState(offsetPos);
        if (!state.getBlock().isAir(state, worldObj, offsetPos))
        {
            //Consume Will and set this block
//            System.out.println("I am polluting you at: " + offsetPos);
            Block block = state.getBlock();
            if (block == Blocks.DIRT || block == Blocks.STONE || block == Blocks.GRASS)
            {
                if (worldObj.setBlockState(offsetPos, ModBlocks.DEMON_EXTRAS.getStateFromMeta(0)))
                {
                    WorldDemonWillHandler.drainWill(worldObj, pos, type, willPerOperation, true);
                    currentInversion -= inversionPerOperation;

                    return 0;
                }
            }

            return 1;
        }

        return 1;
    }
}
