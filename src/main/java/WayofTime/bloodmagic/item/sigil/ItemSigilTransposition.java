package WayofTime.bloodmagic.item.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;

public class ItemSigilTransposition extends ItemSigilBase
{
    public ItemSigilTransposition()
    {
        super("transposition", 1000);
        setRegistryName(Constants.BloodMagicItem.SIGIL_TRANSPOSITION.getRegName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, player, tooltip, advanced);

        stack = NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        if (tag.hasKey(Constants.NBT.CONTAINED_BLOCK_NAME) && tag.hasKey(Constants.NBT.CONTAINED_BLOCK_META))
        {
            tooltip.add(" ");
            BlockStack blockStack = new BlockStack(Block.getBlockFromName(tag.getString(Constants.NBT.CONTAINED_BLOCK_NAME)), tag.getByte(Constants.NBT.CONTAINED_BLOCK_META));
            tooltip.add(blockStack.getItemStack().getDisplayName());
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        if (tag.hasKey(Constants.NBT.CONTAINED_BLOCK_NAME) && tag.hasKey(Constants.NBT.CONTAINED_BLOCK_META))
        {
            BlockStack blockStack = new BlockStack(Block.getBlockFromName(tag.getString(Constants.NBT.CONTAINED_BLOCK_NAME)), tag.getByte(Constants.NBT.CONTAINED_BLOCK_META));
            return super.getItemStackDisplayName(stack) + " (" + blockStack.getItemStack().getDisplayName() + ")";
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        stack = NBTHelper.checkNBT(stack);

        if (!world.isRemote)
        {
            BlockStack rightClickedBlock = BlockStack.getStackFromPos(world, blockPos);
            if (!ConfigHandler.transpositionBlacklist.contains(rightClickedBlock) && player.isSneaking() && (!stack.getTagCompound().hasKey(Constants.NBT.CONTAINED_BLOCK_NAME) || !stack.getTagCompound().hasKey(Constants.NBT.CONTAINED_BLOCK_META)))
            {
                if (rightClickedBlock.getBlock().getPlayerRelativeBlockHardness(player, world, blockPos) >= 0 && rightClickedBlock.getBlock().getBlockHardness(world, blockPos) >= 0)
                {
                    int cost = getLPUsed();

                    NBTTagCompound tileNBTTag = new NBTTagCompound();
                    String blockName = rightClickedBlock.getBlock().getRegistryName();
                    byte metadata = (byte) stack.getMetadata();

                    if (world.getTileEntity(blockPos) != null)
                    {
                        cost *= 5;
                        world.getTileEntity(blockPos).writeToNBT(tileNBTTag);

                        if (world.getTileEntity(blockPos) instanceof TileEntityMobSpawner)
                        {
                            cost *= 6;
                        }
                    }

                    stack.getTagCompound().setString(Constants.NBT.CONTAINED_BLOCK_NAME, blockName);
                    stack.getTagCompound().setByte(Constants.NBT.CONTAINED_BLOCK_META, metadata);
                    stack.getTagCompound().setTag(Constants.NBT.CONTAINED_TILE_ENTITY, tileNBTTag);

                    syphonNetwork(stack, player, cost);
                    lightning(world, blockPos);

                    world.removeTileEntity(blockPos);
                    world.setBlockToAir(blockPos);

                    return true;
                }
            } else if (stack.getTagCompound().hasKey(Constants.NBT.CONTAINED_BLOCK_NAME) && stack.getTagCompound().hasKey(Constants.NBT.CONTAINED_BLOCK_META))
            {
                IBlockState iblockstate = world.getBlockState(blockPos);
                Block block = iblockstate.getBlock();
                BlockStack blockToPlace = new BlockStack(Block.getBlockFromName(stack.getTagCompound().getString(Constants.NBT.CONTAINED_BLOCK_NAME)), stack.getTagCompound().getByte(Constants.NBT.CONTAINED_BLOCK_META));

                if (!block.isReplaceable(world, blockPos))
                {
                    blockPos = blockPos.offset(side);
                }

                if (stack.stackSize != 0 && player.canPlayerEdit(blockPos, side, stack) && world.canBlockBePlaced(blockToPlace.getBlock(), blockPos, false, side, player, stack))
                {
                    if (world.setBlockState(blockPos, blockToPlace.getState(), 3))
                    {
                        blockToPlace.getBlock().onBlockPlacedBy(world, blockPos, blockToPlace.getState(), player, blockToPlace.getItemStack());
                        world.playSoundEffect((double) ((float) blockPos.getX() + 0.5F), (double) ((float) blockPos.getY() + 0.5F), (double) ((float) blockPos.getZ() + 0.5F), blockToPlace.getBlock().stepSound.getPlaceSound(), (blockToPlace.getBlock().stepSound.getVolume() + 1.0F) / 2.0F, blockToPlace.getBlock().stepSound.getFrequency() * 0.8F);

                        if (stack.getTagCompound().hasKey(Constants.NBT.CONTAINED_TILE_ENTITY) && blockToPlace.getBlock().hasTileEntity(blockToPlace.getState()))
                        {
                            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(Constants.NBT.CONTAINED_TILE_ENTITY);
                            tag.setInteger("x", blockPos.getX());
                            tag.setInteger("y", blockPos.getY());
                            tag.setInteger("z", blockPos.getZ());
                            world.getTileEntity(blockPos).readFromNBT(tag);
                        }
                        world.markBlockForUpdate(blockPos);

                        stack.getTagCompound().removeTag(Constants.NBT.CONTAINED_BLOCK_NAME);
                        stack.getTagCompound().removeTag(Constants.NBT.CONTAINED_BLOCK_META);
                        stack.getTagCompound().removeTag(Constants.NBT.CONTAINED_TILE_ENTITY);

                        lightning(world, blockPos);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void lightning(World world, BlockPos blockPos)
    {
        world.addWeatherEffect(new EntityLightningBolt(world, blockPos.getX(), blockPos.getY(), blockPos.getZ()));
    }

}
