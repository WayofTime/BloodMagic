package WayofTime.bloodmagic.item.sigil;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.teleport.TeleportQueue;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.ritual.portal.Teleports;
import WayofTime.bloodmagic.tile.TileTeleposer;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemSigilTeleposition extends ItemSigilBase
{
    public ItemSigilTeleposition()
    {
        super("teleposition");
        setRegistryName(Constants.BloodMagicItem.SIGIL_TELEPOSITION.getRegName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, player, tooltip, advanced);

        stack = NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        if (tag != null && stack.getTagCompound().hasKey(Constants.NBT.DIMENSION_ID) && stack.getTagCompound().hasKey(Constants.NBT.X_COORD) && stack.getTagCompound().hasKey(Constants.NBT.Y_COORD) && stack.getTagCompound().hasKey(Constants.NBT.Z_COORD))
        {
            tooltip.add(" ");
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.telepositionFocus.coords", getValue(tag, Constants.NBT.X_COORD), getValue(tag, Constants.NBT.Y_COORD), getValue(tag, Constants.NBT.Z_COORD)));
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.telepositionFocus.dimension", getValue(tag, Constants.NBT.DIMENSION_ID)));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote && NBTHelper.checkNBT(stack) != null && stack.getTagCompound().hasKey(Constants.NBT.DIMENSION_ID) && stack.getTagCompound().hasKey(Constants.NBT.X_COORD) && stack.getTagCompound().hasKey(Constants.NBT.Y_COORD) && stack.getTagCompound().hasKey(Constants.NBT.Z_COORD))
        {
            BlockPos blockPos = new BlockPos(getValue(stack.getTagCompound(), Constants.NBT.X_COORD), getValue(stack.getTagCompound(), Constants.NBT.Y_COORD), getValue(stack.getTagCompound(), Constants.NBT.Z_COORD)).up();
            if (world.provider.getDimension() == getValue(stack.getTagCompound(), Constants.NBT.DIMENSION_ID))
            {
                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(blockPos, player, getOwnerUUID(stack)));
            } else
            {
                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportToDim(blockPos, player, getOwnerUUID(stack), world, getValue(stack.getTagCompound(), Constants.NBT.DIMENSION_ID)));
            }
        }
        return super.onItemRightClick(stack, world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote && player.isSneaking() && NBTHelper.checkNBT(stack) != null)
        {
            if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileTeleposer)
            {
                stack.getTagCompound().setInteger(Constants.NBT.DIMENSION_ID, world.provider.getDimension());
                stack.getTagCompound().setInteger(Constants.NBT.X_COORD, pos.getX());
                stack.getTagCompound().setInteger(Constants.NBT.Y_COORD, pos.getY());
                stack.getTagCompound().setInteger(Constants.NBT.Z_COORD, pos.getZ());

                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }

    public int getValue(NBTTagCompound tag, String key)
    {
        return tag.getInteger(key);
    }
}
