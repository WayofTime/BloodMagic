package WayofTime.bloodmagic.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.block.BlockMimic;
import WayofTime.bloodmagic.tile.TileMimic;

public class ItemBlockMimic extends ItemBlock
{
    public ItemBlockMimic(Block block)
    {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + BlockMimic.names[stack.getItemDamage()];
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!player.isSneaking())
        {
            return super.onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
        }

        if (stack.stackSize != 0 && player.canPlayerEdit(pos, facing, stack))
        {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState iblockstate1 = this.block.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, i, player);

            IBlockState blockReplaced = world.getBlockState(pos);
            if (!canReplaceBlock(world, pos, blockReplaced))
            {
                return super.onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
            }

            TileEntity tileReplaced = world.getTileEntity(pos);
            if (!canReplaceTile(i, tileReplaced))
            {
                return EnumActionResult.FAIL;
            }

            ItemStack replacedStack = block.getItem(world, pos, iblockstate);

//            ItemStack replacedStack = new ItemStack(block, 1, block.getMetaFromState(iblockstate));

            NBTTagCompound tileTag = getTagFromTileEntity(tileReplaced);
            if (tileReplaced != null)
            {
                NBTTagCompound voidTag = new NBTTagCompound();
                voidTag.setInteger("x", pos.getX());
                voidTag.setInteger("y", pos.getY());
                voidTag.setInteger("z", pos.getZ());
                tileReplaced.readFromNBT(voidTag);
            }

            if (placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
                SoundType soundtype = this.block.getSoundType();
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                --stack.stackSize;

                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TileMimic)
                {
                    TileMimic mimic = (TileMimic) tile;
                    mimic.metaOfReplacedBlock = block.getMetaFromState(iblockstate);
                    mimic.tileTag = tileTag;
                    mimic.setInventorySlotContents(0, replacedStack);
                    mimic.refreshTileEntity();

                    if (player.capabilities.isCreativeMode)
                    {
                        mimic.dropItemsOnBreak = false;
                    }
                }

                return EnumActionResult.SUCCESS;
            } else
            {
                tileReplaced.readFromNBT(tileTag);
            }
        }

        return EnumActionResult.FAIL;
    }

    public boolean canReplaceTile(int meta, TileEntity tile)
    {
        if (tile instanceof TileEntityChest)
        {
            return true;
        }

        return tile == null;
    }

    public boolean canReplaceBlock(World world, BlockPos pos, IBlockState state) {
        return state.getBlockHardness(world, pos) != -1.0F;
    }

    public NBTTagCompound getTagFromTileEntity(TileEntity tile)
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (tile != null)
        {
            return tile.writeToNBT(tag);
        }

        return tag;
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }
}