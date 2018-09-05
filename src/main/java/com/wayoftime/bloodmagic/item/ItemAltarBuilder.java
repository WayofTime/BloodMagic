package com.wayoftime.bloodmagic.item;

import com.wayoftime.bloodmagic.block.BlockBloodAltar;
import com.wayoftime.bloodmagic.core.altar.AltarTier;
import com.wayoftime.bloodmagic.core.altar.AltarUtil;
import com.wayoftime.bloodmagic.core.altar.IAltarManipulator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAltarBuilder extends ItemMundane implements IAltarManipulator {

    public ItemAltarBuilder() {
        super("altar_builder");

        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote)
            return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));

        if (player.isSneaking()) {
            ItemStack held = player.getHeldItem(hand);
            AltarTier newTier = cycleTier(held);
            player.sendStatusMessage(new TextComponentTranslation("chat.bloodmagic:altar_builder_cycled", new TextComponentTranslation("enchantment.level." + newTier.getDisplayNumber())), true);
            return ActionResult.newResult(EnumActionResult.SUCCESS, held);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockBloodAltar) {
            AltarUtil.constructAltar(world, pos, getCurrentTier(player.getHeldItem(hand)));
            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.bloodmagic:tier", I18n.format("enchantment.level." + getCurrentTier(stack).getDisplayNumber())));
    }

    private AltarTier getCurrentTier(ItemStack stack) {
        if (!stack.hasTagCompound())
            return AltarTier.ONE;

        return AltarTier.VALUES[Math.min(AltarTier.VALUES.length - 1, Math.max(0, stack.getTagCompound().getInteger("altar_tier")))];
    }

    private AltarTier cycleTier(ItemStack stack) {
        AltarTier current = getCurrentTier(stack);
        int nextOrdinal = current.ordinal() + 1;
        if (nextOrdinal >= AltarTier.VALUES.length)
            nextOrdinal = 0;

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null)
            stack.setTagCompound(tag = new NBTTagCompound());

        tag.setInteger("altar_tier", nextOrdinal);
        return AltarTier.VALUES[nextOrdinal];
    }
}
