package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@RitualRegister("pump")
public class RitualPump extends Ritual {
    public static final String PUMP_RANGE = "pumpRange";

    private List<Pair<BlockPos, FluidStack>> liquidsCache;
    private Iterator<Pair<BlockPos, FluidStack>> blockPosIterator;

    public RitualPump() {
        super("ritualPump", 0, 500, "ritual." + BloodMagic.MODID + ".pumpRitual");
        addBlockRange(PUMP_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-16, -16, -16), new BlockPos(17, 17, 17)));

        setMaximumVolumeAndDistanceOfRange(PUMP_RANGE, 0, 16, 16);
        liquidsCache = Lists.newArrayList();
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
        TileEntity tileEntity = world.getTileEntity(masterRitualStone.getBlockPos().up());

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        if (tileEntity != null && tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
            IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
            IBlockState tankState = world.getBlockState(masterRitualStone.getBlockPos().up());
            int maxDrain = fluidHandler.getTankProperties()[0].getCapacity();

            if (fluidHandler.getTankProperties()[0].getContents() != null && fluidHandler.getTankProperties()[0].getContents().amount >= maxDrain)
                return;

            for (BlockPos pos : masterRitualStone.getBlockRange(PUMP_RANGE).getContainedPositions(masterRitualStone.getBlockPos())) {
                IBlockState state = world.getBlockState(pos);
                IFluidHandler blockHandler = null;
                if (state.getBlock() instanceof BlockLiquid)
                    blockHandler = new BlockLiquidWrapper((BlockLiquid) state.getBlock(), world, pos);
                else if (state.getBlock() instanceof IFluidHandler)
                    blockHandler = new FluidBlockWrapper((IFluidBlock) state.getBlock(), world, pos);

                if (blockHandler != null) {
                    FluidStack blockDrain = blockHandler.drain(maxDrain, false);
                    if (blockDrain != null && fluidHandler.fill(blockDrain, false) == blockDrain.amount) {
                        Pair<BlockPos, FluidStack> posInfo = Pair.of(pos, blockHandler.drain(maxDrain, false));
                        if (!liquidsCache.contains(posInfo))
                            liquidsCache.add(posInfo);
                    }
                }
            }

            blockPosIterator = liquidsCache.iterator();
            if (blockPosIterator.hasNext()) {
                Pair<BlockPos, FluidStack> posInfo = blockPosIterator.next();
                masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
                fluidHandler.fill(posInfo.getRight(), true);
                world.setBlockState(posInfo.getLeft(), Blocks.STONE.getDefaultState());
                world.notifyBlockUpdate(posInfo.getLeft(), Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), 3);
                world.notifyBlockUpdate(tileEntity.getPos(), tankState, tankState, 3);
                blockPosIterator.remove();
            }
        }
    }

    @Override
    public int getRefreshCost() {
        return 25;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addRune(components, 1, 0, 1, EnumRuneType.WATER);
        addRune(components, 1, 0, -1, EnumRuneType.EARTH);
        addRune(components, -1, 0, -1, EnumRuneType.AIR);
        addRune(components, -1, 0, 1, EnumRuneType.FIRE);

        addCornerRunes(components, 1, 1, EnumRuneType.DUSK);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualPump();
    }
}
