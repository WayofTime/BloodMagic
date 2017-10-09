package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.block.BlockRitualStone;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemInscriptionTool extends ItemBindableBase implements IVariantProvider {
    public ItemInscriptionTool() {
        super();

        setUnlocalizedName(BloodMagic.MODID + ".scribe.");
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + EnumRuneType.values()[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        for (int i = 1; i < EnumRuneType.values().length; i++) {
            ItemStack stack = NBTHelper.checkNBT(new ItemStack(this, 1, i));
            stack.getTagCompound().setInteger(Constants.NBT.USES, 10);
            list.add(stack);
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockRitualStone && !((BlockRitualStone) state.getBlock()).isRuneType(world, pos, getType(stack))) {
            stack = NBTHelper.checkNBT(stack);
            int uses = stack.getTagCompound().getInteger(Constants.NBT.USES);

            world.setBlockState(pos, state.withProperty(((BlockRitualStone) state.getBlock()).getProperty(), getType(stack)));
            if (!player.capabilities.isCreativeMode) {
                stack.getTagCompound().setInteger(Constants.NBT.USES, --uses);
                if (uses <= 0)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
            }
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTagCompound();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        int uses = stack.getTagCompound().getInteger(Constants.NBT.USES);

        return 1.0 - ((double) uses / (double) 10);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        int uses = stack.getTagCompound().getInteger(Constants.NBT.USES);

        return MathHelper.hsvToRGB(Math.max(0.0F, (float) (uses) / 10) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        list.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.inscriber.desc"))));
        super.addInformation(stack, world, list, flag);
    }

    @Override
    public void populateVariants(Int2ObjectMap<String> variants) {
        for (int i = 1; i < EnumRuneType.values().length; i++)
            variants.put(i, "type=" + EnumRuneType.values()[i].name());
    }

    public EnumRuneType getType(ItemStack stack) {
        return EnumRuneType.values()[stack.getItemDamage()];
    }
}
