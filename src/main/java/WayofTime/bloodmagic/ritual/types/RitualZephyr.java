package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("zephyr")
public class RitualZephyr extends Ritual {
    public static final String ZEPHYR_RANGE = "zephyrRange";
    public static final String CHEST_RANGE = "chest";

    public RitualZephyr() {
        super("ritualZephyr", 0, 1000, "ritual." + BloodMagic.MODID + ".zephyrRitual");
        addBlockRange(ZEPHYR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

        setMaximumVolumeAndDistanceOfRange(ZEPHYR_RANGE, 0, 10, 10);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
        BlockPos masterPos = masterRitualStone.getBlockPos();
        AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
        TileEntity tileInventory = world.getTileEntity(chestRange.getContainedPositions(masterPos).get(0));
        if (!masterRitualStone.getWorldObj().isRemote && tileInventory != null) {
            if (currentEssence < getRefreshCost()) {
                masterRitualStone.getOwnerNetwork().causeNausea();
                return;
            }

            AreaDescriptor zephyrRange = masterRitualStone.getBlockRange(ZEPHYR_RANGE);

            List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, zephyrRange.getAABB(masterRitualStone.getBlockPos()));
            int count = 0;

            for (EntityItem entityItem : itemList) {
                if (entityItem.isDead) {
                    continue;
                }

                ItemStack copyStack = entityItem.getItem().copy();
                int originalAmount = copyStack.getCount();
                ItemStack newStack = Utils.insertStackIntoTile(copyStack, tileInventory, EnumFacing.DOWN);

                if (!newStack.isEmpty() && newStack.getCount() < originalAmount) {
                    count++;
                    if (newStack.isEmpty())
                        entityItem.setDead();

                    entityItem.getItem().setCount(newStack.getCount());
                }

                if (newStack.isEmpty()) {
                    entityItem.setDead();
                }
            }

            masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * Math.min(count, 100)));
        }
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return 1;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addParallelRunes(components, 2, 0, EnumRuneType.AIR);
        addCornerRunes(components, 1, 1, EnumRuneType.AIR);
        addParallelRunes(components, 1, -1, EnumRuneType.AIR);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualZephyr();
    }
}
