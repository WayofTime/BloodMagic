package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.Utils;

public class RitualFelling extends Ritual
{

    public static final String FELLING_RANGE = "fellingRange";

    private ArrayList<BlockPos> treePartsCache;
    private Iterator<BlockPos> blockPosIterator;

    private boolean cached = false;
    private BlockPos currentPos;

    public RitualFelling()
    {
        super("ritualFelling", 0, 500, "ritual." + Constants.Mod.MODID + ".fellingRitual");
        addBlockRange(FELLING_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -3, -10), new BlockPos(11, 27, 11)));

        treePartsCache = new ArrayList<BlockPos>();
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        if (!cached || treePartsCache.isEmpty())
        {
            for (BlockPos blockPos : getBlockRange(FELLING_RANGE).getContainedPositions(masterRitualStone.getBlockPos()))
            {
                if (!treePartsCache.contains(blockPos))
                    if (!world.isAirBlock(blockPos) && (world.getBlockState(blockPos).getBlock().isWood(world, blockPos) || world.getBlockState(blockPos).getBlock().isLeaves(world, blockPos)))
                    {
                        treePartsCache.add(blockPos);
                    }
            }

            cached = true;
            blockPosIterator = treePartsCache.iterator();
        }

        if (blockPosIterator.hasNext() && world.getTileEntity(masterRitualStone.getBlockPos().up()) != null && world.getTileEntity(masterRitualStone.getBlockPos().up()) instanceof IInventory)
        {
            network.syphon(getRefreshCost());
            currentPos = blockPosIterator.next();
            placeInInventory(world.getBlockState(currentPos), world, currentPos, masterRitualStone.getBlockPos().up());
            world.setBlockToAir(currentPos);
            blockPosIterator.remove();
        }
    }

    @Override
    public int getRefreshCost()
    {
        return 10;
    }

    @Override
    public int getRefreshTime()
    {
        return 1;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
        addCornerRunes(components, 1, 1, EnumRuneType.EARTH);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualFelling();
    }

    private void placeInInventory(IBlockState blockState, World world, BlockPos blockPos, BlockPos tileEntityPos)
    {
        TileEntity tile = world.getTileEntity(tileEntityPos);
        if (tile != null && blockState.getBlock().getDrops(world, blockPos, world.getBlockState(blockPos), 0) != null)
        {
            if (tile instanceof IInventory)
            {
                for (ItemStack stack : blockState.getBlock().getDrops(world, blockPos, world.getBlockState(blockPos), 0))
                {
                    ItemStack copyStack = stack.copy();
                    Utils.insertStackIntoInventory(copyStack, (IInventory) tile, EnumFacing.DOWN);
                    if (copyStack.stackSize > 0)
                    {
                        world.spawnEntityInWorld(new EntityItem(world, blockPos.getX() + 0.4, blockPos.getY() + 2, blockPos.getZ() + 0.4, copyStack));
                    }
                }
            }
        }
    }
}
