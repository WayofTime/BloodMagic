package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.ritual.EnumRuneType;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.ritual.RitualComponent;
import WayofTime.bloodmagic.ritual.RitualRegistry;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.handler.event.ClientHandler;
import WayofTime.bloodmagic.util.helper.RitualHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ItemRitualDiviner extends Item implements IVariantProvider {
    public static final String tooltipBase = "tooltip.bloodmagic.diviner.";
    public static String[] names = {"normal", "dusk", "dawn"};

    public ItemRitualDiviner() {
        setUnlocalizedName(BloodMagic.MODID + ".ritualDiviner");
        setCreativeTab(BloodMagic.TAB_BM);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    public String getHighlightTip(ItemStack stack, String displayName) {
        if (Strings.isNullOrEmpty(getCurrentRitual(stack)))
            return displayName;

        Ritual ritual = RitualRegistry.getRitualForId(getCurrentRitual(stack));
        if (ritual == null)
            return displayName;

        return displayName + ": " + TextHelper.localize(ritual.getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            if (world.isRemote) {
                trySetDisplayedRitual(stack, world, pos);
            }

            return EnumActionResult.SUCCESS;
        } else if (addRuneToRitual(stack, world, pos, player)) {
            if (world.isRemote) {
                spawnParticles(world, pos.up(), 15);
            }

            return EnumActionResult.SUCCESS;
            // TODO: Have the diviner automagically build the ritual
        }

        return EnumActionResult.PASS;
    }

    /**
     * Adds a single rune to the ritual.
     *
     * @param stack  - The Ritual Diviner stack
     * @param world  - The World
     * @param pos    - Block Position of the MRS.
     * @param player - The Player attempting to place the ritual
     * @return - True if a rune was successfully added
     */
    public boolean addRuneToRitual(ItemStack stack, World world, BlockPos pos, EntityPlayer player) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileMasterRitualStone) {
            Ritual ritual = RitualRegistry.getRitualForId(this.getCurrentRitual(stack));
            if (ritual != null) {
                EnumFacing direction = getDirection(stack);
                List<RitualComponent> components = Lists.newArrayList();
                ritual.gatherComponents(components::add);
                for (RitualComponent component : components) {
                    if (!canPlaceRitualStone(component.getRuneType(), stack)) {
                        return false;
                    }
                    BlockPos offset = component.getOffset(direction);
                    BlockPos newPos = pos.add(offset);
                    IBlockState state = world.getBlockState(newPos);
                    Block block = state.getBlock();
                    if (RitualHelper.isRune(world, newPos)) {
                        if (RitualHelper.isRuneType(world, newPos, component.getRuneType())) {
                            if (world.isRemote) {
                                undisplayHologram();
                            }
                        } else {
                            // Replace existing ritual stone
                            RitualHelper.setRuneType(world, newPos, component.getRuneType());
                            return true;
                        }
                    } else if (block.isAir(state, world, newPos) || block.isReplaceable(world, newPos)) {
                        if (!consumeStone(stack, world, player)) {
                            return false;
                        }
                        int meta = component.getRuneType().ordinal();
                        IBlockState newState = RegistrarBloodMagicBlocks.RITUAL_STONE.getStateFromMeta(meta);
                        world.setBlockState(newPos, newState);
                        return true;
                    } else {
                        return false; // TODO: Possibly replace the block with a
                        // ritual stone
                    }
                }
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public void trySetDisplayedRitual(ItemStack itemStack, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileMasterRitualStone) {
            Ritual ritual = RitualRegistry.getRitualForId(this.getCurrentRitual(itemStack));
            TileMasterRitualStone masterRitualStone = (TileMasterRitualStone) tile;

            if (ritual != null) {
                EnumFacing direction = getDirection(itemStack);
                ClientHandler.setRitualHolo(masterRitualStone, ritual, direction, true);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void undisplayHologram() {
        ClientHandler.setRitualHoloToNull();
    }

    // TODO: Make this work for any IRitualStone
    public boolean consumeStone(ItemStack stack, World world, EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            return true;
        }

        NonNullList<ItemStack> inventory = player.inventory.mainInventory;
        for (ItemStack newStack : inventory) {
            if (newStack.isEmpty()) {

                continue;
            }
            Item item = newStack.getItem();
            if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();
                if (block == RegistrarBloodMagicBlocks.RITUAL_STONE) {
                    newStack.shrink(1);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;

        Ritual ritual = RitualRegistry.getRitualForId(this.getCurrentRitual(stack));
        if (ritual != null) {
            tooltip.add(TextHelper.localize("tooltip.bloodmagic.diviner.currentRitual") + TextHelper.localize(ritual.getUnlocalizedName()));

            boolean sneaking = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
            boolean extraInfo = sneaking && Keyboard.isKeyDown(Keyboard.KEY_M);

            if (extraInfo) {
                tooltip.add("");

                for (EnumDemonWillType type : EnumDemonWillType.values()) {
                    if (TextHelper.canTranslate(ritual.getUnlocalizedName() + "." + type.getName().toLowerCase() + ".info")) {
                        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect(ritual.getUnlocalizedName() + "." + type.getName().toLowerCase() + ".info"))));
                    }
                }
            } else if (sneaking) {
                tooltip.add(TextHelper.localize(tooltipBase + "currentDirection", Utils.toFancyCasing(getDirection(stack).getName())));
                tooltip.add("");
                List<RitualComponent> components = Lists.newArrayList();
                ritual.gatherComponents(components::add);

                int blankRunes = 0;
                int airRunes = 0;
                int waterRunes = 0;
                int fireRunes = 0;
                int earthRunes = 0;
                int duskRunes = 0;
                int dawnRunes = 0;
                int totalRunes = components.size();

                for (RitualComponent component : components) {
                    switch (component.getRuneType()) {
                        case BLANK:
                            blankRunes++;
                            break;
                        case AIR:
                            airRunes++;
                            break;
                        case EARTH:
                            earthRunes++;
                            break;
                        case FIRE:
                            fireRunes++;
                            break;
                        case WATER:
                            waterRunes++;
                            break;
                        case DUSK:
                            duskRunes++;
                            break;
                        case DAWN:
                            dawnRunes++;
                            break;
                    }
                }

                if (blankRunes > 0)
                    tooltip.add(EnumRuneType.BLANK.colorCode + TextHelper.localize(tooltipBase + "blankRune", blankRunes));
                if (waterRunes > 0)
                    tooltip.add(EnumRuneType.WATER.colorCode + TextHelper.localize(tooltipBase + "waterRune", waterRunes));
                if (airRunes > 0)
                    tooltip.add(EnumRuneType.AIR.colorCode + TextHelper.localize(tooltipBase + "airRune", airRunes));
                if (fireRunes > 0)
                    tooltip.add(EnumRuneType.FIRE.colorCode + TextHelper.localize(tooltipBase + "fireRune", fireRunes));
                if (earthRunes > 0)
                    tooltip.add(EnumRuneType.EARTH.colorCode + TextHelper.localize(tooltipBase + "earthRune", earthRunes));
                if (duskRunes > 0)
                    tooltip.add(EnumRuneType.DUSK.colorCode + TextHelper.localize(tooltipBase + "duskRune", duskRunes));
                if (dawnRunes > 0)
                    tooltip.add(EnumRuneType.DAWN.colorCode + TextHelper.localize(tooltipBase + "dawnRune", dawnRunes));

                tooltip.add("");
                tooltip.add(TextHelper.localize(tooltipBase + "totalRune", totalRunes));
            } else {
                tooltip.add("");
                if (TextHelper.canTranslate(ritual.getUnlocalizedName() + ".info")) {
                    tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect(ritual.getUnlocalizedName() + ".info"))));
                    tooltip.add("");
                }

                tooltip.add(TextHelper.localizeEffect(tooltipBase + "extraInfo"));
                tooltip.add(TextHelper.localizeEffect(tooltipBase + "extraExtraInfo"));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        RayTraceResult ray = this.rayTrace(world, player, false);
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }

        if (player.isSneaking()) {
            if (!world.isRemote) {
                cycleRitual(stack, player);
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;

            RayTraceResult ray = this.rayTrace(player.getEntityWorld(), player, false);
            if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
                return false;
            }

            if (!player.isSwingInProgress) {
                if (player.isSneaking()) {
                    cycleRitualBackwards(stack, player);
                } else {
                    cycleDirection(stack, player);
                }
            }
        }

        return false;
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=basic");
        variants.put(1, "type=dusk");
        variants.put(2, "type=dawn");
    }

    public void cycleDirection(ItemStack stack, EntityPlayer player) {
        EnumFacing direction = getDirection(stack);
        EnumFacing newDirection;
        switch (direction) {
            case NORTH:
                newDirection = EnumFacing.EAST;
                break;
            case EAST:
                newDirection = EnumFacing.SOUTH;
                break;
            case SOUTH:
                newDirection = EnumFacing.WEST;
                break;
            case WEST:
                newDirection = EnumFacing.NORTH;
                break;
            default:
                newDirection = EnumFacing.NORTH;
        }

        setDirection(stack, newDirection);
        notifyDirectionChange(newDirection, player);
    }

    public void notifyDirectionChange(EnumFacing direction, EntityPlayer player) {
        player.sendStatusMessage(new TextComponentTranslation(tooltipBase + "currentDirection", Utils.toFancyCasing(direction.name())), true);
    }

    public void setDirection(ItemStack stack, EnumFacing direction) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setInteger(Constants.NBT.DIRECTION, direction.getIndex());
    }

    public EnumFacing getDirection(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            return EnumFacing.NORTH;
        }

        NBTTagCompound tag = stack.getTagCompound();

        int dir = tag.getInteger(Constants.NBT.DIRECTION);
        if (dir == 0) {
            return EnumFacing.NORTH;
        }

        return EnumFacing.VALUES[tag.getInteger(Constants.NBT.DIRECTION)];
    }

    /**
     * Cycles the selected ritual to the next available ritual that is enabled.
     *
     * @param stack  - The ItemStack of the ritual diviner
     * @param player - The player using the ritual diviner
     */
    public void cycleRitual(ItemStack stack, EntityPlayer player) {
        String key = getCurrentRitual(stack);
        List<String> idList = RitualRegistry.getOrderedIds();
        String firstId = "";
        boolean foundId = false;
        boolean foundFirst = false;

        for (String str : idList) {
            Ritual ritual = RitualRegistry.getRitualForId(str);

            if (!RitualRegistry.ritualEnabled(ritual) || !canDivinerPerformRitual(stack, ritual)) {
                continue;
            }

            if (!foundFirst) {
                firstId = str;
                foundFirst = true;
            }

            if (foundId) {
                setCurrentRitual(stack, str);
                notifyRitualChange(str, player);
                return;
            } else {
                if (str.equals(key)) {
                    foundId = true;
                    continue;
                }
            }
        }

        if (foundFirst) {
            setCurrentRitual(stack, firstId);
            notifyRitualChange(firstId, player);
        }
    }

    /**
     * Does the same as cycleRitual but instead cycles backwards.
     *
     * @param stack
     * @param player
     */
    public void cycleRitualBackwards(ItemStack stack, EntityPlayer player) {
        String key = getCurrentRitual(stack);
        List<String> idList = RitualRegistry.getOrderedIds();
        String firstId = "";
        boolean foundId = false;
        boolean foundFirst = false;

        for (int i = idList.size() - 1; i >= 0; i--) {
            String str = idList.get(i);
            Ritual ritual = RitualRegistry.getRitualForId(str);

            if (!RitualRegistry.ritualEnabled(ritual) || !canDivinerPerformRitual(stack, ritual)) {
                continue;
            }

            if (!foundFirst) {
                firstId = str;
                foundFirst = true;
            }

            if (foundId) {
                setCurrentRitual(stack, str);
                notifyRitualChange(str, player);
                return;
            } else {
                if (str.equals(key)) {
                    foundId = true;
                    continue;
                }
            }
        }

        if (foundFirst) {
            setCurrentRitual(stack, firstId);
            notifyRitualChange(firstId, player);
        }
    }

    public boolean canDivinerPerformRitual(ItemStack stack, Ritual ritual) {
        if (ritual == null) {
            return false;
        }

        List<RitualComponent> components = Lists.newArrayList();
        ritual.gatherComponents(components::add);
        for (RitualComponent component : components) {
            if (!canPlaceRitualStone(component.getRuneType(), stack)) {
                return false;
            }
        }

        return true;
    }

    public void notifyRitualChange(String key, EntityPlayer player) {
        Ritual ritual = RitualRegistry.getRitualForId(key);
        if (ritual != null) {
            player.sendStatusMessage(new TextComponentTranslation(ritual.getUnlocalizedName()), true);
        }
    }

    public void setCurrentRitual(ItemStack stack, String key) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setString(Constants.NBT.CURRENT_RITUAL, key);
    }

    public String getCurrentRitual(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getString(Constants.NBT.CURRENT_RITUAL);
    }

    public boolean canPlaceRitualStone(EnumRuneType rune, ItemStack stack) {
        int meta = stack.getItemDamage();
        switch (rune) {
            case BLANK:
            case AIR:
            case EARTH:
            case FIRE:
            case WATER:
                return true;
            case DUSK:
                return meta >= 1;
            case DAWN:
                return meta >= 2;
        }

        return false;
    }

    public static void spawnParticles(World worldIn, BlockPos pos, int amount) {
        IBlockState state = worldIn.getBlockState(pos);
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block.isAir(state, worldIn, pos)) {
            for (int i = 0; i < amount; ++i) {
                double d0 = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double) ((float) pos.getX() + itemRand.nextFloat()), (double) pos.getY() + (double) itemRand.nextFloat(), (double) ((float) pos.getZ() + itemRand.nextFloat()), d0, d1, d2);
            }
        } else {
            for (int i1 = 0; i1 < amount; ++i1) {
                double d0 = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double) ((float) pos.getX() + itemRand.nextFloat()), (double) pos.getY() + (double) itemRand.nextFloat() * 1.0f, (double) ((float) pos.getZ() + itemRand.nextFloat()), d0, d1, d2);
            }
        }
    }
}
