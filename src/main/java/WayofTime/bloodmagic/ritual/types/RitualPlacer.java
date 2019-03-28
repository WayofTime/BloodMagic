package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Consumer;

@RitualRegister("placer")
public class RitualPlacer extends Ritual {
    public static final String PLACER_RANGE = "placerRange";
    public static final String CHEST_RANGE = "chest";

    public RitualPlacer() {
        super("ritualPlacer", 0, 5000, "ritual." + BloodMagic.MODID + ".placerRitual");
        addBlockRange(PLACER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 0, -2), 5, 1, 5));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

        setMaximumVolumeAndDistanceOfRange(PLACER_RANGE, 300, 7, 7);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        BlockPos masterPos = masterRitualStone.getBlockPos();
        AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
        TileEntity tileEntity = world.getTileEntity(chestRange.getContainedPositions(masterPos).get(0));

        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        AreaDescriptor areaDescriptor = masterRitualStone.getBlockRange(PLACER_RANGE);

        if (tileEntity != null) {
            if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

                if (itemHandler.getSlots() <= 0) {
                    return;
                }

                posLoop:
                for (BlockPos blockPos : areaDescriptor.getContainedPositions(masterRitualStone.getBlockPos())) {
                    if (!world.getBlockState(blockPos).getBlock().isReplaceable(world, blockPos))
                        continue;

                    for (int invSlot = 0; invSlot < itemHandler.getSlots(); invSlot++) {
                        ItemStack stack = itemHandler.extractItem(invSlot, 1, true);
                        if (stack.isEmpty() || !(stack.getItem() instanceof ItemBlock))
                            continue;

                        IBlockState placeState = Block.getBlockFromItem(itemHandler.getStackInSlot(invSlot).getItem()).getStateFromMeta(itemHandler.getStackInSlot(invSlot).getItemDamage());
                        world.setBlockState(blockPos, placeState);
                        itemHandler.extractItem(invSlot, 1, false);
                        tileEntity.markDirty();
                        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
                        break posLoop; // Break instead of return in case we add things later
                    }
                }
            }
        }
    }

    @Override
    public int getRefreshCost() {
        return 50;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addRune(components, 3, 0, 3, EnumRuneType.EARTH);
        addRune(components, 3, 0, -3, EnumRuneType.EARTH);
        addRune(components, -3, 0, 3, EnumRuneType.EARTH);
        addRune(components, -3, 0, -3, EnumRuneType.EARTH);

        addRune(components, 3, 0, 2, EnumRuneType.WATER);
        addRune(components, 3, 0, -2, EnumRuneType.WATER);
        addRune(components, 2, 0, 3, EnumRuneType.WATER);
        addRune(components, 2, 0, -3, EnumRuneType.WATER);
        addRune(components, -2, 0, 3, EnumRuneType.WATER);
        addRune(components, -2, 0, -3, EnumRuneType.WATER);
        addRune(components, -3, 0, 2, EnumRuneType.WATER);
        addRune(components, -3, 0, -2, EnumRuneType.WATER);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualPlacer();
    }
}
