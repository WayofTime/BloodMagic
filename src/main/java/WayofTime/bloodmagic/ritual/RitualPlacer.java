package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
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
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        BlockPos masterPos = masterRitualStone.getBlockPos();
        AreaDescriptor chestRange = getBlockRange(CHEST_RANGE);
        TileEntity tileEntity = world.getTileEntity(chestRange.getContainedPositions(masterPos).get(0));

        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNausea();
            return;
        }

        AreaDescriptor areaDescriptor = getBlockRange(PLACER_RANGE);
        IInventory inventory;

        if (tileEntity != null)
        {
            // Using the new Forge inventory system
            if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
            {
                IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

                if (itemHandler.getSlots() <= 0)
                {
                    return;
                }

                for (BlockPos blockPos : areaDescriptor.getContainedPositions(masterRitualStone.getBlockPos()))
                {
                    for (int inv = 0; inv < itemHandler.getSlots(); inv++)
                    {
                        if (world.getBlockState(blockPos).getBlock().isReplaceable(world, blockPos) && !itemHandler.getStackInSlot(inv).isEmpty())
                        {
                            if (itemHandler.getStackInSlot(inv).getItem() instanceof ItemBlock && world.isAirBlock(blockPos.down()))
                            {
                                if (!itemHandler.extractItem(inv, 1, true).isEmpty())
                                {
                                    world.setBlockState(blockPos, Block.getBlockFromItem(itemHandler.getStackInSlot(inv).getItem()).getStateFromMeta(itemHandler.getStackInSlot(inv).getItemDamage()));
                                    itemHandler.extractItem(inv, 1, false);
                                    tileEntity.markDirty();
                                    network.syphon(getRefreshCost());
                                }
                            }
                        }
                    }
                }
                //Compatibility with the old system, as it still exists
            } else if (tileEntity instanceof IInventory)
            {
                inventory = (IInventory) tileEntity;

                if (inventory.getSizeInventory() <= 0)
                {
                    return;
                }

                for (BlockPos blockPos : areaDescriptor.getContainedPositions(masterRitualStone.getBlockPos()))
                {
                    for (int inv = 0; inv < inventory.getSizeInventory(); inv++)
                    {
                        if (world.getBlockState(blockPos).getBlock().isReplaceable(world, blockPos) && !inventory.getStackInSlot(inv).isEmpty())
                        {
                            if (inventory.getStackInSlot(inv).getItem() instanceof ItemBlock && world.isAirBlock(blockPos.down()))
                            {
                                world.setBlockState(blockPos, Block.getBlockFromItem(inventory.getStackInSlot(inv).getItem()).getStateFromMeta(inventory.getStackInSlot(inv).getItemDamage()));
                                inventory.decrStackSize(inv, 1);
                                inventory.markDirty();
                                network.syphon(getRefreshCost());
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
