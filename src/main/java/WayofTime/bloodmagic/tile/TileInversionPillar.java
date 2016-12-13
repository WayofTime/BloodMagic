package WayofTime.bloodmagic.tile;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.animation.TimeValues.VariableValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.inversion.InversionPillarHandler;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.tile.base.TileTicking;

import com.google.common.collect.ImmutableMap;

@Getter
@Setter
public class TileInversionPillar extends TileTicking
{
    public static double willPerOperation = 0.5;
    public static double inversionPerOperation = 4;
    public static double addedInversionPerFailedCheck = 1;
    public static double inversionToIncreaseRadius = 100;
    public static double inversionToAddPillar = 200;
    public static double operationThreshold = 20;
    public static double inversionToSpreadWill = 200;
    public static double willPushRate = 1;
    public static double inversionCostPerWillSpread = 4;
    public static double minimumWillForChunkWhenSpreading = 100;

    private final IAnimationStateMachine asm;
    private float animationOffsetValue = 0;
    private final VariableValue animationOffset = new VariableValue(0);
    private final VariableValue cycleLength = new VariableValue(4);

    public EnumDemonWillType type;
    public double currentInversion = 0;
    public int consecutiveFailedChecks = 0; //If you fail enough checks, increase the radius.
    public int consecutiveFailedAirChecks = 0;
    public int currentInfectionRadius = 1;

//    public int dormantCounter = 0; //Time that the pillar will 

    public int counter = 0;

    public boolean isRegistered = false;

    public static final double maxWillForChunk = 1000;

    public TileInversionPillar()
    {
        this(EnumDemonWillType.DEFAULT);
    }

    public TileInversionPillar(EnumDemonWillType type)
    {
        this.type = type;
        asm = BloodMagic.proxy.load(new ResourceLocation(Constants.Mod.MODID.toLowerCase(), "asms/block/inversion_pillar.json"), ImmutableMap.<String, ITimeValue>of("offset", animationOffset, "cycle_length", cycleLength));
        animationOffsetValue = -1;
    }

    @Override
    public void onUpdate()
    {
        if (animationOffsetValue < 0)
        {
            animationOffsetValue = getWorld().getTotalWorldTime() * getWorld().rand.nextFloat();
            animationOffset.setValue(animationOffsetValue);
        }

        if (getWorld().isRemote)
        {
            return;
        }

        if (!isRegistered)
        {
            isRegistered = InversionPillarHandler.addPillarToMap(getWorld(), getType(), getPos());
        }

        counter++;

        double currentWill = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, type);
        if (counter % 1 == 0)
        {
            List<BlockPos> pillarList = getNearbyPillarsExcludingThis();
//            if (type == EnumDemonWillType.VENGEFUL)
//            {
//                System.out.println(pillarList.size() + " nearby pillars");
//            }
            generateWillForNearbyPillars(currentWill, pillarList);
            generateInversionForNearbyPillars(currentWill, pillarList);
            int pollute = polluteNearbyBlocks(currentWill);
            if (pollute == 1)
            {
                currentInversion += addedInversionPerFailedCheck;
                consecutiveFailedChecks++;
            } else if (pollute == 3)
            {
                currentInversion += addedInversionPerFailedCheck;
                consecutiveFailedAirChecks++;
            } else if (pollute == 0)
            {
                //We successfully found a block to replace!
                consecutiveFailedChecks = 0;
                consecutiveFailedAirChecks = 0;
            }

            if (consecutiveFailedAirChecks > 100)
            {
                createObstructionsInAir();
            }

            if (currentInversion >= inversionToSpreadWill)
            {
                spreadWillToSurroundingChunks();
            }

            if (consecutiveFailedChecks > 5 * currentInfectionRadius && currentInversion >= inversionToIncreaseRadius)
            {
                currentInfectionRadius++;
                consecutiveFailedChecks = 0;
                currentInversion -= inversionToIncreaseRadius;
                System.out.println("Increasing radius!");
            } else if (consecutiveFailedAirChecks > 25 * currentInfectionRadius) //Change this to require a number of "creations" with the orbs in the air.
            {
                currentInfectionRadius++;
                consecutiveFailedChecks = 0;
                currentInversion -= inversionToIncreaseRadius;
                System.out.println("Increasing radius due to being in the air!");
            }

            if (currentInfectionRadius >= 8 && currentInversion >= inversionToAddPillar)
            {
                //TODO: Improve algorithm
                List<BlockPos> allConnectedPos = InversionPillarHandler.getAllConnectedPillars(getWorld(), type, pos);
                BlockPos candidatePos = findCandidatePositionForPillar(getWorld(), type, pos, allConnectedPos, 5, 10);
                if (!candidatePos.equals(BlockPos.ORIGIN))
                {
                    currentInversion = 0;
                    IBlockState pillarState = ModBlocks.INVERSION_PILLAR.getStateFromMeta(type.ordinal());
                    IBlockState bottomState = ModBlocks.INVERSION_PILLAR_END.getStateFromMeta(type.ordinal() * 2);
                    IBlockState topState = ModBlocks.INVERSION_PILLAR_END.getStateFromMeta(type.ordinal() * 2 + 1);
                    getWorld().setBlockState(candidatePos, pillarState);
                    getWorld().setBlockState(candidatePos.down(), bottomState);
                    getWorld().setBlockState(candidatePos.up(), topState);
                }
            }
        }
    }

//    public static int getDormantTimeForConnectedPillarsOnSpawn()
//    {
//        return 0;
//    }

    public void createObstructionsInAir()
    {
        if (currentInversion > 1000)
        {
            Vec3d vec = new Vec3d(getWorld().rand.nextDouble() * 2 - 1, getWorld().rand.nextDouble() * 2 - 1, getWorld().rand.nextDouble() * 2 - 1).normalize().scale(2 * currentInfectionRadius);

            BlockPos centralPos = pos.add(vec.xCoord, vec.yCoord, vec.zCoord);

            getWorld().setBlockState(centralPos, ModBlocks.DEMON_EXTRAS.getStateFromMeta(0));
            currentInversion -= 1000;
        }
    }

    public static BlockPos findCandidatePositionForPillar(World world, EnumDemonWillType type, BlockPos pos, List<BlockPos> posList, double tooCloseDistance, double wantedAverageDistance)
    {
        int maxIterations = 100;
        int heightCheckRange = 3;

        for (int i = 0; i < maxIterations; i++)
        {
            Collections.shuffle(posList);
            BlockPos pillarPos = posList.get(0);

            Vec3d vec = new Vec3d(world.rand.nextDouble() * 2 - 1, world.rand.nextDouble() * 2 - 1, world.rand.nextDouble() * 2 - 1).normalize().scale(wantedAverageDistance);

            BlockPos centralPos = pillarPos.add(vec.xCoord, vec.yCoord, vec.zCoord);
            BlockPos testPos = null;
            candidateTest: for (int h = 0; h <= heightCheckRange; h++)
            {
                for (int sig = -1; sig <= 1; sig += (h > 0 ? 2 : 3))
                {
                    BlockPos candidatePos = centralPos.add(0, sig * h, 0);
                    if (world.isAirBlock(candidatePos) && world.isAirBlock(candidatePos.up()) && world.isAirBlock(candidatePos.down()) && !world.isAirBlock(candidatePos.down(2)))
                    {
                        testPos = candidatePos;
                        break candidateTest;
                    }
                }
            }

            if (testPos != null)
            {
                boolean isValid = true;
                for (BlockPos pillarTestPos : posList)
                {
                    if (pillarTestPos.distanceSq(testPos) <= tooCloseDistance * tooCloseDistance)
                    {
                        isValid = false;
                        break;
                    }
                }

                if (isValid)
                {
                    return testPos;
                }
            }
        }

        return BlockPos.ORIGIN;
    }

    public void spreadWillToSurroundingChunks()
    {
        double currentAmount = WorldDemonWillHandler.getCurrentWill(getWorld(), pos, type);
        if (currentAmount <= minimumWillForChunkWhenSpreading)
        {
            return;
        }

        for (EnumFacing side : EnumFacing.HORIZONTALS)
        {
            BlockPos offsetPos = pos.offset(side, 16);
            double sideAmount = WorldDemonWillHandler.getCurrentWill(getWorld(), offsetPos, type);
            if (currentAmount > sideAmount)
            {
                double drainAmount = Math.min((currentAmount - sideAmount) / 2, willPushRate);
                if (drainAmount < willPushRate / 2)
                {
                    continue;
                }

                double drain = WorldDemonWillHandler.drainWill(getWorld(), pos, type, drainAmount, true);
                drain = WorldDemonWillHandler.fillWillToMaximum(getWorld(), offsetPos, type, drain, maxWillForChunk, true);

                currentInversion -= drain * inversionCostPerWillSpread;
            }
        }
    }

    public void removePillarFromMap()
    {
        if (!getWorld().isRemote)
        {
            InversionPillarHandler.removePillarFromMap(getWorld(), type, pos);
        }
    }

    public List<BlockPos> getNearbyPillarsExcludingThis()
    {
        return InversionPillarHandler.getNearbyPillars(getWorld(), type, pos);
    }

    @Override
    public void deserialize(NBTTagCompound tag)
    {
        super.deserialize(tag);

        if (!tag.hasKey(Constants.NBT.WILL_TYPE))
        {
            type = EnumDemonWillType.DEFAULT;
        }

        type = EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ENGLISH));
        currentInversion = tag.getDouble("currentInversion");
        currentInfectionRadius = tag.getInteger("currentInfectionRadius");
        consecutiveFailedChecks = tag.getInteger("consecutiveFailedChecks");

        animationOffsetValue = tag.getFloat("animationOffset");
        animationOffset.setValue(animationOffsetValue);
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag)
    {
        super.serialize(tag);

        tag.setString(Constants.NBT.WILL_TYPE, type.toString());
        tag.setDouble("currentInversion", currentInversion);
        tag.setInteger("currentInfectionRadius", currentInfectionRadius);
        tag.setInteger("consecutiveFailedChecks", consecutiveFailedChecks);
        tag.setFloat("animationOffset", animationOffsetValue);

        return tag;
    }

    public void generateWillForNearbyPillars(double currentWillInChunk, List<BlockPos> offsetPositions)
    {
        double totalGeneratedWill = 0;
        double willFactor = currentWillInChunk / 1000;

        for (BlockPos offsetPos : offsetPositions)
        {
            double distanceSquared = offsetPos.distanceSq(pos);

            totalGeneratedWill += willFactor * 343 / (343 + Math.pow(distanceSquared, 3 / 2));
        }

        if (totalGeneratedWill > 0)
        {
            WorldDemonWillHandler.fillWillToMaximum(getWorld(), pos, type, totalGeneratedWill, maxWillForChunk, true);
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
     *         not placed due to there not being enough Will or Inversion, 3 if
     *         the block is not placed due to the selected block being air.
     */
    public int polluteNearbyBlocks(double currentWillInChunk)
    {
//        System.out.println("Hai! :D Current Inversion: " + currentInversion + ", Current Will: " + currentWillInChunk);
        if (currentWillInChunk < operationThreshold || currentInversion < inversionPerOperation)
        {
            return 2; //Not enough Will or Inversion available
        }

        for (int i = 0; i < currentInfectionRadius; i++)
        {
            double xOff = (getWorld().rand.nextBoolean() ? 1 : -1) * (getWorld().rand.nextGaussian() + 1) * (currentInfectionRadius);
            double yOff = (getWorld().rand.nextBoolean() ? 1 : -1) * (getWorld().rand.nextGaussian() + 1) * (currentInfectionRadius);
            double zOff = (getWorld().rand.nextBoolean() ? 1 : -1) * (getWorld().rand.nextGaussian() + 1) * (currentInfectionRadius);
            double r2 = xOff * xOff + yOff * yOff + zOff * zOff;
            int maxInfectionRadius2 = (9 * currentInfectionRadius * currentInfectionRadius);
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
                return 1; //Invalid block (itself!)
            }

            IBlockState state = getWorld().getBlockState(offsetPos);
            if (!state.getBlock().isAir(state, getWorld(), offsetPos))
            {
                //Consume Will and set this block
                Block block = state.getBlock();
                if (block == Blocks.DIRT || block == Blocks.STONE || block == Blocks.GRASS)
                {
                    if (getWorld().setBlockState(offsetPos, ModBlocks.DEMON_EXTRAS.getStateFromMeta(0)))
                    {
                        WorldDemonWillHandler.drainWill(getWorld(), pos, type, willPerOperation, true);
                        currentInversion -= inversionPerOperation;

                        return 0; //Successfully placed
                    }
                }

                return 1; //Invalid block
            }
        }

        return 3; //The block was air
    }

    public void handleEvents(float time, Iterable<Event> pastEvents)
    {
        for (Event event : pastEvents)
        {
            System.out.println("Event: " + event.event() + " " + event.offset() + " " + getPos() + " " + time);
        }
    }

    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing side)
    {
        if (capability == CapabilityAnimation.ANIMATION_CAPABILITY)
        {
            return true;
        }
        return super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side)
    {
        if (capability == CapabilityAnimation.ANIMATION_CAPABILITY)
        {
            return CapabilityAnimation.ANIMATION_CAPABILITY.cast(asm);
        }
        return super.getCapability(capability, side);
    }
}
