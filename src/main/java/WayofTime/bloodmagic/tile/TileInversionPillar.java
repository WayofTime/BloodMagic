package WayofTime.bloodmagic.tile;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
    public static double inversionPerOperation = 5;
    public static double operationThreshold = 20;

    public EnumDemonWillType type;
    public double currentInversion = 0;
    public int consecutiveFailedChecks = 0; //If you fail enough checks, increase the radius.
    public int currentInfectionRadius = 3;

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
        if (counter % 20 == 0)
        {
            List<BlockPos> pillarList = getNearbyPillarsExcludingThis();
            generateWillForNearbyPillars(currentWill, pillarList);
        }
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
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag)
    {
        super.serialize(tag);

        tag.setString(Constants.NBT.WILL_TYPE, type.toString());

        return tag;
    }

    public void generateWillForNearbyPillars(double currentWillInChunk, List<BlockPos> offsetPositions)
    {
        double totalGeneratedWill = 0;
        double willFactor = currentWillInChunk / 100;

        for (BlockPos offsetPos : offsetPositions)
        {
            double distanceSquared = offsetPos.distanceSq(pos);

            totalGeneratedWill += willFactor * 350 / (350 + Math.pow(distanceSquared, 3 / 2));
        }

        if (totalGeneratedWill > 0)
        {
            WorldDemonWillHandler.fillWillToMaximum(worldObj, pos, type, totalGeneratedWill, maxWillForChunk, true);
        }
    }

    public void generateInversionForNearbyPillars(double currentWillInChunk, List<BlockPos> offsetPositions)
    {
        double totalGeneratedInversion = 0;
        double willFactor = currentWillInChunk / 100;

        for (BlockPos offsetPos : offsetPositions)
        {
            double distanceSquared = offsetPos.distanceSq(pos);

            totalGeneratedInversion += 3000 / (3000 + Math.pow(distanceSquared, 5 / 2)) + willFactor;
        }

        currentInversion = Math.max(0, totalGeneratedInversion);
    }

    public boolean polluteNearbyBlocks(double currentWillInChunk)
    {
        if (currentWillInChunk < operationThreshold || currentInversion < inversionPerOperation)
        {
            return false;
        }

        double xOff = MathHelper.clamp_double(worldObj.rand.nextGaussian() * currentInfectionRadius, -currentInfectionRadius, currentInfectionRadius);
        double yOff = MathHelper.clamp_double(worldObj.rand.nextGaussian() * currentInfectionRadius, -currentInfectionRadius, currentInfectionRadius);
        double zOff = MathHelper.clamp_double(worldObj.rand.nextGaussian() * currentInfectionRadius, -currentInfectionRadius, currentInfectionRadius);
        BlockPos offsetPos = pos.add(xOff + 0.5, yOff + 0.5, zOff + 0.5);
        if (offsetPos.equals(pos))
        {
            return false;
        }

        IBlockState state = worldObj.getBlockState(offsetPos);
        if (!state.getBlock().isAir(state, worldObj, offsetPos))
        {
            //Consume Will and set this block
            return worldObj.setBlockState(offsetPos, ModBlocks.demonExtras.getStateFromMeta(0));
        }

        return false;
    }
}
