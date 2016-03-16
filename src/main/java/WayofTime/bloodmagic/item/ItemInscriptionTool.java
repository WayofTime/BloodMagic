package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.block.BlockRitualStone;
import WayofTime.bloodmagic.util.helper.TextHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ItemInscriptionTool extends ItemBindable implements IVariantProvider
{
    public ItemInscriptionTool()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".scribe.");
        setRegistryName(Constants.BloodMagicItem.INSCRIPTION_TOOL.getRegName());
        setHasSubtypes(true);
        setLPUsed(100);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + EnumRuneType.values()[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 1; i < EnumRuneType.values().length; i++)
        {
            ItemStack stack = NBTHelper.checkNBT(new ItemStack(id, 1, i));
            stack.getTagCompound().setInteger(Constants.NBT.USES, 10);
            list.add(stack);
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockRitualStone && !((BlockRitualStone) state.getBlock()).isRuneType(world, pos, getType(stack)))
        {
            stack = NBTHelper.checkNBT(stack);
            int uses = stack.getTagCompound().getInteger(Constants.NBT.USES);

            world.setBlockState(pos, state.withProperty(((BlockRitualStone) state.getBlock()).getStringProp(), getType(stack).getName()));
            if (!player.capabilities.isCreativeMode)
            {
                stack.getTagCompound().setInteger(Constants.NBT.USES, --uses);
                if (uses <= 0)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);
        int uses = stack.getTagCompound().getInteger(Constants.NBT.USES);

        return 1.0 - ((double) uses / (double) 10);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced)
    {
        list.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.BloodMagic.inscriber.desc"))));
        super.addInformation(stack, player, list, advanced);
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 1; i < EnumRuneType.values().length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + EnumRuneType.values()[i].name()));
        return ret;
    }

    public EnumRuneType getType(ItemStack stack)
    {
        return EnumRuneType.values()[stack.getItemDamage()];
    }
}
