package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class RitualPlacer extends Ritual
{
    public static final String PLACER_RANGE = "placerRange";

    public RitualPlacer()
    {
        super("ritualPlacer", 0, 5000, "ritual." + Constants.Mod.MODID + ".placerRitual");
        addBlockRange(PLACER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 0, -2), new BlockPos(3, 1, 3)));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        TileEntity tileEntity = world.getTileEntity(masterRitualStone.getBlockPos().up());

        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
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
                                    network.syphon(getRefreshCost());
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
                                network.syphon(getRefreshCost());
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
