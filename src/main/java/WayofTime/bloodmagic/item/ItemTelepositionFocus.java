package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemTelepositionFocus extends ItemBindableBase implements IVariantProvider
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (player.isSneaking())
        {
            RayTraceResult mop = rayTrace(world, player, false);

            if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                setBlockPos(stack, world, mop.getBlockPos());
            }
        }

        return super.onItemRightClick(stack, world, player, hand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localize("tooltip.BloodMagic.telepositionFocus." + names[stack.getItemDamage()]))));

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

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=weak"));
        ret.add(new ImmutablePair<Integer, String>(1, "type=enhanced"));
        ret.add(new ImmutablePair<Integer, String>(2, "type=reinforced"));
        ret.add(new ImmutablePair<Integer, String>(3, "type=demonic"));
        return ret;
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
        stack = NBTHelper.checkNBT(stack);
        NBTTagCompound itemTag = stack.getTagCompound();
        itemTag.setInteger(Constants.NBT.X_COORD, pos.getX());
        itemTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
        itemTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
        itemTag.setInteger(Constants.NBT.DIMENSION_ID, world.provider.getDimension());
        return stack;
    }
}
