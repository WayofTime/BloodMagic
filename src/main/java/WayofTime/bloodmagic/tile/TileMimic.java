package WayofTime.bloodmagic.tile;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import WayofTime.bloodmagic.block.BlockMimic;
import WayofTime.bloodmagic.entity.mob.EntityMimic;
import WayofTime.bloodmagic.registry.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.registry.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Utils;

import javax.annotation.Nullable;

public class TileMimic extends TileInventory implements ITickable
{
    private static Field _blockMetadata = ReflectionHelper.findField(TileEntity.class, "blockMetadata", "field_145847_g");

    public boolean dropItemsOnBreak = true;
    public NBTTagCompound tileTag = new NBTTagCompound();
    public TileEntity mimicedTile = null;
    public int metaOfReplacedBlock = 0;

    public int playerCheckRadius = 5;
    public int potionSpawnRadius = 5;
    public int potionSpawnInterval = 40;

    private int internalCounter = 0;

    public TileMimic()
    {
        super(2, "mimic");
    }

    @Override
    public void update()
    {
        if (getWorld().isRemote)
        {
            return;
        }

        internalCounter++;
        if (internalCounter % potionSpawnInterval == 0 && this.getBlockMetadata() == BlockMimic.sentientMimicMeta)
        {
            ItemStack potionStack = this.getStackInSlot(1);
            if (!potionStack.isEmpty())
            {
                AxisAlignedBB bb = new AxisAlignedBB(this.getPos()).expand(playerCheckRadius, playerCheckRadius, playerCheckRadius);
                List<EntityPlayer> playerList = getWorld().getEntitiesWithinAABB(EntityPlayer.class, bb);

                for (EntityPlayer player : playerList)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        double posX = this.pos.getX() + 0.5 + (2 * getWorld().rand.nextDouble() - 1) * potionSpawnRadius;
                        double posY = this.pos.getY() + 0.5 + (2 * getWorld().rand.nextDouble() - 1) * potionSpawnRadius;
                        double posZ = this.pos.getZ() + 0.5 + (2 * getWorld().rand.nextDouble() - 1) * potionSpawnRadius;

                        ItemStack newStack = new ItemStack(potionStack.getItem() == RegistrarBloodMagicItems.POTION_FLASK ? Items.SPLASH_POTION : potionStack.getItem());
                        newStack.setTagCompound(potionStack.getTagCompound());

                        EntityPotion potionEntity = new EntityPotion(getWorld(), posX, posY, posZ, newStack);

                        getWorld().spawnEntity(potionEntity);
                        break;
                    }
                }
            }
        }

        if (this.getBlockMetadata() == BlockMimic.sentientMimicMeta && getWorld().getDifficulty() != EnumDifficulty.PEACEFUL && !(mimicedTile instanceof IInventory))
        {
            AxisAlignedBB bb = new AxisAlignedBB(this.getPos()).expand(playerCheckRadius, playerCheckRadius, playerCheckRadius);
            List<EntityPlayer> playerList = getWorld().getEntitiesWithinAABB(EntityPlayer.class, bb);

            for (EntityPlayer player : playerList)
            {
                if (!player.capabilities.isCreativeMode && Utils.canEntitySeeBlock(getWorld(), player, getPos()))
                {
                    spawnMimicEntity(player);
                    break;
                }
            }
        }

    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side)
    {
        if (!heldItem.isEmpty() && player.capabilities.isCreativeMode)
        {
            List<PotionEffect> list = PotionUtils.getEffectsFromStack(heldItem);
            if (!list.isEmpty())
            {
                if (!world.isRemote)
                {
                    setInventorySlotContents(1, heldItem.copy());
                    world.notifyBlockUpdate(pos, state, state, 3);
                    ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat.bloodmagic.mimic.potionSet"));
                }
                return true;
            } else if (heldItem.getItem() == RegistrarBloodMagicItems.POTION_FLASK)
            {
                //The potion flask is empty, therefore we have to reset the stored potion.
                if (!world.isRemote)
                {
                    setInventorySlotContents(1, ItemStack.EMPTY);
                    world.notifyBlockUpdate(pos, state, state, 3);
                    ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat.bloodmagic.mimic.potionRemove"));
                }
                return true;
            }
        }

        if (performSpecialAbility(player, side))
        {
            return true;
        }

        if (player.isSneaking())
            return false;

        if (!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() == new ItemStack(RegistrarBloodMagicBlocks.MIMIC).getItem())
            return false;

        if (!getStackInSlot(0).isEmpty() && !player.getHeldItem(hand).isEmpty())
            return false;

        if (!dropItemsOnBreak && !player.capabilities.isCreativeMode)
            return false;

        Utils.insertItemToTile(this, player, 0);
        this.refreshTileEntity();

        if (player.capabilities.isCreativeMode)
        {
            dropItemsOnBreak = getStackInSlot(0).isEmpty();
        }

        world.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    public boolean performSpecialAbility(EntityPlayer player, EnumFacing sideHit)
    {
        switch (this.getBlockMetadata())
        {
        case BlockMimic.sentientMimicMeta:
            if (player.capabilities.isCreativeMode)
            {
                if (player.isSneaking())
                {
                    playerCheckRadius = Math.max(playerCheckRadius - 1, 0);
                    ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat.bloodmagic.mimic.detectRadius.down", playerCheckRadius));
                } else
                {
                    playerCheckRadius++;
                    ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat.bloodmagic.mimic.detectRadius.up", playerCheckRadius));
                }

                return false;
            }

            return spawnMimicEntity(player);
        default:
            if (!player.capabilities.isCreativeMode)
            {
                return false;
            }

            if (player.getActiveItemStack().isEmpty() && !getStackInSlot(1).isEmpty())
            {
                switch (sideHit)
                {
                case EAST: //When the block is clicked on the EAST or WEST side, potionSpawnRadius is edited.
                case WEST:
                    if (player.isSneaking())
                    {
                        potionSpawnRadius = Math.max(potionSpawnRadius - 1, 0);
                        ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat.bloodmagic.mimic.potionSpawnRadius.down", potionSpawnRadius));
                    } else
                    {
                        potionSpawnRadius++;
                        ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat.bloodmagic.mimic.potionSpawnRadius.up", potionSpawnRadius));
                    }
                    break;
                case NORTH: //When the block is clicked on the NORTH or SOUTH side, detectRadius is edited.
                case SOUTH:
                    if (player.isSneaking())
                    {
                        playerCheckRadius = Math.max(playerCheckRadius - 1, 0);
                        ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat.bloodmagic.mimic.detectRadius.down", playerCheckRadius));
                    } else
                    {
                        playerCheckRadius++;
                        ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat.bloodmagic.mimic.detectRadius.up", playerCheckRadius));
                    }
                    break;
                case UP: //When the block is clicked on the UP or DOWN side, potionSpawnInterval is edited.
                case DOWN:
                    if (player.isSneaking())
                    {
                        potionSpawnInterval = Math.max(potionSpawnInterval - 1, 1);
                        ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat.bloodmagic.mimic.potionInterval.down", potionSpawnInterval));
                    } else
                    {
                        potionSpawnInterval++;
                        ChatUtil.sendNoSpam(player, new TextComponentTranslation("chat.bloodmagic.mimic.potionInterval.up", potionSpawnInterval));
                    }
                    break;
                default:
                    break;

                }

                return true;
            }
        }
        return false;
    }

    public boolean spawnMimicEntity(EntityPlayer target)
    {
        if (this.getWorld().getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            return false;
        }

        if (this.getStackInSlot(0).isEmpty() || getWorld().isRemote)
        {
            return false;
        }

        EntityMimic mimicEntity = new EntityMimic(getWorld());
        mimicEntity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);

        mimicEntity.initializeMimic(getStackInSlot(0), tileTag, dropItemsOnBreak, metaOfReplacedBlock, playerCheckRadius, pos);
        tileTag = null;
        mimicedTile = null;
        this.setInventorySlotContents(0, ItemStack.EMPTY);

        getWorld().spawnEntity(mimicEntity);
        if (target != null)
        {
            mimicEntity.setAttackTarget(target);
        }

        getWorld().setBlockToAir(pos);

        return true;
    }

    public void refreshTileEntity()
    {
        if (mimicedTile != null)
        {
            dropMimicedTileInventory();
        }
        mimicedTile = getTileFromStackWithTag(getWorld(), pos, getStackInSlot(0), tileTag, metaOfReplacedBlock);
    }

    @Override
    public void deserialize(NBTTagCompound tag)
    {
        super.deserialize(tag);

        dropItemsOnBreak = tag.getBoolean("dropItemsOnBreak");
        tileTag = tag.getCompoundTag("tileTag");
        metaOfReplacedBlock = tag.getInteger("metaOfReplacedBlock");
        mimicedTile = getTileFromStackWithTag(getWorld(), pos, getStackInSlot(0), tileTag, metaOfReplacedBlock);
        playerCheckRadius = tag.getInteger("playerCheckRadius");
        potionSpawnRadius = tag.getInteger("potionSpawnRadius");
        potionSpawnInterval = Math.max(1, tag.getInteger("potionSpawnInterval"));
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag)
    {
        super.serialize(tag);

        tag.setBoolean("dropItemsOnBreak", dropItemsOnBreak);
        tag.setTag("tileTag", tileTag);
        tag.setInteger("metaOfReplacedBlock", metaOfReplacedBlock);
        tag.setInteger("playerCheckRadius", playerCheckRadius);
        tag.setInteger("potionSpawnRadius", potionSpawnRadius);
        tag.setInteger("potionSpawnInterval", potionSpawnInterval);

        return tag;
    }

    public static void replaceMimicWithBlockActual(TileMimic mimic)
    {
        World world = mimic.getWorld();
        BlockPos pos = mimic.getPos();

        replaceMimicWithBlockActual(world, pos, mimic.getStackInSlot(0), mimic.tileTag, mimic.metaOfReplacedBlock);
    }

    public static boolean replaceMimicWithBlockActual(World world, BlockPos pos, ItemStack stack, NBTTagCompound tileTag, int replacedMeta)
    {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock)
        {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            IBlockState state = block.getStateFromMeta(replacedMeta);
            if (world.setBlockState(pos, state, 3))
            {
                TileEntity tile = world.getTileEntity(pos);
                if (tile != null)
                {
                    tileTag.setInteger("x", pos.getX());
                    tileTag.setInteger("y", pos.getY());
                    tileTag.setInteger("z", pos.getZ());
                    tile.readFromNBT(tileTag);
                }

                return true;
            }
        }

        return false;
    }

    @Nullable
    public static TileEntity getTileFromStackWithTag(World world, BlockPos pos, ItemStack stack, @Nullable NBTTagCompound tag, int replacementMeta)
    {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock)
        {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            IBlockState state = block.getStateFromMeta(stack.getItemDamage());
            if (block.hasTileEntity(state))
            {
                TileEntity tile = block.createTileEntity(world, state);

                if (tile == null)
                    return null;

                if (tag != null)
                {
                    NBTTagCompound copyTag = tag.copy();
                    copyTag.setInteger("x", pos.getX());
                    copyTag.setInteger("y", pos.getY());
                    copyTag.setInteger("z", pos.getZ());
                    tile.readFromNBT(copyTag);
                }

                tile.setWorld(world);

                try
                {
                    _blockMetadata.setInt(tile, replacementMeta);
                } catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }

                return tile;
            }
        }

        return null;
    }

    @Override
    public void dropItems()
    {
        if (dropItemsOnBreak)
        {
            InventoryHelper.dropInventoryItems(getWorld(), getPos(), this);
        }

        dropMimicedTileInventory();
    }

    public void dropMimicedTileInventory()
    {
        if (!getWorld().isRemote && mimicedTile instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(getWorld(), getPos(), (IInventory) mimicedTile);
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return slot == 0 && dropItemsOnBreak;
    }
}