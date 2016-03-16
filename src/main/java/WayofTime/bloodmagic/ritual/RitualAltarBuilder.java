package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.AltarComponent;
import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.util.Utils;

public class RitualAltarBuilder extends Ritual
{
    private Iterator<AltarComponent> altarComponentsIterator = new ArrayList<AltarComponent>(EnumAltarTier.SIX.getAltarComponents()).iterator();
    private boolean cycleDone = false;

    private AltarComponent currentComponent;
    private BlockPos currentPos;

    public RitualAltarBuilder()
    {
        super("ritualAltarBuilder", 0, 450, "ritual." + Constants.Mod.MODID + ".altarBuilderRitual");
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        TileEntity tileEntity = world.getTileEntity(masterRitualStone.getBlockPos().up());
        BlockPos altarPos = masterRitualStone.getBlockPos().up(2);

        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        if (cycleDone)
        {
            altarComponentsIterator = new ArrayList<AltarComponent>(EnumAltarTier.SIX.getAltarComponents()).iterator();
        }

        if (world.getBlockState(altarPos).getBlock().isReplaceable(world, altarPos) && hasItem(tileEntity, Item.getItemFromBlock(ModBlocks.altar), 0, true))
        {
            world.setBlockState(altarPos, ModBlocks.altar.getDefaultState());
            lightning(world, altarPos);
            network.syphon(getRefreshCost());
        }

        if (altarComponentsIterator.hasNext())
        {
            currentComponent = altarComponentsIterator.next();
            currentPos = altarPos.add(currentComponent.getOffset());

            if (world.getBlockState(currentPos).getBlock().isReplaceable(world, currentPos))
            {
                switch (currentComponent.getComponent())
                {
                case NOTAIR:
                {
                    BlockStack blockStack = getMundaneBlock(tileEntity);
                    if (blockStack != null)
                    {
                        world.setBlockState(currentPos, blockStack.getState(), 3);
                        lightning(world, currentPos);
                        network.syphon(getRefreshCost());
                    }
                    break;
                }
                case BLOODRUNE:
                {
                    BlockStack blockStack = getBloodRune(tileEntity);
                    if (blockStack != null)
                    {
                        world.setBlockState(currentPos, blockStack.getState(), 3);
                        lightning(world, currentPos);
                        network.syphon(getRefreshCost());
                    }
                    break;
                }
                default:
                {
                    BlockStack blockStack = new BlockStack(Utils.getBlockForComponent(currentComponent.getComponent()), 0);
                    if (hasItem(tileEntity, Item.getItemFromBlock(blockStack.getBlock()), blockStack.getMeta(), true))
                    {
                        world.setBlockState(currentPos, blockStack.getState(), 3);
                        lightning(world, currentPos);
                        network.syphon(getRefreshCost());
                    }
                    break;
                }
                }
            }
        } else
        {
            cycleDone = true;
        }
    }

    @Override
    public int getRefreshCost()
    {
        return 75;
    }

    @Override
    public int getRefreshTime()
    {
        return 12;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        for (int i = -12; i <= -8; i++)
        {
            addRune(components, i, -6, 13, EnumRuneType.AIR);
            addRune(components, i, -6, -13, EnumRuneType.FIRE);

            addRune(components, 13, -6, i, EnumRuneType.EARTH);
            addRune(components, -13, -6, i, EnumRuneType.WATER);

            addRune(components, i, 5, 13, EnumRuneType.AIR);
            addRune(components, i, 5, -13, EnumRuneType.FIRE);

            addRune(components, 13, 5, i, EnumRuneType.EARTH);
            addRune(components, -13, 5, i, EnumRuneType.WATER);
        }

        for (int i = 8; i <= 12; i++)
        {
            addRune(components, i, -6, 13, EnumRuneType.AIR);
            addRune(components, i, -6, -13, EnumRuneType.FIRE);

            addRune(components, 13, -6, i, EnumRuneType.EARTH);
            addRune(components, -13, -6, i, EnumRuneType.WATER);

            addRune(components, i, 5, 13, EnumRuneType.AIR);
            addRune(components, i, 5, -13, EnumRuneType.FIRE);

            addRune(components, 13, 5, i, EnumRuneType.EARTH);
            addRune(components, -13, 5, i, EnumRuneType.WATER);
        }

        for (int i = -6; i <= -4; i++)
        {
            addCornerRunes(components, 13, i, EnumRuneType.DUSK);
        }

        for (int i = 3; i <= 5; i++)
        {
            addCornerRunes(components, 13, i, EnumRuneType.DUSK);
        }

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualAltarBuilder();
    }

    public void lightning(World world, BlockPos blockPos)
    {
        world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY(), blockPos.getZ()));
    }

    /*
     * 
     * These methods are utilities for this ritual. They support both the old
     * forge inventory system, and the new one.
     */
    public boolean hasItem(TileEntity tileEntity, Item item, int damage, boolean consumeItem)
    {
        if (tileEntity != null)
        {
            if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
            {
                IItemHandler iItemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

                if (iItemHandler.getSlots() <= 0)
                {
                    return false;
                }

                for (int i = 0; i < iItemHandler.getSlots(); i++)
                {
                    if (iItemHandler.getStackInSlot(i) != null && iItemHandler.getStackInSlot(i).stackSize > 0 && iItemHandler.getStackInSlot(i).getItem() == item && iItemHandler.getStackInSlot(i).getItemDamage() == damage && iItemHandler.extractItem(i, 1, !consumeItem) != null)
                    {
                        return true;
                    }
                }
            } else if (tileEntity instanceof IInventory)
            {
                IInventory inv = (IInventory) tileEntity;
                for (int i = 0; i < inv.getSizeInventory(); i++)
                {
                    if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).stackSize > 0 && inv.getStackInSlot(i).getItem() == item && inv.getStackInSlot(i).getItemDamage() == damage)
                    {
                        if (consumeItem)
                        {
                            inv.decrStackSize(i, 1);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public BlockStack getBloodRune(TileEntity tileEntity)
    {
        if (tileEntity != null)
        {
            if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
            {
                IItemHandler iItemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

                if (iItemHandler.getSlots() <= 0)
                {
                    return null;
                }

                for (int i = 0; i < iItemHandler.getSlots(); i++)
                {
                    if (iItemHandler.getStackInSlot(i) != null && iItemHandler.getStackInSlot(i).stackSize > 0 && iItemHandler.getStackInSlot(i).getItem() instanceof ItemBlock && Block.getBlockFromItem(iItemHandler.getStackInSlot(i).getItem()) instanceof BlockBloodRune && iItemHandler.extractItem(i, 1, true) != null)
                    {
                        BlockStack blockStack = new BlockStack(Utils.getBlockForComponent(EnumAltarComponent.BLOODRUNE), iItemHandler.getStackInSlot(i).getItemDamage());
                        iItemHandler.extractItem(i, 1, false);
                        return blockStack;
                    }
                }
            } else if (tileEntity instanceof IInventory)
            {
                IInventory inv = (IInventory) tileEntity;
                for (int i = 0; i < inv.getSizeInventory(); i++)
                {
                    if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).stackSize > 0 && inv.getStackInSlot(i).getItem() instanceof ItemBlock && Block.getBlockFromItem(inv.getStackInSlot(i).getItem()) instanceof BlockBloodRune)
                    {
                        BlockStack blockStack = new BlockStack(Utils.getBlockForComponent(EnumAltarComponent.BLOODRUNE), inv.getStackInSlot(i).getItemDamage());
                        inv.decrStackSize(i, 1);
                        return blockStack;
                    }
                }
            }
        }
        return null;
    }

    public BlockStack getMundaneBlock(TileEntity tileEntity)
    {
        if (tileEntity != null)
        {
            if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
            {
                IItemHandler iItemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

                if (iItemHandler.getSlots() <= 0)
                {
                    return null;
                }

                for (int i = 0; i < iItemHandler.getSlots(); i++)
                {
                    if (iItemHandler.getStackInSlot(i) != null && iItemHandler.getStackInSlot(i).stackSize > 0 && iItemHandler.getStackInSlot(i).getItem() instanceof ItemBlock && !(Block.getBlockFromItem(iItemHandler.getStackInSlot(i).getItem()) instanceof BlockBloodRune) && iItemHandler.extractItem(i, 1, true) != null)
                    {
                        Block block = Block.getBlockFromItem(iItemHandler.getStackInSlot(i).getItem());
                        if (block != null && block != Blocks.glowstone && block != ModBlocks.bloodStoneBrick && block != ModBlocks.crystal)
                        {
                            BlockStack blockStack = new BlockStack(block, iItemHandler.getStackInSlot(i).getItemDamage());
                            iItemHandler.extractItem(i, 1, false);
                            return blockStack;
                        }
                    }
                }
            } else if (tileEntity instanceof IInventory)
            {
                IInventory inv = (IInventory) tileEntity;
                for (int i = 0; i < inv.getSizeInventory(); i++)
                {
                    if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).stackSize > 0 && inv.getStackInSlot(i).getItem() instanceof ItemBlock && !(Block.getBlockFromItem(inv.getStackInSlot(i).getItem()) instanceof BlockBloodRune))
                    {
                        Block block = Block.getBlockFromItem(inv.getStackInSlot(i).getItem());
                        if (block != null && block != Blocks.glowstone && block != ModBlocks.bloodStoneBrick && block != ModBlocks.crystal)
                        {
                            BlockStack blockStack = new BlockStack(block, inv.getStackInSlot(i).getItemDamage());
                            inv.decrStackSize(i, 1);
                            return blockStack;
                        }
                    }
                }
            }
        }
        return null;
    }
}
