package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class RitualPump extends Ritual
{
    public static final String PUMP_RANGE = "pumpRange";

    private ArrayList<BlockPos> liquidsCache;
    private Iterator<BlockPos> blockPosIterator;

    private boolean cached = false;
    private BlockPos currentPos;

    public RitualPump()
    {
        super("ritualPump", 0, 500, "ritual." + Constants.Mod.MODID + ".pumpRitual");
        addBlockRange(PUMP_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-16, -16, -16), new BlockPos(17, 17, 17)));

        liquidsCache = new ArrayList<BlockPos>();
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();
        TileEntity tileEntity = world.getTileEntity(masterRitualStone.getBlockPos().up());

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        if (tileEntity != null && tileEntity instanceof IFluidHandler)
        {
            IFluidHandler fluidHandler = (IFluidHandler) tileEntity;
            if (!cached || liquidsCache.isEmpty())
            {
                if (fluidHandler.drain(EnumFacing.DOWN, 1000, false) != null)
                {
                    FluidStack fluidStack = fluidHandler.drain(EnumFacing.DOWN, 1000, false);
                    for (BlockPos blockPos : getBlockRange(PUMP_RANGE).getContainedPositions(masterRitualStone.getBlockPos()))
                    {
                        if (!liquidsCache.contains(blockPos))
                        {
                            if (!world.isAirBlock(blockPos) && world.getBlockState(blockPos).getBlock() == fluidStack.getFluid().getBlock() && world.getBlockState(blockPos).getValue(BlockLiquid.LEVEL) == 0)
                            {
                                liquidsCache.add(blockPos);
                            }
                        }
                    }
                }

                cached = true;
                blockPosIterator = liquidsCache.iterator();
            }

            if (blockPosIterator.hasNext())
            {
                network.syphon(getRefreshCost());
                currentPos = blockPosIterator.next();
                fluidHandler.fill(EnumFacing.DOWN, fluidHandler.drain(EnumFacing.DOWN, 1000, false), true);
                world.setBlockState(currentPos, Blocks.stone.getDefaultState());
                blockPosIterator.remove();
            }
        }
    }

    @Override
    public int getRefreshCost()
    {
        return 25;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        addRune(components, 1, 0, 1, EnumRuneType.WATER);
        addRune(components, 1, 0, -1, EnumRuneType.EARTH);
        addRune(components, -1, 0, -1, EnumRuneType.AIR);
        addRune(components, -1, 0, 1, EnumRuneType.FIRE);

        addCornerRunes(components, 1, 1, EnumRuneType.DUSK);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualPump();
    }
}
