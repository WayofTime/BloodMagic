package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.RitualHelper;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemRitualDiviner extends Item implements IVariantProvider
{
    public static String[] names = { "normal", "dusk", "dawn" };

    public static final String tooltipBase = "tooltip.BloodMagic.diviner.";

    public ItemRitualDiviner()
    {
        setUnlocalizedName(Constants.Mod.MODID + ".ritualDiviner");
        setRegistryName(Constants.BloodMagicItem.RITUAL_DIVINER.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(id, 1, i));
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (addRuneToRitual(stack, world, pos, player))
        {
            if (world.isRemote)
            {
                spawnParticles(world, pos.up(), 15);
            }
            // TODO: Have the diviner automagically build the ritual
        }

        return false;
    }

    /**
     * Adds a single rune to the ritual.
     * 
     * @param stack
     *        - The Ritual Diviner stack
     * @param world
     *        - The World
     * @param pos
     *        - Block Position of the MRS.
     * @param player
     *        - The Player attempting to place the ritual
     * 
     * @return - True if a rune was successfully added
     */
    public boolean addRuneToRitual(ItemStack stack, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileMasterRitualStone)
        {
            Ritual ritual = RitualRegistry.getRitualForId(this.getCurrentRitual(stack));
            if (ritual != null)
            {
                EnumFacing direction = getDirection(stack);
                for (RitualComponent component : ritual.getComponents())
                {
                    if (!canPlaceRitualStone(component.getRuneType(), stack))
                    {
                        return false;
                    }
                    BlockPos offset = component.getOffset(direction);
                    BlockPos newPos = pos.add(offset);
                    IBlockState state = world.getBlockState(newPos);
                    Block block = state.getBlock();
                    if (RitualHelper.isRune(world, newPos))
                    {
                        if (RitualHelper.isRuneType(world, newPos, component.getRuneType()))
                        {
                            continue;
                        } else
                        {
                            // Replace existing ritual stone
                            RitualHelper.setRuneType(world, newPos, component.getRuneType());
                            return true;
                        }
                    } else if (block.isAir(world, newPos))
                    {
                        if (!consumeStone(stack, world, player))
                        {
                            return false;
                        }
                        int meta = component.getRuneType().ordinal();
                        IBlockState newState = ModBlocks.ritualStone.getStateFromMeta(meta);
                        world.setBlockState(newPos, newState);
                        return true;
                    } else
                    {
                        return false; // TODO: Possibly replace the block with a
                                      // ritual stone
                    }
                }
            }
        }

        return false;
    }

    // TODO: Make this work for any IRitualStone
    public boolean consumeStone(ItemStack stack, World world, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode)
        {
            return true;
        }

        ItemStack[] inventory = player.inventory.mainInventory;
        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack newStack = inventory[i];
            if (newStack == null)
            {
                continue;
            }
            Item item = newStack.getItem();
            if (item instanceof ItemBlock)
            {
                Block block = ((ItemBlock) item).getBlock();
                if (block == ModBlocks.ritualStone)
                {
                    newStack.stackSize--;
                    if (newStack.stackSize <= 0)
                    {
                        inventory[i] = null;
                    }

                    return true;
                }
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        Ritual ritual = RitualRegistry.getRitualForId(this.getCurrentRitual(stack));
        if (ritual != null)
        {
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.diviner.currentRitual") + TextHelper.localize(ritual.getUnlocalizedName()));

            boolean sneaking = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

            if (sneaking)
            {
                tooltip.add(TextHelper.localize(tooltipBase + "currentDirection", getDirection(stack)));
                tooltip.add("");
                ArrayList<RitualComponent> componentList = ritual.getComponents();

                int blankRunes = 0;
                int airRunes = 0;
                int waterRunes = 0;
                int fireRunes = 0;
                int earthRunes = 0;
                int duskRunes = 0;
                int dawnRunes = 0;
                int totalRunes = 0;

                for (RitualComponent component : componentList)
                {
                    totalRunes++;
                    switch (component.getRuneType())
                    {
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
                {
                    tooltip.add(TextHelper.localize(tooltipBase + "blankRune", blankRunes));
                }
                if (waterRunes > 0)
                {
                    tooltip.add(TextHelper.localize(tooltipBase + "waterRune", waterRunes));
                }
                if (airRunes > 0)
                {
                    tooltip.add(TextHelper.localize(tooltipBase + "airRune", airRunes));
                }
                if (fireRunes > 0)
                {
                    tooltip.add(TextHelper.localize(tooltipBase + "fireRune", fireRunes));
                }
                if (earthRunes > 0)
                {
                    tooltip.add(TextHelper.localize(tooltipBase + "earthRune", earthRunes));
                }
                if (duskRunes > 0)
                {
                    tooltip.add(TextHelper.localize(tooltipBase + "duskRune", duskRunes));
                }
                if (dawnRunes > 0)
                {
                    tooltip.add(TextHelper.localize(tooltipBase + "dawnRune", dawnRunes));
                }

                tooltip.add("");
                tooltip.add(TextHelper.localize(tooltipBase + "totalRune", totalRunes));
            } else
            {
                tooltip.add("");
                tooltip.add(TextHelper.localize(tooltipBase + "extraInfo"));
            }
        }
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {

        if (player.isSneaking() && !world.isRemote)
        {
            cycleRitual(stack, player);
        }

        return stack;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entityLiving;

            if (!player.isSwingInProgress)
            {
                if (player.isSneaking())
                {
                    cycleRitualBackwards(stack, player);
                } else
                {
                    cycleDirection(stack, player);
                }
            }
        }

        return false;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=basic"));
        ret.add(new ImmutablePair<Integer, String>(1, "type=dusk"));
        ret.add(new ImmutablePair<Integer, String>(2, "type=dawn"));
        return ret;
    }

    public void cycleDirection(ItemStack stack, EntityPlayer player)
    {
        EnumFacing direction = getDirection(stack);
        EnumFacing newDirection;
        switch (direction)
        {
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

    public void notifyDirectionChange(EnumFacing direction, EntityPlayer player)
    {
        ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentDirection", direction.getName()));
    }

    public void setDirection(ItemStack stack, EnumFacing direction)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setInteger(Constants.NBT.DIRECTION, direction.getIndex());
    }

    public EnumFacing getDirection(ItemStack stack)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
            return EnumFacing.NORTH;
        }

        NBTTagCompound tag = stack.getTagCompound();

        int dir = tag.getInteger(Constants.NBT.DIRECTION);
        if (dir == 0)
        {
            return EnumFacing.NORTH;
        }

        return EnumFacing.VALUES[tag.getInteger(Constants.NBT.DIRECTION)];
    }

    /**
     * Cycles the selected ritual to the next available ritual that is enabled.
     * 
     * @param stack
     *        - The ItemStack of the ritual diviner
     * @param player
     *        - The player using the ritual diviner
     */
    public void cycleRitual(ItemStack stack, EntityPlayer player)
    {
        String key = getCurrentRitual(stack);
        List<String> idList = RitualRegistry.getOrderedIds();
        String firstId = "";
        boolean foundId = false;
        boolean foundFirst = false;

        for (String str : idList)
        {
            Ritual ritual = RitualRegistry.getRitualForId(str);

            if (!RitualRegistry.ritualEnabled(ritual) || !canDivinerPerformRitual(stack, ritual))
            {
                continue;
            }

            if (!foundFirst)
            {
                firstId = str;
                foundFirst = true;
            }

            if (foundId)
            {
                setCurrentRitual(stack, str);
                notifyRitualChange(str, player);
                return;
            } else
            {
                if (str.equals(key))
                {
                    foundId = true;
                    continue;
                }
            }
        }

        if (foundFirst)
        {
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
    public void cycleRitualBackwards(ItemStack stack, EntityPlayer player)
    {
        String key = getCurrentRitual(stack);
        List<String> idList = RitualRegistry.getOrderedIds();
        String firstId = "";
        boolean foundId = false;
        boolean foundFirst = false;

        for (int i = idList.size() - 1; i >= 0; i--)
        {
            String str = idList.get(i);
            Ritual ritual = RitualRegistry.getRitualForId(str);

            if (!RitualRegistry.ritualEnabled(ritual) || !canDivinerPerformRitual(stack, ritual))
            {
                continue;
            }

            if (!foundFirst)
            {
                firstId = str;
                foundFirst = true;
            }

            if (foundId)
            {
                setCurrentRitual(stack, str);
                notifyRitualChange(str, player);
                return;
            } else
            {
                if (str.equals(key))
                {
                    foundId = true;
                    continue;
                }
            }
        }

        if (foundFirst)
        {
            setCurrentRitual(stack, firstId);
            notifyRitualChange(firstId, player);
        }
    }

    public boolean canDivinerPerformRitual(ItemStack stack, Ritual ritual)
    {
        if (ritual == null)
        {
            return false;
        }

        ArrayList<RitualComponent> components = ritual.getComponents();
        for (RitualComponent component : components)
        {
            if (!canPlaceRitualStone(component.getRuneType(), stack))
            {
                return false;
            }
        }

        return true;
    }

    public void notifyRitualChange(String key, EntityPlayer player)
    {
        Ritual ritual = RitualRegistry.getRitualForId(key);
        if (ritual != null)
        {
            ChatUtil.sendNoSpam(player, TextHelper.localize(tooltipBase + "currentRitual") + TextHelper.localize(ritual.getUnlocalizedName()));
        }
    }

    public void setCurrentRitual(ItemStack stack, String key)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setString(Constants.NBT.CURRENT_RITUAL, key);
    }

    public String getCurrentRitual(ItemStack stack)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getString(Constants.NBT.CURRENT_RITUAL);
    }

    public boolean canPlaceRitualStone(EnumRuneType rune, ItemStack stack)
    {
        int meta = stack.getItemDamage();
        switch (rune)
        {
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

    public static void spawnParticles(World worldIn, BlockPos pos, int amount)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block.isAir(worldIn, pos))
        {
            block.setBlockBoundsBasedOnState(worldIn, pos);

            for (int i = 0; i < amount; ++i)
            {
                double d0 = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double) ((float) pos.getX() + itemRand.nextFloat()), (double) pos.getY() + (double) itemRand.nextFloat() * block.getBlockBoundsMaxY(), (double) ((float) pos.getZ() + itemRand.nextFloat()), d0, d1, d2, new int[0]);
            }
        } else
        {
            for (int i1 = 0; i1 < amount; ++i1)
            {
                double d0 = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double) ((float) pos.getX() + itemRand.nextFloat()), (double) pos.getY() + (double) itemRand.nextFloat() * 1.0f, (double) ((float) pos.getZ() + itemRand.nextFloat()), d0, d1, d2, new int[0]);
            }
        }
    }
}
