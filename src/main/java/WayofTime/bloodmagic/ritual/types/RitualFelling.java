package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

@RitualRegister("felling")
public class RitualFelling extends Ritual {
    public static final String FELLING_RANGE = "fellingRange";
    public static final String CHEST_RANGE = "chest";

    private ArrayList<BlockPos> treePartsCache;
    private Iterator<BlockPos> blockPosIterator;

    private boolean cached = false;
    private BlockPos currentPos;

    public RitualFelling() {
        super("ritualFelling", 0, 20000, "ritual." + BloodMagic.MODID + ".fellingRitual");
        addBlockRange(FELLING_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -3, -10), new BlockPos(11, 27, 11)));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

        setMaximumVolumeAndDistanceOfRange(FELLING_RANGE, 14000, 15, 30);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);

        treePartsCache = new ArrayList<>();
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        BlockPos masterPos = masterRitualStone.getBlockPos();
        AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
        TileEntity tileInventory = world.getTileEntity(chestRange.getContainedPositions(masterPos).get(0));

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        if (!cached || treePartsCache.isEmpty()) {
            for (BlockPos blockPos : masterRitualStone.getBlockRange(FELLING_RANGE).getContainedPositions(masterRitualStone.getBlockPos())) {
                if (!treePartsCache.contains(blockPos))
                    if (!world.isAirBlock(blockPos) && (world.getBlockState(blockPos).getBlock().isWood(world, blockPos) || world.getBlockState(blockPos).getBlock().isLeaves(world.getBlockState(blockPos), world, blockPos))) {
                        treePartsCache.add(blockPos);
                    }
            }

            cached = true;
            blockPosIterator = treePartsCache.iterator();
        }

        if (blockPosIterator.hasNext() && tileInventory != null) {
            masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
            currentPos = blockPosIterator.next();
            IItemHandler inventory = Utils.getInventory(tileInventory, EnumFacing.DOWN);
            placeInInventory(world.getBlockState(currentPos), world, currentPos, inventory);
            world.setBlockToAir(currentPos);
            blockPosIterator.remove();
        }
    }

    @Override
    public int getRefreshCost() {
        return 10;
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
        addCornerRunes(components, 1, 1, EnumRuneType.EARTH);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualFelling();
    }

    private void placeInInventory(IBlockState choppedState, World world, BlockPos choppedPos, @Nullable IItemHandler inventory) {
        if (inventory == null)
            return;

        for (ItemStack stack : choppedState.getBlock().getDrops(world, choppedPos, world.getBlockState(choppedPos), 0)) {
            ItemStack remainder = ItemHandlerHelper.insertItem(inventory, stack, false);
            if (!remainder.isEmpty())
                world.spawnEntity(new EntityItem(world, choppedPos.getX() + 0.4, choppedPos.getY() + 2, choppedPos.getZ() + 0.4, remainder));
        }
    }
}
