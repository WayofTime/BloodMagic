package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.*;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;

public class RitualPlacer extends Ritual
{
    public static final String PLACER_RANGE = "placerRange";
    public static final String CHEST_RANGE = "chest";

    public RitualPlacer()
    {
        super("ritualPlacer", 0, 5000, "ritual." + Constants.Mod.MODID + ".placerRitual");
        addBlockRange(PLACER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 0, -2), 5, 1, 5));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

        setMaximumVolumeAndDistanceOfRange(PLACER_RANGE, 300, 7, 7);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        BlockPos masterPos = masterRitualStone.getBlockPos();
        AreaDescriptor chestRange = getBlockRange(CHEST_RANGE);
        TileEntity tileEntity = world.getTileEntity(chestRange.getContainedPositions(masterPos).get(0));

        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        AreaDescriptor areaDescriptor = getBlockRange(PLACER_RANGE);
        IInventory iInventory;

        if (tileEntity != null)
        {
            // Using the new Forge inventory system
            if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
            {
                IItemHandler iItemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

                if (iItemHandler.getSlots() <= 0)
                {
                    return;
                }

                for (BlockPos blockPos : areaDescriptor.getContainedPositions(masterRitualStone.getBlockPos()))
                {
                    for (int inv = 0; inv < iItemHandler.getSlots(); inv++)
                    {
                        if (world.getBlockState(blockPos).getBlock().isReplaceable(world, blockPos) && iItemHandler.getStackInSlot(inv) != null && iItemHandler.getStackInSlot(inv).stackSize != 0)
                        {
                            if (iItemHandler.getStackInSlot(inv).getItem() instanceof ItemBlock && world.getBlockState(blockPos.down()) != null)
                            {
                                if (iItemHandler.extractItem(inv, 1, true) != null)
                                {
                                    world.setBlockState(blockPos, Block.getBlockFromItem(iItemHandler.getStackInSlot(inv).getItem()).getStateFromMeta(iItemHandler.getStackInSlot(inv).getItemDamage()));
                                    iItemHandler.extractItem(inv, 1, false);
                                    tileEntity.markDirty();
                                    masterRitualStone.getOwnerNetwork().syphon(getRefreshCost());
                                }
                            }
                        }
                    }
                }
                //Compatibility with the old system, as it still exists
            } else if (tileEntity instanceof IInventory)
            {
                iInventory = (IInventory) tileEntity;

                if (iInventory.getSizeInventory() <= 0)
                {
                    return;
                }

                for (BlockPos blockPos : areaDescriptor.getContainedPositions(masterRitualStone.getBlockPos()))
                {
                    for (int inv = 0; inv < iInventory.getSizeInventory(); inv++)
                    {
                        if (world.getBlockState(blockPos).getBlock().isReplaceable(world, blockPos) && iInventory.getStackInSlot(inv) != null && iInventory.getStackInSlot(inv).stackSize != 0)
                        {
                            if (iInventory.getStackInSlot(inv).getItem() instanceof ItemBlock && world.getBlockState(blockPos.down()) != null)
                            {
                                world.setBlockState(blockPos, Block.getBlockFromItem(iInventory.getStackInSlot(inv).getItem()).getStateFromMeta(iInventory.getStackInSlot(inv).getItemDamage()));
                                iInventory.decrStackSize(inv, 1);
                                iInventory.markDirty();
                                masterRitualStone.getOwnerNetwork().syphon(getRefreshCost());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getRefreshCost()
    {
        return 50;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

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

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualPlacer();
    }
}
