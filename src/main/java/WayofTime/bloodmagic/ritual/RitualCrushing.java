package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.util.Utils;

public class RitualCrushing extends Ritual
{
    public static final String CRUSHING_RANGE = "crushingRange";
    public static final String CHEST_RANGE = "chest";

    public RitualCrushing()
    {
        super("ritualCrushing", 0, 5000, "ritual." + Constants.Mod.MODID + ".crushingRitual");
        addBlockRange(CRUSHING_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, -3, -1), 3));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

        setMaximumVolumeAndDistanceOfRange(CRUSHING_RANGE, 50, 10, 10);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNausea();
            return;
        }

        TileEntity tile = world.getTileEntity(masterRitualStone.getBlockPos().up());

        boolean isSilkTouch = false;
        int fortune = 0;

        AreaDescriptor crushingRange = getBlockRange(CRUSHING_RANGE);
        BlockPos pos = masterRitualStone.getBlockPos();

        for (BlockPos newPos : crushingRange.getContainedPositions(pos))
        {
            if (world.isAirBlock(newPos))
            {
                continue;
            }

            IBlockState state = world.getBlockState(newPos);
            Block block = state.getBlock();
            if (block.equals(ModBlocks.ritualController) || block.equals(ModBlocks.ritualStone) || block.getBlockHardness(state, world, newPos) == -1.0F)
            {
                continue;
            }

            if (isSilkTouch && block.canSilkHarvest(world, newPos, state, null))
            {
                int meta = block.getMetaFromState(state);
                ItemStack item = new ItemStack(block, 1, meta);
                ItemStack copyStack = ItemStack.copyItemStack(item);

                if (tile != null)
                    Utils.insertStackIntoTile(copyStack, tile, EnumFacing.DOWN);
                else
                    Utils.spawnStackAtBlock(world, pos, EnumFacing.UP, copyStack);

                if (copyStack.stackSize > 0)
                {
                    Utils.spawnStackAtBlock(world, pos, EnumFacing.UP, copyStack);
                }
            } else
            {
                List<ItemStack> stackList = block.getDrops(world, newPos, state, fortune);

                if (stackList != null && !stackList.isEmpty())
                {
                    for (ItemStack item : stackList)
                    {
                        ItemStack copyStack = ItemStack.copyItemStack(item);

                        if (tile != null)
                        {
                            copyStack = Utils.insertStackIntoTile(copyStack, tile, EnumFacing.DOWN);
                        } else
                        {
                            Utils.spawnStackAtBlock(world, pos, EnumFacing.UP, copyStack);
                            continue;
                        }
                        if (copyStack != null && copyStack.stackSize > 0)
                        {
                            Utils.spawnStackAtBlock(world, pos, EnumFacing.UP, copyStack);
                        }
                    }
                }
            }

            world.destroyBlock(newPos, false);
            network.syphon(getRefreshCost());

            break;
        }
    }

    @Override
    public int getRefreshTime()
    {
        return 40;
    }

    @Override
    public int getRefreshCost()
    {
        return 7;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addParallelRunes(ritualComponents, 1, 0, EnumRuneType.EARTH);
        this.addParallelRunes(components, 2, 0, EnumRuneType.FIRE);
        this.addCornerRunes(components, 2, 0, EnumRuneType.DUSK);
        this.addParallelRunes(components, 2, 1, EnumRuneType.AIR);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualCrushing();
    }
}
