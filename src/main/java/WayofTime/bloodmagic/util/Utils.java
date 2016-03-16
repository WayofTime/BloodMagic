package WayofTime.bloodmagic.util;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.tile.TileInventory;

public class Utils
{
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
        if (tile.getStackInSlot(slot) == null && player.getHeldItem() != null)
        {
            ItemStack input = player.getHeldItem().copy();
            input.stackSize = 1;
            player.getHeldItem().stackSize--;
            tile.setInventorySlotContents(slot, input);
            return true;
        } else if (tile.getStackInSlot(slot) != null && player.getHeldItem() == null)
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
            return Blocks.glowstone;
        case BLOODSTONE:
            return ModBlocks.bloodStoneBrick;
        case BEACON:
            return Blocks.beacon;
        case BLOODRUNE:
            return ModBlocks.bloodRune;
        case CRYSTAL:
            return ModBlocks.crystal;
        case NOTAIR:
            return Blocks.stonebrick;
        default:
            return Blocks.air;
        }
    }

    public static float getModifiedDamage(EntityLivingBase attackedEntity, DamageSource source, float amount)
    {
        if (!attackedEntity.isEntityInvulnerable(source))
        {
            if (amount <= 0)
                return 0;

            amount = applyArmor(attackedEntity, attackedEntity.getInventory(), source, amount);
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
        if (source.isDamageAbsolute())
        {
            return damage;
        } else
        {
            if (attackedEntity.isPotionActive(Potion.resistance) && source != DamageSource.outOfWorld)
            {
                int i = (attackedEntity.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = damage * (float) j;
                damage = f / 25.0F;
            }

            if (damage <= 0.0F)
            {
                return 0.0F;
            } else
            {
                int k = EnchantmentHelper.getEnchantmentModifierDamage(attackedEntity.getInventory(), source);

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
            inventory.setInventorySlotContents(i, combinedStacks[1]);

            newLimit -= (prevStackSize - stack.stackSize);

            if (newLimit <= 0 || stack.stackSize <= 0)
            {
                return stack;
            }
        }

        return stack;
    }

    public static boolean isBlockLiquid(Block block)
    {
        return (block instanceof IFluidBlock || block.getMaterial().isLiquid());
    }

    //Shamelessly ripped off of CoFH Lib
    public static boolean fillContainerFromHandler(World world, IFluidHandler handler, EntityPlayer player, FluidStack tankFluid)
    {
        ItemStack container = player.getCurrentEquippedItem();
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
        ItemStack container = player.getCurrentEquippedItem();
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
}
