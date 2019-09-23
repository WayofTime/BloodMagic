package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.block.BlockRitualStone;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.ritual.EnumRuneType;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class ItemInscriptionTool extends ItemEnum.Variant<EnumRuneType> implements IBindable {

    public ItemInscriptionTool() {
        super(EnumRuneType.class, "scribe");

        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(ItemGroup creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        for (EnumRuneType runeType : types) {
            if (runeType == EnumRuneType.BLANK || !ConfigHandler.general.enableTierSixEvenThoughThereIsNoContent && runeType == EnumRuneType.DAWN)
                continue;

            ItemStack stack = new ItemStack(this, 1, runeType.ordinal());
            CompoundNBT tag = new CompoundNBT();
            tag.putInt(Constants.NBT.USES, 10);
            stack.setTagCompound(tag);
            list.add(stack);
        }
    }

    @Override
    public ActionResultType onItemUse(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockRitualStone && !((BlockRitualStone) state.getBlock()).isRuneType(world, pos, getItemType(stack))) {
            stack = NBTHelper.checkNBT(stack);
            int uses = stack.getTagCompound().getInteger(Constants.NBT.USES);

            world.setBlockState(pos, state.withProperty(((BlockRitualStone) state.getBlock()).getProperty(), getItemType(stack)));
            if (!player.capabilities.isCreativeMode) {
                stack.getTagCompound().setInteger(Constants.NBT.USES, --uses);
                if (uses <= 0)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
            }
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;
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
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.inscriber.desc"))));

        if (!stack.hasTagCompound())
            return;

        Binding binding = getBinding(stack);
        if (binding != null)
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentOwner", binding.getOwnerName()));
    }
}
