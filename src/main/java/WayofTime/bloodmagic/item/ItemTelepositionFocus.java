package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class ItemTelepositionFocus extends ItemBindable
{
    public static String[] names = { "weak", "enhanced", "reinforced", "demonic" };

    public ItemTelepositionFocus()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".focus.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setMaxStackSize(1);
        setHasSubtypes(true);
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

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (BindableHelper.checkAndSetItemOwner(stack, player))
        {
            if (player.isSneaking())
            {
                MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, false);

                if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    setBlockPos(stack, world, mop.getBlockPos());
                }
            }
        }

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localize("tooltip.BloodMagic.telepositionfocus." + names[stack.getItemDamage()]))));

        super.addInformation(stack, player, tooltip, advanced);

        stack = NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        BlockPos coords = getBlockPos(stack);

        if (coords != null && tag != null)
        {
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.telepositionFocus.coords", coords.getX(), coords.getY(), coords.getZ()));
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.telepositionFocus.dimension", tag.getInteger(Constants.NBT.DIMENSION_ID)));
        }
    }

    public World getWorld(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);
        return DimensionManager.getWorld(stack.getTagCompound().getInteger(Constants.NBT.DIMENSION_ID));
    }

    public BlockPos getBlockPos(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);
        return new BlockPos(stack.getTagCompound().getInteger(Constants.NBT.X_COORD), stack.getTagCompound().getInteger(Constants.NBT.Y_COORD), stack.getTagCompound().getInteger(Constants.NBT.Z_COORD));
    }

    public ItemStack setBlockPos(ItemStack stack, World world, BlockPos pos)
    {
        NBTTagCompound itemTag = stack.getTagCompound();
        itemTag.setInteger(Constants.NBT.X_COORD, pos.getX());
        itemTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
        itemTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
        itemTag.setInteger(Constants.NBT.DIMENSION_ID, world.provider.getDimensionId());
        return stack;
    }
}
