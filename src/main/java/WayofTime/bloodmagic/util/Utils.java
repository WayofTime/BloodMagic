package WayofTime.bloodmagic.util;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.api.iface.IDemonWillViewer;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.PlayerVelocityPacketProcessor;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.tile.TileInventory;

import com.google.common.collect.Iterables;

public class Utils
{
    public static boolean canPlayerSeeDemonWill(EntityPlayer player)
    {
        ItemStack[] mainInventory = player.inventory.mainInventory;

        for (ItemStack stack : mainInventory)
        {
            if (stack == null)
            {
                continue;
            }

            if (stack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) stack.getItem()).canSeeDemonWillAura(player.worldObj, stack, player))
            {
                return true;
            }
        }

        return false;
    }

    public static int getDemonWillResolution(EntityPlayer player)
    {
        ItemStack[] mainInventory = player.inventory.mainInventory;

        for (ItemStack stack : mainInventory)
        {
            if (stack == null)
            {
                continue;
            }

            if (stack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) stack.getItem()).canSeeDemonWillAura(player.worldObj, stack, player))
            {
                return ((IDemonWillViewer) stack.getItem()).getDemonWillAuraResolution(player.worldObj, stack, player);
            }
        }

        return 1;
    }

    public static NBTTagCompound getPersistentDataTag(EntityPlayer player)
    {
        NBTTagCompound forgeData = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        NBTTagCompound beaconData = forgeData.getCompoundTag("BloodMagic");

        //Creates/sets the tags if they don't exist 
        if (!forgeData.hasKey("BloodMagic"))
            forgeData.setTag("BloodMagic", beaconData);
        if (!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG))
            player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, forgeData);

        return beaconData;
    }

    public static void setPlayerSpeedFromServer(EntityPlayer player, double motionX, double motionY, double motionZ)
    {
        if (!player.worldObj.isRemote && player instanceof EntityPlayerMP)
        {
            BloodMagicPacketHandler.sendTo(new PlayerVelocityPacketProcessor(motionX, motionY, motionZ), (EntityPlayerMP) player);
        }
    }

    public static boolean isInteger(String integer)
    {
        try
        {
            Integer.parseInt(integer);
        } catch (NumberFormatException e)
        {
            return false;
        } catch (NullPointerException e)
        {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static String toFancyCasing(String input)
    {
        return String.valueOf(input.charAt(0)).toUpperCase(Locale.ENGLISH) + input.substring(1);
    }

    public static String prettifyBlockPosString(BlockPos pos)
    {
        return "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]";
    }

    /**
     * @param tile
     *        - The {@link TileInventory} to input the item to
     * @param player
     *        - The player to take the item from.
     * @return {@code true} if the ItemStack is inserted, {@code false}
     *         otherwise
     * @see #insertItemToTile(TileInventory, EntityPlayer, int)
     */
    public static boolean insertItemToTile(TileInventory tile, EntityPlayer player)
    {
        return insertItemToTile(tile, player, 0);
    }

    /**
     * Used for inserting an ItemStack with a stacksize of 1 to a tile's
     * inventory at slot 0
     * <p/>
     * EG: Block Altar
     * 
     * @param tile
     *        - The {@link TileInventory} to input the item to
     * @param player
     *        - The player to take the item from.
     * @param slot
     *        - The slot to attempt to insert to
     * @return {@code true} if the ItemStack is inserted, {@code false}
     *         otherwise
     */
    public static boolean insertItemToTile(TileInventory tile, EntityPlayer player, int slot)
    {
        if (tile.getStackInSlot(slot) == null && player.getHeldItemMainhand() != null)
        {
            ItemStack input = player.getHeldItemMainhand().copy();
            input.stackSize = 1;
            player.getHeldItemMainhand().stackSize--;
            tile.setInventorySlotContents(slot, input);
            return true;
        } else if (tile.getStackInSlot(slot) != null && player.getHeldItemMainhand() == null)
        {
            if (!tile.getWorld().isRemote)
            {
                EntityItem invItem = new EntityItem(tile.getWorld(), player.posX, player.posY + 0.25, player.posZ, tile.getStackInSlot(slot));
                tile.getWorld().spawnEntityInWorld(invItem);
            }
            tile.clear();
            return false;
        }

        return false;
    }

    @Nullable
    public static IItemHandler getInventory(TileEntity tile, @Nullable EnumFacing facing)
    {
        if (facing == null)
            facing = EnumFacing.DOWN;

        IItemHandler itemHandler = null;

        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing))
            itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
        else if (tile instanceof ISidedInventory)
            itemHandler = ((ISidedInventory) tile).getSlotsForFace(facing).length != 0 ? new SidedInvWrapper((ISidedInventory) tile, facing) : null;
        else if (tile instanceof IInventory)
            itemHandler = new InvWrapper((IInventory) tile);

        return itemHandler;
    }

    public static ItemStack setUnbreakable(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);
        stack.getTagCompound().setBoolean("Unbreakable", true);
        return stack;
    }

    /**
     * Gets a default block for each type of {@link EnumAltarComponent}
     * 
     * @param component
     *        - The Component to provide a block for.
     * @return The default Block for the EnumAltarComponent
     */
    public static Block getBlockForComponent(EnumAltarComponent component)
    {
        switch (component)
        {
        case GLOWSTONE:
            return Blocks.GLOWSTONE;
        case BLOODSTONE:
            return ModBlocks.bloodStoneBrick;
        case BEACON:
            return Blocks.BEACON;
        case BLOODRUNE:
            return ModBlocks.bloodRune;
        case CRYSTAL:
            return ModBlocks.crystal;
        case NOTAIR:
            return Blocks.STONEBRICK;
        default:
            return Blocks.AIR;
        }
    }

    public static float getModifiedDamage(EntityLivingBase attackedEntity, DamageSource source, float amount)
    {
        if (!attackedEntity.isEntityInvulnerable(source))
        {
            if (amount <= 0)
                return 0;

            amount = applyArmor(attackedEntity, Iterables.toArray(attackedEntity.getEquipmentAndArmor(), ItemStack.class), source, amount);
            if (amount <= 0)
                return 0;
            amount = applyPotionDamageCalculations(attackedEntity, source, amount);

            return amount;
        }

        return 0;
    }

    public static float applyArmor(EntityLivingBase entity, ItemStack[] inventory, DamageSource source, double damage)
    {
        damage *= 25;
        ArrayList<ArmorProperties> dmgVals = new ArrayList<ArmorProperties>();
        for (int x = 0; x < inventory.length; x++)
        {
            ItemStack stack = inventory[x];
            if (stack == null)
            {
                continue;
            }
            ArmorProperties prop = null;
            if (stack.getItem() instanceof ISpecialArmor)
            {
                ISpecialArmor armor = (ISpecialArmor) stack.getItem();
                prop = armor.getProperties(entity, stack, source, damage / 25D, x).copy();
            } else if (stack.getItem() instanceof ItemArmor && !source.isUnblockable())
            {
                ItemArmor armor = (ItemArmor) stack.getItem();
                prop = new ArmorProperties(0, armor.damageReduceAmount / 25D, Integer.MAX_VALUE);
            }
            if (prop != null)
            {
                prop.Slot = x;
                dmgVals.add(prop);
            }
        }
        if (dmgVals.size() > 0)
        {
            ArmorProperties[] props = dmgVals.toArray(new ArmorProperties[dmgVals.size()]);
            int level = props[0].Priority;
            double ratio = 0;
            for (ArmorProperties prop : props)
            {
                if (level != prop.Priority)
                {
                    damage -= (damage * ratio);
                    ratio = 0;
                    level = prop.Priority;
                }
                ratio += prop.AbsorbRatio;

            }
            damage -= (damage * ratio);
        }

        return (float) (damage / 25.0F);
    }

    public static float applyPotionDamageCalculations(EntityLivingBase attackedEntity, DamageSource source, float damage)
    {
        Potion resistance = MobEffects.RESISTANCE;

        if (source.isDamageAbsolute())
        {
            return damage;
        } else
        {
            if (attackedEntity.isPotionActive(resistance) && source != DamageSource.outOfWorld)
            {
                int i = (attackedEntity.getActivePotionEffect(resistance).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = damage * (float) j;
                damage = f / 25.0F;
            }

            if (damage <= 0.0F)
            {
                return 0.0F;
            } else
            {
                int k = EnchantmentHelper.getEnchantmentModifierDamage(attackedEntity.getArmorInventoryList(), source);

                if (k > 20)
                {
                    k = 20;
                }

                if (k > 0 && k <= 20)
                {
                    int l = 25 - k;
                    float f1 = damage * (float) l;
                    damage = f1 / 25.0F;
                }

                return damage;
            }
        }
    }

    /**
     * Used to determine if stack1 can be placed into stack2. If stack2 is null
     * and stack1 isn't null, returns true. Ignores stack size
     * 
     * @param stack1
     *        Stack that is placed into a slot
     * @param stack2
     *        Slot content that stack1 is placed into
     * @return True if they can be combined
     */
    public static boolean canCombine(ItemStack stack1, ItemStack stack2)
    {
        if (stack1 == null)
        {
            return false;
        }

        if (stack2 == null)
        {
            return true;
        }

        if (stack1.isItemStackDamageable() ^ stack2.isItemStackDamageable())
        {
            return false;
        }

        return stack1.getItem() == stack2.getItem() && stack1.getItemDamage() == stack2.getItemDamage() && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    /**
     * @param stack1
     *        Stack that is placed into a slot
     * @param stack2
     *        Slot content that stack1 is placed into
     * @return Stacks after stacking
     */
    public static ItemStack[] combineStacks(ItemStack stack1, ItemStack stack2, int transferMax)
    {
        ItemStack[] returned = new ItemStack[2];

        if (canCombine(stack1, stack2))
        {
            int transferedAmount = Math.min(transferMax, stack2 == null ? stack1.stackSize : Math.min(stack2.getMaxStackSize() - stack2.stackSize, stack1.stackSize));
            if (transferedAmount > 0)
            {
                ItemStack copyStack = stack1.splitStack(transferedAmount);
                if (stack2 == null)
                {
                    stack2 = copyStack;
                } else
                {
                    stack2.stackSize += transferedAmount;
                }
            }
        }

        returned[0] = stack1;
        returned[1] = stack2;

        return returned;
    }

    /**
     * @param stack1
     *        Stack that is placed into a slot
     * @param stack2
     *        Slot content that stack1 is placed into
     * @return Stacks after stacking
     */
    public static ItemStack[] combineStacks(ItemStack stack1, ItemStack stack2)
    {
        ItemStack[] returned = new ItemStack[2];

        if (canCombine(stack1, stack2))
        {
            int transferedAmount = stack2 == null ? stack1.stackSize : Math.min(stack2.getMaxStackSize() - stack2.stackSize, stack1.stackSize);
            if (transferedAmount > 0)
            {
                ItemStack copyStack = stack1.splitStack(transferedAmount);
                if (stack2 == null)
                {
                    stack2 = copyStack;
                } else
                {
                    stack2.stackSize += transferedAmount;
                }
            }
        }

        returned[0] = stack1;
        returned[1] = stack2;

        return returned;
    }

    public static ItemStack insertStackIntoTile(ItemStack stack, TileEntity tile, EnumFacing dir)
    {
        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir))
        {
            IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);

            return insertStackIntoTile(stack, handler);
        } else if (tile instanceof IInventory)
        {
            return insertStackIntoInventory(stack, (IInventory) tile, dir);
        }

        return stack;
    }

    public static int getNumberOfFreeSlots(TileEntity tile, EnumFacing dir)
    {
        int slots = 0;

        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir))
        {
            IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);

            for (int i = 0; i < handler.getSlots(); i++)
            {
                if (handler.getStackInSlot(i) == null)
                {
                    slots++;
                }
            }
        } else if (tile instanceof IInventory)
        {
            for (int i = 0; i < ((IInventory) tile).getSizeInventory(); i++)
            {
                if (((IInventory) tile).getStackInSlot(i) == null)
                {
                    slots++;
                }
            }
        }

        return slots;
    }

    public static ItemStack insertStackIntoTile(ItemStack stack, IItemHandler handler)
    {
        int numberOfSlots = handler.getSlots();

        ItemStack copyStack = stack.copy();

        for (int slot = 0; slot < numberOfSlots; slot++)
        {
            copyStack = handler.insertItem(slot, copyStack, false);
            if (copyStack == null)
            {
                return null;
            }
        }

        return copyStack;
    }

    /**
     * Inserts the desired stack into the tile up to a limit for the tile.
     * Respects capabilities.
     * 
     * @param stack
     * @param tile
     * @param dir
     * @param limit
     * @return
     */
    public static ItemStack insertStackIntoTile(ItemStack stack, TileEntity tile, EnumFacing dir, int limit)
    {
        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir))
        {
            IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);
            int numberOfSlots = handler.getSlots();

            ItemStack copyStack = stack.copy();

            int numberMatching = 0;

            for (int slot = 0; slot < numberOfSlots; slot++)
            {
                ItemStack invStack = handler.getStackInSlot(slot);

                if (invStack != null && canCombine(stack, invStack))
                {
                    numberMatching += invStack.stackSize;
                }
            }

            if (numberMatching >= limit)
            {
                return stack;
            }

            int newLimit = limit - numberMatching;

            for (int slot = 0; slot < numberOfSlots; slot++)
            {
                ItemStack newCopyStack = copyStack.copy();
                newCopyStack.stackSize = Math.min(copyStack.stackSize, newLimit);

                newCopyStack = handler.insertItem(slot, newCopyStack, false);

                if (newCopyStack == null)
                {
                    return null;
                }

                newLimit -= (copyStack.stackSize - newCopyStack.stackSize);

                if (newLimit <= 0)
                {
                    return null; //TODO
                }

                copyStack.stackSize -= (copyStack.stackSize - newCopyStack.stackSize);
            }

            return copyStack;
        } else if (tile instanceof IInventory)
        {
            return insertStackIntoInventory(stack, (IInventory) tile, dir, limit);
        }

        return stack;
    }

    public static ItemStack insertStackIntoInventory(ItemStack stack, IInventory inventory, EnumFacing dir)
    {
        if (stack == null)
        {
            return null;
        }

        boolean[] canBeInserted = new boolean[inventory.getSizeInventory()];

        if (inventory instanceof ISidedInventory)
        {
            int[] array = ((ISidedInventory) inventory).getSlotsForFace(dir);
            for (int in : array)
            {
                canBeInserted[in] = inventory.isItemValidForSlot(in, stack) && ((ISidedInventory) inventory).canInsertItem(in, stack, dir);
            }
        } else
        {
            for (int i = 0; i < canBeInserted.length; i++)
            {
                canBeInserted[i] = inventory.isItemValidForSlot(i, stack);
            }
        }

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if (!canBeInserted[i])
            {
                continue;
            }

            ItemStack[] combinedStacks = combineStacks(stack, inventory.getStackInSlot(i));
            stack = combinedStacks[0];
            inventory.setInventorySlotContents(i, combinedStacks[1]);

            if (stack.stackSize <= 0)
            {
                return stack;
            }
        }

        return stack;
    }

    public static boolean canInsertStackFullyIntoInventory(ItemStack stack, IInventory inventory, EnumFacing dir)
    {
        return canInsertStackFullyIntoInventory(stack, inventory, dir, false, 0);
    }

    public static boolean canInsertStackFullyIntoInventory(ItemStack stack, IInventory inventory, EnumFacing dir, boolean fillToLimit, int limit)
    {
        if (stack == null)
        {
            return true;
        }

        int itemsLeft = stack.stackSize;

        boolean[] canBeInserted = new boolean[inventory.getSizeInventory()];

        if (inventory instanceof ISidedInventory)
        {
            int[] array = ((ISidedInventory) inventory).getSlotsForFace(dir);
            for (int in : array)
            {
                canBeInserted[in] = inventory.isItemValidForSlot(in, stack) && ((ISidedInventory) inventory).canInsertItem(in, stack, dir);
            }
        } else
        {
            for (int i = 0; i < canBeInserted.length; i++)
            {
                canBeInserted[i] = inventory.isItemValidForSlot(i, stack);
            }
        }

        int numberMatching = 0;

        if (fillToLimit)
        {
            for (int i = 0; i < inventory.getSizeInventory(); i++)
            {
                if (!canBeInserted[i])
                {
                    continue;
                }

                ItemStack invStack = inventory.getStackInSlot(i);

                if (invStack != null && canCombine(stack, invStack))
                {
                    numberMatching += invStack.stackSize;
                }
            }
        }

        if (fillToLimit && limit < stack.stackSize + numberMatching)
        {
            return false;
        }

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if (!canBeInserted[i])
            {
                continue;
            }

            ItemStack invStack = inventory.getStackInSlot(i);
            boolean canCombine = canCombine(stack, invStack);
            if (canCombine)
            {
                if (invStack == null)
                {
                    itemsLeft = 0;
                } else
                {
                    itemsLeft -= (invStack.getMaxStackSize() - invStack.stackSize);
                }
            }

            if (itemsLeft <= 0)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Inserts the desired stack into the inventory up to a limit for the
     * inventory.
     * 
     * @param stack
     * @param inventory
     * @param dir
     * @param limit
     * @return
     */
    public static ItemStack insertStackIntoInventory(ItemStack stack, IInventory inventory, EnumFacing dir, int limit)
    {
        if (stack == null)
        {
            return null;
        }

        boolean[] canBeInserted = new boolean[inventory.getSizeInventory()];

        if (inventory instanceof ISidedInventory)
        {
            int[] array = ((ISidedInventory) inventory).getSlotsForFace(dir);
            for (int in : array)
            {
                canBeInserted[in] = ((ISidedInventory) inventory).canInsertItem(in, stack, dir);
            }
        } else
        {
            for (int i = 0; i < canBeInserted.length; i++)
            {
                canBeInserted[i] = true;
            }
        }

        int numberMatching = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if (!canBeInserted[i])
            {
                continue;
            }

            ItemStack invStack = inventory.getStackInSlot(i);

            if (invStack != null && canCombine(stack, invStack))
            {
                numberMatching += invStack.stackSize;
            }
        }

        if (numberMatching >= limit)
        {
            return stack;
        }

        int newLimit = limit - numberMatching;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if (!canBeInserted[i])
            {
                continue;
            }

            int prevStackSize = stack.stackSize;

            ItemStack[] combinedStacks = combineStacks(stack, inventory.getStackInSlot(i), newLimit);
            stack = combinedStacks[0];
            inventory.setInventorySlotContents(i, combinedStacks[1]); //TODO

            newLimit -= (prevStackSize - stack.stackSize);

            if (newLimit <= 0 || stack.stackSize <= 0)
            {
                return stack;
            }
        }

        return stack;
    }

    public static boolean isBlockLiquid(IBlockState state)
    {
        return (state instanceof IFluidBlock || state.getMaterial().isLiquid());
    }

    public static boolean isFlowingLiquid(World world, BlockPos pos, IBlockState state)
    {
        Block block = state.getBlock();
        return ((block instanceof IFluidBlock && Math.abs(((IFluidBlock) block).getFilledPercentage(world, pos)) == 1) || (block instanceof BlockLiquid && block.getMetaFromState(state) != 0));
    }

    public static boolean spawnStackAtBlock(World world, BlockPos pos, @Nullable EnumFacing pushDirection, ItemStack stack)
    {
        EntityItem entityItem = new EntityItem(world);
        BlockPos spawnPos = new BlockPos(pos);
        double velocity = 0.15D;
        if (pushDirection != null)
        {
            spawnPos.offset(pushDirection);

            switch (pushDirection)
            {
            case DOWN:
            {
                entityItem.motionY = -velocity;
                entityItem.setPosition(spawnPos.getX() + 0.5D, spawnPos.getY() - 1.0D, spawnPos.getZ() + 0.5D);
                break;
            }
            case UP:
            {
                entityItem.motionY = velocity;
                entityItem.setPosition(spawnPos.getX() + 0.5D, spawnPos.getY() + 1.0D, spawnPos.getZ() + 0.5D);
                break;
            }
            case NORTH:
            {
                entityItem.motionZ = -velocity;
                entityItem.setPosition(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.5D, spawnPos.getZ() - 1.0D);
                break;
            }
            case SOUTH:
            {
                entityItem.motionZ = velocity;
                entityItem.setPosition(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.5D, spawnPos.getZ() + 1.0D);
                break;
            }
            case WEST:
            {
                entityItem.motionX = -velocity;
                entityItem.setPosition(spawnPos.getX() - 1.0D, spawnPos.getY() + 0.5D, spawnPos.getZ() + 0.5D);
                break;
            }
            case EAST:
            {
                entityItem.motionX = velocity;
                entityItem.setPosition(spawnPos.getX() + 1.0D, spawnPos.getY() + 0.5D, spawnPos.getZ() + 0.5D);
                break;
            }
            }
        }

        entityItem.setEntityItemStack(stack);
        return world.spawnEntityInWorld(entityItem);
    }

    public static boolean swapLocations(World initialWorld, BlockPos initialPos, World finalWorld, BlockPos finalPos)
    {
        TileEntity initialTile = initialWorld.getTileEntity(initialPos);
        TileEntity finalTile = finalWorld.getTileEntity(finalPos);
        NBTTagCompound initialTag = new NBTTagCompound();
        NBTTagCompound finalTag = new NBTTagCompound();
        if (initialTile != null)
            initialTile.writeToNBT(initialTag);
        if (finalTile != null)
            finalTile.writeToNBT(finalTag);

        BlockStack initialStack = BlockStack.getStackFromPos(initialWorld, initialPos);
        BlockStack finalStack = BlockStack.getStackFromPos(finalWorld, finalPos);

        if ((initialStack.getBlock().equals(Blocks.AIR) && finalStack.getBlock().equals(Blocks.AIR)) || initialStack.getBlock() instanceof BlockPortal || finalStack.getBlock() instanceof BlockPortal)
            return false;

        initialWorld.playSound(initialPos.getX(), initialPos.getY(), initialPos.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
        finalWorld.playSound(finalPos.getX(), finalPos.getY(), finalPos.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);

        //Finally, we get to do something! (CLEARING TILES)
        if (finalStack.getBlock() != null)
            finalWorld.removeTileEntity(finalPos);
        if (initialStack.getBlock() != null)
            initialWorld.removeTileEntity(initialPos);

        //TILES CLEARED
        IBlockState initialBlockState = initialWorld.getBlockState(initialPos);
        IBlockState finalBlockState = finalWorld.getBlockState(finalPos);
        finalWorld.setBlockState(finalPos, initialBlockState, 3);

        if (initialTile != null)
        {
            TileEntity newTileInitial = BloodMagic.getCrossVersionProxy().createTileFromData(finalWorld, initialTag);

            finalWorld.setTileEntity(finalPos, newTileInitial);
            newTileInitial.setPos(finalPos);
            newTileInitial.setWorldObj(finalWorld);
        }

        initialWorld.setBlockState(initialPos, finalBlockState, 3);

        if (finalTile != null)
        {
            TileEntity newTileFinal = BloodMagic.getCrossVersionProxy().createTileFromData(initialWorld, finalTag);

            initialWorld.setTileEntity(initialPos, newTileFinal);
            newTileFinal.setPos(initialPos);
            newTileFinal.setWorldObj(initialWorld);
        }

        initialWorld.notifyNeighborsOfStateChange(initialPos, finalStack.getBlock());
        finalWorld.notifyNeighborsOfStateChange(finalPos, initialStack.getBlock());

        return true;
    }

    //Shamelessly ripped off of CoFH Lib
    public static boolean fillContainerFromHandler(World world, IFluidHandler handler, EntityPlayer player, FluidStack tankFluid)
    {
        ItemStack container = player.getHeldItemMainhand();
        if (FluidContainerRegistry.isEmptyContainer(container))
        {
            ItemStack returnStack = FluidContainerRegistry.fillFluidContainer(tankFluid, container);
            FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(returnStack);
            if (fluid == null || returnStack == null)
            {
                return false;
            }
            if (!player.capabilities.isCreativeMode)
            {
                if (container.stackSize == 1)
                {
                    container = container.copy();
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, returnStack);
                } else if (!player.inventory.addItemStackToInventory(returnStack))
                {
                    return false;
                }
                handler.drain(EnumFacing.UP, fluid.amount, true);
                container.stackSize--;
                if (container.stackSize <= 0)
                {
                    container = null;
                }
            } else
            {
                handler.drain(EnumFacing.UP, fluid.amount, true);
            }
            return true;
        }
        return false;
    }

    //Shamelessly ripped off of CoFH Lib
    public static boolean fillHandlerWithContainer(World world, IFluidHandler handler, EntityPlayer player)
    {
        ItemStack container = player.getHeldItemMainhand();
        FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(container);
        if (fluid != null)
        {
            if (handler.fill(EnumFacing.UP, fluid, false) == fluid.amount || player.capabilities.isCreativeMode)
            {
                if (world.isRemote)
                {
                    return true;
                }
                handler.fill(EnumFacing.UP, fluid, true);
                if (!player.capabilities.isCreativeMode)
                {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(container));
                }
                return true;
            }
        }
        return false;
    }

    //Shamelessly ripped off of CoFH Lib
    public static ItemStack consumeItem(ItemStack stack)
    {
        Item item = stack.getItem();
        boolean largerStack = stack.stackSize > 1;
        if (largerStack)
        {
            stack.stackSize -= 1;
        }
        if (item.hasContainerItem(stack))
        {
            ItemStack ret = item.getContainerItem(stack);
            if (ret == null)
            {
                return null;
            }
            if (ret.isItemStackDamageable() && ret.getItemDamage() > ret.getMaxDamage())
            {
                ret = null;
            }
            return ret;
        }
        return largerStack ? stack : null;
    }

    public static void registerHandlers(Set<ASMDataTable.ASMData> eventHandlers)
    {
        for (ASMDataTable.ASMData data : eventHandlers)
        {
            try
            {
                Class<?> handlerClass = Class.forName(data.getClassName());
                Object handlerImpl = handlerClass.newInstance();
                MinecraftForge.EVENT_BUS.register(handlerImpl);
                BloodMagic.instance.getLogger().debug("Registering event handler for class {}", data.getClassName());
            } catch (Exception e)
            {
                // No-op
            }
        }
    }

    public static boolean hasUUID(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(Constants.NBT.MOST_SIG) && stack.getTagCompound().hasKey(Constants.NBT.LEAST_SIG);
    }

    public static UUID getUUID(ItemStack stack)
    {
        if (!hasUUID(stack))
        {
            return null;
        }

        return new UUID(stack.getTagCompound().getLong(Constants.NBT.MOST_SIG), stack.getTagCompound().getLong(Constants.NBT.LEAST_SIG));
    }

    public static void setUUID(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);

        if (!stack.getTagCompound().hasKey(Constants.NBT.MOST_SIG) && !stack.getTagCompound().hasKey(Constants.NBT.LEAST_SIG))
        {
            UUID itemUUID = UUID.randomUUID();
            stack.getTagCompound().setLong(Constants.NBT.MOST_SIG, itemUUID.getMostSignificantBits());
            stack.getTagCompound().setLong(Constants.NBT.LEAST_SIG, itemUUID.getLeastSignificantBits());
        }
    }
}
