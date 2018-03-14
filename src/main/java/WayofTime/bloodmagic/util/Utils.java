package WayofTime.bloodmagic.util;

import WayofTime.bloodmagic.altar.ComponentType;
import WayofTime.bloodmagic.iface.IDemonWillViewer;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.PlayerVelocityPacketProcessor;
import WayofTime.bloodmagic.tile.TileInventory;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;

public class Utils {

    public static final EnumMap<EnumDyeColor, Integer> DYE_COLOR_VALUES = Maps.newEnumMap(EnumDyeColor.class);

    static {
        try {
            Field colorValue = ReflectionHelper.findField(EnumDyeColor.class, "field_193351_w", "colorValue");
            colorValue.setAccessible(true);
            for (EnumDyeColor color : EnumDyeColor.values()) {
                DYE_COLOR_VALUES.put(color, (int) colorValue.get(color));
            }
        } catch (IllegalAccessException e) {
            BMLog.DEFAULT.error("Error grabbing color values: {}", e.getMessage());
        }
    }

    public static float addAbsorptionToMaximum(EntityLivingBase entity, float added, int maximum, int duration) {
        float currentAmount = entity.getAbsorptionAmount();
        added = Math.min(maximum - currentAmount, added);

        if (added <= 0) {
            return 0;
        }

        if (duration > 0) {
            int potionLevel = (int) ((currentAmount + added) / 4);
            entity.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, duration, potionLevel, true, false));
        }

        entity.setAbsorptionAmount(currentAmount + added);

        return added;
    }

    public static boolean isImmuneToFireDamage(EntityLivingBase entity) {
        return entity.isImmuneToFire() || entity.isPotionActive(MobEffects.FIRE_RESISTANCE);
    }

    public static boolean isPlayerBesideSolidBlockFace(EntityPlayer player) {
        World world = player.getEntityWorld();
        double minimumDistanceFromAxis = 0.7;
        BlockPos centralPos = player.getPosition();
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos offsetPos = centralPos.offset(facing);
            double distance = Math.min(offsetPos.getX() + 0.5 - player.posX, offsetPos.getZ() + 0.5 - player.posZ);
            if (distance > minimumDistanceFromAxis) {
                continue;
            }
            IBlockState state = world.getBlockState(offsetPos);
            if (state.isSideSolid(world, offsetPos, facing.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    public static boolean canPlayerSeeDemonWill(EntityPlayer player) {
        IItemHandler inventory = new PlayerMainInvWrapper(player.inventory);

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) stack.getItem()).canSeeDemonWillAura(player.getEntityWorld(), stack, player)) {
                return true;
            }
        }

        return false;
    }

    public static boolean canEntitySeeBlock(World world, Entity entity, BlockPos pos) {
        Vec3d relativePosition = new Vec3d(entity.posX - pos.getX() - 0.5, entity.posY + (double) entity.getEyeHeight() - pos.getY() - 0.5, entity.posZ - pos.getZ() - 0.5);
        EnumFacing dir = EnumFacing.getFacingFromVector((float) relativePosition.x, (float) relativePosition.y, (float) relativePosition.z);
        RayTraceResult result = world.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ), new Vec3d(pos.getX() + 0.5 + dir.getFrontOffsetX() * 0.4, pos.getY() + 0.5 + dir.getFrontOffsetY() * 0.4, pos.getZ() + 0.5 + dir.getFrontOffsetZ() * 0.4), false, true, true);
        return result == null || pos.equals(result.getBlockPos());
    }

    public static int plantSeedsInArea(World world, AxisAlignedBB aabb, int horizontalRadius, int verticalRadius) {
        int placedBlocks = 0;
        List<EntityItem> itemEntities = world.getEntitiesWithinAABB(EntityItem.class, aabb);

        for (EntityItem itemEntity : itemEntities) {
            placedBlocks += plantEntityItem(itemEntity, horizontalRadius, verticalRadius);
        }

        return placedBlocks;
    }

    public static int plantItemStack(World world, BlockPos centralPos, ItemStack stack, int horizontalRadius, int verticalRadius) {
        if (stack.isEmpty()) {
            return 0;
        }

        Item item = stack.getItem();
        if (!(item instanceof IPlantable)) {
            return 0;
        }

        int planted = 0;

        for (int hR = 0; hR <= horizontalRadius; hR++) {
            for (int vR = 0; vR <= verticalRadius; vR++) {
                for (int i = -hR; i <= hR; i++) {
                    for (int k = -hR; k <= hR; k++) {
                        for (int j = -vR; j <= vR; j += 2 * vR + (vR > 0 ? 0 : 1)) {
                            if (!(Math.abs(i) == hR || Math.abs(k) == hR)) {
                                continue;
                            }

                            BlockPos newPos = centralPos.add(i, j, k);
                            if (world.isAirBlock(newPos)) {
                                BlockPos offsetPos = newPos.offset(EnumFacing.DOWN);
                                IBlockState state = world.getBlockState(offsetPos);
                                if (state.getBlock().canSustainPlant(state, world, offsetPos, EnumFacing.UP, (IPlantable) item)) {
                                    IBlockState plantState = ((IPlantable) item).getPlant(world, newPos);
                                    world.setBlockState(newPos, plantState, 3);
                                    world.playEvent(2001, newPos, Block.getIdFromBlock(plantState.getBlock()) + (plantState.getBlock().getMetaFromState(plantState) << 12));
                                    stack.shrink(1);
                                    planted++;
                                    if (stack.isEmpty() || stack.getCount() <= 0) {
                                        return planted;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return planted;
    }

    public static int plantEntityItem(EntityItem itemEntity, int horizontalRadius, int verticalRadius) {
        if (itemEntity == null || itemEntity.isDead) {
            return 0;
        }

        World world = itemEntity.getEntityWorld();
        BlockPos pos = itemEntity.getPosition();
        ItemStack stack = itemEntity.getItem();

        int planted = plantItemStack(world, pos, stack, horizontalRadius, verticalRadius);

        if (stack.isEmpty()) {
            itemEntity.setDead();
        }

        return planted;
    }

    public static int getDemonWillResolution(EntityPlayer player) {
        IItemHandler inventory = new PlayerMainInvWrapper(player.inventory);

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) stack.getItem()).canSeeDemonWillAura(player.getEntityWorld(), stack, player)) {
                return ((IDemonWillViewer) stack.getItem()).getDemonWillAuraResolution(player.getEntityWorld(), stack, player);
            }
        }

        return 1;
    }

    public static NBTTagCompound getPersistentDataTag(EntityPlayer player) {
        NBTTagCompound forgeData = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        NBTTagCompound beaconData = forgeData.getCompoundTag("BloodMagic");

        //Creates/sets the tags if they don't exist 
        if (!forgeData.hasKey("BloodMagic"))
            forgeData.setTag("BloodMagic", beaconData);
        if (!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG))
            player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, forgeData);

        return beaconData;
    }

    public static void setPlayerSpeedFromServer(EntityPlayer player, double motionX, double motionY, double motionZ) {
        if (!player.getEntityWorld().isRemote && player instanceof EntityPlayerMP) {
            BloodMagicPacketHandler.sendTo(new PlayerVelocityPacketProcessor(motionX, motionY, motionZ), (EntityPlayerMP) player);
        }
    }

    public static boolean isInteger(String integer) {
        try {
            Integer.parseInt(integer);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static String toFancyCasing(String input) {
        return String.valueOf(input.charAt(0)).toUpperCase(Locale.ENGLISH) + input.substring(1);
    }

    public static String prettifyBlockPosString(BlockPos pos) {
        return "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]";
    }

    /**
     * @param tile   - The {@link TileInventory} to input the item to
     * @param player - The player to take the item from.
     * @return {@code true} if the ItemStack is inserted, {@code false}
     * otherwise
     * @see #insertItemToTile(TileInventory, EntityPlayer, int)
     */
    public static boolean insertItemToTile(TileInventory tile, EntityPlayer player) {
        return insertItemToTile(tile, player, 0);
    }

    /**
     * Used for inserting an ItemStack with a stacksize of 1 to a tile's
     * inventory at slot 0
     * <p/>
     * EG: Block Altar
     *
     * @param tile   - The {@link TileInventory} to input the item to
     * @param player - The player to take the item from.
     * @param slot   - The slot to attempt to insert to
     * @return {@code true} if the ItemStack is inserted, {@code false}
     * otherwise
     */
    public static boolean insertItemToTile(TileInventory tile, EntityPlayer player, int slot) {
        ItemStack slotStack = tile.getStackInSlot(slot);
        if (slotStack.isEmpty() && !player.getHeldItemMainhand().isEmpty()) {
            ItemStack input = player.getHeldItemMainhand().copy();
            input.setCount(1);
            player.getHeldItemMainhand().shrink(1);
            tile.setInventorySlotContents(slot, input);
            return true;
        } else if (!slotStack.isEmpty() && player.getHeldItemMainhand().isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, slotStack);
            tile.clear();
            return false;
        }

        return false;
    }

    public static double calculateStandardProgress(Number currentValue, int[] requiredValues, int currentLevel) {
        int nextLevel = currentLevel + 1;
        if (nextLevel >= requiredValues.length)
            return 1.0D;

        int required = requiredValues[nextLevel];
        return Double.parseDouble("" + currentValue) / (double) required;
    }

    @Nullable
    public static IItemHandler getInventory(TileEntity tile, @Nullable EnumFacing facing) {
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

    public static ItemStack setUnbreakable(ItemStack stack) {
        NBTHelper.checkNBT(stack);
        stack.getTagCompound().setBoolean("Unbreakable", true);
        return stack;
    }

    /**
     * Gets a default block for each type of {@link ComponentType}
     *
     * @param component - The Component to provide a block for.
     * @return The default Block for the EnumAltarComponent
     */
    public static Block getBlockForComponent(ComponentType component) {
        switch (component) {
            case GLOWSTONE:
                return Blocks.GLOWSTONE;
            case BLOODSTONE:
                return RegistrarBloodMagicBlocks.DECORATIVE_BRICK;
            case BEACON:
                return Blocks.BEACON;
            case BLOODRUNE:
                return RegistrarBloodMagicBlocks.BLOOD_RUNE;
            case CRYSTAL:
                return RegistrarBloodMagicBlocks.BLOOD_RUNE;
            case NOTAIR:
                return Blocks.STONEBRICK;
            default:
                return Blocks.AIR;
        }
    }

    public static float getModifiedDamage(EntityLivingBase attackedEntity, DamageSource source, float amount) {
        if (!attackedEntity.isEntityInvulnerable(source)) {
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

    public static float applyArmor(EntityLivingBase entity, ItemStack[] inventory, DamageSource source, double damage) {
        damage *= 25;
        ArrayList<ArmorProperties> dmgVals = new ArrayList<>();
        for (int x = 0; x < inventory.length; x++) {
            ItemStack stack = inventory[x];
            if (stack.isEmpty()) {
                continue;
            }
            ArmorProperties prop = null;
            if (stack.getItem() instanceof ISpecialArmor) {
                ISpecialArmor armor = (ISpecialArmor) stack.getItem();
                prop = armor.getProperties(entity, stack, source, damage / 25D, x).copy();
            } else if (stack.getItem() instanceof ItemArmor && !source.isUnblockable()) {
                ItemArmor armor = (ItemArmor) stack.getItem();
                prop = new ArmorProperties(0, armor.damageReduceAmount / 25D, Integer.MAX_VALUE);
            }
            if (prop != null) {
                prop.Slot = x;
                dmgVals.add(prop);
            }
        }
        if (dmgVals.size() > 0) {
            ArmorProperties[] props = dmgVals.toArray(new ArmorProperties[dmgVals.size()]);
            int level = props[0].Priority;
            double ratio = 0;
            for (ArmorProperties prop : props) {
                if (level != prop.Priority) {
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

    public static float applyPotionDamageCalculations(EntityLivingBase attackedEntity, DamageSource source, float damage) {
        Potion resistance = MobEffects.RESISTANCE;

        if (source.isDamageAbsolute()) {
            return damage;
        } else {
            if (attackedEntity.isPotionActive(resistance) && source != DamageSource.OUT_OF_WORLD) {
                int i = (attackedEntity.getActivePotionEffect(resistance).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = damage * (float) j;
                damage = f / 25.0F;
            }

            if (damage <= 0.0F) {
                return 0.0F;
            } else {
                int k = EnchantmentHelper.getEnchantmentModifierDamage(attackedEntity.getArmorInventoryList(), source);

                if (k > 20) {
                    k = 20;
                }

                if (k > 0 && k <= 20) {
                    int l = 25 - k;
                    float f1 = damage * (float) l;
                    damage = f1 / 25.0F;
                }

                return damage;
            }
        }
    }

    /**
     * Used to determine if stack1 can be placed into stack2. If stack2 is is empty
     * and stack1 isn't empty, returns true. Ignores stack size
     *
     * @param stack1 Stack that is placed into a slot
     * @param stack2 Slot content that stack1 is placed into
     * @return True if they can be combined
     * @deprecated use {@link ItemHandlerHelper#canItemStacksStack(ItemStack, ItemStack)}
     */
    @Deprecated
    public static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return stack1.isEmpty() && !stack2.isEmpty() || ItemHandlerHelper.canItemStacksStack(stack1, stack2);
    }

    /**
     * @param stack1 Stack that is placed into a slot
     * @param stack2 Slot content that stack1 is placed into
     * @return Stacks after stacking
     */
    public static ItemStack[] combineStacks(ItemStack stack1, ItemStack stack2, int transferMax) {
        ItemStack[] returned = new ItemStack[2];

        if (ItemHandlerHelper.canItemStacksStack(stack1, stack2)) {
            int transferedAmount = Math.min(transferMax, stack2.isEmpty() ? stack1.getCount() : Math.min(stack2.getMaxStackSize() - stack2.getCount(), stack1.getCount()));
            if (transferedAmount > 0) {
                ItemStack copyStack = stack1.splitStack(transferedAmount);
                if (stack2.isEmpty()) {
                    stack2 = copyStack;
                } else {
                    stack2.grow(transferedAmount);
                }
            }
        }

        returned[0] = stack1;
        returned[1] = stack2;

        return returned;
    }

    /**
     * @param stack1 Stack that is placed into a slot
     * @param stack2 Slot content that stack1 is placed into
     * @return Stacks after stacking
     */
    public static ItemStack[] combineStacks(ItemStack stack1, ItemStack stack2) {
        ItemStack[] returned = new ItemStack[2];

        if (ItemHandlerHelper.canItemStacksStack(stack1, stack2)) {
            int transferedAmount = stack2.isEmpty() ? stack1.getCount() : Math.min(stack2.getMaxStackSize() - stack2.getCount(), stack1.getCount());
            if (transferedAmount > 0) {
                ItemStack copyStack = stack1.splitStack(transferedAmount);
                if (stack2.isEmpty()) {
                    stack2 = copyStack;
                } else {
                    stack2.grow(transferedAmount);
                }
            }
        }

        returned[0] = stack1;
        returned[1] = stack2;

        return returned;
    }

    public static ItemStack insertStackIntoTile(ItemStack stack, TileEntity tile, EnumFacing dir) {
        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir)) {
            IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);

            return insertStackIntoTile(stack, handler);
        } else if (tile instanceof IInventory) {
            return insertStackIntoInventory(stack, (IInventory) tile, dir);
        }

        return stack;
    }

    public static int getNumberOfFreeSlots(TileEntity tile, EnumFacing dir) {
        int slots = 0;

        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir)) {
            IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);

            for (int i = 0; i < handler.getSlots(); i++) {
                if (handler.getStackInSlot(i).isEmpty()) {
                    slots++;
                }
            }
        } else if (tile instanceof IInventory) {
            for (int i = 0; i < ((IInventory) tile).getSizeInventory(); i++) {
                if (((IInventory) tile).getStackInSlot(i).isEmpty()) {
                    slots++;
                }
            }
        }

        return slots;
    }

    public static ItemStack insertStackIntoTile(ItemStack stack, IItemHandler handler) {
        int numberOfSlots = handler.getSlots();

        ItemStack copyStack = stack.copy();

        for (int slot = 0; slot < numberOfSlots; slot++) {
            copyStack = handler.insertItem(slot, copyStack, false);
            if (copyStack.isEmpty()) {
                return ItemStack.EMPTY;
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
    public static ItemStack insertStackIntoTile(ItemStack stack, TileEntity tile, EnumFacing dir, int limit) {
        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir)) {
            IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);
            int numberOfSlots = handler.getSlots();

            ItemStack copyStack = stack.copy();

            int numberMatching = 0;

            for (int slot = 0; slot < numberOfSlots; slot++) {
                ItemStack invStack = handler.getStackInSlot(slot);

                if (!invStack.isEmpty() && ItemHandlerHelper.canItemStacksStack(stack, invStack)) {
                    numberMatching += invStack.getCount();
                }
            }

            if (numberMatching >= limit) {
                return stack;
            }

            int newLimit = limit - numberMatching;

            for (int slot = 0; slot < numberOfSlots; slot++) {
                ItemStack newCopyStack = copyStack.copy();
                newCopyStack.setCount(Math.min(copyStack.getCount(), newLimit));

                newCopyStack = handler.insertItem(slot, newCopyStack, false);

                if (newCopyStack.isEmpty()) {
                    return ItemStack.EMPTY;
                }

                newLimit -= (copyStack.getCount() - newCopyStack.getCount());

                if (newLimit <= 0) {
                    return ItemStack.EMPTY; //TODO
                }

                copyStack.shrink(copyStack.getCount() - newCopyStack.getCount());
            }

            return copyStack;
        } else if (tile instanceof IInventory) {
            return insertStackIntoInventory(stack, (IInventory) tile, dir, limit);
        }

        return stack;
    }

    public static ItemStack insertStackIntoInventory(ItemStack stack, IInventory inventory, EnumFacing dir) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        boolean[] canBeInserted = new boolean[inventory.getSizeInventory()];

        if (inventory instanceof ISidedInventory) {
            int[] array = ((ISidedInventory) inventory).getSlotsForFace(dir);
            for (int in : array) {
                canBeInserted[in] = inventory.isItemValidForSlot(in, stack) && ((ISidedInventory) inventory).canInsertItem(in, stack, dir);
            }
        } else {
            for (int i = 0; i < canBeInserted.length; i++) {
                canBeInserted[i] = inventory.isItemValidForSlot(i, stack);
            }
        }

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (!canBeInserted[i]) {
                continue;
            }

            ItemStack[] combinedStacks = combineStacks(stack, inventory.getStackInSlot(i));
            stack = combinedStacks[0];
            inventory.setInventorySlotContents(i, combinedStacks[1]);

            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        return stack;
    }

    public static boolean canInsertStackFullyIntoInventory(ItemStack stack, IInventory inventory, EnumFacing dir) {
        return canInsertStackFullyIntoInventory(stack, inventory, dir, false, 0);
    }

    public static boolean canInsertStackFullyIntoInventory(ItemStack stack, IInventory inventory, EnumFacing dir, boolean fillToLimit, int limit) {
        if (stack.isEmpty()) {
            return true;
        }

        int itemsLeft = stack.getCount();

        boolean[] canBeInserted = new boolean[inventory.getSizeInventory()];

        if (inventory instanceof ISidedInventory) {
            int[] array = ((ISidedInventory) inventory).getSlotsForFace(dir);
            for (int in : array) {
                canBeInserted[in] = inventory.isItemValidForSlot(in, stack) && ((ISidedInventory) inventory).canInsertItem(in, stack, dir);
            }
        } else {
            for (int i = 0; i < canBeInserted.length; i++) {
                canBeInserted[i] = inventory.isItemValidForSlot(i, stack);
            }
        }

        int numberMatching = 0;

        if (fillToLimit) {
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                if (!canBeInserted[i]) {
                    continue;
                }

                ItemStack invStack = inventory.getStackInSlot(i);

                if (!invStack.isEmpty() && ItemHandlerHelper.canItemStacksStack(stack, invStack)) {
                    numberMatching += invStack.getCount();
                }
            }
        }

        if (fillToLimit && limit < stack.getCount() + numberMatching) {
            return false;
        }

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (!canBeInserted[i]) {
                continue;
            }

            ItemStack invStack = inventory.getStackInSlot(i);
            boolean canCombine = canCombine(stack, invStack);
            if (canCombine) {
                if (invStack.isEmpty()) {
                    itemsLeft = 0;
                } else {
                    itemsLeft -= (invStack.getMaxStackSize() - invStack.getCount());
                }
            }

            if (itemsLeft <= 0) {
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
    public static ItemStack insertStackIntoInventory(ItemStack stack, IInventory inventory, EnumFacing dir, int limit) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        boolean[] canBeInserted = new boolean[inventory.getSizeInventory()];

        if (inventory instanceof ISidedInventory) {
            int[] array = ((ISidedInventory) inventory).getSlotsForFace(dir);
            for (int in : array) {
                canBeInserted[in] = ((ISidedInventory) inventory).canInsertItem(in, stack, dir);
            }
        } else {
            for (int i = 0; i < canBeInserted.length; i++) {
                canBeInserted[i] = true;
            }
        }

        int numberMatching = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (!canBeInserted[i]) {
                continue;
            }

            ItemStack invStack = inventory.getStackInSlot(i);

            if (!invStack.isEmpty() && canCombine(stack, invStack)) {
                numberMatching += invStack.getCount();
            }
        }

        if (numberMatching >= limit) {
            return stack;
        }

        int newLimit = limit - numberMatching;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (!canBeInserted[i]) {
                continue;
            }

            int prevStackSize = stack.getCount();

            ItemStack[] combinedStacks = combineStacks(stack, inventory.getStackInSlot(i), newLimit);
            stack = combinedStacks[0];
            inventory.setInventorySlotContents(i, combinedStacks[1]); //TODO

            newLimit -= (prevStackSize - stack.getCount());

            if (newLimit <= 0 || stack.isEmpty()) {
                return stack;
            }
        }

        return stack;
    }

    public static boolean isBlockLiquid(IBlockState state) {
        return (state instanceof IFluidBlock || state.getMaterial().isLiquid());
    }

    public static boolean isFlowingLiquid(World world, BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        return ((block instanceof IFluidBlock && Math.abs(((IFluidBlock) block).getFilledPercentage(world, pos)) == 1) || (block instanceof BlockLiquid && block.getMetaFromState(state) != 0));
    }

    public static boolean spawnStackAtBlock(World world, BlockPos pos, @Nullable EnumFacing pushDirection, ItemStack stack) {
        EntityItem entityItem = new EntityItem(world);
        BlockPos spawnPos = new BlockPos(pos);
        double velocity = 0.15D;
        if (pushDirection != null) {
            spawnPos.offset(pushDirection);

            switch (pushDirection) {
                case DOWN: {
                    entityItem.motionY = -velocity;
                    entityItem.setPosition(spawnPos.getX() + 0.5D, spawnPos.getY() - 1.0D, spawnPos.getZ() + 0.5D);
                    break;
                }
                case UP: {
                    entityItem.motionY = velocity;
                    entityItem.setPosition(spawnPos.getX() + 0.5D, spawnPos.getY() + 1.0D, spawnPos.getZ() + 0.5D);
                    break;
                }
                case NORTH: {
                    entityItem.motionZ = -velocity;
                    entityItem.setPosition(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.5D, spawnPos.getZ() - 1.0D);
                    break;
                }
                case SOUTH: {
                    entityItem.motionZ = velocity;
                    entityItem.setPosition(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.5D, spawnPos.getZ() + 1.0D);
                    break;
                }
                case WEST: {
                    entityItem.motionX = -velocity;
                    entityItem.setPosition(spawnPos.getX() - 1.0D, spawnPos.getY() + 0.5D, spawnPos.getZ() + 0.5D);
                    break;
                }
                case EAST: {
                    entityItem.motionX = velocity;
                    entityItem.setPosition(spawnPos.getX() + 1.0D, spawnPos.getY() + 0.5D, spawnPos.getZ() + 0.5D);
                    break;
                }
            }
        }

        entityItem.setItem(stack);
        return world.spawnEntity(entityItem);
    }

    public static boolean swapLocations(World initialWorld, BlockPos initialPos, World finalWorld, BlockPos finalPos) {
        return swapLocations(initialWorld, initialPos, finalWorld, finalPos, true);
    }

    public static boolean swapLocations(World initialWorld, BlockPos initialPos, World finalWorld, BlockPos finalPos, boolean playSound) {
        TileEntity initialTile = initialWorld.getTileEntity(initialPos);
        TileEntity finalTile = finalWorld.getTileEntity(finalPos);
        NBTTagCompound initialTag = new NBTTagCompound();
        NBTTagCompound finalTag = new NBTTagCompound();
        if (initialTile != null)
            initialTile.writeToNBT(initialTag);
        if (finalTile != null)
            finalTile.writeToNBT(finalTag);

        IBlockState initialState = initialWorld.getBlockState(initialPos);
        IBlockState finalState = finalWorld.getBlockState(finalPos);

        if ((initialState.getBlock().equals(Blocks.AIR) && finalState.getBlock().equals(Blocks.AIR)) || initialState.getBlock() instanceof BlockPortal || finalState.getBlock() instanceof BlockPortal)
            return false;

        if (playSound) {
            initialWorld.playSound(initialPos.getX(), initialPos.getY(), initialPos.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
            finalWorld.playSound(finalPos.getX(), finalPos.getY(), finalPos.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
        }

        //Finally, we get to do something! (CLEARING TILES)
        if (finalState.getBlock().hasTileEntity(finalState))
            finalWorld.removeTileEntity(finalPos);
        if (initialState.getBlock().hasTileEntity(initialState))
            initialWorld.removeTileEntity(initialPos);

        //TILES CLEARED
        IBlockState initialBlockState = initialWorld.getBlockState(initialPos);
        IBlockState finalBlockState = finalWorld.getBlockState(finalPos);
        finalWorld.setBlockState(finalPos, initialBlockState, 3);

        if (initialTile != null) {
            TileEntity newTileInitial = TileEntity.create(finalWorld, initialTag);


            finalWorld.setTileEntity(finalPos, newTileInitial);
            newTileInitial.setPos(finalPos);
            newTileInitial.setWorld(finalWorld);
        }

        initialWorld.setBlockState(initialPos, finalBlockState, 3);

        if (finalTile != null) {
            TileEntity newTileFinal = TileEntity.create(initialWorld, finalTag);

            initialWorld.setTileEntity(initialPos, newTileFinal);
            newTileFinal.setPos(initialPos);
            newTileFinal.setWorld(initialWorld);
        }

        initialWorld.notifyNeighborsOfStateChange(initialPos, finalState.getBlock(), true);
        finalWorld.notifyNeighborsOfStateChange(finalPos, initialState.getBlock(), true);

        return true;
    }

    //Shamelessly ripped off of CoFH Lib
    public static ItemStack consumeItem(ItemStack stack) {
        Item item = stack.getItem();
        boolean largerStack = stack.getCount() > 1;
        if (largerStack) {
            stack.shrink(1);
        }
        if (item.hasContainerItem(stack)) {
            ItemStack ret = item.getContainerItem(stack);
            if (ret.isEmpty()) {
                return ItemStack.EMPTY;
            }
            if (ret.isItemStackDamageable() && ret.getItemDamage() > ret.getMaxDamage()) {
                ret = ItemStack.EMPTY;
            }
            return ret;
        }
        return largerStack ? stack : ItemStack.EMPTY;
    }

    public static boolean hasUUID(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(Constants.NBT.MOST_SIG) && stack.getTagCompound().hasKey(Constants.NBT.LEAST_SIG);
    }

    public static UUID getUUID(ItemStack stack) {
        if (!hasUUID(stack)) {
            return null;
        }

        return new UUID(stack.getTagCompound().getLong(Constants.NBT.MOST_SIG), stack.getTagCompound().getLong(Constants.NBT.LEAST_SIG));
    }

    public static void setUUID(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);

        if (!stack.getTagCompound().hasKey(Constants.NBT.MOST_SIG) && !stack.getTagCompound().hasKey(Constants.NBT.LEAST_SIG)) {
            UUID itemUUID = UUID.randomUUID();
            stack.getTagCompound().setLong(Constants.NBT.MOST_SIG, itemUUID.getMostSignificantBits());
            stack.getTagCompound().setLong(Constants.NBT.LEAST_SIG, itemUUID.getLeastSignificantBits());
        }
    }

    public static RayTraceResult rayTrace(EntityPlayer player, boolean useLiquids) {
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;
        Vec3d eyePosition = new Vec3d(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);

        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
        float f5 = MathHelper.sin(-pitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;

        double reachDistance = 5.0D;
        if (player instanceof EntityPlayerMP)
            reachDistance = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();

        Vec3d reachPosition = eyePosition.addVector((double) f6 * reachDistance, (double) f5 * reachDistance, (double) f7 * reachDistance);
        return player.getEntityWorld().rayTraceBlocks(eyePosition, reachPosition, useLiquids, !useLiquids, false);
    }
}
