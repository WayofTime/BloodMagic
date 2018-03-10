package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.BlockStack;
import WayofTime.bloodmagic.altar.AltarComponent;
import WayofTime.bloodmagic.altar.ComponentType;
import WayofTime.bloodmagic.altar.AltarTier;
import WayofTime.bloodmagic.ritual.EnumRuneType;
import WayofTime.bloodmagic.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.ritual.RitualComponent;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class RitualAltarBuilder extends Ritual {
    private Iterator<AltarComponent> altarComponentsIterator = new ArrayList<>(AltarTier.SIX.getAltarComponents()).iterator();
    private boolean cycleDone = false;

    private AltarComponent currentComponent;
    private BlockPos currentPos;

    public RitualAltarBuilder() {
        super("ritualAltarBuilder", 0, 450, "ritual." + BloodMagic.MODID + ".altarBuilderRitual");
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        TileEntity tileEntity = world.getTileEntity(masterRitualStone.getBlockPos().up());
        BlockPos altarPos = masterRitualStone.getBlockPos().up(2);

        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        if (cycleDone) {
            altarComponentsIterator = new ArrayList<>(AltarTier.SIX.getAltarComponents()).iterator();
        }

        if (world.getBlockState(altarPos).getBlock().isReplaceable(world, altarPos) && hasItem(tileEntity, Item.getItemFromBlock(RegistrarBloodMagicBlocks.ALTAR), 0, true)) {
            world.setBlockState(altarPos, RegistrarBloodMagicBlocks.ALTAR.getDefaultState());
            lightning(world, altarPos);
            masterRitualStone.getOwnerNetwork().syphon(getRefreshCost());
        }

        if (altarComponentsIterator.hasNext()) {
            currentComponent = altarComponentsIterator.next();
            currentPos = altarPos.add(currentComponent.getOffset());

            if (world.getBlockState(currentPos).getBlock().isReplaceable(world, currentPos)) {
                switch (currentComponent.getComponent()) {
                    case NOTAIR: {
                        BlockStack blockStack = getMundaneBlock(tileEntity);
                        if (blockStack != null) {
                            world.setBlockState(currentPos, blockStack.getState(), 3);
                            lightning(world, currentPos);
                            masterRitualStone.getOwnerNetwork().syphon(getRefreshCost());
                        }
                        break;
                    }
                    case BLOODRUNE: {
                        BlockStack blockStack = getBloodRune(tileEntity);
                        if (blockStack != null) {
                            world.setBlockState(currentPos, blockStack.getState(), 3);
                            lightning(world, currentPos);
                            masterRitualStone.getOwnerNetwork().syphon(getRefreshCost());
                        }
                        break;
                    }
                    default: {
                        BlockStack blockStack = new BlockStack(Utils.getBlockForComponent(currentComponent.getComponent()), 0);
                        if (hasItem(tileEntity, Item.getItemFromBlock(blockStack.getBlock()), blockStack.getMeta(), true)) {
                            world.setBlockState(currentPos, blockStack.getState(), 3);
                            lightning(world, currentPos);
                            masterRitualStone.getOwnerNetwork().syphon(getRefreshCost());
                        }
                        break;
                    }
                }
            }
        } else {
            cycleDone = true;
        }
    }

    @Override
    public int getRefreshCost() {
        return 75;
    }

    @Override
    public int getRefreshTime() {
        return 12;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        for (int i = -12; i <= -8; i++) {
            addRune(components, i, -6, 13, EnumRuneType.AIR);
            addRune(components, i, -6, -13, EnumRuneType.FIRE);

            addRune(components, 13, -6, i, EnumRuneType.EARTH);
            addRune(components, -13, -6, i, EnumRuneType.WATER);

            addRune(components, i, 5, 13, EnumRuneType.AIR);
            addRune(components, i, 5, -13, EnumRuneType.FIRE);

            addRune(components, 13, 5, i, EnumRuneType.EARTH);
            addRune(components, -13, 5, i, EnumRuneType.WATER);
        }

        for (int i = 8; i <= 12; i++) {
            addRune(components, i, -6, 13, EnumRuneType.AIR);
            addRune(components, i, -6, -13, EnumRuneType.FIRE);

            addRune(components, 13, -6, i, EnumRuneType.EARTH);
            addRune(components, -13, -6, i, EnumRuneType.WATER);

            addRune(components, i, 5, 13, EnumRuneType.AIR);
            addRune(components, i, 5, -13, EnumRuneType.FIRE);

            addRune(components, 13, 5, i, EnumRuneType.EARTH);
            addRune(components, -13, 5, i, EnumRuneType.WATER);
        }

        for (int i = -6; i <= -4; i++) {
            addCornerRunes(components, 13, i, EnumRuneType.DUSK);
        }

        for (int i = 3; i <= 5; i++) {
            addCornerRunes(components, 13, i, EnumRuneType.DUSK);
        }
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualAltarBuilder();
    }

    public void lightning(World world, BlockPos blockPos) {
        world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), true));
    }

    /*
     * 
     * These methods are utilities for this ritual. They support both the old
     * forge inventory system, and the new one.
     */
    public boolean hasItem(TileEntity tileEntity, Item item, int damage, boolean consumeItem) {
        if (tileEntity != null) {
            if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

                if (itemHandler.getSlots() <= 0) {
                    return false;
                }

                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    if (!itemHandler.getStackInSlot(i).isEmpty() && itemHandler.getStackInSlot(i).getItem() == item && itemHandler.getStackInSlot(i).getItemDamage() == damage && !itemHandler.extractItem(i, 1, !consumeItem).isEmpty()) {
                        return true;
                    }
                }
            } else if (tileEntity instanceof IInventory) {
                IInventory inv = (IInventory) tileEntity;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    if (!inv.getStackInSlot(0).isEmpty() && inv.getStackInSlot(i).getItem() == item && inv.getStackInSlot(i).getItemDamage() == damage) {
                        if (consumeItem) {
                            inv.decrStackSize(i, 1);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public BlockStack getBloodRune(TileEntity tileEntity) {
        if (tileEntity != null) {
            if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

                if (itemHandler.getSlots() <= 0) {
                    return null;
                }

                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    if (!itemHandler.getStackInSlot(i).isEmpty() && itemHandler.getStackInSlot(i).getItem() instanceof ItemBlock && Block.getBlockFromItem(itemHandler.getStackInSlot(i).getItem()) instanceof BlockBloodRune && itemHandler.extractItem(i, 1, true) != null) {
                        BlockStack blockStack = new BlockStack(Utils.getBlockForComponent(ComponentType.BLOODRUNE), itemHandler.getStackInSlot(i).getItemDamage());
                        itemHandler.extractItem(i, 1, false);
                        return blockStack;
                    }
                }
            } else if (tileEntity instanceof IInventory) {
                IInventory inv = (IInventory) tileEntity;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    if (!inv.getStackInSlot(i).isEmpty() && inv.getStackInSlot(i).getItem() instanceof ItemBlock && Block.getBlockFromItem(inv.getStackInSlot(i).getItem()) instanceof BlockBloodRune) {
                        BlockStack blockStack = new BlockStack(Utils.getBlockForComponent(ComponentType.BLOODRUNE), inv.getStackInSlot(i).getItemDamage());
                        inv.decrStackSize(i, 1);
                        return blockStack;
                    }
                }
            }
        }
        return null;
    }

    public BlockStack getMundaneBlock(TileEntity tileEntity) {
        if (tileEntity != null) {
            if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

                if (itemHandler.getSlots() <= 0) {
                    return null;
                }

                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    if (!itemHandler.getStackInSlot(i).isEmpty() && itemHandler.getStackInSlot(i).getItem() instanceof ItemBlock && !(Block.getBlockFromItem(itemHandler.getStackInSlot(i).getItem()) instanceof BlockBloodRune) && !itemHandler.extractItem(i, 1, true).isEmpty()) {
                        Block block = Block.getBlockFromItem(itemHandler.getStackInSlot(i).getItem());
                        if (block != Blocks.AIR && block != Blocks.GLOWSTONE && block != RegistrarBloodMagicBlocks.DECORATIVE_BRICK) {
                            BlockStack blockStack = new BlockStack(block, itemHandler.getStackInSlot(i).getItemDamage());
                            itemHandler.extractItem(i, 1, false);
                            return blockStack;
                        }
                    }
                }
            } else if (tileEntity instanceof IInventory) {
                IInventory inv = (IInventory) tileEntity;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    if (!inv.getStackInSlot(i).isEmpty() && inv.getStackInSlot(i).getItem() instanceof ItemBlock && !(Block.getBlockFromItem(inv.getStackInSlot(i).getItem()) instanceof BlockBloodRune)) {
                        Block block = Block.getBlockFromItem(inv.getStackInSlot(i).getItem());
                        if (block != Blocks.GLOWSTONE && block != RegistrarBloodMagicBlocks.DECORATIVE_BRICK) {
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
